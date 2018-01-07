### ZenScript

#### Import

```
import mods.artisanworktables.Worktable;
```

#### Syntax

**Note:** The tool slots assume the item being used as the tool is an item with durability. Using an item without durability in a recipe will have unexpected and, most likely, undesired results. Also, matching tools based on NBT data is not supported.

Valid table names are: `basic`, `blacksmith`, `carpenter`, `jeweler`, `mason`, and `tailor`.

All extra outputs are optional.

```
// Normal recipes
Worktable.addRecipeShaped(
  String table, 
  IItemStack result, 
  IIngredient tool, 
  int toolDamage, 
  boolean mirrored, 
  IIngredient[][] input,
  @Optional IItemStack secondaryOutput,
  @Optional float secondaryOutputChance,
  @Optional IItemStack tertiaryOutput,
  @Optional float tertiaryOutputChance,
  @Optional IItemStack quaternaryOutput,
  @Optional float quaternaryOutputChance  
);

Worktable.addRecipeShapeless(
  String table, 
  IItemStack result, 
  IIngredient tool, 
  int toolDamage, 
  IIngredient[] input,
  @Optional IItemStack secondaryOutput,
  @Optional float secondaryOutputChance,
  @Optional IItemStack tertiaryOutput,
  @Optional float tertiaryOutputChance,
  @Optional IItemStack quaternaryOutput,
  @Optional float quaternaryOutputChance
);

// GameStages staged recipes 
Worktable.addStagedRecipeShaped(
  String gameStageName, 
  String table, 
  IItemStack result, 
  IIngredient tool, 
  int toolDamage, 
  boolean mirrored, 
  IIngredient[][] input,
  @Optional IItemStack secondaryOutput,
  @Optional float secondaryOutputChance,
  @Optional IItemStack tertiaryOutput,
  @Optional float tertiaryOutputChance,
  @Optional IItemStack quaternaryOutput,
  @Optional float quaternaryOutputChance
);

Worktable.addStagedRecipeShapeless(
  String gameStageName, 
  String table, 
  IItemStack result, 
  IIngredient tool, 
  int toolDamage, 
  IIngredient[] input,
  @Optional IItemStack secondaryOutput,
  @Optional float secondaryOutputChance,
  @Optional IItemStack tertiaryOutput,
  @Optional float tertiaryOutputChance,
  @Optional IItemStack quaternaryOutput,
  @Optional float quaternaryOutputChance
);
```

All tools are grouped in OreDict entries based on their tool type for easy recipe reference:

```
<ore:blacksmiths_cutters>
<ore:blacksmiths_hammer>
<ore:carpenters_hammer>
<ore:carpenters_handsaw>
<ore:jewelers_gemcutter>
<ore:jewelers_pliers>
<ore:masons_chisel>
<ore:masons_trowel>
<ore:tailors_needle>
<ore:tailors_shears>
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

// Create a recipe that turns three leather into a string using the tailor's shears with a 50% chance to create
// 5 extra string and a 1% chance to create an extra diamond.
Worktable.addStagedRecipeShaped("gameStageOne", "tailor", <minecraft:string>, <ore:tailors_shears>, 10, true, [
    [<minecraft:leather>, null, null],
    [null, <minecraft:leather>, null],
    [null, null, <minecraft:leather>]],
    <minecraft:string> * 5, 0.5,
    <minecraft:diamond>, 0.01
);
```