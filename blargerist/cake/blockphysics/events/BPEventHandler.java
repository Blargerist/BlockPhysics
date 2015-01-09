package blargerist.cake.blockphysics.events;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import blargerist.cake.blockphysics.util.BlockMove;

public class BPEventHandler
{
	static ArrayList<Block> tickingBlocks = new ArrayList<Block>();

	public static void onNeighborBlockChange(World world, int x, int y, int z, Block blockNeighbor)
	{
		if (!world.isRemote)
		{
			Block block = world.getBlock(x, y, z);
			Material material = block.getMaterial();
			String blockName = Block.blockRegistry.getNameForObject(block);

			if (material == Material.air || block.getBlockHardness(world, x, y, z) == -1.0F || material.isLiquid() || material.isReplaceable() || material == Material.plants || material == Material.portal)
			{
				return;
			}

			if (!tickingBlocks.contains(block))
			{
				tickingBlocks.add(block);
				world.scheduleBlockUpdate(x, y, z, block, block.tickRate(world));
				tickingBlocks.remove(block);
			}
		}
	}

	public static void onWorldServerTick(WorldServer server)
	{
	}

	public static void onUpdateBlock(World world, int x, int y, int z, Random random)
	{
		if (!world.isRemote)
		{
			Block block = world.getBlock(x, y, z);
			String blockName = Block.blockRegistry.getNameForObject(block);
			Material material = block.getMaterial();
			if (material == Material.air || block.getBlockHardness(world, x, y, z) == -1.0F || material.isLiquid() || material.isReplaceable() || material == Material.plants || material == Material.portal)
			{
				return;
			}
			BlockMove.fall(world, x, y, z);
		}
	}

	public static boolean func_149831_e(World p_149831_0_, int p_149831_1_, int p_149831_2_, int p_149831_3_)
	{
		Block block = p_149831_0_.getBlock(p_149831_1_, p_149831_2_, p_149831_3_);

		if (block.isAir(p_149831_0_, p_149831_1_, p_149831_2_, p_149831_3_))
		{
			return true;
		}
		else if (block == Blocks.fire)
		{
			return true;
		}
		else
		{
			Material material = block.getMaterial();
			return material == Material.water ? true : material == Material.lava;
		}
	}
}