package com.jk.tweetapi.domain;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a tweet published by a user.
 * The tweet generates the timestamp when created.
 */
public class Tweet {
    @Size(max = 140)
    private String text;

    @JsonCreator
    public Tweet(@JsonProperty("text") String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
