# GameStages

Artisan Worktables integrates with the [Game Stages mod](https://minecraft.curseforge.com/projects/game-stages) to restrict recipe creation based on a player's unlocked game stages.

## Game Stage Rules

If you want to require a player to have certain game stages unlocked to craft a recipe, call the following method on the builder:

```java
RecipeBuilder requireGameStages(String require, String[] stages);
```

Allowed values for the `require` parameter are `ALL` and `ANY`. If set to `ALL`, the player is required to have unlocked all of the game stages provided. If set to `ANY` the player is required to have unlocked at least one of the game stages provided.

If you want to require a player to *not* have certain game stages unlocked, call the following method on the builder:

```java
RecipeBuilder excludeGameStages(String[] stages);
```

If a player has unlocked any of the game stages provided, the recipe can't be crafted.

## Examples

For example, the following recipe requires that the player has unlocked both game stages `one` and `two` to craft the recipe.

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .requireGameStages("ALL", ["one", "two"])
  .create();
```

To present another example, the following recipe requires that the player has unlocked either game stage `one` or `two`, but not `three` to craft the recipe:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .requireGameStages("ANY", ["one", "two"])
  .excludeGameStages(["three"])
  .create();
```
