package com.jk.tweetapi;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.jk.tweetapi.domain.Tweet;
import com.jk.tweetapi.domain.TweetFeed;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
		assertThat(entity.getBody().getTweets().size()).isEqualTo(1);
		assertThat(entity.getBody().getTweets().get(0).getText()).isEqualTo("some tweet");
	}

	@Test
	public void userShouldNotBeAbleToPostATweetMoreThan140Characters() throws IOException {
		// Given
		Tweet tweet = new Tweet("asdfaksdfgkjahgsdfkjgasdkjfghaskjdhgfkjashgdfasjkgdfwgfwuegfkajhsdgfasjhgdfaksjhg"
						+ "dfajskhdgfjkashgdfkjashgdfkjahgsdkfjhgaskjdfgaksjhdgfkajshdgfajshgdf");

		// When
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/tweets/addTweet/user1", tweet, String.class);

		// When
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void userShouldBeAbleToPostATweetOfLength140Characters() throws IOException {
		// Given
		Tweet tweet = new Tweet("asdfaksdfgkjahgsdfkjgasdkjfghaskjdhgfkjashgdfasjkgdfwgfwuegfkajhsdgfasjhgdfaksjhg"
						+ "dfajskhdgfjkashgdfkjashgdfkjahgsdkfjhgaskjdfgaksjhdgfkajsha");

		// When
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/tweets/addTweet/user1", tweet, String.class);

		// When
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void userShouldSeeMoreThanOnePublishedTweet() throws IOException {
		// Given
		restTemplate.postForEntity("/tweets/addTweet/user1", new Tweet("tweet1"), String.class);
		restTemplate.postForEntity("/tweets/addTweet/user1", new Tweet("tweet2"), String.class);

		// When
		ResponseEntity<TweetFeed> entity = this.restTemplate.getForEntity("/tweets/user1", TweetFeed.class);

		// Then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody().getTweets()).isNotNull();
		assertThat(entity.getBody().getTweets().size()).isEqualTo(2);
	}

	@Test
	public void userShouldSeeThePublishedTweetsInReverseChronologicalOrder() throws IOException {
		// Given
		restTemplate.postForEntity("/tweets/addTweet/user1", new Tweet("tweet1"), String.class);
		restTemplate.postForEntity("/tweets/addTweet/user1", new Tweet("tweet2"), String.class);
		restTemplate.postForEntity("/tweets/addTweet/user1", new Tweet("tweet3"), String.class);

		// When
		ResponseEntity<TweetFeed> entity = this.restTemplate.getForEntity("/tweets/user1", TweetFeed.class);

		// Then
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody().getTweets()).isNotNull();
		assertThat(entity.getBody().getTweets().size()).isEqualTo(3);
		assertThat(entity.getBody().getTweets().get(0).getText()).isEqualTo("tweet3");
		assertThat(entity.getBody().getTweets().get(1).getText()).isEqualTo("tweet2");
		assertThat(entity.getBody().getTweets().get(2).getText()).isEqualTo("tweet1");
	}

	@Test
	public void shouldBeAbleToFollowAUser() throws Exception {
		// Given when
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/tweets/followUser/user1", "user2", String.class);

		// Then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void shouldSeeTweetFollowedByTheGivenUser() throws IOException {
		// Given
		restTemplate.postForEntity("/tweets/addTweet/user1", new Tweet("tweet1"), String.class);
		restTemplate.postForEntity("/tweets/addTweet/user2", new Tweet("tweet2"), String.class);

		// When
		restTemplate.postForEntity("/tweets/followUser/user1", "user2", String.class);

		// Then
		ResponseEntity<TweetFeed> entity =  this.restTemplate.getForEntity("/tweets/feed/user1", TweetFeed.class);
		List<Tweet> tweets = entity.getBody().getTweets();
		assertThat(tweets.size()).isEqualTo(1);
		assertThat(tweets.get(0).getText()).isEqualTo("tweet2");
	}

	@Test
	public void shouldSeeEmptyTweetFeedWhenFollowedUserHasNotPostedAnyTweets() throws IOException {
		// Given
		restTemplate.postForEntity("/tweets/addTweet/user1", new Tweet("tweet1"), String.class);

		// When
		restTemplate.postForEntity("/tweets/followUser/user1", "user2", String.class);

		// Then
		ResponseEntity<TweetFeed> entity =  this.restTemplate.getForEntity("/tweets/feed/user1", TweetFeed.class);
		List<Tweet> tweets = entity.getBody().getTweets();
		assertThat(tweets).isNullOrEmpty();
	}
}
