package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.AWContainer;
import com.codetaylor.mc.athenaeum.spi.packet.PacketBlockPosBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SCPacketWorktableContainerJoinedBlockBreak
    extends PacketBlockPosBase<SCPacketWorktableContainerJoinedBlockBreak> {

  @SuppressWarnings("unused")
  public SCPacketWorktableContainerJoinedBlockBreak() {
    // serialization
  }

  public SCPacketWorktableContainerJoinedBlockBreak(BlockPos blockPos) {

    super(blockPos);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(
      SCPacketWorktableContainerJoinedBlockBreak message, MessageContext ctx
  ) {

    Minecraft minecraft = Minecraft.getMinecraft();
    EntityPlayerSP player = minecraft.player;
    WorldClient world = minecraft.world;

    if (player.openContainer instanceof AWContainer) {
      ((AWContainer) player.openContainer).onJoinedBlockBreak(world, message.blockPos);
    }

    return null;
  }
}
