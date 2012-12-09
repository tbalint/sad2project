package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import test.tweet;
import test.tweet_JENS;

@SuppressWarnings("serial")
public class Data implements Serializable {
	public HashMap<Long, User> users = new HashMap<Long, User>();
	public HashMap<Long, Tweet> tweets = new HashMap<Long, Tweet>();
	public HashMap<Long, Retweet> retweets = new HashMap<Long, Retweet>();
	
	private static Data instance;
	private static String fileName = "src\\twitter.ser";
	
	public static Data get() {
		return instance;
	}
	
	static {
		File file = new File(fileName);
		
		if(file.exists()) {
			FileInputStream fis = null;
	        ObjectInputStream in = null;
	        try {
	            fis = new FileInputStream(file);
	            in = new ObjectInputStream(fis);

	            instance = (Data) in.readObject();
	            in.close();
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
		
		if(tweets.size() % 1000 == 0) {
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
}
