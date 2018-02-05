package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import net.minecraft.item.crafting.Ingredient;

public class ToolIngredientEntry {

  private Ingredient tool;
  private int damage;

  public ToolIngredientEntry(Ingredient tool, int damage) {

    this.tool = tool;
    this.damage = damage;
  }

  public Ingredient getTool() {

    return this.tool;
  }

  public int getDamage() {

    return this.damage;
  }
}
