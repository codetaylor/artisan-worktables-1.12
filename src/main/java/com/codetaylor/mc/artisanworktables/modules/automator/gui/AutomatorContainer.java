package com.codetaylor.mc.artisanworktables.modules.automator.gui;

import com.codetaylor.mc.athenaeum.gui.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class AutomatorContainer
    extends ContainerBase {

  private final World world;
  private final TileEntity tile;

  public enum State {
    Gear(0), Pattern(1), Inventory(2), Fluid(3), Tool(4);

    private final int index;

    State(int index) {

      this.index = index;
    }

    public int getIndex() {

      return this.index;
    }

    public static State fromIndex(int index) {

      for (State value : State.values()) {
        if (value.index == index) {
          return value;
        }
      }
      throw new IllegalArgumentException("Unknown index: " + index);
    }
  }

  private State state;

  public AutomatorContainer(
      InventoryPlayer inventoryPlayer,
      World world,
      TileEntity tile
  ) {

    super(inventoryPlayer);
    this.world = world;
    this.tile = tile;

    this.state = State.Gear;
  }

  public boolean setState(State state) {

    if (this.state != state) {
      this.state = state;
      return true;
    }

    return false;
  }

  public State getState() {

    return this.state;
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {

    return player.getDistanceSq((double) this.tile.getPos().getX() + 0.5D, (double) this.tile.getPos().getY() + 0.5D, (double) this.tile.getPos().getZ() + 0.5D) <= 64.0D;
  }
}
