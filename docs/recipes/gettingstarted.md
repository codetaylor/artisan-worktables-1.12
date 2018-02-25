# Getting Started

Artisan Worktables is designed to be used with [CraftTweaker](https://minecraft.curseforge.com/projects/crafttweaker) and its scripting language, ZenScript.

!!! note
    For more information about how to use ZenScript, please visit the [official CraftTweaker documentation](http://crafttweaker.readthedocs.io/en/latest/#).

## Building Recipes

Recipes are created by calling methods on a `RecipeBuilder` object and can be combined in various ways to achieve many unique results.

`RecipeBuilder` objects are retrieved by importing the `RecipeBuilder` and calling `get(String tableName)` for the desired table.

```js
import mods.artisanworktables.builder.RecipeBuilder;

val builder = RecipeBuilder.get("basic");
```

You can reuse builder objects or call `RecipeBuilder.get(String tableName)` each time you need one for a specific table. Calling this method repeatedly won't impact performace because, under the hood, the same builder object is used.

The list of valid table names is: `basic`, `blacksmith`, `carpenter`, `chemist`, `engineer`, `jeweler`, `mage`, `mason`, `scribe`, and `tailor`.

## Using the Builder

The builder object can be stored in a variable and its methods invoked on that variable or those same method calls can be chained back-to-back.

Here is an example recipe created using the builder when it's stored in a variable:

```js
import mods.artisanworktables.builder.RecipeBuilder;

val builder = RecipeBuilder.get("basic");
builder.setShapeless([<minecraft:dirt>]);
builder.addOutput(<minecraft:cobblestone>);
builder.create();
```

Here is the same example recipe using chained method calls:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .create();
```

## Finalizing Recipes

To finalize, or actually create, a recipe, you must call `create()` on the builder object. Doing this will validate and save the recipe, as well as reset the builder, preparing it to create more recipes.

## Method Chaining

Methods can be chained directly after calls to `create()`, however, for better results when debugging, it's more useful to end each method chain after the call to `create()` like this:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .create();

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:string>])
  .addOutput(<minecraft:cobblestone>)
  .create();
```

!!! warning
    For optimal debugging when creating multiple recipes, it is better to end method chains after calls to `create()`.

    While using method chains can be appealing for a variety of reasons, there is a caveat that you should be aware of. When ZenScript compiles the example below, it sees the entire method chain as a single line. This means that when you have multiple recipes created in the same method chain and one of the recipes has an error, the error will indicate that the point of failure is on the line that begins the method chain. This behavior isn't very useful for determining which recipe in the chain has gone bad.

The builder object's methods can be chained repeatedly:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .create()

  .setShapeless([<minecraft:string>])
  .addOutput(<minecraft:cobblestone>)
  .create();
```

