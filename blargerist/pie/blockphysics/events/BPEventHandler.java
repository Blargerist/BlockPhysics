package blargerist.pie.blockphysics.events;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import blargerist.pie.blockphysics.ModConfig;
import blargerist.pie.blockphysics.ModInfo;
import blargerist.pie.blockphysics.util.BTickList;
import blargerist.pie.blockphysics.util.ExplosionQueue;

public class BPEventHandler 
{
	public static Map<Integer, BTickList> tickListMap = new HashMap<Integer, BTickList>();
	public static Map<Integer, ExplosionQueue> explosionQueueMap = new HashMap<Integer, ExplosionQueue>();
	
	public static void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		String blockName = Block.blockRegistry.getNameForObject(block);
		ModInfo.Log.info("onNeighborBlockChange event detected - Block = " + blockName + " " + world.getBlockMetadata(x, y, z));
		int blockMeta = world.getBlockMetadata(x, y, z);
		if (ModConfig.blockMap.containsKey(blockName) && ModConfig.blockMap.get(blockName).containsKey(blockMeta))
		{
			ModInfo.Log.info("Configured block detected: Value = " + ModConfig.blockMap.get(blockName).get(blockMeta).id);
			
			if (!world.isRemote)
			{
				int worldID = world.provider.dimensionId;
				if (false == (tickListMap.containsKey(worldID) && tickListMap.get(worldID) != null))
				{
					ModInfo.Log.info("Adding world " + worldID + " to tickListMap && explosionQueueMap");
					tickListMap.put(worldID, new BTickList());
					explosionQueueMap.put(worldID, new ExplosionQueue());
				}
				ModInfo.Log.info("Block Event Coords: " + x + ":" + z);
				tickListMap.get(worldID).scheduleBlockMoveUpdate(world, x, y, z, blockName, blockMeta, false);
			}
		}
	}
	
	public static void onWorldServerTick(WorldServer server)
	{
		int worldID = server.provider.dimensionId;
		if (tickListMap.containsKey(worldID))
		{
			ModInfo.Log.info("Getting world " + worldID + " from tickListMap");
			tickListMap.get(worldID).tickMoveUpdates(server);
			explosionQueueMap.get(worldID).doNextExplosion();
		}
		else
		{
			ModInfo.Log.info("World " + worldID + " not found in tickListMap");
		}
	}
}
