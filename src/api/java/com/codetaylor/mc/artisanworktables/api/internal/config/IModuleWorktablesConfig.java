package com.codetaylor.mc.artisanworktables.api.internal.config;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;

import javax.annotation.Nullable;

public interface IModuleWorktablesConfig {

  boolean enableWorktables();

  boolean enableWorkstations();

  boolean enableWorkshops();

  boolean restrictCraftMinimumDurability();

  int getFluidCapacityWorktable(String tableName);

  int getFluidCapacityWorkstation(String tableName);

  int getFluidCapacityWorkshop(String tableName);

  int getTextHighlightColor(String tableName);

  int getFluidTankOverlayColor(String tableName);

  @Nullable
  Integer getCraftingGridSlotBackgroundColor(String tableName);

  @Nullable
  Integer getFluidTankBackgroundColor(String tableName);

  @Nullable
  Integer getMainOutputSlotBackgroundColor(String tableName);

  @Nullable
  Integer getPlayerInventorySlotBackgroundColor(String tableName);

  boolean isTierEnabled(EnumTier tier);

  boolean isTypeEnabled(EnumType type);

  boolean enableSneakClickClearPattern();

  boolean patternSlotsEnabledForTier(EnumTier tier);

  boolean enablePatternCreationForRecipesWithRequirements();
}
