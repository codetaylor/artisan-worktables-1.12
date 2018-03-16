package com.codetaylor.mc.artisanworktables.modules.worktables.recipe.copy;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderInternal;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecipeBuilderCopyStrategyByName
    extends RecipeBuilderCopyStrategyBase {

  private String recipeName;

  public RecipeBuilderCopyStrategyByName(
      String recipeName
  ) throws RecipeBuilderException {

    if (recipeName == null) {
      throw new RecipeBuilderException("Recipe name to copy can't be null");
    }

    this.recipeName = recipeName;
  }

  @Override
  public void apply(RecipeBuilderInternal recipeBuilder, List<RecipeBuilderInternal> resultList) throws RecipeBuilderException {

    try {
      Set<Map.Entry<ResourceLocation, IRecipe>> entries = ForgeRegistries.RECIPES.getEntries();

      for (Map.Entry<ResourceLocation, IRecipe> recipeEntry : entries) {
        String recipeName = recipeEntry.getKey().toString();

        if (recipeName.equals(this.recipeName)) {
          this.doCopy(recipeEntry.getValue(), recipeBuilder, resultList);
          break;
        }
      }

    } catch (Exception e) {
      throw new RecipeBuilderException("Unable to copy and register recipe", e);
    }
  }

}
