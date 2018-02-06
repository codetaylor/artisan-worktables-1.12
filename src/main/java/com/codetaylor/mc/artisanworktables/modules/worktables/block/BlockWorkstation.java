package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workstation.*;
import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockWorkstation
    extends BlockBase
    implements IBlockVariant<EnumType>,
    IModelRegistrationStrategyProvider {

  public static final String NAME = "workstation";

  public BlockWorkstation() {

    super(Material.ROCK);

    this.setHardness(5);
    this.setDefaultState(this.blockState.getBaseState()
        .withProperty(VARIANT, EnumType.TAILOR)
        .withProperty(ACTIVE, false));
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {

    EnumType type = state.getValue(VARIANT);

    switch (type) {
      case BASIC:
        return new TileEntityWorkstationBasic();
      case BLACKSMITH:
        return new TileEntityWorkstationBlacksmith();
      case CARPENTER:
        return new TileEntityWorkstationCarpenter();
      case CHEMIST:
        return new TileEntityWorkstationChemist();
      case ENGINEER:
        return new TileEntityWorkstationEngineer();
      case JEWELER:
        return new TileEntityWorkstationJeweler();
      case MAGE:
        return new TileEntityWorkstationMage();
      case MASON:
        return new TileEntityWorkstationMason();
      case SCRIBE:
        return new TileEntityWorkstationScribe();
      case TAILOR:
        return new TileEntityWorkstationTailor();
      default:
        throw new IllegalStateException("Unknown type: " + type);
    }
  }

}

























