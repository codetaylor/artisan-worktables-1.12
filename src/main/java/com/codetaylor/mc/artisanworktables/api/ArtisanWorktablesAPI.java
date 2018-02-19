package com.codetaylor.mc.artisanworktables.api;

import com.codetaylor.mc.artisanworktables.api.recipe.AWRecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import net.minecraft.item.ItemStack;

import java.util.*;

public class ArtisanWorktablesAPI {

  private static final Map<String, AWRecipeRegistry> RECIPE_REGISTRY_MAP;

  private static final List<String> WORKTABLE_NAME_LIST;

  static {

    {
      List<String> list = new ArrayList<>();
      list.addAll(Arrays.asList(EnumType.NAMES));
      WORKTABLE_NAME_LIST = Collections.unmodifiableList(new ArrayList<>(list));
    }

    {
      Map<String, AWRecipeRegistry> map = new HashMap<>();

      for (String name : ArtisanWorktablesAPI.getWorktableNames()) {
        map.put(name, new AWRecipeRegistry());
      }
      RECIPE_REGISTRY_MAP = Collections.unmodifiableMap(new HashMap<>(map));
    }
  }

  public static AWRecipeRegistry getWorktableRecipeRegistry(String name) {

    return RECIPE_REGISTRY_MAP.get(name);
  }

  public static boolean isWorktableNameValid(String name) {

    return WORKTABLE_NAME_LIST.contains(name.toLowerCase());
  }

  public static List<String> getWorktableNames() {

    return Collections.unmodifiableList(WORKTABLE_NAME_LIST);
  }

  public static int getWorktableTextHighlightColor(String name) {

    EnumType type = EnumType.fromName(name);
    return type.getTextOutlineColor();
  }

  public static boolean containsRecipeWithTool(ItemStack itemStack) {

    for (AWRecipeRegistry registry : RECIPE_REGISTRY_MAP.values()) {

      if (registry.containsRecipeWithToolInAnySlot(itemStack)) {
        return true;
      }
    }

    return false;
  }

  private ArtisanWorktablesAPI() {
    //
  }

}
