package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.lib.ISupplierInteger;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.ITileEntityDesigner;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;

public class GuiElementToolboxSide
    extends GuiElementTextureRectangle {

  private final TileEntityToolbox toolbox;
  private final ISupplierInteger elementY;

  public GuiElementToolboxSide(
      GuiContainerBase guiBase,
      TileEntityToolbox toolbox,
      Texture texture,
      int elementX,
      ISupplierInteger elementY
  ) {

    super(guiBase, texture, elementX, 0, 68, 176);
    this.toolbox = toolbox;
    this.elementY = elementY;
  }

  @Override
  public void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    if (this.toolbox != null && !this.toolbox.isInvalid()) {
      super.drawBackgroundLayer(partialTicks, mouseX, mouseY);
    }
  }

  @Override
  protected int elementYModifiedGet() {

    return super.elementYModifiedGet() + this.elementY.get();
  }
}
