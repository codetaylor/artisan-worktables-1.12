package com.codetaylor.mc.artisanworktables.modules.automator.tile;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.athenaeum.tile.IContainerProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileAutomator
    extends TileEntity
    implements IContainerProvider<AutomatorContainer, AutomatorGuiContainer> {

  // ---------------------------------------------------------------------------
  // - IContainerProvider
  // ---------------------------------------------------------------------------

  @Override
  public AutomatorContainer getContainer(InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos) {

    return new AutomatorContainer(inventoryPlayer, world, this);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public AutomatorGuiContainer getGuiContainer(InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos) {

    return new AutomatorGuiContainer(this.getContainer(inventoryPlayer, world, state, pos), 176, 190);
  }
}
