package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import net.minecraft.item.ItemStack;

public class ToolEntry {

  private ItemStack[] tool;
  private int damage;

  public ToolEntry(ToolIngredientEntry entry) {

    this(entry.getTool().getMatchingStacks(), entry.getDamage());
  }

  public ToolEntry(ItemStack[] tool, int damage) {

    this.tool = tool;
    this.damage = damage;
  }

  public ItemStack[] getTool() {

    return this.tool;
  }

  public int getDamage() {

    return this.damage;
  }
}
