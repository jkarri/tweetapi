package com.jk.tweetapi.endpoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import com.jk.tweetapi.service.TweetService;

/**
 * Endpoint to publish, view tweets, follow users and see tweets from followed users.
 */
@RestController
@RequestMapping("/tweets")
public class TweetEndpoint {

    private Map<String, Set<Tweet>> userTweets = new HashMap<>();
    private Map<String, Set<String>> followingUsers = new HashMap<>();

    private final TweetService tweetService;

    public TweetEndpoint(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    /**
     * Publish a tweet
     *
     * @param userId userId
     * @param tweet   tweet with text
     * @return {@link HttpStatus}
     */
    @RequestMapping(value = "/addTweet/{userId}", method = RequestMethod.POST)
    public ResponseEntity addTweet(@PathVariable("userId") String userId, @Valid @RequestBody Tweet tweet) {
        tweetService.addTweet(userId, tweet);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}", produces = "application/json")
    public TweetFeed tweets(@PathVariable String userId) {
        return tweetService.tweetsByUser(userId);
    }

    @RequestMapping(value = "/followUser/{userId}", method= RequestMethod.POST)
    public ResponseEntity followUser(@PathVariable("userId") String userId, @RequestBody String followingUser) {
        tweetService.followUser(userId, followingUser);
        return new ResponseEntity(HttpStatus.CREATED);

    }

    @RequestMapping(method= RequestMethod.GET, value = "/feed/{userId}")
    public TweetFeed feed(@PathVariable String userId) {
        return tweetService.tweetFeed(userId);
    }

}
