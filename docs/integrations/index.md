# Mod Integrations

For mod integration details, see the [docs for Artisan Integrations](https://artisan-integrations.readthedocs.io).

Recipes can have requirements that are specific to mod integrations. These requirements are added using the following builder method:

```java
RecipeBuilder addRequirement(IMatchRequirementBuilder requirementBuilder);
```

## Custom Recipe Requirements

Mods can add their own recipe requirements by implementing the following interfaces:

```
com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirement
com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementBuilder
com.codetaylor.mc.artisanworktables.api.recipe.requirement.IMatchRequirementContext
```