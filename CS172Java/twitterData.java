package CS172Java;
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


public class twitterData {
	
    public static void main(String[] args) throws TwitterException {
    	ConfigurationBuilder cb = new ConfigurationBuilder();
    	
    	cb.setDebugEnabled(true)
    		.setOAuthConsumerKey("Qsfx4W45GV4ntCUOwhp47CWRh")
    		.setOAuthConsumerSecret("fetMzlsvMQsVu3I9k3O4yb1fbgDFy9GoXMyTcNnaCiHeQhMVMw")
    		.setOAuthAccessToken("982370303991889920-rqwZSEhHG3ln8YkoRacp0IP2w4OAOAV")
    		.setOAuthAccessTokenSecret("Z8TOwjlN3tl30CnMhI2yHanCJirdJn0eOwdweHhA6M1C4");
    	
    	TwitterStream ts = new TwitterStreamFactory().getInstance();    	
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
    	TwitterFactory tf = new TwitterFactory(cb.build());
    	twitter4j.Twitter twitter = tf.getInstance();
    	
//    	List<Status> status = twitter.getHomeTimeline();
    	 
    	ts.addListener(listener);
    	
    	  
    }
    
    

}