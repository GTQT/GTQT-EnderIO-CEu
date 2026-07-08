package crazypants.enderio.base.item.travelstaff;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.enderio.core.api.client.gui.IAdvancedTooltipProvider;
import com.enderio.core.client.handlers.SpecialTooltipHandler;

import crazypants.enderio.api.IModObject;
import crazypants.enderio.api.teleport.IItemOfTravel;
import crazypants.enderio.base.EnderIO;
import crazypants.enderio.base.EnderIOTab;
import crazypants.enderio.base.config.config.TeleportConfig;
import crazypants.enderio.base.teleport.TeleportStaffTravelSource;
import crazypants.enderio.base.teleport.TravelController;
import info.loenwind.autoconfig.factory.IValue;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTeleportStaff extends Item implements IItemOfTravel, IAdvancedTooltipProvider {

  private long lastBlinkTick = 0;

  public static ItemTeleportStaff create(@Nonnull IModObject modObject, @Nullable Block block) {
    return new ItemTeleportStaff(modObject);
  }

  protected ItemTeleportStaff(@Nonnull IModObject modObject) {
    setCreativeTab(EnderIOTab.tabEnderIOItems);
    modObject.apply(this);
    setMaxStackSize(1);
    setHasSubtypes(false);
  }

  @Override
  public @Nonnull ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
    ItemStack equipped = player.getHeldItem(hand);
    if (player.isSneaking()) {
      long ticksSinceBlink = EnderIO.proxy.getTickCount() - lastBlinkTick;
      if (ticksSinceBlink < 0) {
        lastBlinkTick = -1;
      }
      if (TeleportConfig.enableBlink.get() && world.isRemote && ticksSinceBlink >= TeleportConfig.blinkDelay.get()) {
        if (TravelController.doBlink(equipped, hand, player, TeleportStaffTravelSource.TELEPORT_STAFF_BLINK)) {
          player.swingArm(hand);
          lastBlinkTick = EnderIO.proxy.getTickCount();
        }
      }
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, equipped);
    }

    if (world.isRemote) {
      TravelController.activateTravelAccessable(equipped, hand, world, player, TeleportStaffTravelSource.TELEPORT_STAFF);
    }
    player.swingArm(hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, equipped);
  }

  @Override
  public boolean canDestroyBlockInCreative(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull ItemStack stack, @Nonnull EntityPlayer player) {
    return false;
  }

  @Override
  public void addCommonEntries(@Nonnull ItemStack stack, @Nullable EntityPlayer player, @Nonnull List<String> list, boolean flag) {
    SpecialTooltipHandler.addCommonTooltipFromResources(list, getUnlocalizedName());
  }

  @Override
  public void addBasicEntries(@Nonnull ItemStack stack, @Nullable EntityPlayer player, @Nonnull List<String> list, boolean flag) {
  }

  @Override
  public void addDetailedEntries(@Nonnull ItemStack stack, @Nullable EntityPlayer player, @Nonnull List<String> list, boolean flag) {
    SpecialTooltipHandler.addDetailedTooltipFromResources(list, "item.item_travel_staff");
    if (Loader.isModLoaded("journeymap")) {
      list.add(EnderIO.lang.localizeExact("item.item_teleport_staff.tooltip.waypoint"));
    }
  }

  @Override
  public boolean isActive(@Nonnull EntityPlayer ep, @Nonnull ItemStack equipped) {
    return true;
  }

  @Override
  public void extractInternal(@Nonnull ItemStack item, int power) {
  }

  @Override
  public void extractInternal(@Nonnull ItemStack item, @Nonnull IValue<Integer> power) {
  }

  @Override
  public int getEnergyStored(@Nonnull ItemStack item) {
    return Integer.MAX_VALUE;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean isFull3D() {
    return true;
  }

}
