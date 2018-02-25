package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder.copy;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RecipeBuilderException;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.builder.copy.ICopyStrategy")
@SuppressWarnings("UnusedReturnValue")
public interface IZenRecipeBuilderCopyStrategy {

  @ZenMethod
  IZenRecipeBuilderCopyStrategy excludeInput();

  @ZenMethod
  IZenRecipeBuilderCopyStrategy excludeOutput();

  @ZenMethod
  IZenRecipeBuilderCopyStrategy replaceOutput(IItemStack replacement) throws RecipeBuilderException;

}
