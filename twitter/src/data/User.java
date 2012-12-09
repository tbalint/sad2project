package data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class User implements Serializable {
	public long id;
	public String name;
	public String lang;
	public String location;
	public Date createdAt;
	public String description;
	public String screenName;
	
	public int statusesCount;
	public HashMap<Long, Tweet> tweets = new HashMap<Long, Tweet>();
	
	// users, who follow this user
	public int followersCount;
	public HashMap<Long, User> followers = new HashMap<Long, User>();
	
	// users, that this user is following
	public HashMap<Long, User> followings = new HashMap<Long, User>();
	
	public void addTweet(Tweet tweet) {
		tweets.put(tweet.id, tweet);
	}
	
	public void addFollower(User user) {
		followers.put(user.id, user);
	}

	public void addFollowing(User user) {
		followings.put(user.id, user);
	}
}
