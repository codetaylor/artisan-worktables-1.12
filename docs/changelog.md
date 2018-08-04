1.18.10

  * Fixed: Issue when crafting item with weighted primary output (#124)
  * Fixed: Some custom tools aren't created (#125)

---

1.18.9

  * Fixed: JEI displays recipes when tank size is insufficient (#122)
  * Fixed: Damaged artisan tool loses oredict (#123)

---

1.18.8

  * Fixed: undesirable behavior with GameStage requirements, (#121)

---

1.18.7

  * Fixed: CraftTweaker's 'marked' only returns last marked item in shaped recipes, (#120)

---

1.18.6

  * Fixed: missing recipes in JEI 4.9.1.188+, (#114)

---

1.18.5

  * Fixed: Switch PlayerDataHandler with GameStageHelper

---

1.18.4

  * Fixed: client crash with GameStages 2.0 update (#111)
  * Optional Requirement: GameStages >= 2.0.90

---

1.18.3

  * Fixed: crash with copied recipes (#101)

---

1.18.2

  * Changed: updated zh_cn.lang (PR#99 DYColdWind)

---

1.18.1

  * BREAKING CHANGES:
    * API: To correct (#98) an oversight was fixed in the API; the signature of `IRequirement getRequirement(String modId)` has been changed to `IRequirement getRequirement(ResourceLocation resourceLocation)`.
  * Added: API: `Map<ResourceLocation, IRequirement> getRequirements()` to `IArtisanRecipe`
  * Added: API: `boolean matchesRequirements(Map<ResourceLocation, IRequirementContext> requirementContextMap)` to `IArtisanRecipe`
  * Changed: cleaned up Reskillable requirement integration
  * Changed: updated API (version 3)

---

1.18.0

  * BREAKING CHANGES:
    * Changed: `AWItemCraftEvent` renamed to `ArtisanCraftEvent`
    * Removed: `RecipeBuilder setTool(IIngredient, int)` - deprecated since 1.13.21 (Feb 8, 2018)
    * Removed: `mods.artisanworktables.Worktable` - deprecated since 1.16.0 (Feb 26, 2018)
  * Added: builder method for new recipe requirement integrations
    * `RecipeBuilder addRequirement(IMatchRequirementBuilder requirementBuilder)`
  * Added: GameStages requirement integration
  * Added: Reskillable requirement integration
  * Added: builder methods to support CraftTweaker recipe functions and actions
    * `setRecipeFunction(IRecipeFunction)`
    * `setRecipeAction(IRecipeAction)`
  * Added: support for the following CraftTweaker item transformers:
    * `item.reuse()`
    * `item.transformReplace(IItemStack)`
    * `item.transformDamage()`
    * `item.transformDamage(int)`
    * `item.noReturn()`
  * Changed: updated API (version 2)
  * Changed: improved tolerance for incomplete recipes (#97)
  * Changed: removed display of tool damage in JEI when the recipe has no tool
  * Deprecated: gamestages recipe builder methods - use the new requirement integrations instead
    * `RecipeBuilder requireGameStages(String require, String[] stages)`
    * `RecipeBuilder excludeGameStages(String[] stages)`
  * Requires: Athenaeum >= 1.14.0

---

1.17.2

 * Fixed: ContentTweaker custom fluids have gray color in GUI (#95)

---

1.17.1

  * Changed: updated de_de.lang (PR#90 Xaikii)

---

1.17.0

  * Note: you will need to refresh the tool config or manually add the new tools to the list
  * Added: new tables!
    * Farmer's Worktable, Workstation, and Workshop
    * Chef's Worktable, Workstation, and Workshop
  * Added: new tools!
    * Lens
    * Sifter
    * Cutting Board
    * Pan
    * Mortar
    * Knife
  * Added: new recipe builder methods!
    * `RecipeBuilder setMaximumTier(int maximumTier);`
    * `RecipeBuilder setConsumeSecondaryIngredients(boolean consumeSecondaryIngredients);`
  * Added: new copy builder methods!
    * `Copy replaceInput(@Nullable IIngredient toReplace, @Nullable IIngredient replacement);`
    * `Copy replaceShapedInput(int col, int row, @Nullable IIngredient replacement);`
  * Added: item handler capability for secondary ingredients
    * This adds the ability to pipe items in and out of the secondary ingredient slots, with a caveat: only one stack of any given item type will be accepted by the slots when piping items in. This means that you can have a chest of nine different items piping into a table and the internal handler will only allow one stack of each item into the slots. This is useful to keep secondary ingredients stocked for manual crafting and will be useful for automation support in a future update.
  * Changed: added emboss effect to all GUI result slot textures
  * Changed: Athame tool texture is now squiggly - the old texture was repurposed for the Knife tool
  * Changed: Athame tool recipe now requires a piece of lapis
  * Fixed: snow should not gather on top of Worktables
  * Fixed: copying shaped recipes rotates and flips ingredient layout
  * Requires: Athenaeum >= 1.13.0

---

1.16.2

  * Fixed: Fluid tank GUI display not updating when piping fluids in / out (#89)
 
---

1.16.1

  * Fixed: Workshop GUI tab arrow in the wrong position (#88)

---

1.16.0

  * NOTES:
    * This beta release has many changes. I encourage you to read the changelog, regen your configs, and reference the new documentation.
    * http://artisan-worktables.readthedocs.io
  * BREAKING CHANGES:
    * the import `mods.artisanworktables.IRecipeBuilder` has moved to `mods.artisanworktables.builder.RecipeBuilder`; note the change in the package as well as the removal of the `I`
    * all config files have been moved into the subfolder `artisanworktables`
    * all tool by type ore dict groups have been renamed, ie. `<ore:masons_chisel>` is now `<ore:artisansChisel>`
    * all tool item names have changed, ie. `<artisanworktables:masons_chisel_iron>` is now `<artisanworktables:artisans_chisel_iron>`
  * Added: json files to define tool materials (#64)
    * Tool materials have been removed from the tool module config file, instead use the `Custom` tool material json file to add / remove tool materials.
    * The file `artisanworktables.module.Tools.Materials.Generated.json` will be regenerated from the defaults each time the app is launched; you may reference this file, but don't edit it - it will be overwritten.
    * Any edits to the file `artisanworktables.module.Tools.Materials.Custom.json` will be read during initialization and tools will be generated using the defined materials.
  * Added: the table text highlight color can now be changed via the config file or the in-game config menu (#75)
  * Added: support for ingredientOr (#68)
  * Added: ore dict groups for tools by material type; the ore dict keys can be changed in the tool material json config file (#74)
  * Added: `AWItemCraftEvent.Post` post craft event (#72)
  * Added: config option to:
    * change tool material group ore dict prefix
    * change tool type group ore dict prefix
    * disable all tool material ore dict groups
    * disable all tool type ore dict groups
  * Added: can now copy existing recipes to worktable recipes (#69, gigitty):
    * Note: this *will not* work to copy recipes added by ZenScript, only with recipes that exist *before* ZenScript recipes are added
    * `RecipeBuilder#setCopy(Copy)` - copy a recipe's input and output, see documentation
  * Changed: if a player is too far away from a table to use it, the table's tab will no longer display in the gui
  * Changed: if a player is too far away from the toolbox to use it, its gui will no longer be displayed
  * Changed: removed profession distinction from tool names, ie. `Mason's Chisel` is now `Artisan's Chisel` (#76)
  * Changed: recipes without tools are now allowed, simply omit the call to `addTool` (#50,#82)
  * Changed: bone tool recipes now use the `bone` ore dict entry (#70)
  * Changed: factored out and exposed limited API package to complete (#72), API Version 1 - subject to change, build against this at your own risk
  * Changed: updated de_de.lang (PR#83 Xaikii)
  * Deprecated:
    * `Worktable.createRecipeBuilder(String)` will be removed in a future version - instead, import `mods.artisanworktables.builder.RecipeBuilder` and call `RecipeBuilder.get(String)`
    * the entire import `mods.artisanworktables.Worktable` and all its methods will be removed in a future version - use the recipe builder instead
  * Requires: Athenaeum >= 1.12.6

---

1.15.27

  * Fixed: compatibility issue with lib Athenaeum 1.12.6+

---

1.15.26

  * Changed: updated zh_cn.lang (PR#84 DYColdWind)

---

1.15.25

  * Fixed: Unable to craft item when recipe is put in by another player (#79)

---

1.15.24

  * Fixed: NoClassDefFoundError: com/codetaylor/mc/artisanworktables/modules/worktables/gui/GuiContainerBase (#78)

---

1.15.23

  * IMPORTANT: This update will break existing worktable and workstation tile entities. To reduce the number of classes used, type information is now stored in the tile entity. Preexisting tiles will not have this information and, when loaded, will adopt type 0, which translates to the Tailor type. This means existing tables and stations in a world will look the same, but behave as a Tailor's table or station. Breaking a table or station and placing it again should reset its tile entity.
  * Added: Workshops!
    * Workshops are third tier worktables. They can support recipes that use up to three tools and nine secondary ingredients.
    * All recipes share the same recipe pool, therefore, any recipe defined for a worktable or workstation will also work in a workshop. If a recipe is defined that uses a pattern larger than a 3x3 crafting grid, or three tools, it will only be craftable in the new workshops.
  * Added: additional error reporting to the ZenScript RecipeBuilder
  * Changed: rope handle texture on the side of the Mechanical Toolbox now matches the Toolbox's rope handle texture
  * Changed: ZenScript RecipeBuilder#create() now resets the builder
  * Changed: ZenScript RecipeBuilder#create() now returns the recipe builder, allowing method chaining
  * Changed: updated de_de.lang (PR#63,PR#65 Xaikii)

---

1.14.23

  * Fixed: Workstation shows wrong internal tank capacity in JEI (#62)

---

1.14.22

  * Added: experience and level requirement / cost to recipes
  * Added: new ZenScript builder methods
    * `setExperienceRequired(int)` - set experience requirement for the recipe (optional)
    * `setLevelRequired(int)` - set level requirement for the recipe (optional)
    * `setConsumeExperience(boolean)` - recipe will consume / not consume experience (optional, default: true)
  * Changed: JEI will now consider items in the secondary ingredient slots as part of the player inventory. This means that, when transferring items, JEI will take into account items in these slots.
  * Requires: Athenaeum >= 1.9.5

---

1.13.22

  * Changed: updated zh_cn.lang (PR#60 DYColdWind)

---

1.13.21

  * Added: Workstations!
    * Workstations are second tier worktables. They can support recipes that use up to two tools and nine secondary ingredients. 
    * All recipes share the same recipe pool, therefore, any recipe defined for a worktable will also work in a workstation. If a recipe is defined that uses two tools or one to nine secondary ingredients, it will only be craftable in the new workstations.
    * Recipes can be defined to require multiple secondary ingredients of the same type. For example, `<minecraft:clay_ball> * 8`, would require consuming 8 clay balls to complete the recipe.
    * JEI recipe transfer will transfer items to the 3x3 crafting grid and tools to the two tool slots, however, it will not transfer items to the secondary ingredient slots. This is intentional. Firstly, JEI has a limitation in that it will only transfer one of each item unless shift is held while clicking the transfer button. The second drawback is that when JEI does the recipe transfer, it will clear all other slots that are not used in the recipe. This means that any secondary ingredients that you have stored in the slots will be returned to your inventory. This is inconvenient. 
  * Added: new ZenScript builder methods
    * `addTool(IIngredient, int)`
    * `setSecondaryIngredients(IIngredient[])`
  * Added: fr_fr.lang (PR#56 Okii35)
  * Changed: new models and textures for the worktables
  * Changed: deprecated ZenScript builder methods
    * `setTool(IIngredient, int)` - use `addTool(IIngredient, int)` instead
  * Changed: updated de_de.lang (PR#58 Xaikii)
  * Fixed: JEI recipe transfer issue with some converted oreDict ingredients
  * Fixed: stacks in secondary output slots are not dropped when the block is broken
  * Note: you will need to refresh the config for the worktable module
  * Requires: Athenaeum >= 1.8.4

---

1.12.21

  * Fixed: crash with liquid in GUI (or invisible items w/ optifine) (#52)
  * Fixed: tertiary and quaternary chanced outputs not set properly with legacy zen methods
  * Changed: refactor of the gui / container code (internal prep)
  * Changed: changed recipes to handle multiple tools (internal prep)
  * Note: This is being released as an alpha because it changes several internal systems. New features will be built using the new changes and it is important to quickly catch and eliminate any bugs that have been introduced by the refactoring. If you would like to help speed the development process and ensure a stable product, please report any issues you find with this version. Thank you!
  * Requires: Athenaeum >= 1.7.4

---

1.11.21

  * Fixed: fluid cleared via GUI remains after re-load (#49)
  * Changed: updated de_de.lang (PR#46 Xaikii) 

---

1.11.20

  * Added: Mechanical Toolbox!
    * This new toolbox will automatically swap old tools for new tools. When a tool is broken,
      or lacks sufficient durability to complete the last successful craft, the Mechanical
      Toolbox will swap a new, appropriate tool into the worktable's tool slot.
  * Added: config option to require sufficient tool durability (#44)
  * Added: durability tooltip to all tools from this mod
  * Added: config option to disable the durability tooltip
  * Note: you will probably need to refresh the config for the tools and the toolbox

---

1.10.20

  * Fixed: shift-click tool swap eats tools of the same type (#45)

---

1.10.19

  * Fixed: GUI fluid tank render bug (#40)
  * Fixed: Non-interactive fluid display in JEI (#42)
  * Changed: updated de_de.lang (PR#43 Xaikii)

---

1.10.18

  * Fixed: NPE crash when using any recipe without a fluid ingredient (#41)
  * Changed: updated zh_cn.lang (PR#39 DYColdWind)

---

1.10.17

  * Added: support for fluid ingredients in recipes (#35)

---

1.9.17
  * Added: de_de.lang (#33 Xaikii)
  * Changed: updated zh_cn.lang (PR#32 DYColdWind)

---

1.9.16

  * Added: Worktable tab paging arrows!
    * You can now page between more than six joined worktables with tab arrows
  * Added: Bone tools!
  * Added: New worktables!
    * Scribe's Worktable
    * Chemist's Worktable
  * Added: New tools!
    * Scribe's Compass
    * Scribe's Quill
    * Chemist's Beaker
    * Chemist's Burner
  * Added: Toolbox!
    * Stores only tools used in recipes by default (configurable)
    * Keeps its inventory when broken (configurable)
    * Accessible from any joined worktable when placed adjacent
    * Shift-click tools in toolbox to swap with tool in worktable
    * Shift-click tool in worktable to place in toolbox
  * Added: Tools used in recipes are now searchable in JEI (hover tool and press 'u')
  * Changed: clicking worktable tabs now plays ui click sound
  * Changed: Engineer's Worktable sound type to anvil
  * Changed: juxtaposed material name with profession name in all tool names
  * Changed: disabled tool enchantability
  * Fixed: many z-fighting issues with the generated tool models
  * Note: don't forget to regenerate the config or manually add the new tools and materials to the lists
  * Requires: Athenaeum >= 1.5.3

---

1.8.16

  * Fixed: gamestage ALL logic not behaving as expected (#26)

---

1.8.15
  
  * Fixed: server crash (#25)

---

1.8.14

  * Added: exposed advanced recipe builder syntax to ZenScript, exposes more recipe features - see ZENSCRIPT_ADVANCED.md for syntax
  * Added: new recipe option - unlimited, exclusive (only one will be crafted) weighted outputs 
  * Added: new recipe option - greater flexibility in defining matching gamestages
  * Changed: disabled MouseTweaks wheel in tables
  * Changed: disabled shift-click recipe crafting for the new, multi-output recipes

---

1.7.14

  * Fixed: worktables not saving contents properly (#23)

---

1.7.13

  * Fixed: staged recipes using stage names with capital letters not showing in JEI (#24) 

---

1.7.12

  * Fixed: JEI recipe category name for the Mage's Worktable using wrong translation key

---

1.7.11

  * Changed: code refactor / cleanup
  * Changed: updated zh_cn.lang (PR#20 DYColdWind)

---

1.7.10

  * Added: new tables: engineers worktable, mages worktable
  * Added: new tools: engineers spanner, engineers driver, mages athame, mages grimoire
  * Changed: lightened the wood tool color slightly
  * Note: don't forget to regenerate the config or manually add the new tool types to the list

---

1.6.10

  * Fixed: extra chance drops are now properly linked in JEI; when searching for recipes to make the extra chance drop, it will show the worktable recipe (#14)
  * Fixed: double-click same item as craft result will not cause a craft to happen (#15) 

---

1.6.9

  * Added: support for up to three optional outputs with chance
  * Added: shapeless recipe indicator with tooltip in JEI
  * Changed: updated zh_cn.lang (PR#10 DYColdWind)
  * Note: be sure to checkout the ZENSCRIPT.md link for the added syntax

---

1.5.9

  * Fixed: shaped recipes displaying as shapeless recipes in JEI (#12)

---

1.5.8

  * Added: tools for the Botania materials - Manasteel, Elementium, Terrasteel
  * Changed: migrated GameStages plugin handler to Athenaeum lib
  * Requires: Athenaeum >= 1.3.3
  * Note: don't forget to regenerate the config or manually add the new materials to the list

---

1.4.8

  * Changed: now loads AFTER gamestages, fixes NoClassDefFound crash

---

1.4.7

  * Added: support for staged recipes using GameStages

---

1.3.7

  * Added: GUI tabs to switch between adjacent tables
  * Added: config option to disable select tool types
  * Added: config option to disable select tool materials
  * Removed: config option to disable all Thermal Foundation materials
  * Changed: updated zh_cn.lang (PR#4 DYColdWind)
  * Requires: Athenaeum >= 1.2.3

---

1.2.7

  * Changed: small change to method signature to reflect bugfix in Athenaeum lib, requires at least Athenaeum 1.1.3
  * Changed: If using JEI, requires a version equal to or greater than 4.8.5.139 as it makes use of a feature added in PR#1068 (https://github.com/mezz/JustEnoughItems/pull/1068). This allows transferring as many items as possible into the worktable using JEI's recipe transfer functionality - it will no longer be limited to one set of recipe ingredients by the tool slot.  

---

1.2.6
  
  * Added: all tools are now grouped in OreDict entries by tool type, see zenscript.md

---

1.2.5

  * Fixed: scala dependency crash?

---

1.2.4

  * Added: tools to use in the tables
  * Added: config option to disable all tools
  * Added: config option to disable all tool recipes
  * Added: tools made from Thermal Foundation (TF) materials
  * Added: config option to disable all tools made from TF materials
  * Added: shift + click ingredients into the table
  * Tested the build on a server BEFORE releasing -.-

---

1.1.4

  * Fixed: Tables now work properly with Tinker's tools (#3)

---

1.1.3

  * Fixed server crash (#2)

---

1.1.2

  * Added feature: shift + click to swap tools

---

1.0.2

  * Fixed dependencies

---

1.0.1

  * Fixed requirement issue

---

1.0.0