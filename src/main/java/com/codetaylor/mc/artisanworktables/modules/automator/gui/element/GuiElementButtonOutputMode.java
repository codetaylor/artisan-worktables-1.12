package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipExtendedProvider;
import net.minecraft.client.resources.I18n;

import java.util.List;
import java.util.function.Supplier;

public class GuiElementButtonOutputMode
    extends GuiElementPanelButtonBase
    implements IGuiElementTooltipExtendedProvider {

  private final IClickHandler clickHandler;
  private final Supplier<TileAutomator.EnumOutputMode> currentOutputMode;

  public interface IClickHandler {

    void onClick();
  }

  public GuiElementButtonOutputMode(
      IClickHandler clickHandler,
      Supplier<TileAutomator.EnumOutputMode> currentOutputMode,
      Supplier<AutomatorContainer.EnumState> currentState,
      GuiContainerBase guiBase,
      Texture[] textures,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight
  ) {

    super(
        currentState, AutomatorContainer.EnumState.Pattern,
        guiBase, textures,
        elementX, elementY,
        elementWidth, elementHeight
    );
    this.clickHandler = clickHandler;
    this.currentOutputMode = currentOutputMode;
  }

  @Override
  protected int textureIndexGet(int mouseX, int mouseY) {

    TileAutomator.EnumOutputMode outputMode = this.currentOutputMode.get();

    if (this.elementIsMouseInside(mouseX, mouseY)) {
      return outputMode.getIndex() + (this.textures.length / 2);
    }

    return outputMode.getIndex();
  }

  @Override
  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    super.elementClicked(mouseX, mouseY, mouseButton);
    this.clickHandler.onClick();
  }

  @Override
  public List<String> tooltipTextGet(List<String> tooltip) {

    TileAutomator.EnumOutputMode outputMode = this.currentOutputMode.get();
    String modeKey = "ERROR";

    switch (outputMode) {
      case Keep:
        modeKey = "item.artisanworktables.tooltip.automator.output.slot.mode.keep";
        break;
      case Manual:
        modeKey = "item.artisanworktables.tooltip.automator.output.slot.mode.manual";
        break;
      case Inventory:
        modeKey = "item.artisanworktables.tooltip.automator.output.slot.mode.inventory";
        break;
      case Export:
        modeKey = "item.artisanworktables.tooltip.automator.output.slot.mode.export";
        break;
    }

    tooltip.add(I18n.format(
        "item.artisanworktables.tooltip.automator.output.slot.mode",
        I18n.format(modeKey)
    ));

    return tooltip;
  }

  @Override
  public List<String> tooltipTextExtendedGet(List<String> tooltip) {

    TileAutomator.EnumOutputMode outputMode = this.currentOutputMode.get();
    String infoKey = "ERROR";

    switch (outputMode) {
      case Keep:
        infoKey = "item.artisanworktables.tooltip.automator.output.slot.mode.keep.info";
        break;
      case Manual:
        infoKey = "item.artisanworktables.tooltip.automator.output.slot.mode.manual.info";
        break;
      case Inventory:
        infoKey = "item.artisanworktables.tooltip.automator.output.slot.mode.inventory.info";
        break;
      case Export:
        infoKey = "item.artisanworktables.tooltip.automator.output.slot.mode.export.info";
        break;
    }

    tooltip.add(I18n.format(infoKey));

    return tooltip;
  }

}
