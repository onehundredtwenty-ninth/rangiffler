package com.onehundredtwentyninth.rangiffler.utils;

import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUtils {

  @SneakyThrows
  public static String getImageFromResourceAsBase64(String imageName) {
    var res = ImageUtils.class.getClassLoader().getResource("image/" + imageName);
    var imageFile = Paths.get(Objects.requireNonNull(res).toURI()).toFile();
    var fileContent = FileUtils.readFileToByteArray(imageFile);
    return "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
  }
}
