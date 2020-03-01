package com.codetaylor.mc.artisanworktables.modules.worktables.item;

import com.codetaylor.mc.artisanworktables.api.internal.reference.Tags;
import com.codetaylor.mc.artisanworktables.api.pattern.IItemDesignPattern;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.athenaeum.util.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemDesignPattern
    extends Item
    implements IItemDesignPattern {

  public static final String NAME = "design_pattern";
  public static final String NAME_BLANK = "blank";
  public static final String NAME_WRITTEN = "written";

  @Nonnull
  @Override
  public String getUnlocalizedName(ItemStack stack) {

    if (this.hasRecipe(stack)) {
      return super.getUnlocalizedName(stack) + "." + NAME_WRITTEN;
    }

    return super.getUnlocalizedName(stack) + "." + NAME_BLANK;
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

    if (!world.isRemote && player.isSneaking() && ModuleWorktablesConfig.PATTERN.ENABLE_SNEAK_CLICK_TO_CLEAR) {
      return new ActionResult<>(
          EnumActionResult.SUCCESS,
          new ItemStack(ModuleWorktables.Items.DESIGN_PATTERN, player.getHeldItem(hand).getCount())
      );
    }

    return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
  }

  @Override
  public boolean hasRecipe(ItemStack itemStack) {

    return (this.getRecipeName(itemStack) != null);
  }

  @Nullable
  @Override
  public String getRecipeName(ItemStack itemStack) {

    if (itemStack.getItem() == this) {
      NBTTagCompound tagCompound = StackHelper.getTagSafe(itemStack);

      if (tagCompound.hasKey(Tags.TAG_ARTISAN_WORKTABLES)) {
        NBTTagCompound innerTagCompound = tagCompound.getCompoundTag(Tags.TAG_ARTISAN_WORKTABLES);

        if (innerTagCompound.hasKey(Tags.TAG_RECIPE_NAME)) {
          return innerTagCompound.getString(Tags.TAG_RECIPE_NAME);
        }
      }
    }

    return null;
  }

  @Override
  public void setRecipeName(ItemStack itemStack, String recipeName) {

    if (itemStack.getItem() == this) {
      NBTTagCompound tagCompound = StackHelper.getTagSafe(itemStack);
      NBTTagCompound innerTagCompound = tagCompound.getCompoundTag(Tags.TAG_ARTISAN_WORKTABLES);
      innerTagCompound.setString(Tags.TAG_RECIPE_NAME, recipeName);
      tagCompound.setTag(Tags.TAG_ARTISAN_WORKTABLES, innerTagCompound);
    }
  }
}
