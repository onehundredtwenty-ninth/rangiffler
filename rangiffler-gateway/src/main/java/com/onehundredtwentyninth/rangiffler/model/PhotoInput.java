package com.onehundredtwentyninth.rangiffler.model;

import java.util.UUID;

public record PhotoInput(UUID id, String src, CountryInput country, String description, LikeInput like) {

}
