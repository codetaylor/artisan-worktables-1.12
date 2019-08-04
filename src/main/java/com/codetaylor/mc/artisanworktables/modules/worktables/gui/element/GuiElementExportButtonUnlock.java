package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.AWGuiContainerBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.network.CSPacketWorktableCreateModeToggle;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureButtonBase;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public class GuiElementExportButtonUnlock
    extends GuiElementTextureButtonBase
    implements IGuiElementTooltipProvider {

  private static final int TEXTURE_BASE_UNLOCKED_INDEX = 2;
  private static final int TEXTURE_HOVERED_UNLOCKED_INDEX = 3;

  public GuiElementExportButtonUnlock(GuiContainerBase guiBase, int elementX, int elementY) {

    super(
        guiBase,
        new Texture[]{
            // locked
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 33, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 33, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            // unlocked
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 44, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 44, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT)
        },
        elementX,
        elementY,
        11,
        11
    );
  }

  public boolean isLocked() {

    AWGuiContainerBase gui = (AWGuiContainerBase) this.guiBase;
    TileEntityBase tileEntity = gui.getTileEntity();
    return !tileEntity.isCreative();
  }

  @Override
  protected int textureIndexGet(int mouseX, int mouseY) {

    if (this.isLocked()) {
      return super.textureIndexGet(mouseX, mouseY);

    } else {

      if (this.elementIsMouseInside(mouseX, mouseY)) {
        return TEXTURE_HOVERED_UNLOCKED_INDEX;
      }

      return TEXTURE_BASE_UNLOCKED_INDEX;
    }
  }

  @Override
  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    super.elementClicked(mouseX, mouseY, mouseButton);

    AWGuiContainerBase gui = (AWGuiContainerBase) this.guiBase;
    TileEntityBase tileEntity = gui.getTileEntity();
    ModuleWorktables.PACKET_SERVICE.sendToServer(new CSPacketWorktableCreateModeToggle(tileEntity.getPos()));
  }

  @Override
  public List<String> tooltipTextGet(List<String> tooltip) {

    AWGuiContainerBase gui = (AWGuiContainerBase) this.guiBase;
    TileEntityBase tileEntity = gui.getTileEntity();

    if (tileEntity.isCreative()) {
      tooltip.add(I18n.translateToLocal("gui.artisanworktables.tooltip.button.creative.enabled"));

    } else {
      tooltip.add(I18n.translateToLocal("gui.artisanworktables.tooltip.button.creative.disabled"));
    }
    return tooltip;
  }
}
