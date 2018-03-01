package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent from the client to the server to signal a fluid destroy.
 */
public class SPacketWorktableTankDestroyFluid
    extends SPacketTileEntityBase<SPacketWorktableTankDestroyFluid> {

  @SuppressWarnings("unused")
  public SPacketWorktableTankDestroyFluid() {
    // Serialization
  }

  public SPacketWorktableTankDestroyFluid(BlockPos blockPos) {

    super(blockPos);
  }

  @Override
  public IMessage onMessage(
      SPacketWorktableTankDestroyFluid message,
      MessageContext ctx,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof TileEntityBase) {
      FluidTank tank = ((TileEntityBase) tileEntity).getTank();
      tank.drain(tank.getCapacity(), true);
      ModuleWorktables.PACKET_SERVICE.sendToAllAround(
          new CPacketWorktableFluidUpdate(tileEntity.getPos(), tank),
          tileEntity
      );
    }

    return null;
  }
}
