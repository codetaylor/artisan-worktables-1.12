package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingMatrixStackHandler;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.recipes.ICraftingInventory;

public class CTArtisanCraftingInventory
    implements ICraftingInventory {

  private final IPlayer player;
  private final ICraftingMatrixStackHandler craftingMatrixStackHandler;

  /* packagte */ CTArtisanCraftingInventory(IPlayer player, ICraftingMatrixStackHandler craftingMatrixStackHandler) {

    this.player = player;
    this.craftingMatrixStackHandler = craftingMatrixStackHandler;
  }

  @Override
  public IPlayer getPlayer() {

    return this.player;
  }

  @Override
  public int getSize() {

    return this.craftingMatrixStackHandler.getSlots();
  }

  @Override
  public int getWidth() {

    return this.craftingMatrixStackHandler.getWidth();
  }

  @Override
  public int getHeight() {

    return craftingMatrixStackHandler.getHeight();
  }

  @Override
  public int getStackCount() {

    int stackCount = 0;

    for (int index = 0; index < getSize(); index++) {

      if (!this.craftingMatrixStackHandler.getStackInSlot(index).isEmpty()) {
        stackCount++;
      }
    }

    return stackCount;
  }

  @Override
  public IItemStack getStack(int i) {

    return CraftTweakerMC.getIItemStack(this.craftingMatrixStackHandler.getStackInSlot(i));
  }

  @Override
  public IItemStack getStack(int x, int y) {

    return getStack(y + x * getWidth());
  }

  @Override
  public void setStack(int x, int y, IItemStack stack) {

    setStack(y + x * getWidth(), stack);
  }

  @Override
  public void setStack(int i, IItemStack stack) {

    this.craftingMatrixStackHandler.setStackInSlot(i, CraftTweakerMC.getItemStack(stack));
  }

  @Override
  public IItemStack[][] getItems() {

    IItemStack[][] output = new IItemStack[getHeight()][getWidth()];
    IItemStack[] oneDimensional = getItemArray();

    for (int row = 0; row < getHeight(); row++) {

      for (int column = 0; column < getWidth(); column++) {
        output[row][column] = oneDimensional[row * getWidth() + column];
      }
    }

    return output;
  }

  @Override
  public IItemStack[] getItemArray() {

    IItemStack[] items = new IItemStack[getSize()];

    for (int index = 0; index < getSize(); index++) {
      items[index] = this.getStack(index);
    }

    return items;
  }

  @Override
  public ICraftingMatrixStackHandler getInternal() {

    return this.craftingMatrixStackHandler;
  }

}
