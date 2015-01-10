package blargerist.cake.blockphysics.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import blargerist.cake.blockphysics.ModConfig;
import blargerist.cake.blockphysics.ModInfo;

public class BlockMove
{
	public static void fall(World world, int x, int y, int z)
	{
		if (!world.isRemote)
		{
			if (playersWithinRange(world, x, y, z))
			{
				byte b0 = 32;

				if (world.checkChunksExist(x - b0, y - b0, z - b0, x + b0, y + b0, z + b0))
				{
					Block block = world.getBlock(x, y, z);
					String blockName = Block.blockRegistry.getNameForObject(block);
					int meta = world.getBlockMetadata(x, y, z);
					BlockDef blockDef = DefinitionMaps.getBlockDef(blockName, meta);

					if (blockDef.canMove)
					{
						MoveDef moveDef = DefinitionMaps.getMovedef(blockDef.id);
						
						if (!floating(world, x, y, z, moveDef.floatingRadius, moveDef.floatingBlock, moveDef.floatingMeta))
						{
							if (moveDef.moveType != 0)//if (moveDef.moveType == 3) 3 = dropped as item
							{
								if (canMoveTo(world, x, y - 1, z, blockDef.mass / 10))
								{
									if (!hanging(world, x, y, z, moveDef.hanging, blockName, meta))
									{
										if (!attached(world, x, y, z, moveDef.attached, blockName, meta))
										{
											if (!nCorbel(world, x, y, z, moveDef.nCorbel))
											{
												if (!corbel(world, x, y, z, moveDef.corbel, blockName, meta))
												{
													if (!moveDef.ceiling || !ceiling(world, x, y, z))
													{
														if (!smallArc(world, x, y, z, moveDef.smallArc))
														{
															if (!bigArc(world, x, y, z, moveDef.bigArc))
															{
																if (!moveDef.branch || !branch(world, x, y, z, blockName, meta))
																{
																	EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block, meta);
																	entityfallingblock.func_145806_a(true);
																	world.spawnEntityInWorld(entityfallingblock);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private static boolean canMoveTo(World world, int x, int y, int z, int mass)
	{
		Block block = world.getBlock(x, y, z);
		if (block == Blocks.air)
		{
			return true;
		}
		String blockName = Block.blockRegistry.getNameForObject(block);
		int meta = world.getBlockMetadata(x, y, z);
		BlockDef blockDef = DefinitionMaps.getBlockDef(blockName, meta);
		Material material = block.getMaterial();
		return material == Material.air || material == Material.fire || material.isLiquid() || material == Material.plants || material == Material.vine || material == Material.circuits || (blockDef.fragile > 0 && blockDef.strength < mass);
	}

	public static boolean playersWithinRange(World world, int x, int y, int z)
	{
		for (Object object : world.playerEntities)
		{
			EntityPlayer player = (EntityPlayer) object;

			if (Math.abs(x - MathHelper.floor_double(player.posX)) <= ModConfig.fallRange && Math.abs(z - MathHelper.floor_double(player.posZ)) <= ModConfig.fallRange)
			{
				return true;
			}
		}
		return false;
	}

	private static boolean floating(World world, int x, int y, int z, int radius, String blockName, int meta)
	{
		for (int yy = y - radius; yy <= y + radius; yy++)
		{
			for (int xx = x - radius; xx <= x + radius; xx++)
			{
				for (int zz = z - radius; zz <= z + radius; zz++)
				{
					if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(xx, yy, zz)), world.getBlockMetadata(xx, yy, zz), blockName, meta))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean hanging(World world, int x, int y, int z, int hanging, String blockName, int meta)
	{
		if (hanging <= 0)
		{
			return false;
		}
		String blockName2;
		int meta2;
		hanging = hanging + y;

		for (int i = y; i < hanging; i++)
		{
			blockName2 = Block.blockRegistry.getNameForObject(world.getBlock(x, i, z));
			meta2 = world.getBlockMetadata(x, i, z);
			if (DefinitionMaps.getBlockDef(blockName2, meta2).supportiveBlock)
			{
				return true;
			}
			else if (sameBlock(blockName, meta, blockName2, meta2))
			{
				return false;
			}
		}
		return false;
	}

	private static boolean attached(World world, int x, int y, int z, int attached, String blockName, int meta)
	{
		if (attached <= 0)
		{
			return false;
		}

		String blockName2;
		int meta2;
		int i;

		for (i = 1; i <= attached; i++)
		{
			blockName2 = Block.blockRegistry.getNameForObject(world.getBlock(x + i, y, z));
			meta2 = world.getBlockMetadata(x + i, y, z);
			if (DefinitionMaps.getBlockDef(blockName2, meta2).supportiveBlock)
			{
				return true;
			}
			else if (!sameBlock(blockName, meta, blockName2, meta2))
			{
				break;
			}
		}
		for (i = 1; i <= attached; i++)
		{
			blockName2 = Block.blockRegistry.getNameForObject(world.getBlock(x - i, y, z));
			meta2 = world.getBlockMetadata(x - i, y, z);
			if (DefinitionMaps.getBlockDef(blockName2, meta2).supportiveBlock)
			{
				return true;
			}
			else if (!sameBlock(blockName, meta, blockName2, meta2))
			{
				break;
			}
		}
		for (i = 1; i <= attached; i++)
		{
			blockName2 = Block.blockRegistry.getNameForObject(world.getBlock(x, y, z + i));
			meta2 = world.getBlockMetadata(x, y, z + i);
			if (DefinitionMaps.getBlockDef(blockName2, meta2).supportiveBlock)
			{
				return true;
			}
			else if (!sameBlock(blockName, meta, blockName2, meta2))
			{
				break;
			}
		}
		for (i = 1; i <= attached; i++)
		{
			blockName2 = Block.blockRegistry.getNameForObject(world.getBlock(x, y, z - i));
			meta2 = world.getBlockMetadata(x, y, z - i);
			if (DefinitionMaps.getBlockDef(blockName2, meta2).supportiveBlock)
			{
				return true;
			}
			else if (!sameBlock(blockName, meta, blockName2, meta2))
			{
				break;
			}
		}
		return false;
	}

	private static boolean nCorbel(World world, int x, int y, int z, int nCorbel)
	{
		if (nCorbel <= 0)
		{
			return false;
		}
		int i;

		for (i = 1; i <= nCorbel; i++)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y, z)), world.getBlockMetadata(x - i, y, z)).supportiveBlock)
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y - 1, z)), world.getBlockMetadata(x - i, y - 1, z)).supportiveBlock)
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		for (i = 1; i <= nCorbel; i++)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y, z)), world.getBlockMetadata(x + i, y, z)).supportiveBlock)
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y - 1, z)), world.getBlockMetadata(x + i, y - 1, z)).supportiveBlock)
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		for (i = 1; i <= nCorbel; i++)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z + i)), world.getBlockMetadata(x, y, z + i)).supportiveBlock)
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z + i)), world.getBlockMetadata(x, y - 1, z + i)).supportiveBlock)
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		for (i = 1; i <= nCorbel; i++)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z - i)), world.getBlockMetadata(x, y, z - i)).supportiveBlock)
			{
				if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z - i)), world.getBlockMetadata(x, y - 1, z - i)).supportiveBlock)
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		return false;
	}

	private static boolean corbel(World world, int x, int y, int z, int corbel, String blockName, int meta)
	{
		if (corbel <= 0 || !DefinitionMaps.getBlockDef(blockName, meta).supportiveBlock)
		{
			return false;
		}
		int i;

		for (i = 1; i <= corbel; i++)
		{
			if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y, z)), world.getBlockMetadata(x + i, y, z), blockName, meta))
			{
				if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y - 1, z)), world.getBlockMetadata(x + i, y - 1, z), blockName, meta))
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		for (i = 1; i <= corbel; i++)
		{
			if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y, z)), world.getBlockMetadata(x - i, y, z), blockName, meta))
			{
				if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y - 1, z)), world.getBlockMetadata(x - i, y - 1, z), blockName, meta))
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		for (i = 1; i <= corbel; i++)
		{
			if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z + i)), world.getBlockMetadata(x, y, z + i), blockName, meta))
			{
				if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z + i)), world.getBlockMetadata(x, y - 1, z + i), blockName, meta))
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		for (i = 1; i <= corbel; i++)
		{
			if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z - i)), world.getBlockMetadata(x, y, z - i), blockName, meta))
			{
				if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z - i)), world.getBlockMetadata(x, y - 1, z - i), blockName, meta))
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		return false;
	}

	private static boolean ceiling(World world, int x, int y, int z)
	{
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y, z)), world.getBlockMetadata(x - 1, y, z)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y, z)), world.getBlockMetadata(x + 1, y, z)).supportiveBlock)
		{
			return true;
		}
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z - 1)), world.getBlockMetadata(x, y, z - 1)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z + 1)), world.getBlockMetadata(x, y, z + 1)).supportiveBlock)
		{
			return true;
		}
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y, z - 1)), world.getBlockMetadata(x - 1, y, z - 1)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y, z + 1)), world.getBlockMetadata(x + 1, y, z + 1)).supportiveBlock)
		{
			return true;
		}
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y, z + 1)), world.getBlockMetadata(x - 1, y, z + 1)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y, z - 1)), world.getBlockMetadata(x + 1, y, z - 1)).supportiveBlock)
		{
			return true;
		}
		return false;
	}

	private static boolean smallArc(World world, int x, int y, int z, int smallArc)
	{
		if (smallArc <= 0)
		{
			return false;
		}
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y, z)), world.getBlockMetadata(x - 1, y, z)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y, z)), world.getBlockMetadata(x + 1, y, z)).supportiveBlock)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y - 1, z)), world.getBlockMetadata(x - 1, y - 1, z)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y - 1, z)), world.getBlockMetadata(x + 1, y - 1, z)).supportiveBlock)
			{
				return true;
			}
			if (smallArc > 1)
			{
				int i;
				for (i = 2; i <= smallArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y, z)), world.getBlockMetadata(x - i, y, z)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y - 1, z)), world.getBlockMetadata(x - i, y - 1, z)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
				for (i = 2; i <= smallArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y, z)), world.getBlockMetadata(x + i, y, z)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y - 1, z)), world.getBlockMetadata(x + i, y - 1, z)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
		}
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z - 1)), world.getBlockMetadata(x, y, z - 1)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z + 1)), world.getBlockMetadata(x, y, z + 1)).supportiveBlock)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z - 1)), world.getBlockMetadata(x, y - 1, z - 1)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z + 1)), world.getBlockMetadata(x, y - 1, z + 1)).supportiveBlock)
			{
				return true;
			}
			if (smallArc > 1)
			{
				int i;
				for (i = 2; i <= smallArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z - i)), world.getBlockMetadata(x, y, z - i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z - i)), world.getBlockMetadata(x, y - 1, z - i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
				for (i = 2; i <= smallArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z + i)), world.getBlockMetadata(x, y, z + i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z + i)), world.getBlockMetadata(x, y - 1, z + i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
		}
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y, z + 1)), world.getBlockMetadata(x - 1, y, z + 1)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y, z - 1)), world.getBlockMetadata(x + 1, y, z - 1)).supportiveBlock)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y - 1, z + 1)), world.getBlockMetadata(x - 1, y - 1, z + 1)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y - 1, z - 1)), world.getBlockMetadata(x + 1, y - 1, z - 1)).supportiveBlock)
			{
				return true;
			}
			if (smallArc > 1)
			{
				int i;
				for (i = 2; i <= smallArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y, z + i)), world.getBlockMetadata(x - i, y, z + i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y - 1, z + i)), world.getBlockMetadata(x - i, y - 1, z + i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
				for (i = 2; i <= smallArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y, z - i)), world.getBlockMetadata(x + i, y, z - i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y - 1, z - i)), world.getBlockMetadata(x + i, y - 1, z - i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
		}
		if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y, z + 1)), world.getBlockMetadata(x + 1, y, z + 1)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y, z - 1)), world.getBlockMetadata(x - 1, y, z - 1)).supportiveBlock)
		{
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y - 1, z + 1)), world.getBlockMetadata(x + 1, y - 1, z + 1)).supportiveBlock && DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y - 1, z - 1)), world.getBlockMetadata(x - 1, y - 1, z - 1)).supportiveBlock)
			{
				return true;
			}
			if (smallArc > 1)
			{
				int i;
				for (i = 2; i <= smallArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y, z + i)), world.getBlockMetadata(x + i, y, z + i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y - 1, z + i)), world.getBlockMetadata(x + i, y - 1, z + i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
				for (i = 2; i <= smallArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y, z - i)), world.getBlockMetadata(x - i, y, z - i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y - 1, z - i)), world.getBlockMetadata(x - i, y - 1, z - i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
		}
		return false;
	}

	private static boolean bigArc(World world, int x, int y, int z, int bigArc)
	{
		if (bigArc <= 0)
		{
			return false;
		}
		if (!DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y + 1, z)), world.getBlockMetadata(x, y + 1, z)).supportiveBlock)
		{
			int i;
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y + 1, z)), world.getBlockMetadata(x + 1, y + 1, z)).supportiveBlock)
			{
				for (i = 1; i <= bigArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y, z)), world.getBlockMetadata(x - i, y, z)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y - 1, z)), world.getBlockMetadata(x - i, y - 1, z)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y + 1, z)), world.getBlockMetadata(x - 1, y + 1, z)).supportiveBlock)
			{
				for (i = 1; i <= bigArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y, z)), world.getBlockMetadata(x + i, y, z)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y - 1, z)), world.getBlockMetadata(x + i, y - 1, z)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y + 1, z + 1)), world.getBlockMetadata(x, y + 1, z + 1)).supportiveBlock)
			{
				for (i = 1; i <= bigArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z - i)), world.getBlockMetadata(x, y, z - i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z - i)), world.getBlockMetadata(x, y - 1, z - i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y + 1, z - 1)), world.getBlockMetadata(x, y + 1, z - 1)).supportiveBlock)
			{
				for (i = 1; i <= bigArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y, z + i)), world.getBlockMetadata(x, y, z + i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z + i)), world.getBlockMetadata(x, y - 1, z + i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y + 1, z + 1)), world.getBlockMetadata(x + 1, y + 1, z + 1)).supportiveBlock)
			{
				for (i = 1; i <= bigArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y, z - i)), world.getBlockMetadata(x - i, y, z - i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y - 1, z - i)), world.getBlockMetadata(x - i, y - 1, z - i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y + 1, z - 1)), world.getBlockMetadata(x - 1, y + 1, z - 1)).supportiveBlock)
			{
				for (i = 1; i <= bigArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y, z + i)), world.getBlockMetadata(x + i, y, z + i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y - 1, z + i)), world.getBlockMetadata(x + i, y - 1, z + i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y + 1, z - 1)), world.getBlockMetadata(x + 1, y + 1, z - 1)).supportiveBlock)
			{
				for (i = 1; i <= bigArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y, z + i)), world.getBlockMetadata(x - i, y, z + i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - i, y - 1, z + i)), world.getBlockMetadata(x - i, y - 1, z + i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
			if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y + 1, z + 1)), world.getBlockMetadata(x - 1, y + 1, z + 1)).supportiveBlock)
			{
				for (i = 1; i <= bigArc; i++)
				{
					if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y, z - i)), world.getBlockMetadata(x + i, y, z - i)).supportiveBlock)
					{
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(world.getBlock(x + i, y - 1, z - i)), world.getBlockMetadata(x + i, y - 1, z - i)).supportiveBlock)
						{
							return true;
						}
					}
					else
					{
						break;
					}
				}
			}
		}
		return false;
	}

	private static boolean branch(World world, int x, int y, int z, String blockName, int meta)
	{
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y - 1, z)), world.getBlockMetadata(x + 1, y - 1, z), blockName, meta))
		{
			return true;
		}
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y - 1, z)), world.getBlockMetadata(x - 1, y - 1, z), blockName, meta))
		{
			return true;
		}
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z + 1)), world.getBlockMetadata(x, y - 1, z + 1), blockName, meta))
		{
			return true;
		}
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x, y - 1, z - 1)), world.getBlockMetadata(x, y - 1, z - 1), blockName, meta))
		{
			return true;
		}
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y - 1, z + 1)), world.getBlockMetadata(x + 1, y - 1, z + 1), blockName, meta))
		{
			return true;
		}
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y - 1, z - 1)), world.getBlockMetadata(x - 1, y - 1, z - 1), blockName, meta))
		{
			return true;
		}
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x - 1, y - 1, z + 1)), world.getBlockMetadata(x - 1, y - 1, z + 1), blockName, meta))
		{
			return true;
		}
		if (sameBlock(Block.blockRegistry.getNameForObject(world.getBlock(x + 1, y - 1, z - 1)), world.getBlockMetadata(x + 1, y - 1, z - 1), blockName, meta))
		{
			return true;
		}
		return false;
	}

	private static boolean sameBlock(String blockName, int blockMetadata, String name, int meta)
	{
		return blockName.equals(name) && blockMetadata == meta;
	}
}