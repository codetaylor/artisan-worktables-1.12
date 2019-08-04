package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.spi.packet.SPacketTileEntityBase;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from the client to the server to signal a fluid destroy.
 */
public class CSPacketWorktableCreateModeToggle
    extends SPacketTileEntityBase<CSPacketWorktableCreateModeToggle> {

  @SuppressWarnings("unused")
  public CSPacketWorktableCreateModeToggle() {
    // Serialization
  }

  public CSPacketWorktableCreateModeToggle(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  public IMessage onMessage(
      CSPacketWorktableCreateModeToggle message,
      MessageContext ctx,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof TileEntityBase) {
      TileEntityBase table = (TileEntityBase) tileEntity;
      table.setCreative(!table.isCreative());
      CSPacketWorktableClear.clear(table, CSPacketWorktableClear.CLEAR_ALL);
      BlockHelper.notifyBlockUpdate(table.getWorld(), table.getPos());
    }

    return null;
  }
}
