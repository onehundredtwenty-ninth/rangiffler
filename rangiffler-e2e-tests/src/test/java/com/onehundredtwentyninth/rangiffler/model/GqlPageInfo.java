package com.onehundredtwentyninth.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GqlPageInfo(Boolean hasPreviousPage, Boolean hasNextPage, @JsonProperty("__typename") String typeName) {

}
