package com.codetaylor.mc.artisanworktables.modules.automator.gui.element;

import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.AutomatorGuiContainer;
import com.codetaylor.mc.artisanworktables.modules.automator.network.CSPacketAutomatorTabStateChange;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureButtonBase;

public class GuiElementButtonAutomatorTab
    extends GuiElementTextureButtonBase {

  private final AutomatorContainer.EnumState state;
  private final AutomatorGuiContainer guiContainer;
  private int mouseX;
  private int mouseY;

  public GuiElementButtonAutomatorTab(
      AutomatorContainer.EnumState state,
      AutomatorGuiContainer guiBase,
      Texture texture,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight
  ) {

    super(guiBase, new Texture[]{texture}, elementX, elementY, elementWidth, elementHeight);
    this.state = state;
    this.guiContainer = guiBase;
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    return (this.guiContainer.getContainerState() != this.state);
  }

  @Override
  protected int textureIndexGet(int mouseX, int mouseY) {

    return 0;
  }

  @Override
  public void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    super.drawBackgroundLayer(partialTicks, mouseX, mouseY);

    this.mouseX = mouseX;
    this.mouseY = mouseY;
  }

  @Override
  protected int elementYModifiedGet() {

    if (this.elementIsMouseInside(this.mouseX, this.mouseY)) {
      return super.elementYModifiedGet() - 3;
    }

    return super.elementYModifiedGet();
  }

  @Override
  public boolean elementIsMouseInside(int mouseX, int mouseY) {

    return this.guiBase.isPointInRegion(
        this.elementX + 1,
        this.elementY,
        this.elementWidth - 2,
        this.elementHeight - 4,
        mouseX,
        mouseY
    );
  }

  @Override
  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    super.elementClicked(mouseX, mouseY, mouseButton);
    ModuleAutomator.PACKET_SERVICE.sendToServer(new CSPacketAutomatorTabStateChange(this.state));
  }
}
