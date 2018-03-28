# Recipe Names

All recipes have names. By default, if no name is supplied when creating a recipe, an attempt is made by the system to provide a unique name that will remain consistent between game loads.

These recipe names are used in Design Patterns and can be used in your own NBT.

## Custom Names

You can supply your own name for a recipe by calling the following method on the builder:

```java
RecipeBuilder setName(String name);
```

!!! note
    Using `setName(String)` to define your own custom name is the best way to guarantee a unique name that will never change.

If the name you supply via this method is not unique, meaning a recipe has already been registered with this name, the system will log a warning and append a number to the end of the given recipe name.

## Generated Names

The `setName(String)` method is optional. If omitted, the system will attempt to generate a unique recipe name by hashing all of the recipe's components and combining the resulting hash with the name of the table the recipe belongs to. If two recipes generate the same hash, the second recipe's hash will be incremented until it's not a duplicate.

!!! note
    Integrated mod requirements are not hashed as part of the generated recipe name. This means that identical recipes with different requirements will produce the same auto-generated name, which would then be incremented.

In most cases, the generated names should be fine, however, it is important to know that there is a small chance that they could cause problems and why it could happen. Read below for more information.

### Hash Collision

Because we use hashes, duplicate names may be auto-generated for recipes with different components. If a duplicate name is generated, the system will increment the resulting hash by one until the recipe name is not a duplicate. There are edge cases where this hash collision could present a problem.

For example, let's say you create recipe A and recipe B. Both recipes have different components, meaning different inputs and outputs, yet the name generation hashing produces a collision and generates the same name for both recipes. Let's say recipe A is named `tailor_34580`, recipe B will then be incremented and named `tailor_34581`. In most cases, this is fine. Now let's say you create a world and create items in that world that reference recipe A and recipe B. If you were to change the order in which those two recipes are loaded, because their auto-generated names collide, the recipes would swap names. The same type of problem can occur if another recipe with a colliding hash is introduced before recipe A or recipe B. These name changes may cause issues in the existing world.

### Copying Recipes

When copying recipes by recipe output (see: [Copying Recipes](copying.md)) multiple recipes are created and duplicate names will be created if you supply your own custom name. The custom name will be incremented accordingly and, in most cases, should be fine. However, if the order in which the recipes are copied changes, or if another recipe with the same output is added, altering the order, the recipe names will change. Again, this could pose problems in a world that has items that rely on those recipe names.

!!! note
    When copying recipes, it is best to let the names be auto-generated. This will reduce the number of duplicate recipe names created, as each recipe will have different components and generate a different hash. Of course, using the auto-generated method, there is still a chance of a hash collision occurring.