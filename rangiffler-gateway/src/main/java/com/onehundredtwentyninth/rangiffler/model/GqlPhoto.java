package com.onehundredtwentyninth.rangiffler.model;

import java.time.LocalDate;
import java.util.UUID;

public record GqlPhoto(
    UUID id,
    String src,
    GqlCountry country,
    String description,
    LocalDate creationDate,
    GqlLikes likes
) {

}
