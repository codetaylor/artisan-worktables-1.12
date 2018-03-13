package com.codetaylor.mc.artisanworktables.modules.worktables.api;

import com.codetaylor.mc.artisanworktables.api.IModuleWorktablesAPI;
import com.codetaylor.mc.artisanworktables.api.config.IModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import net.minecraft.item.ItemStack;

import java.util.*;

public class ModuleWorktablesAPI_Impl
    implements IModuleWorktablesAPI {

  private final boolean modLoadedGameStages;
  private final IModuleWorktablesConfig moduleWorktablesConfig;

  private static final Map<String, RecipeRegistry> RECIPE_REGISTRY_MAP;
  private static final List<String> WORKTABLE_NAME_LIST;

  static {

    {
      List<String> list = new ArrayList<>();
      list.addAll(Arrays.asList(EnumType.NAMES));
      WORKTABLE_NAME_LIST = Collections.unmodifiableList(new ArrayList<>(list));
    }

    {
      Map<String, RecipeRegistry> map = new HashMap<>();

      for (String name : ModuleWorktablesAPI_Impl.getWorktableNamesStatic()) {
        map.put(name, new RecipeRegistry());
      }
      RECIPE_REGISTRY_MAP = Collections.unmodifiableMap(new HashMap<>(map));
    }
  }

  public ModuleWorktablesAPI_Impl(
      boolean modLoadedGameStages,
      IModuleWorktablesConfig moduleWorktablesConfig
  ) {

    this.modLoadedGameStages = modLoadedGameStages;
    this.moduleWorktablesConfig = moduleWorktablesConfig;
  }

  @Override
  public boolean isModLoadedGameStages() {

    return this.modLoadedGameStages;
  }

  @Override
  public IModuleWorktablesConfig getModuleWorktablesConfig() {

    return this.moduleWorktablesConfig;
  }

  @Override
  public List<String> getWorktableNames() {

    return ModuleWorktablesAPI_Impl.getWorktableNamesStatic();
  }

  public static List<String> getWorktableNamesStatic() {

    return WORKTABLE_NAME_LIST;
  }

  @Override
  public RecipeRegistry getWorktableRecipeRegistry(String name) {

    return RECIPE_REGISTRY_MAP.get(name);
  }

  @Override
  public boolean containsRecipeWithTool(ItemStack itemStack) {

    for (RecipeRegistry registry : RECIPE_REGISTRY_MAP.values()) {

      if (registry.containsRecipeWithToolInAnySlot(itemStack)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean isWorktableNameValid(String name) {

    return WORKTABLE_NAME_LIST.contains(name.toLowerCase());
  }

  public static int getWorktableTextHighlightColor(String name) {

    EnumType type = EnumType.fromName(name);
    return type.getTextOutlineColor();
  }

}
