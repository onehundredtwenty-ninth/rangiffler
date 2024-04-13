package com.onehundredtwentyninth.rangiffler.guicebinding;

import com.google.inject.AbstractModule;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepository;
import com.onehundredtwentyninth.rangiffler.db.repository.CountryRepositorySJdbc;

public class BasicModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CountryRepository.class).to(CountryRepositorySJdbc.class);
  }
}
