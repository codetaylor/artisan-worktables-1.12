## Advanced ZenScript

### Import

```
import mods.artisanworktables.Worktable;
import mods.artisanworktables.IRecipeBuilder;
```

### Example

The bare minimum:

```
Worktable.createRecipeBuilder("carpenter")
    .setShaped([
        [<minecraft:planks>],
        [<minecraft:planks>],
        [<minecraft:planks>]])
    .setTool(<ore:carpenters_hammer>, 3)
    .addOutput(<minecraft:planks>)
    .create();
```

More options:

```
Worktable.createRecipeBuilder("carpenter")
    .setShaped([
        [<minecraft:planks>],
        [<minecraft:planks>],
        [<minecraft:planks>]])
    .setTool(<ore:carpenters_hammer>, 3)
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

### Recipe Builder

```
val builder = Worktable.createRecipeBuilder("carpenter") as IRecipeBuilder;
```

It's a good idea to create a new builder for each recipe. If you don't create a new builder for each recipe, parameters set on the previous recipe may leak into subsequent recipes if not explicitly reset.

Chain your method calls as shown in the examples above: it's cleaner.

### Recipe Builder Methods

#### setShaped

Either `setShaped` or `setShapeless` must be called.

```java
IRecipeBuilder setShaped(IIngredient[][] ingredients);
```

#### setShapeless

Either `setShaped` or `setShapeless` must be called.

```java
IRecipeBuilder setShapeless(IIngredient[] ingredients);
```

#### setMirrored

&#x1F539;*Optional* - if omitted, defaults to false
 
Mirror a shaped recipe.

```java
IRecipeBuilder setMirrored();
```

#### setTool

Set the tool required for this recipe. May be an OreDict entry.

```java
IRecipeBuilder setTool(IIngredient tool, int damage);
```

#### addOutput

This method can be called more than once to add multiple, weighted, exclusive outputs to a recipe, but must be called at least once. The `weight` parameter is optional and if omitted, defaults to `1`.

```java
IRecipeBuilder addOutput(IItemStack output, @Optional int weight);
```

#### addExtraOutput

&#x1F539;*Optional*

Add up to three additional, extra outputs.

```java
IRecipeBuilder setExtraOutputOne(IItemStack output, float chance);
IRecipeBuilder setExtraOutputTwo(IItemStack output, float chance);
IRecipeBuilder setExtraOutputThree(IItemStack output, float chance);
```

#### requireGameStages

&#x1F539;*Optional*

Valid enum values for `require` are: `ALL`, `ANY`.

`ALL` will require all listed gamestages to match and `ANY` will require at least one stage to match.

```java
IRecipeBuilder requireGameStages(String require, String[] stages);
```

#### excludeGameStages

&#x1F539;*Optional*

If the player has unlocked any of the listed stages, this recipe will not be available.

```java
IRecipeBuilder excludeGameStages(String[] stages);
```

#### create

This method must be called last to register the defined recipe with the system.

```java
void create();
```