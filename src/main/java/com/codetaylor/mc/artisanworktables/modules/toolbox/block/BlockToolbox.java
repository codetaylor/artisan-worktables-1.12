package com.codetaylor.mc.artisanworktables.modules.toolbox.block;

import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolboxConfig;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockToolbox
    extends BlockToolboxBase {

  public static final String NAME = "toolbox";

  public BlockToolbox() {

    super(Material.WOOD);
    this.setHardness(4);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileEntityToolbox();
  }

  @Override
  protected boolean keepContentsWhenBroken() {

    return ModuleToolboxConfig.TOOLBOX.KEEP_CONTENTS_WHEN_BROKEN;
  }

  protected ItemStack getBlockAsItemStack() {

    return new ItemStack(Item.getItemFromBlock(ModuleToolbox.Blocks.TOOLBOX), 1, 0);
  }

}
