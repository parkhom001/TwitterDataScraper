package cs172_phase1;
import java.util.*;

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
import twitter4j.TwitterStream;
import twitter4j.StatusListener;



public class phase1 {
	public static boolean fileOpen = false;
	public static String directory = "";
	public static int fileCount = 0;
	public static FileWriter fileWriter = null;
	public static BufferedWriter bufferedWriter = null;
	public static int tempCount = 0;
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy HH:mm:ss");
	
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
	            public void onTrackLimitationNotice(int arg0) {
	                // TODO Auto-generated method stub
	            }
	            public void onStallWarning(StallWarning arg0) {
	            	// TODO 
	            }
	            
	            public void onStatus(Status status) {
	            	// we want to store in this format
	            	// username: geolocation: profilelocation: tweet: timestamp: url: urltitle:
	            	
	            	if (tempCount >= 10) {
	            		try {
							bufferedWriter.close();
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
	            		
	            		bufferedWriter = new BufferedWriter(fileWriter);
	            		System.out.println("made buffer writer");
	            		
	            	}
	            	
	            	User user = status.getUser();
	            	String userName = user.getScreenName();
	            	GeoLocation geoLocation = status.getGeoLocation();
	            	double longitude = geoLocation.getLongitude();
	            	double latitude = geoLocation.getLatitude();
	            	String profileLocation = user.getLocation();
	            	String tweetText = status.getText();
	            	tweetText = tweetText.replaceAll("\n", " ");
	            	Date timeStamp = status.getCreatedAt();
	            	String dateAndTime = dateFormat.format(timeStamp);
	            	URLEntity[] urls = status.getURLEntities();
	            	HashtagEntity[] hashtags = status.getHashtagEntities();
	            	
	            	String whatToWrite = "USERNAME: " + userName + " " +
	            						 "LONGITUDE: " + longitude + " " + 
	            						 "LATITUDE: " + latitude + " " +
	            						 "LOCATION: " + profileLocation + " " +
	            						 "TWEET: " + tweetText + " " +
	            						 "TIMESTAMP: " + dateAndTime + " " +
	            						 "URLS: ";
	            	
	            	
	            	for (int i = 0; i < urls.length; i++) {
	            		if (urls[i].getExpandedURL() == null) {
	            			whatToWrite += urls[i].getURL() + " ";
	            		}
	            		else {
	            			whatToWrite += urls[i].getExpandedURL() + " ";
	            		}
	            	}
	            	
	            	
	            	whatToWrite += "URLSTITLE: ";
	            	
	            	for (int i = 0; i < urls.length; i++) {
	            		if (urls[i].getExpandedURL() == null) {
	            			try {
								whatToWrite += Jsoup.connect(urls[i].getURL()).get().title() + " ";
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	            		}
	            		else {
	            			try {
								whatToWrite += Jsoup.connect(urls[i].getExpandedURL()).get().title() + " ";
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	            		}
	            	}
	            	
	            	whatToWrite += "HASHTAGS: ";
	            	
	            	for (int i = 0; i < hashtags.length; i++) {
	            		whatToWrite += hashtags[i].getText() + " ";
	            	}
	            		            	
	            	whatToWrite += "\n";
	            	
	            	
	            	try {
						bufferedWriter.write(whatToWrite);
						System.out.println("wrote to buffer writer" +  tempCount);
						tempCount += 1;
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            }
	        };
	        
	    	
	        TwitterStream ts = new TwitterStreamFactory(cb.build()).getInstance();
	        ts.addListener(listener);
	        FilterQuery tf = new FilterQuery();
	        tf.locations(new double[][]{new double[]{-118.417616,34.029797}, new double[]{-117.301561,34.106632}});
	    	ts.filter(tf);
	    	
	    	
	    	  
	    }
}