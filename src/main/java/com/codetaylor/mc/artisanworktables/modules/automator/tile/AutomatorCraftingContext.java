package com.codetaylor.mc.artisanworktables.modules.automator.tile;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingContext;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

public class AutomatorCraftingContext
    implements ICraftingContext {

  private final World world;
  private final ICraftingMatrixStackHandler craftingMatrixStackHandler;
  private final IItemHandlerModifiable toolItemHandler;
  private final IItemHandler secondaryOutputHandler;
  private final IFluidHandler fluidHandler;
  private final EnumType tableType;
  private final EnumTier tableTier;
  private final BlockPos tablePosition;

  public AutomatorCraftingContext(
      World world,
      ICraftingMatrixStackHandler craftingMatrixStackHandler,
      IItemHandlerModifiable toolItemHandler,
      IItemHandler secondaryOutputHandler,
      IFluidHandler fluidHandler,
      EnumType tableType,
      EnumTier tableTier,
      BlockPos tablePosition
  ) {

    this.world = world;
    this.craftingMatrixStackHandler = craftingMatrixStackHandler;
    this.toolItemHandler = toolItemHandler;
    this.secondaryOutputHandler = secondaryOutputHandler;
    this.fluidHandler = fluidHandler;
    this.tableType = tableType;
    this.tableTier = tableTier;
    this.tablePosition = tablePosition;
  }

  @Override
  public boolean isPattern() {

    return true;
  }

  @Override
  public World getWorld() {

    return this.world;
  }

  @Override
  public EntityPlayer getPlayer() {

    throw new UnsupportedOperationException();
  }

  @Override
  public ICraftingMatrixStackHandler getCraftingMatrixHandler() {

    return this.craftingMatrixStackHandler;
  }

  @Override
  public IItemHandlerModifiable getToolHandler() {

    return this.toolItemHandler;
  }

  @Override
  public IItemHandler getSecondaryOutputHandler() {

    return this.secondaryOutputHandler;
  }

  @Nullable
  @Override
  public IItemHandlerModifiable getSecondaryIngredientHandler() {

    return null;
  }

  @Override
  public IFluidHandler getFluidHandler() {

    return this.fluidHandler;
  }

  @Nullable
  @Override
  public IItemHandler getToolReplacementHandler() {

    return null;
  }

  @Override
  public EnumType getType() {

    return this.tableType;
  }

  @Override
  public EnumTier getTier() {

    return this.tableTier;
  }

  @Override
  public BlockPos getPosition() {

    return this.tablePosition;
  }
}
