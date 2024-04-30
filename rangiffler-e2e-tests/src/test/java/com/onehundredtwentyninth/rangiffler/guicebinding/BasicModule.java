package com.onehundredtwentyninth.rangiffler.guicebinding;

import com.google.inject.AbstractModule;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.PhotoRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.service.UserDbService;
import com.onehundredtwentyninth.rangiffler.service.UserService;

public class BasicModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CountryRepository.class).to(CountryRepositorySJdbc.class);
    bind(UserRepository.class).to(UserRepositorySJdbc.class);
    bind(FriendshipRepository.class).to(FriendshipRepositorySJdbc.class);
    bind(PhotoRepository.class).to(PhotoRepositorySJdbc.class);
    bind(UserService.class).to(UserDbService.class);
  }
}
