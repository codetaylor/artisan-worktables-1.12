package com.codetaylor.mc.artisanworktables.api;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeRegistry;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementBuilderSupplier;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.RequirementContextSupplier;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ArtisanRegistries {

  public static final IForgeRegistry<RequirementContextSupplier> REQUIREMENT_CONTEXT_SUPPLIER;
  public static final IForgeRegistry<RequirementBuilderSupplier> REQUIREMENT_BUILDER_SUPPLIER;
  public static final IForgeRegistry<RecipeRegistry> RECIPE_REGISTRY;

  static {
    REQUIREMENT_CONTEXT_SUPPLIER = GameRegistry.findRegistry(RequirementContextSupplier.class);
    REQUIREMENT_BUILDER_SUPPLIER = GameRegistry.findRegistry(RequirementBuilderSupplier.class);
    RECIPE_REGISTRY = GameRegistry.findRegistry(RecipeRegistry.class);
  }

  private ArtisanRegistries() {
    //
  }
}
