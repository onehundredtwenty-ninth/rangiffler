package com.onehundredtwentyninth.data.repository;

import com.onehundredtwentyninth.data.CountryEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

  Optional<CountryEntity> findByCode(String code);
}
