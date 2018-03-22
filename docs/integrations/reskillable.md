# Reskillable

## Import

```js
import mods.artisanworktables.integration.requirement.Reskillable;
```

## Requirement Builder

```java
ReskillableRequirementBuilder add(String[] requirement);

ReskillableRequirementBuilder addAll(String[] requirements);
```

## Examples

```js
RecipeBuilder.get("basic")
  .setShapeless([<minecraft:string>])
  .addOutput(<minecraft:gold_ingot>)
  .addRequirement(Reskillable.add("reskillable:mining|10"))
  .create();
```

```js
RecipeBuilder.get("basic")
  .setShapeless([<minecraft:string>])
  .addOutput(<minecraft:gold_ingot>)
  .addRequirement(Reskillable.addAll(["reskillable:mining|10", "reskillable:gathering|10"]))
  .create();
```