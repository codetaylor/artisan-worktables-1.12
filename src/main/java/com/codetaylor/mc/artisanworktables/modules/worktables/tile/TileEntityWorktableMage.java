package com.codetaylor.mc.artisanworktables.modules.worktables.tile;

import com.codetaylor.mc.artisanworktables.modules.worktables.ModuleWorktables;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.EnumWorktableType;
import com.codetaylor.mc.artisanworktables.modules.worktables.api.WorktableAPI;
import com.codetaylor.mc.artisanworktables.modules.worktables.particle.ParticleWorktableMage;
import com.codetaylor.mc.artisanworktables.modules.worktables.recipe.RegistryRecipeWorktable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Random;

public class TileEntityWorktableMage
    extends TileEntityWorktableBase
    implements ITickable {

  private static final int TEXT_SHADOW_COLOR = new Color(172, 81, 227).getRGB();
  private static final EnumWorktableType TYPE = EnumWorktableType.MAGE;
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(
      ModuleWorktables.MOD_ID,
      String.format(ModuleWorktables.Textures.WORKTABLE_GUI, TYPE.getName())
  );

  private Random random;

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

    return WorktableAPI.getRecipeRegistry(TYPE);
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
  public void update() {

    if (this.world != null && this.world.isRemote) {

      if (this.getToolHandler().getStackInSlot(0).isEmpty()) {
        return;
      }

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
              this.pos.getY() + 0.5,
              this.pos.getZ() + 0.5 + this.random.nextFloat() * 0.5 - 0.25,
              0,
              this.random.nextFloat(),
              0
          )
      );
    }

  }
}
