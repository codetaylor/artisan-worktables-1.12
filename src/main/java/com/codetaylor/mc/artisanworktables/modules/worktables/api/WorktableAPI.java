package com.codetaylor.mc.artisanworktables.modules.worktables.api;

import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumWorktableType;

import java.util.HashMap;
import java.util.Map;

public class WorktableAPI {

  private static final Map<String, RegistryRecipeWorktable> RECIPE_REGISTRY_MAP = new HashMap<>();

  static {

    EnumWorktableType[] values = EnumWorktableType.values();

    for (EnumWorktableType type : values) {
      RECIPE_REGISTRY_MAP.put(type.getName(), new RegistryRecipeWorktable());
    }

    RECIPE_REGISTRY_MAP.put("mage", new RegistryRecipeWorktable());
  }

  public static RegistryRecipeWorktable getRecipeRegistry(EnumWorktableType type) {

    return WorktableAPI.getRecipeRegistry(type.getName());
  }

  public static RegistryRecipeWorktable getRecipeRegistry(String name) {

    return RECIPE_REGISTRY_MAP.get(name);
  }

  private WorktableAPI() {
    //
  }

}
