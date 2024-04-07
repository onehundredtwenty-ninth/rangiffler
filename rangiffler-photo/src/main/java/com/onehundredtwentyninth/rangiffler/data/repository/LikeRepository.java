package com.onehundredtwentyninth.rangiffler.data.repository;

import com.onehundredtwentyninth.rangiffler.data.LikeEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, UUID> {

}
