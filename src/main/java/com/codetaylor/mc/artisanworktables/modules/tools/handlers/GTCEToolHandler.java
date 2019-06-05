package com.codetaylor.mc.artisanworktables.modules.tools.handlers;

import com.codetaylor.mc.artisanworktables.api.recipe.IToolHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GTCEToolHandler
    implements IToolHandler {

  private static final Logger LOGGER = LogManager.getLogger(GTCEToolHandler.class);

  private static final class Reference {

    private static final String CLASS_GTUTILITY = "gregtech.api.util.GTUtility";
    private static final String CLASS_ITOOLITEM = "gregtech.api.items.IToolItem";
    private static final String CLASS_GREGTECHCAPABILITIES = "gregtech.api.capability.GregtechCapabilities";
    private static final String FIELD_CAPABILITY_ELECTRIC_ITEM = "CAPABILITY_ELECTRIC_ITEM";
    private static final String METHOD_DO_DAMAGE_ITEM = "doDamageItem";
  }

  private final Class<?> classGTUtility;
  private final Class<?> classIDamagableItem;
  private final Class<?> classGregtechCapabilities;
  private final Capability<?> capabilityElectricItem;
  private final Method methodDoDamageItem;

  public GTCEToolHandler() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {

    this.classGTUtility = Class.forName(Reference.CLASS_GTUTILITY);
    this.classIDamagableItem = Class.forName(Reference.CLASS_ITOOLITEM);
    this.classGregtechCapabilities = Class.forName(Reference.CLASS_GREGTECHCAPABILITIES);

    Field field = this.classGregtechCapabilities.getField(Reference.FIELD_CAPABILITY_ELECTRIC_ITEM);
    this.capabilityElectricItem = (Capability<?>) field.get(null);

    this.methodDoDamageItem = this.classGTUtility.getMethod(Reference.METHOD_DO_DAMAGE_ITEM, ItemStack.class, int.class, boolean.class);
  }

  @Override
  public boolean matches(ItemStack itemStack) {

    return (this.classIDamagableItem.isAssignableFrom(itemStack.getItem().getClass()))
        || (itemStack.hasCapability(this.capabilityElectricItem, null));
  }

  @Override
  public boolean canAcceptAllDamage(ItemStack itemStack, int damage) {

    return this.GTUtility_doDamageItem(itemStack, damage, true);
  }

  @Override
  public boolean applyDamage(World world, ItemStack itemStack, int damage, boolean simulate) {

    // The server tag object for the stack is ending up on the client in
    // a single player setup. The same instance - so any changes made on the
    // client's tag were also made on the server's tag because it's the same
    // object.
    //
    // TODO: I don't know what is causing this to happen.
    //  - Is this normal behavior?
    //  - Is something that AW does causing this?
    //
    // Duplicating the tag on the client before it's modified seems to do
    // the trick.
    if (!simulate
        && world.isRemote
        && itemStack.getTagCompound() != null) {
      itemStack.setTagCompound(itemStack.getTagCompound().copy());
    }

    return !this.GTUtility_doDamageItem(itemStack, damage, simulate);
  }

  private boolean GTUtility_doDamageItem(ItemStack itemStack, int damage, boolean simulate) {

    try {
      return (boolean) this.methodDoDamageItem.invoke(null, itemStack, damage, simulate);

    } catch (IllegalAccessException | InvocationTargetException e) {
      LOGGER.error("Error damaging item: " + itemStack, e);
    }

    return false;
  }

}
