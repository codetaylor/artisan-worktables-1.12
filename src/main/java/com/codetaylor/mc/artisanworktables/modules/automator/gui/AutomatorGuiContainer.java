package com.codetaylor.mc.artisanworktables.modules.automator.gui;

import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.element.GuiElementAutomatorPanel;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.element.GuiElementButtonAutomatorTab;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.element.GuiElementButtonAutomatorTabSelected;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.packer.PackAPI;
import com.codetaylor.mc.athenaeum.packer.PackedData;
import net.minecraft.util.ResourceLocation;

public class AutomatorGuiContainer
    extends GuiContainerBase {

  private final AutomatorContainer container;

  public AutomatorGuiContainer(AutomatorContainer container, int width, int height) {

    super(container, width, height);
    this.container = container;

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
          AutomatorContainer.State.Gear,
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
          AutomatorContainer.State.Pattern,
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
          AutomatorContainer.State.Inventory,
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
          AutomatorContainer.State.Fluid,
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
          AutomatorContainer.State.Tool,
          this,
          texture,
          12 + 20 * 4, 21,
          20, 18
      ));
    }

    // power panel texture
    this.guiContainerElementAdd(new GuiElementAutomatorPanel(
        this,
        new Texture[]{
            this.getTexture("background#panel-power"),
            this.getTexture("panel-pattern"),
            this.getTexture("panel-inventory"),
            this.getTexture("panel-fluid"),
            this.getTexture("panel-tool")
        },
        5, 35,
        166, 58
    ));

    // selected gear tab
    this.guiContainerElementAdd(new GuiElementButtonAutomatorTabSelected(
        AutomatorContainer.State.Gear,
        this,
        this.getTexture("tab-gear-selected"),
        12, 17,
        20, 19
    ));

    // selected pattern tab
    this.guiContainerElementAdd(new GuiElementButtonAutomatorTabSelected(
        AutomatorContainer.State.Pattern,
        this,
        this.getTexture("tab-pattern-selected"),
        12 + 20, 17,
        20, 19
    ));

    // selected inventory tab
    this.guiContainerElementAdd(new GuiElementButtonAutomatorTabSelected(
        AutomatorContainer.State.Inventory,
        this,
        this.getTexture("tab-inventory-selected"),
        12 + 20 * 2, 17,
        20, 19
    ));

    // selected fluid tab
    this.guiContainerElementAdd(new GuiElementButtonAutomatorTabSelected(
        AutomatorContainer.State.Fluid,
        this,
        this.getTexture("tab-fluid-selected"),
        12 + 20 * 3, 17,
        20, 19
    ));

    // selected tool tab
    this.guiContainerElementAdd(new GuiElementButtonAutomatorTabSelected(
        AutomatorContainer.State.Tool,
        this,
        this.getTexture("tab-tool-selected"),
        12 + 20 * 4, 17,
        20, 19
    ));
  }

  public AutomatorContainer.State getContainerState() {

    return this.container.getState();
  }

  private Texture getTexture(String path) {

    PackedData.ImageData imageData = PackAPI.getImageData(new ResourceLocation(ModuleAutomator.MOD_ID, path));
    ResourceLocation atlasResourceLocation = new ResourceLocation(ModuleAutomator.MOD_ID, imageData.atlas);
    PackedData.AtlasData atlasData = PackAPI.getAtlasData(atlasResourceLocation);
    return new Texture(atlasResourceLocation, imageData.u, imageData.v, atlasData.width, atlasData.height);
  }
}
