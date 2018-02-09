package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.Container;
import com.codetaylor.mc.artisanworktables.modules.worktables.reference.EnumTier;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class JEIRecipeTransferInfoWorktable
    implements IRecipeTransferInfo<Container> {

  private String name;
  private String uid;
  private EnumTier tier;

  public JEIRecipeTransferInfoWorktable(
      String name,
      String uid,
      EnumTier tier
  ) {

    this.name = name;
    this.uid = uid;
    this.tier = tier;
  }

  @Override
  public Class<Container> getContainerClass() {

    return Container.class;
  }

  @Override
  public String getRecipeCategoryUid() {

    return this.uid;
  }

  @Override
  public boolean canHandle(Container container) {

    return container.canHandleJEIRecipeTransfer(this.name, this.tier);
  }

  @Override
  public List<Slot> getRecipeSlots(Container container) {

    return container.getRecipeSlots(new ArrayList<>());
  }

  @Override
  public List<Slot> getInventorySlots(Container container) {

    return container.getInventorySlots(new ArrayList<>());
  }

  @Override
  public boolean requireCompleteSets() {

    return false;
  }
}
