package com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IRecipe;
import com.codetaylor.mc.athenaeum.util.BlockHelper;
import com.codetaylor.mc.athenaeum.util.BottleHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityFluidBase
    extends TileEntityBase {

  private FluidTank tank;

  protected TileEntityFluidBase() {
    // serialization
  }

  public TileEntityFluidBase(int width, int height, int fluidCapacity) {

    super(width, height);
    this.tank = new FluidTank(fluidCapacity);
  }

  public FluidTank getTank() {

    return this.tank;
  }

  @Override
  public boolean hasCapability(
      @Nonnull Capability<?> capability, @Nullable EnumFacing facing
  ) {

    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      //noinspection unchecked
      return (T) this.tank;
    }

    return super.getCapability(capability, facing);
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {

    super.readFromNBT(tag);
    this.tank.readFromNBT(tag.getCompoundTag("tank"));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {

    tag = super.writeToNBT(tag);
    tag.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
    return tag;
  }

  @Override
  public boolean onBlockActivated(
      World worldIn,
      BlockPos pos,
      IBlockState state,
      EntityPlayer playerIn,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ
  ) {

    if (BottleHelper.drainWaterFromBottle(this, playerIn, this.tank)) {
      return true;
    }

    if (BottleHelper.drainWaterIntoBottle(this, playerIn, this.tank)) {
      return true;
    }

    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
    boolean result = false;

    if (fluidHandler != null) {
      result = FluidUtil.interactWithFluidHandler(playerIn, hand, fluidHandler);
    }

    if (result) {
      BlockHelper.notifyBlockUpdate(this.getWorld(), this.getPos());
      return true;
    }

    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  protected void onCraftReduceIngredients(IRecipe recipe) {

    super.onCraftReduceIngredients(recipe);

    if (recipe != null) {
      FluidStack fluidIngredient = recipe.getFluidIngredient();

      if (fluidIngredient != null) {
        this.tank.drain(fluidIngredient, true);
      }
    }
  }

}
