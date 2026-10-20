package crazypants.enderio.base.recipe.soul;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.enderio.core.common.util.EntityUtil;
import com.enderio.core.common.util.NNList;

import crazypants.enderio.base.recipe.RecipeLevel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class DynamicSoulBinderRecipe extends BasicSoulBinderRecipe {

  public DynamicSoulBinderRecipe(@Nonnull ItemStack inputStack, @Nonnull ItemStack outputStack, int energyRequired, int xpRequired, @Nonnull String uid,
      @Nonnull RecipeLevel recipeLevel, @Nonnull Predicate<ResourceLocation> entityFilter, @Nonnull OutputFilter filter) {
    super(inputStack, outputStack, energyRequired, xpRequired, uid, recipeLevel,
      EntityUtil.getAllRegisteredMobNames().stream().filter(entityFilter).collect(Collectors.toCollection(() -> new NNList<ResourceLocation>())), filter);
  }

}
