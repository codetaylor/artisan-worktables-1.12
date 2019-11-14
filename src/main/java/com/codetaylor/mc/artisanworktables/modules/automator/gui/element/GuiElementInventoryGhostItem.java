package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementItemStack;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class GuiElementInventoryGhostItem
    extends GuiElementItemStack {

  private final Supplier<AutomatorContainer.EnumState> currentState;

  public GuiElementInventoryGhostItem(
      Supplier<AutomatorContainer.EnumState> currentState,
      Supplier<ItemStack> itemStackSupplier,
      GuiContainerBase guiBase,
      int elementX, int elementY
  ) {

    super(itemStackSupplier, 0.5f, guiBase, elementX, elementY);
    this.currentState = currentState;
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    return (this.currentState.get() == AutomatorContainer.EnumState.Inventory);
  }
}
