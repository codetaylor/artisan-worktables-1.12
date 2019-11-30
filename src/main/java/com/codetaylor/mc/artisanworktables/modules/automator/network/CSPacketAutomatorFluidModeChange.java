package com.codetaylor.mc.artisanworktables.modules.automator.network;

import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import com.codetaylor.mc.athenaeum.spi.packet.SPacketTileEntityBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CSPacketAutomatorFluidModeChange
    extends SPacketTileEntityBase<CSPacketAutomatorFluidModeChange> {

  private int slotIndex;

  @SuppressWarnings("unused")
  public CSPacketAutomatorFluidModeChange() {
    // serialization
  }

  public CSPacketAutomatorFluidModeChange(BlockPos pos, int slotIndex) {

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
      CSPacketAutomatorFluidModeChange message,
      MessageContext ctx,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof TileAutomator) {
      ((TileAutomator) tileEntity).cycleFluidMode(message.slotIndex);
    }

    return null;
  }
}
