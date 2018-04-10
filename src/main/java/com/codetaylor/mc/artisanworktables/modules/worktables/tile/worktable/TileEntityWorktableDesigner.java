package com.codetaylor.mc.artisanworktables.modules.worktables.tile.worktable;

import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumType;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.TileEntityDesignerDelegate;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.ITileEntityDesigner;
import com.codetaylor.mc.athenaeum.gui.Texture;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class TileEntityWorktableDesigner
    extends TileEntityWorktable
    implements ITileEntityDesigner {

  private TileEntityDesignerDelegate delegate;

  public TileEntityWorktableDesigner() {

    super(EnumType.DESIGNER);
  }

  @Override
  protected void initialize(EnumType type) {

    super.initialize(type);
    this.delegate = new TileEntityDesignerDelegate();
    this.delegate.initialize();
  }

  @Override
  public ItemStackHandler getPatternStackHandler() {

    return this.delegate.getPatternStackHandler();
  }

  @Override
  public List<ItemStack> getBlockBreakDrops() {

    return this.delegate.getBlockBreakDrops(super.getBlockBreakDrops());
  }

  @Override
  public Texture getTexturePatternSide() {

    return this.delegate.getTexturePatternSide();
  }

  @Override
  public TileEntity getTileEntity() {

    return this;
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {

    super.readFromNBT(tag);
    this.delegate.readFromNBT(tag);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) {

    tag = super.writeToNBT(tag);
    this.delegate.writeToNBT(tag);
    return tag;
  }
}
