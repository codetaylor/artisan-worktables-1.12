1.6.10:
* Fixed: extra chance drops are now properly linked in JEI; when searching for recipes to make the extra chance drop, it will show the worktable recipe 

1.6.9:
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