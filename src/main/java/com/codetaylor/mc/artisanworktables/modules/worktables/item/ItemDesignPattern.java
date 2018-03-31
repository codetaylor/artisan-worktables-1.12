package com.codetaylor.mc.artisanworktables.modules.worktables.item;

import com.blamejared.ctgui.api.GuiBase;
import com.codetaylor.mc.artisanworktables.api.ArtisanAPI;
import com.codetaylor.mc.artisanworktables.api.recipe.IArtisanRecipe;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktablesConfig;
import com.codetaylor.mc.artisanworktables.modules.worktables.Util;
import com.codetaylor.mc.athenaeum.spi.IVariant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class ItemDesignPattern
    extends Item {

  public static final String NAME = "design_pattern";

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    if (stack.hasTagCompound()) {
      return super.getUnlocalizedName(stack) + "." + EnumType.WRITTEN.getName();
    }

    return super.getUnlocalizedName(stack) + "." + EnumType.BLANK.getName();
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

  public enum EnumType
      implements IVariant {

    BLANK(0, "blank"),
    WRITTEN(1, "written");

    private static final EnumType[] META_LOOKUP = Stream.of(EnumType.values())
        .sorted(Comparator.comparing(EnumType::getMeta))
        .toArray(EnumType[]::new);
    private final int meta;

    private final String name;

    EnumType(int meta, String name) {

      this.meta = meta;
      this.name = name;
    }

    @Override
    public int getMeta() {

      return this.meta;
    }

    @Override
    public String getName() {

      return this.name;
    }

    public static EnumType fromMeta(int meta) {

      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }
  }

  @SideOnly(Side.CLIENT)
  public static class MeshDefinition
      implements ItemMeshDefinition {

    private static final ModelResourceLocation MODEL_RESOURCE_LOCATION_BLANK;
    private static final ModelResourceLocation MODEL_RESOURCE_LOCATION_WRITTEN;

    static {
      MODEL_RESOURCE_LOCATION_BLANK = new ModelResourceLocation(new ResourceLocation(
          ModuleWorktables.MOD_ID,
          NAME + "_" + EnumType.BLANK.getName()
      ), "inventory");
      MODEL_RESOURCE_LOCATION_WRITTEN = new ModelResourceLocation(new ResourceLocation(
          ModuleWorktables.MOD_ID,
          NAME + "_" + EnumType.WRITTEN.getName()
      ), "inventory");
    }

    @Nonnull
    @Override
    public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {

      if (stack.hasTagCompound()) {
        return MODEL_RESOURCE_LOCATION_WRITTEN;
      }

      return MODEL_RESOURCE_LOCATION_BLANK;
    }
  }

  public static class BakedModel
      implements IBakedModel {

    private final IBakedModel delegate;

    public BakedModel(IBakedModel delegate) {

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

              if (recipe != null && ItemDesignPattern.BakedModel.canDisplayPatternOutput(stack)) {

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

      return (GuiBase.isShiftKeyDown() || ItemDesignPattern.BakedModel.isPatternInDisplaySlot(stack));
    }

    private static boolean isPatternInDisplaySlot(ItemStack stack) {

      return false;
    }

  }
}
