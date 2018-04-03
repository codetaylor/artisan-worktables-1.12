package com.codetaylor.mc.artisanworktables.modules.worktables.gui.slot;

import net.minecraftforge.items.IItemHandler;

public class ToolboxSideSlot
    extends PredicateEnabledSlot {

  protected final int originX;
  protected final int originY;

  public ToolboxSideSlot(
      Predicate predicate,
      IItemHandler itemHandler,
      int index,
      int originX,
      int originY
  ) {

    super(predicate, itemHandler, index, originX, originY);
    this.originX = originX;
    this.originY = originY;
  }

  public void move(int positionX, int positionY) {

    this.xPos = positionX;
    this.yPos = positionY;
  }

  public void moveToOrigin() {

    this.xPos = this.originX;
    this.yPos = this.originY;
  }
}
