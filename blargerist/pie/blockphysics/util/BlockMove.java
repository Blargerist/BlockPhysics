package blargerist.pie.blockphysics.util;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import blargerist.pie.blockphysics.ModConfig;
import blargerist.pie.blockphysics.ModInfo;
import blargerist.pie.blockphysics.events.BPEventHandler;

public class BlockMove
{
	
	protected static Random rand = new Random();
	static int nextrand = 0;
	final static byte prand[] = {
		71, 65, 10, 40, 75, 97, 98, 39, 50, 47, 52, 65, 22, 32, 46, 86, 84, 22, 10, 41, 
		45, 15, 65, 67, 91, 28, 83, 49, 83, 13, 77, 89, 90, 38, 67, 69, 36, 30, 1, 41, 
		30, 79, 87, 95, 48, 14, 42, 8, 19, 22, 73, 84, 99, 7, 7, 72, 15, 63, 94, 34, 27, 
		31, 79, 85, 62, 68, 11, 86, 10, 83, 54, 4, 74, 78, 45, 26, 56, 7, 45, 25, 58, 90, 
		79, 68, 21, 62, 1, 89, 32, 5, 17, 65, 59, 34, 71, 87, 0, 39, 5, 32, 43, 64, 78, 
		27, 24, 2, 53, 37, 63, 57, 31, 51, 51, 49, 47, 42, 11, 31, 26, 41, 2, 10, 60, 1, 
		21, 45, 14, 18, 76, 75, 29, 32, 36, 60, 88, 51, 62, 55, 57, 3, 20, 80, 2, 0, 35, 
		42, 57, 34, 56, 89, 53, 42, 57, 55, 61, 11, 1, 26, 74, 35, 33, 17, 8, 16, 98, 5, 
		35, 76, 53, 58, 95, 55, 98, 70, 60, 24, 12, 39, 93, 43, 35, 66, 78, 87, 25, 20, 
		68, 33, 84, 6, 23, 13, 24, 20, 30, 0, 46, 49, 38, 76, 70, 49, 85, 31, 72, 18, 50, 
		88, 4, 18, 75, 96, 43, 28, 93, 38, 21, 71, 69, 19, 53, 91, 48, 29, 88, 89, 37, 
		59, 68, 93, 66, 44, 99, 40, 31, 27, 56, 46, 12, 92, 60, 30, 76, 26, 9, 99, 36, 
		77, 70, 9, 29, 12, 66, 3, 33, 43, 19, 3, 97, 81, 67, 72, 97, 32, 28, 96, 62, 71, 
		74, 61, 80, 93, 61, 57, 46, 18, 34, 79, 18, 48, 86, 94, 61, 6, 97, 17, 81, 68, 
		51, 29, 17, 92, 82, 62, 91, 39, 36, 64, 41, 85, 56, 66, 13, 59, 69, 37, 3, 76, 
		2, 28, 21, 36, 54, 49, 64, 87, 63, 23, 10, 78, 23, 8, 74, 54, 33, 86, 25, 44, 
		83, 6, 14, 3, 50, 38, 73, 5, 65, 55, 9, 15, 82, 82, 22, 99, 22, 2, 52, 81, 16, 
		27, 90, 75, 67, 60, 40, 52, 0, 29, 73, 26, 69, 5, 44, 50, 4, 0, 59, 82, 40, 17, 
		75, 12, 13, 99, 73, 72, 4, 25, 29, 55, 77, 80, 46, 74, 92, 44, 85, 88, 48, 84, 
		71, 90, 91, 6, 7, 78, 97, 20, 45, 11, 24, 4, 34, 59, 92, 80, 30, 40, 33, 7, 37, 
		43, 8, 13, 14, 54, 84, 12, 23, 86, 56, 9, 89, 73, 53, 8, 9, 93, 81, 85, 96, 28, 
		20, 14, 64, 80, 19, 51, 79, 16, 82, 19, 16, 38, 21, 63, 83, 98, 69, 77, 81, 77, 
		42, 35, 95, 58, 1, 94, 72, 15, 95, 48, 6, 44, 98, 91, 52, 67, 27, 96, 47, 88, 96, 
		15, 90, 25, 50, 61, 47, 66, 94, 16, 64, 87, 39, 58, 52, 47, 41, 58, 63, 70, 54, 
		37, 94, 23, 70, 11, 24, 95
		};
	
	public static boolean canMoveTo(World world, int x, int y, int z, int mass)
	{
		Block block = world.getBlock(x, y, z);
		String blockName = Block.blockRegistry.getNameForObject(block);
		int blockMeta = world.getBlockMetadata(x, y, z);
		
		if (blockName.equals("minecraft:air") || blockName.equals("minecraft:water") || blockName.equals("minecraft:lava") || blockName.equals("minecraft:fire") || block.getMaterial().isLiquid() || block.getMaterial() == Blocks.air.getMaterial())
		{
			return true;
		}
		
		if (ModConfig.blockMap.containsKey(blockName) && ModConfig.blockMap.get(blockName).containsKey(blockMeta) && ModConfig.blockMap.get(blockName).get(blockMeta).fragile > 0 && mass > ModConfig.blockMap.get(blockName).get(blockMeta).strength)
		{
			return true;
		}
		return false;
	}
	
