package com.jk.tweetapi.service;

import com.jk.tweetapi.domain.Tweet;
import com.jk.tweetapi.domain.TweetFeed;

/**
 * Service that defines the tweet API that endpoints can use.
 */
public interface TweetService {

    /**
     * Fetch the tweets by the given user.
     * Tweets are fetched in reverse chronological order
     * @param userId given user id
     * @return tweet feed that encapsulates the tweets posted by the user
     */
    TweetFeed tweetsByUser(String userId);

    /**
     * Add a tweet from a user, the user is created automatically when does not exist.
     * @param userId user id
     * @param text tweet text
     */
    void addTweet(String userId, Tweet text);

    /**
     * A user can follow another user.
     * @param userId given user
     * @param followedUser user that is followed by the given user
     */
    void followUser(String userId, String followedUser);

    /**
     * Provider the feed for the given user. Feed consists of tweets from all the followed users.
     * @param userId user id
     * @return tweet feed from the followed users, tweets are ordered in reverse chronological order.
     */
    TweetFeed tweetFeed(String userId);
}
