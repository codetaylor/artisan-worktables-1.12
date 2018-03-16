package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import java.util.Collection;

public interface IGameStageMatcher {

  IGameStageMatcher TRUE = unlockedStages -> true;

  boolean matches(Collection<String> unlockedStages);
}
