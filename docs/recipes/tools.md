# Recipes with Tools

This mod was originally created to allow recipes that require tools to craft. Over time it has evolved and now has the ability to support recipes that require up to three different tools to craft.

Adding tool requirements to a recipe is optional. Recipes will work just fine if you don't add a tool.

## Add Tools to Recipes

To add a tool to a recipe, call the following method on the builder:

```java
RecipeBuilder addTool(IIngredient tool, int damage);
```

The `damage` parameter in this method represents the amount of damage applied to a tool during the craft operation.

Here is an example:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .addTool(<ore:artisansHammer>, 10)
  .create();
```

Multiple tools, up to three in fact, can be used in a recipe.

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .addTool(<ore:artisansHammer>, 10)
  .addTool(<ore:artisansAthame>, 5)
  .addTool(<ore:artisansQuill>, 13)
  .create();
```

You might notice that we're using a ore dictionary entries for the tools in these examples. Artisan Worktables provides two different types of ore dictionary groups for its tools: groups by tool type and groups by tool material.

## OreDict Tool Groups

The complete list of tool type groups is as follows:

```
<ore:artisansCutters>
<ore:artisansHammer>
<ore:artisansFramingHammer>
<ore:artisansHandsaw>
<ore:artisansDriver>
<ore:artisansSpanner>
<ore:artisansGemcutter>
<ore:artisansPliers>
<ore:artisansAthame>
<ore:artisansGrimoire>
<ore:artisansChisel>
<ore:artisansTrowel>
<ore:artisansNeedle>
<ore:artisansShears>
<ore:artisansBeaker>
<ore:artisansBurner>
<ore:artisansQuill>
<ore:artisansCompass>
```

## OreDict Tool Material Groups

Tool material groups are prefixed with `artisansTool` and end with the name of the material, like `<ore:artisansToolWood>` and `<ore:artisansToolStone>`.

For a complete list of material groups, take a look at the following file located in the `config/artisanworktables/` folder:

```
artisanworktables.module.Tools.Materials.Generated.json
```