# Hiding Recipes

Recipes can be hidden from JEI by calling the following method on the builder:

```java
RecipeBuilder setHidden(@Optional(default = true) hidden);
```

The `hidden` parameter is an optional `boolean` that defaults to true if omitted.