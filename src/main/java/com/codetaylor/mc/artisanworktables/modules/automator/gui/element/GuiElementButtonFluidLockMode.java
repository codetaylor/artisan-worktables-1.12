package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.lib.IBooleanSupplier;
import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.network.CSPacketAutomatorFluidLockModeChange;
import com.codetaylor.mc.artisanworktables.modules.automator.network.CSPacketAutomatorInventoryLockModeChange;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipExtendedProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.function.Supplier;

public class GuiElementButtonFluidLockMode
    extends GuiElementPanelButtonBase
    implements IGuiElementTooltipExtendedProvider {

  private final int slotIndex;
  private final IBooleanSupplier currentLockedMode;
  private final BlockPos tilePos;

  public GuiElementButtonFluidLockMode(
      BlockPos tilePos,
      int slotIndex,
      IBooleanSupplier currentLockedMode,
      Supplier<AutomatorContainer.EnumState> currentState,
      GuiContainerBase guiBase,
      Texture[] textures,
      int elementX, int elementY
  ) {

    super(
        currentState, AutomatorContainer.EnumState.Fluid,
        guiBase, textures,
        elementX, elementY,
        16, 16
    );
    this.tilePos = tilePos;
    this.slotIndex = slotIndex;
    this.currentLockedMode = currentLockedMode;
  }

  @Override
  protected int textureIndexGet(int mouseX, int mouseY) {

    boolean isLocked = this.currentLockedMode.get();

    if (this.elementIsMouseInside(mouseX, mouseY)) {
      return (isLocked ? 1 : 0) + (this.textures.length / 2);
    }

    return (isLocked ? 1 : 0);
  }

  @Override
  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    super.elementClicked(mouseX, mouseY, mouseButton);
    ModuleAutomator.PACKET_SERVICE.sendToServer(
        new CSPacketAutomatorFluidLockModeChange(this.tilePos, this.slotIndex));
  }

  @Override
  public List<String> tooltipTextGet(List<String> tooltip) {

    boolean isLocked = this.currentLockedMode.get();
    String langKey;

    if (isLocked) {
      langKey = "tooltip.artisanworktables.automator.fluid.locked";

    } else {
      langKey = "tooltip.artisanworktables.automator.fluid.unlocked";
    }

    tooltip.add(I18n.format(langKey));

    return tooltip;
  }

  @Override
  public List<String> tooltipTextExtendedGet(List<String> tooltip) {

    boolean isLocked = this.currentLockedMode.get();
    String langKey;

    if (isLocked) {
      langKey = "tooltip.artisanworktables.automator.fluid.locked.info";

    } else {
      langKey = "tooltip.artisanworktables.automator.fluid.unlocked.info";
    }

    tooltip.add(TextFormatting.GRAY + I18n.format(langKey));

    return tooltip;
  }

}
