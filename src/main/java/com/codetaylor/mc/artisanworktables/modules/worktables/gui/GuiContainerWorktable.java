package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.network.SPacketWorktableTab;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityWorktableBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityWorktableMage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import yalter.mousetweaks.api.MouseTweaksDisableWheelTweak;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@MouseTweaksDisableWheelTweak
public class GuiContainerWorktable
    extends GuiContainer {

  public static final int TAB_VIEW_SIZE = 6;

  private static final int TAB_WIDTH = 24;
  private static final int TAB_SPACING = 2;
  private static final int TAB_CURRENT_OFFSET = 1;
  private static final int TAB_HEIGHT = 21;
  private static final int TAB_LEFT_OFFSET = 4;
  private static final int TAB_ITEM_HORZONTAL_OFFSET = 4;
  private static final int TAB_ITEM_VERTICAL_OFFSET = 4;

  private static final ResourceLocation TEXTURE_TABS = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      "textures/gui/tabs.png"
  );

  private static final double TWO_PI = Math.PI * 2;
  private static final String[] LETTERS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

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

    super(container);
    this.container = container;
    this.backgroundTexture = backgroundTexture;
    this.titleKey = titleKey;
    this.textShadowColor = textShadowColor;
    this.currentWorktable = currentWorktable;
    this.xSize = 176;
    this.ySize = 166;
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
    List<TileEntityWorktableBase> joinedTables = this.getJoinedTableOffsetView(
        actualJoinedTables,
        this.currentWorktable.getGuiTabOffset()
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

    if (this.currentWorktable.getGuiTabOffset() > 0) {
      // check for left button click
      int xMin = this.guiLeft + TAB_LEFT_OFFSET + TAB_ITEM_HORZONTAL_OFFSET - 18;
      int xMax = xMin + 8;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        this.currentWorktable.setGuiTabOffset(Math.max(0, this.currentWorktable.getGuiTabOffset() - TAB_VIEW_SIZE));
        Minecraft.getMinecraft()
            .getSoundHandler()
            .playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }

    if (this.currentWorktable.getGuiTabOffset() + TAB_VIEW_SIZE < actualJoinedTables.size()) {
      // check for right button click
      int xMin = this.guiLeft + this.getXSize() - 12;
      int xMax = xMin + 8;

      if (mouseX <= xMax
          && mouseX >= xMin
          && mouseY <= yMax
          && mouseY >= yMin) {
        this.currentWorktable.setGuiTabOffset(Math.min(
            actualJoinedTables.size() - TAB_VIEW_SIZE,
            this.currentWorktable.getGuiTabOffset() + TAB_VIEW_SIZE
        ));
        Minecraft.getMinecraft()
            .getSoundHandler()
            .playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
      }
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(this.backgroundTexture);
    int x = this.guiLeft;
    int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

    int tabX = x + TAB_LEFT_OFFSET;
    int tabY = y - TAB_HEIGHT;

    List<TileEntityWorktableBase> actualJoinedTables = this.currentWorktable.getJoinedTables(new ArrayList<>());
    List<TileEntityWorktableBase> joinedTables = this.getJoinedTableOffsetView(
        actualJoinedTables,
        this.currentWorktable.getGuiTabOffset()
    );

    this.mc.getTextureManager().bindTexture(TEXTURE_TABS);

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
          56,
          168
      );
    }

    if (this.currentWorktable.getGuiTabOffset() + TAB_VIEW_SIZE < actualJoinedTables.size()) {
      // draw right button
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + this.getXSize() - 12,
          tabY,
          TAB_WIDTH + 8,
          this.currentWorktable.getWorktableGuiTabTextureYOffset() * TAB_HEIGHT,
          8,
          TAB_HEIGHT,
          56,
          168
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
            56,
            168
        );

      } else {
        //this.drawTexturedModalRect(tabX, tabY, 0, textureY, TAB_WIDTH, 21);
        Gui.drawModalRectWithCustomSizedTexture(tabX, tabY, 0, textureY, TAB_WIDTH, TAB_HEIGHT, 56, 168);
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

  public static List<TileEntityWorktableBase> getJoinedTableOffsetView(List<TileEntityWorktableBase> list, int offset) {

    List<TileEntityWorktableBase> result = new ArrayList<>(TAB_VIEW_SIZE);

    if (offset + TAB_VIEW_SIZE > list.size()) {
      offset = list.size() - TAB_VIEW_SIZE;
    }

    int limit = Math.min(list.size(), offset + TAB_VIEW_SIZE);

    for (int i = offset; i < limit; i++) {
      result.add(list.get(i));
    }

    return result;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    this.drawString(this.titleKey, 8, 6);
    this.drawString("container.inventory", 8, this.ySize - 96 + 3);

    if (this.currentWorktable instanceof TileEntityWorktableMage
        && mouseX >= this.guiLeft + 108
        && mouseX <= this.guiLeft + 125
        && mouseY >= this.guiTop + 34
        && mouseY <= this.guiTop + 51
        && !this.container.inventorySlots.get(0).getStack().isEmpty()) {

      int originX = 115;
      int originY = 40;
      int radius = 21;
      int count = 12;
      float angleIncrement = (float) (TWO_PI / (float) count);
      float offset = (float) ((System.currentTimeMillis() / 12 * (Math.PI / 180f)) % TWO_PI);

      for (int i = 0; i < count; i++) {
        int x = Math.round(MathHelper.cos(i * angleIncrement + offset) * radius) + originX;
        int y = Math.round(MathHelper.sin(i * angleIncrement + offset) * radius) + originY;
        this.drawString(LETTERS[i], x, y);
      }

    }
  }

  private void drawString(String translateKey, int x, int y) {

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
