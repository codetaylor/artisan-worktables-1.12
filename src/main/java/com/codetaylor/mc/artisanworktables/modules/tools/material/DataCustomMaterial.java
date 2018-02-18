package com.codetaylor.mc.artisanworktables.modules.tools.material;

import com.google.gson.annotations.SerializedName;

public class DataCustomMaterial {

  private String name;
  private int harvestLevel;
  private int maxUses;
  private float efficiency;
  private float damage;
  private int enchantability;
  private String color;
  private boolean shiny;

  @SerializedName("ingredient")
  private String ingredientString;
  private String langKey;

  public DataCustomMaterial(
      String name,
      int harvestLevel,
      int maxUses,
      float efficiency,
      float damage,
      int enchantability,
      String color,
      boolean shiny,
      String ingredientString,
      String langKey
  ) {

    this.name = name;
    this.harvestLevel = harvestLevel;
    this.maxUses = maxUses;
    this.efficiency = efficiency;
    this.damage = damage;
    this.enchantability = enchantability;
    this.color = color;
    this.shiny = shiny;
    this.ingredientString = ingredientString;
    this.langKey = langKey;
  }

  public String getName() {

    return this.name;
  }

  public int getHarvestLevel() {

    return this.harvestLevel;
  }

  public int getMaxUses() {

    return this.maxUses;
  }

  public float getEfficiency() {

    return this.efficiency;
  }

  public float getDamage() {

    return this.damage;
  }

  public int getEnchantability() {

    return this.enchantability;
  }

  public String getColor() {

    return this.color;
  }

  public boolean isShiny() {

    return this.shiny;
  }

  public String getIngredientString() {

    return this.ingredientString;
  }

  public String getLangKey() {

    return this.langKey;
  }
}
