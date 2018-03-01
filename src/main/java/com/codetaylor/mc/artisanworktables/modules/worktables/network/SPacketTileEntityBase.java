package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class SPacketTileEntityBase<REQ extends SPacketTileEntityBase>
    extends PacketBlockPosBase<REQ> {

  public SPacketTileEntityBase() {
    // serialization
  }

  public SPacketTileEntityBase(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  public IMessage onMessage(
      REQ message,
      MessageContext ctx
  ) {

    NetHandlerPlayServer serverHandler = ctx.getServerHandler();
    EntityPlayerMP player = serverHandler.player;
    TileEntity tileEntity = player.getEntityWorld().getTileEntity(message.blockPos);

    return this.onMessage(message, ctx, tileEntity);
  }

  protected abstract IMessage onMessage(
      REQ message,
      MessageContext ctx,
      TileEntity tileEntity
  );
}
