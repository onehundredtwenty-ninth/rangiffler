package com.onehundredtwentyninth.rangiffler.service;


import com.onehundredtwentyninth.rangiffler.exception.IllegalPhotoAccessException;
import com.onehundredtwentyninth.rangiffler.exception.PhotoNotFoundException;
import com.onehundredtwentyninth.rangiffler.exception.StatisticNotFoundException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcErrorHandler {

  @GrpcExceptionHandler(PhotoNotFoundException.class)
  public Status handlePhotoNotFoundException(final PhotoNotFoundException e) {
    return Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);
  }

  @GrpcExceptionHandler(IllegalPhotoAccessException.class)
  public Status handleIllegalPhotoAccessException(final IllegalPhotoAccessException e) {
    return Status.PERMISSION_DENIED.withDescription(e.getMessage()).withCause(e);
  }

  @GrpcExceptionHandler(StatisticNotFoundException.class)
  public Status handleStatisticNotFoundException(final StatisticNotFoundException e) {
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
