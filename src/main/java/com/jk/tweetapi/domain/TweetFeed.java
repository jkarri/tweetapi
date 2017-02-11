package com.jk.tweetapi.domain;

import java.util.List;

/**
 * Wrapper to hold the tweets.
 */
public class TweetFeed {
    private List<Tweet> tweets;

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

}
