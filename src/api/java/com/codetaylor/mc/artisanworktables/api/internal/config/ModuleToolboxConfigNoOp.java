package com.codetaylor.mc.artisanworktables.api.internal.config;

public class ModuleToolboxConfigNoOp
    implements IModuleToolboxConfig {

  public static final IModuleToolboxConfig INSTANCE = new ModuleToolboxConfigNoOp();

  @Override
  public boolean enableModule() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isToolboxEnabled() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isToolboxRestrictedToToolsOnly() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean doesToolboxKeepContentsWhenBroken() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isMechanicalToolboxEnabled() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isMechanicalToolboxRestrictedToToolsOnly() {

    throw new UnsupportedOperationException();
  }

  @Override
  public boolean doesMechanicalToolboxKeepContentsWhenBroken() {

    throw new UnsupportedOperationException();
  }
}
