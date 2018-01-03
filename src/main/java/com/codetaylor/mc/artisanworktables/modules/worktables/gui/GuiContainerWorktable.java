package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.network.SPacketWorktableTab;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityWorktableBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiContainerWorktable
    extends GuiContainer {

  private static final int TAB_WIDTH = 24;
  private static final int TAB_SPACING = 2;
  private static final int TAB_CURRENT_OFFSET = 1;
  private static final int TAB_HEIGHT = 21;
  private static final int TAB_LEFT_OFFSET = 4;
  private static final int TAB_ITEM_HORZONTAL_OFFSET = 4;
  private static final int TAB_ITEM_VERTICAL_OFFSET = 4;

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

    List<TileEntityWorktableBase> joinedTables = this.currentWorktable.getJoinedTables();

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

    for (TileEntityWorktableBase joinedTable : this.currentWorktable.getJoinedTables()) {
      int textureY = joinedTable.getGuiTabTextureYOffset() * TAB_HEIGHT;

      if (joinedTable == this.currentWorktable) {
        this.drawTexturedModalRect(tabX, tabY + TAB_CURRENT_OFFSET, this.xSize, textureY, TAB_WIDTH, TAB_HEIGHT);

      } else {
        this.drawTexturedModalRect(tabX, tabY, this.xSize, textureY, TAB_WIDTH, 21);
      }

      tabX += TAB_WIDTH + TAB_SPACING;
    }

    tabX = x + TAB_LEFT_OFFSET + TAB_ITEM_HORZONTAL_OFFSET;
    tabY = y - TAB_HEIGHT + TAB_ITEM_VERTICAL_OFFSET;

    RenderHelper.enableGUIStandardItemLighting();

    for (TileEntityWorktableBase joinedTable : this.currentWorktable.getJoinedTables()) {
      IBlockState blockState = joinedTable.getWorld().getBlockState(joinedTable.getPos());
      Block block = blockState.getBlock();
      Item item = Item.getItemFromBlock(block);
      ItemStack itemStack = new ItemStack(item, 1, block.getMetaFromState(blockState));

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

    this.drawString(this.titleKey, 8, 5);
    this.drawString("container.inventory", 8, this.ySize - 96 + 2);
  }

  private void drawString(String translateKey, int x, int y) {

    String displayText = I18n.format(translateKey);

    this.fontRenderer.drawString(displayText, x + 0, y + 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x + 1, y + 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x + 1, y - 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x + 1, y + 0, this.textShadowColor);

    this.fontRenderer.drawString(displayText, x - 0, y - 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x - 1, y - 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x - 1, y + 1, this.textShadowColor);
    this.fontRenderer.drawString(displayText, x - 1, y - 0, this.textShadowColor);

    this.fontRenderer.drawString(displayText, x, y, Color.BLACK.getRGB());
  }

}
