package com.onehundredtwentyninth.rangiffler.model;

import java.util.Map;

public record GqlRequest(String operationName,
                         Map<String, Object> variables,
                         String query) {
}
