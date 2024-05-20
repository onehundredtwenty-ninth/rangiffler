package com.onehundredtwentyninth.rangiffler.model;

import java.time.LocalDate;
import java.util.UUID;

public record GqlLike(
    UUID user,
    String userName,
    LocalDate creationDate
) {

}
