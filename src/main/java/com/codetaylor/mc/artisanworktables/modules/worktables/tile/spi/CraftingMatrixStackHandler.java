package com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi;

import com.codetaylor.mc.artisanworktables.api.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;

public class CraftingMatrixStackHandler
    extends ObservableStackHandler
    implements ICraftingMatrixStackHandler {

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

  @Override
  public int getWidth() {

    return this.width;
  }

  @Override
  public int getHeight() {

    return this.height;
  }
}
