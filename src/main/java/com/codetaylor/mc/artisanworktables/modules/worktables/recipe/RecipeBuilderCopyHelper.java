package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class RecipeBuilderCopyHelper {

  public static RecipeBuilder copyRecipe(IRecipe recipe, RecipeBuilder builder) throws RecipeBuilderException {

    if (recipe instanceof IShapedRecipe) {
      return RecipeBuilderCopyHelper.copyShapedRecipe((IShapedRecipe) recipe, builder);

    } else {
      return RecipeBuilderCopyHelper.copyShapelessRecipe(recipe, builder);
    }
  }

  public static RecipeBuilder copyShapedRecipe(
      IShapedRecipe recipe,
      RecipeBuilder builder
  ) throws RecipeBuilderException {

    // Copy ingredients
    RecipeBuilderCopyHelper.copyShapedRecipeInput(recipe, builder);

    // Copy mirrored
    // Can't copy mirroring because it isn't exposed in IShapedRecipe

    // Copy output
    RecipeBuilderCopyHelper.copyRecipeOutput(recipe, builder);

    return builder;
  }

  public static RecipeBuilder copyShapelessRecipe(IRecipe recipe, RecipeBuilder builder) throws RecipeBuilderException {

    // Copy ingredients
    RecipeBuilderCopyHelper.copyShapelessRecipeInput(recipe, builder);

    // Copy output
    RecipeBuilderCopyHelper.copyRecipeOutput(recipe, builder);

    return builder;
  }

  public static RecipeBuilder copyRecipeInput(IRecipe recipe, RecipeBuilder builder) throws RecipeBuilderException {

    if (recipe instanceof IShapedRecipe) {
      return RecipeBuilderCopyHelper.copyShapedRecipeInput((IShapedRecipe) recipe, builder);

    } else {
      return RecipeBuilderCopyHelper.copyRecipeInput(recipe, builder);
    }
  }

  public static RecipeBuilder copyShapedRecipeInput(
      IShapedRecipe recipe,
      RecipeBuilder builder
  ) throws RecipeBuilderException {

    int width = recipe.getRecipeWidth();
    int height = recipe.getRecipeHeight();

    NonNullList<Ingredient> ingredients = recipe.getIngredients();
    Ingredient[][] convertedIngredients = new Ingredient[width][height];
    int index = 0;

    for (int x = 0; x < width; x++) {

      for (int y = 0; y < height; y++) {
        convertedIngredients[x][y] = ingredients.get(index);
        index += 1;
      }
    }

    builder.setIngredients(convertedIngredients);
    return builder;
  }

  public static RecipeBuilder copyShapelessRecipeInput(
      IRecipe recipe,
      RecipeBuilder builder
  ) throws RecipeBuilderException {

    NonNullList<Ingredient> ingredients = recipe.getIngredients();
    Ingredient[] convertedIngredients = new Ingredient[ingredients.size()];

    for (int i = 0; i < convertedIngredients.length; i++) {
      convertedIngredients[i] = ingredients.get(i);
    }

    builder.setIngredients(convertedIngredients);
    return builder;
  }

  public static RecipeBuilder copyRecipeOutput(IRecipe recipe, RecipeBuilder builder) throws RecipeBuilderException {

    builder.addOutput(recipe.getRecipeOutput().copy(), 1);
    return builder;
  }

  private RecipeBuilderCopyHelper() {
    //
  }

}
