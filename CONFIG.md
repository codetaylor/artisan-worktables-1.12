`artisanworktables.module.Toolbox.cfg`:

```
# Configuration file

general {
    # Set to false to disable the toolbox.
    B:ENABLE_MODULE=true

    toolbox {
        # Set to false to remove the toolbox.
        B:ENABLED=true

        # Set to false to prevent the toolbox from keeping its contents when broken.
        B:KEEP_CONTENTS_WHEN_BROKEN=true

        # Set to false to allow any item to be placed into the toolbox.
        # If set to true, only tools that are part of any worktable recipe may be stored in the toolbox.
        B:RESTRICT_TO_TOOLS_ONLY=true
    }

    mechanical_toolbox {
        # Set to false to remove the mechanical toolbox.
        B:ENABLED=true

        # Set to false to prevent the mechanical toolbox from keeping its contents when broken.
        B:KEEP_CONTENTS_WHEN_BROKEN=true

        # Set to false to allow any item to be placed into the mechanical toolbox.
        # If set to true, only tools that are part of any worktable recipe may be stored in the mechanical toolbox.
        B:RESTRICT_TO_TOOLS_ONLY=true
    }

}
```

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
        bone
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
        chemists_beaker
        chemists_burner
        engineers_driver
        engineers_spanner
        jewelers_gemcutter
        jewelers_pliers
        mages_athame
        mages_grimoire
        masons_chisel
        masons_trowel
        scribes_compass
        scribes_quill
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

`artisanworktables.module.Worktables.cfg`:

```
# Configuration file

general {
    # If set to true, crafting tools must have sufficient durability remaining to perform the craft.
    # If set to false, this restriction is ignored.
    B:RESTRICT_CRAFT_MINIUMUM_DURABILITY=true

    fluid_capacity {
        # Worktable fluid capacity (milli-buckets).
        I:BASIC=4000

        # Worktable fluid capacity (milli-buckets).
        I:BLACKSMITH=4000

        # Worktable fluid capacity (milli-buckets).
        I:CARPENTER=4000

        # Worktable fluid capacity (milli-buckets).
        I:CHEMIST=4000

        # Worktable fluid capacity (milli-buckets).
        I:ENGINEER=4000

        # Worktable fluid capacity (milli-buckets).
        I:JEWELER=4000

        # Worktable fluid capacity (milli-buckets).
        I:MAGE=4000

        # Worktable fluid capacity (milli-buckets).
        I:MASON=4000

        # Worktable fluid capacity (milli-buckets).
        I:SCRIBE=4000

        # Worktable fluid capacity (milli-buckets).
        I:TAILOR=4000
    }

}
```