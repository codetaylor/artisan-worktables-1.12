package com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi;

import com.codetaylor.mc.athenaeum.gui.Texture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.ItemStackHandler;

public interface ITileEntityDesigner {

  ItemStackHandler getPatternStackHandler();

  boolean isInvalid();

  boolean canPlayerUse(EntityPlayer player);

  Texture getTexturePatternSide();
}
