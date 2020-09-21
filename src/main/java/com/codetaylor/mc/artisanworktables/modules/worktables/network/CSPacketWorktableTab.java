package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.Util;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.spi.packet.SPacketTileEntityBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from the client to the server to signal a worktable tab change.
 */
public class CSPacketWorktableTab
    extends SPacketTileEntityBase<CSPacketWorktableTab> {

  @SuppressWarnings("unused")
  public CSPacketWorktableTab() {
    // Serialization
  }

  public CSPacketWorktableTab(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  public IMessage onMessage(
      CSPacketWorktableTab message,
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

      if (!ModuleWorktablesConfig.PREVENT_CONCURRENT_USAGE
          || !Util.anyPlayerHasContainerOpen((WorldServer) player.world, message.blockPos)) {
        player.openGui(
            ModArtisanWorktables.INSTANCE,
            1,
            player.getEntityWorld(),
            message.blockPos.getX(),
            message.blockPos.getY(),
            message.blockPos.getZ()
        );
      }
    }

    if (!heldStack.isEmpty()) {
      player.inventory.setItemStack(heldStack);
      serverHandler.sendPacket(new SPacketSetSlot(-1, -1, heldStack));
    }

    return null;
  }
}
