package cs172_jesus;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import org.jsoup.Jsoup;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import twitter4j.*;
import twitter4j.conf.*;
import twitter4j.management.*;
import twitter4j.api.*;
import twitter4j.json.*;
import twitter4j.auth.*;
import org.json.simple.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


class MultiThreading extends Thread { //implements Runnable {
	   private Status status;
	   private User user;
	   private GeoLocation geoLocation;
	   private SimpleDateFormat dateFormat;

	   MultiThreading(Status s, User u, GeoLocation g) {
		   status = s;
		   user = u;
		   geoLocation = g;
		   dateFormat = new SimpleDateFormat("MMMM dd yyyy HH:mm:ss");
	   }
	   
	   @SuppressWarnings("unchecked")
	public void run() {
       	String tweetText = status.getText();
       	tweetText = tweetText.replaceAll("\n", " ");
       	URLEntity[] urls = status.getURLEntities();
       	HashtagEntity[] hashtags = status.getHashtagEntities();
       	
       	JSONObject tweetJson = new JSONObject();
       	JSONArray urlsJson = new JSONArray();
       	JSONArray urlTitlesJson = new JSONArray();
       	JSONArray hashtagsJson = new JSONArray();
       	
       	tweetJson.put("USERNAME", user.getScreenName());
    	
       	if (user.isVerified())
       	{
       		tweetJson.put("VERIFIED", true);
       	}
       	else
       	{
       		tweetJson.put("VERIFIED", false);
       	}
       	
       	if(geoLocation != null) {
       		
       		tweetJson.put("LONGITUDE", geoLocation.getLongitude());
       		tweetJson.put("LATITUDE", geoLocation.getLatitude());
       		
       	}
       	else {
       		tweetJson.put("LONGITUDE", null);
       		tweetJson.put("LATITUDE", null);
       	}
       	
       	tweetJson.put("LOCATION", user.getLocation());
       	tweetJson.put("TWEET", tweetText);
       	tweetJson.put("FAVORITECOUNT", status.getFavoriteCount());
       	tweetJson.put("RETWEETCOUNT", status.getRetweetCount());
       	tweetJson.put("TIMESTAMP", dateFormat.format(status.getCreatedAt()));
       	
       	
       	for (int i = 0; i < hashtags.length; i++) {
     		hashtagsJson.add(hashtags[i].getText());
     	}
       	
       	tweetJson.put("HASHTAGS", hashtagsJson);
       	
    	for (int i = 0; i < urls.length; i++) {
     		if (urls[i].getExpandedURL() == null) {
     			urlsJson.add(urls[i].getURL());
     		}
     		else {
     			urlsJson.add(urls[i].getExpandedURL());
     		}
     	}
    	
    	tweetJson.put("URLS", urlsJson);
    	
    	for (int i = 0; i < urls.length; i++) {
     		if (urls[i].getExpandedURL() == null) {
     			try {
     					urlTitlesJson.add(Jsoup.connect(urls[i].getURL()).get().title());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
     		}
     		else {
     			try {
     					urlTitlesJson.add(Jsoup.connect(urls[i].getExpandedURL()).get().title());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
     		}
     	}
    	
    	tweetJson.put("URLSTITLE", urlTitlesJson);
    	tweetJson.put("LANGUAGE", user.getLang());
    	tweetJson.put("FOLLOWERCOUNT", user.getFollowersCount());
    	tweetJson.put("FOLLOWINGCOUNT", user.getFriendsCount());


     	
     	try {
     		phase1.lock.lock();
				phase1.bufferedWriter.write(tweetJson.toJSONString());
				phase1.bufferedWriter.write("\n");

				System.out.println("wrote to buffer writer" +  phase1.tempCount);

				phase1.tempCount += 1;
				phase1.lock.unlock();
			} catch (IOException e) {;
				phase1.lock.unlock();

			}
	      System.out.println("Thread exiting.");
	   }
	   
	}

public class phase1 {
	public static boolean fileOpen = false;
	public static String directory = "";
	public static int fileCount = 0;
	public static FileWriter fileWriter = null;
	public static BufferedWriter bufferedWriter = null;
	public static int tempCount = 0;
	public static ReentrantLock lock = new ReentrantLock();

	
	  public static void main(String[] args) throws TwitterException {

	    	ConfigurationBuilder cb = new ConfigurationBuilder();
	    	cb.setDebugEnabled(true)
	    	.setOAuthConsumerKey("")
	    	.setOAuthConsumerSecret("")
	    	.setOAuthAccessToken("")
	    	.setOAuthAccessTokenSecret("");

	        final TwitterStream ts = new TwitterStreamFactory(cb.build()).getInstance();

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

	            public void onTrackLimitationNotice(int arg0) {
	                // TODO Auto-generated method stub
	            }

	            public void onStallWarning(StallWarning arg0) {
	            	// TODO 
	            }


	            public void onStatus(Status status) {

	            	if (fileCount > 500) //500 files for approximately 5gb
	            	{	            		
	            		ts.cleanUp();
	            		ts.shutdown();	          
	            	}
	            	
	            	if (tempCount >= 25000) {  //25000 tweets for approximately 10mb files
	            		try {
	            			lock.lock();
							bufferedWriter.close();
							lock.unlock();
							System.out.println("closed buffer writer");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		fileOpen = false;
	            		tempCount = 0;
	            	}
	            	
	            	if (!fileOpen) {
	            		fileCount += 1;
	            		directory = "";
	            		directory += ("./tweets" + fileCount + ".txt");
	            		File file = new File(directory);

	            		if(!file.exists()) {
	            			try {
								file.createNewFile();
								fileOpen = true;
								System.out.println("made file");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	            		}

	            		try {
							fileWriter = new FileWriter(file);
							fileOpen = true;
							System.out.println("made file writer");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

	            		lock.lock();
	            		bufferedWriter = new BufferedWriter(fileWriter);
	            		System.out.println("made buffer writer");
	            		lock.unlock();		
	            	}

	            	if(tempCount % 5 == 0) {
	            		try {
							bufferedWriter.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	         		
	            	}

	            	if(status.getUser().getLang().equals("en")) {
	            		MultiThreading M = new MultiThreading(status, status.getUser(), status.getGeoLocation());
	            		M.start();
	            	}
	            }
	        };	        
	        
	        
	        ts.addListener(listener);
	        ts.sample();
    	  

	    }
}



