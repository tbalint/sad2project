package data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import twitter4j.GeoLocation;

public class Tweet implements Serializable {
	public User user;
	public long id;
	public String text;
	public Date createdAt;
	
	public long retweetCount;
	public HashMap<Long, Tweet> retweets = new HashMap<Long, Tweet>();
	
	public boolean isRetweet;
	public Tweet originalTweet;
	
	public String source;
	public String inReplyToScreenName;
	
	public GeoLocation location;
	public String locationDescription;
	
	public void addRetweet(Tweet tweet) {
		retweets.put(tweet.id, tweet);
	}
}
