package com.codetaylor.mc.artisanworktables.api.internal.config;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;

public interface IModuleWorktablesConfig {

  boolean enableWorktables();

  boolean enableWorkstations();

  boolean enableWorkshops();

  boolean restrictCraftMinimumDurability();

  int getFluidCapacityWorktable(String tableName);

  int getFluidCapacityWorkstation(String tableName);

  int getFluidCapacityWorkshop(String tableName);

  int getTextHighlightColor(String tableName);

  boolean isTierEnabled(EnumTier tier);

  boolean enableSneakClickClearPattern();

}
