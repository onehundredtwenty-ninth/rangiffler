package com.onehundredtwentyninth.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
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
  private GqlConnection<GqlUser> friends;
  private GqlConnection<GqlUser> incomeInvitations;
  private GqlConnection<GqlUser> outcomeInvitations;
  private GqlCountry location;

  @SuppressWarnings("unchecked")
  @JsonProperty("node")
  private void unpackNode(Map<String, Object> node) {
    var userAsMap = node.entrySet().stream().collect(Collectors.toMap(
            Entry::getKey,
            e -> e.getValue().toString()
        )
    );
    this.id = UUID.fromString(userAsMap.get("id"));
    this.username = userAsMap.get("username");
    this.firstname = userAsMap.get("firstname");
    this.surname = userAsMap.get("surname");
    this.avatar = userAsMap.get("avatar");
    this.friendStatus = FriendStatus.valueOf(userAsMap.get("friendStatus"));

    var locationAsMap = (Map<String, String>) node.get("location");
    this.location = new GqlCountry(
        locationAsMap.get("code"),
        locationAsMap.get("name"),
        locationAsMap.get("flag")
    );
  }
}
