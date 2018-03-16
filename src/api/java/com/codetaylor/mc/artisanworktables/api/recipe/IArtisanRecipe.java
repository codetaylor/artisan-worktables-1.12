package com.codetaylor.mc.artisanworktables.api.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public interface IArtisanRecipe {

  boolean matches(
      Collection<String> unlockedStages,
      int playerExperienceTotal,
      int playerLevels,
      boolean isPlayerCreative,
      ItemStack[] tools,
      ICraftingMatrixStackHandler craftingMatrix,
      FluidStack fluidStack,
      ISecondaryIngredientMatcher secondaryIngredientMatcher,
      EnumTier tier
  );

  int getWidth();

  int getHeight();

  boolean isShaped();

  boolean isMirrored();

  boolean matchGameStages(Collection<String> unlockedStages);

  IArtisanItemStack getSecondaryOutput();

  float getSecondaryOutputChance();

  IArtisanItemStack getTertiaryOutput();

  float getTertiaryOutputChance();

  IArtisanItemStack getQuaternaryOutput();

  float getQuaternaryOutputChance();

  @Nonnull
  List<IArtisanIngredient> getSecondaryIngredients();

  boolean consumeSecondaryIngredients();

  boolean isValidTool(ItemStack tool, int toolIndex);

  boolean hasSufficientToolDurability(ItemStack tool, int toolIndex);

  IArtisanItemStack[] getTools(int toolIndex);

  int getToolDamage(int toolIndex);

  int getToolCount();

  List<IArtisanIngredient> getIngredientList();

  @Nullable
  FluidStack getFluidIngredient();

  List<OutputWeightPair> getOutputWeightPairList();

  IArtisanItemStack selectOutput(Random random);

  IArtisanItemStack getBaseOutput();

  boolean hasMultipleWeightedOutputs();

  boolean matchTier(EnumTier tier);

  int getExperienceRequired();

  int getLevelRequired();

  boolean consumeExperience();

  void doCraft(ICraftingContext context);
}
