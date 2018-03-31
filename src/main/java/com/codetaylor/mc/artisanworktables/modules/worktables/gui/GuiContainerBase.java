package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.element.*;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.ITileEntityDesigner;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTitle;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public abstract class GuiContainerBase
    extends com.codetaylor.mc.athenaeum.gui.GuiContainerBase {

  protected final int textShadowColor;
  protected final TileEntityBase tileEntity;

  public GuiContainerBase(
      Container container,
      ResourceLocation backgroundTexture,
      String titleKey,
      int textShadowColor,
      TileEntityBase tileEntity,
      int width,
      int height
  ) {

    super(container, width, height);
    this.tileEntity = tileEntity;
    this.textShadowColor = textShadowColor;

    // worktable title
    this.guiContainerElementAdd(new GuiElementTitle(
        this,
        titleKey,
        8,
        6
    ));

    // inventory title
    this.addInventoryTitleElement();

    // background texture
    this.guiContainerElementAdd(new GuiElementTextureRectangle(
        this,
        new Texture(backgroundTexture, 0, 0, 256, 256),
        0,
        0,
        this.xSize,
        this.ySize
    ));

    // mage special effect
    if (this.tileEntity.getType() == EnumType.MAGE) {
      this.addMageEffectElement(container);
    }

    // fluid tank
    this.addFluidTankElement();

    // pattern slots
    ITileEntityDesigner designersTable = container.getDesignersTable();

    if (designersTable != null && container.canPlayerUsePatternSlots()) {
      this.guiContainerElementAdd(new GuiElementDesignersSide(
          this,
          designersTable,
          designersTable.getTexturePatternSide(),
          -70,
          0
      ));
    }

    // toolbox side
    TileEntityToolbox toolbox = container.getToolbox();

    if (toolbox != null && container.canPlayerUseToolbox()) {
      this.guiContainerElementAdd(new GuiElementToolboxSide(
          this,
          toolbox,
          toolbox.getTextureSide(),
          -70,
          () -> ((designersTable != null && container.canPlayerUsePatternSlots()) ? 33 : 0)
      ));
    }

    // tabs
    this.guiContainerElementAdd(new GuiElementTabs(
        this,
        this.tileEntity,
        176
    ));
  }

  protected void addInventoryTitleElement() {

    this.guiContainerElementAdd(new GuiElementTitle(
        this,
        "container.inventory",
        8,
        this.ySize - 93
    ));
  }

  protected void addMageEffectElement(Container container) {

    this.guiContainerElementAdd(new GuiElementMageEffect(
        this,
        container,
        115,
        35
    ));
  }

  protected void addFluidTankElement() {

    this.guiContainerElementAdd(new GuiElementFluidTankSmall(
        this,
        this.tileEntity.getTank(),
        this.tileEntity.getPos(),
        this.textShadowColor,
        8,
        17
    ));
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  public void drawString(String translateKey, int x, int y) {

    FontRenderer fontRenderer = this.fontRenderer;

    if (this.tileEntity.getType() == EnumType.MAGE) {
      fontRenderer = this.mc.standardGalacticFontRenderer;
    }

    if (this.tileEntity.getType() == EnumType.DESIGNER) {
      String displayText = I18n.format(translateKey);
      fontRenderer.drawString(displayText, x - 1, y, Color.WHITE.getRGB());

    } else {
      GuiHelper.drawStringOutlined(translateKey, x, y, fontRenderer, this.textShadowColor);
    }
  }

}
