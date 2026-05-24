package crazypants.enderio.base.config.config;

import crazypants.enderio.base.config.factory.IValueFactoryEIO;
import info.loenwind.autoconfig.factory.IValue;

public final class MachineConfig {

  public static final IValueFactoryEIO F = BaseConfig.F.section("machines");

  public static final IValue<Integer> sleepBetweenFailedTries = F.make("sleepBetweenFailedTries", 20, //
      "When a machine doesn't find a recipe for its inputs, how long (in ticks) should it wait until retrying? "
          + "Increasing this can increase tps on bigger servers but will create awkward pauses until machines (re-)start after being idle or out of power.")
      .setRange(5, 20 * 30).sync();

  public static final IValue<Float> globalPowerMultiplier = F.make("globalPowerMultiplier", 1.0f,
      "Global power multiplier for all EnderIO machines. Applies to the power use, power storage, I/O rate, recipe costs, energy loss for simple machines, and RF/t output from generators. Does not apply to generator efficiency or conduits. Warning: high values with unchanged generatorEfficiencyMultiplier may cause Combustion Generator to generate more power than expected due to rounding.")
      .setRange(0.01f, 100.0f).sync();

  public static final IValue<Float> generatorEfficiencyMultiplier = F.make("generatorEfficiencyMultiplier", 1.0f,
      "Generator efficiency multiplier. It is recommended to keep this in sync with global power multiplier.")
      .setRange(0.01f, 100.0f).sync();

}
