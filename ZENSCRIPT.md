### ZenScript

#### Import

```
import mods.artisanworktables.Worktable;
```

#### Shaped

Valid table names are: `basic`, `blacksmith`, `carpenter`, `jeweler`, `mason`, and `tailor`.

##### Syntax

```
Worktable.addRecipeShaped(String table, IItemStack result, IItemStack tool, IIngredient[][] input);
Worktable.addRecipeShaped(String table, IItemStack result, IItemStack tool, int toolDamage, IIngredient[][] input);
```

If no tool damage is provided, the tool will not be damaged.

#### Shapeless

Valid table names are: `basic`, `blacksmith`, `carpenter`, `jeweler`, `mason`, and `tailor`.

##### Syntax

```
Worktable.addRecipeShapeless(String table, IItemStack result, IItemStack tool, IIngredient[] input);
Worktable.addRecipeShapeless(String table, IItemStack result, IItemStack tool, int toolDamage, IIngredient[] input);
```

If no tool damage is provided, the tool will not be damaged.