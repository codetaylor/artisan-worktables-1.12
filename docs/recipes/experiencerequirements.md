# Experience Requirements

Experience requirements can be set on recipes. The requirement can be defined in experience or levels and set to not actually consume the required amount if so desired.

## Add Experience Requirements to Recipes

The following builder methods are used to define experience requirements:

```java
RecipeBuilder setExperienceRequired(int experienceRequired);
RecipeBuilder setLevelRequired(int levelRequired);
RecipeBuilder setConsumeExperience(boolean consumeExperience);
```

By default, recipes will consume experience or levels when the requirements are set. You can call `setConsumeExperience(false)` to keep the experience requirement of a recipe, but prevent it from consuming any when crafting.

This recipe requires and consumes `20` experience when crafting:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .addOutput(<minecraft:cobblestone>)
  .setExperienceRequired(20)
  .create();
```

This recipe requires `30` levels to craft, but does not consume any experience when crafting:

```js
import mods.artisanworktables.builder.RecipeBuilder;

RecipeBuilder.get("basic")
  .setShapeless([<minecraft:dirt>])
  .setLevelRequired(30)
  .setConsumeExperience(false)
  .addOutput(<minecraft:cobblestone>)
  .create();
```
