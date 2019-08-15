package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.spi.packet.SPacketTileEntityBase;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CSPacketWorktableLockedModeToggle
    extends SPacketTileEntityBase<CSPacketWorktableLockedModeToggle> {

  @SuppressWarnings("unused")
  public CSPacketWorktableLockedModeToggle() {
    // Serialization
  }

  public CSPacketWorktableLockedModeToggle(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  public IMessage onMessage(
      CSPacketWorktableLockedModeToggle message,
      MessageContext ctx,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof TileEntityBase) {
      TileEntityBase table = (TileEntityBase) tileEntity;

      if (ModuleWorktablesConfig.allowSlotLockingForTier(table.getTier())) {
        table.setLocked(!table.isLocked());
        BlockHelper.notifyBlockUpdate(table.getWorld(), table.getPos());
      }
    }

    return null;
  }
}
