package com.codetaylor.mc.artisanworktables.proxy;

import net.minecraft.client.Minecraft;

public class ClientProxy
    implements IProxy {

  @Override
  public boolean isIntegratedServerRunning() {

    return Minecraft.getMinecraft().isIntegratedServerRunning();
  }
}
