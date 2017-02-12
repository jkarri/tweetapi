package com.jk.tweetapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.jk.tweetapi.service.InMemoryTweetService;
import com.jk.tweetapi.service.TweetService;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger2
public class TweetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetApiApplication.class, args);
	}

	@Bean
	public TweetService tweetService() {
		return new InMemoryTweetService();
	}

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
						.groupName("tweets")
						.apiInfo(apiInfo())
						.select()
						.paths(regex("/tweets.*"))
						.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
						.title("Tweet API with Swagger")
						.description("Tweet API with Swagger")
						.contact(new Contact("Janardhan Karri", "", "janakarri@gmail.com"))
						.build();
	}
}
