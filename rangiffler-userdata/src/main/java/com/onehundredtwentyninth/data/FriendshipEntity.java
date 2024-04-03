package com.onehundredtwentyninth.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "\"friendship\"")
public class FriendshipEntity {

  @Id
  @ManyToOne
  @JoinColumn(name = "requester_id", referencedColumnName = "id")
  private UserEntity requester;

  @Id
  @ManyToOne
  @JoinColumn(name = "addressee_id", referencedColumnName = "id")
  private UserEntity addressee;

  @Column(name = "created_date")
  private Timestamp createdDate;

  @Enumerated(EnumType.STRING)
  private FriendshipStatus status;
}
