package com.codetaylor.mc.artisanworktables.modules.worktables.block;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workshop.TileEntityWorkshop;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.workshop.TileEntityWorkshopMage;
import com.codetaylor.mc.athenaeum.spi.IBlockVariant;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockWorkshop
    extends BlockBase
    implements IBlockVariant<EnumType>,
    IModelRegistrationStrategyProvider {

  public static final String NAME = "workshop";

  public BlockWorkshop() {

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
      return new TileEntityWorkshopMage();
    }

    return new TileEntityWorkshop(type);
  }

}

























