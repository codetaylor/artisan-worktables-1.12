package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import com.codetaylor.mc.athenaeum.util.OreDictHelper;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntHashMap;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;

public final class OreDictSlotClickDelegate {

  public static boolean slotClick(List<Slot> inventorySlots, int slotId, ItemStack stack, IntHashMap<String> oreDictMap, ClickType clickType, boolean isRemote, boolean oreDictLinked) {

    if (stack.isEmpty()) {
      return false;
    }

    if (clickType != ClickType.QUICK_MOVE) {
      return false;
    }

    if (!isRemote) {
      return true;
    }

    // System.out.println("Slot: " + slotId);

    int[] oreIDs = OreDictionary.getOreIDs(stack);

    if (oreIDs.length > 0) {
      String lookup = oreDictMap.lookup(slotId);

      if (lookup == null) {
        String oreName = OreDictionary.getOreName(oreIDs[0]);
        oreDictMap.addKey(slotId, oreName);

        if (oreDictLinked) {
          OreDictSlotClickDelegate.applyToAll(inventorySlots, stack, oreName, oreDictMap);
        }

      } else {

        for (int i = 0; i < oreIDs.length; i++) {
          String oreName = OreDictionary.getOreName(oreIDs[i]);

          if (lookup.equals(oreName)) {

            if (i == oreIDs.length - 1) {
              oreDictMap.removeObject(slotId);
              if (oreDictLinked) {
                OreDictSlotClickDelegate.applyToAll(inventorySlots, stack, null, oreDictMap);
              }

            } else {
              String nextName = OreDictionary.getOreName(oreIDs[i + 1]);
              oreDictMap.addKey(slotId, nextName);
              if (oreDictLinked) {
                OreDictSlotClickDelegate.applyToAll(inventorySlots, stack, nextName, oreDictMap);
              }
            }
            break;
          }
        }
      }
    }

    return true;
  }

  private static void applyToAll(List<Slot> inventorySlots, ItemStack stack, @Nullable String oreName, IntHashMap<String> oreDictMap) {

    for (Slot inventorySlot : inventorySlots) {
      int slotNumber = inventorySlot.slotNumber;

      if (inventorySlot instanceof ICreativeSlotClick
          && ((ICreativeSlotClick) inventorySlot).allowOredict()) {

        ItemStack otherStack = inventorySlot.getStack();

        if (otherStack.isEmpty()) {
          // System.out.println("Skipping empty stack");
          continue;
        }

        if (stack.getItem() != otherStack.getItem()) {
          // System.out.println("Item mismatch: " + stack + " != " + otherStack);
          continue;
        }

        if (stack.getMetadata() != otherStack.getMetadata()) {
          // System.out.println("Meta mismatch");
          continue;
        }

        if (oreName != null
            && (!OreDictHelper.contains(oreName, stack)
            || !OreDictHelper.contains(oreName, otherStack))) {
          // System.out.println("Oredict mismatch");
          continue;
        }

        if (oreName == null) {
          oreDictMap.removeObject(slotNumber);
          // System.out.println("Removed slot: " + slotNumber);

        } else {
          oreDictMap.addKey(slotNumber, oreName);
          // System.out.println("Added: " + slotNumber + "=" + oreName);
        }
      }
    }
  }

  private OreDictSlotClickDelegate() {
    //
  }
}
