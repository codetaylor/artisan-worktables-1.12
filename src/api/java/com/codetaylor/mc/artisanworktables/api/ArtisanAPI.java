package com.codetaylor.mc.artisanworktables.api;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementBuilderSupplier;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementContextSupplier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementBuilder;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ArtisanAPI {

  public static final Supplier<String> MOD_ID = () -> "";

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

  /**
   * Throws an {@link IllegalStateException} if there is no {@link RecipeRegistry}
   * for the given table name.
   *
   * @param tableName the table name
   * @return the recipe registry for the given table name
   */
  public static RecipeRegistry getWorktableRecipeRegistry(String tableName) {

    RecipeRegistry recipeRegistry = ArtisanRegistries.RECIPE_REGISTRY.getValue(new ResourceLocation(
        MOD_ID.get(),
        tableName
    ));

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

    for (RecipeRegistry registry : ArtisanRegistries.RECIPE_REGISTRY.getValuesCollection()) {

      if (registry.containsRecipeWithToolInAnySlot(itemStack)) {
        return true;
      }
    }

    return false;
  }

  // --------------------------------------------------------------------------
  // - Match Requirement

  public static IMatchRequirementContext getRequirementContext(ResourceLocation key) {

    RequirementContextSupplier supplier = ArtisanRegistries.REQUIREMENT_CONTEXT_SUPPLIER.getValue(key);

    if (supplier == null) {
      throw new IllegalStateException("No requirement context supplier registered for: " + key);
    }

    return supplier.get();
  }

  public static <C extends IMatchRequirementContext, R extends IMatchRequirement<C>> IMatchRequirementBuilder<C, R> getRequirementBuilder(
      ResourceLocation key
  ) {

    RequirementBuilderSupplier supplier = ArtisanRegistries.REQUIREMENT_BUILDER_SUPPLIER.getValue(key);

    if (supplier == null) {
      throw new IllegalStateException("No requirement builder supplier registered for: " + key);
    }

    return supplier.get();
  }

  // --------------------------------------------------------------------------
  // - Internal

  private ArtisanAPI() {
    //
  }

}
