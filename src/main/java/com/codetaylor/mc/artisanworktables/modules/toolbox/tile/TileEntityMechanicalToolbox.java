package com.codetaylor.mc.artisanworktables.modules.toolbox.tile;

import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolboxConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import net.minecraft.util.ResourceLocation;

public class TileEntityMechanicalToolbox
    extends TileEntityToolbox {

  private static final ResourceLocation TEXTURE = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      "textures/gui/mechanical_toolbox.png"
  );

  @Override
  protected boolean restrictToToolsOnly() {

    return ModuleToolboxConfig.MECHANICAL_TOOLBOX.RESTRICT_TO_TOOLS_ONLY;
  }

  @Override
  protected String getGuiContainerTitleKey() {

    return ModuleToolbox.Lang.MECHANICAL_TOOLBOX_TITLE;
  }

  @Override
  public ResourceLocation getGuiTexture() {

    return TEXTURE;
  }
}
