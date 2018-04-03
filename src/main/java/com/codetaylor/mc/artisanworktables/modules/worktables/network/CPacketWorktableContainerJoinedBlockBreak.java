package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.Container;
import com.codetaylor.mc.athenaeum.spi.packet.PacketBlockPosBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketWorktableContainerJoinedBlockBreak
    extends PacketBlockPosBase<CPacketWorktableContainerJoinedBlockBreak> {

  @SuppressWarnings("unused")
  public CPacketWorktableContainerJoinedBlockBreak() {
    // serialization
  }

  public CPacketWorktableContainerJoinedBlockBreak(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  public IMessage onMessage(
      CPacketWorktableContainerJoinedBlockBreak message, MessageContext ctx
  ) {

    Minecraft minecraft = Minecraft.getMinecraft();
    EntityPlayerSP player = minecraft.player;
    WorldClient world = minecraft.world;

    if (player.openContainer instanceof Container) {
      ((Container) player.openContainer).onJoinedBlockBreak(world, message.blockPos);
    }

    return null;
  }
}
