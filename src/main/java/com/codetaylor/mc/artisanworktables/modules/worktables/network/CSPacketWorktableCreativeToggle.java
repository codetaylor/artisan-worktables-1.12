package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.spi.packet.SPacketTileEntityBase;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CSPacketWorktableCreativeToggle
    extends SPacketTileEntityBase<CSPacketWorktableCreativeToggle> {

  @SuppressWarnings("unused")
  public CSPacketWorktableCreativeToggle() {
    // Serialization
  }

  public CSPacketWorktableCreativeToggle(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  public IMessage onMessage(
      CSPacketWorktableCreativeToggle message,
      MessageContext ctx,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof TileEntityBase) {
      TileEntityBase table = (TileEntityBase) tileEntity;
      table.setCreative(!table.isCreative());
      table.setLocked(false);
      CSPacketWorktableClear.clear(table, CSPacketWorktableClear.CLEAR_ALL);
      BlockHelper.notifyBlockUpdate(table.getWorld(), table.getPos());
    }

    return null;
  }
}
