package com.enderio.core.client.gui.button;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.enderio.core.api.client.gui.IGuiScreen;
import com.enderio.core.common.util.DyeColor;

import net.minecraft.item.ItemDye;
import net.minecraft.util.math.MathHelper;

public class ColorButton extends CycleButton<DyeColor> {

  private int colorIndex = 0;

  private @Nonnull String tooltipPrefix = "";

  public ColorButton(@Nonnull IGuiScreen gui, int id, int x, int y) {
    super(gui, id, x, y, DyeColor.class);
  }

  public @Nonnull String getTooltipPrefix() {
    return tooltipPrefix;
  }

  @Override
  public void setMode(DyeColor newMode) {
    setColorIndex(newMode.ordinal());
    try {
      gui.doActionPerformed(this);
    } catch (IOException e) {
      // wtf?
    }
  }

  public void setToolTipHeading(@Nullable String tooltipPrefix) {
    if (tooltipPrefix == null) {
      this.tooltipPrefix = "";
    } else {
      this.tooltipPrefix = tooltipPrefix;
    }
  }

  public int getColorIndex() {
    return colorIndex;
  }

  public void setColorIndex(int colorIndex) {
    this.colorIndex = MathHelper.clamp(colorIndex, 0, ItemDye.DYE_COLORS.length - 1);
    DyeColor color = DyeColor.values()[colorIndex];
    super.setMode(color);
    String colStr = color.getLocalisedName();
    if (tooltipPrefix.length() > 0) {
      setToolTip(tooltipPrefix, colStr);
    } else {
      setToolTip(colStr);
    }
  }
}
