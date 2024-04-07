package com.onehundredtwentyninth.rangiffler.data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "\"photo\"")
public class PhotoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
  private UUID id;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "country_id")
  private UUID countryId;

  @Column
  private String description;

  @Column(name = "photo", columnDefinition = "bytea")
  private byte[] photo;

  @Column(name = "created_date")
  private Timestamp createdDate;

  @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinTable(
      name = "photo_like",
      joinColumns = {@JoinColumn(name = "photo_id")},
      inverseJoinColumns = {@JoinColumn(name = "like_id")}
  )
  private Set<LikeEntity> likes = new HashSet<>();
}
