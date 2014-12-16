package blargerist.cake.blockphysics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import blargerist.cake.blockphysics.asm.ClassTransformer;
import blargerist.cake.blockphysics.asm.IClassTransformerModule;

public class ModConfig
{
	public static Map<String, String> config = new HashMap<String, String>();
	private static Properties properties = new Properties();
	private static File configFile;

	public static void init(File mcLocation)
	{
		configFile = new File(mcLocation + "/config/" + ModInfo.MODID.toLowerCase() +  "ASM.cfg");

		if (configFile != null)
		{
			loadMap(configFile, config);

			if (config != null)
			{
				readConfig();
			}
			saveMap(configFile, config);
		}

	}

	public static void readConfig()
	{
		for (IClassTransformerModule transformerModule : ClassTransformer.getTransformerModules())
		{
			if (!transformerModule.canBeDisabled())
				continue;
			
			String moduleName = transformerModule.getModuleName();
			if (config.containsKey(moduleName) && !config.get(moduleName).equalsIgnoreCase("true"))
			{
				ClassTransformer.disableTransformerModule(moduleName);
			}
			else
			{
				config.put(moduleName, "true");
			}
		}
	}

	public static void saveMap(File file, Map<String, String> map)
	{
		properties.putAll(config);
		try
		{
			properties.store(new FileOutputStream(file), null);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void loadMap(File file, Map<String, String> map)
	{
		try
		{
			properties.load(new FileInputStream(file));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		for (String key : properties.stringPropertyNames())
		{
			config.put(key, properties.get(key).toString());
		}
	}
}