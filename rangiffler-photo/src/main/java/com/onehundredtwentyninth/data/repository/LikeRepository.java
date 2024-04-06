package com.onehundredtwentyninth.data.repository;

import com.onehundredtwentyninth.data.LikeEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, UUID> {

}
