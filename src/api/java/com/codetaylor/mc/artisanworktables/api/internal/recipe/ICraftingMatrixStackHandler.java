package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface ICraftingMatrixStackHandler
    extends IItemHandler,
    IItemHandlerModifiable {

  int getWidth();

  int getHeight();
}
