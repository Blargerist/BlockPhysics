package blargerist.cake.blockphysics.asm;

import java.io.File;
import java.util.Map;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import blargerist.cake.blockphysics.ModConfig;
import squeek.asmhelper.ObfHelper;

@IFMLLoadingPlugin.MCVersion("1.6.4")
@IFMLLoadingPlugin.SortingIndex(90)
@IFMLLoadingPlugin.TransformerExclusions("blargerist.cale.blockphysics.asm")
public class ASMPlugin implements IFMLLoadingPlugin
{
	public static File gameDir = new File(".");;
	public static File bpjarFile = null;
	
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]{ClassTransformer.class.getName(), BPTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		ModConfig.init((File) data.get("mcLocation"));
		ObfHelper.setObfuscated((Boolean) data.get("runtimeDeobfuscationEnabled"));
		
    	this.gameDir = (File) data.get("mcLocation");
    	this.bpjarFile = (File) data.get("coremodLocation");
	}

	//TODO remove comment in 1.7.10 @Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}