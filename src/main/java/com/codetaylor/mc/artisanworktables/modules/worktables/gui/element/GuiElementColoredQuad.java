package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementBase;
import net.minecraft.client.gui.Gui;

public class GuiElementColoredQuad
    extends GuiElementBase {

  private final int color;

  public GuiElementColoredQuad(
      GuiContainerBase guiBase,
      int color,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight
  ) {

    super(guiBase, elementX, elementY, elementWidth, elementHeight);
    this.color = color | (0xFF << 24);
  }

  @Override
  public void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    Gui.drawRect(
        this.elementXModifiedGet(),
        this.elementYModifiedGet(),
        this.elementXModifiedGet() + this.elementWidthModifiedGet(),
        this.elementYModifiedGet() + this.elementHeightModifiedGet(),
        this.color
    );
  }

  @Override
  public void drawForegroundLayer(int mouseX, int mouseY) {
    //
  }
}
