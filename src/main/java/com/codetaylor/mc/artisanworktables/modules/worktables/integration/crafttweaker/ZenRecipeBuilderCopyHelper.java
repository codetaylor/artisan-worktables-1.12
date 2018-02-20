package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class ZenRecipeBuilderCopyHelper {

  public static IZenRecipeBuilder copyShapedRecipe(IShapedRecipe recipe, IZenRecipeBuilder builder) {

    // Copy ingredients
    int width = recipe.getRecipeWidth();
    int height = recipe.getRecipeHeight();

    NonNullList<Ingredient> ingredients = recipe.getIngredients();
    IIngredient[][] convertedIngredients = new IIngredient[width][height];
    int index = 0;

    for (int x = 0; x < width; x++) {

      for (int y = 0; y < height; y++) {
        convertedIngredients[x][y] = CraftTweakerMC.getIIngredient(ingredients.get(index));
        index += 1;
      }
    }

    builder.setShaped(convertedIngredients);

    // Copy mirrored
    // Can't copy mirroring because it isn't exposed in IShapedRecipe

    // Copy output
    builder.addOutput(CraftTweakerMC.getIItemStack(recipe.getRecipeOutput()), 1);

    return builder;
  }

  public static IZenRecipeBuilder copyShapelessRecipe(IRecipe recipe, IZenRecipeBuilder builder) {

    // Copy ingredients
    NonNullList<Ingredient> ingredients = recipe.getIngredients();
    IIngredient[] convertedIngredients = new IIngredient[ingredients.size()];

    for (int i = 0; i < convertedIngredients.length; i++) {
      convertedIngredients[i] = CraftTweakerMC.getIIngredient(ingredients.get(i));
    }

    builder.setShapeless(convertedIngredients);

    // Copy output
    builder.addOutput(CraftTweakerMC.getIItemStack(recipe.getRecipeOutput()), 1);

    return builder;
  }

  private ZenRecipeBuilderCopyHelper() {
    //
  }

}
