package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.network.CSPacketAutomatorClearInventoryGhostSlot;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementItemStack;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementClickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.function.Supplier;

public class GuiElementInventoryGhostItem
    extends GuiElementItemStack
    implements IGuiElementClickable {

  private final int slotIndex;
  private final BlockPos tilePos;
  private final Supplier<AutomatorContainer.EnumState> currentState;

  public GuiElementInventoryGhostItem(
      int slotIndex,
      BlockPos tilePos,
      Supplier<AutomatorContainer.EnumState> currentState,
      Supplier<ItemStack> ghostItemSupplier,
      GuiContainerBase guiBase,
      int elementX, int elementY
  ) {

    super(ghostItemSupplier, 0.5f, guiBase, elementX, elementY);
    this.slotIndex = slotIndex;
    this.tilePos = tilePos;
    this.currentState = currentState;
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    return (this.currentState.get() == AutomatorContainer.EnumState.Inventory);
  }

  @Override
  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    boolean ghostItemExists = !this.itemStackSupplier.get().isEmpty();
    boolean isLeftMouseButton = (mouseButton == 0);
    boolean isControlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
        || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);

    if (isLeftMouseButton
        && ghostItemExists
        && isControlKeyDown) {
      ModuleAutomator.PACKET_SERVICE.sendToServer(new CSPacketAutomatorClearInventoryGhostSlot(
          this.tilePos,
          this.slotIndex
      ));
      Minecraft.getMinecraft()
          .getSoundHandler()
          .playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
    }
  }
}
