package com.jk.tweetapi.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.jk.tweetapi.domain.Tweet;
import com.jk.tweetapi.domain.TweetFeed;

/**
 * Make this class thread safe using {@link ConcurrentHashMap}.
 */
public class InMemoryTweetService implements TweetService {
    private ConcurrentMap<String, Set<Tweet>> userTweets = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Set<String>> userFollowers = new ConcurrentHashMap<>();

    public TweetFeed tweetsByUser(String userId) {
        TweetFeed tweetFeed = new TweetFeed();
        tweetFeed.setTweets(new ArrayList<>(userTweets.getOrDefault(userId, new TreeSet<>())));
        return tweetFeed;
    }

    public void addTweet(String userId, final Tweet tweet) {
        userTweets.computeIfAbsent(userId, k -> new TreeSet<>()).add(tweet);
        System.out.println(userTweets.size());
    }

    public void followUser(String userId, String followedUser) {
        userFollowers.computeIfAbsent(userId, k -> new HashSet<>()).add(followedUser);
    }

    public TweetFeed tweetFeed(String userId) {
        Set<String> followedUsers = userFollowers.computeIfAbsent(userId, k -> new HashSet<>());
        return feedFromFollowers(followedUsers);
    }

    private TweetFeed feedFromFollowers(Set<String> followedUsers) {
        TweetFeed tweetFeed = new TweetFeed();
        Set<Tweet> tweetSet = new TreeSet<>(followedUsers.stream().map(user -> userTweets.get(user))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet()));
        tweetFeed.setTweets(new ArrayList<>(tweetSet));
        return tweetFeed;
    }
}
