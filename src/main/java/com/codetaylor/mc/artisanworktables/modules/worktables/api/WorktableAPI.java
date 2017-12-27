package com.codetaylor.mc.artisanworktables.modules.worktables.api;

import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;

import java.util.HashMap;
import java.util.Map;

public class WorktableAPI {

  public static final Map<String, RegistryRecipeWorktable> RECIPE_REGISTRY_MAP = new HashMap<>();

  static {

    BlockWorktable.EnumType[] values = BlockWorktable.EnumType.values();

    for (BlockWorktable.EnumType type : values) {
      RECIPE_REGISTRY_MAP.put(type.getName(), new RegistryRecipeWorktable());
    }
  }

  private WorktableAPI() {
    //
  }

}
