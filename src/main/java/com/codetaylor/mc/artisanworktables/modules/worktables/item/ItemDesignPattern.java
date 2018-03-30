package com.codetaylor.mc.artisanworktables.modules.worktables.item;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemDesignPattern
    extends Item {

  public static final String NAME = "design_pattern";

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return super.getUnlocalizedName(stack) + "." + EnumType.fromMeta(stack.getMetadata()).getName();
  }

  @Override
  public EnumActionResult onItemUse(
      EntityPlayer player,
      World worldIn,
      BlockPos pos,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ
  ) {

    // TODO: shift-use to erase written pattern

    return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
  }

  public enum EnumType
      implements IVariant {

    BLANK(0, "blank"),
    WRITTEN(1, "written");

    private static final EnumType[] META_LOOKUP = Stream.of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .toArray(EnumType[]::new);
    private final int meta;

    private final String name;

    EnumType(int meta, String name) {

      this.meta = meta;
      this.name = name;
    }

    @Override
    public int getMeta() {

      return this.meta;
    }

    @Override
    public String getName() {

      return this.name;
    }

    public static EnumType fromMeta(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }
  }

  public static class MeshDefinition
      implements ItemMeshDefinition {

    private static final ModelResourceLocation MODEL_RESOURCE_LOCATION_BLANK;
    private static final ModelResourceLocation MODEL_RESOURCE_LOCATION_WRITTEN;

    static {
      MODEL_RESOURCE_LOCATION_BLANK = new ModelResourceLocation(new ResourceLocation(
          ModuleWorktables.MOD_ID,
          NAME + "_" + EnumType.BLANK.getName()
      ), "inventory");
      MODEL_RESOURCE_LOCATION_WRITTEN = new ModelResourceLocation(new ResourceLocation(
          ModuleWorktables.MOD_ID,
          NAME + "_" + EnumType.WRITTEN.getName()
      ), "inventory");
    }

    @Nonnull
    @Override
    public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {

      if (stack.hasTagCompound()) {
        return MODEL_RESOURCE_LOCATION_WRITTEN;
      }

      return MODEL_RESOURCE_LOCATION_BLANK;
    }
  }
}
