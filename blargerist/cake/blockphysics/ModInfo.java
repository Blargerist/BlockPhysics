package blargerist.cake.blockphysics;

import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;


public class ModInfo
{
	public static final String MODID = "BlockPhysics";
	public static final String VERSION = "${version}";

	public static final Logger Log = Logger.getLogger(MODID);
	static
	{
		Log.setParent(FMLLog.getLogger());
	}
}
