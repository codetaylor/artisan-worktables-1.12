package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.network.CSPacketAutomatorFluidModeChange;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipExtendedProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.function.Supplier;

public class GuiElementButtonFluidMode
    extends GuiElementPanelButtonBase
    implements IGuiElementTooltipExtendedProvider {

  private final BlockPos tilePos;
  private final int slotIndex;
  private final Supplier<TileAutomator.EnumFluidMode> currentFluidMode;

  public GuiElementButtonFluidMode(
      BlockPos tilePos,
      int slotIndex,
      Supplier<TileAutomator.EnumFluidMode> currentFluidMode,
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
    this.currentFluidMode = currentFluidMode;
  }

  @Override
  protected int textureIndexGet(int mouseX, int mouseY) {

    TileAutomator.EnumFluidMode mode = this.currentFluidMode.get();

    if (this.elementIsMouseInside(mouseX, mouseY)) {
      return mode.getIndex() + (this.textures.length / 2);
    }

    return mode.getIndex();
  }

  @Override
  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    super.elementClicked(mouseX, mouseY, mouseButton);
    ModuleAutomator.PACKET_SERVICE.sendToServer(
        new CSPacketAutomatorFluidModeChange(this.tilePos, this.slotIndex));
  }

  @Override
  public List<String> tooltipTextGet(List<String> tooltip) {

    TileAutomator.EnumFluidMode mode = this.currentFluidMode.get();
    String modeKey = "ERROR";

    switch (mode) {
      case Fill:
        modeKey = "tooltip.artisanworktables.automator.fluid.mode.fill";
        break;
      case Drain:
        modeKey = "tooltip.artisanworktables.automator.fluid.mode.drain";
        break;
    }

    tooltip.add(I18n.format(
        "tooltip.artisanworktables.automator.fluid.mode",
        I18n.format(modeKey)
    ));

    return tooltip;
  }

  @Override
  public List<String> tooltipTextExtendedGet(List<String> tooltip) {

    TileAutomator.EnumFluidMode mode = this.currentFluidMode.get();
    String infoKey = "ERROR";

    switch (mode) {
      case Fill:
        infoKey = "tooltip.artisanworktables.automator.fluid.mode.fill.info";
        break;
      case Drain:
        infoKey = "tooltip.artisanworktables.automator.fluid.mode.drain.info";
        break;
    }

    tooltip.add(TextFormatting.GRAY + I18n.format(infoKey));

    return tooltip;
  }

}
