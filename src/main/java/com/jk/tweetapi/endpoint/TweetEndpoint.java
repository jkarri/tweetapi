package com.jk.tweetapi.endpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, List<Tweet>> userTweets = new HashMap<>();

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
            List<Tweet> tweets = new ArrayList<>();
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
        tweetFeed.setTweets(userTweets.get(userId));
        return tweetFeed;
    }

}
