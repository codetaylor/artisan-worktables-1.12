package com.codetaylor.mc.artisanworktables.modules.automator.gui;

import com.codetaylor.mc.athenaeum.gui.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class AutomatorContainer
    extends ContainerBase {

  private final World world;
  private final TileEntity tile;

  public AutomatorContainer(
      InventoryPlayer inventoryPlayer,
      World world,
      TileEntity tile
  ) {

    super(inventoryPlayer);
    this.world = world;
    this.tile = tile;
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {

    return player.getDistanceSq((double) this.tile.getPos().getX() + 0.5D, (double) this.tile.getPos().getY() + 0.5D, (double) this.tile.getPos().getZ() + 0.5D) <= 64.0D;
  }
}
