package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class VanillaRecipeCache {

  private static final ThreadLocal<Map<ResourceLocation, IArtisanRecipe>> CACHE = ThreadLocal.withInitial(HashMap::new);
  private static final ThreadLocal<IRecipe[]> LAST_RECIPE = ThreadLocal.withInitial(() -> new IRecipe[1]);

  @Nullable
  public synchronized static IArtisanRecipe getArtisanRecipe(InventoryWrapper inventoryWrapper, World world, EnumTier tier) {

    IRecipe recipe;

    recipe = LAST_RECIPE.get()[0];

    if (recipe == null || !recipe.matches(inventoryWrapper, world)) {
      recipe = CraftingManager.findMatchingRecipe(inventoryWrapper, world);
    }

    if (recipe == null) {
      return null;
    }

    LAST_RECIPE.get()[0] = recipe;

    return VanillaRecipeCache.getArtisanRecipeWrapped(recipe, tier);
  }

  @Nullable
  private static IArtisanRecipe getArtisanRecipeWrapped(IRecipe recipe, EnumTier tier) {

    ResourceLocation resourceLocation = recipe.getRegistryName();

    if (resourceLocation == null) {
      return null;
    }

    IArtisanRecipe cachedRecipe = CACHE.get().get(resourceLocation);

    if (cachedRecipe != null) {
      return cachedRecipe;
    }

    NonNullList<Ingredient> ingredients = recipe.getIngredients();
    List<IArtisanIngredient> artisanIngredients = new ArrayList<>(ingredients.size());

    for (Ingredient ingredient : ingredients) {
      artisanIngredients.add(ArtisanIngredient.from(ingredient));
    }

    ExtraOutputChancePair[] extraOutputChancePairs = new ExtraOutputChancePair[3];
    Arrays.fill(extraOutputChancePairs, new ExtraOutputChancePair(ArtisanItemStack.EMPTY, 0));

    ArtisanRecipe artisanRecipe = new ArtisanRecipe(
        null,
        Collections.emptyMap(),
        Collections.singletonList(new OutputWeightPair(ArtisanItemStack.from(recipe.getRecipeOutput().copy()), 1)),
        new ToolEntry[0],
        artisanIngredients,
        Collections.emptyList(),
        false,
        null,
        0,
        0,
        false,
        extraOutputChancePairs,
        recipe instanceof IShapedRecipe ? IRecipeMatrixMatcher.SHAPED : IRecipeMatrixMatcher.SHAPELESS,
        true,
        tier == EnumTier.WORKSHOP ? 5 : 3,
        tier == EnumTier.WORKSHOP ? 5 : 3,
        EnumTier.WORKTABLE.getId(),
        EnumTier.WORKSHOP.getId(),
        false
    );

    CACHE.get().put(resourceLocation, artisanRecipe);
    return artisanRecipe;
  }

  private VanillaRecipeCache() {
    //
  }

  // ---------------------------------------------------------------------------
// - Inventory
// ---------------------------------------------------------------------------

  public static class InventoryWrapper
      extends InventoryCrafting {

    private final TileEntityBase tile;

    public InventoryWrapper(TileEntityBase tile) {

      super(
          new Container() {

            @Override
            public boolean canInteractWith(@Nonnull EntityPlayer player) {

              return true;
            }
          },
          (tile.getTier() == EnumTier.WORKSHOP) ? 5 : 3,
          (tile.getTier() == EnumTier.WORKSHOP) ? 5 : 3
      );

      this.tile = tile;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {

      if (index >= this.getSizeInventory()) {
        return ItemStack.EMPTY;
      }

      return this.tile.getCraftingMatrixHandler().getStackInSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {

      this.tile.getCraftingMatrixHandler().setStackInSlot(index, stack);
    }

    @Nonnull
    @Override
    public ItemStack getStackInRowAndColumn(int x, int y) {

      int size = (this.tile.getTier() == EnumTier.WORKSHOP) ? 5 : 3;

      if (x >= 0 && x < size && y >= 0 && y < size) {
        int index = x + y * size;
        return this.tile.getCraftingMatrixHandler().getStackInSlot(index);
      }

      return ItemStack.EMPTY;
    }

    @Override
    public void markDirty() {

      this.tile.markDirty();
    }
  }
}
