package crazypants.enderio.conduits.gui;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.enderio.core.api.client.gui.IGuiScreen;
import com.enderio.core.client.gui.IRenderable;
import com.enderio.core.client.gui.button.CollectionCycleButton;
import com.enderio.core.client.gui.button.CycleButton.ICycleEnum;
import com.enderio.core.common.util.NNList;

import crazypants.enderio.base.network.PacketHandler;
import crazypants.enderio.conduits.conduit.item.IItemConduit;
import crazypants.enderio.conduits.lang.Lang;
import crazypants.enderio.conduits.network.PacketItemConduitFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;

public class TicksPerExtractionButton extends CollectionCycleButton<TicksPerExtractionButton.ExtractionTickValue> {
    private IItemConduit conduit;
    private EnumFacing dir;

    public TicksPerExtractionButton(IGuiScreen gui, int id, int x, int y, int[] modes, EnumFacing dir, @Nonnull IItemConduit conduit) {
        super(gui, id, x, y, 24, NNList.wrap(Arrays.stream(modes).mapToObj(ExtractionTickValue::new).collect(Collectors.toList())));
        this.dir = dir;
        this.conduit = conduit;
        super.setMode(new ExtractionTickValue(conduit.getTicksPerExtraction(dir)));
    }

    @Override
    public void setMode(@Nonnull ExtractionTickValue newMode) {
        super.setMode(newMode);
        conduit.setTicksPerExtraction(dir, newMode.ticks);
        PacketHandler.INSTANCE.sendToServer(new PacketItemConduitFilter(conduit, dir));
    }

    public static class ExtractionTickValue implements ICycleEnum {
        public final int ticks;

        ExtractionTickValue(int ticks) {
            this.ticks = ticks;
        }

        public boolean equals(Object other) {
            return other instanceof ExtractionTickValue && other != null && ((ExtractionTickValue) other).ticks == ticks;
        }

        @Override
        public List<String> getTooltipLines() {
            return new NNList<>(Lang.GUI_EXTRACTION_TICKS.get(ticks));
        }

        @Override
        public IRenderable getRenderCallback() {
            return new IRenderable() {

                @Override
                public void render(int x, int y, int width, int height) {
                    FontRenderer font = Minecraft.getMinecraft().fontRenderer;
                    String s = Integer.toString(ticks);
                    int actualWidth = font.getStringWidth(s);
                    int actualHeight = font.FONT_HEIGHT;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0, 0, 500);
                    font.drawString(Integer.toString(ticks), x + (width - actualWidth) / 2, y + (height - actualHeight) / 2, 0);
                    GlStateManager.color(1, 1, 1, 1);
                    GlStateManager.popMatrix();
                }
                
            };
        }
    }
}
