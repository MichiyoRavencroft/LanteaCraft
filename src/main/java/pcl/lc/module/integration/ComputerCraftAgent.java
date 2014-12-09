package pcl.lc.module.integration;

import java.lang.reflect.Method;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pcl.common.helpers.RegistrationHelper;
import pcl.lc.LanteaCraft;
import pcl.lc.LanteaCraft.Blocks;
import pcl.lc.LanteaCraft.Items;
import pcl.lc.api.internal.Agent;
import pcl.lc.api.internal.IIntegrationAgent;
import pcl.lc.module.integration.computercraft.BlockComputerCraftConnector;
import pcl.lc.module.integration.computercraft.TileEntityComputerCraftConnector;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;

@Agent(modname = "ComputerCraft")
public class ComputerCraftAgent implements IIntegrationAgent {

	private class ComputerCraftProvider implements IPeripheralProvider {
		@Override
		public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
			TileEntity entity = world.getBlockTileEntity(x, y, z);
			if (entity instanceof TileEntityComputerCraftConnector)
				return (TileEntityComputerCraftConnector) entity;
			return null;
		}
	}

	private Class<?> clazz_ComputerCraftAPI;
	private Method registerHandler;
	private ComputerCraftProvider provider;

	private BlockComputerCraftConnector block_connector;

	public ComputerCraftAgent() {

	}

	@Override
	public String modName() {
		return "ComputerCraft";
	}

	@Override
	public void init() {
		try {
			clazz_ComputerCraftAPI = Class.forName("dan200.computercraft.api.ComputerCraftAPI");
			registerHandler = clazz_ComputerCraftAPI.getMethod("registerPeripheralProvider",
					new Class<?>[] { IPeripheralProvider.class });
			provider = new ComputerCraftProvider();
			registerHandler.invoke(null, provider);
		} catch (Throwable t) {
			LanteaCraft.getLogger().log(Level.INFO, "ComputerCraft not found!");
			return;
		}
		block_connector = RegistrationHelper.registerBlock(BlockComputerCraftConnector.class,
				"integration.computercraft");
		GameRegistry.registerTileEntity(TileEntityComputerCraftConnector.class, "tileEntityComputercraftAdapter");
		RegistrationHelper.newRecipe(new ItemStack(Blocks.BlockComputerCraftConnector, 1), "OOO", "OcO", "OrO",
				'O', Block.obsidian, 'r', Item.redstone, 'c', Items.controllerCrystal);

	}

}
