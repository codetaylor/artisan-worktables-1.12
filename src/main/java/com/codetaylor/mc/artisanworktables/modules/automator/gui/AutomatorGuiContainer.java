package com.codetaylor.mc.artisanworktables.modules.automator.gui;

import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.element.GuiElementButtonAutomatorTab;
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

    // deselected gear tab
    {
      Texture texture = this.getTexture("tab-gear-dark");
      this.guiContainerElementAdd(new GuiElementButtonAutomatorTab(
          this,
          texture,
          12, 21,
          20, 18
      ));
    }

    // deselected pattern tab
    {
      Texture texture = this.getTexture("tab-pattern-dark");
      this.guiContainerElementAdd(new GuiElementButtonAutomatorTab(
          this,
          texture,
          12 + 20, 21,
          20, 18
      ));
    }

    // deselected inventory tab
    {
      Texture texture = this.getTexture("tab-inventory-dark");
      this.guiContainerElementAdd(new GuiElementButtonAutomatorTab(
          this,
          texture,
          12 + 20 * 2, 21,
          20, 18
      ));
    }

    // deselected fluid tab
    {
      Texture texture = this.getTexture("tab-fluid-dark");
      this.guiContainerElementAdd(new GuiElementButtonAutomatorTab(
          this,
          texture,
          12 + 20 * 3, 21,
          20, 18
      ));
    }
    // deselected tool tab
    {
      Texture texture = this.getTexture("tab-tool-dark");
      this.guiContainerElementAdd(new GuiElementButtonAutomatorTab(
          this,
          texture,
          12 + 20 * 4, 21,
          20, 18
      ));
    }

    // temp panel texture
    this.guiContainerElementAdd(new GuiElementTextureRectangle(
        this,
        this.getTexture("background#panel-power"),
        5,
        36,
        166,
        58
    ));
  }

  private Texture getTexture(String path) {

    PackedData.ImageData imageData = PackAPI.getImageData(new ResourceLocation(ModuleAutomator.MOD_ID, path));
    ResourceLocation atlasResourceLocation = new ResourceLocation(ModuleAutomator.MOD_ID, imageData.atlas);
    PackedData.AtlasData atlasData = PackAPI.getAtlasData(atlasResourceLocation);
    return new Texture(atlasResourceLocation, imageData.u, imageData.v, atlasData.width, atlasData.height);
  }
}
