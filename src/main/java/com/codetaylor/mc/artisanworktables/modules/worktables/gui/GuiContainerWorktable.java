package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.util.ResourceLocation;
import yalter.mousetweaks.api.MouseTweaksDisableWheelTweak;

@MouseTweaksDisableWheelTweak
public class GuiContainerWorktable
    extends com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerBase {

  public GuiContainerWorktable(
      ContainerWorktable container,
      ResourceLocation backgroundTexture,
      String titleKey,
      int textShadowColor,
      TileEntityBase currentWorktable,
      int width,
      int height
  ) {

    super(container, backgroundTexture, titleKey, textShadowColor, currentWorktable, width, height);
  }

  @Override
  protected int getInventoryTitleOffsetY() {

    return 0;
  }

}
