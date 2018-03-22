package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirement;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.recipes.CraftingInfo;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

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

  /* package */ CTArtisanRecipe(
      Map<ResourceLocation, IMatchRequirement> requirementMap,
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
      @Nullable IRecipeFunction recipeFunction
  ) {

    super(
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
        maximumTier
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
      marks = this.getMarksShaped(context, marks);

    } else {
      marks = this.getMarksShapeless(context, marks);
    }

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

  private Map<String, IItemStack> getMarksShapeless(ICraftingContext context, Map<String, IItemStack> marks) {

    List<ItemStack> itemList = new ArrayList<>();
    List<IArtisanIngredient> ingredients = this.getIngredientList();
    ICraftingMatrixStackHandler matrixHandler = context.getCraftingMatrixHandler();

    for (int i = 0; i < matrixHandler.getSlots(); i++) {
      ItemStack itemStack = matrixHandler.getStackInSlot(i);

      if (!itemStack.isEmpty()) {
        itemList.add(itemStack);
      }
    }

    List<Ingredient> ingredientList = new ArrayList<>(ingredients.size());

    for (IArtisanIngredient ingredient : ingredients) {
      ingredientList.add(ingredient.toIngredient());
    }

    int[] matches = RecipeMatcher.findMatches(itemList, ingredientList);

    for (int i = 0; i < matches.length; i++) {
      int index = matches[i];
      IArtisanIngredient artisanIngredient = ingredients.get(index);

      if (!artisanIngredient.isEmpty()) {
        CTArtisanIngredient ingredient = (CTArtisanIngredient) artisanIngredient;
        String mark = ingredient.getIngredient().getMark();

        if (mark != null) {
          marks.put(mark, CraftTweakerMC.getIItemStack(itemList.get(i)));
        }
      }
    }

    return marks;
  }

  private Map<String, IItemStack> getMarksShaped(ICraftingContext context, Map<String, IItemStack> marks) {

    int width = this.getWidth();
    int height = this.getHeight();
    boolean mirrored = this.isMirrored();
    List<IArtisanIngredient> ingredients = this.getIngredientList();
    ICraftingMatrixStackHandler matrixHandler = context.getCraftingMatrixHandler();
    IItemStack[] stacks = new IItemStack[ingredients.size()];

    foundStacks:
    for (int x = 0; x <= matrixHandler.getWidth() - width; ++x) {

      for (int y = 0; y <= matrixHandler.getHeight() - height; ++y) {

        if (this.getStacksArray(stacks, ingredients, matrixHandler, x, y, width, height, false)) {
          break foundStacks;
        }

        if (mirrored && this.getStacksArray(stacks, ingredients, matrixHandler, x, y, width, height, true)) {
          break foundStacks;
        }
      }
    }

    for (int i = 0; i < ingredients.size(); i++) {
      IArtisanIngredient artisanIngredient = ingredients.get(i);

      if (!artisanIngredient.isEmpty()) {
        CTArtisanIngredient ctArtisanIngredient = (CTArtisanIngredient) artisanIngredient;
        String mark = ctArtisanIngredient.getIngredient().getMark();

        if (mark != null) {
          marks.put(mark, stacks[i]);
        }
      }
    }

    return marks;
  }

  private boolean getStacksArray(
      IItemStack stacks[],
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
        IArtisanIngredient ingredient = ArtisanIngredient.EMPTY;
        int index = 0;

        if (subX >= 0 && subY >= 0 && subX < width && subY < height) {

          if (mirror) {
            index = width - subX - 1 + subY * width;
            ingredient = ingredients.get(index);

          } else {
            index = subX + subY * width;
            ingredient = ingredients.get(index);
          }
        }

        ItemStack candidate = craftingMatrix.getStackInSlot(x + y * craftingMatrix.getWidth());

        if (!ingredient.matches(candidate)) {
          return false;
        }

        stacks[index] = CraftTweakerMC.getIItemStack(candidate);
      }
    }

    return true;
  }

  private CraftingInfo getCraftingInfo(ICraftingContext context) {

    return new CraftingInfo(
        new CTArtisanCraftingInventory(
            CraftTweakerMC.getIPlayer(context.getPlayer()),
            context.getCraftingMatrixHandler()
        ),
        CraftTweakerMC.getIWorld(context.getWorld())
    );
  }
}
