package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.builder;

import com.codetaylor.mc.artisanworktables.modules.worktables.api.ArtisanWorktablesAPI;
import com.codetaylor.mc.athenaeum.integration.crafttweaker.mtlib.helpers.CTLogHelper;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.artisanworktables.builder.RecipeBuilder")
public class ZenRecipeBuilderProvider {

  private static final Map<String, IZenRecipeBuilder> BUILDER_MAP = new HashMap<>();

  @ZenMethod
  public static IZenRecipeBuilder get(String table) {

    table = table.toLowerCase();

    if (!ArtisanWorktablesAPI.isWorktableNameValid(table)) {
      CTLogHelper.logErrorFromZenMethod("Unknown table type: " + table);
      CTLogHelper.logInfo("Valid table types are: " + String.join(
          ",",
          ArtisanWorktablesAPI.getWorktableNames()
      ));
      return ZenRecipeBuilderNoOp.INSTANCE;
    }

    IZenRecipeBuilder builder = BUILDER_MAP.get(table);

    if (builder == null) {
      builder = new ZenRecipeBuilder(table);
      BUILDER_MAP.put(table, builder);
    }

    return builder;
  }

}
