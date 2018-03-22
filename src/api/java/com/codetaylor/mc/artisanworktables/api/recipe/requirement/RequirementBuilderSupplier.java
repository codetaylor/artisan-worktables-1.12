package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

public class RequirementBuilderSupplier
    extends IForgeRegistryEntry.Impl<RequirementBuilderSupplier>
    implements Supplier<IMatchRequirementBuilder> {

  private Supplier<IMatchRequirementBuilder> supplier;

  public RequirementBuilderSupplier(String modId, String name, Supplier<IMatchRequirementBuilder> supplier) {

    this.supplier = supplier;
    this.setRegistryName(modId, name);
  }

  @Override
  public IMatchRequirementBuilder get() {

    return this.supplier.get();
  }
}
