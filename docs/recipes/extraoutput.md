# Extra Chance Output

Each recipe can define up to three extra outputs with a chance of being created when the craft operation is performed. When these extra outputs are created, they are placed into one of the three extra output slots found near the main result slot of the GUI. If these slots are full and the extra output can't be placed into one of these storage slots, it will pop out into the world on top of the table.

## Add Extra Output to Recipes

```java
RecipeBuilder setExtraOutputOne(IItemStack output, float chance);
RecipeBuilder setExtraOutputTwo(IItemStack output, float chance);
RecipeBuilder setExtraOutputThree(IItemStack output, float chance);
```

In these methods, `chance` is a float in the range `[0,1]`.

For example, the following recipe has a `75%` chance of dropping a `<minecraft:string>` and a `25%` chance of dropping a `<minecraft:diamond>`:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .setExtraOutputOne(<minecraft:string>, 0.75)
  .setExtraOutputTwo(<minecraft:diamond>, 0.25)
  .create();
```