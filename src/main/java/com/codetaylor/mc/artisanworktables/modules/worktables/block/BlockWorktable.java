package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.worktable.TileEntityWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.worktable.TileEntityWorktableMage;
import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockWorktable
    extends BlockBase
    implements IBlockVariant<EnumType>,
    IModelRegistrationStrategyProvider {

  public static final String NAME = "worktable";

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.937, 1);

  public BlockWorktable() {

    super(Material.ROCK);

    this.setHardness(5);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(VARIANT, EnumType.TAILOR)
        .withProperty(ACTIVE, false));
  }

  @Override
  public AxisAlignedBB getBoundingBox(
      IBlockState state, IBlockAccess source, BlockPos pos
  ) {

    return AABB;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    EnumType type = state.getValue(VARIANT);

    if (type == EnumType.MAGE) {
      return new TileEntityWorktableMage();
    }

    return new TileEntityWorktable(type);
  }

  @Override
  public boolean isTopSolid(IBlockState state) {

    return false;
  }

  @Override
  public BlockFaceShape getBlockFaceShape(
      IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face
  ) {

    if (face == EnumFacing.UP) {
      return BlockFaceShape.UNDEFINED;
    }

    return super.getBlockFaceShape(worldIn, state, pos, face);
  }
}

























