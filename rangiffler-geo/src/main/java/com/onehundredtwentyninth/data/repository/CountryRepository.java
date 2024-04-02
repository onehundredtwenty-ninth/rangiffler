package com.onehundredtwentyninth.data.repository;

import com.onehundredtwentyninth.data.CountryEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

}
