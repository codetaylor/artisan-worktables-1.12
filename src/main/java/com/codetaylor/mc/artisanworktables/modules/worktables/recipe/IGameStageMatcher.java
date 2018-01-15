package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import java.util.Collection;

public interface IGameStageMatcher {

  IGameStageMatcher TRUE = unlockedStages -> true;

  boolean matches(Collection<String> unlockedStages);
}
