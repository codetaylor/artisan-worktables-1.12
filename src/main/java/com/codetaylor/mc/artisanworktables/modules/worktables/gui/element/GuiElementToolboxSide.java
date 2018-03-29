package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.Container;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.ITileEntityDesigner;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;

public class GuiElementToolboxSide
    extends GuiElementTextureRectangle {

  private Container container;
  private final TileEntityToolbox toolbox;
  private ITileEntityDesigner designersTable;

  public GuiElementToolboxSide(
      GuiContainerBase guiBase,
      Container container,
      TileEntityToolbox toolbox,
      Texture texture,
      ITileEntityDesigner designersTable,
      int elementX,
      int elementY
  ) {

    super(guiBase, texture, elementX, elementY, 68, 176);
    this.container = container;
    this.toolbox = toolbox;
    this.designersTable = designersTable;
  }

  @Override
  public void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    if (this.toolbox != null && !this.toolbox.isInvalid()) {
      super.drawBackgroundLayer(partialTicks, mouseX, mouseY);
    }
  }

  @Override
  protected int elementYModifiedGet() {

    return super.elementYModifiedGet() + ((this.designersTable != null && this.container.canPlayerUsePatternSlots()) ? 33 : 0);
  }
}
