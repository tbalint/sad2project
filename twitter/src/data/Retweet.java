package data;

import java.io.Serializable;

public class Retweet implements Serializable {
	public long tweetId;
	public User user;
	public Tweet tweet;
	
	public String originalUserScreenName;
	public User originalUser;
	public Tweet originalTweet;
}
