package com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanIngredient;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeBuilderCopyStrategyByOutput
    extends RecipeBuilderCopyStrategyBase {

  private IArtisanIngredient[] toCopy;

  public RecipeBuilderCopyStrategyByOutput(IArtisanIngredient[] toCopy) throws RecipeBuilderException {

    if (toCopy == null) {
      throw new RecipeBuilderException("Recipe ingredient to copy can't be null");
    }

    for (IArtisanIngredient ingredient : toCopy) {

      if (ingredient == null) {
        throw new RecipeBuilderException("Recipe ingredient to copy can't be null");
      }
    }

    this.toCopy = toCopy;
  }

  @Override
  public void apply(RecipeBuilderInternal recipeBuilder, List<RecipeBuilderInternal> resultList) {

    try {
      Collection<IRecipe> recipes = ForgeRegistries.RECIPES.getValuesCollection();

      Set<IRecipe> toCopy = new HashSet<>();

      for (IRecipe recipe : recipes) {

        for (IArtisanIngredient copyRecipe : this.toCopy) {

          if (!recipe.getRecipeOutput().isEmpty()
              && copyRecipe.matches(recipe.getRecipeOutput())) {
            toCopy.add(recipe);
          }
        }
      }

      for (IRecipe recipe : toCopy) {
        this.doCopy(recipe, recipeBuilder.copy(), resultList);
      }

    } catch (Exception e) {
      CTLogHelper.logError("Unable to copy and register recipe", e);
    }
  }

}
