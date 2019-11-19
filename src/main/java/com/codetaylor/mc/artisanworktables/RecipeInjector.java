package com.codetaylor.mc.artisanworktables;

import com.codetaylor.mc.artisanworktables.api.internal.recipe.ArtisanIngredient;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.ArtisanItemStack;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.IArtisanIngredient;
import com.codetaylor.mc.artisanworktables.api.internal.recipe.RecipeBuilderException;
import com.codetaylor.mc.artisanworktables.api.recipe.RecipeBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

public final class RecipeInjector {

  public static void inject() {

    IArtisanIngredient[] inputs = new IArtisanIngredient[]{
        ArtisanIngredient.from(Ingredient.fromStacks(new ItemStack(Items.APPLE))),
        ArtisanIngredient.from(Ingredient.fromStacks(new ItemStack(Items.POTATO))),
        ArtisanIngredient.from(Ingredient.fromStacks(new ItemStack(Items.BLAZE_ROD)))
    };

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        for (int k = 0; k < 3; k++) {
          for (int l = 0; l < 3; l++) {
            for (int m = 0; m < 3; m++) {
              for (int n = 0; n < 3; n++) {
                for (int o = 0; o < 3; o++) {

                  try {
                    RecipeBuilder.get("blacksmith")
                        .setIngredients(new IArtisanIngredient[][]{
                            new IArtisanIngredient[]{
                                inputs[i], inputs[j], inputs[k], inputs[l], inputs[m]
                            },
                            new IArtisanIngredient[]{
                                inputs[n], inputs[o]
                            }
                        })
                        .addOutput(ArtisanItemStack.from(new ItemStack(Blocks.GRAVEL)), 1)
                        .addTool(ArtisanIngredient.from(new OreIngredient("artisansHammer")), 10)
                        .addTool(ArtisanIngredient.from(new OreIngredient("artisansPliers")), 10)
                        .addTool(ArtisanIngredient.from(new OreIngredient("artisansKnife")), 10)
                        .create();

                  } catch (RecipeBuilderException ignore) {
                    //
                  }

                }
              }
            }
          }
        }
      }
    }

  }

  private RecipeInjector() {
    //
  }

}
