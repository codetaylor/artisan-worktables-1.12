package com.codetaylor.mc.artisanworktables.api.internal.recipe;

public class ToolIngredientEntry {

  private IArtisanIngredient tool;
  private int damage;

  public ToolIngredientEntry(IArtisanIngredient tool, int damage) {

    this.tool = tool;
    this.damage = damage;
  }

  public IArtisanIngredient getTool() {

    return this.tool;
  }

  public int getDamage() {

    return this.damage;
  }
}
