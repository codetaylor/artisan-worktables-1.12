package com.codetaylor.mc.artisanworktables.modules.worktables.gui;

import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "yalter.mousetweaks.api.MouseTweaksDisableWheelTweak", modid = "mousetweaks")
public class GuiContainerWorkstation
    extends GuiContainerBase {

  public GuiContainerWorkstation(
      Container container,
      ResourceLocation backgroundTexture,
      String titleKey,
      int textShadowColor,
      TileEntityBase tileEntity,
      int width,
      int height
  ) {

    super(container, backgroundTexture, titleKey, textShadowColor, tileEntity, width, height);
  }
}
