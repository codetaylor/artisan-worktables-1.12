# ZenScript Reference

## RecipeBuilder

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

RecipeBuilder requireGameStages(String require, String[] stages);

RecipeBuilder excludeGameStages(String[] stages);

RecipeBuilder setMinimumTier(int minimumTier);

RecipeBuilder setMaximumTier(int maximumTier);

RecipeBuilder setExperienceRequired(int experienceRequired);

RecipeBuilder setLevelRequired(int levelRequired);

RecipeBuilder setConsumeExperience(boolean consumeExperience);

RecipeBuilder setCopy(Copy copyTask);

RecipeBuilder create();
```

## Copy

```java
static Copy byName(String recipeName);

static Copy byRecipe(ICraftingRecipe recipe);

static Copy byOutput(IIngredient[] outputs);
```

```java
Copy noInput();

Copy noOutput();

Copy replaceOutput(IItemStack replacement);
```