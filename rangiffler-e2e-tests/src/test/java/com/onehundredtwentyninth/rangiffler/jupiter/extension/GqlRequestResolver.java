package com.onehundredtwentyninth.rangiffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onehundredtwentyninth.rangiffler.jupiter.annotation.GqlRequestFile;
import com.onehundredtwentyninth.rangiffler.model.GqlRequest;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class GqlRequestResolver implements ParameterResolver {

  private static final ObjectMapper om = new ObjectMapper();
  private final ClassLoader cl = GqlRequestResolver.class.getClassLoader();

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return AnnotationSupport.isAnnotated(parameterContext.getParameter(), GqlRequestFile.class)
        && parameterContext.getParameter().getType().isAssignableFrom(GqlRequest.class)
        && !AnnotationSupport.findAnnotation(parameterContext.getParameter(), GqlRequestFile.class)
        .orElseThrow()
        .value()
        .isBlank();
  }

  @Override
  public GqlRequest resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    GqlRequestFile annotation = AnnotationSupport.findAnnotation(parameterContext.getParameter(), GqlRequestFile.class)
        .orElseThrow();
    try (InputStream is = cl.getResourceAsStream(annotation.value())) {
      return om.readValue(is.readAllBytes(), GqlRequest.class);
    } catch (IOException e) {
      throw new ParameterResolutionException(e.getMessage());
    }
  }
}
