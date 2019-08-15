package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class GuiElementGhostItem
    extends GuiElementBase {

  private final TileEntityBase tile;
  private final IItemHandler handler;
  private final IItemHandler handlerGhost;
  private final int index;

  public GuiElementGhostItem(
      GuiContainerBase guiBase,
      TileEntityBase tile,
      IItemHandler handler,
      IItemHandler handlerGhost,
      int index,
      int elementX,
      int elementY,
      int elementWidth,
      int elementHeight
  ) {

    super(guiBase, elementX, elementY, elementWidth, elementHeight);
    this.tile = tile;
    this.handler = handler;
    this.handlerGhost = handlerGhost;
    this.index = index;
  }

  @Override
  public void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    ItemStack handlerStack = this.handler.getStackInSlot(this.index);
    ItemStack ghostStack = this.handlerGhost.getStackInSlot(this.index);

    if (handlerStack.isEmpty()
        && !ghostStack.isEmpty()
        && this.tile.isLocked()) {
      RenderItem itemRender = this.guiBase.getItemRender();
      RenderHelper.enableGUIStandardItemLighting();
      itemRender.renderItemIntoGUI(ghostStack, this.elementXModifiedGet(), this.elementYModifiedGet());
      itemRender.renderItemOverlayIntoGUI(this.guiBase.getFontRenderer(), ghostStack, this.elementXModifiedGet(), this.elementYModifiedGet(), "-");
      RenderHelper.disableStandardItemLighting();
    }
  }

  @Override
  public void drawForegroundLayer(int mouseX, int mouseY) {
    //
  }
}
