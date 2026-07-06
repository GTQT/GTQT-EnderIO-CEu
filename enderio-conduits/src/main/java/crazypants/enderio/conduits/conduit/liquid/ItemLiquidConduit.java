package crazypants.enderio.conduits.conduit.liquid;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.enderio.core.api.client.gui.IAdvancedTooltipProvider;
import com.enderio.core.client.handlers.SpecialTooltipHandler;

import crazypants.enderio.api.IModObject;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.conduit.ConduitDisplayMode;
import crazypants.enderio.base.conduit.ConnectionMode;
import crazypants.enderio.base.conduit.IConduit;
import crazypants.enderio.base.conduit.IServerConduit;
import crazypants.enderio.base.conduit.geom.Offset;
import crazypants.enderio.base.conduit.registry.ConduitBuilder;
import crazypants.enderio.base.conduit.registry.ConduitRegistry;
import crazypants.enderio.base.gui.IconEIO;
import crazypants.enderio.conduits.conduit.AbstractItemConduit;
import crazypants.enderio.conduits.conduit.ItemConduitSubtype;
import crazypants.enderio.conduits.config.ConduitConfig;
import crazypants.enderio.conduits.lang.Lang;
import crazypants.enderio.conduits.render.ConduitBundleRenderManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLiquidConduit extends AbstractItemConduit implements IAdvancedTooltipProvider {

  public static ItemLiquidConduit create(@Nonnull IModObject modObject, @Nullable Block block) {
    return new ItemLiquidConduit(modObject);
  }

  protected ItemLiquidConduit(@Nonnull IModObject modObject) {
    super(modObject, new ItemConduitSubtype(modObject.getUnlocalisedName(), modObject.getRegistryName().toString()),
        new ItemConduitSubtype(modObject.getUnlocalisedName() + "_advanced", modObject.getRegistryName().toString() + "_advanced"),
        new ItemConduitSubtype(modObject.getUnlocalisedName() + "_ender", modObject.getRegistryName().toString() + "_ender"),
        new ItemConduitSubtype(modObject.getUnlocalisedName() + "_crystalline_ender", modObject.getRegistryName().toString() + "_crystalline_ender"),
        new ItemConduitSubtype(modObject.getUnlocalisedName() + "_crystalline_pink_slime_ender",
            modObject.getRegistryName().toString() + "_crystalline_pink_slime_ender"),
        new ItemConduitSubtype(modObject.getUnlocalisedName() + "_melodic_ender", modObject.getRegistryName().toString() + "_melodic_ender"),
        new ItemConduitSubtype(modObject.getUnlocalisedName() + "_stellar_ender", modObject.getRegistryName().toString() + "_stellar_ender"));
    ConduitRegistry.register(ConduitBuilder.start().setUUID(new ResourceLocation(EnderIO.DOMAIN, "fluid")).setClass(getBaseConduitType())
        .setOffsets(Offset.WEST, Offset.NORTH, Offset.WEST, Offset.WEST).build().setUUID(new ResourceLocation(EnderIO.DOMAIN, "liquid_conduit"))
        .setClass(LiquidConduit.class).build().setUUID(new ResourceLocation(EnderIO.DOMAIN, "advanced_liquid_conduit")).setClass(AdvancedLiquidConduit.class)
        .build().setUUID(new ResourceLocation(EnderIO.DOMAIN, "ender_liquid_conduit")).setClass(EnderLiquidConduit.class).build()
        .setUUID(new ResourceLocation(EnderIO.DOMAIN, "crystalline_ender_liquid_conduit")).setClass(CrystallineEnderLiquidConduit.class).build()
        .setUUID(new ResourceLocation(EnderIO.DOMAIN, "crystalline_pink_slime_ender_liquid_conduit"))
        .setClass(CrystallinePinkSlimeEnderLiquidConduit.class).build().setUUID(new ResourceLocation(EnderIO.DOMAIN, "melodic_ender_liquid_conduit"))
        .setClass(MelodicEnderLiquidConduit.class).build().setUUID(new ResourceLocation(EnderIO.DOMAIN, "stellar_ender_liquid_conduit"))
        .setClass(StellarEnderLiquidConduit.class).build().finish());
    ConduitDisplayMode.registerDisplayMode(new ConduitDisplayMode(getBaseConduitType(), IconEIO.WRENCH_OVERLAY_FLUID, IconEIO.WRENCH_OVERLAY_FLUID_OFF));
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void registerRenderers(@Nonnull IModObject modObject) {
    super.registerRenderers(modObject);
    ConduitBundleRenderManager.instance.getConduitBundleRenderer().registerRenderer(LiquidConduitRenderer.create());
    ConduitBundleRenderManager.instance.getConduitBundleRenderer().registerRenderer(new AdvancedLiquidConduitRenderer());
    ConduitBundleRenderManager.instance.getConduitBundleRenderer().registerRenderer(new EnderLiquidConduitRenderer());
  }

  @Override
  public @Nonnull Class<? extends IConduit> getBaseConduitType() {
    return ILiquidConduit.class;
  }

  @Override
  public IServerConduit createConduit(@Nonnull ItemStack stack, @Nonnull EntityPlayer player) {
    AbstractLiquidConduit conduit;
    if (stack.getItemDamage() == 1) {
      conduit = new AdvancedLiquidConduit();
      configureAsInput(conduit);
    } else if (stack.getItemDamage() == 2) {
      conduit = new EnderLiquidConduit();
      configureAsInput(conduit);
    } else if (stack.getItemDamage() == CrystallineEnderLiquidConduit.META) {
      conduit = new CrystallineEnderLiquidConduit();
      configureAsInput(conduit);
    } else if (stack.getItemDamage() == CrystallinePinkSlimeEnderLiquidConduit.META) {
      conduit = new CrystallinePinkSlimeEnderLiquidConduit();
      configureAsInput(conduit);
    } else if (stack.getItemDamage() == MelodicEnderLiquidConduit.META) {
      conduit = new MelodicEnderLiquidConduit();
      configureAsInput(conduit);
    } else if (stack.getItemDamage() == StellarEnderLiquidConduit.META) {
      conduit = new StellarEnderLiquidConduit();
      configureAsInput(conduit);
    } else {
      conduit = new LiquidConduit();
    }
    return conduit;
  }

  private void configureAsInput(AbstractLiquidConduit conduit) {
    for (EnumFacing dir : EnumFacing.values()) {
      conduit.setConnectionMode(dir, ConnectionMode.INPUT);
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addCommonEntries(@Nonnull ItemStack itemstack, @Nullable EntityPlayer entityplayer, @Nonnull List<String> list, boolean flag) {
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addBasicEntries(@Nonnull ItemStack itemstack, @Nullable EntityPlayer entityplayer, @Nonnull List<String> list, boolean flag) {

  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addDetailedEntries(@Nonnull ItemStack itemstack, @Nullable EntityPlayer entityplayer, @Nonnull List<String> list, boolean flag) {
    int extractRate;
    int maxIo;

    if (itemstack.getItemDamage() == 0) {
      extractRate = ConduitConfig.fluid_tier1_extractRate.get();
      maxIo = ConduitConfig.fluid_tier1_maxIO.get();
    } else if (itemstack.getItemDamage() == 1) {
      extractRate = ConduitConfig.fluid_tier2_extractRate.get();
      maxIo = ConduitConfig.fluid_tier2_maxIO.get();
    } else {
      switch (itemstack.getItemDamage()) {
      case CrystallineEnderLiquidConduit.META:
        extractRate = ConduitConfig.fluid_tier4_extractRate.get();
        maxIo = ConduitConfig.fluid_tier4_maxIO.get();
        break;
      case CrystallinePinkSlimeEnderLiquidConduit.META:
        extractRate = ConduitConfig.fluid_tier5_extractRate.get();
        maxIo = ConduitConfig.fluid_tier5_maxIO.get();
        break;
      case MelodicEnderLiquidConduit.META:
        extractRate = ConduitConfig.fluid_tier6_extractRate.get();
        maxIo = ConduitConfig.fluid_tier6_maxIO.get();
        break;
      case StellarEnderLiquidConduit.META:
        extractRate = ConduitConfig.fluid_tier7_extractRate.get();
        maxIo = ConduitConfig.fluid_tier7_maxIO.get();
        break;
      default:
        extractRate = ConduitConfig.fluid_tier3_extractRate.get();
        maxIo = ConduitConfig.fluid_tier3_maxIO.get();
        break;
      }
    }

    String mbt = " " + Lang.FLUID_MILLIBUCKETS_TICK.get();
    list.add(Lang.GUI_LIQUID_TOOLTIP_MAX_EXTRACT.get() + " " + extractRate + mbt);
    list.add(Lang.GUI_LIQUID_TOOLTIP_MAX_IO.get() + " " + maxIo + mbt);

    if (itemstack.getItemDamage() == 0) {
      SpecialTooltipHandler.addDetailedTooltipFromResources(list, "enderio.item_liquid_conduit");
    } else if (itemstack.getItemDamage() == 2) {
      SpecialTooltipHandler.addDetailedTooltipFromResources(list, "enderio.item_liquid_conduit_ender");
    } else if (itemstack.getItemDamage() >= CrystallineEnderLiquidConduit.META && itemstack.getItemDamage() <= StellarEnderLiquidConduit.META) {
      SpecialTooltipHandler.addDetailedTooltipFromResources(list, "enderio.item_liquid_conduit_ender");
    }

  }

  @Override
  public boolean shouldHideFacades(@Nonnull ItemStack stack, @Nonnull EntityPlayer player) {
    return true;
  }

}