	public static boolean canmove(World world, int x, int y, int z, BlockPistonBase blockPiston)
    {
    	int orient = blockPiston.getPistonOrientation(world.getBlockMetadata(x, y, z));
    	if ( orient > 5 ) orient = 0;
    	int faceX = x + Facing.offsetsXForSide[orient];
    	int faceY = y + Facing.offsetsYForSide[orient];
    	int faceZ = z + Facing.offsetsZForSide[orient];
    	
    	Block block = world.getBlock(faceX, faceY, faceZ);
    	String blockName = Block.blockRegistry.getNameForObject(block);
    	
    	if (true != (block instanceof BlockPistonMoving || block instanceof BlockPistonExtension)) return true;
    	
    	int orient2 = blockPiston.getPistonOrientation(world.getBlockMetadata(faceX, faceY, faceZ) );
    	if ( orient2 > 5 ) orient2 = 0;
    	
    	if (block instanceof BlockPistonExtension && orient == orient2 ) return false;
    	if (block instanceof BlockPistonMoving)
    	{
    		TileEntity tileEntity = world.getTileEntity(faceX, faceY, faceZ);
            if (tileEntity instanceof TileEntityPiston)
            {
            	if ( ((TileEntityPiston)tileEntity).getPistonOrientation() == orient ) 
            	{
            		return false;
            	}
            }
    	}
    	return true;
    }
	
