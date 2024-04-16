package com.onehundredtwentyninth.rangiffler.jupiter;

import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.mapper.UserEntityMapper;
import com.onehundredtwentyninth.rangiffler.model.TestUser;
import com.onehundredtwentyninth.rangiffler.service.PhotoDbService;
import com.onehundredtwentyninth.rangiffler.service.PhotoTestService;
import com.onehundredtwentyninth.rangiffler.service.UserDbService;
import com.onehundredtwentyninth.rangiffler.service.UserService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

@Slf4j
public class CreateUserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(CreateUserExtension.class);
  private final UserService userService = new UserDbService();
  private final PhotoTestService photoService = new PhotoDbService();
  private final UserRepository userRepository = new UserRepositorySJdbc();

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    var userParameters = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        CreateUser.class
    );

    if (userParameters.isPresent()) {
      var createdUser = userService.createTestUser(userParameters.get());
      extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), createdUser);
    }
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    var createdUser = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), TestUser.class);
    if (createdUser != null) {
      createdUser.getPhotos().forEach(s -> photoService.deletePhoto(UUID.fromString(s.getId())));
      createdUser.getPhotos().stream()
          .flatMap(s -> s.getLikes().getLikesList().stream())
          .forEach(s -> {
            var userId = UUID.fromString(s.getUserId());
            var username = userRepository.findById(userId).getUsername();
            userService.deleteUser(userId, username);
          });
      createdUser.getFriends().forEach(s -> userService.deleteUser(s.getId(), s.getUsername()));
      userService.deleteUser(createdUser.getId(), createdUser.getUsername());
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return (parameterContext.getParameter().getType().isAssignableFrom(User.class)
        || (parameterContext.getParameter().getType().isAssignableFrom(User[].class)
        && parameterContext.getParameter().isAnnotationPresent(Friends.class))
    )
        && extensionContext.getRequiredTestMethod().isAnnotationPresent(CreateUser.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    var user = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), TestUser.class);
    if (parameterContext.getParameter().getType().isAssignableFrom(User[].class)
        && parameterContext.getParameter().isAnnotationPresent(Friends.class)) {
      return user.getFriends().stream().map(UserEntityMapper::toMessage).toArray(User[]::new);
    } else {
      return UserEntityMapper.toMessage(user);
    }
  }
}
