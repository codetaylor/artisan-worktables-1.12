package com.codetaylor.mc.artisanworktables.modules.worktables.integration.gamestages;

import net.darkhax.gamestages.data.GameStageSaveHandler;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;

public class GameStagesProxyClient
    extends GameStagesProxy {

  @Override
  public Collection<String> getStages(EntityPlayer player) {

    return GameStageSaveHandler.clientData.getStages();
  }
}
