package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.athenaeum.gui.GuiContainerBase;
import com.codetaylor.mc.athenaeum.gui.GuiElementBase;
import net.minecraft.util.math.MathHelper;

public class GuiElementMageHover
    extends GuiElementBase {

  private static final double TWO_PI = Math.PI * 2;
  private static final String[] LETTERS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

  private final ContainerWorktable container;

  public GuiElementMageHover(
      GuiContainerBase guiBase,
      ContainerWorktable container,
      int elementX,
      int elementY
  ) {

    super(guiBase, elementX, elementY, 16, 16);
    this.container = container;
  }

  @Override
  public void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    //
  }

  @Override
  public void drawForegroundLayer(int mouseX, int mouseY) {

    if (this.elementIsMouseInside(mouseX, mouseY)
        && !this.container.inventorySlots.get(0).getStack().isEmpty()) {

      float radius = 21f;
      int count = 12;
      float angleIncrement = (float) (TWO_PI / (float) count);
      float offset = (float) ((System.currentTimeMillis() / 12 * (Math.PI / 180f)) % TWO_PI);

      for (int i = 0; i < count; i++) {
        int x = Math.round(MathHelper.cos(i * angleIncrement + offset) * radius) + this.elementX + this.elementWidth / 2 - 3;
        int y = Math.round(MathHelper.sin(i * angleIncrement + offset) * radius) + this.elementY + this.elementHeight / 2 - 4;
        this.guiBase.drawString(LETTERS[i], x, y);
      }

    }
  }
}
