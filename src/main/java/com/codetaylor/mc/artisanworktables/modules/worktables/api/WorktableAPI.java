package com.codetaylor.mc.artisanworktables.modules.worktables.api;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktableEnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import net.minecraft.item.ItemStack;

import java.util.*;

public class WorktableAPI {

  private static final Map<String, RegistryRecipeWorktable> RECIPE_REGISTRY_MAP = new HashMap<>();

  private static final List<String> WORKTABLE_NAME_LIST = new ArrayList<>();

  static {
    WORKTABLE_NAME_LIST.addAll(Arrays.asList(BlockWorktableEnumType.NAMES));

    for (String name : WorktableAPI.getWorktableNames()) {
      RECIPE_REGISTRY_MAP.put(name, new RegistryRecipeWorktable());
    }
  }

  public static RegistryRecipeWorktable getWorktableRecipeRegistry(String name) {

    return RECIPE_REGISTRY_MAP.get(name);
  }

  public static boolean isWorktableNameValid(String name) {

    return WORKTABLE_NAME_LIST.contains(name.toLowerCase());
  }

  public static List<String> getWorktableNames() {

    return Collections.unmodifiableList(WORKTABLE_NAME_LIST);
  }

  public static ItemStack getWorktableAsItemStack(String name) {

    try {
      BlockWorktableEnumType type = BlockWorktableEnumType.fromName(name);
      return new ItemStack(ModuleWorktables.Blocks.WORKTABLE, 1, type.getMeta());

    } catch (Exception e) {
      //
    }

    return ItemStack.EMPTY;
  }

  public static boolean containsRecipeWithTool(ItemStack itemStack) {

    for (RegistryRecipeWorktable registry : RECIPE_REGISTRY_MAP.values()) {

      if (registry.containsRecipeWithTool(itemStack)) {
        return true;
      }
    }

    return false;
  }

  private WorktableAPI() {
    //
  }

}
