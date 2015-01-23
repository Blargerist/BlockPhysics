package blargerist.cake.blockphysics;

import java.io.File;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import blargerist.cake.blockphysics.util.BlockDef;
import blargerist.cake.blockphysics.util.DefinitionMaps;
import blargerist.cake.blockphysics.util.MoveDef;

public class ModConfig
{
	private static Configuration config;

	public static ConfigCategory categoryMap = new ConfigCategory(null);

	public static int fallRange;
	//public static int fallRenderRange;
	//public static int maxMovingBlocks;
	//public static int maxTickTime;
	//public static int explosionStrength;
	//public static int explosionQueue;
	//public static int explosionInterval;
	//public static boolean catapult;
	//public static boolean explosionFire;

	public static void init(File file)
	{
		config = new Configuration(file);

		load();

		fallRange = config.get("Options", "Fall Range", "60", "The max distance from the player, within which the blocks will begin to move (in blocks).").getInt();
		//fallRenderRange = config.get("Options", "Fall Render Range", "30", "The max distance from the player, within which the falling blocks will be rendered (in blocks).").getInt();
		//maxMovingBlocks = config.get("Options", "Max Moving Blocks", "300", "Maximum number of moving blocks / world.").getInt();
		//maxTickTime = config.get("Options", "Max Tick Time", "1850", "The time length of one tick when falling switches off.").getInt();
		//explosionStrength = config.get("Options", "Explosion Strength", "80", "Explosion strength modifier 0 - 200 original:100, affects only blocks.").getInt();
		//explosionQueue = config.get("Options", "Explosion Queue", "200", "Max size of the explosion queue / world.").getInt();
		//explosionInterval = config.get("Options", "Explosion Interval", "1", "Interval between explosions in ticks.").getInt();
		//catapult = config.get("Options", "Catapult", "true", "Enable / disable catapult piston.").getBoolean(true);
		//explosionFire = config.get("Options", "Explosion Fire", "true", "Can explosions cause fire?").getBoolean(true);
		
		categoryMap = config.getCategory("move definitions");
		
		if (categoryMap == null || categoryMap.keySet().size() == 0)
		{
			config.get("move definitions", "brick", new String[]{"movetype:1", "slidechance:0", "ceiling:1", "smallarc:0", "bigarc:1", "corbel:1", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			config.get("move definitions", "cobblestone", new String[]{"movetype:1", "slidechance:0", "ceiling:0", "smallarc:1", "bigarc:1", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			config.get("move definitions", "dirt", new String[]{"movetype:1", "slidechance:0", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			config.get("move definitions", "grass", new String[]{"movetype:2", "slidechance:70", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			config.get("move definitions", "leaves", new String[]{"movetype:1", "slidechance:0", "ceiling:1", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:1", "attached:2", "floating:0", "branch:0"}).getStringList();
			config.get("move definitions", "sand", new String[]{"movetype:2", "slidechance:90", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			config.get("move definitions", "stone", new String[]{"movetype:1", "slidechance:0", "ceiling:0", "smallarc:2", "bigarc:2", "corbel:0", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			config.get("move definitions", "stonebrick", new String[]{"movetype:1", "slidechance:0", "ceiling:0", "smallarc:2", "bigarc:3", "corbel:2", "ncorbel:0", "hanging:0", "attached:0", "floating:0", "branch:0"}).getStringList();
			config.get("move definitions", "wall", new String[]{"movetype:1", "slidechance:0", "ceiling:1", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:1", "attached:2", "floating:0", "branch:0"}).getStringList();
			config.get("move definitions", "wood", new String[]{"movetype:1", "slidechance:0", "ceiling:1", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:6", "hanging:0", "attached:0", "floating:0", "branch:1"}).getStringList();
			config.get("move definitions", "wool", new String[]{"movetype:1", "slidechance:0", "ceiling:0", "smallarc:0", "bigarc:0", "corbel:0", "ncorbel:0", "hanging:5", "attached:5", "floating:0", "branch:0"}).getStringList();
			
			categoryMap = config.getCategory("move definitions");
		}
		
		for (String key : categoryMap.keySet())
		{
			String[] properties = categoryMap.get(key).getStringList();
			
			String id = key;
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
			int tree = 0;

			for (int i = 0; i < properties.length; i++)
			{
				int colonIndex = properties[i].indexOf(":");
				String keyString = properties[i].substring(0, colonIndex);

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
					else if (keyString.equals("tree"))
					{
						tree = value;
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
			DefinitionMaps.putMoveDef(id, new MoveDef(id, movetype, slidechance, ceiling, smallarc, bigarc, corbel, ncorbel, hanging, attached, floatingradius, floatingblock, floatingmeta, branch, tree));
		}
		
		categoryMap = config.getCategory("block definitions");
		
		if (categoryMap == null || categoryMap.keySet().size() == 0)
		{
			config.get("block definitions", "brick", new String[]{"canmove:true", "movedef:brick", "supportiveblock:true", "fragile:0", "trapping:false", "mass:1500", "strength:64000"}).getStringList();
			config.get("block definitions", "cobblestone", new String[]{"canmove:true", "movedef:cobblestone", "supportiveblock:true", "fragile:0", "trapping:false", "mass:2100", "strength:64000"}).getStringList();
			config.get("block definitions", "dirt", new String[]{"canmove:true", "movedef:dirt", "supportiveblock:false", "fragile:0", "trapping:true", "mass:1500", "strength:64000"}).getStringList();
			config.get("block definitions", "dispenser", new String[]{"canmove:no", "movedef:default", "supportiveblock:true", "fragile:0", "trapping:false", "mass:1500", "strength:64000"}).getStringList();
			config.get("block definitions", "gemblock", new String[]{"canmove:no", "movedef:default", "supportiveblock:false", "fragile:0", "trapping:false", "mass:1500", "strength:64000"}).getStringList();
			config.get("block definitions", "giantmushroom", new String[]{"canmove:false", "movedef:default", "supportiveblock:false", "fragile:1", "trapping:false", "mass:40", "strength:10"}).getStringList();
			config.get("block definitions", "grass", new String[]{"canmove:true", "movedef:grass", "supportiveblock:false", "fragile:0", "trapping:false", "mass:1500", "strength:64000"}).getStringList();
			config.get("block definitions", "leaves", new String[]{"canmove:false", "movedef:leaves", "supportiveblock:false", "fragile:1", "trapping:false", "mass:40", "strength:10"}).getStringList();
			config.get("block definitions", "misc", new String[]{"canmove:false", "movedef:default", "supportiveblock:false", "fragile:2", "trapping:false", "mass:10", "strength:0"}).getStringList();
			config.get("block definitions", "piston", new String[]{"canmove:false", "movedef:default", "supportiveblock:true", "fragile:0", "trapping:false", "mass:1500", "strength:64000"}).getStringList();
			config.get("block definitions", "plants", new String[]{"canmove:false", "movedef:default", "supportiveblock:false", "fragile:1", "trapping:false", "mass:10", "strength:0"}).getStringList();
			config.get("block definitions", "sand", new String[]{"canmove:true", "movedef:sand", "supportiveblock:false", "fragile:0", "trapping:true", "mass:1700", "strength:64000"}).getStringList();
			config.get("block definitions", "stone", new String[]{"canmove:true", "movedef:stone", "supportiveblock:true", "fragile:0", "trapping:false", "mass:2500", "strength:64000"}).getStringList();
			config.get("block definitions", "stonebrick", new String[]{"canmove:true", "movedef:stonebrick", "supportiveblock:true", "fragile:0", "trapping:false", "mass:2500", "strength:64000"}).getStringList();
			config.get("block definitions", "wall", new String[]{"canmove:true", "movedef:wall", "supportiveblock:true", "fragile:0", "trapping:false", "mass:700", "strength:64000"}).getStringList();
			config.get("block definitions", "wood", new String[]{"canmove:true", "movedef:wood", "supportiveblock:true", "fragile:1", "trapping:false", "mass:500", "strength:2000"}).getStringList();
			config.get("block definitions", "wool", new String[]{"canmove:true", "movedef:wool", "supportiveblock:false", "fragile:1", "trapping:false", "mass:10", "strength:100"}).getStringList();
			
			categoryMap = config.getCategory("block definitions");
		}
		
		for (String key : categoryMap.keySet())
		{
			String[] properties = categoryMap.get(key).getStringList();
			
			String id = key;
			boolean canMove = false;
			String moveDef = null;
			boolean supportiveBlock = false;
			int fragile = 0;
			boolean trapping = true;
			int mass = 1500;
			int strength = 64000;
			boolean hurts = true;

			for (int i = 0; i < properties.length; i++)
			{
				int colonIndex = properties[i].indexOf(":");
				String keyString = properties[i].substring(0, colonIndex);
				

				if (keyString.equals("fragile") || keyString.equals("mass") || keyString.equals("strength"))
				{
					int value = Integer.parseInt(properties[i].substring(colonIndex +1));
					if (keyString.equals("fragile"))
					{
						fragile = value;
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
				else if (keyString.equals("trapping") || keyString.equals("supportiveblock") || keyString.equals("canmove") || keyString.equals("hurts"))
				{
					Boolean value = Boolean.valueOf(properties[i].substring(colonIndex +1));
					
					if (keyString.equals("trapping"))
					{
						trapping = value;
					}
					else if (keyString.equals("supportiveblock"))
					{
						supportiveBlock = value;
					}
					else if (keyString.equals("canmove"))
					{
						canMove = value;
					}
					else if (keyString.equals("hurts"))
					{
						hurts = value;
					}
				}
				else if (keyString.equals("movedef"))
				{
					moveDef = keyString;
				}
			}
			DefinitionMaps.putBlockDef(id, new BlockDef(id, canMove, moveDef, supportiveBlock, fragile, trapping, mass, strength, hurts));
		}
		
		categoryMap = config.getCategory("blocks");

		if (categoryMap == null || categoryMap.keySet().size() == 0)
		{
			config.get("blocks", "bricks", new String[]{"minecraft:brick_block"}).getStringList();
			config.get("blocks", "cobblestone", new String[]{"minecraft:cobblestone", "minecraft:mossy_cobblestone", "minecraft:monster_egg:0:1:2:3:4:5"}).getStringList();
			config.get("blocks", "dirt", new String[]{"minecraft:dirt:0:1"}).getStringList();
			config.get("blocks", "dispenser", new String[]{"minecraft:dispenser:0:1:2:3:4:5", "minecraft:piston_extension", "minecraft:furnace:2:3:4:5", "minecraft:lit_furnace:2:3:4:5", "minecraft:dropper:0:1:2:3:4:5"}).getStringList();
			config.get("blocks", "gemblock", new String[]{"minecraft:lapis_block", "minecraft:redstone_block", "minecraft:emerald_block"}).getStringList();
			config.get("blocks", "giantmushroom", new String[]{"minecraft:brown_mushroom_block:0:1:2:3:4:5:6:7:8:9:10:11:12:13:14:15", "minecraft:red_mushroom_block:0:1:2:3:4:5:6:7:8:9:10:11:12:13:14:15"}).getStringList();
			config.get("blocks", "grass", new String[]{"minecraft:grass:0:1", "minecraft:farmland", "minecraft:clay", "minecraft:mycelium"}).getStringList();
			config.get("blocks", "leaves", new String[]{"minecraft:leaves:0:1:2:3:4:5:6:7:8:9:10:11:12:13:14:15", "minecraft:leaves2:0:1:4:5:8:9:12:13"}).getStringList();
			config.get("blocks", "misc", new String[]{"minecraft:torch:1:2:3:4:5", "minecraft:wheat:0:1:2:3:4:5:6:7", "minecraft:cactus:0:1:2:3:4:5:6:7:8:9:10:11:12:13:14:15", "minecraft:reeds:0:1:2:3:4:5:6:7:8:9:10:11:12:13:14:15", "minecraft:nether_wart:0:1:2:3", "minecraft:cocoa:0:1:2:3", "minecraft:tripwire", "minecraft:string", "minecraft:flower_pot", "minecraft:carrots:0:1:2:3:4:5:6:7", "minecraft:potatoes:0:1:2:3:4:5:6:7"}).getStringList();
			config.get("blocks", "piston", new String[]{"minecraft:piston:0:1:2:3:4:5", "minecraft:sticky_piston:0:1:2:3:4:5"}).getStringList();
			config.get("blocks", "plants", new String[]{"minecraft:sapling:0:1:2:3:4:5", "minecraft:tallgrass", "minecraft:deadbush", "minecraft:yellow_flower", "minecraft:red_flower:0:1:2:3:4:5:6:7:8", "minecraft:double_plant:0:1:2:3:4:5", "minecraft:red_mushroom", "minecraft:brown_mushroom", "minecraft:snow_layer:0:1:2:3:4:5:6:7", "minecraft:glass_pane", "minecraft:pumpkin_seeds:0:1:2:3:4:5:6:7", "minecraft:melon_stem:0:1:2:3:4:5:6:7", "minecraft:vine:0:1:2:4:8", "minecraft:waterlily"}).getStringList();
			config.get("blocks", "sand", new String[]{"minecraft:sand:0:1", "minecraft:soul_sand", "minecraft:gravel"}).getStringList();
			config.get("blocks", "stone", new String[]{"minecraft:stone:0:1", "minecraft:sandstone:0:1:2"}).getStringList();
			config.get("blocks", "stonebrick", new String[]{"minecraft:stonebrick:0:1:2:3", "minecraft:quartz_block:0:1:2:3:4"}).getStringList();
			config.get("blocks", "wall", new String[]{"minecraft:cobblestone_wall:0:1"}).getStringList();
			config.get("blocks", "wood", new String[]{"minecraft:log:0:1:2:3:4:5:6:7:8:9:10:11:12:13:14:15", "minecraft:log2:0:1:4:5:8:9:12:13"}).getStringList();
			config.get("blocks", "wool", new String[]{"minecraft:wool:0:1:2:3:4:5:6:7:8:9:10:11:12:13:14:15", "minecraft:sponge:0:1"}).getStringList();
			
			categoryMap = config.getCategory("blocks");
		}

		for (String key : categoryMap.keySet())
		{
			String[] properties = categoryMap.get(key).getStringList();
			
			
			for (int i = 0; i < properties.length; i++)
			{
				int index = properties[i].indexOf(":");
				int index1 = properties[i].indexOf(":", index +1);
				String block;
				if (index1 != -1)
				{
					block = properties[i].substring(0, index1);
				}
				else
				{
					block = properties[i].substring(0);
				}
				int meta = 0;
				String[] blockDefs = new String[16];
				
				while (index1 != -1)
				{
					int index2 = properties[i].indexOf(":", index1 +1);
					String subString1;
					
					if (index2 != -1)
					{
						subString1 = properties[i].substring(index1 +1, index2);
					}
					else
					{
						subString1 = properties[i].substring(index1 +1);
					}
					
					meta = Integer.parseInt(subString1);
					blockDefs[meta] = key;
					index1 = index2;
				}
				if (meta == 0)
				{
					blockDefs[meta] = key;
				}
				DefinitionMaps.putBlockToBlockDef(block, blockDefs);
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