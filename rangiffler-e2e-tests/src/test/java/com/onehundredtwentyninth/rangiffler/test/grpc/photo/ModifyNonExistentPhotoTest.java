package com.onehundredtwentyninth.rangiffler.test.grpc.photo;

import com.onehundredtwentyninth.rangiffler.assertion.GrpcStatusExceptionAssertions;
import com.onehundredtwentyninth.rangiffler.constant.Epics;
import com.onehundredtwentyninth.rangiffler.constant.Features;
import com.onehundredtwentyninth.rangiffler.constant.JUnitTags;
import com.onehundredtwentyninth.rangiffler.constant.Layers;
import com.onehundredtwentyninth.rangiffler.constant.Suites;
import com.onehundredtwentyninth.rangiffler.grpc.DeletePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.LikePhotoRequest;
import com.onehundredtwentyninth.rangiffler.grpc.UpdatePhotoRequest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@Epic(Epics.PHOTOS)
@Feature(Features.UPDATE_PHOTO)
@Tags({@Tag(Layers.GRPC), @Tag(Suites.SMOKE), @Tag(JUnitTags.PHOTOS), @Tag(JUnitTags.UPDATE_PHOTO)})
class ModifyNonExistentPhotoTest extends GrpcPhotoTestBase {

  @DisplayName("Обновление несуществующего фото")
  @Test
  void updateNonExistentPhotoTest() {
    var request = UpdatePhotoRequest.newBuilder().setId("00000000-0000-0000-0000-000000000000").build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.updatePhoto(request))
        .isInstanceOfStatusRuntimeException()
        .hasPhotoNotFoundMessage(request.getId());
  }

  @DisplayName("Проставление лайка несуществующему фото")
  @Test
  void likeNonExistentPhotoTest() {
    var request = LikePhotoRequest.newBuilder().setPhotoId("00000000-0000-0000-0000-000000000000").build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.likePhoto(request))
        .isInstanceOfStatusRuntimeException()
        .hasPhotoNotFoundMessage(request.getPhotoId());
  }

  @DisplayName("Удаление несуществующего фото")
  @Test
  void deleteNonExistentPhotoTest() {
    var request = DeletePhotoRequest.newBuilder().setPhotoId("00000000-0000-0000-0000-000000000000").build();
    GrpcStatusExceptionAssertions.assertThatThrownBy(() -> blockingStub.deletePhoto(request))
        .isInstanceOfStatusRuntimeException()
        .hasPhotoNotFoundMessage(request.getPhotoId());
  }
}
