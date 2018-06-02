package edu.ucr.cs.nle020.lucenesearcher;

public class Article {

    public String username;
    public String timestamp;
    public String tweet;
    public String hashtags;
    public String urlstitle;
    public String longitude;
    public String latitude;

    public Article(){}

    public Article(String username, String timestamp, String tweet, String hashtags, String urlstitle, String longitude, String latitude) {
        this.username = username;
        this.timestamp = timestamp;
        this.tweet = tweet;
        this.hashtags = hashtags;
        this.urlstitle = urlstitle;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getUsername() {
        return username;
    }

    public void setId(String username) {
        this.username = username;
    }
    
    public String getTimestamp() {
    	return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
    	this.timestamp = timestamp;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }
    
    public String getUrlstitle() {
    	return urlstitle;
    }
    
    public void setUrlstitle(String urlstitle) {
    	this.urlstitle = urlstitle;
    }
    
    public String getLongitude() {
    	return longitude;
    }
    
    public void setLongitude(String longitude) {
    	this.longitude = longitude;
    }
    
    public String getLatitude() {
    	return latitude;
    }
    
    public void setLatitude(String latitude) {
    	this.latitude = latitude;
    }

    @Override
    public String toString() {
        return String.format("Article[username=%s, tweet=%s, hashtags=%s, urlstitle=%s]", username, tweet, hashtags, urlstitle);
    }
}
