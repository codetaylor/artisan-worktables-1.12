package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockTabIcon
    extends Block {

  public static final String NAME = "tab_icon";

  public BlockTabIcon() {

    super(Material.WOOD);
  }

  @Override
  public CreativeTabs getCreativeTabToDisplayOn() {

    return null;
  }
}
