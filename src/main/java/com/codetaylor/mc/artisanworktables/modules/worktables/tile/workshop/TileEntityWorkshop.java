package com.codetaylor.mc.artisanworktables.modules.worktables.tile.workshop;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.GuiContainerWorkshop;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.CraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntitySecondaryInputBase;
import com.codetaylor.mc.athenaeum.inventory.ObservableStackHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWorkshop
    extends TileEntitySecondaryInputBase {

  @SuppressWarnings("unused")
  public TileEntityWorkshop() {
    // serialization
  }

  public TileEntityWorkshop(
      EnumType type
  ) {

    super(type);
  }

  @Override
  protected ResourceLocation getGuiBackgroundTexture() {

    return new ResourceLocation(
        ModuleWorktables.MOD_ID,
        String.format(ModuleWorktables.Textures.WORKSHOP_GUI, this.getType().getName())
    );
  }

  @Override
  protected String getTableTitleKey() {

    return String.format(ModuleWorktables.Lang.WORKSHOP_TITLE, this.getWorktableName());
  }

  @Override
  public EnumTier getTier() {

    return EnumTier.WORKSHOP;
  }

  @Override
  protected CraftingMatrixStackHandler createCraftingMatrixHandler() {

    return new CraftingMatrixStackHandler(5, 5);
  }

  @Override
  protected ObservableStackHandler createSecondaryOutputHandler() {

    return new ObservableStackHandler(3);
  }

  @Override
  protected int getFluidTankCapacity(EnumType type) {

    return ModuleWorktablesConfig.FLUID_CAPACITY_WORKSHOP.get(type.getName());
  }

  @Override
  protected ObservableStackHandler createToolHandler() {

    return new ObservableStackHandler(3);
  }

  @Override
  protected int getSecondaryInputSlotCount() {

    return 9;
  }

  @Override
  public int getMaximumDisplayedTabCount() {

    return 6;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public GuiContainerBase getGuiContainer(
      InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos
  ) {

    return new GuiContainerWorkshop(
        this.getContainer(inventoryPlayer, world, state, pos),
        this.getGuiBackgroundTexture(),
        this.getTableTitleKey(),
        this.getGuiTextShadowColor(),
        this,
        176,
        225
    );
  }

  @Override
  public boolean allowTabs() {

    return ModuleWorktablesConfig.ENABLE_TABS_WORKSHOPS;
  }
}
