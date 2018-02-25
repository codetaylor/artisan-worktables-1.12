# Copying Recipes

The Artisan Worktables builder API allows you to copy recipes in a reasonably flexible manner. Copying recipes, however, is a little more involved than simply calling a method on the builder, but not by much.

## Import

First, you'll need an extra import:

```js
import mods.artisanworktables.builder.Copy;
```

This new import, `Copy`, has three static methods to copy recipes by recipe name, recipe, and recipe output:

```java
// copies a recipe by recipe name
Copy byName(String recipeName);

// copies the provided recipe
Copy byRecipe(ICraftingRecipe recipe);

// copies all recipes that match the provided recipe ingredients
Copy byOutput(IIngredient[] outputs);
```

By default, the copy methods above will copy both the input and output of the provided recipe(s). Calling `byOutput(IIngredient[])` may result in multiple recipes being copied and created if multiple recipes match the outputs provided.

Each of these methods returns a `Copy` object that has methods that you can chain to further define how you want to copy the recipe(s) indicated.

```java
// prevents copying a recipe's input
Copy excludeInput();

// prevents copying a recipe's output
Copy excludeOutput();

// replaces a copied recipe's output and uses the recipe's output quantity
Copy replaceOutput(IItemStack replacement);
```

You'll then pass this configured `Copy` object into this recipe builder method:

```java
IRecipeBuilder setCopy(Copy copy);
```

This sounds complicated, but it's really not. Here are some examples to better illustrate the syntax.

## Examples

### By Name

This recipe will copy both the input and output of the recipe with the name `minecraft:furnace` and add a tool requirement:

```js
import mods.artisanworktables.builder.RecipeBuilder;
import mods.artisanworktables.builder.Copy;

RecipeBuilder.get("basic")
  .setCopy(Copy.byName("minecraft:furnace"))
  .addTool(<ore:artisansHammer>, 10)
  .create();
```

### By Recipe

This recipe will iterate through all recipes for `<ore:ingotGold>`, copy each recipe's input, add a tool requirement, and replace each recipe's output with a `<minecraft:diamond>` with respect to the original recipe's output quantity.

```js
import mods.artisanworktables.builder.RecipeBuilder;
import mods.artisanworktables.builder.Copy;

val builder = RecipeBuilder.get("basic");

for recipe in recipes.getRecipesFor(<ore:ingotGold>) {
    builder
        .setCopy(Copy.byRecipe(recipe).replaceOutput(<minecraft:diamond>))
        .addTool(<ore:artisansHammer>, 10)
        .create();
}
```

!!! error "CraftTweaker Issues"
    As of CraftTweaker version 4.1.4.432 there are still issues when using: `recipes.getRecipesFor(IIngredient)`

### By Recipe Output

This recipe will copy only the input of all recipes that have an output of `<ore:ingotIron>`, add a tool requirement, and add an output of `<minecraft:string>`.

```js
import mods.artisanworktables.builder.RecipeBuilder;
import mods.artisanworktables.builder.Copy;

RecipeBuilder.get("basic")
  .setCopy(Copy.byOutput([<ore:ingotIron>, <ore:ingotGold>]).excludeOutput())
  .addTool(<ore:artisansHammer>, 10)
  .addOutput(<minecraft:string>)
  .create();
```
