# Copying Recipes

The Artisan Worktables builder API allows you to copy recipes in a reasonably flexible manner. Copying recipes, however, is a little more involved than simply calling a method on the builder, but not by much.

Here you will use a new type of builder: `Copy`. You will use this new builder to create a task that defines what to copy and how to copy it. This new builder works comparably to the `RecipeBuilder` in that you must first call a method to retrieve a builder object and can chain more methods on that object to provide additional directives to the task.

The copy task that you define with the `Copy` builder will then be passed to the `RecipeBuilder`.

First, you'll need an extra import:

```js
import mods.artisanworktables.builder.Copy;
```

Next, one, *and only one*, of the following methods must be called to initialize a new `Copy` task:

```java
// copies a recipe by recipe name
Copy byName(String recipeName);

// copies the provided recipe
Copy byRecipe(ICraftingRecipe recipe);

// copies all recipes that match the provided recipe ingredients
Copy byOutput(IIngredient[] outputs);
```

By default, the initializing methods above will instruct the `Copy` task to copy both the input and output of the provided recipe(s). This behavior can be altered by chaining additional calls to further define how you want to copy the recipe(s) indicated. Altering the behavior of a copy task is described below in the section titled [Refining a Copy Task](copying#refining-a-copy-task).

!!! note
    Calling `byOutput(IIngredient[])` may result in multiple recipes being copied and created if multiple recipes match the outputs provided.

Finally, to tell a `RecipeBuilder` about your copy task, you will pass this configured `Copy` object into the following recipe builder method:

```java
RecipeBuilder setCopy(Copy copy);
```

For example, initializing a `Copy` task and passing it to a `RecipeBuilder` will look something like this:

```js
builder.setCopy(Copy.byName("minecraft:furnace"));
```

!!! note
    Due to the way things work internally, the mirrored flag of a recipe won't be copied. When copying a shaped recipe that is mirrored, you must explicily call `setMirrored()` on the `RecipeBuilder` if you want the copy to also be mirrored.

## Refining a Copy Task

After calling one of the initializing methods above, you can call the following methods to refine exactly what is copied.

### Exclude Input

The following method will instruct the `Copy` task to not copy a recipe's input. If you call this method, you will need to supply your own recipe input on the `RecipeBuilder`.

```java
Copy noInput();
```

For example, to copy the furnace recipe by name and exclude the input, define a `Copy` task like this:

```js
builder.setCopy(Copy.byName("minecraft:furnace").noInput());
```

!!! warning
    The `noInput()` method is mutually exclusive with the `noOutput()` method.

### Exclude Output

The following method will instruct the `Copy` task to not copy a recipe's output. If you call this method, you will need to supply at least one recipe output to the `RecipeBuilder`.

```java
Copy noOutput();
```

For example, to copy the furnace recipe by name and exclude the output, define a `Copy` task like this:

```js
builder.setCopy(Copy.byName("minecraft:furnace").noOutput());
```

!!! warning
    The `noOutput()` method is mutually exclusive with both the `noInput()` method and the `replaceOutput(IItemStack)` method.

### Replace Output

The following method will instruct the `Copy` task to replace a recipe's output with the `IItemStack` provided.

```java
Copy replaceOutput(IItemStack replacement);
```

The output quantity of the replaced item will respect the output quantity of the copied recipe. This means that if the recipe you are replacing outputs `9` items, the copied recipe with the replacement output will also output `9` items.

When copying a recipe with this directive, it is not required to supply the `RecipeBuilder` with an output because the `Copy` task will supply the output for you. You can, however, supply additional output to the recipe for a weighted output pool. See [Weighted Primary Output](weighted) for more information on weighted output pools.

For example, to copy the furnace recipe by name and replace the output with a `<minecraft:diamond>`, define a `Copy` task like this:

```js
builder.setCopy(Copy.byName("minecraft:furnace").replaceOutput(<minecraft:diamond>));
```

!!! warning
    The `replaceOutput(IItemStack)` method is mutually exclusive with the `noOutput()` method.

## Examples

This sounds complicated, but it's really not. Here are some examples to better illustrate the syntax.

### By Name

This recipe will copy both the input and output of the recipe with the name `minecraft:furnace` and add a tool requirement:

```js
import mods.artisanworktables.builder.RecipeBuilder;
import mods.artisanworktables.builder.Copy;

RecipeBuilder.get("basic")
  .setCopy(Copy.byName("minecraft:furnace"))
  .addTool(<ore:artisansHammer>, 10)
  .create();
```

### By Recipe

This recipe will iterate through all recipes for `<ore:ingotGold>`, copy each recipe's input, add a tool requirement, and replace each recipe's output with a `<minecraft:diamond>` with respect to the original recipe's output quantity.

```js
import mods.artisanworktables.builder.RecipeBuilder;
import mods.artisanworktables.builder.Copy;

val builder = RecipeBuilder.get("basic");

for recipe in recipes.getRecipesFor(<ore:ingotGold>) {
    builder
        .setCopy(Copy.byRecipe(recipe).replaceOutput(<minecraft:diamond>))
        .addTool(<ore:artisansHammer>, 10)
        .create();
}
```

### By Recipe Output

This recipe will copy only the input of all recipes that have an output of `<ore:ingotIron>`, add a tool requirement, and add an output of `<minecraft:string>`.

```js
import mods.artisanworktables.builder.RecipeBuilder;
import mods.artisanworktables.builder.Copy;

RecipeBuilder.get("basic")
  .setCopy(Copy.byOutput([<ore:ingotIron>, <ore:ingotGold>]).noOutput())
  .addTool(<ore:artisansHammer>, 10)
  .addOutput(<minecraft:string>)
  .create();
```
