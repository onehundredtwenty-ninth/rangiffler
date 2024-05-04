package com.onehundredtwentyninth.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

public record GqlCountry(
    @JsonIgnore
    UUID id,
    String code,
    String name,
    String flag
) {

}
