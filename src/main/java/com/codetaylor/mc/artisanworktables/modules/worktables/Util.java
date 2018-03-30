package com.codetaylor.mc.artisanworktables.modules.worktables;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

/**
 * Some utils borrowed from:
 * https://github.com/raoulvdberge/refinedstorage/blob/mc1.12/src/main/java/com/raoulvdberge/refinedstorage/util/RenderUtils.java
 */
public class Util {

  public static final Matrix4f EMPTY_MATRIX_TRANSFORM = getTransform(0, 0, 0, 0, 0, 0, 1.0f).getMatrix();
  public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> DEFAULT_ITEM_TRANSFORM;

  public static TRSRTransformation getTransform(
      float tx,
      float ty,
      float tz,
      float ax,
      float ay,
      float az,
      float s
  ) {

    return new TRSRTransformation(
        new Vector3f(tx / 16, ty / 16, tz / 16),
        TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
        new Vector3f(s, s, s),
        null
    );
  }

  public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> getDefaultItemTransforms() {

    if (DEFAULT_ITEM_TRANSFORM != null) {
      return DEFAULT_ITEM_TRANSFORM;
    }

    return DEFAULT_ITEM_TRANSFORM = ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
        .put(
            ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
            getTransform(0, 3, 1, 0, 0, 0, 0.55f)
        )
        .put(
            ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
            getTransform(0, 3, 1, 0, 0, 0, 0.55f)
        )
        .put(
            ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,
            getTransform(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f)
        )
        .put(
            ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
            getTransform(1.13f, 3.2f, 1.13f, 0, 90, -25, 0.68f)
        )
        .put(
            ItemCameraTransforms.TransformType.GROUND,
            getTransform(0, 2, 0, 0, 0, 0, 0.5f)
        )
        .put(
            ItemCameraTransforms.TransformType.HEAD,
            getTransform(0, 13, 7, 0, 180, 0, 1)
        )
        .build();
  }
}
