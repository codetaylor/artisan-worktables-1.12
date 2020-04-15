package com.codetaylor.mc.artisanworktables.modules.worktables.integration.craftingtweaks;

import com.codetaylor.mc.artisanworktables.ModArtisanWorktables;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.AWContainer;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerWorkshop;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.VanillaRecipeCache;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.blay09.mods.craftingtweaks.api.DefaultProviderV2;
import net.blay09.mods.craftingtweaks.api.TweakProvider;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class CraftingTweaksPlugin {

  public static void init() {

    CraftingTweaksAPI.registerProvider(AWContainer.class, new AWTweakProvider());
  }

  public static class AWTweakProvider
      implements TweakProvider<AWContainer> {

    private DefaultProviderV2 defaultProvider;

    /* package */ AWTweakProvider() {

      this.defaultProvider = CraftingTweaksAPI.createDefaultProviderV2();
    }

    @Nonnull
    @Override
    public String getModId() {

      return ModArtisanWorktables.MOD_ID;
    }

    @Override
    public boolean load() {

      return true;
    }

    @Override
    public int getCraftingGridStart(EntityPlayer entityPlayer, AWContainer container, int id) {

      return container.slotIndexCraftingMatrixStart;
    }

    @Override
    public int getCraftingGridSize(EntityPlayer entityPlayer, AWContainer container, int id) {

      return (container.getTile().getTier() == EnumTier.WORKSHOP) ? 25 : 9;
    }

    @Override
    public void clearGrid(EntityPlayer entityPlayer, AWContainer container, int id, boolean forced) {

      this.defaultProvider.clearGrid(this, id, entityPlayer, container, false, forced);
    }

    @Override
    public void rotateGrid(EntityPlayer entityPlayer, AWContainer container, int id, boolean counterClockwise) {

      EnumTier tier = container.getTile().getTier();

      if (tier == EnumTier.WORKSHOP) {
        // TODO special handling

      } else {
        this.defaultProvider.rotateGrid(this, id, entityPlayer, container, counterClockwise);
      }
    }

    @Override
    public void balanceGrid(EntityPlayer entityPlayer, AWContainer container, int id) {

      this.defaultProvider.balanceGrid(this, id, entityPlayer, container);
    }

    @Override
    public void spreadGrid(EntityPlayer entityPlayer, AWContainer container, int id) {

      this.defaultProvider.spreadGrid(this, id, entityPlayer, container);
    }

    @Override
    public boolean canTransferFrom(EntityPlayer entityPlayer, AWContainer container, int id, Slot sourceSlot) {

      return this.defaultProvider.canTransferFrom(entityPlayer, container, sourceSlot);
    }

    @Override
    public boolean transferIntoGrid(EntityPlayer entityPlayer, AWContainer container, int id, Slot sourceSlot) {

      return this.defaultProvider.transferIntoGrid(this, id, entityPlayer, container, sourceSlot);
    }

    @Override
    public ItemStack putIntoGrid(EntityPlayer entityPlayer, AWContainer container, int id, ItemStack itemStack, int index) {

      return this.defaultProvider.putIntoGrid(this, id, entityPlayer, container, itemStack, index);
    }

    @Override
    public IInventory getCraftMatrix(EntityPlayer entityPlayer, AWContainer container, int id) {

      return new VanillaRecipeCache.InventoryWrapper(container.getTile());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initGui(GuiContainer guiContainer, List<GuiButton> buttonList) {

      if (!((guiContainer instanceof GuiContainerWorkshop))) {
        int buttonX = -10;
        int buttonY = 0 + 17;
        buttonList.add(CraftingTweaksAPI.createRotateButtonRelative(0, guiContainer, buttonX, buttonY));
      }

      {
        int buttonX = -10;
        int buttonY = 18 + 17;
        buttonList.add(CraftingTweaksAPI.createBalanceButtonRelative(0, guiContainer, buttonX, buttonY));
      }

      {
        int buttonX = -10;
        int buttonY = 36 + 17;
        buttonList.add(CraftingTweaksAPI.createClearButtonRelative(0, guiContainer, buttonX, buttonY));
      }
    }
  }

}
