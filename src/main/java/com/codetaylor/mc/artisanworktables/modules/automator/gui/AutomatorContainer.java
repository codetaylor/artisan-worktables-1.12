package com.codetaylor.mc.artisanworktables.modules.automator.gui;

import com.codetaylor.mc.artisanworktables.modules.automator.gui.slot.PanelSlot;
import com.codetaylor.mc.artisanworktables.modules.automator.tile.TileAutomator;
import com.codetaylor.mc.athenaeum.gui.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class AutomatorContainer
    extends ContainerBase {

  private final World world;
  private final TileAutomator tile;

  public enum EnumState {
    Gear(0), Pattern(1), Inventory(2), Fluid(3), Tool(4);

    private final int index;

    EnumState(int index) {

      this.index = index;
    }

    public int getIndex() {

      return this.index;
    }

    public static EnumState fromIndex(int index) {

      for (EnumState value : EnumState.values()) {
        if (value.index == index) {
          return value;
        }
      }
      throw new IllegalArgumentException("Unknown index: " + index);
    }
  }

  private EnumState state;

  public AutomatorContainer(
      InventoryPlayer inventoryPlayer,
      World world,
      TileAutomator tile
  ) {

    super(inventoryPlayer);
    this.world = world;
    this.tile = tile;

    this.state = EnumState.Gear;

    this.containerPlayerInventoryAdd();
    this.containerPlayerHotbarAdd();

    this.containerSlotAdd(new PanelSlot(
        () -> this.state, EnumState.Gear,
        this.tile.getTableItemStackHandler(), 0, 26, 56
    ));

    for (int i = 0; i < 9; i++) {
      this.containerSlotAdd(new PanelSlot(
          () -> this.state, EnumState.Pattern,
          this.tile.getPatternItemStackHandler(), i, 8 + (i * 18), 38
      ));
    }
  }

  @Override
  protected int containerInventoryPositionGetY() {

    return 108;
  }

  @Override
  protected int containerHotbarPositionGetY() {

    return 166;
  }

  public boolean setState(EnumState state) {

    if (this.state != state) {
      this.state = state;
      return true;
    }

    return false;
  }

  public EnumState getState() {

    return this.state;
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {

    return player.getDistanceSq((double) this.tile.getPos().getX() + 0.5D, (double) this.tile.getPos().getY() + 0.5D, (double) this.tile.getPos().getZ() + 0.5D) <= 64.0D;
  }
}
