package com.codetaylor.mc.artisanworktables.api;

import com.codetaylor.mc.artisanworktables.api.internal.config.*;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import net.minecraft.item.ItemStack;

import java.util.*;

public class ArtisanAPI {

  // --------------------------------------------------------------------------
  // - Integration

  public static final boolean IS_MOD_LOADED_GAMESTAGES = false;

  // --------------------------------------------------------------------------
  // - Module Configs

  public static final IModuleWorktablesConfig MODULE_WORKTABLES_CONFIG = ModuleWorktablesConfigNoOp.INSTANCE;

  public static final IModuleToolsConfig MODULE_TOOLS_CONFIG = ModuleToolsConfigNoOp.INSTANCE;

  public static final IModuleToolboxConfig MODULE_TOOLBOX_CONFIG = ModuleToolboxConfigNoOp.INSTANCE;

  // --------------------------------------------------------------------------
  // - Worktable Names

  private static final List<String> WORKTABLE_NAME_LIST;

  static {
    List<String> list = new ArrayList<>();
    list.addAll(Arrays.asList(EnumType.NAMES));
    WORKTABLE_NAME_LIST = Collections.unmodifiableList(new ArrayList<>(list));
  }

  /**
   * @return unmodifiable list of worktable names
   */
  public static List<String> getWorktableNames() {

    return WORKTABLE_NAME_LIST;
  }

  /**
   * @param name the worktable name
   * @return true if the worktable name is valid
   */
  public static boolean isWorktableNameValid(String name) {

    return WORKTABLE_NAME_LIST.contains(name.toLowerCase());
  }

  // --------------------------------------------------------------------------
  // - Worktable Recipe Registries

  private static final Map<String, RecipeRegistry> RECIPE_REGISTRY_MAP;

  static {
    Map<String, RecipeRegistry> map = new HashMap<>();

    for (String name : WORKTABLE_NAME_LIST) {
      map.put(name, new RecipeRegistry());
    }
    RECIPE_REGISTRY_MAP = Collections.unmodifiableMap(new HashMap<>(map));
  }

  /**
   * Throws an {@link IllegalStateException} if there is no {@link RecipeRegistry}
   * for the given table name.
   *
   * @param tableName the table name
   * @return the recipe registry for the given table name
   */
  public static RecipeRegistry getWorktableRecipeRegistry(String tableName) {

    RecipeRegistry recipeRegistry = RECIPE_REGISTRY_MAP.get(tableName);

    if (recipeRegistry == null) {
      throw new IllegalStateException("Can't find recipe registry for table: " + tableName);
    }

    return recipeRegistry;
  }

  /**
   * @param itemStack the tool item stack
   * @return true if any recipe registry contains a recipe that uses the given tool
   */
  public static boolean containsRecipeWithTool(ItemStack itemStack) {

    for (RecipeRegistry registry : RECIPE_REGISTRY_MAP.values()) {

      if (registry.containsRecipeWithToolInAnySlot(itemStack)) {
        return true;
      }
    }

    return false;
  }

}
