package com.codetaylor.mc.artisanworktables.modules.automator.event;

import com.codetaylor.mc.artisanworktables.api.internal.reference.Tags;
import com.codetaylor.mc.artisanworktables.modules.automator.item.ItemUpgrade;
import com.codetaylor.mc.athenaeum.util.TooltipHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class TooltipEventHandler {

  @SubscribeEvent
  public void on(ItemTooltipEvent event) {

    ItemStack itemStack = event.getItemStack();

    NBTTagCompound upgradeTag = ItemUpgrade.getUpgradeTag(itemStack);

    if (upgradeTag == null || upgradeTag.getSize() == 0) {
      return;
    }

    List<String> tooltip = event.getToolTip();

    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

      if (upgradeTag.hasKey(Tags.TAG_UPGRADE_SPEED)) {
        int speedModifier = (int) (upgradeTag.getFloat(Tags.TAG_UPGRADE_SPEED) * 100);
        if (speedModifier != 0) {
          tooltip.add(TextFormatting.GRAY + I18n.format(
              "tooltip.artisanworktables.automator.upgrade.speed",
              (speedModifier > 0) ? "+" + speedModifier : speedModifier
          ));
        }
      }

    } else {
      tooltip.add(TooltipHelper.tooltipHoldShiftStringGet());
    }
  }

}
