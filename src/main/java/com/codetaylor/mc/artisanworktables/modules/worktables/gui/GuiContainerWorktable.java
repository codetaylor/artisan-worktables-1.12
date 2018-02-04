package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.element.GuiElementMageEffect;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.element.GuiElementToolboxSide;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.element.GuiElementWorktableFluidTank;
import com.codetaylor.mc.artisanworktables.modules.worktables.network.SPacketWorktableTab;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityWorktableMage;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityWorktableBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityWorktableFluidBase;
import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiElementTextureRectangle;
import com.codetaylor.mc.athenaeum.gui.Texture;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import yalter.mousetweaks.api.MouseTweaksDisableWheelTweak;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@MouseTweaksDisableWheelTweak
public class GuiContainerWorktable
    extends GuiContainerBase {

  private static final int TAB_WIDTH = 24;
  private static final int TAB_SPACING = 2;
  private static final int TAB_CURRENT_OFFSET = 1;
  private static final int TAB_HEIGHT = 21;
  private static final int TAB_LEFT_OFFSET = 4;
  private static final int TAB_ITEM_HORZONTAL_OFFSET = 4;
  private static final int TAB_ITEM_VERTICAL_OFFSET = 4;

  private static final int GUI_ELEMENTS_TEXTURE_HEIGHT = 256;
  private static final int GUI_ELEMENTS_TEXTURE_WIDTH = 256;

  private static final ResourceLocation TEXTURE_GUI_ELEMENTS = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      "textures/gui/gui_elements.png"
  );

  private final ContainerWorktable container;
  private final ResourceLocation backgroundTexture;
  private final String titleKey;
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
    this.container = container;
    this.backgroundTexture = backgroundTexture;
    this.titleKey = titleKey;
    this.textShadowColor = textShadowColor;
    this.currentWorktable = currentWorktable;

    // background texture
    this.guiContainerElementAdd(new GuiElementTextureRectangle(
        this,
        new Texture(this.backgroundTexture, 0, 0, 256, 256),
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
    TileEntityToolbox toolbox = this.container.getToolbox();

    if (toolbox != null && !toolbox.isInvalid()) {
      this.guiContainerElementAdd(new GuiElementToolboxSide(
          this,
          toolbox,
          toolbox.getTextureSide(),
          -70,
          0
      ));
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    super.mouseClicked(mouseX, mouseY, mouseButton);

    if (mouseButton != 0) {
      return;
    }

    List<TileEntityWorktableBase> actualJoinedTables = this.currentWorktable.getJoinedTables(new ArrayList<>());
    List<TileEntityWorktableBase> joinedTables = GuiContainerWorktable.getJoinedTableOffsetView(
        actualJoinedTables,
        this.currentWorktable.getGuiTabOffset(),
        this.currentWorktable.getMaximumDisplayedTabCount()
    );

    int yMin = (this.height - this.ySize) / 2 - TAB_HEIGHT;
    int yMax = yMin + TAB_HEIGHT;

    for (int i = 0; i < joinedTables.size(); i++) {
      int xMin = this.guiLeft + TAB_ITEM_HORZONTAL_OFFSET + (TAB_WIDTH + TAB_SPACING) * i;
      int xMax = xMin + TAB_WIDTH;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        TileEntityWorktableBase table = joinedTables.get(i);
        BlockPos pos = table.getPos();
        ModuleWorktables.PACKET_SERVICE.sendToServer(new SPacketWorktableTab(
            pos.getX(),
            pos.getY(),
            pos.getZ()
        ));
        Minecraft.getMinecraft()
            .getSoundHandler()
            .playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }

    int maximumDisplayedTabCount = this.currentWorktable.getMaximumDisplayedTabCount();

    if (this.currentWorktable.getGuiTabOffset() > 0) {
      // check for left button click
      int xMin = this.guiLeft + TAB_LEFT_OFFSET + TAB_ITEM_HORZONTAL_OFFSET - 18;
      int xMax = xMin + 8;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        int tabOffset = this.currentWorktable.getGuiTabOffset() - maximumDisplayedTabCount;
        this.currentWorktable.setGuiTabOffset(Math.max(0, tabOffset));
        Minecraft.getMinecraft()
            .getSoundHandler()
            .playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }

    if (this.currentWorktable.getGuiTabOffset() + maximumDisplayedTabCount < actualJoinedTables.size()) {
      // check for right button click
      int xMin = this.guiLeft + this.getXSize() - 12;
      int xMax = xMin + 8;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        this.currentWorktable.setGuiTabOffset(Math.min(
            actualJoinedTables.size() - maximumDisplayedTabCount,
            this.currentWorktable.getGuiTabOffset() + maximumDisplayedTabCount
        ));
        Minecraft.getMinecraft()
            .getSoundHandler()
            .playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(this.backgroundTexture);
    int x = this.guiLeft;
    int y = (this.height - this.ySize) / 2;

    int tabX = x + TAB_LEFT_OFFSET;
    int tabY = y - TAB_HEIGHT;

    List<TileEntityWorktableBase> actualJoinedTables = this.currentWorktable.getJoinedTables(new ArrayList<>());
    List<TileEntityWorktableBase> joinedTables = GuiContainerWorktable.getJoinedTableOffsetView(
        actualJoinedTables,
        this.currentWorktable.getGuiTabOffset(),
        this.currentWorktable.getMaximumDisplayedTabCount()
    );

    this.mc.getTextureManager().bindTexture(TEXTURE_GUI_ELEMENTS);

    // draw arrows

    if (this.currentWorktable.getGuiTabOffset() > 0) {
      // draw left button
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + TAB_LEFT_OFFSET + TAB_ITEM_HORZONTAL_OFFSET - 18,
          tabY,
          TAB_WIDTH,
          this.currentWorktable.getWorktableGuiTabTextureYOffset() * TAB_HEIGHT,
          8,
          TAB_HEIGHT,
          GUI_ELEMENTS_TEXTURE_WIDTH,
          GUI_ELEMENTS_TEXTURE_HEIGHT
      );
    }

    int maximumDisplayedTabCount = this.currentWorktable.getMaximumDisplayedTabCount();

    if (this.currentWorktable.getGuiTabOffset() + maximumDisplayedTabCount < actualJoinedTables.size()) {
      // draw right button
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + this.getXSize() - 12,
          tabY,
          TAB_WIDTH + 8,
          this.currentWorktable.getWorktableGuiTabTextureYOffset() * TAB_HEIGHT,
          8,
          TAB_HEIGHT,
          GUI_ELEMENTS_TEXTURE_WIDTH,
          GUI_ELEMENTS_TEXTURE_HEIGHT
      );
    }

    // draw tabs

    for (TileEntityWorktableBase joinedTable : joinedTables) {
      int textureY = joinedTable.getWorktableGuiTabTextureYOffset() * TAB_HEIGHT;

      if (joinedTable == this.currentWorktable) {
        //this.drawTexturedModalRect(tabX, tabY + TAB_CURRENT_OFFSET, 0, textureY, TAB_WIDTH, TAB_HEIGHT);
        Gui.drawModalRectWithCustomSizedTexture(
            tabX,
            tabY + TAB_CURRENT_OFFSET,
            0,
            textureY,
            TAB_WIDTH,
            TAB_HEIGHT,
            GUI_ELEMENTS_TEXTURE_WIDTH,
            GUI_ELEMENTS_TEXTURE_HEIGHT
        );

      } else {
        //this.drawTexturedModalRect(tabX, tabY, 0, textureY, TAB_WIDTH, 21);
        Gui.drawModalRectWithCustomSizedTexture(
            tabX,
            tabY,
            0,
            textureY,
            TAB_WIDTH,
            TAB_HEIGHT,
            GUI_ELEMENTS_TEXTURE_WIDTH,
            GUI_ELEMENTS_TEXTURE_HEIGHT
        );
      }

      tabX += TAB_WIDTH + TAB_SPACING;
    }

    // draw tab icons
    tabX = x + TAB_LEFT_OFFSET + TAB_ITEM_HORZONTAL_OFFSET;
    tabY = y - TAB_HEIGHT + TAB_ITEM_VERTICAL_OFFSET;

    RenderHelper.enableGUIStandardItemLighting();

    for (TileEntityWorktableBase joinedTable : joinedTables) {
      IBlockState blockState = joinedTable.getWorld().getBlockState(joinedTable.getPos());
      blockState = blockState.getBlock().getActualState(blockState, this.mc.world, joinedTable.getPos());
      ItemStack itemStack = joinedTable.getItemStackForTabDisplay(blockState);

      if (joinedTable == this.currentWorktable) {
        this.itemRender.renderItemAndEffectIntoGUI(itemStack, tabX, tabY + TAB_CURRENT_OFFSET);

      } else {
        this.itemRender.renderItemAndEffectIntoGUI(itemStack, tabX, tabY);
      }

      tabX += TAB_WIDTH + TAB_SPACING;
    }

  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    this.drawString(this.titleKey, 8, 6);
    this.drawString("container.inventory", 8, this.ySize - 96 + 3);

    super.drawGuiContainerForegroundLayer(mouseX, mouseY);

  }

  public static List<TileEntityWorktableBase> getJoinedTableOffsetView(
      List<TileEntityWorktableBase> list,
      int offset,
      int maximumDisplayedTabCount
  ) {

    List<TileEntityWorktableBase> result = new ArrayList<>(maximumDisplayedTabCount);

    if (offset + maximumDisplayedTabCount > list.size()) {
      offset = list.size() - maximumDisplayedTabCount;
    }

    if (offset < 0) {
      offset = 0;
    }

    int limit = Math.min(list.size(), offset + maximumDisplayedTabCount);

    for (int i = offset; i < limit; i++) {
      result.add(list.get(i));
    }

    return result;
  }

  @Override
  public void drawString(String translateKey, int x, int y) {

    String displayText = I18n.format(translateKey);

    FontRenderer fontRenderer = this.fontRenderer;

    if (this.currentWorktable instanceof TileEntityWorktableMage) {
      fontRenderer = this.mc.standardGalacticFontRenderer;
    }

    fontRenderer.drawString(displayText, x + 0, y + 1, this.textShadowColor);
    fontRenderer.drawString(displayText, x + 1, y + 1, this.textShadowColor);
    fontRenderer.drawString(displayText, x + 1, y - 1, this.textShadowColor);
    fontRenderer.drawString(displayText, x + 1, y + 0, this.textShadowColor);

    fontRenderer.drawString(displayText, x - 0, y - 1, this.textShadowColor);
    fontRenderer.drawString(displayText, x - 1, y - 1, this.textShadowColor);
    fontRenderer.drawString(displayText, x - 1, y + 1, this.textShadowColor);
    fontRenderer.drawString(displayText, x - 1, y - 0, this.textShadowColor);

    fontRenderer.drawString(displayText, x, y, Color.BLACK.getRGB());
  }

}
