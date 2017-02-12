package com.jk.tweetapi.endpoint;

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

    private final TweetService tweetService;

    public TweetEndpoint(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    /**
     * Publish a tweet
     *
     * @param userId userId
     * @param tweet   tweet with text
     * @return {@link ResponseEntity}
     */
    @RequestMapping(value = "/addTweet/{userId}", method = RequestMethod.POST)
    public ResponseEntity addTweet(@PathVariable("userId") String userId, @Valid @RequestBody Tweet tweet) {
        tweetService.addTweet(userId, tweet);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Get tweets created by a given user
     * @param userId user id
     * @return tweets created by the user
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}", produces = "application/json")
    public TweetFeed tweets(@PathVariable String userId) {
        return tweetService.tweetsByUser(userId);
    }

    /**
     * endpoint to follow a user
     * @param userId given user
     * @param followingUser user that is being followed
     * @return {@link ResponseEntity}
     */
    @RequestMapping(value = "/followUser/{userId}", method= RequestMethod.POST)
    public ResponseEntity followUser(@PathVariable("userId") String userId, @RequestBody String followingUser) {
        tweetService.followUser(userId, followingUser);
        return new ResponseEntity(HttpStatus.CREATED);

    }

    /**
     * fetches the tweets from the following users in reverse chronological order.
     * @param userId user id
     * @return all tweets from the following users in reverse chronological order.
     */
    @RequestMapping(method= RequestMethod.GET, value = "/feed/{userId}")
    public TweetFeed feed(@PathVariable String userId) {
        return tweetService.tweetFeed(userId);
    }

}
