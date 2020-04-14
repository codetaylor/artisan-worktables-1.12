package com.codetaylor.mc.artisanworktables.modules.worktables.integration.jei;

import com.codetaylor.mc.artisanworktables.modules.worktables.gui.AWContainer;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class JEIRecipeTransferInfoVanilla
    implements IRecipeTransferInfo<AWContainer> {

  private String name;
  private String uid;

  public JEIRecipeTransferInfoVanilla() {

    this.name = "vanilla";
    this.uid = VanillaRecipeCategoryUid.CRAFTING;
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

    return container.canHandleRecipeTransferJEI(this.name, null);
  }

  @Override
  public List<Slot> getRecipeSlots(AWContainer container) {

    return container.getRecipeSlotsJEI(new ArrayList<>(), 3);
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
