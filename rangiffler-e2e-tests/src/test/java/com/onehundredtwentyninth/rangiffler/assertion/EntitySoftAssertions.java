package com.onehundredtwentyninth.rangiffler.assertion;

import com.onehundredtwentyninth.rangiffler.db.model.PhotoEntity;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.SoftAssertionsProvider;
import org.assertj.core.api.StandardSoftAssertionsProvider;

public class EntitySoftAssertions extends AbstractSoftAssertions implements StandardSoftAssertionsProvider {

  public static void assertSoftly(Consumer<EntitySoftAssertions> consumer) {
    SoftAssertionsProvider.assertSoftly(EntitySoftAssertions.class, consumer);
  }

  public EntityPhotoAssertions assertThat(PhotoEntity actual) {
    return proxy(EntityPhotoAssertions.class, PhotoEntity.class, actual);
  }
}
