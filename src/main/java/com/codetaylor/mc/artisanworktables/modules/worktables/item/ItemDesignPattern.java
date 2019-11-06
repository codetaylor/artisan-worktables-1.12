package com.codetaylor.mc.artisanworktables.modules.worktables.item;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess")
public class ItemDesignPattern
    extends Item {

  public static final String NAME = "design_pattern";
  public static final String NAME_BLANK = "blank";
  public static final String NAME_WRITTEN = "written";

  @Nonnull
  @Override
  public String getUnlocalizedName(ItemStack stack) {

    // TODO: fix this
    if (stack.hasTagCompound()) {
      return super.getUnlocalizedName(stack) + "." + NAME_WRITTEN;
    }

    return super.getUnlocalizedName(stack) + "." + NAME_BLANK;
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
}
