package com.jk.tweetapi.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.jk.tweetapi.domain.Tweet;
import com.jk.tweetapi.domain.TweetFeed;

/**
 * Make this class thread safe using {@link ReentrantReadWriteLock}s.
 * Write locks protect writes to the map and read locks to protect reads.
 */
public class InMemoryTweetService implements TweetService {
    private Map<String, Set<Tweet>> userTweets = new HashMap<>();
    private Map<String, Set<String>> userFollowers = new HashMap<>();

    private ReadWriteLock tweetsLock = new ReentrantReadWriteLock();
    private ReadWriteLock followersLock = new ReentrantReadWriteLock();

    public TweetFeed tweetsByUser(String userId) {
        tweetsLock.readLock().lock();
        try {
            TweetFeed tweetFeed = new TweetFeed();
            if (userTweets.get(userId) != null) {
                tweetFeed.setTweets(new ArrayList<>(userTweets.get(userId)));
            }
            return tweetFeed;
        } finally {
            tweetsLock.readLock().unlock();
        }
    }

    public void addTweet(String userId, Tweet tweet) {
        tweetsLock.writeLock().lock();
        try {
            if (userTweets.get(userId) == null) {
                Set<Tweet> tweets = new TreeSet<>();
                tweets.add(tweet);
                userTweets.put(userId, tweets);
            } else {
                userTweets.get(userId).add(tweet);
            }
        } finally {
            tweetsLock.writeLock().unlock();
        }
    }

    public void followUser(String userId, String followedUser) {
        followersLock.writeLock().lock();
        try {
            if (userFollowers.get(userId) == null) {
                Set<String> followedUsers = new HashSet<>();
                followedUsers.add(followedUser);
                userFollowers.put(userId, followedUsers);
            } else {
                userFollowers.get(userId).add(followedUser);
            }
        } finally {
            followersLock.writeLock().unlock();
        }
    }

    public TweetFeed tweetFeed(String userId) {
        followersLock.readLock().lock();

        Set<String> followedUsers;
        try {
            followedUsers = userFollowers.get(userId);
        } finally {
            followersLock.readLock().unlock();
        }

        return feedFromFollowers(followedUsers);
    }

    private TweetFeed feedFromFollowers(Set<String> followedUsers) {
        tweetsLock.readLock().lock();
        try {
            Set<Tweet> tweetSet = new TreeSet<>(followedUsers.stream().map(user -> userTweets.get(user))
                            .flatMap(Collection::stream)
                            .collect(Collectors.toSet()));

            TweetFeed tweetFeed = new TweetFeed();
            tweetFeed.setTweets(new ArrayList<>(tweetSet));

            return tweetFeed;
        } finally {
            tweetsLock.readLock().unlock();
        }
    }
}
