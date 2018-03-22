# GameStages

Artisan Worktables provides integration with the [Game Stages mod](https://minecraft.curseforge.com/projects/game-stages) to restrict recipe creation based on a player's unlocked game stages.

## Import

```js
import mods.artisanworktables.integration.requirement.GameStages;
```

## Requirement Builder

If you want to require a player to have certain game stages unlocked to craft a recipe, call the following methods on the builder:

```java
GameStagesRequirementBuilder allOf(String[] stages);
GameStagesRequirementBuilder anyOf(String[] stages);
```

The player will be required to have unlocked all of the stages defined in the method `all` and at least one of the stages defined in the method `any` to craft the recipe.

If you want to require a player to *not* have certain game stages unlocked, call the following method on the builder:

```java
GameStagesRequirementBuilder exclude(String[] stages);
```

If a player has unlocked any of the game stages provided in the method `exclude`, the recipe can't be crafted.

## Examples

For example, the following recipe requires that the player has unlocked both game stages `one` and `two` to craft the recipe.

```js
import mods.artisanworktables.builder.RecipeBuilder;
import mods.artisanworktables.integration.requirement.GameStages;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .addRequirement(GameStages.allOf(["one", "two"]))
  .create();
```

To present another example, the following recipe requires that the player has unlocked either game stage `one` or `two`, but not `three` to craft the recipe:

```js
import mods.artisanworktables.builder.RecipeBuilder;
import mods.artisanworktables.integration.requirement.GameStages;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .addRequirement(GameStages.anyOf(["one", "two"]).exclude(["three"]))
  .create();
```
