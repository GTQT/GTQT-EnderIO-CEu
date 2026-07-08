package crazypants.enderio.api.teleport;

import java.util.Locale;

import javax.annotation.Nonnull;

import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.config.config.TeleportConfig;
import crazypants.enderio.base.teleport.TravelSourceRegistry;
import crazypants.enderio.base.sound.IModSound;
import crazypants.enderio.base.sound.SoundRegistry;
import net.minecraft.util.ResourceLocation;

public enum TravelSource implements ITravelSource {

  BLOCK(SoundRegistry.TRAVEL_SOURCE_BLOCK) {
    @Override
    public int getMaxDistanceTravelled() {
      return TeleportConfig.rangeBlocks.get();
    }
  },
  STAFF(SoundRegistry.TRAVEL_SOURCE_ITEM) {
    @Override
    public int getMaxDistanceTravelled() {
      return TeleportConfig.rangeItem2Block.get();
    }

    @Override
    public float getPowerCostPerBlockTraveledRF() {
      return TeleportConfig.costItem2Block.get();
    }
  },
  STAFF_BLINK(SoundRegistry.TRAVEL_SOURCE_ITEM) {
    @Override
    public int getMaxDistanceTravelled() {
      return TeleportConfig.rangeItem2Blink.get();
    }

    @Override
    public float getPowerCostPerBlockTraveledRF() {
      return TeleportConfig.costItem2Blink.get();
    }
  },
  TELEPAD(SoundRegistry.TELEPAD);

  public static int getMaxDistanceSq() {
    int result = 0;
    for (ITravelSource source : TravelSourceRegistry.values()) {
      if (source.getMaxDistanceTravelled() > result) {
        result = source.getMaxDistanceTravelled();
      }
    }
    return result * result;
  }

  public final @Nonnull IModSound sound;

  private final @Nonnull ResourceLocation registryName;

  private TravelSource(IModSound sound) {
    this.sound = sound;
    this.registryName = new ResourceLocation(EnderIO.DOMAIN, name().toLowerCase(Locale.ENGLISH));
  }

  @Override
  public @Nonnull IModSound getSound() {
    return sound;
  }

  @Override
  public boolean getConserveMomentum() {
    return this == STAFF_BLINK;
  }

  @Override
  public int getMaxDistanceTravelled() {
    return 0;
  }

  @Override
  public int getMaxDistanceTravelledSq() {
    return getMaxDistanceTravelled() * getMaxDistanceTravelled();
  }

  @Override
  public float getPowerCostPerBlockTraveledRF() {
    return 0;
  }

  @Override
  public ITravelSource setRegistryName(ResourceLocation name) {
    throw new UnsupportedOperationException("Travel sources use fixed registry names");
  }

  @Override
  public @Nonnull ResourceLocation getRegistryName() {
    return registryName;
  }

  @Override
  public Class<ITravelSource> getRegistryType() {
    return ITravelSource.class;
  }

}
