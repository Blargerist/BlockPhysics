package blargerist.cake.blockphysics;

import java.util.Iterator;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.tree.*;
import blargerist.cake.blockphysics.events.BPEventHandler;
import blargerist.cake.blockphysics.network.proxies.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModInfo.MODID, version = ModInfo.VERSION, dependencies = "")
public class BlockPhysics
{
	//@SidedProxy(clientSide = "blargerist.cake.blockphysics.network.proxies.ClientProxy", serverSide = "blargerist.cake.blockphysics.network.proxies.CommonProxy")
	//public static CommonProxy proxy;
	
	//@Instance("BlockPhysics")
	//public static BlockPhysics instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModConfig.init(event.getSuggestedConfigurationFile());
		//MinecraftForge.EVENT_BUS.register(new BPEventHandler());
		//FMLCommonHandler.instance().bus().register(new BPEventHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		//proxy.initSounds();
		//EntityRegistry.registerModEntity(EntityMovingBlock.class, "entityMovingBlock", 1, ModInfo.MODID, ModConfig.fallRenderRange, 3, true);
		//proxy.initRenderers();
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