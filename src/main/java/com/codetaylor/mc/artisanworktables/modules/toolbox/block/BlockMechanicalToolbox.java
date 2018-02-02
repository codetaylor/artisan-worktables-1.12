package com.codetaylor.mc.artisanworktables.modules.toolbox.block;

import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolboxConfig;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityMechanicalToolbox;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMechanicalToolbox
    extends BlockToolboxBase {

  public static final String NAME = "mechanical_toolbox";

  public BlockMechanicalToolbox() {

    super(Material.WOOD);
    this.setHardness(4);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {

    return new TileEntityMechanicalToolbox();
  }

  @Override
  protected boolean keepContentsWhenBroken() {

    return ModuleToolboxConfig.MECHANICAL_TOOLBOX.KEEP_CONTENTS_WHEN_BROKEN;
  }

  protected ItemStack getBlockAsItemStack() {

    return new ItemStack(Item.getItemFromBlock(ModuleToolbox.Blocks.MECHANICAL_TOOLBOX), 1, 0);
  }

}
