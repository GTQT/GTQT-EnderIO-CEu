package crazypants.enderio.base.teleport;

import java.util.Locale;

import javax.annotation.Nonnull;

import crazypants.enderio.api.teleport.ITravelSource;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.config.config.TeleportConfig;
import crazypants.enderio.base.sound.IModSound;
import crazypants.enderio.base.sound.SoundRegistry;
import net.minecraft.util.ResourceLocation;

public enum TeleportStaffTravelSource implements ITravelSource {

  TELEPORT_STAFF(SoundRegistry.TRAVEL_SOURCE_ITEM) {
    @Override
    public int getMaxDistanceTravelled() {
      return TeleportConfig.rangeTeleportStaff2Block.get();
    }
  },
  TELEPORT_STAFF_BLINK(SoundRegistry.TRAVEL_SOURCE_ITEM) {
    @Override
    public boolean getConserveMomentum() {
      return true;
    }

    @Override
    public int getMaxDistanceTravelled() {
      return TeleportConfig.rangeTeleportStaff2Blink.get();
    }
  };

  private final @Nonnull ResourceLocation registryName;
  private final @Nonnull IModSound sound;

  TeleportStaffTravelSource(@Nonnull IModSound sound) {
    this.registryName = new ResourceLocation(EnderIO.DOMAIN, name().toLowerCase(Locale.ENGLISH));
    this.sound = sound;
  }

  @Override
  public @Nonnull IModSound getSound() {
    return sound;
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