	public static boolean tryToMove(World world, int i, int j, int k, String blockName, int meta, boolean contslide)
	{
		Block block = world.getBlock(i, j, k);
		int colon = blockName.indexOf(":");
		String blockLName = blockName.substring(colon +1);
		ModInfo.Log.info("tile.air = ID:" + Block.getIdFromBlock(Blocks.air));
		ModInfo.Log.info("Trying to move block: " + blockName + "-" + Block.blockRegistry.getNameForObject(block) + ":" + meta);
		
		if (world.isRemote)
		{
			ModInfo.Log.info("Return1");
			return false;
		}
		if (!ModConfig.blockMap.containsKey(blockName))
		{
			ModInfo.Log.info("Return21");
			return false;
		}
		
		if (!ModConfig.blockMap.get(blockName).containsKey(meta))
		{
			ModInfo.Log.info("Return22");
			return false;
		}
		
		if (ModConfig.blockMap.get(blockName).get(meta).movenum == 0)
		{
			ModInfo.Log.info("'movenum' = " + ModConfig.blockDefMap.get(ModConfig.blockMap.get(blockName).get(meta).id).movenum);
			ModInfo.Log.info("Return23");
			return false;
		}
		
		if (block.getMaterial() == Material.air || block instanceof BlockAir || blockName.equals("minecraft:air"))
		{
			ModInfo.Log.info("Caught Air Block");
			return false;
		}
		
		int players = world.playerEntities.size();
		
		if (players == 0)
		{
			ModInfo.Log.info("Return3");
			return false;
		}

		if (block instanceof BlockPistonBase && !canmove(world, i, j, k, (BlockPistonBase) block))
		{
			ModInfo.Log.info("Return4");
			return false;
		}
		
		boolean outofRenderRange = true;
		boolean outofFallRange = true;
		
		int ii;
		for (ii = 0; ii < players; ii++)
		{
			EntityPlayer entityplayer = (EntityPlayer) world.playerEntities.get(ii);
			ModInfo.Log.info("Player Coords: " + entityplayer.posX + ":" + entityplayer.posZ + " VS Block Coords: " + i + ":" + k);
			if (Math.abs(i - MathHelper.floor_double(entityplayer.posX)) <= ModConfig.fallRange && Math.abs(k - MathHelper.floor_double(entityplayer.posZ)) <= ModConfig.fallRange)
			{
				outofFallRange = false;
				break;
			}
		}
		
		if (outofFallRange)
		{
			ModInfo.Log.info("Distance from block > " + ModConfig.fallRange);
			return false;
		}
		ModInfo.Log.info("Return55");
		int move = 0;
		if (ModConfig.blockMap.get(blockName).get(meta).movenum == 2)
		{
			if ((getBlockBPdata(world, i, j, k) & 15) >= ModConfig.blockMap.get(blockName).get(meta).moveflipnumber)
				move = 1;
		}
		
		int movedefnum = ModConfig.blockMap.get(blockName).get(meta).move[move];
		MoveDef blockMoveDef = ModConfig.moveDefMap.get(movedefnum);
		
		if (blockMoveDef.floatingradius > 0 && blockMoveDef.floatingblock != null)
		{
			if (floating(world, i, j, k, blockMoveDef.floatingradius, blockMoveDef.floatingblock, blockMoveDef.floatingmeta))
				return false;
			move = 1;
			movedefnum = ModConfig.blockMap.get(blockName).get(meta).move[move];
			setBlockBPdata(world, i, j, k, 15);
		}
		ModInfo.Log.info("Return54");
		if (blockMoveDef.movetype == 3)
		{
			if (canMoveTo(world, i, j - 1, k, ModConfig.blockMap.get(blockName).get(meta).mass / 10))
			{
				int sv = blockMoveDef.hanging;
				if (sv > 0 && hanging(world, i, j, k, sv, blockName, meta))
					return false;
				sv = blockMoveDef.attached;
				if (sv > 0 && attached(world, i, j, k, sv, blockName, meta))
					return false;
				sv = blockMoveDef.ncorbel;
				if (sv > 0 && ncorbel(world, i, j, k, sv))
					return false;
				sv = blockMoveDef.corbel;
				if (sv > 0 && corbel(world, i, j, k, sv, blockName, meta))
					return false;
				if (blockMoveDef.ceiling && ceiling(world, i, j, k))
					return false;
				sv = blockMoveDef.smallarc;
				if (sv > 0 && smallArc(world, i, j, k, sv))
					return false;
				sv = blockMoveDef.bigarc;
				if (sv > 0 && bigArc(world, i, j, k, sv))
					return false;
				if (blockMoveDef.branch && branch(world, i, j, k, blockName, meta))
					return false;
								
				if (block.hasTileEntity(meta))
				{
					NBTTagCompound nnn = new NBTTagCompound();
					world.getTileEntity(i, j, k).writeToNBT(nnn);
					dropItemsNBT(world, i, j, k, nnn);
					world.removeTileEntity(i, j, k);
				}
				
				block.dropBlockAsItem(world, i, j, k, meta, 0);
				
				world.setBlock(i, j, k, Block.getBlockById(0), 0, 3);
				
				world.playSoundEffect((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				
				return true;
			}
			else
				return false;
		}
		ModInfo.Log.info("Return53");
		for (int iii = ii; iii < players; iii++)
		{
			EntityPlayer entityplayer = (EntityPlayer) world.playerEntities.get(iii);
			
			if (Math.abs(i - MathHelper.floor_double(entityplayer.posX)) <= ModConfig.fallRenderRange && Math.abs(k - MathHelper.floor_double(entityplayer.posZ)) <= ModConfig.fallRenderRange)
			{
				if (MathHelper.floor_double(entityplayer.posY) - j <= ModConfig.fallRenderRange)
				{
					outofRenderRange = false;
					break;
				}
			}
		}
		
		if (!outofRenderRange && world.countEntities(BlockFalling.class) >= ModConfig.maxMovingBlocks)
			return false;
		
		int movetype;
		if (contslide)
			movetype = 2;
		else
			movetype = ModConfig.moveDefMap.get(movedefnum).movetype;
		
		if (movetype == 0)
			return false;
		int ms = ModConfig.blockMap.get(blockName).get(meta).mass / 10;
		boolean canfall = canMoveTo(world, i, j - 1, k, ms);
		
		if (!canfall)
		{
			if (movetype == 1)
				return false;
			if (ModConfig.moveDefMap.get(movedefnum).slidechance != 100 && (ModConfig.moveDefMap.get(movedefnum).slidechance == 0 || ModConfig.moveDefMap.get(movedefnum).slidechance < prandnextint(100) + 1))
				return false;
		}
		ModInfo.Log.info("Return52");
		if (!contslide)
		{
			int sv = ModConfig.moveDefMap.get(movedefnum).hanging;
			if (sv > 0 && hanging(world, i, j, k, sv, blockName, meta))
				return false;
			sv = ModConfig.moveDefMap.get(movedefnum).attached;
			if (sv > 0 && attached(world, i, j, k, sv, blockName, meta))
				return false;
			sv = ModConfig.moveDefMap.get(movedefnum).ncorbel;
			if (sv > 0 && ncorbel(world, i, j, k, sv))
				return false;
			sv = ModConfig.moveDefMap.get(movedefnum).corbel;
			if (sv > 0 && corbel(world, i, j, k, sv, blockName, meta))
				return false;
			if (ModConfig.moveDefMap.get(movedefnum).ceiling && ceiling(world, i, j, k))
				return false;
			sv = ModConfig.moveDefMap.get(movedefnum).smallarc;
			if (sv > 0 && smallArc(world, i, j, k, sv))
				return false;
			sv = ModConfig.moveDefMap.get(movedefnum).bigarc;
			if (sv > 0 && bigArc(world, i, j, k, sv))
				return false;
			if (ModConfig.moveDefMap.get(movedefnum).branch && branch(world, i, j, k, blockName, meta))
				return false;
		}
		ModInfo.Log.info("Return51");
		boolean canslide[] = new boolean[4];
		if (movetype == 2 && !canfall)
		{
			canslide[0] = canMoveTo(world, i - 1, j - 1, k, ms);
			canslide[1] = canMoveTo(world, i, j - 1, k - 1, ms);
			canslide[2] = canMoveTo(world, i, j - 1, k + 1, ms);
			canslide[3] = canMoveTo(world, i + 1, j - 1, k, ms);
			if (!(canslide[0] || canslide[1] || canslide[2] || canslide[3]))
			{
				ModInfo.Log.info("Return50.1 : " + canslide[0] + " " + canslide[1] + " " + canslide[2] + " " + canslide[3]);
				return false;
			}
			
			if (canslide[0])
				canslide[0] = canMoveTo(world, i - 1, j, k, ms);
			if (canslide[1])
				canslide[1] = canMoveTo(world, i, j, k - 1, ms);
			if (canslide[2])
				canslide[2] = canMoveTo(world, i, j, k + 1, ms);
			if (canslide[3])
				canslide[3] = canMoveTo(world, i + 1, j, k, ms);
			if (!(canslide[0] || canslide[1] || canslide[2] || canslide[3]))
			{
				ModInfo.Log.info("Return50.2 : " + canslide[0] + " " + canslide[1] + " " + canslide[2] + " " + canslide[3]);
				return false;
			}
		}
		
		if (outofRenderRange)
		{
			int bpdata = getBlockBPdata(world, i, j, k);
			world.setBlock(i, j, k, Blocks.air, 0, 3);
			setBlockBPdata(world, i, j, k, 0);
			notifyMove(world, i, j, k);
			int jv = j;
			if (canfall)
			{
				for (; canMoveTo(world, i, jv - 1, k, ms) && jv > 0; jv--)
					;
				if (jv > 0)
				{
					world.setBlock(i, jv, k, block, meta, 3);
					setBlockBPdata(world, i, jv, k, bpdata);
					notifyMove(world, i, jv, k);
				}
			}
			else
			{
				byte slide[] = {0, 0, 0, 0};
				byte count = 0;
				for (byte si = 0; si < 4; si++)
				{
					if (canslide[si])
					{
						slide[count] = si;
						count++;
					}
				}
				
				int id = 0;
				int kd = 0;
				int rr = 0;
				if (count > 1)
					rr = prandnextint(count);
				switch (slide[rr])
				{
					case 0:
						id = -1;
						break;
					case 1:
						kd = -1;
						break;
					case 2:
						kd = +1;
						break;
					case 3:
						id = +1;
						break;
				}
				int iv = i + id, kv = k + kd;
				for (; canMoveTo(world, iv, jv - 1, kv, ms) && jv > 0; jv--)
					;
				if (jv > 0)
				{
					world.setBlock(iv, jv, kv, block, meta, 3);
					setBlockBPdata(world, iv, jv, kv, bpdata);
					notifyMove(world, iv, jv, kv);
				}
			}
			j++;
			tryToMove(world, i, j, k, Block.blockRegistry.getNameForObject(block), world.getBlockMetadata(i, j, k), false);
			ModInfo.Log.info("Return49");
			return true;
		}
		
		if (canfall)
		{
			ModInfo.Log.info("Block " + Block.blockRegistry.getNameForObject(block) + " can fall");
			EntityMovingBlock entityMoving = new EntityMovingBlock(world, 0.5D + i, 0.5D + j, 0.5D + k, block, meta);
			if (block.hasTileEntity(meta))
			{
				ModInfo.Log.info("Block has tileEntity");
				entityMoving.field_145810_d = new NBTTagCompound();
				world.getTileEntity(i, j, k).writeToNBT(entityMoving.field_145810_d);
				world.removeTileEntity(i, j, k);
			}
			if (canBurn(blockLName) && world.getBlock(i, j + 1, k) instanceof BlockFire)
				entityMoving.setFire(60);
			entityMoving.bpdata = getBlockBPdata(world, i, j, k);
			world.spawnEntityInWorld(entityMoving);
		}
		else
		{
			if (canslide[0])
				canslide[0] = isFallingEmpty(world, i - 1, j, k);
			if (canslide[1])
				canslide[1] = isFallingEmpty(world, i, j, k - 1);
			if (canslide[2])
				canslide[2] = isFallingEmpty(world, i, j, k + 1);
			if (canslide[3])
				canslide[3] = isFallingEmpty(world, i + 1, j, k);
			if (!(canslide[0] || canslide[1] || canslide[2] || canslide[3]))
				return false;
			
			byte slide[] = {0, 0, 0, 0};
			byte count = 0;
			for (byte si = 0; si < 4; si++)
			{
				if (canslide[si])
				{
					slide[count] = si;
					count++;
				}
			}
			
			int id = 0;
			int kd = 0;
			int rr = 0;
			if (count > 1)
				rr = prandnextint(count);
			switch (slide[rr])
			{
				case 0:
					id = -1;
					break;
				case 1:
					kd = -1;
					break;
				case 2:
					kd = +1;
					break;
				case 3:
					id = +1;
					break;
			}
			ModInfo.Log.info("Creating EntityFallingBlock");
			//int metadata = world.getBlockMetadata(i, j, k);
			EntityMovingBlock entityMoving = new EntityMovingBlock(world, 0.5D + i + 0.0625D * id, 0.5D + j - 0.0625D, 0.5D + k + 0.0625D * kd, block, meta);
			if (block.hasTileEntity(meta))
			{
				entityMoving.field_145810_d = new NBTTagCompound();
				world.getTileEntity(i, j, k).writeToNBT(entityMoving.field_145810_d);
				world.removeTileEntity(i, j, k);
			}
			
			entityMoving.slideDir = (byte) (slide[rr] + 1);
			if (canBurn(blockLName) && world.getBlock(i, j + 1, k) instanceof BlockFire)
				entityMoving.setFire(60);
			entityMoving.bpdata = getBlockBPdata(world, i, j, k);
			world.spawnEntityInWorld(entityMoving);
		}
		ModInfo.Log.info("Reached End");
		world.setBlockToAir(i, j, k);
		setBlockBPdata(world, i, j, k, 0);
		j++;
		tryToMove(world, i, j, k, Block.blockRegistry.getNameForObject(world.getBlock(i, j, k)), world.getBlockMetadata(i, j, k), false);
		return true;
	}
	
	public static boolean isFallingEmpty(World world, int i, int j, int k)
    {
        AxisAlignedBB Sandbbox;
        Sandbbox = AxisAlignedBB.getBoundingBox((float)i, (float)j, (float)k, (float)i + 1, (float)j + 1, (float)k + 1);
        List ls = world.getEntitiesWithinAABB(net.minecraft.entity.item.EntityFallingBlock.class, Sandbbox);
        if (ls.size() != 0) return false;
        return true;
    }

	public static boolean canBurn(String blockName)
	{
		BlockFire blockFire = (BlockFire) Block.getBlockFromName("fire");
		
		if (blockFire.getFlammability(Block.getBlockFromName(blockName)) != 0)
			return true;
		if (blockName.equals("hellstone"))
			return true;
		return false;
	}

	public static void notifyMove(World world, int i, int j, int k)
	{
		for (int i1 = i - 1; i1 <= i + 1; i1++)
		{
			for (int j1 = j - 1; j1 <= j + 1; j1++)
			{
				for (int k1 = k - 1; k1 <= k + 1; k1++)
					BPEventHandler.tickListMap.get(world.provider.dimensionId).scheduleBlockMoveUpdate(world, i1, j1, k1, Block.blockRegistry.getNameForObject(world.getBlock(i1, j1, k1)), world.getBlockMetadata(i1, j1, k1), false);
			}
		}
	}

	public static int prandnextint(int max)
    {
    	nextrand++;
    	if (nextrand > 99) nextrand = nextrand - 100;
    	return (int)prand[nextrand] % max;
    }

	public static void dropItemsNBT(World world, int i, int j, int k, NBTTagCompound tileEntityData)
	{
		if (tileEntityData == null)
			return;
		
		NBTTagList nbttaglist = tileEntityData.getTagList("Items", 10);
		
		for (int tl = 0; tl < nbttaglist.tagCount(); ++tl)
		{
			
			ItemStack itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbttaglist.getCompoundTagAt(tl));
			
			if (itemstack != null)
			{
				float f = rand.nextFloat() * 0.8F + 0.1F;
				float f1 = rand.nextFloat() * 0.8F + 0.1F;
				EntityItem entityitem;
				
				for (float f2 = rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
				{
					int k1 = rand.nextInt(21) + 10;
					
					if (k1 > itemstack.stackSize)
					{
						k1 = itemstack.stackSize;
					}
					
					itemstack.stackSize -= k1;
					entityitem = new EntityItem(world, (double) ((float) i + f), (double) ((float) j + f1), (double) ((float) k + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
					
					if (itemstack.hasTagCompound())
					{
						entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
					}
				}
			}
		}
	}

	public static boolean branch(World world, int x, int y, int z, String blockName, int meta)
	{
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y - 1, z)), world.getBlockMetadata(x + 1, y - 1, z), blockName, meta))
			return true;
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y - 1, z)), world.getBlockMetadata(x - 1, y - 1, z), blockName, meta))
			return true;
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z + 1)), world.getBlockMetadata(x, y - 1, z + 1), blockName, meta))
			return true;
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z - 1)), world.getBlockMetadata(x, y - 1, z - 1), blockName, meta))
			return true;
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y - 1, z + 1)), world.getBlockMetadata(x + 1, y - 1, z + 1), blockName, meta))
			return true;
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y - 1, z - 1)), world.getBlockMetadata(x - 1, y - 1, z - 1), blockName, meta))
			return true;
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y - 1, z + 1)), world.getBlockMetadata(x - 1, y - 1, z + 1), blockName, meta))
			return true;
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y - 1, z - 1)), world.getBlockMetadata(x + 1, y - 1, z - 1), blockName, meta))
			return true;
		return false;
	}

	public static boolean bigArc(World world, int i, int j, int k, int bi)
	{
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j + 1, k))).get(world.getBlockMetadata(i, j + 1, k)).supportingblock == 0)
			return false;
		
		int c;
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j + 1, k))).get(world.getBlockMetadata(i + 1, j + 1, k)).supportingblock > 0)
		{
			for (c = 1; c <= bi; c++)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j, k))).get(world.getBlockMetadata(i - c, j, k)).supportingblock > 0)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j - 1, k))).get(world.getBlockMetadata(i - c, j - 1, k)).supportingblock > 0)
						return true;
				}
				else
					break;
			}
		}
		
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j + 1, k))).get(world.getBlockMetadata(i - 1, j + 1, k)).supportingblock > 0)
		{
			for (c = 1; c <= bi; c++)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j, k))).get(world.getBlockMetadata(i + c, j, k)).supportingblock > 0)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j - 1, k))).get(world.getBlockMetadata(i + c, j - 1, k)).supportingblock > 0)
						return true;
				}
				else
					break;
			}
		}
		
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j + 1, k + 1))).get(world.getBlockMetadata(i, j + 1, k + 1)).supportingblock > 0)
		{
			for (c = 1; c <= bi; c++)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k - c))).get(world.getBlockMetadata(i, j, k - c)).supportingblock > 0)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k - c))).get(world.getBlockMetadata(i, j - 1, k - c)).supportingblock > 0)
						return true;
				}
				else
					break;
			}
		}
		
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j + 1, k - 1))).get(world.getBlockMetadata(i, j + 1, k - 1)).supportingblock > 0)
		{
			for (c = 1; c <= bi; c++)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k + c))).get(world.getBlockMetadata(i, j, k + c)).supportingblock > 0)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k + c))).get(world.getBlockMetadata(i, j - 1, k + c)).supportingblock > 0)
						return true;
				}
				else
					break;
			}
		}
		
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j + 1, k + 1))).get(world.getBlockMetadata(i + 1, j + 1, k + 1)).supportingblock > 0)
		{
			for (c = 1; c <= bi; c++)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j, k - c))).get(world.getBlockMetadata(i - c, j, k - c)).supportingblock > 0)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j - 1, k - c))).get(world.getBlockMetadata(i - c, j - 1, k - c)).supportingblock > 0)
						return true;
				}
				else
					break;
			}
		}
		
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j + 1, k - 1))).get(world.getBlockMetadata(i - 1, j + 1, k - 1)).supportingblock > 0)
		{
			for (c = 1; c <= bi; c++)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j, k + c))).get(world.getBlockMetadata(i + c, j, k + c)).supportingblock > 0)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j - 1, k + c))).get(world.getBlockMetadata(i + c, j - 1, k + c)).supportingblock > 0)
						return true;
				}
				else
					break;
			}
		}
		
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j + 1, k - 1))).get(world.getBlockMetadata(i + 1, j + 1, k - 1)).supportingblock > 0)
		{
			for (c = 1; c <= bi; c++)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j, k + c))).get(world.getBlockMetadata(i - c, j, k + c)).supportingblock > 0)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j - 1, k + c))).get(world.getBlockMetadata(i - c, j - 1, k + c)).supportingblock > 0)
						return true;
				}
				else
					break;
			}
		}
		
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j + 1, k + 1))).get(world.getBlockMetadata(i - 1, j + 1, k + 1)).supportingblock > 0)
		{
			for (c = 1; c <= bi; c++)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j, k - c))).get(world.getBlockMetadata(i + c, j, k - c)).supportingblock > 0)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j - 1, k - c))).get(world.getBlockMetadata(i + c, j - 1, k - c)).supportingblock > 0)
						return true;
				}
				else
					break;
			}
		}
		
		return false;
	}

	public static boolean smallArc(World world, int i, int j, int k, int si)
	{
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j, k))).get(world.getBlockMetadata(i - 1, j, k)).supportingblock > 0 && ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j, k))).get(world.getBlockMetadata(i + 1, j, k)).supportingblock > 0)
		{
			if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j - 1, k))).get(world.getBlockMetadata(i - 1, j - 1, k)).supportingblock > 0 || ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j - 1, k))).get(world.getBlockMetadata(i + 1, j - 1, k)).supportingblock > 0)
				return true;
			if (si > 1)
			{
				int c;
				for (c = 2; c <= si; c++)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j, k))).get(world.getBlockMetadata(i - c, j, k)).supportingblock > 0)
					{
						if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j - 1, k))).get(world.getBlockMetadata(i - c, j - 1, k)).supportingblock > 0)
							return true;
					}
					else
						break;
				}
				
				for (c = 2; c <= si; c++)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j, k))).get(world.getBlockMetadata(i + c, j, k)).supportingblock > 0)
					{
						if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j - 1, k))).get(world.getBlockMetadata(i + c, j - 1, k)).supportingblock > 0)
							return true;
					}
					else
						break;
				}
			}
			
		}
		
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k - 1))).get(world.getBlockMetadata(i, j, k - 1)).supportingblock > 0 && ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k + 1))).get(world.getBlockMetadata(i, j, k + 1)).supportingblock > 0)
		{
			if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k - 1))).get(world.getBlockMetadata(i, j - 1, k - 1)).supportingblock > 0 || ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k + 1))).get(world.getBlockMetadata(i, j - 1, k + 1)).supportingblock > 0)
				return true;
			if (si > 1)
			{
				int c;
				for (c = 2; c <= si; c++)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k - c))).get(world.getBlockMetadata(i, j, k - c)).supportingblock > 0)
					{
						if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k - c))).get(world.getBlockMetadata(i, j - 1, k - c)).supportingblock > 0)
							return true;
					}
					else
						break;
				}
				
				for (c = 2; c <= si; c++)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k + c))).get(world.getBlockMetadata(i, j, k + c)).supportingblock > 0)
					{
						if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k + c))).get(world.getBlockMetadata(i, j - 1, k + c)).supportingblock > 0)
							return true;
					}
					else
						break;
				}
			}
		}
		
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j, k + 1))).get(world.getBlockMetadata(i - 1, j, k + 1)).supportingblock > 0 && ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j, k - 1))).get(world.getBlockMetadata(i + 1, j, k - 1)).supportingblock > 0)
		{
			if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j - 1, k + 1))).get(world.getBlockMetadata(i - 1, j - 1, k + 1)).supportingblock > 0 || ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j - 1, k - 1))).get(world.getBlockMetadata(i + 1, j - 1, k - 1)).supportingblock > 0)
				return true;
			if (si > 1)
			{
				int c;
				for (c = 2; c <= si; c++)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j, k + c))).get(world.getBlockMetadata(i - c, j, k + c)).supportingblock > 0)
					{
						if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j - 1, k + c))).get(world.getBlockMetadata(i - c, j - 1, k + c)).supportingblock > 0)
							return true;
					}
					else
						break;
				}
				
				for (c = 2; c <= si; c++)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j, k - c))).get(world.getBlockMetadata(i + c, j, k - c)).supportingblock > 0)
					{
						if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j - 1, k - c))).get(world.getBlockMetadata(i + c, j - 1, k - c)).supportingblock > 0)
							return true;
					}
					else
						break;
				}
			}
		}
		
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j, k + 1))).get(world.getBlockMetadata(i + 1, j, k + 1)).supportingblock > 0 && ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j, k - 1))).get(world.getBlockMetadata(i - 1, j, k - 1)).supportingblock > 0)
		{
			if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j - 1, k + 1))).get(world.getBlockMetadata(i + 1, j - 1, k + 1)).supportingblock > 0 || ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j - 1, k - 1))).get(world.getBlockMetadata(i - 1, j - 1, k - 1)).supportingblock > 0)
				return true;
			if (si > 1)
			{
				int c;
				for (c = 2; c <= si; c++)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j, k + c))).get(world.getBlockMetadata(i + c, j, k + c)).supportingblock > 0)
					{
						if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j - 1, k + c))).get(world.getBlockMetadata(i + c, j - 1, k + c)).supportingblock > 0)
							return true;
					}
					else
						break;
				}
				
				for (c = 2; c <= si; c++)
				{
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j, k - c))).get(world.getBlockMetadata(i - c, j, k - c)).supportingblock > 0)
					{
						if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j - 1, k - c))).get(world.getBlockMetadata(i - c, j - 1, k - c)).supportingblock > 0)
							return true;
					}
					else
						break;
				}
			}
		}
		
		return false;
	}

	public static boolean ceiling(World world, int i, int j, int k)
	{
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j, k))).get(world.getBlockMetadata(i - 1, j, k)).supportingblock > 0 && ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j, k))).get(world.getBlockMetadata(i + 1, j, k)).supportingblock > 0)
			return true;
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k - 1))).get(world.getBlockMetadata(i, j, k - 1)).supportingblock > 0 && ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k + 1))).get(world.getBlockMetadata(i, j, k + 1)).supportingblock > 0)
			return true;
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j, k - 1))).get(world.getBlockMetadata(i - 1, j, k - 1)).supportingblock > 0 && ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j, k + 1))).get(world.getBlockMetadata(i + 1, j, k + 1)).supportingblock > 0)
			return true;
		if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - 1, j, k + 1))).get(world.getBlockMetadata(i - 1, j, k + 1)).supportingblock > 0 && ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + 1, j, k - 1))).get(world.getBlockMetadata(i + 1, j, k - 1)).supportingblock > 0)
			return true;
		
		return false;
	}

	public static boolean corbel(World world, int x, int y, int z, int ci, String blockName, int meta)
	{
		if (ModConfig.blockMap.get(blockName).get(meta).supportingblock == 0)
			return false;
		int c;
		for (c = 1; c <= ci; c++)
		{
			if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x + c, y, z)), world.getBlockMetadata(x + c, y, z), blockName, meta))
			{
				if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x + c, y - 1, z)), world.getBlockMetadata(x + c, y - 1, z), blockName, meta))
					return true;
			}
			else
				break;
		}
		
		for (c = 1; c <= ci; c++)
		{
			if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x - c, y, z)), world.getBlockMetadata(x - c, y, z), blockName, meta))
			{
				if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x - c, y - 1, z)), world.getBlockMetadata(x - c, y - 1, z), blockName, meta))
					return true;
			}
			else
				break;
		}
		
		for (c = 1; c <= ci; c++)
		{
			if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z + c)), world.getBlockMetadata(x, y, z + c), blockName, meta))
			{
				if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z + c)), world.getBlockMetadata(x, y - 1, z + c), blockName, meta))
					return true;
			}
			else
				break;
		}
		
		for (c = 1; c <= ci; c++)
		{
			if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z - c)), world.getBlockMetadata(x, y, z - c), blockName, meta))
			{
				if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z - c)), world.getBlockMetadata(x, y - 1, z - c), blockName, meta))
					return true;
			}
			else
				break;
		}
		
		return false;
	}

	public static boolean ncorbel(World world, int i, int j, int k, int ni)
	{
		int c;
		for (c = 1; c <= ni; c++)
		{
			if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j, k))).get(world.getBlockMetadata(i - c, j, k)).supportingblock > 0)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i - c, j - 1, k))).get(world.getBlockMetadata(i - c, j - 1, k)).supportingblock > 0)
					return true;
			}
			else
				break;
		}
		
		for (c = 1; c <= ni; c++)
		{
			if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j, k))).get(world.getBlockMetadata(i + c, j, k)).supportingblock > 0)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i + c, j - 1, k))).get(world.getBlockMetadata(i + c, j - 1, k)).supportingblock > 0)
					return true;
			}
			else
				break;
		}
		
		for (c = 1; c <= ni; c++)
		{
			if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k + c))).get(world.getBlockMetadata(i, j, k + c)).supportingblock > 0)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k + c))).get(world.getBlockMetadata(i, j - 1, k + c)).supportingblock > 0)
					return true;
			}
			else
				break;
		}
		
		for (c = 1; c <= ni; c++)
		{
			if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j, k - c))).get(world.getBlockMetadata(i, j, k - c)).supportingblock > 0)
			{
				if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(world.getBlock(i, j - 1, k - c))).get(world.getBlockMetadata(i, j - 1, k - c)).supportingblock > 0)
					return true;
			}
			else
				break;
		}
		
		return false;
	}
	
	public static boolean attached(World world, int i, int j, int k, int att, String blockName, int meta)
	{
		String blockName1;
		int meta1;
		int cc;
		for (cc = 1; cc <= att; cc++)
		{
			blockName1 = Block.blockRegistry.getNameForObject(world.getBlock(i + cc, j, k));
			meta1 = world.getBlockMetadata(i + cc, j, k);
			if (ModConfig.blockMap.get(blockName1).get(meta1).supportingblock > 0)
				return true;
			else if (!sameBlock(blockName, meta, blockName1, meta1))
				break;
		}
		
		for (cc = 1; cc <= att; cc++)
		{
			blockName1 = Block.blockRegistry.getNameForObject(world.getBlock(i - cc, j, k));
			meta1 = world.getBlockMetadata(i - cc, j, k);
			if (ModConfig.blockMap.get(blockName1).get(meta1).supportingblock > 0)
				return true;
			else if (!sameBlock(blockName, meta, blockName1, meta1))
				break;
		}
		
		for (cc = 1; cc <= att; cc++)
		{
			blockName1 = Block.blockRegistry.getNameForObject(world.getBlock(i, j, k + cc));
			meta1 = world.getBlockMetadata(i, j, k + cc);
			if (ModConfig.blockMap.get(blockName1).get(meta1).supportingblock > 0)
				return true;
			else if (!sameBlock(blockName, meta, blockName1, meta1))
				break;
		}
		
		for (cc = 1; cc <= att; cc++)
		{
			blockName1 = Block.blockRegistry.getNameForObject(world.getBlock(i, j, k - cc));
			meta1 = world.getBlockMetadata(i, j, k - cc);
			if (ModConfig.blockMap.get(blockName1).get(meta1).supportingblock > 0)
				return true;
			else if (!sameBlock(blockName, meta, blockName1, meta1))
				break;
		}
		
		return false;
	}
	
	public static boolean hanging(World world, int i, int j, int k, int hang, String blockName, int meta)
	{
		String blockName1;
		int meta1;
		j++;
		hang = j + hang;
		for (int cc = j; cc < hang; cc++)
		{
			blockName1 = Block.blockRegistry.getNameForObject(world.getBlock(i, cc, k));
			meta1 = world.getBlockMetadata(i, cc, k);
			if (ModConfig.blockMap.get(blockName1).get(meta1).supportingblock > 0)
				return true;
			else if (!sameBlock(blockName, meta, blockName1, meta1))
				return false;
			
		}
		return false;
	}
	
	public static boolean floating(World world, int x, int y, int z, int rad, String blockName, int meta)
	{
		for (int yy = y - rad; yy <= y + rad; yy++)
		{
			for (int xx = x - rad; xx <= x + rad; xx++)
			{
				for (int zz = z - rad; zz <= z + rad; zz++)
				{
					if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(xx, yy, zz)), world.getBlockMetadata(xx, yy, zz), blockName, meta))
						return true;
				}
			}
		}
		return false;
	}

	public static boolean sameBlock(String blockName1, int meta1, String blockName2, int meta2)
	{
		return blockName1 == blockName2 && meta1 == meta2;
	}
	
	public static boolean setBlockBPdata( World world, int x, int y, int z, int par4 )
    {
        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000)
        {
            if (y < 0)
            {
                return false;
            }
            else if (y >= 256)
            {
                return false;
            }
            else
            {
                Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
                int j1 = x & 15;
                int k1 = z & 15;
                return chunk.setBlockBPdata(j1, y, k1, par4);
            }
        }
        else
        {
            return false;
        }
    }

	public static int getBlockBPdata(World world, int x, int y, int z)
	{
		if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000)
		{
			if (y < 0)
			{
				return 0;
			}
			else if (y >= 256)
			{
				return 0;
			}
			else
			{
				Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
				x &= 15;
				z &= 15;
				ModInfo.Log.info("Returning BPdata: " + chunk.getBlockBPdata(x, y, z));
				return chunk.getBlockBPdata(x, y, z);
			}
		}
		else
		{
			return 0;
		}
	}
}
