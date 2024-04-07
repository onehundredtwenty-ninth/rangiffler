package com.onehundredtwentyninth.rangiffler.service;


import com.onehundredtwentyninth.rangiffler.exception.FriendshipNotFoundException;
import com.onehundredtwentyninth.rangiffler.exception.FriendshipRequestNotFoundException;
import com.onehundredtwentyninth.rangiffler.exception.UserNotFoundException;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@Slf4j
@GrpcAdvice
public class GrpcErrorHandler {

  @GrpcExceptionHandler(FriendshipNotFoundException.class)
  public Status handleFriendshipNotFoundException(final FriendshipNotFoundException e) {
    return Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);
  }

  @GrpcExceptionHandler(FriendshipRequestNotFoundException.class)
  public Status handleFriendshipRequestNotFoundException(final FriendshipRequestNotFoundException e) {
    return Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);
  }

  @GrpcExceptionHandler(UserNotFoundException.class)
  public Status handleUserNotFoundException(final UserNotFoundException e) {
    return Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);
  }

  @GrpcExceptionHandler(Exception.class)
  public Status handleException(final Exception e) {
    log.error("Exception occurred", e);
    return Status.ABORTED.withDescription(e.getMessage()).withCause(e);
  }
}
