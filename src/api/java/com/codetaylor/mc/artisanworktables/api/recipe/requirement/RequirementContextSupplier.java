package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

public class RequirementContextSupplier
    extends IForgeRegistryEntry.Impl<RequirementContextSupplier>
    implements Supplier<IRequirementContext> {

  private Supplier<IRequirementContext> supplier;

  public RequirementContextSupplier(String modId, String name, Supplier<IRequirementContext> supplier) {

    this.supplier = supplier;
    this.setRegistryName(modId, name);
  }

  @Override
  public IRequirementContext get() {

    return this.supplier.get();
  }
}
