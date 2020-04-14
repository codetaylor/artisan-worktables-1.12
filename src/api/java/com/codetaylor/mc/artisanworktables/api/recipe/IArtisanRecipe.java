package com.codetaylor.mc.artisanworktables.api.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public interface IArtisanRecipe {

  /**
   * @return this recipe's name
   */
  String getName();

  /**
   * Return true if this recipe matches the given criteria.
   *
   * @param requirementContextMap      the requirement context map
   * @param playerExperienceTotal      the player's total experience
   * @param playerLevels               the player's total level count
   * @param isPlayerCreative           is the player in creative mode?
   * @param tools                      the tools in the crafting GUI
   * @param toolHandlers               the tools' handlers
   * @param craftingMatrix             the crafting matrix stack handler
   * @param fluidStack                 the fluid in the table
   * @param secondaryIngredientMatcher the secondary ingredient matcher
   * @param tier                       the table tier
   * @return true if this recipe matches the given criteria
   */
  boolean matches(
      Map<ResourceLocation, IRequirementContext> requirementContextMap,
      int playerExperienceTotal,
      int playerLevels,
      boolean isPlayerCreative,
      ItemStack[] tools,
      IToolHandler[] toolHandlers,
      ICraftingMatrixStackHandler craftingMatrix,
      @Nullable FluidStack fluidStack,
      ISecondaryIngredientMatcher secondaryIngredientMatcher,
      EnumTier tier
  );

  /**
   * Return true if the given tools match the required tools.
   *
   * @param tools        the tools
   * @param toolHandlers the tools' tool handlers
   * @return true if the given tools match the required tools
   */
  boolean matchesTools(ItemStack[] tools, IToolHandler[] toolHandlers);

  /**
   * @param requirementContextMap requirement contexts
   * @return true if the recipe's requirements match the given context
   */
  boolean matchesRequirements(Map<ResourceLocation, IRequirementContext> requirementContextMap);

  /**
   * @param resourceLocation requirement resource location
   * @return the recipe's requirement for the given resource location or null if none exists
   */
  @Nullable
  IRequirement getRequirement(ResourceLocation resourceLocation);

  /**
   * @return a read-only view of this recipe's requirements
   */
  Map<ResourceLocation, IRequirement> getRequirements();

  /**
   * @param handler the tool's handler
   * @param tool    the tool to check
   * @return true if the given tool is used in this recipe
   */
  boolean usesTool(IToolHandler handler, ItemStack tool);

  /**
   * @param handler
   * @param tool    the tool to check
   * @return true if the given tool has sufficient durability to complete the craft
   */
  boolean hasSufficientToolDurability(IToolHandler handler, ItemStack tool);

  /**
   * @param tier the tier to match
   * @return true if this recipe can be crafted in the given tier
   */
  boolean matchTier(EnumTier tier);

  /**
   * @return the width of this recipe if shaped, otherwise 0
   */
  int getWidth();

  /**
   * @return the height of this recipe if shaped, otherwise 0
   */
  int getHeight();

  /**
   * @return true if this recipe is shaped
   */
  boolean isShaped();

  /**
   * @return true if this recipe is shaped and mirrored
   */
  boolean isMirrored();

  /**
   * @return a copy of the secondary output
   */
  IArtisanItemStack getSecondaryOutput();

  /**
   * @return secondary output chance, [0,1]
   */
  float getSecondaryOutputChance();

  /**
   * @return a copy of the tertiary output
   */
  IArtisanItemStack getTertiaryOutput();

  /**
   * @return tertiary output chance, [0,1]
   */
  float getTertiaryOutputChance();

  /**
   * @return a copy of the quaternary output
   */
  IArtisanItemStack getQuaternaryOutput();

  /**
   * @return quaternary output chance, [0,1]
   */
  float getQuaternaryOutputChance();

  /**
   * @return an immutable list of secondary ingredients
   */
  @Nonnull
  List<IArtisanIngredient> getSecondaryIngredients();

  /**
   * @return true if this recipe should consume the required secondary ingredients
   */
  boolean consumeSecondaryIngredients();

  /**
   * @param toolIndex the tool slot index [0,2]
   * @return the array of acceptable tools for the given tool slot index
   */
  IArtisanItemStack[] getTools(int toolIndex);

  /**
   * @param toolIndex the tool slot index [0,2]
   * @return the amount of tool damage to be applied to the tool in the given slot index
   */
  int getToolDamage(int toolIndex);

  /**
   * @return the number of tools required for this recipe
   */
  int getToolCount();

  ToolEntry[] getToolEntries();

  /**
   * @return an immutable list of the recipe's ingredients
   */
  List<IArtisanIngredient> getIngredientList();

  /**
   * @return a copy of the fluid requirement, or null if there isn't one
   */
  @Nullable
  FluidStack getFluidIngredient();

  /**
   * @return the output weight pair list
   */
  List<OutputWeightPair> getOutputWeightPairList();

  /**
   * Selects and returns a random output from the list of weighted outputs.
   *
   * @param context the crafting context
   * @param random  random
   * @return the selected output
   */
  IArtisanItemStack selectOutput(ICraftingContext context, Random random);

  /**
   * Returns the first, or base, output of the recipe. If multiple weighted
   * outputs are available for this recipe, this is the output that is
   * displayed to the player in the crafting GUI.
   *
   * @param context the crafting context
   * @return the base output
   */
  IArtisanItemStack getBaseOutput(ICraftingContext context);

  /**
   * @return true if this recipe has multiple outputs
   */
  boolean hasMultipleWeightedOutputs();

  /**
   * @return the amount of experience required to craft this recipe
   */
  int getExperienceRequired();

  /**
   * @return the number of levels required to craft this recipe
   */
  int getLevelRequired();

  /**
   * @return true if crafting this recipe should consume the required experience
   */
  boolean consumeExperience();

  /**
   * @return true if the recipe should be hidden by default in JEI
   */
  boolean isHidden();

  /**
   * Perform the craft.
   *
   * @param context the crafting context
   */
  void doCraft(ICraftingContext context, @Nullable List<ItemStack> output);

  void damageTools(IItemHandlerModifiable toolStackHandler, World world, @Nullable EntityPlayer player, BlockPos position, @Nullable IItemHandler toolReplacementHandler);
}
