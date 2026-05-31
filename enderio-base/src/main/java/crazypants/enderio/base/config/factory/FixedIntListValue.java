package crazypants.enderio.base.config.factory;

import java.util.Arrays;

import javax.annotation.Nullable;

import info.loenwind.autoconfig.factory.AbstractValue;
import info.loenwind.autoconfig.factory.ByteBufAdapters;
import info.loenwind.autoconfig.factory.IByteBufAdapter;
import info.loenwind.autoconfig.factory.IValueFactory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Property;

public class FixedIntListValue extends AbstractValue<int[]> {
  /**
   * Replaces IntListValue with the one that properly stringifies its default value.
   */

  protected FixedIntListValue(IValueFactory owner, String section, String keyname, int[] defaultValue, String text) {
    super(owner, section, keyname, defaultValue, text);
  }

  @Override
  protected @Nullable int[] makeValue() {
    int min = minValue != null ? minValue.intValue() : Integer.MIN_VALUE, max = maxValue != null ? maxValue.intValue() : Integer.MAX_VALUE;
    Property prop = owner.getConfig().get(section, keyname, defaultValue);
    prop.setLanguageKey(keyname);
    prop.setComment(getText() + " [range: " + min + " ~ " + max + ", default: " + Arrays.toString(defaultValue) + "]");
    prop.setMinValue(min);
    prop.setMaxValue(max);
    prop.setRequiresMcRestart(isStartup);
    final int[] intList = prop.getIntList();
    for (int i = 0; i < intList.length; i++) {
      intList[i] = MathHelper.clamp(intList[i], min, max);
    }
    return intList;
  }

  @Override
  protected IByteBufAdapter<int[]> getDataType() {
    return ByteBufAdapters.INTEGERARRAY;
  }

}

