package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import data.Data;
import data.Retweet;
import data.Tweet;

import twitter4j.*;
import twitter4j.api.FriendsFollowersResources;
import twitter4j.conf.ConfigurationBuilder;

public abstract class tweet_JENS{

	public static boolean showDetails = false;
	public static boolean getNewUsers = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		readStatus();
		
		//readFollowers();
	}
	
	public static void readStatus() {
		StatusListener listener = new StatusListener(){
			public void onStatus(Status status) {
				//String userlang = status.getUser().getLang();
				//if(userlang.equals("da") || userlang.equals("en")) {
				if(status.isRetweet()) {	
					GeoLocation geolo = status.getGeoLocation();
					String description = null;
					if(geolo != null) //{
						description = GeoLocator.getNearestLocation(geolo.getLatitude(), geolo.getLongitude());
						
					System.out.println("");
					
					//if(description != null) {
					data.User user = getUser(status);
					
					Tweet tweet = getTweet(status, description);
					
					tweet.user = user;
					user.addTweet(tweet);
					
					if(tweet.isRetweet) {
						Status originalStatus = status.getRetweetedStatus();
						
						data.User originalUser = getUser(originalStatus);
						
						Tweet originalTweet = getTweet(originalStatus, description);
						originalTweet.user = originalUser;
						originalTweet.addRetweet(tweet);
						
						originalUser.addTweet(originalTweet);
						
						tweet.originalTweet = originalTweet;
						
						Retweet retweet = new Retweet();
						retweet.tweet = tweet;
						retweet.user = user;
						retweet.tweetId = tweet.id;
						
						retweet.originalUser = originalUser;
						retweet.originalTweet = originalTweet;
						
						Data.get().addRetweet(retweet);
						
						System.out.println(Data.get().retweets.size() + " RETWEETS");
					}
					
					System.out.println(Data.get().tweets.size() + " tweets");
						//}
					//}
				}
			}
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	        }
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}
	    };
	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    //cb.setUser(readParameter("Enter your twitter account name"));
	    //cb.setPassword(readParameter("Enter your password"));
	    cb.setUser(TwitterAccount.user);
	    cb.setPassword(TwitterAccount.password);
	    
	    TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	    
	    twitterStream.addListener(listener);
	    twitterStream.sample();
	    
	    //FilterQuery fq = new FilterQuery();
	    //double[][] locations = new double[][] { new double[]{8, 54}, new double[]{13,58}};
	    //fq.locations(locations);
	    
	    //twitterStream.filter(fq);		
	}
	
	public static data.User getUser(Status status) {
		long userId = status.getUser().getId();
		
		data.User user = Data.get().getUser(userId);
		if(user == null) {
			user = new data.User();
			
			user.id = status.getUser().getId();
			if(showDetails)
				System.out.println("User Id : " + user.id);
			
			user.name = status.getUser().getName();
			if(showDetails)
				System.out.println("User Name : " + user.name);
			
			user.lang = status.getUser().getLang();
			if(showDetails)
				System.out.println("User Lang : " + user.lang);
			
			user.location = status.getUser().getLocation();
			if(showDetails)
				System.out.println("User Location : " + user.location);
			
			// created at
			user.createdAt = status.getUser().getCreatedAt();
			if(showDetails)
				System.out.println("User Created at : " + user.createdAt);
			
			// description
			user.description = status.getUser().getDescription();
			if(showDetails)
				System.out.println("User Description : " + user.description);
			
			// screen name
			user.screenName = status.getUser().getScreenName();
			if(showDetails)
				System.out.println("User Screen name : " + user.screenName);
			
			// statuses count
			user.statusesCount = status.getUser().getStatusesCount();
			if(showDetails)
				System.out.println("User Statuses count : " + user.statusesCount);
			
			// followers count
			user.followersCount = status.getUser().getFollowersCount();
			if(showDetails)
				System.out.println("Followers count : " + user.followersCount);
			
			Data.get().addUser(user);
			
			System.out.println(Data.get().users.size() + " users");
		}
		
		return user;
	}
	
	public static Tweet getTweet(Status status, String description) {
		Tweet tweet = Data.get().getTweet(status.getId());
		
		if(tweet == null) {
			tweet = new Tweet();
			
			// id
			tweet.id = status.getId();
			if(showDetails)
				System.out.println("Id : " + tweet.id);
			
			// text
			tweet.text = status.getText();
			if(showDetails)
				System.out.println("Text : " + tweet.text);
			
			// created at
			tweet.createdAt = status.getCreatedAt();
			if(showDetails)
				System.out.println("Created at : " + tweet.createdAt);
			
			// retweet count
			tweet.retweetCount = status.getRetweetCount();
			if(showDetails)
				System.out.println("Retweet count : " + tweet.retweetCount);
			
			// is retweet
			tweet.isRetweet = status.isRetweet();
			if(showDetails)
				System.out.println("Is Retweet : " + tweet.isRetweet);
			
			// source
			tweet.source = status.getSource();
			if(showDetails)
				System.out.println("Source : " + tweet.source);
			
			// in reply to screen name
			tweet.inReplyToScreenName = status.getInReplyToScreenName();
			if(showDetails)
				System.out.println("In reply to screen name : " + tweet.inReplyToScreenName);
			
			// location
			tweet.location = status.getGeoLocation();
			tweet.locationDescription = description;
			if(showDetails)
				System.out.println("Geo Location : " + tweet.location.getLatitude() + ", " + tweet.location.getLongitude() + " (" + description + ")");
			
			Data.get().addTweet(tweet);
		}
		
		return tweet;
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String readParameter(String paramName){
		System.out.print(paramName+": ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String param = null;
		try {
			param = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your name!");
		
		}
		return param;
	}
}
