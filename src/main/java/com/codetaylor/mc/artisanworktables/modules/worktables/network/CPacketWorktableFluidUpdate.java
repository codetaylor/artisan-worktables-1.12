package com.codetaylor.mc.artisanworktables.modules.worktables.network;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class CPacketWorktableFluidUpdate
    extends CPacketTileEntityBase<CPacketWorktableFluidUpdate> {

  private FluidTank fluidTank;

  @SuppressWarnings("unused")
  public CPacketWorktableFluidUpdate() {
    // serialization
  }

  public CPacketWorktableFluidUpdate(BlockPos blockPos, FluidTank fluidTank) {

    super(blockPos);
    this.fluidTank = fluidTank;
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    super.fromBytes(buf);

    PacketBuffer packetBuffer = new PacketBuffer(buf);

    try {
      NBTTagCompound compound = packetBuffer.readCompoundTag();
      this.fluidTank = new FluidTank(0);

      if (compound != null) {
        this.fluidTank.readFromNBT(compound);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {

    super.toBytes(buf);

    PacketBuffer packetBuffer = new PacketBuffer(buf);

    packetBuffer.writeCompoundTag(this.fluidTank.writeToNBT(new NBTTagCompound()));
  }

  @Override
  public IMessage onMessage(
      CPacketWorktableFluidUpdate message,
      MessageContext ctx,
      TileEntity tileEntity
  ) {

    if (tileEntity != null) {
      TileEntityBase tileEntityBase = (TileEntityBase) tileEntity;
      tileEntityBase.getTank().setFluid(message.fluidTank.getFluid());
      tileEntityBase.triggerContainerRecipeUpdate();
    }

    return null;
  }
}
