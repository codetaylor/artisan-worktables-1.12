# Weighted Primary Output

A recipe's primary output, the item that is shown in the main result slot in the GUI, can be chosen from a weighted pool of items when the item is crafted. When multiple item outputs are added to a recipe, the first item added will be the item shown to the player in the crafting GUI. 

The `addOutput` method can be called multiple times and accepts an optional weight parameter. By default this parameter is set to `1`.

```java
RecipeBuilder addOutput(IItemStack output, @Optional int weight);
```

## Equal Weight

In this example, each output item is added with the same weight: `1`. This means that for two items, each item has a 50% chance of being crafted.

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .addOutput(<minecraft:diamond>)
  .create();
```

## Varied Weight

In this example, the item weights are different. The `<minecraft:cobblestone>` has a 90% chance to be crafted, while the `<minecraft:diamond>` has a 10% chance to be crafted.

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>, 90)
  .addOutput(<minecraft:diamond>, 10)
  .create();
```
