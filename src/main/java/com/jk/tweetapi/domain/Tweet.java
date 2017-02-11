package com.jk.tweetapi.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a tweet published by a user.
 * The tweet generates the timestamp when created.
 */
public class Tweet implements Comparable<Tweet> {
    @Size(max = 140)
    private String text;

    @JsonIgnore
    private LocalDateTime created = LocalDateTime.now();

    public LocalDateTime getCreated() {
        return created;
    }

    @JsonCreator
    public Tweet(@JsonProperty("text") String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public int compareTo(Tweet other) {
        return other.getCreated().compareTo(this.created);
    }
}
