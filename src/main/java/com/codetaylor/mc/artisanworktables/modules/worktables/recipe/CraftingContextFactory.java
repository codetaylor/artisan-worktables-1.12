package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingContext;
import com.codetaylor.mc.artisanworktables.api.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityMechanicalToolbox;
import com.codetaylor.mc.artisanworktables.modules.toolbox.tile.TileEntityToolbox;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

public class CraftingContextFactory {

  public static ICraftingContext createContext(
      final TileEntityBase tile,
      final EntityPlayer player,
      @Nullable final IItemHandlerModifiable secondaryIngredientHandler
  ) {

    return new ICraftingContext() {

      @Override
      public World getWorld() {

        return tile.getWorld();
      }

      @Override
      public EntityPlayer getPlayer() {

        return player;
      }

      @Override
      public IItemHandlerModifiable getCraftingMatrixHandler() {

        return tile.getCraftingMatrixHandler();
      }

      @Override
      public IItemHandlerModifiable getToolHandler() {

        return tile.getToolHandler();
      }

      @Override
      public IItemHandler getSecondaryOutputHandler() {

        return tile.getSecondaryOutputHandler();
      }

      @Nullable
      @Override
      public IItemHandlerModifiable getSecondaryIngredientHandler() {

        return secondaryIngredientHandler;
      }

      @Override
      public IFluidHandler getFluidHandler() {

        return tile.getTank();
      }

      @Nullable
      @Override
      public IItemHandler getToolReplacementHandler() {

        TileEntityToolbox adjacentToolbox = tile.getAdjacentToolbox();

        if (adjacentToolbox == null
            || !(adjacentToolbox instanceof TileEntityMechanicalToolbox)) {
          return null;
        }

        return adjacentToolbox.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
      }

      @Override
      public EnumType getType() {

        return tile.getType();
      }

      @Override
      public EnumTier getTier() {

        return tile.getTier();
      }

      @Override
      public BlockPos getPosition() {

        return tile.getPos();
      }
    };

  }

  private CraftingContextFactory() {
    //
  }

}
