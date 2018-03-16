package com.codetaylor.mc.artisanworktables.api.internal.recipe;

public class OutputWeightPair {

  private IArtisanItemStack output;
  private int weight;

  public OutputWeightPair(IArtisanItemStack output, int weight) {

    this.output = output;
    this.weight = weight;
  }

  public IArtisanItemStack getOutput() {

    return this.output.copy();
  }

  public int getWeight() {

    return this.weight;
  }
}
