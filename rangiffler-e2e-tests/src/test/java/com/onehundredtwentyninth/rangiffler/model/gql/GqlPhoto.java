package com.onehundredtwentyninth.rangiffler.model.gql;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.onehundredtwentyninth.rangiffler.model.gql.GqlPhoto.PhotoNodeSerializer;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "src")
@JsonDeserialize(using = PhotoNodeSerializer.class)
public class GqlPhoto extends GqlResponseType {

  private UUID id;
  private String src;
  private GqlCountry country;
  private String description;
  private LocalDate creationDate;
  private GqlLikes likes;

  static class PhotoNodeSerializer extends JsonDeserializer<GqlPhoto> {

    @Override
    public GqlPhoto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      var value = (JsonNode) p.readValueAsTree();
      var node = Optional.ofNullable(value.get("node")).orElse(value);

      var photo = new GqlPhoto();
      photo.setId(UUID.fromString(node.get("id").asText()));
      photo.setSrc(node.get("src") != null ? node.get("src").asText() : null);
      photo.setDescription(node.get("description").asText());
      photo.setCreationDate(node.get("creationDate") == null || node.get("creationDate").asText().isBlank()
          ? null
          : LocalDate.parse(node.get("creationDate").asText()));

      var country = ctxt.readTreeAsValue(node.get("country"), GqlCountry.class);
      var likes = ctxt.readTreeAsValue(node.get("likes"), GqlLikes.class);
      photo.setCountry(country);
      photo.setLikes(likes);

      return photo;
    }
  }
}
