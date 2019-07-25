package com.codetaylor.mc.artisanworktables.api;

import com.codetaylor.mc.artisanworktables.api.recipe.DefaultToolHandler;
import com.codetaylor.mc.artisanworktables.api.recipe.IToolHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArtisanToolHandlers {

  private static final IToolHandler DEFAULT_HANDLER = DefaultToolHandler.INSTANCE;
  private static final List<IToolHandler> HANDLERS = new ArrayList<>();

  public static void register(IToolHandler toolHandler) {

    HANDLERS.add(toolHandler);
  }

  public static IToolHandler get(ItemStack itemStack) {

    for (IToolHandler handler : HANDLERS) {

      if (handler.matches(itemStack)) {
        return handler;
      }
    }

    return DEFAULT_HANDLER;
  }
}
