package com.codetaylor.mc.artisanworktables.modules.toolbox.tile;

import com.codetaylor.mc.artisanworktables.ReferenceTexture;
import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.ModuleToolboxConfig;
import com.codetaylor.mc.athenaeum.gui.Texture;

public class TileEntityMechanicalToolbox
    extends TileEntityToolbox {

  @Override
  protected boolean restrictToToolsOnly() {

    return ModuleToolboxConfig.MECHANICAL_TOOLBOX.RESTRICT_TO_TOOLS_ONLY;
  }

  @Override
  protected String getGuiContainerTitleKey() {

    return ModuleToolbox.Lang.MECHANICAL_TOOLBOX_TITLE;
  }

  @Override
  public Texture getTexture() {

    return ReferenceTexture.TEXTURE_TOOLBOX_MECHANICAL;
  }

  @Override
  public Texture getTextureSide() {

    return ReferenceTexture.TEXTURE_TOOLBOX_MECHANICAL_SIDE;
  }
}
