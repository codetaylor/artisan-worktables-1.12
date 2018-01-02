package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.ContainerWorktable;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityWorktableBase;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class JEIRecipeTransferInfoWorktable
    implements IRecipeTransferInfo<ContainerWorktable> {

  private BlockWorktable.EnumType type;
  private String uid;

  public JEIRecipeTransferInfoWorktable(BlockWorktable.EnumType type, String uid) {

    this.type = type;
    this.uid = uid;
  }

  @Override
  public Class<ContainerWorktable> getContainerClass() {

    return ContainerWorktable.class;
  }

  @Override
  public String getRecipeCategoryUid() {

    return this.uid;
  }

  @Override
  public boolean canHandle(ContainerWorktable container) {

    TileEntityWorktableBase tile = container.getTile();
    IBlockState blockState = tile.getWorld().getBlockState(tile.getPos());

    return (blockState.getBlock() == ModuleWorktables.Blocks.WORKTABLE
        && blockState.getValue(BlockWorktable.VARIANT) == this.type);
  }

  @Override
  public List<Slot> getRecipeSlots(ContainerWorktable container) {

    return container.getRecipeSlots(new ArrayList<>());
  }

  @Override
  public List<Slot> getInventorySlots(ContainerWorktable container) {

    return container.getInventorySlots(new ArrayList<>());
  }

  @Override
  public boolean requireCompleteSets() {

    return false;
  }
}
