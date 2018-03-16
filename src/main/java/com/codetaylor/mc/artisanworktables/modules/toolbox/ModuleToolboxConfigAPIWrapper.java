package com.codetaylor.mc.artisanworktables.modules.toolbox;

import com.codetaylor.mc.artisanworktables.api.internal.config.IModuleToolboxConfig;

public class ModuleToolboxConfigAPIWrapper
    implements IModuleToolboxConfig {

  @Override
  public boolean enableModule() {

    return ModuleToolboxConfig.ENABLE_MODULE;
  }

  @Override
  public boolean isToolboxEnabled() {

    return ModuleToolboxConfig.TOOLBOX.ENABLED;
  }

  @Override
  public boolean isToolboxRestrictedToToolsOnly() {

    return ModuleToolboxConfig.TOOLBOX.RESTRICT_TO_TOOLS_ONLY;
  }

  @Override
  public boolean doesToolboxKeepContentsWhenBroken() {

    return ModuleToolboxConfig.TOOLBOX.KEEP_CONTENTS_WHEN_BROKEN;
  }

  @Override
  public boolean isMechanicalToolboxEnabled() {

    return ModuleToolboxConfig.MECHANICAL_TOOLBOX.ENABLED;
  }

  @Override
  public boolean isMechanicalToolboxRestrictedToToolsOnly() {

    return ModuleToolboxConfig.MECHANICAL_TOOLBOX.RESTRICT_TO_TOOLS_ONLY;
  }

  @Override
  public boolean doesMechanicalToolboxKeepContentsWhenBroken() {

    return ModuleToolboxConfig.MECHANICAL_TOOLBOX.KEEP_CONTENTS_WHEN_BROKEN;
  }
}
