package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderException;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTInputHelper;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import crafttweaker.api.item.IIngredient;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RecipeBuilderCopyStrategyByOutput
    extends RecipeBuilderCopyStrategyBase {

  private IIngredient[] toCopy;

  public RecipeBuilderCopyStrategyByOutput(IIngredient[] toCopy) throws RecipeBuilderException {

    if (toCopy == null) {
      throw new RecipeBuilderException("Recipe ingredient to copy can't be null");
    }

    for (IIngredient ingredient : toCopy) {

      if (ingredient == null) {
        throw new RecipeBuilderException("Recipe ingredient to copy can't be null");
      }
    }

    this.toCopy = toCopy;
  }

  @Override
  public void apply() {

    try {
      Collection<IRecipe> recipes = ForgeRegistries.RECIPES.getValuesCollection();

      Set<IRecipe> toCopy = new HashSet<>();

      for (IRecipe recipe : recipes) {

        for (IIngredient copyRecipe : this.toCopy) {

          if (!recipe.getRecipeOutput().isEmpty()
              && copyRecipe.matches(CTInputHelper.toIItemStack(recipe.getRecipeOutput()))) {
            toCopy.add(recipe);
          }
        }
      }

      for (IRecipe recipe : toCopy) {
        this.doCopy(recipe, this.builder.copy());
      }

    } catch (Exception e) {
      CTLogHelper.logError("Unable to copy and register recipe", e);
    }
  }

  @Override
  public String describe() {

    return "RecipeCopyStrategyByOutput[" + Arrays.toString(this.toCopy) + "]";
  }
}
