package com.onehundredtwentyninth.rangiffler.service;


import io.grpc.Status;
import java.util.NoSuchElementException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcErrorHandler {

  @GrpcExceptionHandler(NoSuchElementException.class)
  public Status handleNoSuchElementException(final NoSuchElementException e) {
    return Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);
  }

  @GrpcExceptionHandler(IllegalArgumentException.class)
  public Status handleIllegalArgumentException(final IllegalArgumentException e) {
    return Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);
  }

  @GrpcExceptionHandler(IllegalStateException.class)
  public Status handleIllegalStateException(final IllegalStateException e) {
    return Status.ABORTED.withDescription(e.getMessage()).withCause(e);
  }
}
