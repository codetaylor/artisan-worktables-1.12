package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class CPacketTileEntityBase<REQ extends CPacketTileEntityBase>
    extends PacketBlockPosBase<REQ> {

  public CPacketTileEntityBase() {
    // serialization
  }

  public CPacketTileEntityBase(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  public IMessage onMessage(
      REQ message,
      MessageContext ctx
  ) {

    EntityPlayerSP player = Minecraft.getMinecraft().player;
    TileEntity tileEntity = player.getEntityWorld().getTileEntity(message.blockPos);

    return this.onMessage(message, ctx, tileEntity);
  }

  protected abstract IMessage onMessage(
      REQ message,
      MessageContext ctx,
      TileEntity tileEntity
  );
}
