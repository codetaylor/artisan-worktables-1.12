package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityWorktableFluidBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from the client to the server to signal a fluid destroy.
 */
public class SPacketWorktableTankDestroyFluid
    implements IMessage,
    IMessageHandler<SPacketWorktableTankDestroyFluid, IMessage> {

  private int posX;
  private int posY;
  private int posZ;

  @SuppressWarnings("unused")
  public SPacketWorktableTankDestroyFluid() {
    // Serialization
  }

  public SPacketWorktableTankDestroyFluid(int posX, int posY, int posZ) {

    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    this.posX = buf.readInt();
    this.posY = buf.readInt();
    this.posZ = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {

    buf.writeInt(this.posX);
    buf.writeInt(this.posY);
    buf.writeInt(this.posZ);
  }

  @Override
  public IMessage onMessage(
      SPacketWorktableTankDestroyFluid message, MessageContext ctx
  ) {

    NetHandlerPlayServer serverHandler = ctx.getServerHandler();
    EntityPlayerMP player = serverHandler.player;
    BlockPos pos = new BlockPos(message.posX, message.posY, message.posZ);
    TileEntity tileEntity = player.getEntityWorld().getTileEntity(pos);

    if (tileEntity instanceof TileEntityWorktableFluidBase) {
      FluidTank tank = ((TileEntityWorktableFluidBase) tileEntity).getTank();
      tank.drain(tank.getCapacity(), true);
      ((TileEntityWorktableFluidBase) tileEntity).notifyBlockUpdate();
    }

    return null;
  }
}
