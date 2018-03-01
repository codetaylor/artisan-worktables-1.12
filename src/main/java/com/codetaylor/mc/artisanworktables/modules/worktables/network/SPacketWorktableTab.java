package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from the client to the server to signal a worktable tab change.
 */
public class SPacketWorktableTab
    extends SPacketTileEntityBase<SPacketWorktableTab> {

  @SuppressWarnings("unused")
  public SPacketWorktableTab() {
    // Serialization
  }

  public SPacketWorktableTab(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  public IMessage onMessage(
      SPacketWorktableTab message,
      MessageContext ctx,
      TileEntity tileEntity
  ) {

    // Reference:
    // https://github.com/SlimeKnights/TinkersConstruct/blob/master/src/main/java/slimeknights/tconstruct/tools/common/network/TinkerStationTabPacket.java

    NetHandlerPlayServer serverHandler = ctx.getServerHandler();
    EntityPlayerMP player = serverHandler.player;
    ItemStack heldStack = player.inventory.getItemStack();

    if (!heldStack.isEmpty()) {
      player.inventory.setItemStack(ItemStack.EMPTY);
    }

    if (tileEntity instanceof TileEntityBase) {
      player.openGui(
          ModuleWorktables.MOD_INSTANCE,
          1,
          player.getEntityWorld(),
          message.blockPos.getX(),
          message.blockPos.getY(),
          message.blockPos.getZ()
      );
    }

    if (!heldStack.isEmpty()) {
      player.inventory.setItemStack(heldStack);
      serverHandler.sendPacket(new SPacketSetSlot(-1, -1, heldStack));
    }

    return null;
  }
}
