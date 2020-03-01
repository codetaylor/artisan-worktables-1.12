package com.codetaylor.mc.artisanworktables.modules.automator.item;

import com.codetaylor.mc.artisanworktables.api.internal.reference.Tags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemUpgrade
    extends Item {

  public static final String NAME_SPEED = "upgrade_speed";

  public ItemUpgrade() {

    this.setMaxStackSize(1);
  }

  public static boolean isUpgrade(ItemStack itemStack) {

    NBTTagCompound itemTag = itemStack.getTagCompound();

    if (itemStack.isEmpty() || itemTag == null) {
      return false;
    }

    if (!itemTag.hasKey(Tags.TAG_ARTISAN_WORKTABLES)) {
      return false;
    }

    NBTTagCompound artisanTag = itemTag.getCompoundTag(Tags.TAG_ARTISAN_WORKTABLES);

    if (!artisanTag.hasKey(Tags.TAG_UPGRADE)) {
      return false;
    }

    return true;
  }
}
