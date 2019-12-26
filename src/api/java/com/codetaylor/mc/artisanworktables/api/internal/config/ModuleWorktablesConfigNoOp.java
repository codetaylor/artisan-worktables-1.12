package com.codetaylor.mc.artisanworktables.api.internal.config;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;

import javax.annotation.Nullable;

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
  public int getFluidTankOverlayColor(String tableName) {

    throw new UnsupportedOperationException();
  }

  @Nullable
  @Override
  public Integer getCraftingGridSlotBackgroundColor(String tableName) {

    throw new UnsupportedOperationException();
  }

  @Nullable
  @Override
  public Integer getFluidTankBackgroundColor(String tableName) {

    throw new UnsupportedOperationException();
  }

  @Nullable
  @Override
  public Integer getMainOutputSlotBackgroundColor(String tableName) {

    throw new UnsupportedOperationException();
  }

  @Nullable
  @Override
  public Integer getPlayerInventorySlotBackgroundColor(String tableName) {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isTierEnabled(EnumTier tier) {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isTypeEnabled(EnumType type) {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enableSneakClickClearPattern() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean patternSlotsEnabledForTier(EnumTier tier) {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean enablePatternCreationForRecipesWithRequirements() {

    throw new UnsupportedOperationException();
  }
}
