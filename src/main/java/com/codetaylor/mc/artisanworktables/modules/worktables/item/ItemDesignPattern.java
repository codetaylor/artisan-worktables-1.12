package com.codetaylor.mc.artisanworktables.modules.worktables.item;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.stream.Stream;

public class ItemDesignPattern
    extends Item {

  public static final String NAME = "design_pattern";

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    if (stack.hasTagCompound()) {
      return super.getUnlocalizedName(stack) + "." + EnumType.WRITTEN.getName();
    }

    return super.getUnlocalizedName(stack) + "." + EnumType.BLANK.getName();
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    if (!world.isRemote && player.isSneaking() && ModuleWorktablesConfig.PATTERN.ENABLE_SNEAK_CLICK_TO_CLEAR) {
      return new ActionResult<>(
          EnumActionResult.SUCCESS,
          new ItemStack(ModuleWorktables.Items.DESIGN_PATTERN, player.getHeldItem(hand).getCount())
      );
    }

    return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
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

}
