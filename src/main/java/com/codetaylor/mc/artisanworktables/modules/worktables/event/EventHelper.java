package com.codetaylor.mc.artisanworktables.modules.worktables.event;

import com.codetaylor.mc.artisanworktables.api.event.AWItemCraftEvent;
import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class EventHelper {

  private EventHelper() {
    //
  }

  public static void fireItemCraftEventPost(
      EntityPlayer player,
      EnumType type,
      EnumTier tier,
      ItemStack craftedItem,
      List<ItemStack> extraOutputList
  ) {

    MinecraftForge.EVENT_BUS.post(new AWItemCraftEvent.Post(
        player,
        type,
        tier,
        craftedItem.copy(),
        StackHelper.copyInto(extraOutputList, new ArrayList<>(3))
    ));
  }
}
