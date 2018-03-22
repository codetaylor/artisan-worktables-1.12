package com.codetaylor.mc.artisanworktables.api;

import com.codetaylor.mc.artisanworktables.api.internal.config.*;

public class ArtisanConfig {

  // --------------------------------------------------------------------------
  // - Module Configs

  public static final IModuleWorktablesConfig MODULE_WORKTABLES_CONFIG = ModuleWorktablesConfigNoOp.INSTANCE;

  public static final IModuleToolsConfig MODULE_TOOLS_CONFIG = ModuleToolsConfigNoOp.INSTANCE;

  public static final IModuleToolboxConfig MODULE_TOOLBOX_CONFIG = ModuleToolboxConfigNoOp.INSTANCE;

  private ArtisanConfig() {
    //
  }
}
