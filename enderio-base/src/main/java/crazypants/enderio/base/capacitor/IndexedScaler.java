package crazypants.enderio.base.capacitor;

import javax.annotation.Nonnull;

import crazypants.enderio.api.capacitor.Scaler;

/**
 * The IndexedScaler is s scaler that interpolates linearly between a number of points. Those points are at fixed intervals on the x-axis (one every 'scale'
 * units).
 *
 * The points are at (scale * n; keyValues[n]) for n in 0...keyValues.length-1
 *
 * Again, plotting it out is helpful.
 */
public class IndexedScaler implements Scaler {
  private final float scale;
  private final float[] keyValues;
  private final boolean supportsOverscale;

  public IndexedScaler(float scale, float... keyValues) {
    this(false, scale, keyValues);
  }

  public IndexedScaler(boolean supportsOverscale, float scale, float... keyValues) {
    this.scale = scale;
    this.keyValues = keyValues;
    this.supportsOverscale = supportsOverscale;
    if (supportsOverscale && keyValues.length <= 1) {
      throw new IllegalArgumentException("Scalers supporting overscaling must have at least 2 key values");
    }
  }

  public @Nonnull String store() {
    StringBuffer sb = new StringBuffer();
    sb.append("idx(");
    sb.append(scale);
    sb.append(")");
    for (float f : keyValues) {
      sb.append(":");
      sb.append(f);
    }
    return sb.toString();
  }

  @Override
  public float scaleValue(float idx) {
    float idx_scaled = idx / scale;
    int idxi = (int) idx_scaled;
    float idxf = idx_scaled - idxi;
    if (idxi < 0) {
      return keyValues[0];
    }
    if (idxi >= keyValues.length - 1) {
      if (supportsOverscale) {
        float base = keyValues[keyValues.length - 1], delta = keyValues[keyValues.length - 1] - keyValues[keyValues.length - 2];
        return base + delta * (idxi - (keyValues.length - 1));
      }
      return keyValues[keyValues.length - 1];
    }
    return (1 - idxf) * keyValues[idxi] + idxf * keyValues[idxi + 1];
  }
}