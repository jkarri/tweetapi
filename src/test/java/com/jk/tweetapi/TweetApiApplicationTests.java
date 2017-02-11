package com.jk.tweetapi;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.jk.tweetapi.domain.Tweet;
import com.jk.tweetapi.domain.TweetFeed;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TweetApiApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void userShouldBeAbleToPublishATweet() throws IOException {
		// Given
		Tweet tweet = new Tweet("some tweet");

		// When
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/tweets/addTweet/user1", tweet, String.class);

		// Then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void userShouldSeePublishedTweet() throws IOException {
		// Given
		Tweet tweet = new Tweet("some tweet");
		restTemplate.postForEntity("/tweets/addTweet/user1", tweet, String.class);

		// When
		ResponseEntity<TweetFeed> entity = this.restTemplate.getForEntity("/tweets/user1", TweetFeed.class);

		// Then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody().getTweets()).isNotNull();
	}
}
