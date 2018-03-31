package com.codetaylor.mc.artisanworktables.api.internal.config;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;

public class ModuleWorktablesConfigNoOp
    implements IModuleWorktablesConfig {

  public static final IModuleWorktablesConfig INSTANCE = new ModuleWorktablesConfigNoOp();

  @Override
  public boolean enableWorktables() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enableWorkstations() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enableWorkshops() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean restrictCraftMinimumDurability() {

    throw new UnsupportedOperationException();
  }

  @Override
  public int getFluidCapacityWorktable(String tableName) {

    throw new UnsupportedOperationException();
  }

  @Override
  public int getFluidCapacityWorkstation(String tableName) {

    throw new UnsupportedOperationException();
  }

  @Override
  public int getFluidCapacityWorkshop(String tableName) {

    throw new UnsupportedOperationException();
  }

  @Override
  public int getTextHighlightColor(String tableName) {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isTierEnabled(EnumTier tier) {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enableSneakClickClearPattern() {

    throw new UnsupportedOperationException();
  }
}
