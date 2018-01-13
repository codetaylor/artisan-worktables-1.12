`artisanworktables.module.Tools.cfg`:

```
# Configuration file

general {
    # To disable a tool material, remove it from this list.
    # If new tool materials are added to the mod, you may have to regenerate your config file
    # or manually add them to this list to activate them.
    S:ENABLED_TOOL_MATERIALS <
        wood
        stone
        iron
        gold
        diamond
        flint
        aluminum
        bronze
        constantan
        copper
        electrum
        invar
        lead
        nickel
        platinum
        silver
        steel
        tin
        manasteel
        elementium
        terrasteel
     >

    # To disable a tool type, remove it from this list.
    # If new tool types are added to the mod, you may have to regenerate your config file
    # or manually add them to this list to activate them.
    S:ENABLED_TOOL_TYPES <
        blacksmiths_cutters
        blacksmiths_hammer
        carpenters_hammer
        carpenters_handsaw
        engineers_driver
        engineers_spanner
        jewelers_gemcutter
        jewelers_pliers
        mages_athame
        mages_grimoire
        masons_chisel
        masons_trowel
        tailors_needle
        tailors_shears
     >

    # Set to false to disable all tools.
    # This supersedes all other tool settings.
    B:ENABLE_MODULE=true

    # Set to false to disable all tool recipes.
    B:ENABLE_TOOL_RECIPES=true
}
```