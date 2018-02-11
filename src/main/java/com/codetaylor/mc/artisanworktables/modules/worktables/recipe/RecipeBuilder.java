package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import crafttweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecipeBuilder {

  public static final int MAX_TOOL_COUNT = 4;
  private int width;
  private int height;
  private boolean mirrored;
  private List<Ingredient> ingredients;
  private List<IIngredient> secondaryIngredients;
  private FluidStack fluidIngredient;
  private ToolIngredientEntry[] tools;
  private List<OutputWeightPair> outputWeightPairList;
  private ExtraOutputChancePair[] extraOutputs;
  private EnumGameStageRequire gameStageRequire;
  private String[] includeGamestages;
  private String[] excludeGamestages;
  private int minimumTier;
  private int experienceRequired;
  private int levelRequired;
  private boolean consumeExperience;

  public RecipeBuilder() {

    this.ingredients = Collections.emptyList();
    this.secondaryIngredients = Collections.emptyList();
    this.fluidIngredient = null;
    this.outputWeightPairList = new ArrayList<>();
    this.extraOutputs = new ExtraOutputChancePair[3];
    Arrays.fill(this.extraOutputs, new ExtraOutputChancePair(ItemStack.EMPTY, 0));
    this.gameStageRequire = EnumGameStageRequire.ANY;
    this.includeGamestages = new String[0];
    this.excludeGamestages = new String[0];
    this.tools = new ToolIngredientEntry[MAX_TOOL_COUNT];
    this.minimumTier = 0;
    this.experienceRequired = 0;
    this.levelRequired = 0;
    this.consumeExperience = true;
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

  public RecipeBuilder setFluidIngredient(FluidStack fluidIngredient) {

    this.fluidIngredient = fluidIngredient;
    return this;
  }

  public RecipeBuilder setSecondaryIngredients(IIngredient[] secondaryIngredients) {

    this.secondaryIngredients = new ArrayList<>();
    Collections.addAll(this.secondaryIngredients, secondaryIngredients);
    return this;
  }

  public RecipeBuilder setMirrored() {

    this.mirrored = true;
    return this;
  }

  public RecipeBuilder setTool(int index, Ingredient tool, int toolDamage) {

    this.tools[index] = new ToolIngredientEntry(tool, toolDamage);
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

  public RecipeBuilder setMinimumTier(int minimumTier) {

    this.minimumTier = minimumTier;
    return this;
  }

  public RecipeBuilder setExperienceRequired(int cost) {

    this.levelRequired = 0;
    this.experienceRequired = cost;
    return this;
  }

  public RecipeBuilder setLevelRequired(int cost) {

    this.experienceRequired = 0;
    this.levelRequired = cost;
    return this;
  }

  public RecipeBuilder setConsumeExperience(boolean consumeExperience) {

    this.consumeExperience = consumeExperience;
    return this;
  }

  public IRecipe create() throws RecipeBuilderException {

    if (this.outputWeightPairList == null || this.outputWeightPairList.isEmpty()) {
      throw new RecipeBuilderException("No outputs defined for recipe");
    }

    int toolCount = 0;

    for (int i = 0; i < MAX_TOOL_COUNT; i++) {

      if (this.tools[i] == null) {
        break;
      }

      toolCount += 1;
    }

    if (toolCount == 0) {
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

    IRecipeMatrixMatcher recipeMatcher;

    if (this.width > 0 && this.height > 0) {
      recipeMatcher = IRecipeMatrixMatcher.SHAPED;

    } else {
      recipeMatcher = IRecipeMatrixMatcher.SHAPELESS;
    }

    ToolEntry[] tools = new ToolEntry[toolCount];

    for (int i = 0; i < toolCount; i++) {
      tools[i] = new ToolEntry(this.tools[i]);
    }

    return new Recipe(
        gameStageMatcher,
        this.outputWeightPairList,
        tools,
        this.ingredients,
        this.secondaryIngredients,
        this.fluidIngredient,
        this.experienceRequired,
        this.levelRequired,
        this.consumeExperience,
        this.extraOutputs,
        recipeMatcher,
        this.mirrored,
        this.width,
        this.height,
        this.minimumTier
    );
  }
}
