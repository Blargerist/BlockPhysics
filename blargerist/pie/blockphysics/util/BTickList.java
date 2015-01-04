package blargerist.pie.blockphysics.util;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import blargerist.pie.blockphysics.BlockPhysics;
import blargerist.pie.blockphysics.ModConfig;
import blargerist.pie.blockphysics.ModInfo;

public class BTickList
{
	private ArrayList<BTickListEntry> bticklist;
	
	public BTickList()
	{
		bticklist = new ArrayList<BTickListEntry>();
	}
	
	public void scheduleBlockMoveUpdate(World world, int x, int y, int z, String blockName, int blockMeta, boolean slide)
	{
		if (ModConfig.blockMap.containsKey(blockName) && ModConfig.blockMap.get(blockName).containsKey(blockMeta))
		{
			
			if (bticklist.size() <= 10000)
			{
				
				byte offset = 8;
				
				if (world.checkChunksExist(x - offset, y - offset, z - offset, x + offset, y + offset, z + offset))
				{
					ModInfo.Log.info("Adding block to world tick list: " + blockName + ":" + blockMeta);
					BTickListEntry entry = new BTickListEntry(x, y, z, slide, (long) ModConfig.blockMap.get(blockName).get(blockMeta).tickrate + world.getWorldInfo().getWorldTime());
					this.bticklist.add(entry);
				}
			}
		}
	}
	
	public void tickMoveUpdates(World world)
	{
		if (!BlockPhysics.skipMove)
		{
			
			int listSize = getSize();
			
			if (listSize == 0)
				return;
			else if (listSize > 1000)
				listSize = 1000;
			
			for (int i = 0; i < listSize; ++i)
			{
				BTickListEntry listEntry = (BTickListEntry) bticklist.remove(0);
				
				if (listEntry.scheduledTime <= world.getWorldInfo().getWorldTime() + 60)
				{
					BlockMove.tryToMove(world, listEntry.xCoord, listEntry.yCoord, listEntry.zCoord, Block.blockRegistry.getNameForObject(world.getBlock(listEntry.xCoord, listEntry.yCoord, listEntry.zCoord)), world.getBlockMetadata(listEntry.xCoord, listEntry.yCoord, listEntry.zCoord), listEntry.slide);
				}
			}
		}
	}
	
	public int getSize()
	{
		if (bticklist == null)
			return 0;
		return bticklist.size();
	}
	
	public void reset()
	{
		bticklist.clear();
	}
}