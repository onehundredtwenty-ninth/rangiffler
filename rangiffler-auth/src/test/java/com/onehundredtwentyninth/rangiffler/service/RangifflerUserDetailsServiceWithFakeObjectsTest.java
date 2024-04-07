package com.onehundredtwentyninth.rangiffler.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.onehundredtwentyninth.rangiffler.data.Authority;
import com.onehundredtwentyninth.rangiffler.data.AuthorityEntity;
import com.onehundredtwentyninth.rangiffler.data.UserEntity;
import com.onehundredtwentyninth.rangiffler.data.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class RangifflerUserDetailsServiceWithFakeObjectsTest {

  private RangifflerUserDetailsService nifflerUserDetailsService;
  private UserEntity testUserEntity;
  private List<AuthorityEntity> authorityEntities;

  @BeforeEach
  void initMockRepository() {
    var userRepository = new UserRepository.Fake();
    AuthorityEntity read = new AuthorityEntity();
    read.setUser(testUserEntity);
    read.setAuthority(Authority.read);
    AuthorityEntity write = new AuthorityEntity();
    write.setUser(testUserEntity);
    write.setAuthority(Authority.write);
    authorityEntities = List.of(read, write);

    testUserEntity = new UserEntity();
    testUserEntity.setUsername("correct");
    testUserEntity.setAuthorities(authorityEntities);
    testUserEntity.setEnabled(true);
    testUserEntity.setPassword("test-pass");
    testUserEntity.setAccountNonExpired(true);
    testUserEntity.setAccountNonLocked(true);
    testUserEntity.setCredentialsNonExpired(true);
    testUserEntity.setId(UUID.randomUUID());

    userRepository.withUserEntities(List.of(testUserEntity));

    nifflerUserDetailsService = new RangifflerUserDetailsService(userRepository);
  }

  @Test
  void loadUserByUsername() {
    final UserDetails correct = nifflerUserDetailsService.loadUserByUsername("correct");

    final List<SimpleGrantedAuthority> expectedAuthorities = authorityEntities.stream()
        .map(a -> new SimpleGrantedAuthority(a.getAuthority().name()))
        .toList();

    assertEquals(
        "correct",
        correct.getUsername()
    );
    assertEquals(
        "test-pass",
        correct.getPassword()
    );
    assertEquals(
        expectedAuthorities,
        correct.getAuthorities()
    );

    assertTrue(correct.isAccountNonExpired());
    assertTrue(correct.isAccountNonLocked());
    assertTrue(correct.isCredentialsNonExpired());
    assertTrue(correct.isEnabled());
  }

  @Test
  void loadUserByUsernameNegayive() {
    final UsernameNotFoundException exception = assertThrows(
        UsernameNotFoundException.class,
        () -> nifflerUserDetailsService.loadUserByUsername("incorrect")
    );

    assertEquals(
        "Username: incorrect not found",
        exception.getMessage()
    );
  }
}