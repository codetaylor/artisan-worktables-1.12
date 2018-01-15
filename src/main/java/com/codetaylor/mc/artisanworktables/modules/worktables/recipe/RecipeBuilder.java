package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecipeBuilder {

  private int width;
  private int height;
  private boolean mirrored;
  private List<Ingredient> ingredients;
  private Ingredient tool;
  private int toolDamage;
  private List<OutputWeightPair> outputWeightPairList;
  private ExtraOutputChancePair[] extraOutputs;
  private EnumGameStageRequire gameStageRequire;
  private String[] includeGamestages;
  private String[] excludeGamestages;

  public RecipeBuilder() {

    this.ingredients = Collections.emptyList();
    this.outputWeightPairList = new ArrayList<>();
    this.extraOutputs = new ExtraOutputChancePair[3];
    Arrays.fill(this.extraOutputs, new ExtraOutputChancePair(ItemStack.EMPTY, 0));
    this.gameStageRequire = EnumGameStageRequire.ANY;
    this.includeGamestages = new String[0];
    this.excludeGamestages = new String[0];
  }

  public RecipeBuilder setIngredients(Ingredient[][] ingredients) {

    this.ingredients = new ArrayList<>();
    this.width = 0;

    for (Ingredient[] row : ingredients) {

      if (row.length > this.width) {
        this.width = row.length;
      }

      Collections.addAll(this.ingredients, row);
    }

    this.height = ingredients.length;
    return this;
  }

  public RecipeBuilder setIngredients(Ingredient[] ingredients) {

    this.ingredients = new ArrayList<>();
    Collections.addAll(this.ingredients, ingredients);
    return this;
  }

  public RecipeBuilder setMirrored() {

    this.mirrored = true;
    return this;
  }

  public RecipeBuilder setTools(Ingredient tool, int toolDamage) {

    this.tool = tool;
    this.toolDamage = toolDamage;
    return this;
  }

  public RecipeBuilder addOutput(ItemStack output, int weight) {

    weight = MathHelper.clamp(weight, 0, Integer.MAX_VALUE);
    this.outputWeightPairList.add(new OutputWeightPair(output, weight));
    return this;
  }

  public RecipeBuilder setExtraOutput(int index, ItemStack output, float chance) {

    if (index >= this.extraOutputs.length || index < 0) {
      index = 0;
    }

    chance = MathHelper.clamp(chance, 0, 1);
    this.extraOutputs[index] = new ExtraOutputChancePair(output, chance);
    return this;
  }

  public RecipeBuilder requireGamestages(EnumGameStageRequire require, String[] includeGamestages) {

    this.gameStageRequire = require;
    this.includeGamestages = includeGamestages;
    return this;
  }

  public RecipeBuilder excludeGamestages(String[] excludeGamestages) {

    this.excludeGamestages = excludeGamestages;
    return this;
  }

  public IRecipeWorktable create() throws RecipeBuilderException {

    if (this.outputWeightPairList == null || this.outputWeightPairList.isEmpty()) {
      throw new RecipeBuilderException("No outputs defined for recipe");
    }

    if (this.tool == null) {
      throw new RecipeBuilderException("No tools defined for recipe");
    }

    if (this.ingredients == null || this.ingredients.isEmpty()) {
      throw new RecipeBuilderException("No ingredients defined for recipe");
    }

    IGameStageMatcher gameStageMatcher;

    if (this.includeGamestages.length == 0 && this.excludeGamestages.length == 0) {
      gameStageMatcher = GameStageMatcher.TRUE;

    } else {
      gameStageMatcher = new GameStageMatcher(
          this.gameStageRequire,
          Arrays.asList(this.includeGamestages),
          Arrays.asList(this.excludeGamestages)
      );
    }

    IRecipeMatcher recipeMatcher;

    if (this.width > 0 && this.height > 0) {
      recipeMatcher = IRecipeMatcher.SHAPED;

    } else {
      recipeMatcher = IRecipeMatcher.SHAPELESS;
    }

    return new RecipeWorktable(
        gameStageMatcher,
        this.outputWeightPairList,
        this.tool.getMatchingStacks(),
        this.toolDamage,
        this.ingredients,
        this.extraOutputs,
        recipeMatcher,
        this.mirrored,
        this.width,
        this.height
    );
  }
}
