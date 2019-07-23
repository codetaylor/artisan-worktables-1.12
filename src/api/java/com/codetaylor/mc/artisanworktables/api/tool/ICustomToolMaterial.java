package com.codetaylor.mc.artisanworktables.api.tool;

public interface ICustomToolMaterial {

  String getName();

  int getHarvestLevel();

  int getMaxUses();

  float getEfficiency();

  float getDamage();

  int getEnchantability();

  String getColor();

  boolean isShiny();

  String getIngredientString();

  String getLangKey();

  String getOreDictKey();
}
