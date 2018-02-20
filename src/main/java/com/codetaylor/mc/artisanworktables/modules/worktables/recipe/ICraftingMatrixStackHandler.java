package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import net.minecraftforge.items.IItemHandler;

public interface ICraftingMatrixStackHandler
    extends IItemHandler {

  int getWidth();

  int getHeight();
}
