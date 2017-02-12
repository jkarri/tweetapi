package com.jk.tweetapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.jk.tweetapi.service.InMemoryTweetService;
import com.jk.tweetapi.service.TweetService;

@SpringBootApplication
public class TweetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetApiApplication.class, args);
	}

	@Bean
	public TweetService tweetService() {
		return new InMemoryTweetService();
	}

}
