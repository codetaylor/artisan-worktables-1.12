package com.codetaylor.mc.artisanworktables.api.recipe.requirement;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

public class RequirementContextSupplier
    extends IForgeRegistryEntry.Impl<RequirementContextSupplier>
    implements Supplier<IMatchRequirementContext> {

  private Supplier<IMatchRequirementContext> supplier;

  public RequirementContextSupplier(String modId, String name, Supplier<IMatchRequirementContext> supplier) {

    this.supplier = supplier;
    this.setRegistryName(modId, name);
  }

  @Override
  public IMatchRequirementContext get() {

    return this.supplier.get();
  }
}
