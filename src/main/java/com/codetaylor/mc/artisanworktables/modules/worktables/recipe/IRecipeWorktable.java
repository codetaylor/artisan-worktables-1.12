package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.CraftingMatrixStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public interface IRecipeWorktable {

  boolean matches(
      Collection<String> unlockedStages,
      ItemStack tool,
      CraftingMatrixStackHandler craftingMatrix,
      FluidStack fluidStack
  );

  int getWidth();

  int getHeight();

  boolean isShaped();

  boolean isMirrored();

  boolean matchGameStages(Collection<String> unlockedStages);

  ItemStack getSecondaryOutput();

  float getSecondaryOutputChance();

  ItemStack getTertiaryOutput();

  float getTertiaryOutputChance();

  ItemStack getQuaternaryOutput();

  float getQuaternaryOutputChance();

  boolean isValidTool(ItemStack tool, int toolIndex);

  boolean hasSufficientToolDurability(ItemStack tool, int toolIndex);

  ItemStack[] getTools(int toolIndex);

  int getToolDamage(int toolIndex);

  int getToolCount();

  List<Ingredient> getIngredientList();

  @Nullable
  FluidStack getFluidIngredient();

  List<OutputWeightPair> getOutputWeightPairList();

  ItemStack selectOutput(Random random);

  ItemStack getBaseOutput();

  boolean hasMultipleWeightedOutputs();
}
