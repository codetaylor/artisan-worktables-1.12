package com.codetaylor.mc.artisanworktables.modules.automator.gui;

import com.codetaylor.mc.artisanworktables.modules.automator.ModuleAutomator;
import com.codetaylor.mc.artisanworktables.modules.automator.gui.element.*;
import com.codetaylor.mc.artisanworktables.modules.automator.network.CSPacketAutomatorOutputModeChange;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTitle;
import com.codetaylor.mc.athenaeum.packer.PackAPI;
import com.codetaylor.mc.athenaeum.packer.PackedData;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class AutomatorGuiContainer
    extends GuiContainerBase {

  public static final int TEXT_OUTLINE_COLOR = new Color(133, 90, 49).getRGB();

  private final TileAutomator tile;
  private final AutomatorContainer container;

  public AutomatorGuiContainer(TileAutomator tile, AutomatorContainer container, int width, int height) {

    super(container, width, height);
    this.tile = tile;
    this.container = container;

    this.createBackgroundElement();
    this.createContainerTextElements();
    this.createDeselectedTabElements();
    this.createPanelElement();
    this.createPowerPanelElements();
    this.createPatternPanelElements();
    this.createInventoryPanelElements();
    this.createSelectedTabElements();
  }

  private void createInventoryPanelElements() {

    for (int i = 0; i < 26; i++) {
      int x = i % 9;
      int y = i / 9;
      int slotIndex = i;
      this.guiContainerElementAdd(new GuiElementInventoryGhostItem(
          slotIndex,
          this.tile.getPos(),
          this::getContainerState,
          () -> this.tile.getInventoryGhostItemStackHandler().getStackInSlot(slotIndex),
          this,
          8 + (x * 18), 38 + (y * 18)
      ));
    }

    this.guiContainerElementAdd(new GuiElementButtonLockedMode(
        this.tile.getPos(),
        this.tile::isInventoryLocked,
        this::getContainerState,
        this,
        new Texture[]{
            // Expects regular textures first, then hovered textures
            this.getTexture("inventory-button-lock-unlocked"),
            this.getTexture("inventory-button-lock-locked"),
            this.getTexture("inventory-button-lock-unlocked-hovered"),
            this.getTexture("inventory-button-lock-locked-hovered")
        },
        8 + (8 * 18), 38 + (2 * 18)
    ));
  }

  private void createPatternPanelElements() {

    for (int i = 0; i < 9; i++) {
      int slotIndex = i;
      this.guiContainerElementAdd(new GuiElementButtonOutputMode(
          () -> ModuleAutomator.PACKET_SERVICE.sendToServer(
              new CSPacketAutomatorOutputModeChange(this.tile.getPos(), slotIndex)
          ),
          () -> this.tile.getOutputMode(slotIndex),
          this::getContainerState,
          this,
          new Texture[]{
              // Expects regular textures first, then hovered textures
              this.getTexture("pattern-button-keep"),
              this.getTexture("pattern-button-manual"),
              this.getTexture("pattern-button-inventory"),
              this.getTexture("pattern-button-export"),
              this.getTexture("pattern-button-keep-hovered"),
              this.getTexture("pattern-button-manual-hovered"),
              this.getTexture("pattern-button-inventory-hovered"),
              this.getTexture("pattern-button-export-hovered")
          },
          8 + (18 * i), 74,
          16, 16
      ));
    }
  }

  private void createSelectedTabElements() {

    // selected gear tab
    this.guiContainerElementAdd(new GuiElementButtonAutomatorTabSelected(
        AutomatorContainer.EnumState.Gear,
        this,
        this.getTexture("tab-gear-selected"),
        12, 17,
        20, 19
    ));

    // selected pattern tab
    this.guiContainerElementAdd(new GuiElementButtonAutomatorTabSelected(
        AutomatorContainer.EnumState.Pattern,
        this,
        this.getTexture("tab-pattern-selected"),
        12 + 20, 17,
        20, 19
    ));

    // selected inventory tab
    this.guiContainerElementAdd(new GuiElementButtonAutomatorTabSelected(
        AutomatorContainer.EnumState.Inventory,
        this,
        this.getTexture("tab-inventory-selected"),
        12 + 20 * 2, 17,
        20, 19
    ));

    // selected fluid tab
    this.guiContainerElementAdd(new GuiElementButtonAutomatorTabSelected(
        AutomatorContainer.EnumState.Fluid,
        this,
        this.getTexture("tab-fluid-selected"),
        12 + 20 * 3, 17,
        20, 19
    ));

    // selected tool tab
    this.guiContainerElementAdd(new GuiElementButtonAutomatorTabSelected(
        AutomatorContainer.EnumState.Tool,
        this,
        this.getTexture("tab-tool-selected"),
        12 + 20 * 4, 17,
        20, 19
    ));
  }

  private void createPowerPanelElements() {

    // lit power bar
    this.guiContainerElementAdd(new GuiElementPowerBar(
        this.tile::getEnergyAmount,
        this.tile::getEnergyCapacity,
        this,
        this.getTexture("power-power-bar-lit"),
        74, 49,
        83, 7
    ));

    // lit duration bar
    this.guiContainerElementAdd(new GuiElementDurationBar(
        this.tile::getProgress,
        this,
        this.getTexture("power-duration-bar-lit"),
        74, 57,
        83, 4
    ));
  }

  private void createPanelElement() {

    // panel texture
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
  }

  private void createBackgroundElement() {

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

  private void createContainerTextElements() {

    // title
    this.guiContainerElementAdd(new GuiElementTitle(
        this,
        "tile.artisanworktables.automator.name",
        8,
        7
    ));

    // inventory title
    this.guiContainerElementAdd(new GuiElementTitle(
        this,
        "container.inventory",
        8,
        this.ySize - 93
    ));
  }

  private void createDeselectedTabElements() {

    // deselected gear tab
    {
      Texture texture = this.getTexture("tab-gear-dark");
      this.guiContainerElementAdd(new GuiElementButtonAutomatorTab(
          AutomatorContainer.EnumState.Gear,
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
          AutomatorContainer.EnumState.Pattern,
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
          AutomatorContainer.EnumState.Inventory,
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
          AutomatorContainer.EnumState.Fluid,
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
          AutomatorContainer.EnumState.Tool,
          this,
          texture,
          12 + 20 * 4, 21,
          20, 18
      ));
    }
  }

  public AutomatorContainer.EnumState getContainerState() {

    return this.container.getState();
  }

  @Override
  public void drawString(String translateKey, int x, int y) {

    FontRenderer fontRenderer = this.fontRenderer;
    GuiHelper.drawStringOutlined(translateKey, x, y, fontRenderer, TEXT_OUTLINE_COLOR);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  private Texture getTexture(String path) {

    PackedData.ImageData imageData = PackAPI.getImageData(new ResourceLocation(ModuleAutomator.MOD_ID, path));
    ResourceLocation atlasResourceLocation = new ResourceLocation(ModuleAutomator.MOD_ID, imageData.atlas);
    PackedData.AtlasData atlasData = PackAPI.getAtlasData(atlasResourceLocation);
    return new Texture(atlasResourceLocation, imageData.u, imageData.v, atlasData.width, atlasData.height);
  }
}
