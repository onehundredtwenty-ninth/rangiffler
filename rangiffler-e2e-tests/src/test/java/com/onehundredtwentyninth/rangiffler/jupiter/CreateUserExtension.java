package com.onehundredtwentyninth.rangiffler.jupiter;

import com.github.javafaker.Faker;
import com.onehundredtwentyninth.rangiffler.grpc.Photo;
import com.onehundredtwentyninth.rangiffler.grpc.User;
import com.onehundredtwentyninth.rangiffler.service.PhotoDbService;
import com.onehundredtwentyninth.rangiffler.service.PhotoTestService;
import com.onehundredtwentyninth.rangiffler.service.UserDbService;
import com.onehundredtwentyninth.rangiffler.service.UserService;
import java.util.ArrayList;
import java.util.List;
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
  private final Faker faker = new Faker();

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    var userParameters = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        CreateUser.class
    );

    if (userParameters.isPresent()) {
      var createdUser = userParameters.get().username().isEmpty()
          ? userService.createRandomUser()
          : userService.createUser(userParameters.get().username(), userParameters.get().password(),
              faker.name().firstName(), faker.name().lastName());
      extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), createdUser);

      var futureFriends = new ArrayList<User>();
      var createdPhotos = new ArrayList<Photo>();
      var createdLikeUsers = new ArrayList<User>();

      for (var photoParameters : userParameters.get().photos()) {
        var createdPhoto = photoService.createPhoto(UUID.fromString(createdUser.getId()), photoParameters.countryCode(),
            photoParameters.image(), photoParameters.description());
        createdPhotos.add(createdPhoto);

        for (var i = 0; i < photoParameters.likes(); i++) {
          var likeUser = userService.createRandomUser();
          photoService.likePhoto(UUID.fromString(likeUser.getId()), UUID.fromString(createdPhoto.getId()));
          createdLikeUsers.add(likeUser);
        }
      }

      for (var friendParameters : userParameters.get().friends()) {
        var createdFriend = userService.createFriend(createdUser.getId(), friendParameters);
        futureFriends.add(createdFriend);

        for (var photoParameters : friendParameters.photos()) {
          var createdPhoto = photoService.createPhoto(UUID.fromString(createdFriend.getId()),
              photoParameters.countryCode(), photoParameters.image(), photoParameters.description());
          createdPhotos.add(createdPhoto);

          for (var i = 0; i < photoParameters.likes(); i++) {
            var likeUser = userService.createRandomUser();
            photoService.likePhoto(UUID.fromString(likeUser.getId()), UUID.fromString(createdPhoto.getId()));
            createdLikeUsers.add(likeUser);
          }
        }
      }

      setCreatedFriends(extensionContext, futureFriends);
      setCreatedPhotos(extensionContext, createdPhotos);
      setCreatedLikeUsers(extensionContext, createdLikeUsers);
    }
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) {
    var createdUser = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), User.class);
    if (createdUser != null) {
      var createdPhotos = getCreatedPhotos(extensionContext);
      var createdFriends = getCreatedFriends(extensionContext);
      var createdLikeUsers = getCreatedLikeUsers(extensionContext);

      createdPhotos.forEach(s -> photoService.deletePhoto(UUID.fromString(s.getId())));
      createdLikeUsers.forEach(userService::deleteUser);
      createdFriends.forEach(userService::deleteUser);
      userService.deleteUser(createdUser);
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
    if (parameterContext.getParameter().getType().isAssignableFrom(User[].class)
        && parameterContext.getParameter().isAnnotationPresent(Friends.class)) {
      return getCreatedFriends(extensionContext).toArray(User[]::new);
    } else {
      return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), User.class);
    }
  }

  private void setCreatedFriends(ExtensionContext extensionContext, List<User> futureFriends) {
    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId() + "createdFriends", futureFriends);
  }

  @SuppressWarnings("unchecked")
  private List<User> getCreatedFriends(ExtensionContext extensionContext) {
    return extensionContext.getStore(NAMESPACE)
        .getOrDefault(extensionContext.getUniqueId() + "createdFriends", List.class, new ArrayList<>());
  }

  private void setCreatedPhotos(ExtensionContext extensionContext, List<Photo> photos) {
    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId() + "createdPhotos", photos);
  }

  @SuppressWarnings("unchecked")
  private List<Photo> getCreatedPhotos(ExtensionContext extensionContext) {
    return extensionContext.getStore(NAMESPACE)
        .getOrDefault(extensionContext.getUniqueId() + "createdPhotos", List.class, new ArrayList<>());
  }

  private void setCreatedLikeUsers(ExtensionContext extensionContext, List<User> likeUsers) {
    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId() + "createdLikeUsers", likeUsers);
  }

  @SuppressWarnings("unchecked")
  private List<User> getCreatedLikeUsers(ExtensionContext extensionContext) {
    return extensionContext.getStore(NAMESPACE)
        .getOrDefault(extensionContext.getUniqueId() + "createdLikeUsers", List.class, new ArrayList<>());
  }
}
