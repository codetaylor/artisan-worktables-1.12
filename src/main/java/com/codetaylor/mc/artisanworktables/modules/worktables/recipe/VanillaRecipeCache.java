package com.codetaylor.mc.artisanworktables.modules.worktables.recipe;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.recipe.ArtisanRecipe;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTArtisanIngredient;
import com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker.CTArtisanRecipe;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import crafttweaker.api.recipes.IRecipeFunction;
import crafttweaker.mc1120.item.VanillaIngredient;
import crafttweaker.mc1120.recipes.MCRecipeBase;
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
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class VanillaRecipeCache {

  private static final ThreadLocal<Map<ResourceLocation, IArtisanRecipe>> CACHE = ThreadLocal.withInitial(HashMap::new);
  private static final ThreadLocal<IRecipe[]> LAST_RECIPE = ThreadLocal.withInitial(() -> new IRecipe[1]);

  private static MethodHandle mcRecipeBase$recipeFunctionGetter;

  static {
    try {
      mcRecipeBase$recipeFunctionGetter = MethodHandles.lookup().unreflectGetter(
          MCRecipeBase.class.getDeclaredField("recipeFunction")
      );

    } catch (IllegalAccessException | NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

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

    ExtraOutputChancePair[] extraOutputChancePairs = new ExtraOutputChancePair[3];
    Arrays.fill(extraOutputChancePairs, new ExtraOutputChancePair(ArtisanItemStack.EMPTY, 0));

    boolean isShaped = (recipe instanceof IShapedRecipe);
    int size = (isShaped) ? (tier == EnumTier.WORKSHOP ? 5 : 3) : 0;

    IArtisanRecipe artisanRecipe;

    if (recipe instanceof MCRecipeBase) {
      MCRecipeBase mcRecipeBase = (MCRecipeBase) recipe;

      for (Ingredient ingredient : ingredients) {

        if (ingredient instanceof VanillaIngredient) {
          artisanIngredients.add(CTArtisanIngredient.from(((VanillaIngredient) ingredient).getIngredient()));

        } else {
          artisanIngredients.add(ArtisanIngredient.from(ingredient));
        }
      }

      artisanRecipe = VanillaRecipeCache.createCTArtisanRecipe(recipe, artisanIngredients, extraOutputChancePairs, isShaped, size, mcRecipeBase);

      if (artisanRecipe == null) {
        return null;
      }

    } else {

      for (Ingredient ingredient : ingredients) {
        artisanIngredients.add(ArtisanIngredient.from(ingredient));
      }

      artisanRecipe = VanillaRecipeCache.createArtisanRecipe(recipe, artisanIngredients, extraOutputChancePairs, isShaped, size);
    }

    CACHE.get().put(resourceLocation, artisanRecipe);
    return artisanRecipe;
  }

  private static IArtisanRecipe createCTArtisanRecipe(IRecipe recipe, List<IArtisanIngredient> artisanIngredients, ExtraOutputChancePair[] extraOutputChancePairs, boolean isShaped, int size, MCRecipeBase mcRecipeBase) {

    IArtisanRecipe artisanRecipe;
    try {
      artisanRecipe = new CTArtisanRecipe(
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
          isShaped ? IRecipeMatrixMatcher.SHAPED : IRecipeMatrixMatcher.SHAPELESS,
          true,
          size,
          size,
          EnumTier.WORKTABLE.getId(),
          EnumTier.WORKSHOP.getId(),
          mcRecipeBase.getRecipeAction(),
          mcRecipeBase$recipeFunctionGetter == null ? null : (IRecipeFunction) mcRecipeBase$recipeFunctionGetter.invokeExact(recipe),
          false
      );

    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return null;
    }
    return artisanRecipe;
  }

  private static ArtisanRecipe createArtisanRecipe(IRecipe recipe, List<IArtisanIngredient> artisanIngredients, ExtraOutputChancePair[] extraOutputChancePairs, boolean isShaped, int size) {

    return new ArtisanRecipe(
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
        isShaped ? IRecipeMatrixMatcher.SHAPED : IRecipeMatrixMatcher.SHAPELESS,
        true,
        size,
        size,
        EnumTier.WORKTABLE.getId(),
        EnumTier.WORKSHOP.getId(),
        false
    );
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
