package com.jk.tweetapi.endpoint;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jk.tweetapi.domain.Tweet;

/**
 * Endpoint to publish, view tweets, follow users and see tweets from followed users.
 */
@RestController
@RequestMapping("/tweets")
public class TweetEndpoint {

    /**
     * Publish a tweet
     * @param userId userId
     * @param text tweet text
     * @return {@link HttpStatus}
     */
    @RequestMapping(value = "/addTweet/{userId}", method= RequestMethod.POST)
    public ResponseEntity addTweet(@PathVariable("userId") String userId, @RequestBody Tweet text) {
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
