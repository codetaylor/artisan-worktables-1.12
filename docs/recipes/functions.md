# Recipe Functions

Artisan Worktables' recipes support CraftTweaker recipe functions.

```java
RecipeBuilder setRecipeFunction(IRecipeFunction recipeFunction);
RecipeBuilder setRecipeAction(IRecipeAction recipeAction);
```

For more information on recipe functions, please refer to the [official CraftTweaker documentation](http://crafttweaker.readthedocs.io/en/latest/#).

!!! note
    JEI will not display the results of the provided recipe function. It is not practical to try and determine all possible outputs of any given recipe function, therefore, JEI will only display the normal results of a recipe.

!!! note
    The `IItemStack` returned from a provided `IRecipeFunction` will be overridden by the selected weighted output if more than one output is provided for a recipe.