package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumGameStageRequire;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.recipe.IRecipeFactory;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.requirement.gamestages.requirement.GameStagesRequirementBuilder;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.IRecipeBuilderCopyStrategyInternal;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.RecipeBuilderCopyStrategyByName;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.RecipeBuilderCopyStrategyByOutput;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy.RecipeBuilderCopyStrategyByRecipe;
import crafttweaker.api.recipes.ICraftingRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class RecipeBuilderInternal
    implements IRecipeBuilder,
    IRecipeBuilderAction {

  public static RecipeBuilderInternal get(String table, IRecipeFactory recipeFactory) throws RecipeBuilderException {

    table = table.toLowerCase();

    if (!ArtisanAPI.isWorktableNameValid(table)) {
      throw new RecipeBuilderException("Unknown table type: " + table + ". Valid table types are: " + String.join(
          ",",
          ArtisanAPI.getWorktableNames()
      ));
    }

    return new RecipeBuilderInternal(table, ModuleWorktables.RECIPE_ADDITION_QUEUE, recipeFactory);
  }

  public static class Copy {

    public static IRecipeBuilderCopyStrategy byName(String recipeName) throws RecipeBuilderException {

      return new RecipeBuilderCopyStrategyByName(recipeName);
    }

    public static IRecipeBuilderCopyStrategy byRecipe(ICraftingRecipe recipe) throws RecipeBuilderException {

      return new RecipeBuilderCopyStrategyByRecipe(recipe);
    }

    public static IRecipeBuilderCopyStrategy byOutput(IArtisanIngredient[] outputs) throws RecipeBuilderException {

      return new RecipeBuilderCopyStrategyByOutput(outputs);
    }
  }

  private static final int MAX_TOOL_COUNT = 3;

  private String tableName;
  private final IRecipeAdditionQueue recipeAdditionQueue;
  private IRecipeFactory recipeFactory;

  // --------------------------------------------------------------------------
  // - Recipe

  private String name;
  private int width;
  private int height;
  private boolean mirrored;
  private List<IArtisanIngredient> ingredients;
  private List<IArtisanIngredient> secondaryIngredients;
  private boolean consumeSecondaryIngredients;
  private FluidStack fluidIngredient;
  private List<ToolIngredientEntry> tools;
  private List<OutputWeightPair> outputWeightPairList;
  private ExtraOutputChancePair[] extraOutputs;
  private int minimumTier;
  private int maximumTier;
  private int experienceRequired;
  private int levelRequired;
  private boolean consumeExperience;
  private Map<ResourceLocation, IRequirement> requirementMap;
  private boolean hidden;

  @Deprecated
  private EnumGameStageRequire gameStageRequire;

  @Deprecated
  private String[] includeGameStages;

  @Deprecated
  private String[] excludeGameStages;

  // --------------------------------------------------------------------------
  // - Copy

  private IRecipeBuilderCopyStrategyInternal recipeCopyStrategy;

  // --------------------------------------------------------------------------
  // - Validation

  private boolean invalid;
  private boolean inputSet;
  private boolean outputSet;
  private boolean validateRun;

  // --------------------------------------------------------------------------
  // - Constructor

  public RecipeBuilderInternal(
      String tableName,
      IRecipeAdditionQueue recipeAdditionQueue,
      IRecipeFactory recipeFactory
  ) {

    this.tableName = tableName;
    this.recipeAdditionQueue = recipeAdditionQueue;
    this.recipeFactory = recipeFactory;

    this.ingredients = null;
    this.secondaryIngredients = Collections.emptyList();
    this.consumeSecondaryIngredients = true;
    this.fluidIngredient = null;
    this.outputWeightPairList = new ArrayList<>();
    this.extraOutputs = new ExtraOutputChancePair[3];
    Arrays.fill(this.extraOutputs, new ExtraOutputChancePair(ArtisanItemStack.EMPTY, 0));
    this.tools = new ArrayList<>();
    this.minimumTier = 0;
    this.maximumTier = Integer.MAX_VALUE;
    this.experienceRequired = 0;
    this.levelRequired = 0;
    this.consumeExperience = true;

    this.requirementMap = new HashMap<>();

    // Deprecated
    this.gameStageRequire = EnumGameStageRequire.ANY;
    this.includeGameStages = new String[0];
    this.excludeGameStages = new String[0];
  }

  // --------------------------------------------------------------------------
  // - Validation

  private void isNonnull(Object o, String message) throws RecipeBuilderException {

    if (o == null) {
      this.setInvalid(message);
    }
  }

  private void isNull(Object o, String message) throws RecipeBuilderException {

    if (o != null) {
      this.setInvalid(message);
    }
  }

  private void isTrue(boolean b, String message) throws RecipeBuilderException {

    if (!b) {
      this.setInvalid(message);
    }
  }

  private void isFalse(boolean b, String message) throws RecipeBuilderException {

    if (b) {
      this.setInvalid(message);
    }
  }

  private void isNotZeroLength(Object[] o, String message) throws RecipeBuilderException {

    if (o.length == 0) {
      this.setInvalid(message);
    }
  }

  private void setInvalid(String message) throws RecipeBuilderException {

    this.invalid = true;
    throw new RecipeBuilderException(message);
  }

  // --------------------------------------------------------------------------
  // - Recipe

  @Override
  public IRecipeBuilder setName(String name) throws RecipeBuilderException {

    this.isNonnull(name, "Recipe name can't be null");
    this.isTrue(name.length() > 0, "Recipe name can't be zero length");

    this.name = name;
    return this;
  }

  @Override
  public IRecipeBuilder setIngredients(IArtisanIngredient[][] ingredients) throws RecipeBuilderException {

    this.isNonnull(ingredients, "Ingredient matrix can't be null");
    this.isFalse(this.inputSet, "Recipe input already set");
    this.isNull(this.ingredients, "Ingredients already set, can't be set twice");

    this.ingredients = new ArrayList<>();
    this.width = 0;

    for (IArtisanIngredient[] row : ingredients) {

      if (row.length > this.width) {
        this.width = row.length;
      }
    }

    for (IArtisanIngredient[] row : ingredients) {

      this.isNonnull(row, "Ingredient row can't be null");
      this.isNotZeroLength(row, "Ingredient row can't be zero length");

      List<IArtisanIngredient> cols = NonNullList.withSize(this.width, ArtisanIngredient.EMPTY);

      for (int i = 0; i < row.length; i++) {
        cols.set(i, row[i]);
      }
      this.ingredients.addAll(cols);
    }

    this.height = ingredients.length;
    this.inputSet = true;
    return this;
  }

  @Override
  public IRecipeBuilder setIngredients(IArtisanIngredient[] ingredients) throws RecipeBuilderException {

    this.isNonnull(ingredients, "Ingredient array can't be null");
    this.isFalse(this.inputSet, "Recipe input already set");
    this.isNull(this.ingredients, "Ingredients already set, can't be set twice");

    this.ingredients = new ArrayList<>();
    Collections.addAll(this.ingredients, ingredients);
    this.inputSet = true;
    return this;
  }

  @Override
  public IRecipeBuilder setFluidIngredient(FluidStack fluidIngredient) throws RecipeBuilderException {

    this.isNull(this.fluidIngredient, "Fluid ingredient already set, can't be set twice");

    this.fluidIngredient = fluidIngredient;
    return this;
  }

  @Override
  public IRecipeBuilder setSecondaryIngredients(IArtisanIngredient[] secondaryIngredients) throws RecipeBuilderException {

    this.isNonnull(secondaryIngredients, "Secondary ingredients array can't be null");
    this.isNotZeroLength(secondaryIngredients, "Secondary ingredients array can't be zero length");
    this.isTrue(this.secondaryIngredients.isEmpty(), "Secondary ingredients already set, can't be set twice");
    this.isTrue(secondaryIngredients.length <= 9, "Exceeded max allowed 9 secondary ingredients");

    this.secondaryIngredients = new ArrayList<>();
    Collections.addAll(this.secondaryIngredients, secondaryIngredients);
    return this;
  }

  @Override
  public IRecipeBuilder setConsumeSecondaryIngredients(boolean consumeSecondaryIngredients) {

    this.consumeSecondaryIngredients = consumeSecondaryIngredients;
    return this;
  }

  @Override
  public IRecipeBuilder setMirrored() {

    this.mirrored = true;
    return this;
  }

  @Override
  public IRecipeBuilder addTool(IArtisanIngredient tool, int toolDamage) throws RecipeBuilderException {

    this.isNonnull(tool, "Tool can't be null");
    this.isTrue(this.tools.size() < MAX_TOOL_COUNT, "Exceeded maximum tool count");

    this.tools.add(new ToolIngredientEntry(tool, toolDamage));
    return this;
  }

  @Override
  public IRecipeBuilder addOutput(IArtisanItemStack output, int weight) throws RecipeBuilderException {

    this.isNonnull(output, "Output can't be null");

    if (weight < 1) {
      weight = 1;
    }

    this.outputWeightPairList.add(new OutputWeightPair(output, weight));
    this.outputSet = true;
    return this;
  }

  @Override
  public IRecipeBuilder setExtraOutput(
      int index,
      IArtisanItemStack output,
      float chance
  ) throws RecipeBuilderException {

    this.isTrue(index >= 0, "Extra output index out of bounds");
    this.isTrue(index < this.extraOutputs.length, "Extra output index out of bounds");

    chance = MathHelper.clamp(chance, 0, 1);
    this.extraOutputs[index] = new ExtraOutputChancePair(output, chance);
    return this;
  }

  @Override
  public IRecipeBuilder requireGameStages(
      EnumGameStageRequire require,
      String[] includeGameStages
  ) throws RecipeBuilderException {

    this.isNonnull(require, "Game stage enum can't be null");
    this.isNonnull(includeGameStages, "Game stage array can't be null");
    this.isNotZeroLength(includeGameStages, "Game stage array can't be zero length");

    this.gameStageRequire = require;
    this.includeGameStages = includeGameStages;
    return this;
  }

  @Override
  public IRecipeBuilder excludeGameStages(String[] excludeGameStages) throws RecipeBuilderException {

    this.isNonnull(excludeGameStages, "Game stage array can't be null");
    this.isNotZeroLength(excludeGameStages, "Game stage array can't be zero length");

    this.excludeGameStages = excludeGameStages;
    return this;
  }

  @Override
  public IRecipeBuilder setMinimumTier(int tier) throws RecipeBuilderException {

    try {
      EnumTier.fromId(tier);

    } catch (Exception e) {
      this.setInvalid("Invalid tier: " + tier);
    }

    if (tier > this.maximumTier) {
      this.setInvalid("Minimum tier can't be larger than maximum tier: " + this.maximumTier + " < " + tier);
    }

    this.minimumTier = tier;
    return this;
  }

  @Override
  public IRecipeBuilder setMaximumTier(int tier) throws RecipeBuilderException {

    try {
      EnumTier.fromId(tier);

    } catch (Exception e) {
      this.setInvalid("Invalid tier: " + tier);
    }

    if (tier < this.minimumTier) {
      this.setInvalid("Maximum tier can't be smaller than minimum tier: " + this.minimumTier + ">" + tier);
    }

    this.maximumTier = tier;
    return this;
  }

  @Override
  public IRecipeBuilder setExperienceRequired(int cost) throws RecipeBuilderException {

    this.isTrue(cost > 0, "Experience required must be greater than zero");

    this.levelRequired = 0;
    this.experienceRequired = cost;
    return this;
  }

  @Override
  public IRecipeBuilder setLevelRequired(int cost) throws RecipeBuilderException {

    this.isTrue(cost > 0, "Level required must be greater than zero");

    this.experienceRequired = 0;
    this.levelRequired = cost;
    return this;
  }

  @Override
  public IRecipeBuilder setConsumeExperience(boolean consumeExperience) {

    this.consumeExperience = consumeExperience;
    return this;
  }

  @Override
  public IRecipeBuilder setCopy(IRecipeBuilderCopyStrategy copyStrategy) {

    this.recipeCopyStrategy = (IRecipeBuilderCopyStrategyInternal) copyStrategy;
    return this;
  }

  @Override
  public IRecipeBuilder addRequirement(IRequirementBuilder matchRequirementBuilder) {

    ResourceLocation location = matchRequirementBuilder.getResourceLocation();

    if (Loader.isModLoaded(matchRequirementBuilder.getRequirementId().toLowerCase())) {
      IRequirement requirement = matchRequirementBuilder.create();
      this.requirementMap.put(location, requirement);
    }

    return this;
  }

  public void validate() throws RecipeBuilderException {

    if (this.invalid) {
      throw new RecipeBuilderException("Invalid recipe");
    }

    if (this.recipeCopyStrategy != null) {

      if (!this.recipeCopyStrategy.isValid()) {
        this.setInvalid("Invalid recipe copy strategy");

      } else if (!this.inputSet && this.recipeCopyStrategy.isExcludeInput()) {
        this.setInvalid("Recipe missing input");

      } else if (!this.outputSet && this.recipeCopyStrategy.isExcludeOutput()) {
        this.setInvalid("Recipe missing output");
      }

    } else {

      // Recipe must have a minimum of one output
      this.isTrue(this.outputSet, "No outputs defined for recipe");

      // Recipe must have ingredients
      this.isTrue(this.inputSet, "No ingredients defined for recipe");
    }

    // Must be able to calculate recipe tier
    EnumTier tier = RecipeTierCalculator.calculateTier(
        this.width,
        this.height,
        this.tools.size(),
        this.secondaryIngredients.size()
    );

    if (tier == null) {
      this.setInvalid("Unable to calculate recipe tier");

    } else {
      this.minimumTier = Math.max(this.minimumTier, tier.getId());
    }

    this.validateRun = true;
  }

  @Override
  public IRecipeBuilder setHidden(boolean hidden) {

    this.hidden = hidden;
    return this;
  }

  public IRecipeBuilderCopyStrategyInternal getRecipeCopyStrategy() {

    return this.recipeCopyStrategy;
  }

  public String getTableName() {

    return this.tableName;
  }

  public IRecipeAdditionQueue getRecipeAdditionQueue() {

    return this.recipeAdditionQueue;
  }

  public IRecipeFactory getRecipeFactory() {

    return this.recipeFactory;
  }

  public RecipeBuilderInternal copy() {

    RecipeBuilderInternal copy = new RecipeBuilderInternal(this.tableName, recipeAdditionQueue, recipeFactory);

    copy.outputSet = this.outputSet;
    copy.inputSet = this.inputSet;
    copy.invalid = this.invalid;

    copy.width = this.width;
    copy.height = this.height;
    copy.mirrored = this.mirrored;

    if (this.ingredients != null) {
      copy.ingredients = new ArrayList<>(this.ingredients);
    }
    copy.secondaryIngredients = new ArrayList<>(this.secondaryIngredients);
    copy.fluidIngredient = this.fluidIngredient;
    copy.tools = new ArrayList<>(this.tools);
    copy.outputWeightPairList = new ArrayList<>(this.outputWeightPairList);
    copy.extraOutputs = new ExtraOutputChancePair[this.extraOutputs.length];
    System.arraycopy(this.extraOutputs, 0, copy.extraOutputs, 0, this.extraOutputs.length);
    copy.minimumTier = this.minimumTier;
    copy.experienceRequired = this.experienceRequired;
    copy.levelRequired = this.levelRequired;
    copy.consumeExperience = this.consumeExperience;

    copy.requirementMap = new HashMap<>(this.requirementMap);

    // Deprecated
    copy.gameStageRequire = this.gameStageRequire;
    copy.includeGameStages = new String[this.includeGameStages.length];
    System.arraycopy(this.includeGameStages, 0, copy.includeGameStages, 0, this.includeGameStages.length);
    copy.excludeGameStages = new String[this.excludeGameStages.length];
    System.arraycopy(this.excludeGameStages, 0, copy.excludeGameStages, 0, this.excludeGameStages.length);

    return copy;
  }

  @Override
  public IRecipeBuilder create() throws RecipeBuilderException {

    if (!this.validateRun) {
      this.validate();
    }

    if (this.recipeCopyStrategy != null) {
      this.recipeAdditionQueue.offerWithCopy(this);

    } else {
      this.recipeAdditionQueue.offer(this);
    }

    return new RecipeBuilderInternal(this.tableName, this.recipeAdditionQueue, this.recipeFactory);
  }

  @Override
  public void apply(ILogger logger) throws RecipeBuilderException {

    // Build the recipe object and add it to the registry.

    // Assemble the GameStages requirement from deprecated fields.
    // TODO: remove this in future version (2018-03-28)

    if (Loader.isModLoaded("gamestages")) {

      if (this.includeGameStages.length != 0
          || this.excludeGameStages.length != 0) {

        logger.logWarning("Using deprecated GameStages builder methods! Please use the new requirement system instead.");

        GameStagesRequirementBuilder builder = new GameStagesRequirementBuilder();

        if (this.gameStageRequire == EnumGameStageRequire.ALL) {
          builder.allOf(this.includeGameStages);

        } else {
          builder.anyOf(this.includeGameStages);
        }

        builder.exclude(this.excludeGameStages);

        this.requirementMap.put(builder.getResourceLocation(), builder.create());
      }
    }

    // Decide which style recipe matcher to use.

    IRecipeMatrixMatcher recipeMatcher;

    if (this.width > 0 && this.height > 0) {
      recipeMatcher = IRecipeMatrixMatcher.SHAPED;

    } else {
      recipeMatcher = IRecipeMatrixMatcher.SHAPELESS;
    }

    // Prepare the tool entry array.

    ToolEntry[] tools = new ToolEntry[this.tools.size()];

    for (int i = 0; i < this.tools.size(); i++) {
      tools[i] = new ToolEntry(this.tools.get(i));
    }

    // Ensure that the secondary ingredient collection is not null.

    if (this.secondaryIngredients == null) {
      this.secondaryIngredients = Collections.emptyList();
    }

    // Calculate the recipe name and ensure it is unique.

    RecipeRegistry registry = ArtisanAPI.getWorktableRecipeRegistry(this.tableName);

    if (this.name == null) {
      this.name = this.calculateName(registry, logger);

    } else {
      this.name = this.tableName + "_" + this.name;

      // Ensure the recipe has a unique name.
      int i = 0;

      while (registry.hasRecipe(this.name)) {
        logger.logWarning("Duplicate recipe name found: " + this.name);

        if (i > 1000) {
          // sanity
          break;
        }

        i += 1;
        this.name = this.tableName + "_" + this.name + "_" + i;
      }
    }

    if (registry.hasRecipe(this.name)) {
      throw new RecipeBuilderException("Duplicate recipe name: " + this.name);
    }

    registry.addRecipe(this.recipeFactory.create(
        this.name,
        new HashMap<>(this.requirementMap),
        this.outputWeightPairList,
        tools,
        this.ingredients,
        this.secondaryIngredients,
        this.consumeSecondaryIngredients,
        this.fluidIngredient,
        this.experienceRequired,
        this.levelRequired,
        this.consumeExperience,
        this.extraOutputs,
        recipeMatcher,
        this.mirrored,
        this.width,
        this.height,
        this.minimumTier,
        this.maximumTier,
        this.hidden
    ));
  }

  private String calculateName(RecipeRegistry registry, ILogger logger) {

    HashCodeBuilder builder = new HashCodeBuilder(17, 37);

    // Requirements
    for (ResourceLocation resourceLocation : this.requirementMap.keySet()) {
      builder.append(resourceLocation.hashCode());
    }

    // Output
    for (OutputWeightPair pair : this.outputWeightPairList) {
      builder.append(HashCodeUtil.get(pair));
    }

    // Tools
    for (ToolIngredientEntry entry : this.tools) {
      builder.append(HashCodeUtil.get(entry));
    }

    // Ingredients
    for (IArtisanIngredient ingredient : this.ingredients) {
      builder.append(HashCodeUtil.get(ingredient));
    }

    // Secondary Ingredients
    for (IArtisanIngredient ingredient : this.secondaryIngredients) {
      builder.append(HashCodeUtil.get(ingredient));
    }

    builder.append(this.consumeSecondaryIngredients)
        .append(HashCodeUtil.get(this.fluidIngredient))
        .append(this.experienceRequired)
        .append(this.levelRequired)
        .append(this.consumeExperience);

    // Extra Chance Outputs
    for (ExtraOutputChancePair pair : this.extraOutputs) {
      builder.append(HashCodeUtil.get(pair));
    }

    builder.append(this.mirrored)
        .append(this.width)
        .append(this.height)
        .append(this.minimumTier)
        .append(this.maximumTier);

    int hash = builder.build();
    String recipeName = this.tableName + "_" + hash;

    // check for duplicate recipe name
    while (registry.hasRecipe(recipeName)) {
      logger.logWarning("Duplicate recipe name found: " + recipeName);
      hash += 1;
      recipeName = this.tableName + "_" + hash;
    }

    return recipeName;
  }
}
