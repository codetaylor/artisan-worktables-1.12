# Secondary Ingredients

Secondary ingredients are items that are required by a recipe and consumed when crafting, but aren't placed in the crafting grid. Instead, these items are placed in the secondary ingredient storage located directly above the player's inventory in the crafting GUI.

`Workstation` and `Workshop` tier table recipes support up to nine secondary ingredients.

## Add Secondary Ingredients to Recipes

To add secondary ingredients to a recipe, call the following method on a builder:

```java
RecipeBuilder setSecondaryIngredients(IIngredient[] secondaryIngredients);
```

This recipe requires `8` `<minecraft:gravel>` and one `<minecraft:string>` in the secondary ingredient storage area to craft:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .setSecondaryIngredients([<minecraft:gravel> * 8, <minecraft:string>])
  .addOutput(<minecraft:cobblestone>)
  .create();
```
