package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public abstract class tweet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StatusListener listener = new StatusListener(){
	        public void onStatus(Status status) {
	        	if (status.isRetweet()){
	            System.out.println(status.getUser().getScreenName() + " : " + status.getText());
	            System.out.println(status.getRetweetCount()+" : " +status.isRetweet());
	            System.out.println(status.getPlace());
	            System.out.println(status.getGeoLocation());
	            System.out.println(status.getSource());
	            System.out.println(status.getUser().getFollowersCount());
	            System.out.println(status.getInReplyToScreenName());
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
	    cb.setUser(readParameter("Enter your twitter account name"));
	    cb.setPassword(readParameter("Enter your password"));

	    TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	    twitterStream.addListener(listener);
	    /*FilterQuery fq=new FilterQuery();
	    double[][] d={{-74,40,-73,41}};
	    fq.locations(d);
	    try {
			twitterStream.getFilterStream(fq);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    //twitterStream.filter(fq);
	    twitterStream.sample();
	    
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
