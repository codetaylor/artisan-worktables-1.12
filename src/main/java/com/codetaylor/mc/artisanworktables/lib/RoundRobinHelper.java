package com.codetaylor.mc.artisanworktables.lib;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class RoundRobinHelper {

  public static IItemHandler copyItemHandler(IItemHandler toCopy) {

    IItemHandler result = new ItemStackHandler(toCopy.getSlots());

    for (int i = 0; i < toCopy.getSlots(); i++) {

      result.insertItem(i, toCopy.getStackInSlot(i), false);
    }

    return result;
  }

  /**
   * Returns a list of index, count tuples sorted by count.
   *
   * @param itemStack        item stack being inserted
   * @param itemHandler      real item handler
   * @param ghostItemHandler ghost item handler
   * @return list of tuples: index, count
   */
  public static List<Tuple> getSortedIndices(ItemStack itemStack, IItemHandler itemHandler, IItemHandler ghostItemHandler) {

    // Collect all the slot indices we're interested in
    IntList ghostIndices = new IntArrayList();

    for (int i = 0; i < ghostItemHandler.getSlots(); i++) {
      ItemStack ghostStack = ghostItemHandler.getStackInSlot(i);

      if (!ghostStack.isEmpty()
          && ItemStack.areItemsEqual(itemStack, ghostStack)
          && ItemStack.areItemStackTagsEqual(itemStack, ghostStack)) {
        ghostIndices.add(i);
      }
    }

    // Get slot indices and count, sort by count
    List<Tuple> list = new ArrayList<>();

    for (int i = 0; i < ghostIndices.size(); i++) {
      int ghostIndex = ghostIndices.getInt(i);
      ItemStack stackInSlot = itemHandler.getStackInSlot(ghostIndex);
      ItemStack ghostStackInSlot = ghostItemHandler.getStackInSlot(ghostIndex);

      // if the slot is empty or the slot's item matches the ghost item
      if (stackInSlot.isEmpty()
          || (ItemStack.areItemsEqual(stackInSlot, ghostStackInSlot) && ItemStack.areItemStackTagsEqual(stackInSlot, ghostStackInSlot))) {

        // index, count
        Tuple<Integer, Integer> tuple = new Tuple<>(ghostIndex, stackInSlot.getCount());
        list.add(tuple);
      }
    }

    list.sort(Comparator.comparingInt(o -> (int) o.getSecond()));
    return list;
  }

  private RoundRobinHelper() {
    //
  }
}
