package com.codetaylor.mc.artisanworktables.modules.worktables.integration.gamestages;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingContext;
import com.codetaylor.mc.artisanworktables.api.recipe.IMatchRequirementContext;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameStagesMatchRequirementContext
    implements IMatchRequirementContext {

  private Set<String> unlockedStages;

  @Override
  public void initialize(ICraftingContext craftingContext) {

    Collection<String> unlockedStages = GameStagesHelper.getUnlockedStages(craftingContext.getPlayer());
    this.setUnlockedStages(unlockedStages);
  }

  /* package */ Set<String> getUnlockedStages() {

    return this.unlockedStages;
  }

  /* package */ void setUnlockedStages(Collection<String> unlockedStages) {

    this.unlockedStages = Collections.unmodifiableSet(new HashSet<>(unlockedStages));
  }
}
