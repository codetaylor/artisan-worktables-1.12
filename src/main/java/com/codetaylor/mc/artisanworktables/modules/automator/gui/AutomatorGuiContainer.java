package com.codetaylor.mc.artisanworktables.modules.automator.gui;

import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.athenaeum.gui.ContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.packer.PackAPI;
import com.codetaylor.mc.athenaeum.packer.PackedData;
import net.minecraft.util.ResourceLocation;

public class AutomatorGuiContainer
    extends GuiContainerBase {

  public AutomatorGuiContainer(ContainerBase container, int width, int height) {

    super(container, width, height);

    // background texture
    this.guiContainerElementAdd(new GuiElementTextureRectangle(
        this,
        this.getTexture("background#all"),
        0,
        0,
        this.xSize,
        this.ySize
    ));
  }

  private Texture getTexture(String path) {

    PackedData.ImageData imageData = PackAPI.getImageData(new ResourceLocation(ModuleAutomator.MOD_ID, path));
    ResourceLocation atlasResourceLocation = new ResourceLocation(ModuleAutomator.MOD_ID, imageData.atlas);
    PackedData.AtlasData atlasData = PackAPI.getAtlasData(atlasResourceLocation);
    return new Texture(atlasResourceLocation, imageData.u, imageData.v, atlasData.width, atlasData.height);
  }
}
