package com.onehundredtwentyninth.rangiffler.service;

import com.onehundredtwentyninth.rangiffler.data.Authority;
import com.onehundredtwentyninth.rangiffler.data.AuthorityEntity;
import com.onehundredtwentyninth.rangiffler.data.UserEntity;
import com.onehundredtwentyninth.rangiffler.data.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.model.UserJson;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final KafkaTemplate<String, UserJson> kafkaTemplate;

  @Autowired
  public UserService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     KafkaTemplate<String, UserJson> kafkaTemplate) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Transactional
  public @Nonnull
  String registerUser(@Nonnull String username, @Nonnull String password, @Nonnull String countryCode) {
    UserEntity userEntity = new UserEntity();
    userEntity.setEnabled(true);
    userEntity.setAccountNonExpired(true);
    userEntity.setCredentialsNonExpired(true);
    userEntity.setAccountNonLocked(true);
    userEntity.setUsername(username);
    userEntity.setPassword(passwordEncoder.encode(password));

    AuthorityEntity readAuthorityEntity = new AuthorityEntity();
    readAuthorityEntity.setAuthority(Authority.read);
    AuthorityEntity writeAuthorityEntity = new AuthorityEntity();
    writeAuthorityEntity.setAuthority(Authority.write);

    userEntity.addAuthorities(readAuthorityEntity, writeAuthorityEntity);
    String savedUser = userRepository.save(userEntity).getUsername();
    kafkaTemplate.send("users", new UserJson(savedUser, countryCode));
    LOG.info("### Kafka topic [users] sent message: {}", savedUser);
    return savedUser;
  }
}
