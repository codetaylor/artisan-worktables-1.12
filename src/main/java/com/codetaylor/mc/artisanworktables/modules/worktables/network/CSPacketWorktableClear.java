package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntitySecondaryInputBase;
import com.codetaylor.mc.athenaeum.spi.packet.SPacketTileEntityBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Sent from the client to the server to signal a fluid destroy.
 */
public class CSPacketWorktableClear
    extends SPacketTileEntityBase<CSPacketWorktableClear> {

  public static final int CLEAR_FLUID = 1;
  public static final int CLEAR_GRID = 2;
  public static final int CLEAR_TOOLS = 4;
  public static final int CLEAR_OUTPUT = 8;
  public static final int CLEAR_EXTRA_OUTPUT = 16;
  public static final int CLEAR_SECONDARY_INPUT = 32;

  public static final int CLEAR_ALL = CLEAR_FLUID | CLEAR_GRID | CLEAR_TOOLS | CLEAR_OUTPUT | CLEAR_EXTRA_OUTPUT | CLEAR_SECONDARY_INPUT;

  private int clearFlags;

  @SuppressWarnings("unused")
  public CSPacketWorktableClear() {
    // Serialization
  }

  public CSPacketWorktableClear(BlockPos blockPos, int clearFlags) {

    super(blockPos);
    this.clearFlags = clearFlags;
  }

  @Override
  public void toBytes(ByteBuf buf) {

    super.toBytes(buf);
    buf.writeInt(this.clearFlags);
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    super.fromBytes(buf);
    this.clearFlags = buf.readInt();
  }

  @Override
  public IMessage onMessage(
      CSPacketWorktableClear message,
      MessageContext ctx,
      TileEntity tileEntity
  ) {

    if (tileEntity instanceof TileEntityBase) {
      TileEntityBase table = (TileEntityBase) tileEntity;
      CSPacketWorktableClear.clear(table, message.clearFlags);
    }

    return null;
  }

  public static void clear(TileEntityBase table, int clearFlags) {

    if ((clearFlags & CLEAR_FLUID) == CLEAR_FLUID) {
      FluidTank tank = table.getTank();
      tank.drain(tank.getCapacity(), true);
      ModuleWorktables.PACKET_SERVICE.sendToAllAround(
          new SCPacketWorktableFluidUpdate(table.getPos(), tank),
          table
      );
    }

    if ((clearFlags & CLEAR_GRID) == CLEAR_GRID) {
      ICraftingMatrixStackHandler craftingMatrixHandler = table.getCraftingMatrixHandler();

      for (int i = 0; i < craftingMatrixHandler.getSlots(); i++) {
        craftingMatrixHandler.setStackInSlot(i, ItemStack.EMPTY);
      }
    }

    if ((clearFlags & CLEAR_OUTPUT) == CLEAR_OUTPUT) {
      ItemStackHandler resultHandler = table.getResultHandler();
      resultHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

    if ((clearFlags & CLEAR_TOOLS) == CLEAR_TOOLS) {
      ItemStackHandler toolHandler = table.getToolHandler();

      for (int i = 0; i < toolHandler.getSlots(); i++) {
        toolHandler.setStackInSlot(i, ItemStack.EMPTY);
      }
    }

    if ((clearFlags & CLEAR_EXTRA_OUTPUT) == CLEAR_EXTRA_OUTPUT) {
      ItemStackHandler secondaryOutputHandler = table.getSecondaryOutputHandler();

      for (int i = 0; i < secondaryOutputHandler.getSlots(); i++) {
        secondaryOutputHandler.setStackInSlot(i, ItemStack.EMPTY);
      }
    }

    if (table instanceof TileEntitySecondaryInputBase
        && (clearFlags & CLEAR_SECONDARY_INPUT) == CLEAR_SECONDARY_INPUT) {
      IItemHandlerModifiable secondaryIngredientHandler = ((TileEntitySecondaryInputBase) table).getSecondaryIngredientHandler();

      for (int i = 0; i < secondaryIngredientHandler.getSlots(); i++) {
        secondaryIngredientHandler.setStackInSlot(i, ItemStack.EMPTY);
      }
    }
  }
}
