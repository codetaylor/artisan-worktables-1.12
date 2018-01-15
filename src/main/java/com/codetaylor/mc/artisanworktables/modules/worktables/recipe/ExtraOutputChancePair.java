package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import net.minecraft.item.ItemStack;

public class ExtraOutputChancePair {

  private ItemStack output;
  private float chance;

  public ExtraOutputChancePair(ItemStack output, float chance) {

    this.output = output;
    this.chance = chance;
  }

  public ItemStack getOutput() {

    return this.output;
  }

  public float getChance() {

    return this.chance;
  }
}
