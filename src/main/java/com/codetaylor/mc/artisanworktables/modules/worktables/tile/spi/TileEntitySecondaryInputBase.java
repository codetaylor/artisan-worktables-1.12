package com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanItemStack;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingContext;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ISecondaryIngredientMatcher;
import com.codetaylor.mc.artisanworktables.api.recipe.ArtisanItemStack;
import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.CraftingContextFactory;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.SecondaryIngredientMatcher;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.MutuallyExclusiveStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class TileEntitySecondaryInputBase
    extends TileEntityBase {

  protected MutuallyExclusiveStackHandler secondaryIngredientHandler;

  protected TileEntitySecondaryInputBase() {
    // serialization
  }

  public TileEntitySecondaryInputBase(
      EnumType type
  ) {

    super(type);
  }

  @Override
  protected void initialize(EnumType type) {

    super.initialize(type);
    this.secondaryIngredientHandler = new MutuallyExclusiveStackHandler(this.getSecondaryInputSlotCount());
    this.secondaryIngredientHandler.addObserver((stackHandler, slotIndex) -> {
      this.markDirty();
      this.triggerContainerRecipeUpdate();
    });
  }

  public IItemHandler getSecondaryIngredientHandler() {

    return this.secondaryIngredientHandler;
  }

  @Override
  public boolean hasCapability(
      @Nonnull Capability<?> capability, @Nullable EnumFacing facing
  ) {

    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return true;
    }

    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      //noinspection unchecked
      return (T) this.secondaryIngredientHandler;
    }

    return super.getCapability(capability, facing);
  }

  @Override
  protected ICraftingContext getCraftingContext(EntityPlayer player) {

    return CraftingContextFactory.createContext(this, player, this.secondaryIngredientHandler);
  }

  @Override
  public ISecondaryIngredientMatcher getSecondaryIngredientMatcher() {

    int slotCount = this.secondaryIngredientHandler.getSlots();
    List<IArtisanItemStack> inputs = new ArrayList<>(slotCount);

    for (int i = 0; i < slotCount; i++) {
      ItemStack itemStack = this.secondaryIngredientHandler.getStackInSlot(i);
      inputs.add(ArtisanItemStack.from(itemStack));
    }

    return new SecondaryIngredientMatcher(inputs);
  }

  @Override
  public List<ItemStack> getBlockBreakDrops() {

    List<ItemStack> drops = super.getBlockBreakDrops();

    for (int i = 0; i < this.secondaryIngredientHandler.getSlots(); i++) {
      drops.add(this.secondaryIngredientHandler.getStackInSlot(i));
    }

    return drops;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {

    super.readFromNBT(tag);
    this.secondaryIngredientHandler.deserializeNBT(tag.getCompoundTag("secondaryIngredientHandler"));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {

    tag = super.writeToNBT(tag);
    tag.setTag("secondaryIngredientHandler", this.secondaryIngredientHandler.serializeNBT());
    return tag;
  }

  protected abstract int getSecondaryInputSlotCount();
}
