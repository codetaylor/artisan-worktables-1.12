package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.gui.Texture;

public class GuiElementToolboxSide
    extends GuiElementTextureRectangle {

  private final TileEntityToolbox toolbox;

  public GuiElementToolboxSide(
      GuiContainerBase guiBase,
      TileEntityToolbox toolbox,
      Texture texture,
      int elementX,
      int elementY
  ) {

    super(guiBase, texture, elementX, elementY, 68, 176);
    this.toolbox = toolbox;
  }

  @Override
  public void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    if (this.toolbox != null && !this.toolbox.isInvalid()) {
      super.drawBackgroundLayer(partialTicks, mouseX, mouseY);
    }
  }
}
