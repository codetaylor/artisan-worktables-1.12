# Table Tiers

The Artisan Worktables crafting tables come in three different flavors: `Worktables`, `Workstations`, and `Workshops`.

| Tier        | Grid | Tools | Fluid Capacity | Secondary |
|-------------|:----:|:-----:|:--------------:|:---------:|
| Worktable   | 3x3  | 1     | 4,000mb        | 0         |
| Workstation | 3x3  | 2     | 8,000mb        | 9         |
| Workshop    | 5x5  | 3     | 16,000mb       | 9         |

## Recipe Tiers

Recipes that don't exceed any of a tier's limitations can be crafted in that tier. For example, a recipe that uses a 3x3 grid pattern and one tool can be crafted in any of the three tiers, while a recipe that uses a 4x4 grid and two tools will only be craftable in a tier three table.

If you want to restrict a recipe from being crafted in a lower tier table, you can use the following method when constructing a recipe:

```java
RecipeBuilder setMinimumTier(int minimumTier);
```

Conversely, if you want to restrict a recipe from being crafted in a higher tier table, you can use the following method:

```java
RecipeBuilder setMaximumTier(int maximumTier);
```

Accepted values for `minimumTier` and `maximumTier` are:

| Tier        | Tier Id |
|-------------|:-------:|
| Worktable   | 0       |
| Workstation | 1       |
| Workshop    | 2       |

## Examples

In the following recipe, even though the table doesn't exceed the limitations of any of the three tiers, it can only be crafted in a `Workshop` tier table:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .setMinimumTier(2)
  .create();
```

Alternately, the following recipe can only be crafted in a `Worktable` tier table:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .setMaximumTier(0)
  .create();
```
