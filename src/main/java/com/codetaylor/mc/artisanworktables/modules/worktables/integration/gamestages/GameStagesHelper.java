package com.codetaylor.mc.artisanworktables.modules.worktables.integration.gamestages;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.SidedProxy;

import java.util.Collection;

public class GameStagesHelper {

  @SidedProxy(
      serverSide = "com.codetaylor.mc.artisanworktables.modules.worktables.integration.gamestages.GameStagesProxy",
      clientSide = "com.codetaylor.mc.artisanworktables.modules.worktables.integration.gamestages.GameStagesProxyClient",
      modId = ModuleWorktables.MOD_ID
  )
  private static GameStagesProxy gameStagesProxy;

  public static Collection<String> getUnlockedStages(EntityPlayer player) {

    return gameStagesProxy.getStages(player);
  }

  private GameStagesHelper() {
    //
  }

}
