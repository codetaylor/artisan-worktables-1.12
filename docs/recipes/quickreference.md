# Quick Reference

## ZenScript Methods

### RecipeBuilder

```java
import mods.artisanworktables.builder.RecipeBuilder;
```

```java
static RecipeBuilder get(String table);
```

```java
RecipeBuilder setName(String name);

RecipeBuilder setShaped(IIngredient[][] ingredients);

RecipeBuilder setShapeless(IIngredient[] ingredients);

RecipeBuilder setFluid(ILiquidStack fluidIngredient);

RecipeBuilder setSecondaryIngredients(IIngredient[] secondaryIngredients);

RecipeBuilder setConsumeSecondaryIngredients(@Optional(default = true) boolean consume);

RecipeBuilder setMirrored(@Optional(default = true) boolean mirrored);

RecipeBuilder addTool(IIngredient tool, int damage);

RecipeBuilder addOutput(IItemStack output, @Optional(default = 1) int weight);

RecipeBuilder setExtraOutputOne(IItemStack output, float chance);

RecipeBuilder setExtraOutputTwo(IItemStack output, float chance);

RecipeBuilder setExtraOutputThree(IItemStack output, float chance);

RecipeBuilder setMinimumTier(int minimumTier);

RecipeBuilder setMaximumTier(int maximumTier);

RecipeBuilder setExperienceRequired(int experienceRequired);

RecipeBuilder setLevelRequired(int levelRequired);

RecipeBuilder setConsumeExperience(@Optional(default = true) boolean consume);

RecipeBuilder setHidden(@Optional(default = true) boolean hidden);

RecipeBuilder setRecipeFunction(IRecipeFunction recipeFunction);

RecipeBuilder setRecipeAction(IRecipeAction recipeAction);

RecipeBuilder setCopy(Copy copyTask);

RecipeBuilder addRequirement(IMatchRequirementBuilder requirementBuilder);

RecipeBuilder create();
```

### Copy

```java
import mods.artisanworktables.builder.Copy;
```

```java
static Copy byName(String recipeName);

static Copy byRecipe(ICraftingRecipe recipe);

static Copy byOutput(IIngredient[] outputs);
```

```java
Copy noInput();

Copy replaceInput(@Nullable IIngredient toReplace, @Nullable IIngredient replacement);

Copy replaceShapedInput(int col, int row, @Nullable IIngredient replacement);

Copy noOutput();

Copy replaceOutput(IItemStack replacement);
```

## Table Names

The list of valid table names is:

```xml
basic
blacksmith
carpenter
chef
chemist
engineer
farmer
jeweler
mage
mason
potter
scribe
tailor
tanner
```

## OreDict Tool Groups

The complete list of tool type groups is as follows:

```ruby
<ore:artisansAthame>
<ore:artisansBeaker>
<ore:artisansBurner>
<ore:artisansCarver>
<ore:artisansChisel>
<ore:artisansCompass>
<ore:artisansCutters>
<ore:artisansCuttingBoard>
<ore:artisansDriver>
<ore:artisansFile>
<ore:artisansFramingHammer>
<ore:artisansGemCutter>
<ore:artisansGrimoire>
<ore:artisansGroover>
<ore:artisansHammer>
<ore:artisansHandsaw>
<ore:artisansHatchet>
<ore:artisansKnife>
<ore:artisansLens>
<ore:artisansNeedle>
<ore:artisansPan>
<ore:artisansPencil>
<ore:artisansPliers>
<ore:artisansPunch>
<ore:artisansQuill>
<ore:artisansShears>
<ore:artisansSifter>
<ore:artisansSolderer>
<ore:artisansSpanner>
<ore:artisansTrowel>
<ore:artisansTSquare>
```