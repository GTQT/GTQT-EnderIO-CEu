package crazypants.enderio.conduits.gui;

import javax.annotation.Nonnull;

import com.enderio.core.client.gui.button.ColorButton;

import crazypants.enderio.base.conduit.IExtractor;
import crazypants.enderio.base.conduit.IGuiExternalConnection;
import crazypants.enderio.base.machine.interfaces.IRedstoneModeControlable;
import crazypants.enderio.base.machine.modes.RedstoneControlMode;
import crazypants.enderio.base.network.PacketHandler;
import crazypants.enderio.conduits.network.PacketExtractMode;

public class ConduitRedstoneModeControlable implements IRedstoneModeControlable {

  private final IExtractor con;
  private final IGuiExternalConnection gui;
  private final Runnable showRedstoneControl;
  private final Runnable hideRedstoneControl;

  public ConduitRedstoneModeControlable(@Nonnull IExtractor con, @Nonnull IGuiExternalConnection gui, @Nonnull Runnable showRedstoneControl, @Nonnull Runnable hideRedstoneControl) {
    this.con = con;
    this.gui = gui;
    this.showRedstoneControl = showRedstoneControl;
    this.hideRedstoneControl = hideRedstoneControl;
  }

  public ConduitRedstoneModeControlable(@Nonnull IExtractor con, @Nonnull IGuiExternalConnection gui, @Nonnull ColorButton colorB) {
    this(con, gui, () -> colorB.setIsVisible(true), () -> colorB.setIsVisible(false));
  }

  public void configureGUI(@Nonnull RedstoneControlMode mode) {
    if (mode == RedstoneControlMode.OFF || mode == RedstoneControlMode.ON) {
      showRedstoneControl.run();
    } else {
      hideRedstoneControl.run();
    }
  }

  @Override
  public void setRedstoneControlMode(@Nonnull RedstoneControlMode mode) {
    RedstoneControlMode curMode = getRedstoneControlMode();
    con.setExtractionRedstoneMode(mode, gui.getDir());
    configureGUI(mode);
    if (curMode != mode) {
      PacketHandler.INSTANCE.sendToServer(new PacketExtractMode(con, gui.getDir()));
    }

  }

  @Override
  @Nonnull
  public RedstoneControlMode getRedstoneControlMode() {
    return con.getExtractionRedstoneMode(gui.getDir());
  }

  @Override
  public boolean getRedstoneControlStatus() {
    return false;
  }

}
