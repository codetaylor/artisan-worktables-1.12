package com.codetaylor.mc.artisanworktables.modules.worktables.item;

import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

public class ItemWorktable
    extends ItemMultiTexture {

  public ItemWorktable(
      Block block
  ) {

    super(block, block, ((BlockWorktable) block)::getModelName);
  }

  @Override
  public int getItemBurnTime(ItemStack itemStack) {

    return 0;
  }
}
