package com.codetaylor.mc.artisanworktables.api.internal.config;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;

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

  boolean isTypeEnabled(EnumType type);

  boolean enableSneakClickClearPattern();

  boolean patternSlotsEnabledForTier(EnumTier tier);

}
