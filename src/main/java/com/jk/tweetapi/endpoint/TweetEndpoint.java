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

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
    @ApiOperation(value = "allows to post a tweet", nickname = "post a tweet")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "userId", value = "user id", required = true, dataType = "string", paramType = "path", defaultValue="Jana"),
                    @ApiImplicitParam(name = "tweet", value = "tweet text", required = true, paramType = "body", defaultValue="{\n"
                                    + "\"text\" : \"sample tweet\"\n"
                                    + "}")
    })
    @ApiResponses(value = {
                    @ApiResponse(code = 201, message = "Success, tweet posted", response = ResponseEntity.class),
                    @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "allows to get tweets by the given user in reverse chronological order", nickname = "get tweets by the given user")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "userId", value = "user id", required = true, dataType = "string", paramType = "path", defaultValue = "Jana")
    })
    @ApiResponses(value = {
                    @ApiResponse(code = 200, message = "Success", response = TweetFeed.class),
                    @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "allows for a given user to follow another user", nickname = "follow a user")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "userId", value = "user id", required = true, dataType = "string", paramType = "path", defaultValue="Jana"),
                    @ApiImplicitParam(name = "followingUser", value = "user id", required = true, dataType = "string", paramType = "body", defaultValue="Mark")
    })
    @ApiResponses(value = {
                    @ApiResponse(code = 201, message = "Success", response = ResponseEntity.class),
                    @ApiResponse(code = 500, message = "Failure")})
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
    @ApiOperation(value = "allows to fetch tweets of all following users in reverse chronological order", nickname = "all tweets feed")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "userId", value = "user id", required = true, dataType = "string", paramType = "path", defaultValue="Jana")
    })
    @ApiResponses(value = {
                    @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
                    @ApiResponse(code = 500, message = "Failure")})
    @RequestMapping(method= RequestMethod.GET, value = "/feed/{userId}")
    public TweetFeed feed(@PathVariable String userId) {
        return tweetService.tweetFeed(userId);
    }

}
