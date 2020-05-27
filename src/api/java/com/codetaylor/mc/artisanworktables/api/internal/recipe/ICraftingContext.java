package com.codetaylor.mc.artisanworktables.api.internal.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public interface ICraftingContext {

  World getWorld();

  Optional<EntityPlayer> getPlayer();

  ICraftingMatrixStackHandler getCraftingMatrixHandler();

  IItemHandlerModifiable getToolHandler();

  IItemHandler getSecondaryOutputHandler();

  @Nullable
  IItemHandlerModifiable getSecondaryIngredientHandler();

  IFluidHandler getFluidHandler();

  @Nullable
  IItemHandler getToolReplacementHandler();

  EnumType getType();

  EnumTier getTier();

  BlockPos getPosition();

  default boolean swapOutputWithCursorItem() {

    return true;
  }

}
