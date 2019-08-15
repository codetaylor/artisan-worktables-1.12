package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

public class GuiElementLockedSlotIndicator
    extends GuiElementTextureRectangle {

  private final TileEntityBase tile;

  public GuiElementLockedSlotIndicator(
      GuiContainerBase guiBase,
      TileEntityBase tile,
      int elementX,
      int elementY
  ) {

    super(
        guiBase,
        new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 154, 0, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
        elementX,
        elementY,
        4,
        5
    );
    this.tile = tile;
  }

  @Override
  public void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

//    super.drawBackgroundLayer(partialTicks, mouseX, mouseY);
  }

  @Override
  public void drawForegroundLayer(int mouseX, int mouseY) {

    super.drawForegroundLayer(mouseX, mouseY);

    this.textureBind(this.textures[0]);

    GlStateManager.color(1,1,1);

    GuiHelper.drawModalRectWithCustomSizedTexture(
        this.elementX,
        this.elementY,
        300,
        this.textures[0].getPositionX(),
        this.textures[0].getPositionY(),
        4,
        5,
        this.textures[0].getWidth(),
        this.textures[0].getHeight()
    );
    //super.drawBackgroundLayer(0, mouseX, mouseY);
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    return this.tile.isLocked();
  }
}
