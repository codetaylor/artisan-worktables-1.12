package com.codetaylor.mc.artisanworktables.modules.automator.network;

import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import com.codetaylor.mc.athenaeum.spi.packet.CPacketTileEntityBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CSPacketAutomatorClearInventoryGhostSlot
    extends CPacketTileEntityBase<CSPacketAutomatorClearInventoryGhostSlot> {

  private int slotIndex;

  @SuppressWarnings("unused")
  public CSPacketAutomatorClearInventoryGhostSlot() {
    // serialization
  }

  public CSPacketAutomatorClearInventoryGhostSlot(BlockPos pos, int slotIndex) {

    super(pos);
    this.slotIndex = slotIndex;
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    super.fromBytes(buf);
    this.slotIndex = buf.readByte();
  }

  @Override
  public void toBytes(ByteBuf buf) {

    super.toBytes(buf);
    buf.writeByte(this.slotIndex);
  }

  @Override
  protected IMessage onMessage(
      CSPacketAutomatorClearInventoryGhostSlot message,
      MessageContext ctx,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof TileAutomator) {
      ((TileAutomator) tileEntity).getInventoryGhostItemStackHandler()
          .setStackInSlot(message.slotIndex, ItemStack.EMPTY);
    }

    return null;
  }
}
