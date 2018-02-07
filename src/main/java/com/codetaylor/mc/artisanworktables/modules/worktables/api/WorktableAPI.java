package com.codetaylor.mc.artisanworktables.modules.worktables.api;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumTier;
import net.minecraft.item.ItemStack;

import java.util.*;

public class WorktableAPI {

  private static final Map<String, RegistryRecipeWorktable> RECIPE_REGISTRY_MAP;

  private static final List<String> WORKTABLE_NAME_LIST;

  static {

    {
      List<String> list = new ArrayList<>();
      list.addAll(Arrays.asList(EnumType.NAMES));
      WORKTABLE_NAME_LIST = Collections.unmodifiableList(new ArrayList<>(list));
    }

    {
      Map<String, RegistryRecipeWorktable> map = new HashMap<>();

      for (String name : WorktableAPI.getWorktableNames()) {
        map.put(name, new RegistryRecipeWorktable());
      }
      RECIPE_REGISTRY_MAP = Collections.unmodifiableMap(new HashMap<>(map));
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

  public static ItemStack getWorktableAsItemStack(String name, EnumTier tier) {

    EnumType type = EnumType.fromName(name);

    switch (tier) {

      case WORKTABLE:
        return new ItemStack(ModuleWorktables.Blocks.WORKTABLE, 1, type.getMeta());

      case WORKSTATION:
        return new ItemStack(ModuleWorktables.Blocks.WORKSTATION, 1, type.getMeta());

      default:
        throw new IllegalArgumentException("Unknown tier: " + tier);
    }
  }

  public static boolean containsRecipeWithTool(ItemStack itemStack) {

    for (RegistryRecipeWorktable registry : RECIPE_REGISTRY_MAP.values()) {

      if (registry.containsRecipeWithToolInAnySlot(itemStack)) {
        return true;
      }
    }

    return false;
  }

  private WorktableAPI() {
    //
  }

}
