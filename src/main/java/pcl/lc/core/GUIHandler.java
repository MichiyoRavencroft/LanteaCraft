package pcl.lc.core;

import java.lang.reflect.Constructor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;

import pcl.lc.LanteaCraft;
import pcl.lc.util.RegistrationHelper;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		LanteaCraft.getLogger().log(Level.DEBUG, String.format("Initializing Container with ordinal %s.", ID));
		Class<? extends Container> container = RegistrationHelper.getRegisteredContainer(ID);
		if (container != null)
			try {
				LanteaCraft.getLogger().log(Level.DEBUG,
						String.format("Initializing Container of class %s.", container.getName()));
				TileEntity entity = world.getTileEntity(x, y, z);
				Constructor<?> constr = container
						.getConstructor(new Class<?>[] { entity.getClass(), EntityPlayer.class });
				Object val = constr.newInstance(entity, player);
				return val;
			} catch (Throwable t) {
				LanteaCraft.getLogger().log(Level.WARN, "Failed to create GUI component!", t);
			}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

}
