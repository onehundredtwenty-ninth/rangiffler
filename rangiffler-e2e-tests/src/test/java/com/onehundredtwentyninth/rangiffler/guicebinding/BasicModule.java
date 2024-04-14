package com.onehundredtwentyninth.rangiffler.guicebinding;

import com.google.inject.AbstractModule;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.FriendshipRepositorySJdbc;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.UserRepositorySJdbc;

public class BasicModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CountryRepository.class).to(CountryRepositorySJdbc.class);
    bind(UserRepository.class).to(UserRepositorySJdbc.class);
    bind(FriendshipRepository.class).to(FriendshipRepositorySJdbc.class);
  }
}
