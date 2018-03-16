package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import java.util.Collection;

public interface ISecondaryIngredientMatcher {

  ISecondaryIngredientMatcher FALSE = requiredIngredients -> false;

  boolean matches(Collection<IArtisanIngredient> requiredIngredients);
}
