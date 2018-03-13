package com.codetaylor.mc.artisanworktables.api;

import com.codetaylor.mc.artisanworktables.api.internal.ModuleWorktablesAPINoOp;

public class ArtisanAPI {

  private static final IModuleWorktablesAPI MODULE_WORKTABLES_INSTANCE = new ModuleWorktablesAPINoOp();

  public static IModuleWorktablesAPI getModuleWorktablesInstance() {

    return MODULE_WORKTABLES_INSTANCE;
  }

}
