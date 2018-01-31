package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityWorktableBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from the client to the server to signal a worktable tab change.
 */
public class SPacketWorktableTab
    implements IMessage,
    IMessageHandler<SPacketWorktableTab, IMessage> {

  private int posX;
  private int posY;
  private int posZ;

  @SuppressWarnings("unused")
  public SPacketWorktableTab() {
    // Serialization
  }

  public SPacketWorktableTab(int posX, int posY, int posZ) {

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
      SPacketWorktableTab message, MessageContext ctx
  ) {

    // Reference:
    // https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/slimeknights/tconstruct/tools/common/network/TinkerStationTabPacket.java

    NetHandlerPlayServer serverHandler = ctx.getServerHandler();
    EntityPlayerMP player = serverHandler.player;
    ItemStack heldStack = player.inventory.getItemStack();

    if (!heldStack.isEmpty()) {
      player.inventory.setItemStack(ItemStack.EMPTY);
    }

    BlockPos pos = new BlockPos(message.posX, message.posY, message.posZ);
    TileEntity tileEntity = player.getEntityWorld().getTileEntity(pos);

    if (tileEntity instanceof TileEntityWorktableBase) {
      player.openGui(
          ModuleWorktables.MOD_INSTANCE,
          1,
          player.getEntityWorld(),
          message.posX,
          message.posY,
          message.posZ
      );
    }

    if (!heldStack.isEmpty()) {
      player.inventory.setItemStack(heldStack);
      serverHandler.sendPacket(new SPacketSetSlot(-1, -1, heldStack));
    }

    return null;
  }
}
