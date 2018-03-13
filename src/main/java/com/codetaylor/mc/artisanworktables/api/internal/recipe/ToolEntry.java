package com.codetaylor.mc.artisanworktables.api.internal.recipe;

public class ToolEntry {

  private IArtisanItemStack[] tool;
  private int damage;

  public ToolEntry(ToolIngredientEntry entry) {

    this(entry.getTool().getMatchingStacks(), entry.getDamage());
  }

  public ToolEntry(IArtisanItemStack[] tool, int damage) {

    this.tool = tool;
    this.damage = damage;
  }

  public IArtisanItemStack[] getTool() {

    return this.tool;
  }

  public int getDamage() {

    return this.damage;
  }
}
