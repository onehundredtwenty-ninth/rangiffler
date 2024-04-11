package com.onehundredtwentyninth.rangiffler.jupiter;

import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.jupiter.Friend.FriendshipRequestType;
import com.onehundredtwentyninth.rangiffler.service.UserDbService;
import com.onehundredtwentyninth.rangiffler.service.UserService;
import java.util.ArrayList;
import java.util.List;
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

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    var userParameters = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        CreateUser.class
    );

    if (userParameters.isPresent()) {
      var createdUser = userParameters.get().username().isEmpty()
          ? userService.createRandomUser()
          : userService.createUser(userParameters.get().username(), userParameters.get().password());
      extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), createdUser);

      if (userParameters.get().friends().length != 0) {
        createFriends(userParameters.get(), createdUser, extensionContext);
      }
    }
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    var createdUser = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), User.class);
    if (createdUser != null) {
      var createdFriends = getCreatedFriends(extensionContext);
      createdFriends.forEach(userService::deleteUser);
      userService.deleteUser(createdUser);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(User.class)
        && extensionContext.getRequiredTestMethod().isAnnotationPresent(CreateUser.class);
  }

  @Override
  public User resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
      throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), User.class);
  }

  private void createFriends(CreateUser userParameters, User user, ExtensionContext extensionContext) {
    List<User> futureFriends = new ArrayList<>();

    for (int i = 0; i < userParameters.friends().length; i++) {
      var createdFriend = userService.createRandomUser();
      futureFriends.add(createdFriend);

      if (!userParameters.friends()[i].pending()) {
        userService.createFriendship(user.getId(), createdFriend.getId(), false);
      } else {
        if (userParameters.friends()[i].friendshipRequestType() == FriendshipRequestType.OUTCOME) {
          userService.createFriendship(user.getId(), createdFriend.getId(), true);
        } else {
          userService.createFriendship(createdFriend.getId(), user.getId(), true);
        }
      }
    }
    setCreatedFriends(extensionContext, futureFriends);
  }

  private void setCreatedFriends(ExtensionContext extensionContext, List<User> futureFriends) {
    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId() + "createdFriends", futureFriends);
  }

  @SuppressWarnings("unchecked")
  private List<User> getCreatedFriends(ExtensionContext extensionContext) {
    return extensionContext.getStore(NAMESPACE)
        .getOrDefault(extensionContext.getUniqueId() + "createdFriends", List.class, new ArrayList<>());
  }
}
