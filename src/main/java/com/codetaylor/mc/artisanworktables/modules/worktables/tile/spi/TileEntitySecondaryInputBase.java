package com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi;

import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.IAWRecipe;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.ISecondaryIngredientMatcher;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.SecondaryIngredientMatcher;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public abstract class TileEntitySecondaryInputBase
    extends TileEntityBase {

  protected ObservableStackHandler secondaryIngredientHandler;

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
    this.secondaryIngredientHandler = new ObservableStackHandler(this.getSecondaryInputSlotCount());
    this.secondaryIngredientHandler.addObserver((stackHandler, slotIndex) -> {
      this.markDirty();
      this.triggerContainerRecipeUpdate();
    });
  }

  public ObservableStackHandler getSecondaryIngredientHandler() {

    return this.secondaryIngredientHandler;
  }

  @Override
  protected void onCraftReduceIngredients(IAWRecipe recipe) {

    super.onCraftReduceIngredients(recipe);

    List<IIngredient> secondaryIngredients = recipe.getSecondaryIngredients();

    if (!secondaryIngredients.isEmpty()) {
      // reduce secondary ingredients

      for (IIngredient requiredIngredient : secondaryIngredients) {
        int requiredAmount = requiredIngredient.getAmount();

        // Set the amount to 1 to avoid quantity discrepancies when matching
        requiredIngredient = requiredIngredient.amount(1);

        int slotCount = this.secondaryIngredientHandler.getSlots();

        for (int i = 0; i < slotCount; i++) {
          ItemStack stackInSlot = this.secondaryIngredientHandler.getStackInSlot(i);
          IItemStack iItemStack = CTInputHelper.toIItemStack(stackInSlot);

          if (stackInSlot.isEmpty() || iItemStack == null) {
            continue;
          }

          if (requiredIngredient.matches(iItemStack.anyAmount())) {

            if (stackInSlot.getCount() <= requiredAmount) {
              requiredAmount -= stackInSlot.getCount();
              this.secondaryIngredientHandler.setStackInSlot(i, ItemStack.EMPTY);

            } else if (stackInSlot.getCount() > requiredAmount) {
              this.secondaryIngredientHandler.setStackInSlot(
                  i,
                  StackHelper.decrease(stackInSlot.copy(), requiredAmount, false)
              );
              requiredAmount = 0;
            }

            if (requiredAmount == 0) {
              break;
            }
          }
        }

        if (requiredAmount > 0) {
          // TODO: failed to find all required ingredients... shouldn't happen if the matching code is correct
        }

      }
    }
  }

  @Override
  public ISecondaryIngredientMatcher getSecondaryIngredientMatcher() {

    int slotCount = this.secondaryIngredientHandler.getSlots();
    List<IItemStack> inputs = new ArrayList<>(slotCount);

    for (int i = 0; i < slotCount; i++) {
      ItemStack itemStack = this.secondaryIngredientHandler.getStackInSlot(i);
      inputs.add(CTInputHelper.toIItemStack(itemStack));
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
