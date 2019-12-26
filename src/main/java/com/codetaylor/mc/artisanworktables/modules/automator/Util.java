package com.codetaylor.mc.artisanworktables.modules.automator;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanIngredient;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public final class Util {

  public static void consumeIngredientsFor(
      List<IArtisanIngredient> recipeIngredients,
      List<IArtisanIngredient> recipeSecondaryIngredients,
      TileAutomator.InventoryItemStackHandler inventoryItemStackHandler
  ) {

    Util.consumeIngredientsFor(recipeIngredients, inventoryItemStackHandler);
    Util.consumeIngredientsFor(recipeSecondaryIngredients, inventoryItemStackHandler);
  }

  private static void consumeIngredientsFor(List<IArtisanIngredient> recipeIngredients, TileAutomator.InventoryItemStackHandler inventoryItemStackHandler) {

    for (IArtisanIngredient recipeIngredient : recipeIngredients) {
      int amount = recipeIngredient.getAmount();

      for (int i = 0; i < inventoryItemStackHandler.getSlots(); i++) {
        ItemStack itemStack = inventoryItemStackHandler.getStackInSlot(i);

        if (recipeIngredient.matchesIgnoreAmount(itemStack)) {

          if (itemStack.getCount() >= amount) {
            inventoryItemStackHandler.extractItem(i, amount, false);
            break;

          } else {
            inventoryItemStackHandler.setStackInSlot(i, ItemStack.EMPTY);
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

    int requiredFluidAmount = requiredFluid.amount;

    for (TileAutomator.FluidHandler handler : fluidHandler) {
      FluidStack availableFluid = handler.getFluid();

      if (availableFluid == null) {
        continue;
      }

      if (availableFluid.isFluidEqual(requiredFluid)) {

        if (availableFluid.amount >= requiredFluidAmount) {
          handler.drain(requiredFluidAmount, true);
          return true;

        } else {
          handler.drain(availableFluid.amount, true);
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
