package com.codetaylor.mc.artisanworktables.modules.automator.network;

import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import com.codetaylor.mc.athenaeum.spi.packet.SPacketTileEntityBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CSPacketAutomatorInventoryLockModeChange
    extends SPacketTileEntityBase<CSPacketAutomatorInventoryLockModeChange> {

  @SuppressWarnings("unused")
  public CSPacketAutomatorInventoryLockModeChange() {
    // serialization
  }

  public CSPacketAutomatorInventoryLockModeChange(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  protected IMessage onMessage(
      CSPacketAutomatorInventoryLockModeChange message,
      MessageContext ctx,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof TileAutomator) {
      TileAutomator tileAutomator = (TileAutomator) tileEntity;
      tileAutomator.setInventoryLocked(!tileAutomator.isInventoryLocked());
      System.out.println("Inventory locked: " + tileAutomator.isInventoryLocked());

      TileAutomator.InventoryGhostItemStackHandler ghostHandler;
      ghostHandler = tileAutomator.getInventoryGhostItemStackHandler();

      if (tileAutomator.isInventoryLocked()) {
        TileAutomator.InventoryItemStackHandler handler;
        handler = tileAutomator.getInventoryItemStackHandler();

        for (int i = 0; i < handler.getSlots(); i++) {
          ItemStack stackInSlot = handler.getStackInSlot(i).copy();

          if (!stackInSlot.isEmpty()) {
            stackInSlot.setCount(1);
            ghostHandler.setStackInSlot(i, stackInSlot);
          }
        }

      } else {

        for (int i = 0; i < ghostHandler.getSlots(); i++) {
          ghostHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
      }
    }

    return null;
  }
}
