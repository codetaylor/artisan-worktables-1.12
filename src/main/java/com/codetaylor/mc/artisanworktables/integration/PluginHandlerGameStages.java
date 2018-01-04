package com.codetaylor.mc.artisanworktables.integration;

import com.codetaylor.mc.athenaeum.integration.IIntegrationPluginHandler;

public class PluginHandlerGameStages
    implements IIntegrationPluginHandler {

  @Override
  public void execute(String plugin) throws Exception {

    Class.forName(plugin).newInstance();
  }
}
