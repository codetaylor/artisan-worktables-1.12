package com.codetaylor.mc.artisanworktables.modules.worktables.integration.crafttweaker;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.ICraftingMatrixStackHandler;
import com.codetaylor.mc.artisanworktables.modules.worktables.gui.AWContainer;
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

  public static String getExportString(AWContainer container, TileEntityBase tileEntity, boolean shaped) {

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

      for (int i = container.slotIndexCraftingMatrixStart; i <= container.slotIndexCraftingMatrixEnd; i++) {
        int x = (i - container.slotIndexCraftingMatrixStart) % width;

        if (x == 0) {
          out.append("    [");
        }

        String oreDict = tileEntity.oreDictMap.lookup(i);
        ItemStack stackInSlot = container.getSlot(i).getStack();

        if (oreDict == null) {
          ZSRecipeExport.getItemString(stackInSlot, out, true, false);

        } else {
          ZSRecipeExport.getItemStringOredict(oreDict, stackInSlot, out, true);
        }

        if (i == container.slotIndexCraftingMatrixEnd) {
          // finished with the whole thing
          out.append("]])\n");

        } else if (x == width - 1) {
          // finished with the row
          out.append("],\n");

        } else {
          out.append(", ");
        }
      }

    } else {
      List<String> list = new ArrayList<>();

      for (int i = container.slotIndexCraftingMatrixStart; i <= container.slotIndexCraftingMatrixEnd; i++) {
        String oreDict = tileEntity.oreDictMap.lookup(i);
        ItemStack stackInSlot = container.getSlot(i).getStack();

        if (!stackInSlot.isEmpty()) {
          StringBuilder builder = new StringBuilder();

          if (oreDict == null) {
            ZSRecipeExport.getItemString(stackInSlot, builder, true, false);

          } else {
            ZSRecipeExport.getItemStringOredict(oreDict, stackInSlot, builder, true);
          }

          list.add(builder.toString());
        }
      }

      out.append("  .setShapeless([").append(String.join(", ", list)).append("])\n");
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

        for (int i = container.slotIndexSecondaryInputStart; i <= container.slotIndexSecondaryInputEnd; i++) {
          String oreDict = tileEntity.oreDictMap.lookup(i);
          ItemStack stackInSlot = container.getSlot(i).getStack();

          if (stackInSlot.isEmpty()) {
            continue;
          }

          StringBuilder builder = new StringBuilder();

          if (oreDict == null) {
            ZSRecipeExport.getItemString(stackInSlot, builder, true, false);

          } else {
            ZSRecipeExport.getItemStringOredict(oreDict, stackInSlot, builder, true);
          }

          count += 1;
          out.append(builder.toString());
          out.append((count == total) ? "" : ", ");
        }

        out.append("])\n");
      }
    }

    // - addTool
    // -------------------------------------------------------------------------
    {
      for (int i = container.slotIndexToolsStart; i <= container.slotIndexToolsEnd; i++) {
        String oreDict = tileEntity.oreDictMap.lookup(i);
        ItemStack stackInSlot = container.getSlot(i).getStack();

        if (!stackInSlot.isEmpty()) {
          out.append("  .addTool(");

          StringBuilder builder = new StringBuilder();

          if (oreDict == null) {
            ZSRecipeExport.getItemString(stackInSlot, builder, true, false);

          } else {
            ZSRecipeExport.getItemStringOredict(oreDict, stackInSlot, builder, true);
          }

          out.append(builder.toString());
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

  private static StringBuilder getItemStringOredict(String oreDict, ItemStack itemStack, StringBuilder out, boolean ignoreCount) {

    out.append("<ore:").append(oreDict).append(">");

    if (!ignoreCount
        && itemStack.getCount() > 1) {
      out.append(" * ").append(itemStack.getCount());
    }

    return out;
  }

  private ZSRecipeExport() {
    //
  }
}
