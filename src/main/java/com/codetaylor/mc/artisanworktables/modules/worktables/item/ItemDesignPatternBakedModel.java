package com.codetaylor.mc.artisanworktables.modules.worktables.item;

import com.blamejared.ctgui.api.GuiBase;
import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.modules.worktables.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

public class ItemDesignPatternBakedModel
    implements IBakedModel {

  private final IBakedModel delegate;

  public ItemDesignPatternBakedModel(IBakedModel delegate) {

    this.delegate = delegate;
  }

  @Nonnull
  @Override
  public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType) {

    TRSRTransformation transform = Util.getDefaultItemTransforms().get(cameraTransformType);

    return Pair.of(this, transform == null ? Util.EMPTY_MATRIX_TRANSFORM : transform.getMatrix());
  }

  @Nonnull
  @Override
  public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {

    return this.delegate.getQuads(state, side, rand);
  }

  @Override
  public boolean isAmbientOcclusion() {

    return this.delegate.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {

    return this.delegate.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {

    return this.delegate.isBuiltInRenderer();
  }

  @Nonnull
  @Override
  public TextureAtlasSprite getParticleTexture() {

    return this.delegate.getParticleTexture();
  }

  @Nonnull
  @Override
  public ItemOverrideList getOverrides() {

    return new ItemOverrideList(this.delegate.getOverrides().getOverrides()) {

      @Nonnull
      @Override
      public IBakedModel handleItemState(
          @Nonnull IBakedModel originalModel,
          ItemStack stack,
          @Nullable World world,
          @Nullable EntityLivingBase entity
      ) {

        NBTTagCompound tag = stack.getTagCompound();

        if (tag != null) {
          String recipeName = tag.getString("recipe");

          if (!recipeName.isEmpty()) {
            IArtisanRecipe recipe = ArtisanAPI.getRecipe(recipeName);

            if (recipe != null && ItemDesignPatternBakedModel.canDisplayPatternOutput(stack)) {

              // Retrieving the base output from the recipe here requires a crafting context
              // that we don't have. This context is required for recipe functions that may
              // alter the output of the recipe.
              //
              // Either we restrict the creation of patterns from recipes that have recipe
              // functions / actions, or we find another workaround.

              ItemStack output = recipe.getOutputWeightPairList().get(0).getOutput().toItemStack();
              return Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(output, world, entity);
            }
          }
        }

        return super.handleItemState(originalModel, stack, world, entity);
      }
    };
  }

  private static boolean canDisplayPatternOutput(ItemStack stack) {

    return (GuiBase.isShiftKeyDown() || ItemDesignPatternBakedModel.isPatternInDisplaySlot(stack));
  }

  private static boolean isPatternInDisplaySlot(ItemStack stack) {

    return false;
  }

}
