# Advanced ZenScript

## Import

```js
import mods.artisanworktables.Worktable;
import mods.artisanworktables.IRecipeBuilder;
```

## Example

The bare minimum:

```js
Worktable.createRecipeBuilder("carpenter")
    .setShaped([
        [<minecraft:planks>],
        [<minecraft:planks>],
        [<minecraft:planks>]])
    .addTool(<ore:carpenters_hammer>, 3)
    .addOutput(<minecraft:planks>)
    .create();
```

More options:

```js
Worktable.createRecipeBuilder("carpenter")
    .setShaped([
        [<minecraft:planks>],
        [<minecraft:planks>],
        [<minecraft:planks>]])
    .setFluid(<liquid:water> * 250)
    .setSecondaryIngredients([<minecraft:clay_ball> * 4, <ore:logWood> * 2])
    .addTool(<ore:carpenters_hammer>, 3)
    .addTool(<ore:carpenters_handsaw>, 1)
    .addOutput(<minecraft:planks> * 10, 10)
    .addOutput(<minecraft:planks:1> * 20, 10)
    .addOutput(<minecraft:planks:2> * 30, 20)
    .addOutput(<minecraft:planks:3> * 50, 30)
    .setExtraOutputOne(<minecraft:dye> * 5, 0.12)
    .setExtraOutputTwo(<minecraft:dye:1> * 5, 0.24)
    .setExtraOutputThree(<minecraft:dye:2> * 5, 0.36)
    .requireGameStages("ANY", ["one"])
    .excludeGameStages(["two"])
    .create();
```

## Recipe Builder

```
val builder = Worktable.createRecipeBuilder("carpenter") as IRecipeBuilder;
```

It's a good idea to create a new builder for each recipe. If you don't create a new builder for each recipe, parameters set on the previous recipe may leak into subsequent recipes if not explicitly reset.

Chain your method calls as shown in the examples above: it's cleaner.

## Recipe Builder Methods

### setShaped

```java
IRecipeBuilder setShaped(IIngredient[][] ingredients);
```

Either `setShaped` or `setShapeless` must be called.

This method is mutually exclusive with `setShapeless`.

### setShapeless

```java
IRecipeBuilder setShapeless(IIngredient[] ingredients);
```

Either `setShaped` or `setShapeless` must be called.

This method is mutually exclusive with `setShaped`.

### setFluid

```java
IRecipeBuilder setFluid(IFluidStack fluid);
```

&#x1F539;*Optional* - if omitted, no fluid will be required to complete the recipe

### setSecondaryIngredients

```
IRecipeBuilder setSecondaryIngredients(IIngredient[] secondaryIngredients);
```

&#x1F539;*Optional* - if omitted, no secondary ingredients will be required to complete the recipe

Up to nine secondary ingredients may be defined for the workstation and up to eleven for the workshop.

### setMirrored

```java
IRecipeBuilder setMirrored();
```

&#x1F539;*Optional* - if omitted, defaults to false
 
Mirror a shaped recipe.

### addTool

```java
IRecipeBuilder addTool(IIngredient tool, int damage);
```

Add a tool required for this recipe. The tool parameter may be an OreDict entry. The damage parameter indicates how much damage is applied to the tool per craft.

A minimum of one tool is required for each recipe. Two tools may be defined for workstation recipes and three or four tools may be defined for workshop recipes.

### addOutput

```java
IRecipeBuilder addOutput(IItemStack output, @Optional int weight);
```

This method can be called more than once to add multiple, weighted, exclusive outputs to a recipe, but must be called at least once. The `weight` parameter is optional and if omitted, defaults to `1`.

### setExtraOutput[One, Two, Three]

```java
IRecipeBuilder setExtraOutputOne(IItemStack output, float chance);
IRecipeBuilder setExtraOutputTwo(IItemStack output, float chance);
IRecipeBuilder setExtraOutputThree(IItemStack output, float chance);
```

&#x1F539;*Optional*

Add up to three additional, extra outputs.

The chance parameter is a float in the range `[0,1]`.

### requireGameStages

```java
IRecipeBuilder requireGameStages(String require, String[] stages);
```

&#x1F539;*Optional*

Valid enum values for `require` are: `ALL`, `ANY`.

`ALL` will require all listed gamestages to match and `ANY` will require at least one stage to match.

### excludeGameStages

```java
IRecipeBuilder excludeGameStages(String[] stages);
```

&#x1F539;*Optional*

If the player has unlocked any of the listed stages, this recipe will not be available.

### setMinimumTier

```java
IRecipeBuilder setMinimumTier(int tier);
```

&#x1F539;*Optional*

Allowed values:
* `0`: Worktable
* `1`: Workstation
* `2`: Workshop

This can be used to restrict a recipe to a higher tier that would otherwise be craftable in a lower tier table.

### setExperienceRequired

```java
IRecipeBuilder setExperienceRequired(int value);
```

&#x1F539;*Optional*

Sets a minimum amount of experience required to craft the recipe.

This method is mutually exclusive with `setLevelRequired`.

### setLevelRequired

```java
IRecipeBuilder setLevelRequired(int value);
```

&#x1F539;*Optional*

Sets a minimum level required to craft the recipe.

This method is mutually exclusive with `setExperienceRequired`.

### setConsumeExperience

```java
IRecipeBuilder setConsumeExperience(boolean value);
```

&#x1F539;*Optional* - if omitted, defaults to `true`.

If set to `true`, crafting the recipe will consume an amount of experience or levels set with `setExperienceRequired` or `setLevelRequired`. If set to false, crafting the recipe will not consume any experience.

### create

```java
void create();
```

This method must be called last to register the defined recipe with the system.