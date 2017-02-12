package com.jk.tweetapi.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.jk.tweetapi.domain.Tweet;
import com.jk.tweetapi.domain.TweetFeed;

public class InMemoryTweetService implements TweetService {
    private Map<String, Set<Tweet>> userTweets = new HashMap<>();
    private Map<String, Set<String>> userFollowers = new HashMap<>();

    public TweetFeed tweetsByUser(String userId) {
        TweetFeed tweetFeed = new TweetFeed();
        if (userTweets.get(userId) != null) {
            tweetFeed.setTweets(new ArrayList<>(userTweets.get(userId)));
        }
        return tweetFeed;
    }

    public void addTweet(String userId, Tweet tweet) {
        if (userTweets.get(userId) == null) {
            Set<Tweet> tweets = new TreeSet<>();
            tweets.add(tweet);
            userTweets.put(userId, tweets);
        } else {
            userTweets.get(userId).add(tweet);
        }
    }

    public void followUser(String userId, String followedUser) {
        if (userFollowers.get(userId) == null) {
            Set<String> followedUsers = new HashSet<>();
            followedUsers.add(followedUser);
            userFollowers.put(userId, followedUsers);
        } else {
            userFollowers.get(userId).add(followedUser);
        }
    }

    public TweetFeed tweetFeed(String userId) {
        Set<String> followedUsers = userFollowers.get(userId);

        return feedFromFollowers(followedUsers);
    }

    private TweetFeed feedFromFollowers(Set<String> followedUsers) {
        Set<Tweet> tweetSet = new TreeSet<>(followedUsers.stream().map(user -> userTweets.get(user))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet()));

        TweetFeed tweetFeed = new TweetFeed();
        tweetFeed.setTweets(new ArrayList<>(tweetSet));

        return tweetFeed;
    }
}
