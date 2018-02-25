# Recipes with Fluids

Each crafting table in Artisan Worktables has a fluid tank and recipes can require a fluid to craft. Fluids can be inserted and extracted using buckets or any other mechanism that interacts using the Forge fluid capability.

## Add Fluids to Recipes

To add a fluid to a recipe, call the following method on a builder:

```java
IRecipeBuilder setFluid(ILiquidStack fluidIngredient);
```

This example will consume `250` millibuckets of `<liquid:water>` to craft:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .setFluid(<liquid:water> * 250)
  .addOutput(<minecraft:cobblestone>)
  .create();
```
