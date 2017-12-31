Quick links:

* [ZENSCRIPT.md](https://github.com/codetaylor/artisan-worktables/blob/master/ZENSCRIPT.md)
* [CHANGELOG.md](https://github.com/codetaylor/artisan-worktables/blob/master/CHANGELOG.md)
* [LICENSE](https://github.com/codetaylor/artisan-worktables/blob/master/LICENSE)

### Artisan Worktables

Themed worktables with a tool slot.

This mod is designed for modpack makers. It contains only example recipes and is intended to be used with CraftTweaker. JEI integration is supported.

Depends on the library mod Athenaeum.

#### Tables

![Tables](https://raw.githubusercontent.com/codetaylor/artisan-worktables/master/assets/tables.png)

![TableGuis](https://raw.githubusercontent.com/codetaylor/artisan-worktables/master/assets/table_gui.gif)

Recipes for the worktables are like the vanilla crafting recipes (shaped, shapeless) except they require a tool and tool durability cost to be specified. Tools go into the extra slot in the table and remain in the table between crafts. The tables will also retain their contents when closed, but not when broken.

Shift-clicking items will swap tools and place items in the crafting matrix.

Recipes to craft the tables are not provided. It is intended that you use CraftTweaker to supply your own recipes for making and using the worktables.  

Vanilla crafting table recipes are not supported in any table and there are currently no plans to do so.

#### Tools

![TableTools](https://raw.githubusercontent.com/codetaylor/artisan-worktables/master/assets/items.png)

Two tools for each table are provided if you want to use them. The tools and their recipes can be easily disabled in the config file. Tools for vanilla materials and Thermal Foundation materials are provided.