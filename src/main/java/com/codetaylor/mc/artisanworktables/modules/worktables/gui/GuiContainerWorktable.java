package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.element.*;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityWorktableMage;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityWorktableBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityWorktableFluidBase;
import com.codetaylor.mc.athenaeum.gui.*;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTitle;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import yalter.mousetweaks.api.MouseTweaksDisableWheelTweak;

@MouseTweaksDisableWheelTweak
public class GuiContainerWorktable
    extends GuiContainerBase {

  private final int textShadowColor;
  private final TileEntityWorktableBase currentWorktable;

  public GuiContainerWorktable(
      ContainerWorktable container,
      ResourceLocation backgroundTexture,
      String titleKey,
      int textShadowColor,
      TileEntityWorktableBase currentWorktable
  ) {

    super(container, 176, 166);
    this.textShadowColor = textShadowColor;
    this.currentWorktable = currentWorktable;

    // worktable title
    this.guiContainerElementAdd(new GuiElementTitle(
        this,
        titleKey,
        8,
        6
    ));

    // inventory title
    this.guiContainerElementAdd(new GuiElementTitle(
        this,
        "container.inventory",
        8,
        this.ySize - 93
    ));

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
    if (this.currentWorktable instanceof TileEntityWorktableMage) {
      this.guiContainerElementAdd(new GuiElementMageEffect(
          this,
          container,
          115,
          35
      ));
    }

    // fluid tank
    if (this.currentWorktable instanceof TileEntityWorktableFluidBase) {
      this.guiContainerElementAdd(new GuiElementWorktableFluidTank(
          this,
          ((TileEntityWorktableFluidBase) this.currentWorktable).getTank(),
          this.currentWorktable.getPos(),
          this.textShadowColor,
          8,
          17
      ));
    }

    // toolbox side
    TileEntityToolbox toolbox = container.getToolbox();

    if (toolbox != null && !toolbox.isInvalid()) {
      this.guiContainerElementAdd(new GuiElementToolboxSide(
          this,
          toolbox,
          toolbox.getTextureSide(),
          -70,
          0
      ));
    }

    // tabs
    this.guiContainerElementAdd(new GuiElementTabs(
        this,
        this.currentWorktable,
        176
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

    if (this.currentWorktable instanceof TileEntityWorktableMage) {
      fontRenderer = this.mc.standardGalacticFontRenderer;
    }

    GuiHelper.drawStringOutlined(translateKey, x, y, fontRenderer, this.textShadowColor);
  }

}
