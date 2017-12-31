package com.codetaylor.mc.artisanworktables;

public class Reference {

  public static final String ATHENAEUM_VERSION = "1.12.2-1.1.2";
  public static final String ATHENAEUM_STRING = "@[" + ATHENAEUM_VERSION + ",)";

  public static final String MOD_ID = "artisanworktables";
  public static final String VERSION = "@@VERSION@@";
  public static final String NAME = "Artisan Worktables";
  public static final String DEPENDENCIES = "required-after:athenaeum;after:crafttweaker;after:jei;";

  public static final boolean IS_DEV = VERSION.equals("@@" + "VERSION" + "@@");
}
