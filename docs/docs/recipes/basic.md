# Basic Recipes

All recipes require, at the very least, an input and an output. Artisan Worktable's recipes come in two forms that most of you are probably familiar with: shaped and shapeless.

## Shaped Recipes

Shaped recipes are created by calling the following method on the builder:

```java
IRecipeBuilder setShaped(IIngredient[][] ingredients);
```

This recipe will craft a furnace using the vanilla furnace pattern in any of the basic tables.

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShaped([
    [<minecraft:cobblestone>, <minecraft:cobblestone>, <minecraft:cobblestone>],
    [<minecraft:cobblestone>, null, <minecraft:cobblestone>],
    [<minecraft:cobblestone>, <minecraft:cobblestone>, <minecraft:cobblestone>]])
  .addOutput(<minecraft:furnace>)
  .create();
```

To allow a shaped recipe to be mirrored in the crafting grid, call the following method on the builder:

```java
IRecipeBuilder setMirrored();
```

## Shapeless Recipes

Shapeless recipes are created by calling the following method on the builder:

```java
IRecipeBuilder setShapeless(IIngredient[] ingredients);
```

This recipe will take any item that matches the ore dictionary `logWood` and craft an oak plank in any of the basic tables.

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<ore:logWood>])
  .addOutput(<minecraft:planks>)
  .create();
```