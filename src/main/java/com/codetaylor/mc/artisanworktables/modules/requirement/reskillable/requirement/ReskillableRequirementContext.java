package com.codetaylor.mc.artisanworktables.modules.requirement.reskillable.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingContext;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementContext;

public class ReskillableRequirementContext
    implements IMatchRequirementContext {

  private PlayerData playerData;

  @Override
  public void initialize(ICraftingContext craftingContext) {

    this.playerData = PlayerDataHandler.get(craftingContext.getPlayer());
  }

  /* package */ PlayerData getPlayerData() {

    return this.playerData;
  }
}
