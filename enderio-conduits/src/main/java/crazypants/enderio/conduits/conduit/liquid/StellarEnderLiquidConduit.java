package crazypants.enderio.conduits.conduit.liquid;

import javax.annotation.Nonnull;

import crazypants.enderio.base.conduit.IConduitTexture;
import crazypants.enderio.base.render.registry.TextureRegistry;
import crazypants.enderio.conduits.config.ConduitConfig;
import crazypants.enderio.conduits.render.ConduitTexture;

public class StellarEnderLiquidConduit extends EnderLiquidConduit {

  public static final int META = 6;
  public static final IConduitTexture ICON_KEY = new ConduitTexture(TextureRegistry.registerTexture("blocks/liquid_conduit_stellar_ender"));
  public static final IConduitTexture ICON_CORE_KEY = new ConduitTexture(TextureRegistry.registerTexture("blocks/liquid_conduit_core_stellar_ender"));

  @Override
  protected int getItemMeta() {
    return META;
  }

  @Override
  protected int getEnderConduitType() {
    return META;
  }

  @Override
  public int getExtractRate() {
    return ConduitConfig.fluid_tier7_extractRate.get();
  }

  @Override
  public int getMaxIO() {
    return ConduitConfig.fluid_tier7_maxIO.get();
  }

  @Override
  protected @Nonnull IConduitTexture getConduitTexture() {
    return ICON_KEY;
  }

  @Override
  protected @Nonnull IConduitTexture getCoreTexture() {
    return ICON_CORE_KEY;
  }

  @Override
  @Nonnull
  public EnderLiquidConduitNetwork createNetworkForType() {
    return new EnderLiquidConduitNetwork(StellarEnderLiquidConduit.class);
  }

}
