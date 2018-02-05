package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import crafttweaker.api.item.IIngredient;

import java.util.Collection;

public interface ISecondaryIngredientMatcher {

  ISecondaryIngredientMatcher FALSE = requiredIngredients -> false;

  boolean matches(Collection<IIngredient> requiredIngredients);
}
