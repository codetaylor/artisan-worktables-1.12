package com.codetaylor.mc.artisanworktables.api.tool;

public class CustomToolMaterialRegistrationEntry {

  private final ICustomToolMaterial material;
  private final ICustomToolProvider provider;

  public CustomToolMaterialRegistrationEntry(ICustomToolMaterial material, ICustomToolProvider provider) {

    this.material = material;
    this.provider = provider;
  }

  public ICustomToolMaterial getMaterial() {

    return this.material;
  }

  public ICustomToolProvider getProvider() {

    return this.provider;
  }
}
