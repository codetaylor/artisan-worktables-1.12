package com.codetaylor.mc.artisanworktables.modules.worktables;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.*;

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

  /**
   * Uses a flood fill to find all tables adjacent to this one. An empty list is returned
   * if any of the tables found are of the same type and tier. If a player is provided,
   * any tables outside of the player's reach will not be returned in the list.
   *
   * @param result a list to store the result
   * @param player the player, can be null
   * @return result list
   */
  public static List<TileEntityBase> getJoinedTables(
      List<TileEntityBase> result,
      World world,
      BlockPos pos,
      @Nullable EntityPlayer player
  ) {

    Map<String, TileEntityBase> joinedTableMap = new TreeMap<>();
    Set<BlockPos> searchedPositionSet = new HashSet<>();
    Queue<BlockPos> toSearchQueue = new ArrayDeque<>();

    toSearchQueue.offer(pos);
    toSearchQueue.offer(pos.offset(EnumFacing.NORTH));
    toSearchQueue.offer(pos.offset(EnumFacing.EAST));
    toSearchQueue.offer(pos.offset(EnumFacing.SOUTH));
    toSearchQueue.offer(pos.offset(EnumFacing.WEST));

    BlockPos searchPosition;

    while ((searchPosition = toSearchQueue.poll()) != null) {

      if (searchedPositionSet.contains(searchPosition)) {
        // we've already looked here, skip
        continue;
      }

      // record that we've looked here
      searchedPositionSet.add(searchPosition);

      TileEntity tileEntity = world.getTileEntity(searchPosition);

      if (tileEntity instanceof TileEntityBase) {
        String key = ((TileEntityBase) tileEntity).getUuid();

        if (joinedTableMap.containsKey(key)) {
          // this indicates two tables of the same type joined in the pseudo-multiblock
          // and we need to invalidate the structure by returning nothing
          return Collections.emptyList();
        }

        // found a table!
        if (player == null || ((TileEntityBase) tileEntity).canPlayerUse(player)) {
          joinedTableMap.put(key, (TileEntityBase) tileEntity);
        }

        // check around this newly discovered table
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.NORTH));
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.EAST));
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.SOUTH));
        toSearchQueue.offer(tileEntity.getPos().offset(EnumFacing.WEST));
      }
    }

    result.addAll(joinedTableMap.values());
    //return result.size() < 2 ? Collections.emptyList() : result;
    return result;
  }
}
