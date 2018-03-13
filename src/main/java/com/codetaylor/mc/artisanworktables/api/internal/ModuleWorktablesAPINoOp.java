package com.codetaylor.mc.artisanworktables.api.internal;

import com.codetaylor.mc.artisanworktables.api.IModuleWorktablesAPI;
import com.codetaylor.mc.artisanworktables.api.config.IModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ModuleWorktablesAPINoOp
    implements IModuleWorktablesAPI {

  @Override
  public boolean isModLoadedGameStages() {

    return false;
  }

  @Override
  public IModuleWorktablesConfig getModuleWorktablesConfig() {

    return null;
  }

  @Override
  public List<String> getWorktableNames() {

    return null;
  }

  @Override
  public RecipeRegistry getWorktableRecipeRegistry(String name) {

    return null;
  }

  @Override
  public boolean containsRecipeWithTool(ItemStack itemStack) {

    return false;
  }

  @Override
  public boolean isWorktableNameValid(String name) {

    return false;
  }
}
