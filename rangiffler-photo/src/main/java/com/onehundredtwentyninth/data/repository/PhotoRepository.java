package com.onehundredtwentyninth.data.repository;

import com.onehundredtwentyninth.data.PhotoEntity;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {

  Page<PhotoEntity> findByUserId(@Nonnull UUID userId, @Nonnull Pageable pageable);

  Page<PhotoEntity> findByUserIdIn(@Nonnull List<UUID> userId, @Nonnull Pageable pageable);
}
