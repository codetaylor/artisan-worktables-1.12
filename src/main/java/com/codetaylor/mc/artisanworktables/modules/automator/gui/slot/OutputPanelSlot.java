package com.codetaylor.mc.artisanworktables.modules.automator.gui.slot;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class OutputPanelSlot
    extends PanelSlot {

  public OutputPanelSlot(
      Supplier<AutomatorContainer.EnumState> currentState,
      AutomatorContainer.EnumState state,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(currentState, state, itemHandler, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {

    return false;
  }
}
