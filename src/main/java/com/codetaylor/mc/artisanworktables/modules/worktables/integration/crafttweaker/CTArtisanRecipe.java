package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.ArtisanToolHandlers;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.internal.util.Util;
import com.codetaylor.mc.artisanworktables.api.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.IToolHandler;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.recipes.CraftingInfo;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CTArtisanRecipe
    extends ArtisanRecipe {

  private final IRecipeAction recipeAction;
  private final IRecipeFunction recipeFunction;

  public CTArtisanRecipe(
      String name,
      Map<ResourceLocation, IRequirement> requirementMap,
      List<OutputWeightPair> output,
      ToolEntry[] tools,
      List<IArtisanIngredient> ingredients,
      List<IArtisanIngredient> secondaryIngredients,
      boolean consumeSecondaryIngredients,
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
      int maximumTier,
      @Nullable IRecipeAction recipeAction,
      @Nullable IRecipeFunction recipeFunction,
      boolean hidden
  ) {

    super(
        name,
        requirementMap,
        output,
        tools,
        ingredients,
        secondaryIngredients,
        consumeSecondaryIngredients,
        fluidIngredient,
        experienceRequired,
        levelRequired,
        consumeExperience,
        extraOutputs,
        recipeMatrixMatcher,
        mirrored,
        width,
        height,
        minimumTier,
        maximumTier,
        hidden
    );

    this.recipeAction = recipeAction;
    this.recipeFunction = recipeFunction;
  }

  @Override
  public IArtisanItemStack getBaseOutput(ICraftingContext context) {

    IArtisanItemStack output = super.getBaseOutput(context);

    if (this.recipeFunction != null) {
      return this.processRecipeFunction(this.recipeFunction, output, context);
    }

    return output;
  }

  @Nonnull
  @Override
  protected List<ItemStack> getRemainingItems(
      ICraftingContext context,
      NonNullList<ItemStack> itemStacks
  ) {

    MatchInfo matchInfo;

    if (this.isShaped()) {
      matchInfo = this.getStacksShaped(context);

    } else {
      matchInfo = this.getStacksShapeless(context);
    }

    ICraftingMatrixStackHandler matrixHandler = context.getCraftingMatrixHandler();
    List<IArtisanIngredient> ingredients = this.getIngredientList();
    IItemStack[] stacks = matchInfo.getStacks();
    int[] matrixIndices = matchInfo.getMatrixIndices();

    for (int i = 0; i < ingredients.size(); i++) {
      IArtisanIngredient artisanIngredient = ingredients.get(i);

      if (artisanIngredient.isEmpty()) {
        continue;
      }

      if (artisanIngredient instanceof CTArtisanIngredient) {
        IIngredient ingredient = ((CTArtisanIngredient) artisanIngredient).getIngredient();

        if (ingredient.hasNewTransformers()) {
          IItemStack remainingItem = null;

          try {
            remainingItem = ingredient.applyNewTransform(stacks[i]);

          } catch (Throwable e) {
            CraftTweakerAPI.logError("Could not execute NewRecipeTransformer on " + ingredient.toCommandString(), e);
          }

          itemStacks.set(matrixIndices[i], CraftTweakerMC.getItemStack(remainingItem));
        }

      } else {
        itemStacks.set(matrixIndices[i], Util.getContainerItem(matrixHandler.getStackInSlot(matrixIndices[i])));
      }
    }

    return itemStacks;
  }

  @Override
  protected ItemStack getRemainingSecondaryItem(
      ICraftingContext context,
      IArtisanIngredient artisanIngredient,
      ItemStack stack
  ) {

    if (!artisanIngredient.isEmpty()) {

      IIngredient ingredient = ((CTArtisanIngredient) artisanIngredient).getIngredient();

      if (ingredient.hasNewTransformers()) {
        IItemStack remainingItem = null;

        try {
          remainingItem = ingredient.applyNewTransform(CraftTweakerMC.getIItemStack(stack));

        } catch (Throwable e) {
          CraftTweakerAPI.logError("Could not execute NewRecipeTransformer on " + ingredient.toCommandString(), e);
        }

        return CraftTweakerMC.getItemStack(remainingItem);
      }
    }

    return super.getRemainingSecondaryItem(context, artisanIngredient, stack);
  }

  @Override
  protected void onCraftCompleteServer(
      ItemStack craftedItem,
      List<IArtisanItemStack> extraOutputList,
      ICraftingContext context
  ) {

    super.onCraftCompleteServer(craftedItem, extraOutputList, context);

    if (this.recipeAction != null) {

      try {
        this.recipeAction.process(
            CraftTweakerMC.getIItemStack(craftedItem.copy()),
            this.getCraftingInfo(context),
            CraftTweakerMC.getIPlayer(context.getPlayer())
        );

      } catch (Throwable e) {
        CraftTweakerAPI.logError("Error executing RecipeAction", e);
      }
    }
  }

  @ParametersAreNonnullByDefault
  private IArtisanItemStack processRecipeFunction(
      IRecipeFunction recipeFunction,
      IArtisanItemStack output,
      ICraftingContext context
  ) {

    Map<String, IItemStack> marks = new HashMap<>();

    if (this.isShaped()) {
      this.getMarksShaped(context, marks);

    } else {
      this.getMarksShapeless(context, marks);
    }

    this.getMarksSecondaryIngredients(context, marks);
    this.getMarksTools(context, marks);

    try {
      IItemStack functionResult = recipeFunction.process(
          ((CTArtisanItemStack) output).getIItemStack(),
          marks,
          this.getCraftingInfo(context)
      );

      output = ArtisanItemStack.from(CraftTweakerMC.getItemStack(functionResult));

    } catch (Throwable e) {
      CraftTweakerAPI.logError("Error executing RecipeFunction", e);
    }

    return output;
  }

  private void getMarksTools(ICraftingContext context, Map<String, IItemStack> marks) {

    ToolEntry[] toolEntries = this.getToolEntries();
    IItemHandlerModifiable contextToolHandler = context.getToolHandler();

    if (toolEntries == null || toolEntries.length == 0) {
      return;
    }

    toolSearch:
    for (ToolEntry toolEntry : toolEntries) {
      IArtisanIngredient tool = toolEntry.getTool();

      for (int i = 0; i < contextToolHandler.getSlots(); i++) {
        ItemStack stackInSlot = contextToolHandler.getStackInSlot(i);

        if (stackInSlot.isEmpty()) {
          continue;
        }

        IToolHandler handler = ArtisanToolHandlers.get(stackInSlot);

        if (tool instanceof CTArtisanIngredient && toolEntry.matches(handler, stackInSlot)) {
          String mark = ((CTArtisanIngredient) tool).getIngredient().getMark();

          if (mark != null) {

            if (!marks.containsKey(mark)) {
              marks.put(mark, CraftTweakerMC.getIItemStack(stackInSlot.copy()));
            }

            break toolSearch;
          }
        }
      }
    }
  }

  private void getMarksSecondaryIngredients(ICraftingContext context, Map<String, IItemStack> marks) {

    List<IArtisanIngredient> secondaryIngredients = this.getSecondaryIngredients();
    IItemHandlerModifiable secondaryIngredientHandler = context.getSecondaryIngredientHandler();

    if (secondaryIngredients.isEmpty() || secondaryIngredientHandler == null) {
      return;
    }

    ingredientSearch:
    for (IArtisanIngredient ingredient : secondaryIngredients) {

      for (int i = 0; i < secondaryIngredientHandler.getSlots(); i++) {
        ItemStack stackInSlot = secondaryIngredientHandler.getStackInSlot(i);

        if (stackInSlot.isEmpty()) {
          continue;
        }

        if (ingredient instanceof CTArtisanIngredient && ingredient.matchesIgnoreAmount(stackInSlot)) {
          String mark = ((CTArtisanIngredient) ingredient).getIngredient().getMark();

          if (mark != null) {

            if (!marks.containsKey(mark)) {
              marks.put(mark, CraftTweakerMC.getIItemStack(stackInSlot.copy()));
            }

            break ingredientSearch;
          }
        }
      }
    }
  }

  private void getMarksShapeless(ICraftingContext context, Map<String, IItemStack> marks) {

    List<IArtisanIngredient> ingredients = this.getIngredientList();
    IItemStack[] stacks = this.getStacksShapeless(context).getStacks();

    for (int i = 0; i < ingredients.size(); i++) {
      IArtisanIngredient ingredient = ingredients.get(i);

      if (ingredient instanceof CTArtisanIngredient) {
        String mark = ((CTArtisanIngredient) ingredient).getIngredient().getMark();

        if (mark != null) {
          marks.put(mark, stacks[i]);
        }
      }
    }
  }

  private void getMarksShaped(ICraftingContext context, Map<String, IItemStack> marks) {

    List<IArtisanIngredient> ingredients = this.getIngredientList();
    IItemStack[] stacks = this.getStacksShaped(context).getStacks();

    for (int i = 0; i < ingredients.size(); i++) {
      IArtisanIngredient ingredient = ingredients.get(i);

      if (ingredient instanceof CTArtisanIngredient) {
        String mark = ((CTArtisanIngredient) ingredient).getIngredient().getMark();

        if (mark != null) {
          marks.put(mark, stacks[i]);
        }
      }
    }
  }

  /**
   * @return the matrix items re-ordered to match the recipe's ingredient list order
   */
  private MatchInfo getStacksShapeless(ICraftingContext context) {

    List<IArtisanIngredient> ingredients = this.getIngredientList();
    ICraftingMatrixStackHandler matrixHandler = context.getCraftingMatrixHandler();
    List<ItemStack> itemList = new ArrayList<>(matrixHandler.getSlots());
    List<Integer> indexList = new ArrayList<>(matrixHandler.getSlots());

    IItemStack[] stacks = new IItemStack[ingredients.size()];
    int[] indices = new int[ingredients.size()];

    for (int i = 0; i < matrixHandler.getSlots(); i++) {
      ItemStack itemStack = matrixHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        itemList.add(itemStack);
        indexList.add(i);
      }
    }

    List<Ingredient> ingredientList = new ArrayList<>(ingredients.size());

    for (IArtisanIngredient ingredient : ingredients) {
      ingredientList.add(ingredient.toIngredient());
    }

    int[] matches = RecipeMatcher.findMatches(itemList, ingredientList);

    for (int i = 0; i < matches.length; i++) {
      stacks[matches[i]] = CraftTweakerMC.getIItemStack(itemList.get(i));
      indices[matches[i]] = indexList.get(i);
    }

    return new MatchInfo(stacks, indices);
  }

  /**
   * @return the matrix items re-ordered to match the recipe's ingredient list order
   */
  private MatchInfo getStacksShaped(ICraftingContext context) {

    int width = this.getWidth();
    int height = this.getHeight();
    boolean mirrored = this.isMirrored();
    List<IArtisanIngredient> ingredients = this.getIngredientList();
    ICraftingMatrixStackHandler matrixHandler = context.getCraftingMatrixHandler();

    IItemStack[] stacks = new IItemStack[ingredients.size()];
    int[] indices = new int[ingredients.size()];

    foundStacks:
    for (int x = 0; x <= matrixHandler.getWidth() - width; ++x) {

      for (int y = 0; y <= matrixHandler.getHeight() - height; ++y) {

        if (this.getStacksArray(stacks, indices, ingredients, matrixHandler, x, y, width, height, false)) {
          break foundStacks;
        }

        if (mirrored && this.getStacksArray(stacks, indices, ingredients, matrixHandler, x, y, width, height, true)) {
          break foundStacks;
        }
      }
    }

    return new MatchInfo(stacks, indices);
  }

  /**
   * Sets the given stacks array to the matrix items re-ordered to match the recipe's ingredient list order.
   *
   * @return false if no recipe match
   */
  private boolean getStacksArray(
      IItemStack[] stacks,
      int[] indices,
      List<IArtisanIngredient> ingredients,
      ICraftingMatrixStackHandler craftingMatrix,
      int startX,
      int startY,
      int width,
      int height,
      boolean mirror
  ) {

    for (int x = 0; x < craftingMatrix.getWidth(); ++x) {

      for (int y = 0; y < craftingMatrix.getHeight(); ++y) {

        int subX = x - startX;
        int subY = y - startY;
        IArtisanIngredient ingredient;
        int index;

        if (subX >= 0 && subY >= 0 && subX < width && subY < height) {

          if (mirror) {
            index = width - subX - 1 + subY * width;
            ingredient = ingredients.get(index);

          } else {
            index = subX + subY * width;
            ingredient = ingredients.get(index);
          }

        } else {
          continue;
        }

        int matrixIndex = x + y * craftingMatrix.getWidth();
        ItemStack candidate = craftingMatrix.getStackInSlot(matrixIndex);

        if (!ingredient.matches(candidate)) {
          return false;
        }

        // index is the recipe's ingredient index
        stacks[index] = CraftTweakerMC.getIItemStack(candidate);
        indices[index] = matrixIndex;
      }
    }

    return true;
  }

  /**
   * Creates and returns a {@link CraftingInfo} object.
   *
   * @param context the context
   * @return a {@link CraftingInfo} object
   */
  private CraftingInfo getCraftingInfo(ICraftingContext context) {

    return new CraftingInfo(
        new CTArtisanCraftingInventory(
            CraftTweakerMC.getIPlayer(context.getPlayer()),
            context.getCraftingMatrixHandler()
        ),
        CraftTweakerMC.getIWorld(context.getWorld())
    );
  }

  private class MatchInfo {

    private final IItemStack[] stacks;
    private final int[] matrixIndices;

    private MatchInfo(IItemStack[] stacks, int[] matrixIndices) {

      this.stacks = stacks;
      this.matrixIndices = matrixIndices;
    }

    public IItemStack[] getStacks() {

      return this.stacks;
    }

    public int[] getMatrixIndices() {

      return this.matrixIndices;
    }
  }
}
