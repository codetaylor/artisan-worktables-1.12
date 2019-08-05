package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.element.*;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot.ICreativeSlotClick;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.ITileEntityDesigner;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.athenaeum.gui.GuiHelper;
import com.codetaylor.mc.athenaeum.gui.Texture;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.gui.element.GuiElementTitle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.ArrayList;

public abstract class AWGuiContainerBase
    extends com.codetaylor.mc.athenaeum.gui.GuiContainerBase {

  protected final int textShadowColor;
  protected final TileEntityBase tileEntity;
  protected final int fluidTankOverlayColor;

  public AWGuiContainerBase(
      AWContainer container,
      ResourceLocation backgroundTexture,
      String titleKey,
      TileEntityBase tileEntity,
      int width,
      int height
  ) {

    super(container, width, height);
    this.tileEntity = tileEntity;

    EnumType tableType = tileEntity.getType();
    String tableTypeName = tableType.getName();
    this.textShadowColor = ModuleWorktablesConfig.CLIENT.getTextHighlightColor(tableTypeName);
    this.fluidTankOverlayColor = ModuleWorktablesConfig.CLIENT.getFluidTankOverlayColor(tableTypeName);

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

    // export shaped button
    if (Minecraft.getMinecraft().player.isCreative()) {
      this.guiContainerElementAdd(new GuiElementExportButtonUnlock(
          this, this.getXSize() - 18, 4
      ));

      int elementY = this.ySize - 95;
      this.guiContainerElementAdd(new GuiElementExportButtonShaped(
          this, this.getXSize() - 18, elementY
      ));
      this.guiContainerElementAdd(new GuiElementExportButtonShapeless(
          this, this.getXSize() - 18 - 12, elementY
      ));
      this.guiContainerElementAdd(new GuiElementExportButtonOredictLink(
          this, this.getXSize() - 18 - 12 * 2, elementY
      ));
      this.guiContainerElementAdd(new GuiElementExportButtonClearAll(
          this, this.getXSize() - 18 - 12 * 3, elementY
      ));
    }

    // slot background quads
    this.addSlotBackgrounds();

    // mage special effect
    if (tableType == EnumType.MAGE) {
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
    if (this.tileEntity.allowTabs()) {
      this.guiContainerElementAdd(new GuiElementTabs(
          this,
          this.tileEntity,
          176
      ));
    }
  }

  protected void addSlotBackgrounds() {

    //if (this.tileEntity.getTier() == EnumTier.WORKTABLE) {
    String tableTypeName = this.tileEntity.getType().getName();

    // secondary slots
    {
      Integer color = ModuleWorktablesConfig.CLIENT.getCraftingGridSlotBackgroundColor(tableTypeName);

      if (color != null) {
        if (this.tileEntity.getTier() == EnumTier.WORKSTATION) {
          for (int x = 0; x < 9; x++) {
            this.guiContainerElementAdd(new GuiElementColoredQuad(
                this,
                color,
                8 + x * 18,
                75,
                16,
                16
            ));
          }

        } else if (this.tileEntity.getTier() == EnumTier.WORKSHOP) {
          for (int x = 0; x < 9; x++) {
            this.guiContainerElementAdd(new GuiElementColoredQuad(
                this,
                color,
                8 + x * 18,
                111,
                16,
                16
            ));
          }
        }
      }
    }

    // player inventory
    {
      Integer color = ModuleWorktablesConfig.CLIENT.getPlayerInventorySlotBackgroundColor(tableTypeName);

      int offsetY = 0;

      if (this.tileEntity.getTier() == EnumTier.WORKSTATION) {
        offsetY = 23;

      } else if (this.tileEntity.getTier() == EnumTier.WORKSHOP) {
        offsetY = 59;
      }

      if (color != null) {
        for (int x = 0; x < 9; x++) {
          for (int y = 0; y < 3; y++) {
            this.guiContainerElementAdd(new GuiElementColoredQuad(
                this,
                color,
                8 + x * 18,
                84 + y * 18 + offsetY,
                16,
                16
            ));
          }
        }
        for (int x = 0; x < 9; x++) {
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              8 + x * 18,
              142 + offsetY,
              16,
              16
          ));
        }
      }
    }

    // crafting grid
    {
      Integer color = ModuleWorktablesConfig.CLIENT.getCraftingGridSlotBackgroundColor(tableTypeName);

      if (color != null) {

        int size = 3;

        if (this.tileEntity.getTier() == EnumTier.WORKSHOP) {
          size = 5;
        }

        for (int x = 0; x < size; x++) {
          for (int y = 0; y < size; y++) {
            this.guiContainerElementAdd(new GuiElementColoredQuad(
                this,
                color,
                20 + x * 18,
                17 + y * 18,
                16,
                16
            ));
          }
        }
      }
    }

    // tool slot
    {
      Integer color = ModuleWorktablesConfig.CLIENT.getCraftingGridSlotBackgroundColor(tableTypeName);

      if (color != null) {
        if (this.tileEntity.getTier() == EnumTier.WORKTABLE) {
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              78,
              35,
              16,
              16
          ));

        } else if (this.tileEntity.getTier() == EnumTier.WORKSTATION) {
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              78,
              24,
              16,
              16
          ));
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              78,
              46,
              16,
              16
          ));

        } else if (this.tileEntity.getTier() == EnumTier.WORKSHOP) {
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              114,
              24 + 16,
              16,
              16
          ));
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              114,
              46 + 16,
              16,
              16
          ));
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              114,
              68 + 16,
              16,
              16
          ));
        }
      }
    }

    // fluid tank
    {
      Integer color = ModuleWorktablesConfig.CLIENT.getFluidTankBackgroundColor(tableTypeName);

      if (color != null) {
        if (this.tileEntity.getTier() == EnumTier.WORKSHOP) {
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              8,
              17,
              6,
              88
          ));

        } else {
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              8,
              17,
              6,
              52
          ));
        }
      }
    }

    // main output
    {
      Integer color = ModuleWorktablesConfig.CLIENT.getMainOutputSlotBackgroundColor(tableTypeName);

      if (color != null) {
        if (this.tileEntity.getTier() == EnumTier.WORKSHOP) {
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              143,
              62,
              16,
              16
          ));

        } else {
          this.guiContainerElementAdd(new GuiElementColoredQuad(
              this,
              color,
              111,
              31,
              24,
              24
          ));
        }
      }
    }

    // extra outputs
    {
      Integer color = ModuleWorktablesConfig.CLIENT.getCraftingGridSlotBackgroundColor(tableTypeName);

      if (color != null) {
        if (this.tileEntity.getTier() == EnumTier.WORKSHOP) {
          for (int x = 0; x < 3; x++) {
            this.guiContainerElementAdd(new GuiElementColoredQuad(
                this,
                color,
                116 + x * 18,
                17,
                16,
                16
            ));
          }

        } else {
          for (int y = 0; y < 3; y++) {
            this.guiContainerElementAdd(new GuiElementColoredQuad(
                this,
                color,
                135 + 17,
                17 + y * 18,
                16,
                16
            ));
          }
        }
      }
    }
    //}
  }

  protected void addInventoryTitleElement() {

    this.guiContainerElementAdd(new GuiElementTitle(
        this,
        "container.inventory",
        8,
        this.ySize - 93
    ));
  }

  protected void addMageEffectElement(AWContainer container) {

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
        this.fluidTankOverlayColor,
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

  public TileEntityBase getTileEntity() {

    return this.tileEntity;
  }

  protected void renderToolTip(ItemStack stack, int x, int y) {

    Slot slot = this.getSlotUnderMouse();

    if (!(slot instanceof ICreativeSlotClick)
        || !((ICreativeSlotClick) slot).allowOredict()) {
      super.renderToolTip(stack, x, y);
      return;
    }

    Item item = stack.getItem();
    ResourceLocation resourceLocation = item.getRegistryName();

    if (this.tileEntity.isCreative()
        && resourceLocation != null) {

      FontRenderer font = item.getFontRenderer(stack);
      net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);

      int slotNumber = slot.slotNumber;
      String lookup = this.tileEntity.oreDictMap.lookup(slotNumber);

      ArrayList<String> lines = new ArrayList<>();
      String suffix = String.format("(#%04d/%d)", Item.getIdFromItem(item), stack.getItemDamage());
      lines.add(stack.getRarity().rarityColor + stack.getDisplayName() + " " + suffix);
      lines.add(TextFormatting.DARK_GRAY + resourceLocation.getResourceDomain() + ":" + resourceLocation.getResourcePath());

      int[] oreIDs = OreDictionary.getOreIDs(stack);

      if (oreIDs.length > 0) {
        lines.add("");
        lines.add(I18n.format(ModuleWorktables.Lang.ITEM_TOOLTIP_CREATIVE_TABLE_OREDICT_HEADER, TextFormatting.DARK_GRAY));

        if (lookup == null) {
          lines.add("  " + TextFormatting.GOLD + I18n.format(ModuleWorktables.Lang.ITEM_TOOLTIP_CREATIVE_TABLE_OREDICT_NONE));

        } else {
          //lines.add(lookup);
          lines.add("  " + I18n.format(ModuleWorktables.Lang.ITEM_TOOLTIP_CREATIVE_TABLE_OREDICT_NONE));
        }

        for (int oreID : oreIDs) {
          String oreName = OreDictionary.getOreName(oreID);

          if (lookup != null
              && lookup.equals(oreName)) {
            lines.add("  " + TextFormatting.GOLD + oreName);

          } else {
            lines.add("  " + oreName);
          }
        }
      }

      this.drawHoveringText(lines, x, y, (font == null ? this.fontRenderer : font));
      net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();

    } else {
      super.renderToolTip(stack, x, y);
    }
  }
}
