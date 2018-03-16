package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workstation.TileEntityWorkstation;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workstation.TileEntityWorkstationMage;
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

    if (type == EnumType.MAGE) {
      return new TileEntityWorkstationMage();
    }

    return new TileEntityWorkstation(type);
  }

}

























