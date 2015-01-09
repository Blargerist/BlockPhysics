package blargerist.cake.blockphysics.util;

import java.util.HashMap;
import blargerist.cake.blockphysics.ModInfo;

public class DefinitionMaps
{
	private static HashMap<String, MoveDef> moveDefMap = new HashMap<String, MoveDef>();
	private static HashMap<String, BlockDef> blockDefMap = new HashMap<String, BlockDef>();
	private static HashMap<String, String[]> blockToBlockDefMap = new HashMap<String, String[]>();
	
	
	public static void putBlockToBlockDef(String blockName, String[] metas)
	{
		if (blockToBlockDefMap.containsKey(blockName) && blockToBlockDefMap.get(blockName) != null)
		{
			throw new RuntimeException("You attempted to override pre-existing entries in the BlockToBlockDef Map: " + blockName);
		}
		else
		{
			blockToBlockDefMap.put(blockName, metas);
		}
	}
	
	public static String getBlockToBlockDef(String blockName, int meta)
	{
		if (blockToBlockDefMap.containsKey(blockName) && blockToBlockDefMap.get(blockName)[meta] != null)
		{
			return blockToBlockDefMap.get(blockName)[meta];
		}
		return "default";
	}
	
	public static BlockDef getBlockDef(String blockName, int meta)
	{
		return getBlockDef(getBlockToBlockDef(blockName, meta));
	}
	
	private static BlockDef getBlockDef(String name)
	{
		if (blockDefMap.containsKey(name) && blockDefMap.get(name) != null)
		{
			return blockDefMap.get(name);
		}
		else if (!blockDefMap.containsKey("default") || blockDefMap.get("default") == null)
		{
			putBlockDef("default", new BlockDef("default"));
		}
		return blockDefMap.get("default");
	}
	
	public static void putBlockDef(String name, BlockDef blockDef)
	{
		if (blockDefMap.containsKey(name) && moveDefMap.get(name) != null)
		{
			throw new RuntimeException("You attempted to override pre-existing entries in the BlockDef Map: " + name);
		}
		else
		{
			blockDefMap.put(name, blockDef);
		}
	}
	
	public static MoveDef getMovedef(String name)
	{
		if (moveDefMap.containsKey(name) && moveDefMap.get(name) != null)
		{
			return moveDefMap.get(name);
		}
		else if (!moveDefMap.containsKey("default") || moveDefMap.get("default") == null)
		{
			putMoveDef("default", new MoveDef("default"));
		}
		return moveDefMap.get("default");
	}
	
	public static void putMoveDef(String name, MoveDef moveDef)
	{
		if (moveDefMap.containsKey(name) && moveDefMap.get(name) != null)
		{
			throw new RuntimeException("You attempted to override pre-existing entries in the MoveDef Map: " + name);
		}
		else
		{
			moveDefMap.put(name, moveDef);
		}
	}
}