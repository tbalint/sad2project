package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import test.tweet;
import test.tweet_JENS;

public class Data implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public HashMap<Long, User> users = new HashMap<Long, User>();
	public HashMap<Long, Tweet> tweets = new HashMap<Long, Tweet>();
	public HashMap<Long, Retweet> retweets = new HashMap<Long, Retweet>();
	
	private static Data instance;
	private static String fileName = "twitter.ser";
	
	public static Data get() {
		return instance;
	}
	
	static {
		//loadFile(fileName);
	}
	
	public static void loadFile(String fileName) {
		File file = new File(fileName);
		
		if(file.exists()) {
			FileInputStream fis = null;
	        ObjectInputStream in = null;
	        try {
	            fis = new FileInputStream(file);
	            in = new ObjectInputStream(fis);

	            instance = (Data) in.readObject();
	            in.close();
	
	            System.out.println(" ------------------------------------------------");
	            System.out.println("  data loaded");
	            System.out.println("  " + instance.users.size() + " users");
	            System.out.println("  " + instance.tweets.size() + " tweets");
	            System.out.println(" ------------------------------------------------");
	    	}
	        catch(IOException ex) {
	            ex.printStackTrace();
	        } catch (ClassNotFoundException e) {
				// from in.readObject()
				e.printStackTrace();
				System.exit(0);
			}

		}
		else {
			instance = new Data();
		}
	}
	
	public synchronized void addTweet(Tweet tweet) {
		tweets.put(tweet.id, tweet);
		
		if(tweets.size() % 2000 == 0) {
			save();
		}
	}
	
	public Tweet getTweet(long id) {
		return tweets.get(id);
	}
	
	public synchronized void addUser(User user) {
		users.put(user.id, user);
	}
	
	public User getUser(long id) {
		return users.get(id);
	}
	
	public synchronized void addRetweet(Retweet retweet) {
		retweets.put(retweet.tweetId, retweet);
	}
	
	public Retweet getRetweet(long id) {
		return retweets.get(id);
	}
	
	public static void save() {
		System.gc();
		tweet_JENS.sleep(1);
		
		saveFile(fileName);
    }
	
	public static void saveFile(String fileName) {
		File file = new File(fileName);
		
		FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);

            out.writeObject(instance);
            out.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        System.out.println(" ------------------------------------------------");
        System.out.println("  data saved");
        System.out.println("  " + instance.users.size() + " users");
        System.out.println("  " + instance.tweets.size() + " tweets");
        System.out.println(" ------------------------------------------------");
	}
	
	public static void reduceInput(String input) {
		// load
		loadFile(input + ".ser");
		
		// find removable objects
		List<Long> usersToBeRemoved = new ArrayList<Long>();
		List<Long> tweetsToBeRemoved = new ArrayList<Long>();
		List<Long> retweetsToBeRemoved = new ArrayList<Long>();
		
		for(long ri : instance.retweets.keySet()) {
			Retweet retweet = instance.retweets.get(ri);
			
			User user = retweet.user;
			User originalUser = retweet.originalUser;
			Tweet originalTweet = retweet.originalTweet;
			
			boolean removeRetweet = false;
			
			// check for remove
			boolean oneRetweet = originalTweet.retweets.size() == 1;			// only 1 retweeter
			boolean userZeroInputOneOutput = user.tweets.size() == 1;			// if 1 retweet, then 0 tweets
			boolean ouserOneInputZeroOutput = originalUser.tweets.size() == 1;	// if 1 tweet, then 0 retweets
			
			if(oneRetweet && userZeroInputOneOutput && ouserOneInputZeroOutput) {
				removeRetweet = true;
			}
			else {
				removeRetweet = checkUsers(user, originalUser);
				
				// if true, try to contradict it
				if(removeRetweet) {
					removeRetweet = checkUsers(originalUser, user);
				}
			}
			
			// register, that retweet should be removed
			if(removeRetweet) {
				usersToBeRemoved.add(user.id);
				usersToBeRemoved.add(originalUser.id);
				
				tweetsToBeRemoved.add(originalTweet.id);
				tweetsToBeRemoved.add(retweet.tweetId);
				
				retweetsToBeRemoved.add(retweet.tweetId);
			}
		}
		
		// remove objects
		for(long ri : retweetsToBeRemoved) {
			instance.retweets.remove(ri);
		}
		for(long ti : tweetsToBeRemoved) {
			instance.tweets.remove(ti);
		}
		for(long ui : usersToBeRemoved) {
			instance.users.remove(ui);
		}
		
		// save
		saveFile(input + "-reduced" + ".ser");
	}
	
	public static boolean checkUsers(User user, User oppositeUser) {
		boolean removeRetweet = true;
		
		HashMap<Long, Tweet> tweets = user.tweets;
		
		for(long ti : tweets.keySet()) {
			Tweet tweet = tweets.get(ti);
			
			if(tweet.isRetweet) {
				boolean isNewUser = tweet.originalTweet.user != oppositeUser;
				
				if(isNewUser) {
					removeRetweet = false;
					break;
				}
			}
			// is a normal tweet
			else {
				HashMap<Long, Tweet> retweets = tweet.retweets;
				
				for(long ri2 : retweets.keySet()) {
					Tweet tweet2 = retweets.get(ri2);
					boolean isNewUser = tweet2.user != oppositeUser;
					
					if(isNewUser) {
						removeRetweet = false;
						break;
					}
				}
				
				if(removeRetweet == false)
					break;
			}
		}
		
		return removeRetweet;
	}
	
	public static void main(String[] args) {
		reduceInput("twitter20MB");
	}
}
