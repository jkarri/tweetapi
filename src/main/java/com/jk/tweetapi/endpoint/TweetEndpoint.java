package com.jk.tweetapi.endpoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jk.tweetapi.domain.Tweet;
import com.jk.tweetapi.domain.TweetFeed;

/**
 * Endpoint to publish, view tweets, follow users and see tweets from followed users.
 */
@RestController
@RequestMapping("/tweets")
public class TweetEndpoint {

    private Map<String, Set<Tweet>> userTweets = new HashMap<>();
    private Map<String, Set<String>> followingUsers = new HashMap<>();

    /**
     * Publish a tweet
     *
     * @param userId userId
     * @param tweet   tweet with text
     * @return {@link HttpStatus}
     */
    @RequestMapping(value = "/addTweet/{userId}", method = RequestMethod.POST)
    public ResponseEntity addTweet(@PathVariable("userId") String userId, @Valid @RequestBody Tweet tweet) {
        if (userTweets.get(userId) == null) {
            Set<Tweet> tweets = new TreeSet<>();
            tweets.add(tweet);
            userTweets.put(userId, tweets);
        } else {
            userTweets.get(userId).add(tweet);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}", produces = "application/json")
    public TweetFeed tweets(@PathVariable String userId) {
        TweetFeed tweetFeed = new TweetFeed();
        if (userTweets.get(userId) != null) {
            tweetFeed.setTweets(new ArrayList<>(userTweets.get(userId)));
        }
        return tweetFeed;
    }

    @RequestMapping(value = "/followUser/{userId}", method= RequestMethod.POST)
    public ResponseEntity followUser(@PathVariable("userId") String userId, @RequestBody String followingUser) {
        if (followingUsers.get(userId) == null) {
            Set<String> users = new HashSet<>();
            users.add(followingUser);
            followingUsers.put(userId, users);
        } else {
            followingUsers.get(userId).add(followingUser);
        }

        return new ResponseEntity(HttpStatus.CREATED);

    }

    @RequestMapping(method= RequestMethod.GET, value = "/feed/{userId}")
    public TweetFeed feed(@PathVariable String userId) {
        Set<String> following = followingUsers.get(userId);

        TweetFeed tweetFeed = new TweetFeed();
        if (following != null && !following.isEmpty()) {
            tweetFeed.setTweets(new ArrayList<>(new TreeSet<>(following.stream()
                            .map(user -> userTweets.get(user))
                            .flatMap(Collection::stream)
                            .collect(Collectors.toSet()))));
        }
        return tweetFeed;
    }

}
