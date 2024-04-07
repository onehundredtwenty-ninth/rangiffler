package com.onehundredtwentyninth.rangiffler.service;


import com.onehundredtwentyninth.rangiffler.exception.CountryNotFoundException;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@Slf4j
@GrpcAdvice
public class GrpcErrorHandler {

  @GrpcExceptionHandler(CountryNotFoundException.class)
  public Status handleNoSuchElementException(final CountryNotFoundException e) {
    return Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);
  }

  @GrpcExceptionHandler(Exception.class)
  public Status handleException(final Exception e) {
    log.error("Exception occurred", e);
    return Status.ABORTED.withDescription(e.getMessage()).withCause(e);
  }
}
