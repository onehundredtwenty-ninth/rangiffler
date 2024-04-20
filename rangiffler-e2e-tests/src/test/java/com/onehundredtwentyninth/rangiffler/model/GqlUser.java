package com.onehundredtwentyninth.rangiffler.model;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class GqlUser extends GqlResponseType {

  private UUID id;
  private String username;
  private String firstname;
  private String surname;
  private String avatar;
  private FriendStatus friendStatus;
  private List<GqlUser> friends;
  private List<GqlUser> incomeInvitations;
  private List<GqlUser> outcomeInvitations;
  private GqlCountry location;
}
