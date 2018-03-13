package com.codetaylor.mc.artisanworktables.api;

import com.codetaylor.mc.artisanworktables.api.config.IModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IModuleWorktablesAPI {

  boolean isModLoadedGameStages();

  IModuleWorktablesConfig getModuleWorktablesConfig();

  List<String> getWorktableNames();

  RecipeRegistry getWorktableRecipeRegistry(String name);

  boolean containsRecipeWithTool(ItemStack itemStack);

  boolean isWorktableNameValid(String name);
}
