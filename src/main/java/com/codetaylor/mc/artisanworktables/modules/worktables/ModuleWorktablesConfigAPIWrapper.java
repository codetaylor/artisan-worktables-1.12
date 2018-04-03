package com.codetaylor.mc.artisanworktables.modules.worktables;

import com.codetaylor.mc.artisanworktables.api.internal.config.IModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;

public class ModuleWorktablesConfigAPIWrapper
    implements IModuleWorktablesConfig {

  @Override
  public boolean enableWorktables() {

    return ModuleWorktablesConfig.ENABLE_WORKTABLES;
  }

  @Override
  public boolean enableWorkstations() {

    return ModuleWorktablesConfig.ENABLE_WORKSTATIONS;
  }

  @Override
  public boolean enableWorkshops() {

    return ModuleWorktablesConfig.ENABLE_WORKSHOPS;
  }

  @Override
  public boolean restrictCraftMinimumDurability() {

    return ModuleWorktablesConfig.RESTRICT_CRAFT_MINIMUM_DURABILITY;
  }

  @Override
  public int getFluidCapacityWorktable(String tableName) {

    return ModuleWorktablesConfig.FLUID_CAPACITY_WORKTABLE.get(tableName);
  }

  @Override
  public int getFluidCapacityWorkstation(String tableName) {

    return ModuleWorktablesConfig.FLUID_CAPACITY_WORKSTATION.get(tableName);
  }

  @Override
  public int getFluidCapacityWorkshop(String tableName) {

    return ModuleWorktablesConfig.FLUID_CAPACITY_WORKSHOP.get(tableName);
  }

  @Override
  public int getTextHighlightColor(String tableName) {

    return ModuleWorktablesConfig.CLIENT.getTextHighlightColor(tableName);
  }

  @Override
  public boolean isTierEnabled(EnumTier tier) {

    return ModuleWorktablesConfig.isTierEnabled(tier);
  }

  @Override
  public boolean enableSneakClickClearPattern() {

    return ModuleWorktablesConfig.PATTERN.ENABLE_SNEAK_CLICK_TO_CLEAR;
  }

  @Override
  public boolean patternSlotsEnabledForTier(EnumTier tier) {

    return ModuleWorktablesConfig.patternSlotsEnabledForTier(tier);
  }
}
