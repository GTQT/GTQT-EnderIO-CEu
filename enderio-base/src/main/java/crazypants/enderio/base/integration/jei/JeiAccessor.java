package crazypants.enderio.base.integration.jei;

import javax.annotation.Nonnull;

import com.enderio.core.common.util.NNList;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class JeiAccessor {

  static boolean jeiRuntimeAvailable = false;

  public static boolean isJeiRuntimeAvailable() {
    return jeiRuntimeAvailable;
  }

  public static void setFilterText(@Nonnull String filterText) {
    if (jeiRuntimeAvailable) {
      JeiPlugin.setFilterText(filterText);
    }
  }

  public static @Nonnull String getFilterText() {
    if (jeiRuntimeAvailable) {
      return JeiPlugin.getFilterText();
    }
    return "";
  }

  public static void showCraftingRecipes() {
    if (jeiRuntimeAvailable) {
      JeiPlugin.showCraftingRecipes();
    }
  }

  private static final @Nonnull NNList<AlternativeRecipe> ALTERNATIVES = new NNList<>();

  public static void addAlternativeRecipe(@Nonnull ResourceLocation parentRecipeName, @Nonnull IRecipe recipe) {
      ALTERNATIVES.add(new AlternativeRecipe(parentRecipeName, recipe));
  }

  static @Nonnull NNList<IRecipe> getActiveAlternativeRecipes() {
    NNList<IRecipe> activeAlternatives = new NNList<>();
    for (AlternativeRecipe alternative : ALTERNATIVES) {
      if (ForgeRegistries.RECIPES.containsKey(alternative.parentRecipeName)) {
        activeAlternatives.add(alternative.recipe);
      }
    }
    return activeAlternatives;
  }

  private static class AlternativeRecipe {

    private final @Nonnull ResourceLocation parentRecipeName;
    private final @Nonnull IRecipe recipe;

    private AlternativeRecipe(@Nonnull ResourceLocation parentRecipeName, @Nonnull IRecipe recipe) {
      this.parentRecipeName = parentRecipeName;
      this.recipe = recipe;
    }
  }

}
