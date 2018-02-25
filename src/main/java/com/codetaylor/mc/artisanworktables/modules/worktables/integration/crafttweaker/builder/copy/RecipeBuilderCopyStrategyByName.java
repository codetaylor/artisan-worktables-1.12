package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderException;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Map;
import java.util.Set;

public class RecipeBuilderCopyStrategyByName
    extends RecipeBuilderCopyStrategyBase {

  private String recipeName;

  public RecipeBuilderCopyStrategyByName(String recipeName) throws RecipeBuilderException {

    if (recipeName == null) {
      throw new RecipeBuilderException("Recipe name to copy can't be null");
    }

    this.recipeName = recipeName;
  }

  @Override
  public void apply() {

    try {
      Set<Map.Entry<ResourceLocation, IRecipe>> entries = ForgeRegistries.RECIPES.getEntries();

      for (Map.Entry<ResourceLocation, IRecipe> recipeEntry : entries) {
        String recipeName = recipeEntry.getKey().toString();

        if (recipeName.equals(this.recipeName)) {
          this.doCopy(recipeEntry.getValue(), this.builder);
          break;
        }
      }

    } catch (Exception e) {
      CTLogHelper.logError("Unable to copy and register recipe", e);
    }
  }

  @Override
  public String describe() {

    return "RecipeCopyStrategyByName[" + this.recipeName + "]";
  }
}
