package com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanIngredient;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanItemStack;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.InputReplacements;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ArtisanIngredient;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ArtisanItemStack;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.IRecipeBuilder;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class RecipeBuilderCopyHelper {

  public static IRecipeBuilder copyRecipeInput(
      IRecipe recipe,
      InputReplacements inputReplacements,
      IRecipeBuilder builder
  ) throws RecipeBuilderException {

    if (recipe instanceof IShapedRecipe) {
      return RecipeBuilderCopyHelper.copyShapedRecipeInput((IShapedRecipe) recipe, inputReplacements, builder);

    } else {
      return RecipeBuilderCopyHelper.copyShapelessRecipeInput(recipe, inputReplacements, builder);
    }
  }

  public static IRecipeBuilder copyShapedRecipeInput(
      IShapedRecipe recipe,
      InputReplacements inputReplacements,
      IRecipeBuilder builder
  ) throws RecipeBuilderException {

    int width = recipe.getRecipeWidth();
    int height = recipe.getRecipeHeight();

    NonNullList<Ingredient> ingredients = recipe.getIngredients();
    IArtisanIngredient[][] convertedIngredients = new IArtisanIngredient[height][width];
    int index = 0;

    for (int row = 0; row < height; row++) {

      for (int col = 0; col < width; col++) {
        convertedIngredients[row][col] = ArtisanIngredient.from(ingredients.get(index));
        index += 1;
      }
    }

    width = Math.max(width, inputReplacements.getWidth());
    height = Math.max(height, inputReplacements.getHeight());

    IArtisanIngredient[][] replacedIngredients = new IArtisanIngredient[height][width];

    for (int col = 0; col < width; col++) {

      for (int row = 0; row < height; row++) {

        if (row >= convertedIngredients.length) {
          replacedIngredients[row][col] = inputReplacements.replace(col, row, null);

        } else if (col >= convertedIngredients[row].length) {
          replacedIngredients[row][col] = inputReplacements.replace(col, row, null);

        } else {
          replacedIngredients[row][col] = inputReplacements.replace(col, row, convertedIngredients[row][col]);
        }
      }
    }

    builder.setIngredients(replacedIngredients);
    return builder;
  }

  public static IRecipeBuilder copyShapelessRecipeInput(
      IRecipe recipe,
      InputReplacements inputReplacements,
      IRecipeBuilder builder
  ) throws RecipeBuilderException {

    NonNullList<Ingredient> ingredients = recipe.getIngredients();
    IArtisanIngredient[] convertedIngredients = new IArtisanIngredient[ingredients.size()];

    for (int i = 0; i < convertedIngredients.length; i++) {
      convertedIngredients[i] = inputReplacements.replace(ArtisanIngredient.from(ingredients.get(i)));
    }

    builder.setIngredients(convertedIngredients);
    return builder;
  }

  public static IRecipeBuilder copyRecipeOutput(IRecipe recipe, IRecipeBuilder builder) throws RecipeBuilderException {

    builder.addOutput(ArtisanItemStack.from(recipe.getRecipeOutput().copy()), 1);
    return builder;
  }

  public static IRecipeBuilder replaceRecipeOutput(
      IRecipe recipe,
      IArtisanItemStack toReplace,
      IRecipeBuilder builder
  ) throws RecipeBuilderException {

    ItemStack itemStack = toReplace.toItemStack();
    int count = recipe.getRecipeOutput().getCount();
    itemStack.setCount(count);
    builder.addOutput(ArtisanItemStack.from(itemStack), 1);
    return builder;
  }

  private RecipeBuilderCopyHelper() {
    //
  }

}
