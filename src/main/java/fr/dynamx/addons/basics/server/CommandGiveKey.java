package fr.dynamx.addons.basics.server;

import fr.dynamx.addons.basics.common.modules.LicensePlateModule;
import fr.dynamx.addons.basics.utils.VehicleKeyUtils;
import fr.dynamx.common.entities.BaseVehicleEntity;
import fr.dynamx.common.entities.vehicles.CarEntity;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EntitySelectors;

import java.util.List;

public class CommandGiveKey extends CommandBase {
    @Override
    public String getName() {
        return "givekey";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/givekey <liscence-plate>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer p = (EntityPlayer) sender;
        if (args.length < 1) {
            if (p.isRiding()) {
                if (p.getRidingEntity() instanceof CarEntity) {
                    CarEntity car = (CarEntity) p.getRidingEntity();
                    ItemStack key = VehicleKeyUtils.getKeyForVehicle((BaseVehicleEntity<?>) car);
                    p.inventory.addItemStackToInventory(key.copy());
                }
            }
        } else if (args.length > 0) {
            List<Entity> carList;
            carList = sender.getEntityWorld().getEntities(CarEntity.class, EntitySelectors.IS_ALIVE);
            for (Entity entity : carList) {
                CarEntity car = (CarEntity) entity;
                LicensePlateModule licensePlateModule = (LicensePlateModule) car.getModuleByType(LicensePlateModule.class);
                String licensePlate = licensePlateModule.getPlate().replace(" ", "");
                if (licensePlate.equals(args[0])) {
                    ItemStack key = VehicleKeyUtils.getKeyForVehicle((BaseVehicleEntity<?>) car);
                    p.inventory.addItemStackToInventory(key.copy());
                }
            }
        }
    }
}
