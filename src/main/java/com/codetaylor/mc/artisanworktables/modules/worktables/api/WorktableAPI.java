package com.codetaylor.mc.artisanworktables.modules.worktables.api;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;

import java.util.HashMap;
import java.util.Map;

public class WorktableAPI {

  private static final Map<String, RegistryRecipeWorktable> RECIPE_REGISTRY_MAP = new HashMap<>();

  static {

    EnumWorktableType[] values = EnumWorktableType.values();

    for (EnumWorktableType type : values) {
      RECIPE_REGISTRY_MAP.put(type.getName(), new RegistryRecipeWorktable());
    }
  }

  public static RegistryRecipeWorktable getRecipeRegistry(EnumWorktableType type) {

    return RECIPE_REGISTRY_MAP.get(type.getName());
  }

  private WorktableAPI() {
    //
  }

}
