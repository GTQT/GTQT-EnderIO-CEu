package crazypants.enderio.api.teleport;

import javax.annotation.Nonnull;

import crazypants.enderio.base.sound.IModSound;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ITravelSource extends IForgeRegistryEntry<ITravelSource> {

  @Nonnull
  IModSound getSound();

  default boolean getConserveMomentum() {
    return false;
  }

  default int getMaxDistanceTravelled() {
    return 0;
  }

  default int getMaxDistanceTravelledSq() {
    return getMaxDistanceTravelled() * getMaxDistanceTravelled();
  }

  default float getPowerCostPerBlockTraveledRF() {
    return 0;
  }

}
