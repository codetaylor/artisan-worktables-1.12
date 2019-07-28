package com.codetaylor.mc.artisanworktables.api.internal.config;

import net.minecraft.enchantment.Enchantment;

import java.util.List;

public class ModuleToolsConfigNoOp
    implements IModuleToolsConfig {

  public static final IModuleToolsConfig INSTANCE = new ModuleToolsConfigNoOp();

  @Override
  public boolean enableDurabilityTooltip() {

    throw new UnsupportedOperationException();
  }

  @Override
  public String getToolByTypeOreDictPrefix() {

    throw new UnsupportedOperationException();
  }

  @Override
  public String getToolByMaterialOreDictPrefix() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enableModule() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enableToolRecipes() {

    throw new UnsupportedOperationException();
  }

  @Override
  public List<String> getEnabledToolTypes(List<String> result) {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enableToolMaterialOreDictGroups() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enableToolTypeOreDictGroups() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enableToolRepair() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enableToolEnchanting() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean allowToolEnchantment(Enchantment enchantment) {

    throw new UnsupportedOperationException();
  }
}
