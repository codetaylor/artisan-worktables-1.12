package com.codetaylor.mc.artisanworktables.api.internal.recipe;

public class ExtraOutputChancePair {

  private IArtisanItemStack output;
  private float chance;

  public ExtraOutputChancePair(IArtisanItemStack output, float chance) {

    this.output = output;
    this.chance = chance;
  }

  public IArtisanItemStack getOutput() {

    return this.output.copy();
  }

  public float getChance() {

    return this.chance;
  }
}
