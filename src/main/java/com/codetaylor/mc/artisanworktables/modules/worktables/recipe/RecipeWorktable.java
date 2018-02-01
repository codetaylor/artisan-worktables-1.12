package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.CraftingMatrixStackHandler;
import com.codetaylor.mc.athenaeum.util.WeightedPicker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RecipeWorktable
    implements IRecipeWorktable {

  private IGameStageMatcher gameStageMatcher;
  private ItemStack[] tools;
  private int toolDamage;
  private List<OutputWeightPair> output;
  private List<Ingredient> ingredients;
  private FluidStack fluidIngredient;
  private ExtraOutputChancePair[] extraOutputs;
  private IRecipeMatcher recipeMatcher;
  private boolean mirrored;
  private int width;
  private int height;

  public RecipeWorktable(
      IGameStageMatcher gameStageMatcher,
      List<OutputWeightPair> output,
      ItemStack[] tools,
      int toolDamage,
      List<Ingredient> ingredients,
      @Nullable FluidStack fluidIngredient,
      ExtraOutputChancePair[] extraOutputs,
      IRecipeMatcher recipeMatcher,
      boolean mirrored,
      int width,
      int height
  ) {

    this.gameStageMatcher = gameStageMatcher;
    this.output = output;
    this.tools = tools;
    this.toolDamage = toolDamage;
    this.ingredients = ingredients;
    this.fluidIngredient = fluidIngredient;
    this.extraOutputs = extraOutputs;
    this.recipeMatcher = recipeMatcher;
    this.mirrored = mirrored;
    this.width = width;
    this.height = height;
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
  public boolean isValidTool(ItemStack tool) {

    for (ItemStack itemStack : this.tools) {

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
  public ItemStack[] getTools() {

    return this.tools;
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
  public int getToolDamage() {

    return this.toolDamage;
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
  public boolean matches(
      Collection<String> unlockedStages,
      ItemStack tool,
      CraftingMatrixStackHandler craftingMatrix,
      FluidStack fluidStack
  ) {

    // Do we have the correct tool?
    if (!this.isValidTool(tool)) {
      return false;
    }

    // Does the tool have enough durability for this recipe?
    /*if (tool.getItemDamage() + this.toolDamage > tool.getMaxDamage()) {
      return false;
    }*/

    if (ModuleWorktables.MOD_LOADED_GAMESTAGES) {

      if (!this.gameStageMatcher.matches(unlockedStages)) {
        return false;
      }
    }

    return this.recipeMatcher.matches(this, craftingMatrix, fluidStack);
  }

}
