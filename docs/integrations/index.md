# Mod Integrations

Recipes can have requirements that are specific to mod integrations such as GameStages or Reskillable. These requirements are added using the following builder method:

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

See the classes in the following package for an example implementation:

```
com.codetaylor.mc.artisanworktables.modules.requirement.gamestages
```