package blargerist.cake.blockphysics.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import blargerist.cake.blockphysics.ModInfo;
import blargerist.cake.blockphysics.events.BPEventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;

public class EntityMove
{
	public static void onUpdate(EntityFallingBlock entity)
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
                
                if (BlockMove.canMoveTo(entity.worldObj, i, j - 1, k, DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(entity.func_145805_f()), entity.field_145814_a).mass) == 2)
                {
                	entity.worldObj.setBlockToAir(i, j - 1, k);
                }

                if (entity.onGround)
                {
                	if (entity.motionX < 0.0025 && entity.motionX > -0.0025 && entity.motionZ < 0.0025 && entity.motionZ > -0.0025)
                	{
                		if (entity.posX > Math.round(entity.posX))
                		{
                			ModInfo.Log.info("Entity posX > " + entity.posX);
                			entity.motionX = 0.07;
                		}
                		else if (entity.posX < Math.round(entity.posX))
                		{
                			ModInfo.Log.info("Entity posX < " + entity.posX);
                			entity.motionX = -0.07;
                		}
                		if (entity.posZ > Math.round(entity.posZ))
                		{
                			ModInfo.Log.info("Entity posZ > " + entity.posZ);
                			entity.motionZ = 0.07;
                		}
                		else if (entity.posZ < Math.round(entity.posZ))
                		{
                			ModInfo.Log.info("Entity posZ < " + entity.posZ);
                			entity.motionZ = -0.07;
                		}
                	}
                    entity.motionX *= 0.899999988079071D;
                    entity.motionZ *= 0.899999988079071D;
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
                        else if (entity.field_145813_c && (entity.field_145812_b > 600 || (entity.field_145812_b > 200 && entity.onGround)))
                        {
                        	entity.setDead();
                            entity.entityDropItem(new ItemStack(entity.func_145805_f(), 1, entity.func_145805_f().damageDropped(entity.field_145814_a)), 0.0F);
                        }
                    }
                }
                else if (entity.field_145812_b > 100 && !entity.worldObj.isRemote && (j < 1 || j > 256))
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
	
	public void moveEntity(EntityFallingBlock entity, double p_70091_1_, double p_70091_3_, double p_70091_5_)
    {
        if (entity.noClip)
        {
            entity.boundingBox.offset(p_70091_1_, p_70091_3_, p_70091_5_);
            entity.posX = (entity.boundingBox.minX + entity.boundingBox.maxX) / 2.0D;
            entity.posY = entity.boundingBox.minY + (double)entity.yOffset - (double)entity.ySize;
            entity.posZ = (entity.boundingBox.minZ + entity.boundingBox.maxZ) / 2.0D;
        }
        else
        {
            entity.worldObj.theProfiler.startSection("move");
            entity.ySize *= 0.4F;
            double d3 = entity.posX;
            double d4 = entity.posY;
            double d5 = entity.posZ;

            double d6 = p_70091_1_;
            double d7 = p_70091_3_;
            double d8 = p_70091_5_;
            AxisAlignedBB axisalignedbb = entity.boundingBox.copy();

            List list = entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.addCoord(p_70091_1_, p_70091_3_, p_70091_5_));

            for (int i = 0; i < list.size(); ++i)
            {
                p_70091_3_ = ((AxisAlignedBB)list.get(i)).calculateYOffset(entity.boundingBox, p_70091_3_);
            }

            entity.boundingBox.offset(0.0D, p_70091_3_, 0.0D);

            if (!entity.field_70135_K && d7 != p_70091_3_)
            {
                p_70091_5_ = 0.0D;
                p_70091_3_ = 0.0D;
                p_70091_1_ = 0.0D;
            }

            boolean flag1 = entity.onGround || d7 != p_70091_3_ && d7 < 0.0D;
            int j;

            for (j = 0; j < list.size(); ++j)
            {
                p_70091_1_ = ((AxisAlignedBB)list.get(j)).calculateXOffset(entity.boundingBox, p_70091_1_);
            }

            entity.boundingBox.offset(p_70091_1_, 0.0D, 0.0D);

            if (!entity.field_70135_K && d6 != p_70091_1_)
            {
                p_70091_5_ = 0.0D;
                p_70091_3_ = 0.0D;
                p_70091_1_ = 0.0D;
            }

            for (j = 0; j < list.size(); ++j)
            {
                p_70091_5_ = ((AxisAlignedBB)list.get(j)).calculateZOffset(entity.boundingBox, p_70091_5_);
            }

            entity.boundingBox.offset(0.0D, 0.0D, p_70091_5_);

            if (!entity.field_70135_K && d8 != p_70091_5_)
            {
                p_70091_5_ = 0.0D;
                p_70091_3_ = 0.0D;
                p_70091_1_ = 0.0D;
            }

            double d10;
            double d11;
            int k;
            double d12;

            if (entity.stepHeight > 0.0F && flag1 && entity.ySize < 0.05F && (d6 != p_70091_1_ || d8 != p_70091_5_))
            {
                d12 = p_70091_1_;
                d10 = p_70091_3_;
                d11 = p_70091_5_;
                p_70091_1_ = d6;
                p_70091_3_ = (double)entity.stepHeight;
                p_70091_5_ = d8;
                AxisAlignedBB axisalignedbb1 = entity.boundingBox.copy();
                entity.boundingBox.setBB(axisalignedbb);
                list = entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox.addCoord(d6, p_70091_3_, d8));

                for (k = 0; k < list.size(); ++k)
                {
                    p_70091_3_ = ((AxisAlignedBB)list.get(k)).calculateYOffset(entity.boundingBox, p_70091_3_);
                }

                entity.boundingBox.offset(0.0D, p_70091_3_, 0.0D);

                if (!entity.field_70135_K && d7 != p_70091_3_)
                {
                    p_70091_5_ = 0.0D;
                    p_70091_3_ = 0.0D;
                    p_70091_1_ = 0.0D;
                }

                for (k = 0; k < list.size(); ++k)
                {
                    p_70091_1_ = ((AxisAlignedBB)list.get(k)).calculateXOffset(entity.boundingBox, p_70091_1_);
                }

                entity.boundingBox.offset(p_70091_1_, 0.0D, 0.0D);

                if (!entity.field_70135_K && d6 != p_70091_1_)
                {
                    p_70091_5_ = 0.0D;
                    p_70091_3_ = 0.0D;
                    p_70091_1_ = 0.0D;
                }

                for (k = 0; k < list.size(); ++k)
                {
                    p_70091_5_ = ((AxisAlignedBB)list.get(k)).calculateZOffset(entity.boundingBox, p_70091_5_);
                }

                entity.boundingBox.offset(0.0D, 0.0D, p_70091_5_);

                if (!entity.field_70135_K && d8 != p_70091_5_)
                {
                    p_70091_5_ = 0.0D;
                    p_70091_3_ = 0.0D;
                    p_70091_1_ = 0.0D;
                }

                if (!entity.field_70135_K && d7 != p_70091_3_)
                {
                    p_70091_5_ = 0.0D;
                    p_70091_3_ = 0.0D;
                    p_70091_1_ = 0.0D;
                }
                else
                {
                    p_70091_3_ = (double)(-entity.stepHeight);

                    for (k = 0; k < list.size(); ++k)
                    {
                        p_70091_3_ = ((AxisAlignedBB)list.get(k)).calculateYOffset(entity.boundingBox, p_70091_3_);
                    }

                    entity.boundingBox.offset(0.0D, p_70091_3_, 0.0D);
                }

                if (d12 * d12 + d11 * d11 >= p_70091_1_ * p_70091_1_ + p_70091_5_ * p_70091_5_)
                {
                    p_70091_1_ = d12;
                    p_70091_3_ = d10;
                    p_70091_5_ = d11;
                    entity.boundingBox.setBB(axisalignedbb1);
                }
            }

            entity.worldObj.theProfiler.endSection();
            entity.worldObj.theProfiler.startSection("rest");
            entity.posX = (entity.boundingBox.minX + entity.boundingBox.maxX) / 2.0D;
            entity.posY = entity.boundingBox.minY + (double)entity.yOffset - (double)entity.ySize;
            entity.posZ = (entity.boundingBox.minZ + entity.boundingBox.maxZ) / 2.0D;
            entity.isCollidedHorizontally = d6 != p_70091_1_ || d8 != p_70091_5_;
            entity.isCollidedVertically = d7 != p_70091_3_;
            entity.onGround = d7 != p_70091_3_ && d7 < 0.0D;
            entity.isCollided = entity.isCollidedHorizontally || entity.isCollidedVertically;
            updateFallState(entity, p_70091_3_, entity.onGround);

            if (d6 != p_70091_1_)
            {
                entity.motionX = 0.0D;
            }

            if (d7 != p_70091_3_)
            {
                entity.motionY = 0.0D;
            }

            if (d8 != p_70091_5_)
            {
                entity.motionZ = 0.0D;
            }

            d12 = entity.posX - d3;
            d10 = entity.posY - d4;
            d11 = entity.posZ - d5;

            try
            {
                checkEntityBlockCollisions(entity);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                entity.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            entity.worldObj.theProfiler.endSection();
        }
    }
	
	protected void updateFallState(EntityFallingBlock entity, double p_70064_1_, boolean p_70064_3_)
    {
        if (p_70064_3_)
        {
            if (entity.fallDistance > 0.0F)
            {
            	entity.fallDistance = 0.0F;
            }
        }
        else if (p_70064_1_ < 0.0D)
        {
        	entity.fallDistance = (float)((double)entity.fallDistance - p_70064_1_);
        }
    }
	
	public static void checkEntityBlockCollisions(Entity entity)
	{
		int i = MathHelper.floor_double(entity.boundingBox.minX + 0.001D);
		int j = MathHelper.floor_double(entity.boundingBox.minY + 0.001D);
		int k = MathHelper.floor_double(entity.boundingBox.minZ + 0.001D);
		int l = MathHelper.floor_double(entity.boundingBox.maxX - 0.001D);
		int i1 = MathHelper.floor_double(entity.boundingBox.maxY - 0.001D);
		int j1 = MathHelper.floor_double(entity.boundingBox.maxZ - 0.001D);

		if (entity.worldObj.checkChunksExist(i, j, k, l, i1, j1))
		{
			for (int k1 = i; k1 <= l; ++k1)
			{
				for (int l1 = j; l1 <= i1; ++l1)
				{
					for (int i2 = k; i2 <= j1; ++i2)
					{
						Block block = entity.worldObj.getBlock(k1, l1, i2);

						int meta = entity.worldObj.getBlockMetadata(k1, l1, i2);
						String blockName = Block.blockRegistry.getNameForObject(block);
						if (DefinitionMaps.getBlockDef(Block.blockRegistry.getNameForObject(block), entity.worldObj.getBlockMetadata(k1, l1, i2)).fragile > 0)
						{
							ModInfo.Log.info("Block Fragile: " + blockName);
							BPEventHandler.onFragileBlockCollision(entity, k1, l1, i2);
						}
						else
						{
							try
							{
								block.onEntityCollidedWithBlock(entity.worldObj, k1, l1, i2, entity);
							}
							catch (Throwable throwable)
							{
								CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
								CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
								CrashReportCategory.func_147153_a(crashreportcategory, k1, l1, i2, block, entity.worldObj.getBlockMetadata(k1, l1, i2));
								throw new ReportedException(crashreport);
							}
						}
					}
				}
			}
		}
	}
}