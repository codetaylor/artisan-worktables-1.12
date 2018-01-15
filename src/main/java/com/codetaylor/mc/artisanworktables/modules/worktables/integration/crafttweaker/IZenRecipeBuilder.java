package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.artisanworktables.RecipeBuilder")
public interface IZenRecipeBuilder {

  @ZenMethod
  IZenRecipeBuilder setShaped(IIngredient[][] ingredients);

  @ZenMethod
  IZenRecipeBuilder setShapeless(IIngredient[] ingredients);

  @ZenMethod
  IZenRecipeBuilder setMirrored();

  @ZenMethod
  IZenRecipeBuilder setTool(IIngredient tool, int damage);

  @ZenMethod
  IZenRecipeBuilder addOutput(IItemStack output, @Optional int weight);

  @ZenMethod
  IZenRecipeBuilder setExtraOutputOne(IItemStack output, float chance);

  @ZenMethod
  IZenRecipeBuilder setExtraOutputTwo(IItemStack output, float chance);

  @ZenMethod
  IZenRecipeBuilder setExtraOutputThree(IItemStack output, float chance);

  @ZenMethod
  IZenRecipeBuilder requireGameStages(String require, String[] stages);

  @ZenMethod
  IZenRecipeBuilder excludeGameStages(String[] stages);

  @ZenMethod
  void create();

}
