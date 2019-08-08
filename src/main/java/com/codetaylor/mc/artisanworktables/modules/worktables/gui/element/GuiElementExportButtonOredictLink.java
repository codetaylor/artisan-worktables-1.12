package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.AWGuiContainerBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureButtonBase;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public class GuiElementExportButtonOredictLink
    extends GuiElementTextureButtonBase
    implements IGuiElementTooltipProvider {

  private static final int TEXTURE_BASE_UNLINKED_INDEX = 2;
  private static final int TEXTURE_HOVERED_UNLINKED_INDEX = 3;

  public GuiElementExportButtonOredictLink(GuiContainerBase guiBase, int elementX, int elementY) {

    super(
        guiBase,
        new Texture[]{
            // locked
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 55, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 55, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            // unlocked
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 66, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 66, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT)
        },
        elementX,
        elementY,
        11,
        11
    );
  }

  public boolean isOreDictLinked() {

    AWGuiContainerBase gui = (AWGuiContainerBase) this.guiBase;
    TileEntityBase tileEntity = gui.getTileEntity();
    return tileEntity.isOreDictLinked();
  }

  @Override
  protected int textureIndexGet(int mouseX, int mouseY) {

    if (!this.isOreDictLinked()) {
      return super.textureIndexGet(mouseX, mouseY);

    } else {

      if (this.elementIsMouseInside(mouseX, mouseY)) {
        return TEXTURE_HOVERED_UNLINKED_INDEX;
      }

      return TEXTURE_BASE_UNLINKED_INDEX;
    }
  }

  @Override
  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    super.elementClicked(mouseX, mouseY, mouseButton);

    AWGuiContainerBase gui = (AWGuiContainerBase) this.guiBase;
    TileEntityBase tileEntity = gui.getTileEntity();
    tileEntity.setOredictLinked(!tileEntity.isOreDictLinked());
  }

  @Override
  public List<String> tooltipTextGet(List<String> tooltip) {

    AWGuiContainerBase gui = (AWGuiContainerBase) this.guiBase;
    TileEntityBase tileEntity = gui.getTileEntity();

    if (tileEntity.isOreDictLinked()) {
      tooltip.add(I18n.translateToLocal("gui.artisanworktables.tooltip.button.oredict.linked"));

    } else {
      tooltip.add(I18n.translateToLocal("gui.artisanworktables.tooltip.button.oredict.unlinked"));
    }
    return tooltip;
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    AWGuiContainerBase gui = (AWGuiContainerBase) this.guiBase;
    TileEntityBase tileEntity = gui.getTileEntity();
    return tileEntity.isCreative();
  }
}
