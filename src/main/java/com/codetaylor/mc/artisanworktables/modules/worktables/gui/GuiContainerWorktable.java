package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.util.ResourceLocation;
import yalter.mousetweaks.api.MouseTweaksDisableWheelTweak;

@MouseTweaksDisableWheelTweak
public class GuiContainerWorktable
    extends GuiContainerBase {

  public GuiContainerWorktable(
      Container container,
      ResourceLocation backgroundTexture,
      String titleKey,
      TileEntityBase tileEntity,
      int width,
      int height
  ) {

    super(container, backgroundTexture, titleKey, tileEntity, width, height);
  }
}
