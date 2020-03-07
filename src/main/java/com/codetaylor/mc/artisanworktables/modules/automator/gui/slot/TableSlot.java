package com.codetaylor.mc.artisanworktables.modules.automator.gui.slot;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Supplier;

public class TableSlot
    extends PanelSlot {

  public TableSlot(
      Supplier<AutomatorContainer.EnumState> currentState,
      AutomatorContainer.EnumState state,
      IItemHandler itemHandler,
      int index,
      int xPosition,
      int yPosition
  ) {

    super(currentState, state, itemHandler, index, xPosition, yPosition);
  }
}
