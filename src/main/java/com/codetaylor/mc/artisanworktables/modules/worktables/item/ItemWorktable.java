package com.codetaylor.mc.artisanworktables.modules.worktables.item;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktable;
import com.codetaylor.mc.athenaeum.helper.BlockRegistrationHelper;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;

public class ItemWorktable
    extends ItemMultiTexture {

  public ItemWorktable(
      BlockWorktable block
  ) {

    super(block, block, block::getModelName);
    BlockRegistrationHelper.setItemBlockRegistryName(block, this);
    this.setCreativeTab(ModuleWorktables.CREATIVE_TAB);
  }

  @Override
  public int getItemBurnTime(ItemStack itemStack) {

    return 0;
  }
}
