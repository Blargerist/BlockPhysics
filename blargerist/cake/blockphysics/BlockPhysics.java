package blargerist.cake.blockphysics;

import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import blargerist.cake.blockphysics.events.BPEventHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = ModInfo.MODID, version = ModInfo.VERSION, dependencies = "")
public class BlockPhysics
{

	@Instance("BlockPhysics")
	public static BlockPhysics instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModConfig.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new BPEventHandler());
		//FMLCommonHandler.instance().bus().register(new BPEventHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
	}
}