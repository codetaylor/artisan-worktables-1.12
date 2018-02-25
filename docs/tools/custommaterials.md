# Custom Tool Materials

Artisan Worktables allows you to add and remove tool materials from the mod through the use of `.json` files in the config folder.

The tool material definitions are located here:

```
config/
  artisanworktables/
    artisanworktables.module.Tools.Materials.Custom.json
    artisanworktables.module.Tools.Materials.Generated.json
```

The `artisanworktables.module.Tools.Materials.Generated.json` file is regenerated every time the game is loaded. This will always contain the default values that are shipped with the mod. It is meant for reference only and shouldn't be modified, as it will be overwritten anyway.

The `artisanworktables.module.Tools.Materials.Custom.json` is the file you are interested in. This file can be modified to add or remove materials from the mod. Tools are automatically generated from the materials in this file.

## Json Format

```json
{
  "name": "wood",
  "harvestLevel": 0,
  "maxUses": 59,
  "efficiency": 2.0,
  "damage": 0.0,
  "enchantability": 15,
  "color": "73523e",
  "shiny": false,
  "ingredient": "ore:plankWood",
  "langKey": "material.athenaeum.wood",
  "oreDictKey": "artisansToolWood"
}
```

All the `.json` keys should be fairly self explanatory. Some that might not be so obvious are explained below.

### ingredient

This is the primary ingredient used in a tool's recipe.

### langKey

The lang keys point to entries in a `en_us.lang` (or other language) lang file located in a mod's or resource pack's `assets` folder.

For example:

```
"langKey": "material.athenaeum.elementium",
```

Refers to the entry in the Athenaeum mod's `assets/athenaeum/lang` files:

```
material.athenaeum.elementium=Elementium
```

You can supply your own lang files using a resource pack or a mod such as [Resource Loader](https://minecraft.curseforge.com/projects/resource-loader).

These lang keys are used when displaying a material's name in-game, like `Blacksmith's Elementium Hammer` or `Mage's Iron Athame`.

If the material you're adding comes from a mod in the modpack, it is possible that the mod would already have a lang key for the material name that you could use. You could open up the mod's .jar file and dive into the `assets/[modname]/lang` folder and inspect those files for a key.

As a last resort, you could simply use the name of the material as the lang key and that would be displayed. For example, using `Sand` as the lang key for a sand material would display as `Blacksmith's Sand Hammer` or `Mage's Sand Athame`. It works, but won't allow for translations.

### oreDictKey

All tools generated with this material will be added to the ore dictionary using the `oreDictKey` provided.