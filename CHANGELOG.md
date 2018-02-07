1.13.21
* Added: Workstations!
  * Workstations are second tier worktables. They can support recipes that use up to two tools and nine secondary ingredients. All recipes share the same recipe pool, therefore, any recipe defined for a worktable will also work in a workstation. If a recipe is defined that uses two tools or one to nine secondary ingredients, it will only be craftable in the new workstations. 
* Added: new ZenScript builder methods
  * `addTool(IIngredient, int)`
  * `setSecondaryIngredients(IIngredient[])`
* Added: fr_fr.lang (PR#56 Okii35)
* Changed: deprecated ZenScript builder methods
  * `setTool(IIngredient, int)` - use `addTool(IIngredient, int)` instead
* Changed: updated de_de.lang (PR#58 Xaikii)
* Note: you will need to refresh the config for the worktable module

1.12.21
* Fixed: crash with liquid in GUI (or invisible items w/ optifine) (#52)
* Fixed: tertiary and quaternary chanced outputs not set properly with legacy zen methods
* Changed: refactor of the gui / container code (internal prep)
* Changed: changed recipes to handle multiple tools (internal prep)
* Note: This is being released as an alpha because it changes several internal systems. New features will be built using the new changes and it is important to quickly catch and eliminate any bugs that have been introduced by the refactoring. If you would like to help speed the development process and ensure a stable product, please report any issues you find with this version. Thank you!
* Requires: Athenaeum >= 1.7.4

1.11.21
* Fixed: fluid cleared via GUI remains after re-load (#49)
* Changed: updated de_de.lang (PR#46 Xaikii) 

1.11.20
* Added: Mechanical Toolbox!
  * This new toolbox will automatically swap old tools for new tools. When a tool is broken,
    or lacks sufficient durability to complete the last successful craft, the Mechanical
    Toolbox will swap a new, appropriate tool into the worktable's tool slot.
* Added: config option to require sufficient tool durability (#44)
* Added: durability tooltip to all tools from this mod
* Added: config option to disable the durability tooltip
* Note: you will probably need to refresh the config for the tools and the toolbox

1.10.20
* Fixed: shift-click tool swap eats tools of the same type (#45)

1.10.19
* Fixed: GUI fluid tank render bug (#40)
* Fixed: Non-interactive fluid display in JEI (#42)
* Changed: updated de_de.lang (PR#43 Xaikii)

1.10.18
* Fixed: NPE crash when using any recipe without a fluid ingredient (#41)
* Changed: updated zh_cn.lang (PR#39 DYColdWind)

1.10.17
* Added: support for fluid ingredients in recipes (#35)

1.9.17
* Added: de_de.lang (#33 Xaikii)
* Changed: updated zh_cn.lang (PR#32 DYColdWind)

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

1.8.16
* Fixed: gamestage ALL logic not behaving as expected (#26)

1.8.15
* Fixed: server crash (#25)

1.8.14
* Added: exposed advanced recipe builder syntax to ZenScript, exposes more recipe features - see ZENSCRIPT_ADVANCED.md for syntax
* Added: new recipe option - unlimited, exclusive (only one will be crafted) weighted outputs 
* Added: new recipe option - greater flexibility in defining matching gamestages
* Changed: disabled MouseTweaks wheel in tables
* Changed: disabled shift-click recipe crafting for the new, multi-output recipes

1.7.14
* Fixed: worktables not saving contents properly (#23)

1.7.13
* Fixed: staged recipes using stage names with capital letters not showing in JEI (#24) 

1.7.12
* Fixed: JEI recipe category name for the Mage's Worktable using wrong translation key

1.7.11
* Changed: code refactor / cleanup
* Changed: updated zh_cn.lang (PR#20 DYColdWind)

1.7.10
* Added: new tables: engineers worktable, mages worktable
* Added: new tools: engineers spanner, engineers driver, mages athame, mages grimoire
* Changed: lightened the wood tool color slightly
* Note: don't forget to regenerate the config or manually add the new tool types to the list

1.6.10
* Fixed: extra chance drops are now properly linked in JEI; when searching for recipes to make the extra chance drop, it will show the worktable recipe (#14)
* Fixed: double-click same item as craft result will not cause a craft to happen (#15) 

1.6.9
* Added: support for up to three optional outputs with chance
* Added: shapeless recipe indicator with tooltip in JEI
* Changed: updated zh_cn.lang (PR#10 DYColdWind)
* Note: be sure to checkout the ZENSCRIPT.md link for the added syntax

1.5.9
* Fixed: shaped recipes displaying as shapeless recipes in JEI (#12)

1.5.8
* Added: tools for the Botania materials - Manasteel, Elementium, Terrasteel
* Changed: migrated GameStages plugin handler to Athenaeum lib
* Requires: Athenaeum >= 1.3.3
* Note: don't forget to regenerate the config or manually add the new materials to the list

1.4.8
* Changed: now loads AFTER gamestages, fixes NoClassDefFound crash

1.4.7
* Added: support for staged recipes using GameStages

1.3.7
* Added: GUI tabs to switch between adjacent tables
* Added: config option to disable select tool types
* Added: config option to disable select tool materials
* Removed: config option to disable all Thermal Foundation materials
* Changed: updated zh_cn.lang (PR#4 DYColdWind)
* Requires: Athenaeum >= 1.2.3

1.2.7
* Changed: small change to method signature to reflect bugfix in Athenaeum lib, requires at least Athenaeum 1.1.3
* Changed: If using JEI, requires a version equal to or greater than 4.8.5.139 as it makes use of a feature added in PR#1068 (https://github.com/mezz/JustEnoughItems/pull/1068). This allows transferring as many items as possible into the worktable using JEI's recipe transfer functionality - it will no longer be limited to one set of recipe ingredients by the tool slot.  

1.2.6
* Added: all tools are now grouped in OreDict entries by tool type, see zenscript.md

1.2.5
* Fixed: scala dependency crash?

1.2.4
* Added: tools to use in the tables
* Added: config option to disable all tools
* Added: config option to disable all tool recipes
* Added: tools made from Thermal Foundation (TF) materials
* Added: config option to disable all tools made from TF materials
* Added: shift + click ingredients into the table
* Tested the build on a server BEFORE releasing -.-

1.1.4
* Fixed: Tables now work properly with Tinker's tools (#3)

1.1.3
* Fixed server crash (#2)

1.1.2
* Added feature: shift + click to swap tools

1.0.2
* Fixed dependencies

1.0.1
* Fixed requirement issue

1.0.0