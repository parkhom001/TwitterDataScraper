package cs172_phase1;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.management.*;
import twitter4j.api.*;
import twitter4j.json.*;
import twitter4j.auth.*;
import twitter4j.TwitterStream;
import twitter4j.StatusListener;



public class phase1 {
	  public static void main(String[] args) throws TwitterException {
	    	ConfigurationBuilder cb = new ConfigurationBuilder();
	    	
	    	cb.setDebugEnabled(true)
	    		.setOAuthConsumerKey("Qsfx4W45GV4ntCUOwhp47CWRh")
	    		.setOAuthConsumerSecret("fetMzlsvMQsVu3I9k3O4yb1fbgDFy9GoXMyTcNnaCiHeQhMVMw")
	    		.setOAuthAccessToken("982370303991889920-rqwZSEhHG3ln8YkoRacp0IP2w4OAOAV")
	    		.setOAuthAccessTokenSecret("Z8TOwjlN3tl30CnMhI2yHanCJirdJn0eOwdweHhA6M1C4");
	    	
	    	    	
	        StatusListener listener = new StatusListener() {
	            public void onException(Exception arg0) {
	                // TODO Auto-generated method stub
	            }
	            
	            public void onDeletionNotice(StatusDeletionNotice arg0) {
	                // TODO Auto-generated method stub
	            }
	            public void onScrubGeo(long arg0, long arg1) {
	                // TODO Auto-generated method stub
	            }
	            
	            public void onStatus(Status status) {
	                User user = status.getUser();
	                GeoLocation gl = status.getGeoLocation();
	                if (gl != null)
	                {
	                	System.out.println("Long: " + gl.getLongitude());
	                	System.out.println("lat: " + gl.getLatitude());
	                }
	                // gets Username
	                String username = status.getUser().getScreenName();
	                System.out.println(username);
	                String profileLocation = user.getLocation();
	                System.out.println(profileLocation);
	                long tweetId = status.getId(); 
	                System.out.println(tweetId);
	                String content = status.getText();
	                System.out.println(content +"\n");
	            }
	            
	            public void onTrackLimitationNotice(int arg0) {
	                // TODO Auto-generated method stub
	            }
	            public void onStallWarning(StallWarning arg0) {
	            }
	        };
	        
	    	
	        TwitterStream ts = new TwitterStreamFactory(cb.build()).getInstance();
	        ts.addListener(listener);
	        FilterQuery tf = new FilterQuery();
	        tf.locations(new double[][]{new double[]{-118.417616,34.029797}, new double[]{-117.301561,34.106632}});
	    	ts.filter(tf);
	    	
	    	  
	    }
}
