package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.athenaeum.util.WeightedPicker;
import crafttweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Recipe
    implements IAWRecipe {

  private IGameStageMatcher gameStageMatcher;
  private ToolEntry[] tools;
  private List<OutputWeightPair> output;
  private List<Ingredient> ingredients;
  private List<IIngredient> secondaryIngredients;
  private FluidStack fluidIngredient;
  private ExtraOutputChancePair[] extraOutputs;
  private IRecipeMatrixMatcher recipeMatrixMatcher;
  private boolean mirrored;
  private int width;
  private int height;
  private int minimumTier;
  private int maximumTier;
  private int experienceRequired;
  private int levelRequired;
  private boolean consumeExperience;

  public Recipe(
      IGameStageMatcher gameStageMatcher,
      List<OutputWeightPair> output,
      ToolEntry[] tools,
      List<Ingredient> ingredients,
      @Nonnull List<IIngredient> secondaryIngredients,
      @Nullable FluidStack fluidIngredient,
      int experienceRequired,
      int levelRequired,
      boolean consumeExperience,
      ExtraOutputChancePair[] extraOutputs,
      IRecipeMatrixMatcher recipeMatrixMatcher,
      boolean mirrored,
      int width,
      int height,
      int minimumTier,
      int maximumTier
  ) {

    this.gameStageMatcher = gameStageMatcher;
    this.output = output;
    this.tools = tools;
    this.ingredients = ingredients;
    this.secondaryIngredients = secondaryIngredients;
    this.fluidIngredient = fluidIngredient;
    this.experienceRequired = experienceRequired;
    this.levelRequired = levelRequired;
    this.consumeExperience = consumeExperience;
    this.extraOutputs = extraOutputs;
    this.recipeMatrixMatcher = recipeMatrixMatcher;
    this.mirrored = mirrored;
    this.width = width;
    this.height = height;
    this.minimumTier = minimumTier;
    this.maximumTier = maximumTier;
  }

  @Override
  public int getExperienceRequired() {

    return this.experienceRequired;
  }

  @Override
  public int getLevelRequired() {

    return this.levelRequired;
  }

  @Override
  public boolean consumeExperience() {

    return this.consumeExperience;
  }

  @Override
  public boolean matchGameStages(Collection<String> unlockedStages) {

    return this.gameStageMatcher.matches(unlockedStages);
  }

  @Override
  public ItemStack getSecondaryOutput() {

    if (this.extraOutputs[0].getOutput().isEmpty()) {
      return ItemStack.EMPTY;
    }

    return this.extraOutputs[0].getOutput().copy();
  }

  @Override
  public float getSecondaryOutputChance() {

    return this.extraOutputs[0].getChance();
  }

  @Override
  public ItemStack getTertiaryOutput() {

    if (this.extraOutputs[1].getOutput().isEmpty()) {
      return ItemStack.EMPTY;
    }

    return this.extraOutputs[1].getOutput().copy();
  }

  @Override
  public float getTertiaryOutputChance() {

    return this.extraOutputs[1].getChance();
  }

  @Override
  public ItemStack getQuaternaryOutput() {

    if (this.extraOutputs[2].getOutput().isEmpty()) {
      return ItemStack.EMPTY;
    }

    return this.extraOutputs[2].getOutput().copy();
  }

  @Override
  public float getQuaternaryOutputChance() {

    return this.extraOutputs[2].getChance();
  }

  @Override
  public List<IIngredient> getSecondaryIngredients() {

    return this.secondaryIngredients;
  }

  @Override
  public boolean isValidTool(ItemStack tool, int toolIndex) {

    if (toolIndex >= this.tools.length) {
      return false;
    }

    for (ItemStack itemStack : this.tools[toolIndex].getTool()) {

      // We can't use itemStack.isItemEqualIgnoreDurability(tool) here because
      // apparently Tinker's tools don't set the max durability on the tool
      // which causes that check to fail because it thinks the item can't be
      // damaged. Instead, we assume the item being used has durability and
      // just compare items.
      if (itemStack.getItem() == tool.getItem()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean hasSufficientToolDurability(ItemStack tool, int toolIndex) {

    if (tool.isEmpty()) {
      return false;
    }

    if (toolIndex >= this.tools.length) {
      return false;
    }

    if (ModuleWorktablesConfig.RESTRICT_CRAFT_MINIMUM_DURABILITY) {

      // Note: this may fail with tinker's tools because as far as I know,
      // tinker's tools don't have a max damage value set
      if (tool.getItemDamage() + this.tools[toolIndex].getDamage() > tool.getMaxDamage()) {
        return false;
      }
    }

    return true;
  }

  @Override
  public ItemStack[] getTools(int toolIndex) {

    if (toolIndex >= this.tools.length) {
      return new ItemStack[0];
    }

    return this.tools[toolIndex].getTool();
  }

  @Override
  public List<Ingredient> getIngredientList() {

    return Collections.unmodifiableList(this.ingredients);
  }

  @Override
  @Nullable
  public FluidStack getFluidIngredient() {

    if (this.fluidIngredient != null) {
      return this.fluidIngredient.copy();
    }

    return null;
  }

  @Override
  public List<OutputWeightPair> getOutputWeightPairList() {

    return Collections.unmodifiableList(this.output);
  }

  @Override
  public ItemStack selectOutput(Random random) {

    if (this.output.size() > 1) {
      WeightedPicker<ItemStack> picker = new WeightedPicker<>(random);

      for (OutputWeightPair pair : this.output) {
        picker.add(pair.getWeight(), pair.getOutput());
      }

      return picker.get();

    } else {
      return this.getBaseOutput();
    }
  }

  @Override
  public ItemStack getBaseOutput() {

    return this.output.get(0).getOutput();
  }

  @Override
  public boolean hasMultipleWeightedOutputs() {

    return this.output.size() > 1;
  }

  @Override
  public int getToolDamage(int toolIndex) {

    if (toolIndex >= this.tools.length) {
      return 0;
    }

    return this.tools[toolIndex].getDamage();
  }

  @Override
  public int getWidth() {

    return this.width;
  }

  @Override
  public int getHeight() {

    return this.height;
  }

  @Override
  public boolean isShaped() {

    return this.width > 0 && this.height > 0;
  }

  @Override
  public boolean isMirrored() {

    return this.mirrored;
  }

  @Override
  public int getToolCount() {

    return this.tools.length;
  }

  @Override
  public boolean matches(
      Collection<String> unlockedStages,
      int playerExperienceTotal,
      int playerLevels,
      boolean isPlayerCreative,
      ItemStack[] tools,
      ICraftingMatrixStackHandler craftingMatrix,
      FluidStack fluidStack,
      ISecondaryIngredientMatcher secondaryIngredientMatcher,
      EnumTier tier
  ) {

    if (!this.matchTier(tier)) {
      return false;
    }

    if (!isPlayerCreative) {

      if (playerExperienceTotal < this.experienceRequired) {
        return false;
      }

      if (playerLevels < this.levelRequired) {
        return false;
      }
    }

    if (this.getToolCount() > tools.length) {
      // this recipe requires more tools than the tools in the table
      return false;
    }

    // Do we have the correct tools?
    // Do the tools have enough durability for this recipe?
    for (int i = 0; i < this.getToolCount(); i++) {

      if (!this.isValidTool(tools[i], i)
          || !this.hasSufficientToolDurability(tools[i], i)) {
        return false;
      }
    }

    // match gamestages
    if (ModuleWorktables.MOD_LOADED_GAMESTAGES) {

      if (!this.gameStageMatcher.matches(unlockedStages)) {
        return false;
      }
    }

    if (!this.recipeMatrixMatcher.matches(this, craftingMatrix, fluidStack)) {
      return false;
    }

    if (!this.secondaryIngredients.isEmpty()) {
      return secondaryIngredientMatcher.matches(this.secondaryIngredients);
    }

    return true;
  }

  @Override
  public boolean matchTier(EnumTier tier) {

    return this.minimumTier <= tier.getId()
        && this.maximumTier >= tier.getId();
  }
}
