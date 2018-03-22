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
RecipeBuilder setShaped(IIngredient[][] ingredients);

RecipeBuilder setShapeless(IIngredient[] ingredients);

RecipeBuilder setFluid(ILiquidStack fluidIngredient);

RecipeBuilder setSecondaryIngredients(IIngredient[] secondaryIngredients);

RecipeBuilder setConsumeSecondaryIngredients(boolean consumeSecondaryIngredients);

RecipeBuilder setMirrored();

RecipeBuilder addTool(IIngredient tool, int damage);

RecipeBuilder addOutput(IItemStack output, @Optional int weight);

RecipeBuilder setExtraOutputOne(IItemStack output, float chance);

RecipeBuilder setExtraOutputTwo(IItemStack output, float chance);

RecipeBuilder setExtraOutputThree(IItemStack output, float chance);

RecipeBuilder setMinimumTier(int minimumTier);

RecipeBuilder setMaximumTier(int maximumTier);

RecipeBuilder setExperienceRequired(int experienceRequired);

RecipeBuilder setLevelRequired(int levelRequired);

RecipeBuilder setConsumeExperience(boolean consumeExperience);

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

## GameStages

```java
import mods.artisanworktables.integration.requirement.GameStages;
```

```java
static GameStagesRequirementBuilder allOf(String[] stages);

static GameStagesRequirementBuilder anyOf(String[] stages);

static GameStagesRequirementBuilder exclude(String[] stages);
```

```java
GameStagesRequirementBuilder allOf(String[] stages);

GameStagesRequirementBuilder anyOf(String[] stages);

GameStagesRequirementBuilder exclude(String[] stages);
```

## Reskillable

```java
import mods.artisanworktables.integration.requirement.Reskillable;
```

```java
static ReskillableRequirementBuilder add(String[] requirement);

static ReskillableRequirementBuilder addAll(String[] requirements);
```

```java
ReskillableRequirementBuilder add(String[] requirement);

ReskillableRequirementBuilder addAll(String[] requirements);
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
scribe
tailor
```

## OreDict Tool Groups

The complete list of tool type groups is as follows:

```ruby
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
<ore:artisansCuttingBoard>
<ore:artisansPan>
<ore:artisansLens>
<ore:artisansSifter>
<ore:artisansKnife>
```