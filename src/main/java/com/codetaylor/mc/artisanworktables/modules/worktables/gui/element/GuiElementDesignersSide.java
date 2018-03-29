package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.ITileEntityDesigner;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;

public class GuiElementDesignersSide
    extends GuiElementTextureRectangle {

  private final ITileEntityDesigner tile;

  public GuiElementDesignersSide(
      GuiContainerBase guiBase,
      ITileEntityDesigner tile,
      Texture texture,
      int elementX,
      int elementY
  ) {

    super(guiBase, texture, elementX, elementY, 68, 176);
    this.tile = tile;
  }

  @Override
  public void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    if (this.tile != null && !this.tile.isInvalid()) {
      super.drawBackgroundLayer(partialTicks, mouseX, mouseY);
    }
  }
}
