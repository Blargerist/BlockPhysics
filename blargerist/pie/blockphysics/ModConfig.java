package blargerist.pie.blockphysics;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import blargerist.pie.blockphysics.util.BlockDef;
import blargerist.pie.blockphysics.util.MoveDef;

public class ModConfig
{
	private static Configuration config;

	public static Map<Integer, BlockDef> blockDefMap = new HashMap<Integer, BlockDef>();
	public static Map<Integer, MoveDef> moveDefMap = new HashMap<Integer, MoveDef>();
	public static Map<String, Map<Integer, BlockDef>> blockMap = new HashMap<String, Map<Integer, BlockDef>>();
	public static ConfigCategory categoryMap = new ConfigCategory(null);

	public static int fallRange;
	public static int fallRenderRange;
	public static int maxMovingBlocks;
	public static int maxTickTime;
	public static int explosionStrength;
	public static int explosionQueue;
	public static int explosionInterval;
	public static boolean catapult;
	public static boolean explosionFire;

	public static void init(File file)
	{
		config = new Configuration(file);

		load();

		fallRange = config.get("Options", "Fall Range", "60", "The max distance from the player, within which the blocks will begin to move (in blocks).").getInt();
		fallRenderRange = config.get("Options", "Fall Render Range", "30", "The max distance from the player, within which the falling blocks will be rendered (in blocks).").getInt();
		maxMovingBlocks = config.get("Options", "Max Moving Blocks", "300", "Maximum number of moving blocks / world.").getInt();
		maxTickTime = config.get("Options", "Max Tick Time", "1850", "The time length of one tick when falling switches off.").getInt();
		explosionStrength = config.get("Options", "Explosion Strength", "80", "Explosion strength modifier 0 - 200 original:100, affects only blocks.").getInt();
		explosionQueue = config.get("Options", "Explosion Queue", "200", "Max size of the explosion queue / world.").getInt();
		explosionInterval = config.get("Options", "Explosion Interval", "1", "Interval between explosions in ticks.").getInt();
		catapult = config.get("Options", "Catapult", "true", "Enable / disable catapult piston.").getBoolean(true);
		explosionFire = config.get("Options", "Explosion Fire", "true", "Can explosions cause fire?").getBoolean(true);
		
		categoryMap = config.getCategory("move definitions");
		
		if (categoryMap == null || categoryMap.keySet().size() == 0)
		{
			config.get("move definitions", "1", new String[]{"movetype:1", "slidechance:60", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			categoryMap = config.getCategory("move definitions");
		}
		
		for (String key : categoryMap.keySet())
		{
			String[] properties = categoryMap.get(key).getStringList();
			Integer intKey = Integer.parseInt(key);
			
			int id = intKey;
			int movetype = 0;
			int slidechance = 0;
			boolean ceiling = false;
			int smallarc = 0;
			int bigarc = 0;
			int corbel = 0;
			int ncorbel = 0;
			int hanging = 0;
			int attached = 0;
			int floatingradius = 0;
			String floatingblock = null;
			int floatingmeta = 0;
			boolean branch = false;

			for (int i = 0; i < properties.length; i++)
			{
				int colonIndex = properties[i].indexOf(":");
				String keyString = properties[i].substring(0, colonIndex);
				moveDefMap.put(intKey, new MoveDef(intKey));

				if (!keyString.equals("floating") && !keyString.equals("branch") && !keyString.equals("ceiling"))
				{
					int value = Integer.parseInt(properties[i].substring(colonIndex +1));
					if (keyString.equals("movetype"))
					{
						movetype = value;
					}
					else if (keyString.equals("slidechance"))
					{
						slidechance = value;
					}
					else if (keyString.equals("smallarc"))
					{
						smallarc = value;
					}
					else if (keyString.equals("bigarc"))
					{
						bigarc = value;
					}
					else if (keyString.equals("corbel"))
					{
						corbel = value;
					}
					else if (keyString.equals("ncorbel"))
					{
						ncorbel = value;
					}
					else if (keyString.equals("hanging"))
					{
						hanging = value;
					}
					else if (keyString.equals("attached"))
					{
						attached = value;
					}
				}
				else if (keyString.equals("ceiling") || keyString.equals("branch"))
				{
					Boolean value = Boolean.valueOf(properties[i].substring(colonIndex +1));
					
					if (keyString.equals("ceiling"))
					{
						ceiling = value;
					}
					else if (keyString.equals("branch"))
					{
						branch = value;
					}
				}
				else if (keyString.equals("floating"))
				{
					
					int value2 = properties[i].indexOf(":", colonIndex +1);

					if (value2 != -1)
					{
						floatingradius = Integer.parseInt(properties[i].substring(colonIndex +1, value2 - 1));
						int value3 = properties[i].indexOf(":", value2);

						if (value3 != -1)
						{
							floatingblock = properties[i].substring(value2 +1, value3 - 1);
							int value4 = properties[i].indexOf(":", value3);

							if (value4 != -1)
							{
								floatingmeta = Integer.parseInt(properties[i].substring(value3 +1, value4 - 1));
							}
						}
					}
				}
			}
			moveDefMap.put(intKey, new MoveDef(id, movetype, slidechance, ceiling, smallarc, bigarc, corbel, ncorbel, hanging, attached, floatingradius, floatingblock, floatingmeta, branch));
		}
		
		categoryMap = config.getCategory("block definitions");
		
		if (categoryMap == null || categoryMap.keySet().size() == 0)
		{
			config.get("block definitions", "1", new String[]{"movenum:1", "move:0:1", "move:1:0", "moveflipnumber:0", "movechanger:0", "supportingblock:0", "fragile:0", "trapping:1", "pushtype:1", "randomtick:1", "tickrate:5", "placedmove:0", "mass:1500", "strength:64000"}).getStringList();
			categoryMap = config.getCategory("block definitions");
		}
		
		for (String key : categoryMap.keySet())
		{
			String[] properties = categoryMap.get(key).getStringList();
			Integer intKey = Integer.parseInt(key);
			
			int id = intKey;
			int movenum = 0;
			int[] move = new int[]{0, 0};
			int moveflipnumber = 0;
			int movechanger = 0;
			int supportingblock = 0;
			int fragile = 0;
			boolean trapping = true;
			int pushtype = 1;
			boolean randomtick = true;
			int tickrate = 5;
			int placedmove = 0;
			int mass = 1500;
			int strength = 64000;

			for (int i = 0; i < properties.length; i++)
			{
				int colonIndex = properties[i].indexOf(":");
				String keyString = properties[i].substring(0, colonIndex);
				

				if (!keyString.equals("move") && !keyString.equals("randomtick") && !keyString.equals("trapping"))
				{
					int value = Integer.parseInt(properties[i].substring(colonIndex +1));
					if (keyString.equals("movenum"))
					{
						movenum = value;
					}
					else if (keyString.equals("moveflipnumber"))
					{
						moveflipnumber = value;
					}
					else if (keyString.equals("movechanger"))
					{
						movechanger = value;
					}
					else if (keyString.equals("supportingblock"))
					{
						supportingblock = value;
					}
					else if (keyString.equals("fragile"))
					{
						fragile = value;
					}
					else if (keyString.equals("pushtype"))
					{
						pushtype = value;
					}
					else if (keyString.equals("tickrate"))
					{
						tickrate = value;
					}
					else if (keyString.equals("placedmove"))
					{
						placedmove = value;
					}
					else if (keyString.equals("mass"))
					{
						mass = value;
					}
					else if (keyString.equals("strength"))
					{
						strength = value;
					}
				}
				else if (keyString.equals("trapping") || keyString.equals("randomtick"))
				{
					Boolean value = Boolean.valueOf(properties[i].substring(colonIndex +1));
					
					if (keyString.equals("trapping"))
					{
						trapping = value;
					}
					else if (keyString.equals("randomtick"))
					{
						randomtick = value;
					}
				}
				else if (keyString.equals("move"))
				{
					int index = properties[i].indexOf(":", colonIndex +1);
					
					if (index != -1)
					{
						int value1 = Integer.parseInt(properties[i].substring(colonIndex +1, index));
						int value2 = Integer.parseInt(properties[i].substring(index +1));
						
						if (value1 == 0)
						{
							move[0] = value2;
						}
						else if (value1 == 1)
						{
							move[1] = value2;
						}
					}
				}
			}
			blockDefMap.put(intKey, new BlockDef(id, movenum, move[0], move[1], moveflipnumber, movechanger, supportingblock, fragile, trapping, pushtype, randomtick, tickrate, placedmove, mass, strength));
		}
		
		categoryMap = config.getCategory("blocks");

		if (categoryMap == null || categoryMap.keySet().size() == 0)
		{
			config.get("blocks", "10", new String[]{Block.blockRegistry.getNameForObject(Blocks.grass) + ":0", Block.blockRegistry.getNameForObject(Blocks.air) + ":0"}).getStringList();
			categoryMap = config.getCategory("blocks");
		}

		for (String key : categoryMap.keySet())
		{
			String[] properties = categoryMap.get(key).getStringList();
			
			Integer intKey = Integer.parseInt(key);
			if (!blockDefMap.containsKey(intKey))
			{
				ModInfo.Log.info("Adding new blockDef: " + intKey);
				blockDefMap.put(intKey, new BlockDef(intKey));
			}
			
			for (int i = 0; i < properties.length; i++)
			{
				int index = properties[i].indexOf(":");
				int index1 = properties[i].indexOf(":", index +1);
				
				if (index1 != -1)
				{
					String block = properties[i].substring(0, index1);
					String subString1 = properties[i].substring(index1 +1);
					Integer meta = Integer.parseInt(subString1);
					ModInfo.Log.info("CATSNHATS : " + properties[i] + " : " + block + " : " + meta);
					Map<Integer, BlockDef> mapy = new HashMap<Integer, BlockDef>();
					mapy.put(meta, blockDefMap.get(intKey));
					blockMap.put(block, mapy);
					ModInfo.Log.info("'move[0]' = " + blockDefMap.get(intKey).move[0] + " in blockDef: " + intKey);
					ModInfo.Log.info("'move[1]' = " + blockDefMap.get(intKey).move[1] + " in blockDef: " + intKey);
				}
				else
				{
					ModInfo.Log.info("CATSNHATS" + properties[i]);
					Map<Integer, BlockDef> mapy = new HashMap<Integer, BlockDef>();
					mapy.put(0, blockDefMap.get(intKey));
					blockMap.put(properties[i], mapy);
					ModInfo.Log.info("'move[0]' = " + blockDefMap.get(intKey).move[0] + " in blockDef: " + intKey);
					ModInfo.Log.info("'move[1]' = " + blockDefMap.get(intKey).move[1] + " in blockDef: " + intKey);
				}
			}
		}
		
		save();
	}

	public static void save()
	{
		config.save();
	}

	public static void load()
	{
		config.load();
	}
	
	public static void readBlocks()
	{
		
	}
}