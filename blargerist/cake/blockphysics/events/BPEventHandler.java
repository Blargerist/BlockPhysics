package blargerist.cake.blockphysics.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import blargerist.cake.blockphysics.util.BlockMove;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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

	@SubscribeEvent
	public void onPlayerBlockPlace(PlaceEvent event)
	{
		event.world.scheduleBlockUpdate(event.x, event.y, event.z, event.block, event.block.tickRate(event.world));
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
	
	public static void entityFallingBlockUpdate(EntityFallingBlock entity)
	{
		if (entity.func_145805_f().getMaterial() == Material.air)
        {
            entity.setDead();
        }
        else
        {
            entity.prevPosX = entity.posX;
            entity.prevPosY = entity.posY;
            entity.prevPosZ = entity.posZ;
            ++entity.field_145812_b;
            entity.motionY -= 0.03999999910593033D;
            entity.moveEntity(entity.motionX, entity.motionY, entity.motionZ);
            entity.motionX *= 0.9800000190734863D;
            entity.motionY *= 0.9800000190734863D;
            entity.motionZ *= 0.9800000190734863D;

            if (!entity.worldObj.isRemote)
            {
                int i = MathHelper.floor_double(entity.posX);
                int j = MathHelper.floor_double(entity.posY);
                int k = MathHelper.floor_double(entity.posZ);

                if (entity.field_145812_b == 1)
                {
                    if (entity.worldObj.getBlock(i, j, k) != entity.func_145805_f())
                    {
                        entity.setDead();
                        return;
                    }

                    entity.worldObj.setBlockToAir(i, j, k);
                }
                else if (entity.field_145812_b == 4)
                {
                	entity.noClip = false;
                }

                if (entity.onGround)
                {
                    entity.motionX *= 0.699999988079071D;
                    entity.motionZ *= 0.699999988079071D;
                    entity.motionY *= -0.5D;

                    if (entity.worldObj.getBlock(i, j, k) != Blocks.piston_extension)
                    {

                        if (entity.worldObj.canPlaceEntityOnSide(entity.func_145805_f(), i, j, k, true, 1, (Entity)null, (ItemStack)null) && !BlockFalling.func_149831_e(entity.worldObj, i, j - 1, k) && entity.worldObj.setBlock(i, j, k, entity.func_145805_f(), entity.field_145814_a, 3))
                        {
                            entity.setDead();
                            
                            if (entity.func_145805_f() instanceof BlockFalling)
                            {
                                ((BlockFalling)entity.func_145805_f()).func_149828_a(entity.worldObj, i, j, k, entity.field_145814_a);
                            }

                            if (entity.field_145810_d != null && entity.func_145805_f() instanceof ITileEntityProvider)
                            {
                                TileEntity tileentity = entity.worldObj.getTileEntity(i, j, k);

                                if (tileentity != null)
                                {
                                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                                    tileentity.writeToNBT(nbttagcompound);
                                    Iterator iterator = entity.field_145810_d.func_150296_c().iterator();

                                    while (iterator.hasNext())
                                    {
                                        String s = (String)iterator.next();
                                        NBTBase nbtbase = entity.field_145810_d.getTag(s);

                                        if (!s.equals("x") && !s.equals("y") && !s.equals("z"))
                                        {
                                            nbttagcompound.setTag(s, nbtbase.copy());
                                        }
                                    }

                                    tileentity.readFromNBT(nbttagcompound);
                                    tileentity.markDirty();
                                }
                            }
                        }
                        else if (entity.field_145813_c && entity.field_145812_b > 600)
                        {
                        	entity.setDead();
                            entity.entityDropItem(new ItemStack(entity.func_145805_f(), 1, entity.func_145805_f().damageDropped(entity.field_145814_a)), 0.0F);
                        }
                    }
                }
                else if (entity.field_145812_b > 100 && !entity.worldObj.isRemote && (j < 1 || j > 256) || entity.field_145812_b > 600)
                {
                    if (entity.field_145813_c)
                    {
                        entity.entityDropItem(new ItemStack(entity.func_145805_f(), 1, entity.func_145805_f().damageDropped(entity.field_145814_a)), 0.0F);
                    }

                    entity.setDead();
                }
            }
        }
	}
}