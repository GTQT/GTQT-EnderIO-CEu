package com.enderio.core.client.gui.button;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.enderio.core.api.client.gui.IGuiScreen;
import com.enderio.core.api.client.render.IWidgetIcon;
import com.enderio.core.client.gui.IRenderable;
import com.enderio.core.client.gui.button.CycleButton.ICycleEnum;
import com.enderio.core.common.util.NNList;

/**
 * A button which automatically parses enum constants and cycles between them when clicked.
 *
 * @param <T>
 *          The enum type for this button.
 */
public class CycleButton<T extends Enum<T> & ICycleEnum> extends CollectionCycleButton<T> {

  public interface ICycleEnum {

    /**
     * @return The icon to display when the button has selected this mode.
     */
    @Nullable
    default IWidgetIcon getIcon() {
      return null;
    }

    /**
     * @return the render callback to display when the button has selected this mode
     */
    @Nullable
    default IRenderable getRenderCallback() {
      return null;
    }

    /**
     * @return Localized tooltip lines.
     */
    @Nonnull
    List<String> getTooltipLines();
  }

  public CycleButton(@Nonnull IGuiScreen gui, int id, int x, int y, @Nonnull Class<T> clazz) {
    super(gui, id, x, y, NNList.of(clazz));
  }

  public CycleButton(@Nonnull IGuiScreen gui, int id, int x, int y, @Nonnull Class<T> clazz, IButtonProcessor ownerScreen) {
    super(gui, id, x, y, 16, NNList.of(clazz), ownerScreen);
  }
}
