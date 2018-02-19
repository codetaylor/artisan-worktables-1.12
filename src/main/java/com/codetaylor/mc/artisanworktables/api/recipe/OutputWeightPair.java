package com.codetaylor.mc.artisanworktables.api.recipe;

import net.minecraft.item.ItemStack;

public class OutputWeightPair {

  private ItemStack output;
  private int weight;

  public OutputWeightPair(ItemStack output, int weight) {

    this.output = output;
    this.weight = weight;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public int getWeight() {

    return this.weight;
  }
}
