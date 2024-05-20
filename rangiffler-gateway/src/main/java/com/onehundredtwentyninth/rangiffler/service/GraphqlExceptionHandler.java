package com.onehundredtwentyninth.rangiffler.service;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GraphqlExceptionHandler extends DataFetcherExceptionResolverAdapter {

  @Override
  protected GraphQLError resolveToSingleError(@Nonnull Throwable ex, @Nonnull DataFetchingEnvironment env) {
    log.error(ex.getMessage(), ex);

    if (ex instanceof IllegalArgumentException || ex instanceof IllegalStateException) {
      return GraphqlErrorBuilder.newError()
          .errorType(ErrorType.BAD_REQUEST)
          .message(ex.getMessage())
          .path(env.getExecutionStepInfo().getPath())
          .location(env.getField().getSourceLocation())
          .build();
    }

    if (ex instanceof NoSuchElementException) {
      return GraphqlErrorBuilder.newError()
          .errorType(ErrorType.NOT_FOUND)
          .message(ex.getMessage())
          .path(env.getExecutionStepInfo().getPath())
          .location(env.getField().getSourceLocation())
          .build();
    }

    return GraphqlErrorBuilder.newError()
        .errorType(ErrorType.INTERNAL_ERROR)
        .message(ex.getMessage())
        .path(env.getExecutionStepInfo().getPath())
        .location(env.getField().getSourceLocation())
        .build();
  }
}
