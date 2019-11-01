package com.codetaylor.mc.artisanworktables.modules.automator.gui.slot;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.function.Supplier;

public class PanelSlot
    extends SlotItemHandler {

  private final Supplier<AutomatorContainer.EnumState> currentState;
  private final AutomatorContainer.EnumState state;

  public PanelSlot(
      Supplier<AutomatorContainer.EnumState> currentState,
      AutomatorContainer.EnumState state,
      IItemHandler itemHandler,
      int index,
      int xPosition, int yPosition
  ) {

    super(itemHandler, index, xPosition, yPosition);
    this.currentState = currentState;
    this.state = state;
  }

  @Override
  public boolean isEnabled() {

    return (this.currentState.get() == this.state);
  }
}
