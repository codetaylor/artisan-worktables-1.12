package com.codetaylor.mc.artisanworktables.modules.automator.block;

import com.codetaylor.mc.artisanworktables.lib.BlockPartialBase;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.ITileAutomatorPower;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import com.codetaylor.mc.athenaeum.util.AABBHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockAutomator
    extends BlockPartialBase {

  public static final String NAME = "automator";

  private static final AxisAlignedBB AABB = AABBHelper.create(2, 0, 2, 14, 16, 14);

  public BlockAutomator() {

    super(Material.ROCK);
    this.setSoundType(SoundType.GLASS);
    this.setHarvestLevel("pickaxe", 0);
  }

  // --------------------------------------------------------------------------
  // - Placement
  // --------------------------------------------------------------------------

  @Override
  public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {

    if (!(world.getTileEntity(pos.down()) instanceof ITileAutomatorPower)) {
      return false;
    }

    return super.canPlaceBlockAt(world, pos);
  }

  // --------------------------------------------------------------------------
  // - Display
  // --------------------------------------------------------------------------

  @Nonnull
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.CUTOUT;
  }

  // --------------------------------------------------------------------------
  // - Collision
  // --------------------------------------------------------------------------

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

    return AABB;
  }

  // --------------------------------------------------------------------------
  // - Tile
  // --------------------------------------------------------------------------

  @Override
  public boolean hasTileEntity(IBlockState state) {

    return (state.getBlock() == this);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    return new TileAutomator();
  }
}
