package com.codetaylor.mc.artisanworktables.modules.automator;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public final class TooltipUtil {

  public static String getAutoExportItemsString(boolean autoExportItems) {

    String prefix = (autoExportItems) ? TextFormatting.DARK_GREEN.toString() : TextFormatting.DARK_RED.toString();

    return I18n.format(
        "tooltip.artisanworktables.automator.upgrade.auto.export.items",
        prefix + autoExportItems
    );
  }

  public static String getAutoImportItemsString(boolean autoExportItems) {

    String prefix = (autoExportItems) ? TextFormatting.DARK_GREEN.toString() : TextFormatting.DARK_RED.toString();

    return I18n.format(
        "tooltip.artisanworktables.automator.upgrade.auto.import.items",
        prefix + autoExportItems
    );
  }

  public static String getSpeedString(int speedModifier, boolean isUpgrade) {

    String prefix = TooltipUtil.getPrefix(speedModifier, isUpgrade);

    return I18n.format(
        "tooltip.artisanworktables.automator.upgrade.speed",
        prefix + speedModifier
    );
  }

  public static String getEnergyUsageString(int energyUsageModifier, boolean isUpgrade) {

    String prefix = TooltipUtil.getPrefixInverse(energyUsageModifier, isUpgrade);

    return I18n.format(
        "tooltip.artisanworktables.automator.upgrade.energy.usage",
        prefix + energyUsageModifier
    );
  }

  public static String getFluidCapacityString(int fluidCapacityModifier, boolean isUpgrade) {

    String prefix = TooltipUtil.getPrefix(fluidCapacityModifier, isUpgrade);

    return I18n.format(
        "tooltip.artisanworktables.automator.upgrade.fluid.capacity",
        prefix + fluidCapacityModifier
    );
  }

  public static String getEnergyCapacityString(int energyCapacityModifier, boolean isUpgrade) {

    String prefix = TooltipUtil.getPrefix(energyCapacityModifier, isUpgrade);

    return I18n.format(
        "tooltip.artisanworktables.automator.upgrade.energy.capacity",
        prefix + energyCapacityModifier
    );
  }

  private static String getPrefix(int modifier, boolean isUpgrade) {

    String prefix = "";

    int zero = (isUpgrade) ? 0 : 100;

    if (modifier == zero) {

      if (isUpgrade) {
        prefix = "+";
      }

    } else if (modifier > zero) {
      prefix = TextFormatting.DARK_GREEN.toString() + ((isUpgrade) ? "+" : "");

    } else {
      prefix = TextFormatting.DARK_RED.toString();
    }
    return prefix;
  }

  private static String getPrefixInverse(int modifier, boolean isUpgrade) {

    String prefix = "";

    int zero = (isUpgrade) ? 0 : 100;

    if (modifier == zero) {

      if (isUpgrade) {
        prefix = "+";
      }

    } else if (modifier > zero) {
      prefix = TextFormatting.DARK_RED.toString() + ((isUpgrade) ? "+" : "");

    } else {
      prefix = TextFormatting.DARK_GREEN.toString();
    }
    return prefix;
  }

  private TooltipUtil() {
    //
  }
}
