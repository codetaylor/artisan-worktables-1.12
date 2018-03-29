package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.ReferenceTexture;
import com.codetaylor.mc.athenaeum.gui.Texture;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class TileEntityDesignerDelegate {

  private ItemStackHandler patternStackHandler;

  public void initialize() {

    this.patternStackHandler = new ItemStackHandler(1);
  }

  public ItemStackHandler getPatternStackHandler() {

    return this.patternStackHandler;
  }

  public List<ItemStack> getBlockBreakDrops(List<ItemStack> drops) {

    for (int i = 0; i < this.patternStackHandler.getSlots(); i++) {
      drops.add(this.patternStackHandler.getStackInSlot(i));
    }

    return drops;
  }

  public void readFromNBT(NBTTagCompound tag) {

    this.patternStackHandler.deserializeNBT(tag.getCompoundTag("patternStackHandler"));
  }

  public NBTTagCompound writeToNBT(NBTTagCompound tag) {

    tag.setTag("patternStackHandler", this.patternStackHandler.serializeNBT());
    return tag;
  }

  public Texture getTexturePatternSide() {

    return ReferenceTexture.TEXTURE_PATTERN_SIDE;
  }
}
