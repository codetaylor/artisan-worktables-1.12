### ZenScript

#### Import

```
import mods.artisanworktables.Worktable;
```

#### Syntax

**Note:** The tool slots assume the item being used as the tool is an item with durability. Using an item without durability in a recipe will have unexpected and, most likely, undesired results. Also, matching tools based on NBT data is not supported.

Valid table names are: `basic`, `blacksmith`, `carpenter`, `jeweler`, `mason`, and `tailor`.

```
Worktable.addRecipeShaped(String table, IItemStack result, IIngredient tool, int toolDamage, boolean mirrored, IIngredient[][] input);
Worktable.addRecipeShapeless(String table, IItemStack result, IIngredient tool, int toolDamage, IIngredient[] input);
```

#### Examples

```
import mods.artisanworktables.Worktable;

Worktable.addRecipeShaped("tailor", <minecraft:string>, <minecraft:iron_sword>, 10, true, [
    [<minecraft:leather>, null, null],
    [null, <minecraft:leather>, null],
    [null, null, <minecraft:leather>]]);

Worktable.addRecipeShapeless("tailor", <minecraft:string> * 9, <minecraft:shears>, 5, [<minecraft:wool>]);

Worktable.addRecipeShapeless("carpenter", <minecraft:planks> * 16, <minecraft:diamond_axe>, 1, [<ore:logWood>, <minecraft:egg>]);

// Create the `pickaxe` OreDict and add some pickaxes to use for recipes...
<ore:pickaxe>.addItems([
    <minecraft:diamond_pickaxe>,
    <minecraft:iron_pickaxe>,
    <tconstruct:pickaxe> // <- this will use any Tinker's pickaxe
]);

Worktable.addRecipeShapeless("jeweler", <minecraft:emerald>, <ore:pickaxe>, 1, [<minecraft:diamond>]);
```