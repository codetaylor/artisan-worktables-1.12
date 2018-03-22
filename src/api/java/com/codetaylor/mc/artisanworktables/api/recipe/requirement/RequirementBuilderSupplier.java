package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

public class RequirementBuilderSupplier
    extends IForgeRegistryEntry.Impl<RequirementBuilderSupplier>
    implements Supplier<IRequirementBuilder> {

  private Supplier<IRequirementBuilder> supplier;

  public RequirementBuilderSupplier(String modId, String name, Supplier<IRequirementBuilder> supplier) {

    this.supplier = supplier;
    this.setRegistryName(modId, name);
  }

  @Override
  public IRequirementBuilder get() {

    return this.supplier.get();
  }
}
