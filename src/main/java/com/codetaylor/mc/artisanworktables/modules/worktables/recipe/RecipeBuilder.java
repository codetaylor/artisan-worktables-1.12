package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
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
  private List<ToolIngredientEntry> tools;
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

    this.ingredients = null;
    this.secondaryIngredients = Collections.emptyList();
    this.fluidIngredient = null;
    this.outputWeightPairList = new ArrayList<>();
    this.extraOutputs = new ExtraOutputChancePair[3];
    Arrays.fill(this.extraOutputs, new ExtraOutputChancePair(ItemStack.EMPTY, 0));
    this.gameStageRequire = EnumGameStageRequire.ANY;
    this.includeGamestages = new String[0];
    this.excludeGamestages = new String[0];
    this.tools = new ArrayList<>();
    this.minimumTier = 0;
    this.experienceRequired = 0;
    this.levelRequired = 0;
    this.consumeExperience = true;
  }

  public RecipeBuilder setIngredients(Ingredient[][] ingredients) throws RecipeBuilderException {

    if (this.ingredients != null) {
      throw new RecipeBuilderException("Ingredients already set, can't be set twice");
    }

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

  public RecipeBuilder setIngredients(Ingredient[] ingredients) throws RecipeBuilderException {

    if (this.ingredients != null) {
      throw new RecipeBuilderException("Ingredients already set, can't be set twice");
    }

    this.ingredients = new ArrayList<>();
    Collections.addAll(this.ingredients, ingredients);
    return this;
  }

  public RecipeBuilder setFluidIngredient(FluidStack fluidIngredient) throws RecipeBuilderException {

    if (this.fluidIngredient != null) {
      throw new RecipeBuilderException("Fluid ingredient already set, can't be set twice");
    }

    this.fluidIngredient = fluidIngredient;
    return this;
  }

  public RecipeBuilder setSecondaryIngredients(IIngredient[] secondaryIngredients) throws RecipeBuilderException {

    if (!this.secondaryIngredients.isEmpty()) {
      throw new RecipeBuilderException("Secondary ingredients already set, can't ve set twice");
    }

    this.secondaryIngredients = new ArrayList<>();
    Collections.addAll(this.secondaryIngredients, secondaryIngredients);
    return this;
  }

  public RecipeBuilder setMirrored() {

    this.mirrored = true;
    return this;
  }

  public RecipeBuilder addTool(Ingredient tool, int toolDamage) throws RecipeBuilderException {

    if (this.tools.size() == MAX_TOOL_COUNT) {
      throw new RecipeBuilderException("Exceeded maximum tool count of " + MAX_TOOL_COUNT + " tools: " + this.tools.size() + 1);
    }

    this.tools.add(new ToolIngredientEntry(tool, toolDamage));
    return this;
  }

  public RecipeBuilder addOutput(ItemStack output, int weight) throws RecipeBuilderException {

    if (weight < 0) {
      throw new RecipeBuilderException("Output weight can't be < 0: " + weight);
    }

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

  public RecipeBuilder requireGamestages(
      EnumGameStageRequire require,
      String[] includeGamestages
  ) throws RecipeBuilderException {

    if (includeGamestages == null) {
      throw new RecipeBuilderException("Gamestage array can't be null");
    }

    this.gameStageRequire = require;
    this.includeGamestages = includeGamestages;
    return this;
  }

  public RecipeBuilder excludeGamestages(String[] excludeGamestages) throws RecipeBuilderException {

    if (excludeGamestages == null) {
      throw new RecipeBuilderException("Gamestage array can't be null");
    }

    this.excludeGamestages = excludeGamestages;
    return this;
  }

  public RecipeBuilder setMinimumTier(int minimumTier) throws RecipeBuilderException {

    try {
      EnumTier.fromId(minimumTier);

    } catch (Exception e) {
      throw new RecipeBuilderException("Invalid tier: " + minimumTier);
    }

    this.minimumTier = minimumTier;
    return this;
  }

  public RecipeBuilder setExperienceRequired(int cost) throws RecipeBuilderException {

    if (cost < 0) {
      throw new RecipeBuilderException("Experience can't be < 0");
    }

    this.levelRequired = 0;
    this.experienceRequired = cost;
    return this;
  }

  public RecipeBuilder setLevelRequired(int cost) throws RecipeBuilderException {

    if (cost < 0) {
      throw new RecipeBuilderException("Level can't be < 0");
    }

    this.experienceRequired = 0;
    this.levelRequired = cost;
    return this;
  }

  public RecipeBuilder setConsumeExperience(boolean consumeExperience) {

    this.consumeExperience = consumeExperience;
    return this;
  }

  public void validate() throws RecipeBuilderException {

    // Recipe must have a minimum of one output
    if (this.outputWeightPairList == null || this.outputWeightPairList.isEmpty()) {
      throw new RecipeBuilderException("No outputs defined for recipe");
    }

    // Recipe must have ingredients
    if (this.ingredients == null || this.ingredients.isEmpty()) {
      throw new RecipeBuilderException("No ingredients defined for recipe");
    }

    // Must be able to calculate recipe tier
    EnumTier tier = RecipeTierCalculator.calculateTier(
        this.width,
        this.height,
        this.tools.size(),
        this.secondaryIngredients.size()
    );

    if (tier == null) {
      throw new RecipeBuilderException("Unable to calculate recipe tier");
    }

    this.minimumTier = Math.max(this.minimumTier, tier.getId());

  }

  public IAWRecipe create() throws RecipeBuilderException {

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

    ToolEntry[] tools = new ToolEntry[this.tools.size()];

    for (int i = 0; i < this.tools.size(); i++) {
      tools[i] = new ToolEntry(this.tools.get(i));
    }

    if (this.secondaryIngredients == null) {
      this.secondaryIngredients = Collections.emptyList();
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
