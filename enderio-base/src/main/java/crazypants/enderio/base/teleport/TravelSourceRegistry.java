package crazypants.enderio.base.teleport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.enderio.core.common.util.NNList;

import crazypants.enderio.api.teleport.ITravelSource;
import crazypants.enderio.api.teleport.TravelSource;
import crazypants.enderio.base.EnderIO;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = EnderIO.MODID)
public final class TravelSourceRegistry {

  public static final ResourceLocation NAME = new ResourceLocation(EnderIO.DOMAIN, "travel_source");

  private static IForgeRegistry<ITravelSource> registry;

  @SubscribeEvent
  public static void registerRegistry(@Nonnull RegistryEvent.NewRegistry event) {
    registry = new RegistryBuilder<ITravelSource>().setName(NAME).setType(ITravelSource.class).setMaxID(64).disableSaving().disableOverrides().create();
  }

  @SubscribeEvent
  public static void registerBuiltinSources(@Nonnull RegistryEvent.Register<ITravelSource> event) {
    registry = event.getRegistry();
    for (TravelSource source : TravelSource.values()) {
      event.getRegistry().register(source);
    }
    for (TeleportStaffTravelSource source : TeleportStaffTravelSource.values()) {
      event.getRegistry().register(source);
    }
  }

  public static @Nullable ITravelSource getValue(@Nullable ResourceLocation name) {
    return name != null && registry != null ? registry.getValue(name) : null;
  }

  public static @Nonnull Iterable<ITravelSource> values() {
    if (registry != null) {
      return registry;
    }
    NNList<ITravelSource> result = new NNList<ITravelSource>();
    for (TravelSource source : TravelSource.values()) {
      result.add(source);
    }
    return result;
  }

  private TravelSourceRegistry() {
  }

}
