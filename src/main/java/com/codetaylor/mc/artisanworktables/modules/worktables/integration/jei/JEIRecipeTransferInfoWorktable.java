package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.ContainerWorktable;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class JEIRecipeTransferInfoWorktable
    implements IRecipeTransferInfo<ContainerWorktable> {

  private String name;
  private String uid;

  public JEIRecipeTransferInfoWorktable(String name, String uid) {

    this.name = name;
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

    return container.canHandleJEIRecipeTransfer(this.name);
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
