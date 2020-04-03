package com.codetaylor.mc.artisanworktables.modules.automator;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanIngredient;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class Util {

  public static void consumeIngredientsFor(
      List<IArtisanIngredient> recipeIngredients,
      TileAutomator.InventoryItemStackHandler inventoryItemStackHandler,
      @Nullable IItemHandler itemHandler
  ) {

    for (int i = 0; i < recipeIngredients.size(); i++) {
      IArtisanIngredient recipeIngredient = recipeIngredients.get(i);
      int amount = recipeIngredient.getAmount();

      for (int j = 0; j < inventoryItemStackHandler.getSlots(); j++) {
        ItemStack itemStack = inventoryItemStackHandler.getStackInSlot(j);

        if (recipeIngredient.matchesIgnoreAmount(itemStack)) {

          if (itemStack.getCount() >= amount) {
            ItemStack extractItem = inventoryItemStackHandler.extractItem(j, amount, false);
            if (itemHandler != null) {
              itemHandler.insertItem(i, extractItem, false);
            }
            break;

          } else {
            inventoryItemStackHandler.setStackInSlot(j, ItemStack.EMPTY);
            amount -= itemStack.getCount();
            itemStack.setCount(0);
          }
        }
      }

    }
  }

  public static boolean hasIngredientsFor(
      List<IArtisanIngredient> recipeIngredients,
      List<IArtisanIngredient> recipeSecondaryIngredients,
      TileAutomator.InventoryItemStackHandler inventoryItemStackHandler
  ) {

    int inventorySlotCount = inventoryItemStackHandler.getSlots();
    List<ItemStack> inventoryCopy = new ArrayList<>(inventorySlotCount);

    for (int i = 0; i < inventorySlotCount; i++) {
      ItemStack itemStack = inventoryItemStackHandler.getStackInSlot(i);
      inventoryCopy.add(itemStack.copy());
    }

    return Util.hasIngredientsFor(recipeIngredients, inventoryCopy)
        && Util.hasIngredientsFor(recipeSecondaryIngredients, inventoryCopy);
  }

  private static boolean hasIngredientsFor(List<IArtisanIngredient> recipeIngredients, List<ItemStack> inventoryCopy) {

    for (IArtisanIngredient recipeIngredient : recipeIngredients) {

      if (recipeIngredient.isEmpty()) {
        continue;
      }

      int amount = recipeIngredient.getAmount();

      for (ItemStack itemStack : inventoryCopy) {

        if (recipeIngredient.matchesIgnoreAmount(itemStack)) {

          if (itemStack.getCount() >= amount) {
            itemStack.setCount(itemStack.getCount() - amount);
            amount = 0;
            break;

          } else {
            amount -= itemStack.getCount();
            itemStack.setCount(0);
          }
        }
      }

      if (amount != 0) {
        return false;
      }
    }

    return true;
  }

  public static boolean consumeFluidsFor(FluidStack requiredFluid, TileAutomator.FluidHandler[] fluidHandler) {

    if (requiredFluid == null
        || requiredFluid.amount == 0) {
      return true;
    }

    int requiredFluidAmount = requiredFluid.amount;

    for (TileAutomator.FluidHandler handler : fluidHandler) {
      FluidStack availableFluid = handler.getFluid();

      if (availableFluid == null) {
        continue;
      }

      if (availableFluid.isFluidEqual(requiredFluid)) {

        if (availableFluid.amount >= requiredFluidAmount) {
          handler.forceDrain(requiredFluidAmount);
          return true;

        } else {
          handler.forceDrain(availableFluid.amount);
          requiredFluidAmount -= availableFluid.amount;
        }
      }

      if (requiredFluidAmount == 0) {
        return true;
      }
    }

    return false;
  }

  public static boolean hasFluidsFor(FluidStack requiredFluid, TileAutomator.FluidHandler[] fluidHandler) {

    int requiredFluidAmount = requiredFluid.amount;

    for (TileAutomator.FluidHandler handler : fluidHandler) {
      FluidStack availableFluid = handler.getFluid();

      if (availableFluid == null) {
        continue;
      }

      if (availableFluid.isFluidEqual(requiredFluid)) {

        if (availableFluid.amount >= requiredFluidAmount) {
          return true;

        } else {
          requiredFluidAmount -= availableFluid.amount;
        }
      }

      if (requiredFluidAmount == 0) {
        return true;
      }
    }

    return false;
  }

  private Util() {
    //
  }
}
