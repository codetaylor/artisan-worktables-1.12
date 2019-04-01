package com.codetaylor.mc.artisanworktables.api.recipe;

import com.codetaylor.mc.artisanworktables.api.ArtisanConfig;
import com.codetaylor.mc.artisanworktables.api.ArtisanToolHandlers;
import com.codetaylor.mc.artisanworktables.api.event.ArtisanCraftEvent;
import com.codetaylor.mc.artisanworktables.api.internal.event.ArtisanInventory;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.*;
import com.codetaylor.mc.artisanworktables.api.internal.reference.EnumTier;
import com.codetaylor.mc.artisanworktables.api.internal.util.EnchantmentHelper;
import com.codetaylor.mc.artisanworktables.api.internal.util.Util;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirement;
import com.codetaylor.mc.artisanworktables.api.recipe.requirement.IRequirementContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class ArtisanRecipe
    implements IArtisanRecipe {

  private String name;
  private ToolEntry[] tools;
  private Map<ResourceLocation, IRequirement> requirementMap;
  private List<OutputWeightPair> output;
  private List<IArtisanIngredient> ingredients;
  private List<IArtisanIngredient> secondaryIngredients;
  private boolean consumeSecondaryIngredients;
  private FluidStack fluidIngredient;
  private ExtraOutputChancePair[] extraOutputs;
  private IRecipeMatrixMatcher recipeMatrixMatcher;
  private boolean mirrored;
  private int width;
  private int height;
  private int minimumTier;
  private int maximumTier;
  private int experienceRequired;
  private int levelRequired;
  private boolean consumeExperience;
  private boolean hidden;

  @ParametersAreNonnullByDefault
  public ArtisanRecipe(
      @Nullable String name,
      Map<ResourceLocation, IRequirement> requirementMap,
      List<OutputWeightPair> output,
      ToolEntry[] tools,
      List<IArtisanIngredient> ingredients,
      List<IArtisanIngredient> secondaryIngredients,
      boolean consumeSecondaryIngredients,
      @Nullable FluidStack fluidIngredient,
      int experienceRequired,
      int levelRequired,
      boolean consumeExperience,
      ExtraOutputChancePair[] extraOutputs,
      IRecipeMatrixMatcher recipeMatrixMatcher,
      boolean mirrored,
      int width,
      int height,
      int minimumTier,
      int maximumTier,
      boolean hidden
  ) {

    this.name = name;
    this.requirementMap = Collections.unmodifiableMap(requirementMap);
    this.output = output;
    this.tools = tools;
    this.ingredients = ingredients;
    this.secondaryIngredients = Collections.unmodifiableList(secondaryIngredients);
    this.consumeSecondaryIngredients = consumeSecondaryIngredients;
    this.fluidIngredient = fluidIngredient;
    this.experienceRequired = experienceRequired;
    this.levelRequired = levelRequired;
    this.consumeExperience = consumeExperience;
    this.extraOutputs = extraOutputs;
    this.recipeMatrixMatcher = recipeMatrixMatcher;
    this.mirrored = mirrored;
    this.width = width;
    this.height = height;
    this.minimumTier = minimumTier;
    this.maximumTier = maximumTier;
    this.hidden = hidden;
  }

  // --------------------------------------------------------------------------
  // - Getters

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public int getExperienceRequired() {

    return this.experienceRequired;
  }

  @Override
  public int getLevelRequired() {

    return this.levelRequired;
  }

  @Override
  public boolean consumeExperience() {

    return this.consumeExperience;
  }

  @Override
  public IArtisanItemStack getSecondaryOutput() {

    if (this.extraOutputs[0].getOutput().isEmpty()) {
      return ArtisanItemStack.EMPTY;
    }

    return this.extraOutputs[0].getOutput();
  }

  @Override
  public float getSecondaryOutputChance() {

    return this.extraOutputs[0].getChance();
  }

  @Override
  public IArtisanItemStack getTertiaryOutput() {

    if (this.extraOutputs[1].getOutput().isEmpty()) {
      return ArtisanItemStack.EMPTY;
    }

    return this.extraOutputs[1].getOutput();
  }

  @Override
  public float getTertiaryOutputChance() {

    return this.extraOutputs[1].getChance();
  }

  @Override
  public IArtisanItemStack getQuaternaryOutput() {

    if (this.extraOutputs[2].getOutput().isEmpty()) {
      return ArtisanItemStack.EMPTY;
    }

    return this.extraOutputs[2].getOutput();
  }

  @Override
  public float getQuaternaryOutputChance() {

    return this.extraOutputs[2].getChance();
  }

  @Nonnull
  @Override
  public List<IArtisanIngredient> getSecondaryIngredients() {

    return this.secondaryIngredients;
  }

  @Override
  public boolean consumeSecondaryIngredients() {

    return this.consumeSecondaryIngredients;
  }

  @Override
  public IArtisanItemStack[] getTools(int toolIndex) {

    if (toolIndex >= this.tools.length) {
      return new IArtisanItemStack[0];
    }

    return this.tools[toolIndex].getTool();
  }

  @Override
  public List<IArtisanIngredient> getIngredientList() {

    return Collections.unmodifiableList(this.ingredients);
  }

  @Override
  @Nullable
  public FluidStack getFluidIngredient() {

    if (this.fluidIngredient != null) {
      return this.fluidIngredient.copy();
    }

    return null;
  }

  @Override
  public List<OutputWeightPair> getOutputWeightPairList() {

    return Collections.unmodifiableList(this.output);
  }

  @Override
  public IArtisanItemStack getBaseOutput(ICraftingContext context) {

    return this.output.get(0).getOutput();
  }

  @Override
  public boolean hasMultipleWeightedOutputs() {

    return this.output.size() > 1;
  }

  @Override
  public int getToolDamage(int toolIndex) {

    if (toolIndex >= this.tools.length) {
      return 0;
    }

    return this.tools[toolIndex].getDamage();
  }

  @Override
  public int getWidth() {

    return this.width;
  }

  @Override
  public int getHeight() {

    return this.height;
  }

  @Override
  public boolean isShaped() {

    return this.width > 0 && this.height > 0;
  }

  @Override
  public boolean isMirrored() {

    return this.mirrored;
  }

  @Override
  public int getToolCount() {

    return this.tools.length;
  }

  @Nullable
  @Override
  public IRequirement getRequirement(ResourceLocation resourceLocation) {

    return this.requirementMap.get(resourceLocation);
  }

  @Override
  public Map<ResourceLocation, IRequirement> getRequirements() {

    return this.requirementMap;
  }

  @Override
  public boolean isHidden() {

    return this.hidden;
  }

  // --------------------------------------------------------------------------
  // - Matching

  @Override
  public boolean isValidTool(ItemStack tool, int toolIndex) {

    if (toolIndex >= this.tools.length) {
      return false;
    }

    for (IArtisanItemStack itemStack : this.tools[toolIndex].getTool()) {

      // We can't use itemStack.isItemEqualIgnoreDurability(tool) here because
      // apparently Tinker's tools don't set the max durability on the tool
      // which causes that check to fail because it thinks the item can't be
      // damaged. Instead, we assume the item being used has durability and
      // just compare items.
      if (itemStack.getItem() == tool.getItem()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean hasSufficientToolDurability(ItemStack tool, int toolIndex) {

    if (tool.isEmpty()) {
      return false;
    }

    if (toolIndex >= this.tools.length) {
      return false;
    }

    if (ArtisanConfig.MODULE_WORKTABLES_CONFIG.restrictCraftMinimumDurability()) {

      IToolHandler handler = ArtisanToolHandlers.get(tool);
      int toolDamage = this.getToolDamage(toolIndex);
      return handler.canAcceptAllDamage(tool, toolDamage);
    }

    return true;
  }

  @Override
  public IArtisanItemStack selectOutput(
      ICraftingContext context,
      Random random
  ) {

    if (this.hasMultipleWeightedOutputs()) {
      WeightedPicker<IArtisanItemStack> picker = new WeightedPicker<>(random);

      for (OutputWeightPair pair : this.output) {
        picker.add(pair.getWeight(), pair.getOutput());
      }

      return picker.get();

    } else {
      return this.getBaseOutput(context);
    }
  }

  @Override
  public boolean matches(
      Map<ResourceLocation, IRequirementContext> requirementContextMap,
      int playerExperienceTotal,
      int playerLevels,
      boolean isPlayerCreative,
      ItemStack[] tools,
      ICraftingMatrixStackHandler craftingMatrix,
      FluidStack fluidStack,
      ISecondaryIngredientMatcher secondaryIngredientMatcher,
      EnumTier tier
  ) {

    if (!this.matchTier(tier)) {
      return false;
    }

    if (!isPlayerCreative) {

      if (playerExperienceTotal < this.experienceRequired) {
        return false;
      }

      if (playerLevels < this.levelRequired) {
        return false;
      }
    }

    if (this.getToolCount() > tools.length) {
      // this recipe requires more tools than the tools in the table
      return false;
    }

    // Do we have the correct tools?
    // Do the tools have enough durability for this recipe?
    for (int i = 0; i < this.getToolCount(); i++) {

      if (!this.isValidTool(tools[i], i)
          || !this.hasSufficientToolDurability(tools[i], i)) {
        return false;
      }
    }

    if (!this.matchesRequirements(requirementContextMap)) {
      return false;
    }

    if (!this.recipeMatrixMatcher.matches(this, craftingMatrix, fluidStack)) {
      return false;
    }

    if (!this.secondaryIngredients.isEmpty()) {
      return secondaryIngredientMatcher.matches(this.secondaryIngredients);
    }

    return true;
  }

  @Override
  public boolean matchesRequirements(Map<ResourceLocation, IRequirementContext> requirementContextMap) {

    // match requirements
    for (IRequirement requirement : this.requirementMap.values()) {

      ResourceLocation location = requirement.getResourceLocation();
      IRequirementContext context = requirementContextMap.get(location);

      if (context == null) {
        return false;
      }

      //noinspection unchecked
      if (!requirement.match(context)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean matchTier(EnumTier tier) {

    return this.minimumTier <= tier.getId()
        && this.maximumTier >= tier.getId();
  }

  // --------------------------------------------------------------------------
  // - Crafting

  @Override
  public void doCraft(
      ICraftingContext context
  ) {

    World world = context.getWorld();

    // Reduce player experience
    this.onCraftReduceExperience(context);

    // Decrease stacks in crafting matrix
    this.onCraftReduceIngredients(context);

    // Decrease stacks in secondary ingredient slots
    this.onCraftReduceSecondaryIngredients(context);

    ItemStack craftedItem = ItemStack.EMPTY;
    List<IArtisanItemStack> extraOutputList = new ArrayList<>(3);

    if (!world.isRemote) {
      // These methods must only be called on the server. They depend on RNG
      // and, as such, must be calculated with server authority.

      // Check if the recipe has multiple, weighted outputs and swap outputs accordingly.
      craftedItem = this.onCraftCheckAndSwapWeightedOutput(context);

      // Check for and populate secondary, tertiary and quaternary outputs
      extraOutputList = this.onCraftProcessExtraOutput(context, extraOutputList);
    }

    // Damage or destroy tools
    // Check for replacement tool
    for (int i = 0; i < this.getToolCount(); i++) {
      this.onCraftDamageTool(context, i);
      this.onCraftCheckAndReplaceTool(context, i);
    }

    // Issue #150:
    // When shift-clicking a recipe, craftedItem is empty.
    // The craftedItem is the return value of calling onCraftCheckAndSwapWeightedOutput
    // and is only used here to determine if onCraftCompleteServer should be called.
    if (!world.isRemote && !craftedItem.isEmpty()) {
      this.onCraftCompleteServer(craftedItem, extraOutputList, context);
    }
  }

  protected void onCraftCompleteServer(
      ItemStack craftedItem,
      List<IArtisanItemStack> extraOutputList,
      ICraftingContext context
  ) {

    List<ItemStack> extraOutputConvertedList = new ArrayList<>(extraOutputList.size());

    for (IArtisanItemStack artisanItemStack : extraOutputList) {
      extraOutputConvertedList.add(artisanItemStack.toItemStack());
    }

    MinecraftForge.EVENT_BUS.post(new ArtisanCraftEvent.Post(
        context.getPlayer(),
        context.getType(),
        context.getTier(),
        craftedItem.copy(),
        extraOutputConvertedList
    ));

    MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(
        context.getPlayer(),
        craftedItem.copy(),
        new ArtisanInventory(
            context.getCraftingMatrixHandler(),
            context.getCraftingMatrixHandler().getWidth(),
            context.getCraftingMatrixHandler().getHeight()
        )
    ));
  }

  protected void onCraftReduceIngredients(ICraftingContext context) {

    IItemHandlerModifiable matrixHandler = context.getCraftingMatrixHandler();
    IFluidHandler fluidHandler = context.getFluidHandler();

    List<ItemStack> remainingItems = this.getRemainingItems(
        context,
        NonNullList.withSize(matrixHandler.getSlots(), ItemStack.EMPTY)
    );

    for (int i = 0; i < remainingItems.size(); i++) {

      ItemStack itemStack = matrixHandler.getStackInSlot(i);
      ItemStack remainingItemStack = remainingItems.get(i);

      if (!itemStack.isEmpty()) {
        matrixHandler.setStackInSlot(i, Util.decrease(itemStack.copy(), 1, false));
        itemStack = matrixHandler.getStackInSlot(i);
      }

      if (!remainingItemStack.isEmpty()) {

        if (itemStack.isEmpty()) {
          matrixHandler.setStackInSlot(i, remainingItemStack);

        } else if (ItemStack.areItemsEqual(itemStack, remainingItemStack)
            && ItemStack.areItemStackTagsEqual(itemStack, remainingItemStack)) {

          int combinedStackSize = remainingItemStack.getCount() + itemStack.getCount();

          if (combinedStackSize > itemStack.getMaxStackSize()) {

            // Special handling if combined stack size exceeds max stack size.
            // First, set the existing slot to the maximum stack size. Next,
            // loop while the combined stack size is larger than the max stack
            // size and put the items into the player's inventory or on the
            // ground. Finally, put the remaining items in the player's
            // inventory or on the ground.

            ItemStack copy = remainingItemStack.copy();
            copy.setCount(itemStack.getMaxStackSize());
            matrixHandler.setStackInSlot(i, copy);
            combinedStackSize -= itemStack.getMaxStackSize();

            while (combinedStackSize > itemStack.getMaxStackSize()) {
              copy = remainingItemStack.copy();
              copy.setCount(itemStack.getMaxStackSize());

              if (!context.getPlayer().addItemStackToInventory(copy)) {
                context.getPlayer().dropItem(copy, false);
              }
              combinedStackSize -= itemStack.getMaxStackSize();
            }

            remainingItemStack.grow(itemStack.getCount());

            if (!context.getPlayer().addItemStackToInventory(remainingItemStack)) {
              context.getPlayer().dropItem(remainingItemStack, false);
            }

          } else {
            remainingItemStack.grow(itemStack.getCount());
            matrixHandler.setStackInSlot(i, remainingItemStack);
          }

        } else if (!context.getPlayer().addItemStackToInventory(remainingItemStack)) {
          context.getPlayer().dropItem(remainingItemStack, false);
        }

      }
    }

    FluidStack fluidIngredient = this.getFluidIngredient();

    if (fluidIngredient != null) {
      fluidHandler.drain(fluidIngredient, true);
    }
  }

  @Nonnull
  protected List<ItemStack> getRemainingItems(
      ICraftingContext context,
      NonNullList<ItemStack> itemStacks
  ) {

    ICraftingMatrixStackHandler matrixHandler = context.getCraftingMatrixHandler();

    for (int i = 0; i < matrixHandler.getSlots(); i++) {
      itemStacks.set(i, Util.getContainerItem(matrixHandler.getStackInSlot(i)));
    }

    return itemStacks;
  }

  protected void onCraftReduceSecondaryIngredients(ICraftingContext context) {

    IItemHandlerModifiable secondaryIngredientHandler = context.getSecondaryIngredientHandler();

    if (!this.consumeSecondaryIngredients()
        || secondaryIngredientHandler == null) {
      return;
    }

    List<IArtisanIngredient> secondaryIngredients = this.secondaryIngredients;

    if (!secondaryIngredients.isEmpty()) {
      // reduce secondary ingredients

      for (IArtisanIngredient requiredIngredient : secondaryIngredients) {
        int requiredAmount = requiredIngredient.getAmount();
        int slotCount = secondaryIngredientHandler.getSlots();

        for (int i = 0; i < slotCount; i++) {
          ItemStack stackInSlot = secondaryIngredientHandler.getStackInSlot(i);

          if (stackInSlot.isEmpty()) {
            continue;
          }

          if (requiredIngredient.matchesIgnoreAmount(stackInSlot)) {

            // get the remaining secondary item
            ItemStack remainingItemStack = this.getRemainingSecondaryItem(context, requiredIngredient, stackInSlot);
            int existingStackCount = stackInSlot.getCount();

            if (existingStackCount <= requiredAmount) {

              // Replace the entire stack in slot with remaining item.

              if (remainingItemStack.isEmpty()) {

                // If the remaining item is empty, empty the slot.
                secondaryIngredientHandler.setStackInSlot(i, ItemStack.EMPTY);

              } else {

                // First, empty the slot. Next, increase the remaining item count to match
                // the slot's previous stack count. Finally, attempt to insert the remaining
                // item into the secondary ingredient slots spilling any overflow into the
                // player's inventory or onto the ground.

                secondaryIngredientHandler.setStackInSlot(i, ItemStack.EMPTY);
                remainingItemStack.setCount(existingStackCount);
                ItemStack itemStack = secondaryIngredientHandler.insertItem(i, remainingItemStack, false);

                for (int j = 0; !itemStack.isEmpty() && j < secondaryIngredientHandler.getSlots() && j != i; j++) {
                  itemStack = secondaryIngredientHandler.insertItem(j, itemStack, false);
                }

                if (!itemStack.isEmpty()
                    && !context.getPlayer().addItemStackToInventory(itemStack)) {
                  context.getPlayer().dropItem(itemStack, false);
                }
              }

              requiredAmount -= existingStackCount;

            } else if (existingStackCount > requiredAmount) {

              // Replace partial stack in slot with remaining item.

              // First, decrease the existing stack size. Next, since we know that the
              // existing stack has been decreased by the required amount, we increase
              // the remaining item stack count by the required amount. Finally, attempt
              // to insert the remaining item stack into the secondary ingredient slots
              // spilling any overflow into the player's inventory or onto the ground.

              ItemStack decreasedStack = Util.decrease(stackInSlot.copy(), requiredAmount, false);
              secondaryIngredientHandler.setStackInSlot(i, decreasedStack);

              if (!remainingItemStack.isEmpty()) {
                remainingItemStack.setCount(requiredAmount);
                ItemStack itemStack = secondaryIngredientHandler.insertItem(i, remainingItemStack, false);

                for (int j = 0; !itemStack.isEmpty() && j < secondaryIngredientHandler.getSlots() && j != i; j++) {
                  itemStack = secondaryIngredientHandler.insertItem(j, itemStack, false);
                }

                if (!itemStack.isEmpty()
                    && !context.getPlayer().addItemStackToInventory(itemStack)) {
                  context.getPlayer().dropItem(itemStack, false);
                }
              }

              requiredAmount = 0;
            }

            if (requiredAmount == 0) {
              break;
            }
          }
        }

        if (requiredAmount > 0) {
          // TODO: failed to find all required ingredients... shouldn't happen if the matching code is correct
        }

      }
    }
  }

  protected ItemStack getRemainingSecondaryItem(
      ICraftingContext context,
      IArtisanIngredient ingredient,
      ItemStack stack
  ) {

    return Util.getContainerItem(stack);
  }

  protected void onCraftReduceExperience(ICraftingContext context) {

    EntityPlayer player = context.getPlayer();

    if (player.isCreative()) {
      // Don't consume experience when the player is in creative mode.
      return;
    }

    if (this.consumeExperience()) {

      if (this.getExperienceRequired() > 0) {
        EnchantmentHelper.adjustPlayerExperience(player, -this.getExperienceRequired());

      } else if (this.getLevelRequired() > 0) {
        int experience = EnchantmentHelper.getExperienceToLevel(
            player.experienceLevel - this.getLevelRequired(),
            player.experienceLevel
        );
        EnchantmentHelper.adjustPlayerExperience(player, -experience);
      }
    }
  }

  @Nonnull
  protected ItemStack onCraftCheckAndSwapWeightedOutput(ICraftingContext context) {

    // Select an output.
    ItemStack itemStack = this.selectOutput(context, Util.RANDOM).toItemStack();

    EntityPlayer player = context.getPlayer();

    if (!player.inventory.getItemStack().isEmpty()) {

      // If the player is holding an item under their mouse cursor swap the item accordingly.

      if (this.hasMultipleWeightedOutputs()) {
        player.inventory.setItemStack(itemStack);
        ((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(-1, -1, itemStack));
      }
    }

    // This is returned if the player has shift-clicked the craft.
    return itemStack;
  }

  @Nonnull
  protected List<IArtisanItemStack> onCraftProcessExtraOutput(
      ICraftingContext context,
      List<IArtisanItemStack> result
  ) {

    IArtisanItemStack extraOutput = this.getSecondaryOutput();

    if (!extraOutput.isEmpty()) {

      if (Util.RANDOM.nextFloat() < this.getSecondaryOutputChance()) {
        result.add(this.generateExtraOutput(context, extraOutput));
      }
    }

    extraOutput = this.getTertiaryOutput();

    if (!extraOutput.isEmpty()) {

      if (Util.RANDOM.nextFloat() < this.getTertiaryOutputChance()) {
        result.add(this.generateExtraOutput(context, extraOutput));
      }
    }

    extraOutput = this.getQuaternaryOutput();

    if (!extraOutput.isEmpty()) {

      if (Util.RANDOM.nextFloat() < this.getQuaternaryOutputChance()) {
        result.add(this.generateExtraOutput(context, extraOutput));
      }
    }

    return result;
  }

  protected void onCraftDamageTool(ICraftingContext context, int toolIndex) {

    World world = context.getWorld();
    EntityPlayer player = context.getPlayer();
    IItemHandlerModifiable toolHandler = context.getToolHandler();
    ItemStack itemStack = toolHandler.getStackInSlot(toolIndex);

    if (!itemStack.isEmpty() && this.isValidTool(itemStack, toolIndex)) {
      IToolHandler handler = ArtisanToolHandlers.get(itemStack);
      boolean broken = handler.applyDamage(itemStack, this.getToolDamage(toolIndex), false);

      if (broken && !world.isRemote) {
        world.playSound(
            null,
            player.posX,
            player.posY,
            player.posZ,
            SoundEvents.ENTITY_ITEM_BREAK,
            SoundCategory.PLAYERS,
            1.0f,
            1.0f
        );
      }
    }
  }

  protected void onCraftCheckAndReplaceTool(ICraftingContext context, int toolIndex) {

    IItemHandlerModifiable toolHandler = context.getToolHandler();
    ItemStack itemStack = toolHandler.getStackInSlot(toolIndex);

    if (!this.hasSufficientToolDurability(itemStack, toolIndex)) {
      // Tool needs to be replaced
      IItemHandler capability = context.getToolReplacementHandler();

      if (capability == null) {
        return;
      }

      int slotCount = capability.getSlots();

      for (int i = 0; i < slotCount; i++) {
        ItemStack potentialTool = capability.getStackInSlot(i);

        if (potentialTool.isEmpty()) {
          continue;
        }

        if (this.isValidTool(potentialTool, toolIndex)
            && this.hasSufficientToolDurability(potentialTool, toolIndex)) {
          // Found an acceptable tool
          potentialTool = capability.extractItem(i, 1, false);
          capability.insertItem(i, toolHandler.getStackInSlot(toolIndex), false);
          toolHandler.setStackInSlot(toolIndex, potentialTool);
        }
      }
    }
  }

  @Nonnull
  protected IArtisanItemStack generateExtraOutput(ICraftingContext context, IArtisanItemStack extraOutput) {

    World world = context.getWorld();
    IItemHandler secondaryOutputHandler = context.getSecondaryOutputHandler();
    ItemStack result = extraOutput.toItemStack();

    for (int i = 0; i < 3; i++) {
      result = secondaryOutputHandler.insertItem(i, result, false);

      if (result.isEmpty()) {
        break;
      }
    }

    if (!result.isEmpty() && !world.isRemote) {
      BlockPos pos = context.getPosition();
      Util.spawnStackOnTop(world, result, pos.add(0, 1, 0), 1.0);
    }

    return ArtisanItemStack.from(result);
  }

}
