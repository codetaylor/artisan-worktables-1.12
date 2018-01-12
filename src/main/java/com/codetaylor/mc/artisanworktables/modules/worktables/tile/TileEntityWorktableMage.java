package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.block.BlockWorktableMage;
import com.codetaylor.mc.artisanworktables.modules.worktables.particle.ParticleWorktableMage;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

public class TileEntityWorktableMage
    extends TileEntityWorktableBase
    implements ITickable {

  private static final int TEXT_SHADOW_COLOR = new Color(172, 81, 227).getRGB();
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      String.format(ModuleWorktables.Textures.WORKTABLE_GUI, "mage")
  );

  private Random random;
  private boolean activeDirty = true;
  private boolean active;

  public TileEntityWorktableMage() {

    super(3, 3);
    this.random = new Random();
  }

  @Override
  protected int getWorkbenchGuiTextShadowColor() {

    return TEXT_SHADOW_COLOR;
  }

  @Override
  public RegistryRecipeWorktable getRecipeRegistry() {

    return WorktableAPI.getRecipeRegistry("mage");
  }

  @Override
  protected ResourceLocation getBackgroundTexture() {

    return BACKGROUND_TEXTURE;
  }

  @Override
  public int getGuiTabTextureYOffset() {

    return 7;
  }

  @Override
  protected String getTableTypeName(IBlockState state) {

    return "mage";
  }

  @Override
  protected String getTableTitleKey(IBlockState state) {

    return String.format("tile.%s.worktable_mage.name", ModuleWorktables.MOD_ID);
  }

  @Override
  public void update() {

    if (this.world == null) {
      return;
    }

    if (this.world.isRemote) { // client

      if (this.getToolHandler().getStackInSlot(0).isEmpty()) {
        return;
      }

      if (this.random.nextFloat() < 0.5) {
        this.spawnParticles();
      }

    } else { // server

      boolean active = this.isActive();

      if (this.active != active || this.activeDirty) {
        this.activeDirty = false;
        this.active = active;

        this.world.setBlockState(
            this.pos,
            ModuleWorktables.Blocks.WORKTABLE_MAGE.getDefaultState()
                .withProperty(BlockWorktableMage.ACTIVE, active)
        );

        this.validate();
        this.world.setTileEntity(this.pos, this);
      }
    }

  }

  @SideOnly(Side.CLIENT)
  private void spawnParticles() {

    this.world.spawnParticle(
        EnumParticleTypes.PORTAL,
        this.pos.getX() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
        this.pos.getY() + 0.5,
        this.pos.getZ() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
        0,
        this.random.nextFloat(),
        0
    );

    Minecraft.getMinecraft().effectRenderer.addEffect(
        new ParticleWorktableMage(
            this.world,
            this.pos.getX() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
            this.pos.getY() + 1.5,
            this.pos.getZ() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
            0,
            this.random.nextFloat(),
            0
        )
    );
  }

  private boolean isActive() {

    return !this.getToolHandler().getStackInSlot(0).isEmpty();
  }

}
