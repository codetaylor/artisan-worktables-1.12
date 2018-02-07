package com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi;

import com.codetaylor.mc.artisanworktables.modules.worktables.block.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumTier;

public abstract class TileEntityTypedBase
    extends TileEntityFluidBase {

  protected EnumType type;

  public TileEntityTypedBase(int width, int height, int fluidCapacity, EnumType type) {

    super(width, height, fluidCapacity);
    this.type = type;
  }

  @Override
  protected String getWorktableName() {

    return this.type.getName();
  }

  @Override
  protected int getGuiTextShadowColor() {

    return this.type.getTextOutlineColor();
  }

  @Override
  public boolean canHandleJEIRecipeTransfer(
      String name,
      EnumTier tier
  ) {

    return this.type.getName().equals(name) && tier.getId() <= this.getTier().getId();
  }

  @Override
  public int getWorktableGuiTabTextureYOffset() {

    return this.type.getGuiTabTextureOffsetY();
  }

  public EnumType getType() {

    return this.type;
  }

  protected abstract EnumTier getTier();
}
