package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntityBase;
import com.codetaylor.mc.artisanworktables.modules.worktables.tile.spi.TileEntitySecondaryInputBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public final class ZSRecipeExport {

  public static String getExportString(TileEntityBase tileEntity, boolean shaped) {

    ICraftingMatrixStackHandler craftingMatrixHandler = tileEntity.getCraftingMatrixHandler();
    int width = craftingMatrixHandler.getWidth();
    int height = craftingMatrixHandler.getHeight();

    ItemStackHandler resultHandler = tileEntity.getResultHandler();
    ItemStackHandler toolHandler = tileEntity.getToolHandler();
    ItemStackHandler secondaryOutputHandler = tileEntity.getSecondaryOutputHandler();
    FluidTank tank = tileEntity.getTank();

    // - start

    StringBuilder out = new StringBuilder();
    out.append("RecipeBuilder.get(\"").append(tileEntity.getType().getName()).append("\")\n");

    // - setShaped
    // -------------------------------------------------------------------------
    if (shaped) {
      out.append("  .setShaped([\n");

      for (int y = 0; y < height; y++) {
        out.append("    [");

        for (int x = 0; x < width; x++) {
          ItemStack stackInSlot = craftingMatrixHandler.getStackInSlot(x + y * width);
          ZSRecipeExport.getItemString(stackInSlot, out, true, false);
          out.append((x == width - 1) ? "" : ", ");
        }
        out.append((y == height - 1) ? "]])\n" : "],\n");
      }
    } else {
      out.append("  .setShapeless([");

      List<ItemStack> list = new ArrayList<>();

      for (int i = 0; i < craftingMatrixHandler.getSlots(); i++) {
        ItemStack stackInSlot = craftingMatrixHandler.getStackInSlot(i);

        if (!stackInSlot.isEmpty()) {
          list.add(stackInSlot.copy());
        }
      }

      for (int i = 0; i < list.size(); i++) {
        ItemStack itemStack = list.get(i);
        ZSRecipeExport.getItemString(itemStack, out, true, false);
        out.append((i == list.size() - 1) ? "" : ", ");
      }
      out.append("])\n");
    }

    // - setFluid
    // -------------------------------------------------------------------------
    if (tank.getFluidAmount() > 0
        && tank.getFluid() != null) {

      FluidStack fluid = tank.getFluid();
      String name = fluid.getFluid().getName();
      out.append("  .setFluid(<").append("liquid:").append(name).append("> * ").append(fluid.amount).append(")\n");
    }

    // - setSecondaryIngredients
    // -------------------------------------------------------------------------
    if (tileEntity instanceof TileEntitySecondaryInputBase) {
      IItemHandlerModifiable secondaryIngredientHandler = ((TileEntitySecondaryInputBase) tileEntity).getSecondaryIngredientHandler();

      int total = 0;
      int count = 0;

      for (int i = 0; i < secondaryIngredientHandler.getSlots(); i++) {

        if (!secondaryIngredientHandler.getStackInSlot(i).isEmpty()) {
          total += 1;
        }
      }

      if (total > 0) {
        out.append("  .setSecondaryIngredients([");

        for (int i = 0; i < secondaryIngredientHandler.getSlots(); i++) {
          ItemStack stackInSlot = secondaryIngredientHandler.getStackInSlot(i);
          count += 1;

          if (stackInSlot.isEmpty()) {
            continue;
          }

          ZSRecipeExport.getItemString(stackInSlot, out, false, false);
          out.append((count == total) ? "" : ", ");
        }

        out.append("])\n");
      }
    }

    // - addTool
    // -------------------------------------------------------------------------
    {
      for (int i = 0; i < toolHandler.getSlots(); i++) {
        ItemStack stackInSlot = toolHandler.getStackInSlot(i);

        if (!stackInSlot.isEmpty()) {
          out.append("  .addTool(");
          ZSRecipeExport.getItemString(stackInSlot, out, true, true);
          out.append(", 1)\n");
        }
      }
    }

    // - setOutput
    // -------------------------------------------------------------------------
    {
      ItemStack stackInSlot = resultHandler.getStackInSlot(0);
      out.append("  .addOutput(");
      ZSRecipeExport.getItemString(stackInSlot, out, false, false);
      out.append(")\n");
    }

    // - setSecondaryOutput
    // -------------------------------------------------------------------------
    {
      int slot = 0;

      for (int i = 0; i < secondaryOutputHandler.getSlots(); i++) {
        ItemStack stackInSlot = secondaryOutputHandler.getStackInSlot(i);

        if (stackInSlot.isEmpty()) {
          continue;
        }

        if (slot == 0) {
          slot += 1;
          out.append("  .setExtraOutputOne(");
          ZSRecipeExport.getItemString(stackInSlot, out, false, false);
          out.append(", 1.0)\n");

        } else if (slot == 1) {
          slot += 1;
          out.append("  .setExtraOutputTwo(");
          ZSRecipeExport.getItemString(stackInSlot, out, false, false);
          out.append(", 1.0)\n");

        } else if (slot == 2) {
          slot += 1;
          out.append("  .setExtraOutputThree(");
          ZSRecipeExport.getItemString(stackInSlot, out, false, false);
          out.append(", 1.0)\n");
        }
      }
    }

    // - create
    // -------------------------------------------------------------------------
    out.append("  .create();");

    // - end

    return out.toString();
  }

  private static StringBuilder getItemString(ItemStack itemStack, StringBuilder out, boolean ignoreCount, boolean wildcard) {

    Item item = itemStack.getItem();
    ResourceLocation registryName = item.getRegistryName();

    if (registryName == null || itemStack.isEmpty()) {
      out.append("null");

    } else {
      out.append("<").append(registryName.getResourceDomain()).append(":")
          .append(registryName.getResourcePath());

      if (wildcard) {
        out.append(":*");

      } else if (itemStack.getMetadata() > 0) {
        out.append(":").append(itemStack.getMetadata());
      }

      out.append(">");

      if (!ignoreCount
          && itemStack.getCount() > 1) {
        out.append(" * ").append(itemStack.getCount());
      }
    }

    return out;
  }

  private ZSRecipeExport() {
    //
  }
}
