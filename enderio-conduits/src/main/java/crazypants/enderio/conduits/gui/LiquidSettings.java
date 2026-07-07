package crazypants.enderio.conduits.gui;

import java.awt.Color;

import javax.annotation.Nonnull;

import com.enderio.core.client.gui.button.ColorButton;
import com.enderio.core.client.gui.button.IconButton;
import com.enderio.core.client.gui.button.ToggleButton;
import com.enderio.core.client.gui.widget.GhostSlot;
import com.enderio.core.client.render.ColorUtil;
import com.enderio.core.client.render.EnderWidget;
import com.enderio.core.client.render.RenderUtil;
import com.enderio.core.common.util.DyeColor;
import com.enderio.core.common.util.NNList;

import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.conduit.ConnectionMode;
import crazypants.enderio.base.conduit.IClientConduit;
import crazypants.enderio.base.conduit.IGuiExternalConnection;
import crazypants.enderio.base.filter.fluid.FluidFilter;
import crazypants.enderio.base.filter.fluid.IFluidFilter;
import crazypants.enderio.base.filter.gui.FilterGuiUtil;
import crazypants.enderio.base.gui.IconEIO;
import crazypants.enderio.base.gui.RedstoneModeButton;
import crazypants.enderio.base.init.ModObject;
import crazypants.enderio.base.machine.modes.RedstoneControlMode;
import crazypants.enderio.base.network.PacketHandler;
import crazypants.enderio.conduits.conduit.liquid.EnderLiquidConduit;
import crazypants.enderio.conduits.conduit.liquid.ILiquidConduit;
import crazypants.enderio.conduits.init.ConduitObject;
import crazypants.enderio.conduits.lang.Lang;
import crazypants.enderio.conduits.network.PacketEnderLiquidConduit;
import crazypants.enderio.conduits.network.PacketExtractMode;
import crazypants.enderio.util.EnumReader;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class LiquidSettings extends BaseSettingsPanel {

  static final int ID_REDSTONE_BUTTON = GuiExternalConnection.nextButtonId();

  private static final int ID_COLOR_BUTTON = GuiExternalConnection.nextButtonId();
  private static final int ID_INSERT_CHANNEL = GuiExternalConnection.nextButtonId();
  private static final int ID_EXTRACT_CHANNEL = GuiExternalConnection.nextButtonId();
  private static final int ID_ROUND_ROBIN = GuiExternalConnection.nextButtonId();
  private static final int ID_INSERT_WHITELIST = GuiExternalConnection.nextButtonId();
  private static final int ID_EXTRACT_WHITELIST = GuiExternalConnection.nextButtonId();

  private static final int INSERT_FILTER_X = 4;
  private static final int EXTRACT_FILTER_X = 104;
  private static final int FILTER_Y = 91;
  private static final int FILTER_LABEL_OFFSET_Y = 20;
  private static final int FILTER_SLOT_SIZE = 18;
  private static final int EMBEDDED_FILTER_SLOTS = 5;

  private final RedstoneModeButton<?> rsB;
  private final ColorButton colorB;
  private boolean isEnder = false;
  private final EnderLiquidConduit eCon;

  private ColorButton insertChannelB;
  private ColorButton extractChannelB;

  private final ToggleButton roundRobinB;
  private final IconButton insertWhiteListB;
  private final IconButton extractWhiteListB;
  private final NNList<GhostSlot> fluidFilterGhostSlots = new NNList<GhostSlot>();

  private final @Nonnull ILiquidConduit conduit;

  public LiquidSettings(@Nonnull final IGuiExternalConnection gui, @Nonnull IClientConduit con) {
    super(IconEIO.WRENCH_OVERLAY_FLUID, ConduitObject.item_liquid_conduit.getUnlocalisedName(), gui, con, "in_out_settings");

    conduit = (ILiquidConduit) con;
    if (con instanceof EnderLiquidConduit) {
      isEnder = true;
      eCon = (EnderLiquidConduit) con;
    } else {
      eCon = null;
    }

    int x = leftColumn + 21;
    int y = customTop;

    insertChannelB = new ColorButton(gui, ID_INSERT_CHANNEL, x, y);
    insertChannelB.setColorIndex(0);
    insertChannelB.setToolTipHeading(Lang.GUI_CONDUIT_CHANNEL.get());

    extractChannelB = new ColorButton(gui, ID_EXTRACT_CHANNEL, x + rightColumn - leftColumn, y);
    extractChannelB.setColorIndex(0);
    extractChannelB.setToolTipHeading(Lang.GUI_CONDUIT_CHANNEL.get());

    x += insertChannelB.getWidth();
    x += rightColumn - leftColumn;
    int redstoneX = x;

    colorB = new ColorButton(gui, ID_COLOR_BUTTON, redstoneX + 16, y);
    colorB.setToolTipHeading(Lang.GUI_SIGNAL_COLOR.get());
    colorB.setColorIndex(conduit.getExtractionSignalColor(gui.getDir()).ordinal());

    rsB = new RedstoneModeButton(gui, ID_REDSTONE_BUTTON, redstoneX, y, new ConduitRedstoneModeControlable(conduit, gui, colorB));

    x = redstoneX + rsB.getWidth() + colorB.getWidth();
    roundRobinB = new ToggleButton(gui, ID_ROUND_ROBIN, x, y, IconEIO.ROUND_ROBIN_OFF, IconEIO.ROUND_ROBIN);
    roundRobinB.setSelectedToolTip(Lang.GUI_ROUND_ROBIN_ENABLED.get());
    roundRobinB.setUnselectedToolTip(Lang.GUI_ROUND_ROBIN_DISABLED.get());
    roundRobinB.setPaintSelectedBorder(false);

    insertWhiteListB = new IconButton(gui, ID_INSERT_WHITELIST, leftColumn, customTop, IconEIO.FILTER_WHITELIST);
    insertWhiteListB.setToolTip(crazypants.enderio.base.lang.Lang.GUI_ITEM_FILTER_WHITELIST.get());
    extractWhiteListB = new IconButton(gui, ID_EXTRACT_WHITELIST, rightColumn, customTop, IconEIO.FILTER_WHITELIST);
    extractWhiteListB.setToolTip(crazypants.enderio.base.lang.Lang.GUI_ITEM_FILTER_WHITELIST.get());
  }

  @Override
  @Nonnull
  public ResourceLocation getTexture() {
    return isEnder ? EnderIO.proxy.getGuiTexture("filter_upgrade_settings") : super.getTexture();
  }

  @Override
  public void actionPerformed(@Nonnull GuiButton guiButton) {
    super.actionPerformed(guiButton);
    if (guiButton.id == ID_COLOR_BUTTON) {
      conduit.setExtractionSignalColor(gui.getDir(), DyeColor.fromIndex(colorB.getColorIndex()));
      PacketHandler.INSTANCE.sendToServer(new PacketExtractMode(conduit, gui.getDir()));
    } else if (guiButton.id == ID_INSERT_FILTER_OPTIONS) {
      doOpenFilterGui(FilterGuiUtil.INDEX_OUTPUT_FLUID);
      return;
    } else if (guiButton.id == ID_EXTRACT_FILTER_OPTIONS) {
      doOpenFilterGui(FilterGuiUtil.INDEX_INPUT_FLUID);
      return;
    } else if (guiButton.id == ID_INSERT_WHITELIST) {
      toggleBlacklist(false);
      return;
    } else if (guiButton.id == ID_EXTRACT_WHITELIST) {
      toggleBlacklist(true);
      return;
    }
    if (eCon != null) {
      if (guiButton.id == ID_INSERT_CHANNEL) {
        DyeColor col = EnumReader.get(DyeColor.class, insertChannelB.getColorIndex());
        eCon.setOutputColor(gui.getDir(), col);
      } else if (guiButton.id == ID_EXTRACT_CHANNEL) {
        DyeColor col = EnumReader.get(DyeColor.class, extractChannelB.getColorIndex());
        eCon.setInputColor(gui.getDir(), col);
      } else if (guiButton.id == ID_ROUND_ROBIN) {
        eCon.setRoundRobinEnabled(gui.getDir(), !eCon.isRoundRobinEnabled(gui.getDir()));
      }
      PacketHandler.INSTANCE.sendToServer(new PacketEnderLiquidConduit(eCon, gui.getDir()));
    }
  }

  private void toggleBlacklist(boolean isInput) {
    if (!isEnder || eCon == null) {
      return;
    }
    IFluidFilter filter = eCon.getFilter(gui.getDir(), isInput);
    filter.setBlacklist(!filter.isBlacklist());
    setConduitFilter(isInput, filter);
    updateWhiteListButton(filter, isInput);
  }

  private void setConduitFilter(boolean isInput, @Nonnull IFluidFilter filter) {
    eCon.setFilter(gui.getDir(), filter, isInput);
    PacketHandler.INSTANCE.sendToServer(new PacketEnderLiquidConduit(eCon, gui.getDir()));
  }

  @Override
  protected void connectionModeChanged(@Nonnull ConnectionMode conectionMode) {
    super.connectionModeChanged(conectionMode);
    if (isEnder) {
      PacketHandler.INSTANCE.sendToServer(new PacketExtractMode(eCon, gui.getDir()));
    }
    updateGuiVisibility();
  }

  @Override
  protected void initCustomOptions() {
    gui.getContainer().setInOutSlotsVisible(true, true, conduit);
    createGhostSlots();
    updateGuiVisibility();
  }

  private void createGhostSlots() {
    NNList<ItemStack> filtersAll = new NNList<>(new ItemStack(ModObject.itemFluidFilter.getItemNN()));
    NNList<ItemStack> upgrades = new NNList<>(new ItemStack(ConduitObject.item_extract_speed_upgrade.getItemNN()),
        new ItemStack(ConduitObject.item_extract_speed_downgrade.getItemNN()));
    gui.getContainer().createGhostSlots(gui.getGhostSlotHandler().getGhostSlots(), filtersAll, upgrades);
  }

  private void updateGuiVisibility() {
    rsB.onGuiInit();
    rsB.setMode(RedstoneControlMode.IconHolder.getFromMode(conduit.getExtractionRedstoneMode(gui.getDir())));
    removeFluidFilterGhostSlots();

    if (isEnder) {
      insertChannelB.onGuiInit();
      insertChannelB.setColorIndex(eCon.getOutputColor(gui.getDir()).ordinal());
      extractChannelB.onGuiInit();
      extractChannelB.setColorIndex(eCon.getInputColor(gui.getDir()).ordinal());

      roundRobinB.onGuiInit();
      roundRobinB.setSelected(eCon.isRoundRobinEnabled(gui.getDir()));

      insertWhiteListB.onGuiInit();
      extractWhiteListB.onGuiInit();
      updateWhiteListButtons();
      addFluidFilterGhostSlots();
    }

  }

  private void updateWhiteListButtons() {
    updateWhiteListButton(eCon.getFilter(gui.getDir(), false), false);
    updateWhiteListButton(eCon.getFilter(gui.getDir(), true), true);
  }

  private void updateWhiteListButton(@Nonnull IFluidFilter filter, boolean isInput) {
    IconButton whiteListB = isInput ? extractWhiteListB : insertWhiteListB;
    if (filter.isBlacklist()) {
      whiteListB.setIcon(IconEIO.FILTER_BLACKLIST);
      whiteListB.setToolTip(crazypants.enderio.base.lang.Lang.GUI_ITEM_FILTER_BLACKLIST.get());
    } else {
      whiteListB.setIcon(IconEIO.FILTER_WHITELIST);
      whiteListB.setToolTip(crazypants.enderio.base.lang.Lang.GUI_ITEM_FILTER_WHITELIST.get());
    }
  }

  private void addFluidFilterGhostSlots() {
    addFluidFilterGhostSlots(false, INSERT_FILTER_X + 1, FILTER_Y + 1);
    addFluidFilterGhostSlots(true, EXTRACT_FILTER_X + 1, FILTER_Y + 1);
  }

  private void addFluidFilterGhostSlots(boolean isInput, int x, int y) {
    IFluidFilter filter = eCon.getFilter(gui.getDir(), isInput);
    if (filter instanceof FluidFilter) {
      NNList<GhostSlot> slots = new NNList<GhostSlot>();
      ((FluidFilter) filter).createGhostSlots(slots, x, y, new Runnable() {
        @Override
        public void run() {
          setConduitFilter(isInput, filter);
          updateWhiteListButtons();
        }
      });
      fluidFilterGhostSlots.addAll(slots);
      gui.getGhostSlotHandler().getGhostSlots().addAll(slots);
    }
  }

  private void removeFluidFilterGhostSlots() {
    gui.getGhostSlotHandler().getGhostSlots().removeAll(fluidFilterGhostSlots);
    fluidFilterGhostSlots.clear();
  }

  @Override
  public void deactivate() {
    super.deactivate();
    gui.getContainer().setInOutSlotsVisible(false, false, conduit);
    removeFluidFilterGhostSlots();
    rsB.detach();
    colorB.detach();
    insertChannelB.detach();
    extractChannelB.detach();
    roundRobinB.detach();
    insertWhiteListB.detach();
    extractWhiteListB.detach();
  }

  @Override
  protected void renderCustomOptions(int top, float par1, int par2, int par3) {
    if (!isEnder) {
      return;
    }
    FontRenderer fr = gui.getFontRenderer();

    String filter = crazypants.enderio.base.lang.Lang.GUI_FLUID_FILTER.get();
    int sw = fr.getStringWidth(filter);
    int labelY = top + FILTER_LABEL_OFFSET_Y;
    fr.drawString(filter, getGuiLeft() + 50 - sw / 2, labelY, ColorUtil.getRGB(Color.darkGray));
    fr.drawString(filter, getGuiLeft() + 150 - sw / 2, labelY, ColorUtil.getRGB(Color.darkGray));

    renderFluidFilter(false, INSERT_FILTER_X, FILTER_Y);
    renderFluidFilter(true, EXTRACT_FILTER_X, FILTER_Y);
  }

  private void renderFluidFilter(boolean isInput, int filterX, int filterY) {
    int x = getGuiLeft() + filterX;
    int y = gui.getGuiTop() + filterY;

    GlStateManager.color(1, 1, 1);
    for (int i = 0; i < EMBEDDED_FILTER_SLOTS; i++) {
      EnderWidget.map.render(EnderWidget.BUTTON_DOWN, x + i * FILTER_SLOT_SIZE, y, FILTER_SLOT_SIZE, FILTER_SLOT_SIZE, 0, true);
    }

    IFluidFilter filter = eCon.getFilter(gui.getDir(), isInput);
    if (!filter.isEmpty()) {
      for (int i = 0; i < Math.min(filter.size(), EMBEDDED_FILTER_SLOTS); i++) {
        FluidStack fluid = filter.getFluidStackAt(i);
        if (fluid != null) {
          RenderUtil.renderGuiTank(fluid, 1000, 1000, x + (i * FILTER_SLOT_SIZE) + 1, y + 1, 0, 16, 16);
        }
      }
    }
  }

  private int getGuiLeft() {
    return left - 10;
  }

  @Override
  protected boolean hasFilters() {
    return false;
  }

  @Override
  protected boolean hasFilterGui(boolean input) {
    return false;
  }

  @Override
  protected boolean hasUpgrades() {
    return false;
  }

}
