package com.codetaylor.mc.artisanworktables.api.internal.config;

public interface IModuleToolboxConfig {

  boolean enableModule();

  boolean isToolboxEnabled();

  boolean isToolboxRestrictedToToolsOnly();

  boolean doesToolboxKeepContentsWhenBroken();

  boolean isMechanicalToolboxEnabled();

  boolean isMechanicalToolboxRestrictedToToolsOnly();

  boolean doesMechanicalToolboxKeepContentsWhenBroken();

}
