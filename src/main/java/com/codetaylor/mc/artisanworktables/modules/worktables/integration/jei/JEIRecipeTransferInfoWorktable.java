package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.AWContainer;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class JEIRecipeTransferInfoWorktable
    implements IRecipeTransferInfo<AWContainer> {

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
  public Class<AWContainer> getContainerClass() {

    return AWContainer.class;
  }

  @Override
  public String getRecipeCategoryUid() {

    return this.uid;
  }

  @Override
  public boolean canHandle(AWContainer container) {

    return container.canHandleRecipeTransferJEI(this.name, this.tier);
  }

  @Override
  public List<Slot> getRecipeSlots(AWContainer container) {

    return container.getRecipeSlotsJEI(new ArrayList<>(), this.tier == EnumTier.WORKSHOP ? 5 : 3);
  }

  @Override
  public List<Slot> getInventorySlots(AWContainer container) {

    return container.getInventorySlotsJEI(new ArrayList<>());
  }

  @Override
  public boolean requireCompleteSets() {

    return false;
  }
}
