package com.codetaylor.mc.artisanworktables.modules.worktables.gui.element;

import com.codetaylor.mc.artisanworktables.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.AWContainer;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.AWGuiContainerBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.ZSRecipeExport;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureButtonBase;
import com.codetaylor.mc.athenaeum.gui.element.IGuiElementTooltipProvider;
import net.minecraft.util.text.translation.I18n;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

public class GuiElementExportButtonShapeless
    extends GuiElementTextureButtonBase
    implements IGuiElementTooltipProvider {

  public GuiElementExportButtonShapeless(GuiContainerBase guiBase, int elementX, int elementY) {

    super(
        guiBase,
        new Texture[]{
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158, 11, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT),
            new Texture(ReferenceTexture.RESOURCE_LOCATION_GUI_ELEMENTS, 158 + 11, 11, ReferenceTexture.TEXTURE_GUI_ELEMENTS_WIDTH, ReferenceTexture.TEXTURE_GUI_ELEMENTS_HEIGHT)
        },
        elementX,
        elementY,
        11,
        11
    );
  }

  @Override
  public void elementClicked(int mouseX, int mouseY, int mouseButton) {

    super.elementClicked(mouseX, mouseY, mouseButton);

    AWGuiContainerBase gui = (AWGuiContainerBase) this.guiBase;
    TileEntityBase tileEntity = gui.getTileEntity();

    String data = ZSRecipeExport.getExportString((AWContainer) gui.inventorySlots, tileEntity, false);
    StringSelection contents = new StringSelection(data);
    Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
    Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
    systemClipboard.setContents(contents, null);
  }

  @Override
  public List<String> tooltipTextGet(List<String> tooltip) {

    tooltip.add(I18n.translateToLocal("gui.artisanworktables.tooltip.button.export.shapeless"));
    return tooltip;
  }

  @Override
  public boolean elementIsVisible(int mouseX, int mouseY) {

    AWGuiContainerBase gui = (AWGuiContainerBase) this.guiBase;
    TileEntityBase tileEntity = gui.getTileEntity();
    return tileEntity.isCreative();
  }
}
