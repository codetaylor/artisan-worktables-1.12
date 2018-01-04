package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import net.minecraftforge.items.ItemStackHandler;

public class CraftingMatrixStackHandler
    extends ItemStackHandler {

  private int width;
  private int height;

  public CraftingMatrixStackHandler(
      int width,
      int height
  ) {

    super(width * height);
    this.width = width;
    this.height = height;
  }

  public int getWidth() {

    return this.width;
  }

  public int getHeight() {

    return this.height;
  }
}
