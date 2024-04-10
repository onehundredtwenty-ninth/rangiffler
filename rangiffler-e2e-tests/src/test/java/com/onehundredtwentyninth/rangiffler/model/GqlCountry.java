package com.onehundredtwentyninth.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GqlCountry(String code,
                         String name,
                         String flag,
                         @JsonProperty("__typename")
                         String typename) {

}
