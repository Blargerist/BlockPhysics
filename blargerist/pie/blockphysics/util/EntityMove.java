package blargerist.pie.blockphysics.util;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import blargerist.pie.blockphysics.ModConfig;
import blargerist.pie.blockphysics.ModInfo;
import blargerist.pie.blockphysics.events.BPEventHandler;

public class EntityMove
{
	final static double[][][] slideSpeedz = {{{-0.125D, -0.005D, 0D}, {0D, -0.005D, -0.125D}, {0D, -0.005D, 0.125D}, {0.125D, -0.005D, 0D}}, {{-0.25D, -0.06D, 0D}, {0D, -0.06D, -0.25D}, {0D, -0.06D, 0.25D}, {0.25D, -0.06D, 0D}}, {{-0.3D, -0.25D, 0D}, {0D, -0.25D, -0.3D}, {0D, -0.25D, 0.3D}, {0.3D, -0.25D, 0D}}, {{-0.18D, -0.35D, 0D}, {0D, -0.35D, -0.18D}, {0D, -0.35D, 0.18D}, {0.18D, -0.35D, 0D}}, {{-0.05D, -0.15D, 0D}, {0D, -0.15D, -0.05D}, {0D, -0.15D, 0.05D}, {0.05D, -0.15D, 0D}}};
	
	public static void moveEntity(World world, EntityMovingBlock block, double x, double y, double z)
	{
		if (block.noClip)
		{
			block.boundingBox.offset(x, y, z);
			block.posX = (block.boundingBox.minX + block.boundingBox.maxX) / 2.0D;
			block.posY = block.boundingBox.minY + (double) block.yOffset - (double) block.ySize;
			block.posZ = (block.boundingBox.minZ + block.boundingBox.maxZ) / 2.0D;
		}
		else
		{
			block.ySize *= 0.4F;
			double d3 = block.posX;
			double d4 = block.posY;
			double d5 = block.posZ;
			
			double d6 = x;
			double d7 = y;
			double d8 = z;
			AxisAlignedBB axisalignedbb = block.boundingBox.copy();
			
			List list = world.getCollidingBoundingBoxes(block, block.boundingBox.addCoord(x, y, z));
			
			for (int i = 0; i < list.size(); ++i)
			{
				y = ((AxisAlignedBB) list.get(i)).calculateYOffset(block.boundingBox, y);
			}
			
			block.boundingBox.offset(0.0D, y, 0.0D);
			
			if (!block.field_70135_K && d7 != y)
			{
				z = 0.0D;
				y = 0.0D;
				x = 0.0D;
			}
			
			boolean flag1 = block.onGround || d7 != y && d7 < 0.0D;
			int j;
			
			for (j = 0; j < list.size(); ++j)
			{
				x = ((AxisAlignedBB) list.get(j)).calculateXOffset(block.boundingBox, x);
			}
			
			block.boundingBox.offset(x, 0.0D, 0.0D);
			
			if (!block.field_70135_K && d6 != x)
			{
				z = 0.0D;
				y = 0.0D;
				x = 0.0D;
			}
			
			for (j = 0; j < list.size(); ++j)
			{
				z = ((AxisAlignedBB) list.get(j)).calculateZOffset(block.boundingBox, z);
			}
			
			block.boundingBox.offset(0.0D, 0.0D, z);
			
			if (!block.field_70135_K && d8 != z)
			{
				z = 0.0D;
				y = 0.0D;
				x = 0.0D;
			}
			
			double d10;
			double d11;
			int k;
			double d12;
			
			if (block.stepHeight > 0.0F && flag1 && block.ySize < 0.05F && (d6 != x || d8 != z))
			{
				d12 = x;
				d10 = y;
				d11 = z;
				x = d6;
				y = (double) block.stepHeight;
				z = d8;
				AxisAlignedBB axisalignedbb1 = block.boundingBox.copy();
				block.boundingBox.setBB(axisalignedbb);
				list = world.getCollidingBoundingBoxes(block, block.boundingBox.addCoord(d6, y, d8));
				
				for (k = 0; k < list.size(); ++k)
				{
					y = ((AxisAlignedBB) list.get(k)).calculateYOffset(block.boundingBox, y);
				}
				
				block.boundingBox.offset(0.0D, y, 0.0D);
				
				if (!block.field_70135_K && d7 != y)
				{
					z = 0.0D;
					y = 0.0D;
					x = 0.0D;
				}
				
				for (k = 0; k < list.size(); ++k)
				{
					x = ((AxisAlignedBB) list.get(k)).calculateXOffset(block.boundingBox, x);
				}
				
				block.boundingBox.offset(x, 0.0D, 0.0D);
				
				if (!block.field_70135_K && d6 != x)
				{
					z = 0.0D;
					y = 0.0D;
					x = 0.0D;
				}
				
				for (k = 0; k < list.size(); ++k)
				{
					z = ((AxisAlignedBB) list.get(k)).calculateZOffset(block.boundingBox, z);
				}
				
				block.boundingBox.offset(0.0D, 0.0D, z);
				
				if (!block.field_70135_K && d8 != z)
				{
					z = 0.0D;
					y = 0.0D;
					x = 0.0D;
				}
				
				if (!block.field_70135_K && d7 != y)
				{
					z = 0.0D;
					y = 0.0D;
					x = 0.0D;
				}
				else
				{
					y = (double) (-block.stepHeight);
					
					for (k = 0; k < list.size(); ++k)
					{
						y = ((AxisAlignedBB) list.get(k)).calculateYOffset(block.boundingBox, y);
					}
					
					block.boundingBox.offset(0.0D, y, 0.0D);
				}
				
				if (d12 * d12 + d11 * d11 >= x * x + z * z)
				{
					x = d12;
					y = d10;
					z = d11;
					block.boundingBox.setBB(axisalignedbb1);
				}
			}
			
			block.posX = (block.boundingBox.minX + block.boundingBox.maxX) / 2.0D;
			block.posY = block.boundingBox.minY + (double) block.yOffset - (double) block.ySize;
			block.posZ = (block.boundingBox.minZ + block.boundingBox.maxZ) / 2.0D;
			block.isCollidedHorizontally = d6 != x || d8 != z;
			block.isCollidedVertically = d7 != y;
			block.onGround = d7 != y && d7 < 0.0D;
			block.isCollided = block.isCollidedHorizontally || block.isCollidedVertically;
			//fsand.updateFallState(par3, fsand.onGround);
			
			if (d6 != x)
			{
				block.motionX = 0.0D;
			}
			
			if (d7 != y)
			{
				block.motionY = 0.0D;
			}
			
			if (d8 != z)
			{
				block.motionZ = 0.0D;
			}
			
			d12 = block.posX - d3;
			d10 = block.posY - d4;
			d11 = block.posZ - d5;
		}
	}
	
	public static void fallingSandUpdate(World world, EntityMovingBlock fsand)
	{
		Block blocky = fsand.func_145805_f();
		ModInfo.Log.info("blocky = " + Block.blockRegistry.getNameForObject(blocky));
		
		fsand.field_145812_b++;
		if (fsand.field_145812_b < 3)
		{
			if (fsand.field_145812_b == 1)
				fsand.field_145814_a &= 15;
			return;
		}
		
		int i = MathHelper.floor_double(fsand.posX);
		int j = MathHelper.floor_double(fsand.posY);
		int k = MathHelper.floor_double(fsand.posZ);
		
		if (fsand.field_145812_b == 4)
			BlockMove.notifyMove(world, i, j, k);
		
		if (!world.isRemote && fsand.dead < 4)
		{
			fsand.dead--;
			if (fsand.dead <= 0)
				fsand.setDead();
			return;
		}
		Material mt;
		fsand.noClip = true;
		fsand.onGround = false;
		
		if (j < -3 || fsand.field_145812_b > 600)
		{
			fsand.setDead();
			if (!world.isRemote)
				dropFallingSand(fsand);
		}
		
		fsand.media = Block.blockRegistry.getNameForObject(world.getBlock(i, j, k));
		
		if (fsand.slideDir != 0)
		{
			if (fsand.field_145812_b < 8)
			{
				int stime = fsand.field_145812_b - 3;
				int sdir = fsand.slideDir - 1;
				
				if (stime == 0)
				{
					switch (sdir)
					{
						case 0:
							fsand.setPosition((double) i - 0.0625D + 0.5D, (double) j - 0.0625D + 0.5D, (double) k + 0.5D);
							break;
						case 1:
							fsand.setPosition((double) i + 0.5D, (double) j - 0.0625D + 0.5D, (double) k - 0.0625D + 0.5D);
							break;
						case 2:
							fsand.setPosition((double) i + 0.5D, (double) j - 0.0625D + 0.5D, (double) k + 0.0625D + 0.5D);
							break;
						case 3:
							fsand.setPosition((double) i + 0.0625D + 0.5D, (double) j - 0.0625D + 0.5D, (double) k + 0.5D);
							break;
						default:
					}
				}
				
				fsand.motionX = slideSpeedz[stime][sdir][0];
				fsand.motionY = slideSpeedz[stime][sdir][1];
				fsand.motionZ = slideSpeedz[stime][sdir][2];
				fsand.accelerationX = 0D;
				fsand.accelerationY = 0D;
				fsand.accelerationZ = 0D;
			}
			else
			{
				fsand.slideDir = 0;
			}
		}
		
		if (fsand.motionX > 3.9D)
			fsand.motionX = 3.9D;
		else if (fsand.motionX < -3.9D)
			fsand.motionX = -3.9D;
		if (fsand.motionY > 3.9D)
			fsand.motionY = 3.9D;
		else if (fsand.motionY < -3.9D)
			fsand.motionY = -3.9D;
		if (fsand.motionZ > 3.9D)
			fsand.motionZ = 3.9D;
		else if (fsand.motionZ < -3.9D)
			fsand.motionZ = -3.9D;
		
		//writetoLog(";"+world.isRemote+";ID: ;"+fsand.entityId+";slidedir: ;"+fsand.slideDir+";mass: ;"+fsand.mass+"; falltime: ;"+fsand.fallTime+";pos: ;"+fsand.posX+"; "+fsand.posY+"; "+fsand.posZ+";speed: ;"+fsand.motionX+"; "+fsand.motionY+"; "+fsand.motionZ+"; acceleration: ;"+fsand.accelerationX+"; "+fsand.accelerationY+"; "+fsand.accelerationZ);
		
		double cmotionX = fsand.motionX;
		double cmotionY = fsand.motionY;
		double cmotionZ = fsand.motionZ;
		
		double caccelerationX = fsand.accelerationX;
		double caccelerationY = fsand.accelerationY;
		double caccelerationZ = fsand.accelerationZ;
		
		fsand.accelerationX = 0;
		fsand.accelerationY = 0;
		fsand.accelerationZ = 0;
		
		if (fsand.slideDir == 0)
		{
			fsand.motionX = bSpeedR(fsand.motionX + caccelerationX);
			fsand.motionY = bSpeedR(fsand.motionY + caccelerationY);
			fsand.motionZ = bSpeedR(fsand.motionZ + caccelerationZ);
		}
		
		double moveX = cmotionX + caccelerationX * 0.5D;
		double moveY = cmotionY + caccelerationY * 0.5D;
		double moveZ = cmotionZ + caccelerationZ * 0.5D;
		
		double axisaligned_maxmove = MathHelper.abs_max(MathHelper.abs_max(moveX, moveZ), moveY);
		double blockofsX, blockofsY, blockofsZ;  // point on the face of the block in the direction of moving
		if (axisaligned_maxmove != 0)
		{
			blockofsX = 0.498D * moveX / axisaligned_maxmove;
			blockofsY = 0.498D * moveY / axisaligned_maxmove;
			blockofsZ = 0.498D * moveZ / axisaligned_maxmove;
		}
		else
		{
			blockofsX = 0D;
			blockofsY = 0D;
			blockofsZ = 0D;
		}
		
		double djumpdist2 = blockofsX * blockofsX + blockofsY * blockofsY + blockofsZ * blockofsZ;
		double jumpdist2 = moveX * moveX + moveY * moveY + moveZ * moveZ;
		ModInfo.Log.info("Fsand Meta = " + fsand.field_145814_a + " Block = " + Block.blockRegistry.getNameForObject(blocky));
		ModInfo.Log.info("blockMap .contains(Block.blockRegistry.getNameForObject(blocky)) = " + ModConfig.blockMap.containsKey(Block.blockRegistry.getNameForObject(blocky)));
		ModInfo.Log.info("blockMap .get(Block.blockRegistry.getNameForObject(blocky)).containsKey(meta) = " + ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(blocky)).containsKey(fsand.field_145814_a));
		ModInfo.Log.info("Mass = " + ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(blocky)).get(fsand.field_145814_a).mass);
		int mass = ModConfig.blockMap
				.get(
				     Block.blockRegistry.getNameForObject(blocky))
		                                  .get(fsand.field_145814_a)//TODO save and read from nbt
		                                  .mass;
		int em = mass / 10 + (int) (0.5D * (double) mass * jumpdist2);
		//if (!world.isRemote) writetoLog(";"+em);
		
		if (fsand.isBurning() && jumpdist2 > 4D && blocky != Blocks.netherrack)
			fsand.extinguish();
		
		AxisAlignedBB Sandbbox = null;
		
		int ii;
		if (djumpdist2 == 0)
			ii = 0;
		else
			ii = (int) Math.ceil((double) MathHelper.sqrt_double(jumpdist2 / djumpdist2));
		
		double jumpPosX = 0;
		double jumpPosY = 0;
		double jumpPosZ = 0;
		
		int in = 0;
		int jn = 0;
		int kn = 0;
		int ip = i;
		int jp = j;
		int kp = k;
		
		for (int i1 = 1; i1 <= ii; i1++)
		{
			if (i1 == ii)
			{
				jumpPosX = fsand.posX + moveX;
				jumpPosY = fsand.posY + moveY;
				jumpPosZ = fsand.posZ + moveZ;
				
				Sandbbox = fsand.boundingBox.copy();
				Sandbbox.offset(moveX, moveY, moveZ);
			}
			else if (i1 == 1)
			{
				jumpPosX = fsand.posX + blockofsX;
				jumpPosY = fsand.posY + blockofsY;
				jumpPosZ = fsand.posZ + blockofsZ;
				
				Sandbbox = fsand.boundingBox.copy();
				Sandbbox.offset(blockofsX, blockofsY, blockofsZ);
			}
			else
			{
				jumpPosX += blockofsX;
				jumpPosY += blockofsY;
				jumpPosZ += blockofsZ;
				
				Sandbbox.offset(blockofsX, blockofsY, blockofsZ);
			}
			
			//writetoLog(";"+world.isRemote+";ID: ;"+fsand.entityId+";slidedir: ;"+fsand.slideDir+";onground: ;"+fsand.onGround+";mass: ;"+ModConfig.blockMap.get(fsand.Block.blockRegistry.getNameForObject(func_145805_f())).get(fsand.field_145814_a).mass+"; falltime: ;"+fsand.fallTime+";pos: ;"+jumpPosX+"; "+jumpPosY+"; "+jumpPosZ+";speed: ;"+fsand.motionX+"; "+fsand.motionY+"; "+fsand.motionZ+"; acceleration: ;"+fsand.accelerationX+"; "+fsand.accelerationY+"; "+fsand.accelerationZ);
			
			in = MathHelper.floor_double(jumpPosX);
			jn = MathHelper.floor_double(jumpPosY);
			kn = MathHelper.floor_double(jumpPosZ);
			
			if (jp != jn || ip != in || kp != kn)
			{
				Block bidn = world.getBlock(in, jn, kn);
				String blockName = Block.blockRegistry.getNameForObject(bidn);
				int metan = world.getBlockMetadata(in, jn, kn);
				
				if (ModConfig.blockMap.containsKey(blockName) && ModConfig.blockMap.get(blockName).containsKey(metan))
				{
					if (ModConfig.blockMap.get(blockName).get(metan).fragile > 0)
					{
						
						if (!world.isRemote)
						{
							if (ModConfig.blockMap.get(blockName).get(metan).fragile > 0)
							{
								if (bidn.hasTileEntity(metan))
								{
									NBTTagCompound nnn = new NBTTagCompound();
									world.getTileEntity(in, jn, kn).writeToNBT(nnn);
									BlockMove.dropItemsNBT(world, in, jn, kn, nnn);
									world.removeTileEntity(in, jn, kn);
								}
								
								if (ModConfig.blockMap.get(blockName).get(metan).fragile == 2)
									bidn.dropBlockAsItem(world, in, jn, kn, metan, 0);
								
								world.setBlock(in, jn, kn, Blocks.air, 0, 3);
							}
						}
						bidn = Blocks.air;
						
						world.playSoundEffect((double) ((float) in + 0.5F), (double) ((float) jn + 0.5F), (double) ((float) kn + 0.5F), bidn.stepSound.getBreakSound(), (bidn.stepSound.getVolume() + 1.0F) / 2.0F, bidn.stepSound.getPitch() * 0.8F);
						
						double sl = 1D - (double) ModConfig.blockMap.get(blockName).get(metan).strength / 64000D;
						fsand.motionX *= sl;
						fsand.motionY *= sl;
						fsand.motionZ *= sl;
					}
				}
				
				if (fsand.isBurning() && bidn == Blocks.air)
				{
					world.spawnParticle("largesmoke", (float) jumpPosX + BlockMove.rand.nextFloat(), (float) jumpPosY + BlockMove.rand.nextFloat(), (float) jumpPosZ + BlockMove.rand.nextFloat(), 0D, 0D, 0D);
					world.spawnParticle("flame", (float) jumpPosX + BlockMove.rand.nextFloat(), (float) jumpPosY + BlockMove.rand.nextFloat(), (float) jumpPosZ + BlockMove.rand.nextFloat(), 0D, 0.2D, 0D);
				}
				
				if (!fsand.media.equals(Block.blockRegistry.getNameForObject(bidn)))
				{
					if (bidn != Blocks.air)
					{
						mt = bidn.getMaterial();
						if (mt.isLiquid())
						{
							if (mt == Material.lava)
							{
								if (BlockMove.canBurn(Block.blockRegistry.getNameForObject(fsand.func_145805_f())))
									fsand.setFire(60);
								else
									fsand.setFire(1);
								world.playSoundAtEntity(fsand, "random.fizz", 1F, 1.0F + (BlockMove.rand.nextFloat() - BlockMove.rand.nextFloat()) * 0.4F);
							}
							else
							{
								fsand.extinguish();
								world.playSoundAtEntity(fsand, "random.splash", 1F, 1.0F + (BlockMove.rand.nextFloat() - BlockMove.rand.nextFloat()) * 0.4F);
							}
						}
						else if (bidn == Blocks.fire)
						{
							world.playSoundAtEntity(fsand, "random.fizz", 0.5F, 1.0F + (BlockMove.rand.nextFloat() - BlockMove.rand.nextFloat()) * 0.4F);
						}
					}
					
					fsand.media = Block.blockRegistry.getNameForObject(bidn);
					
					if (fsand.slideDir == 0 && bidn != Blocks.fire)
					{
						moveX = jumpPosX - fsand.posX;
						moveY = jumpPosY - fsand.posY;
						moveZ = jumpPosZ - fsand.posZ;
						
						break;
					}
				}
				
				ip = in;
				jp = jn;
				kp = kn;
			}
			
			if (fsand.slideDir == 0)
			{
				
				if ((!BlockMove.canMoveTo(world, MathHelper.floor_double(jumpPosX + 0.499D), MathHelper.floor_double(jumpPosY + 0.499D), MathHelper.floor_double(jumpPosZ + 0.499D), em) || !BlockMove.canMoveTo(world, MathHelper.floor_double(jumpPosX + 0.499D), MathHelper.floor_double(jumpPosY + 0.499D), MathHelper.floor_double(jumpPosZ - 0.499D), em) || !BlockMove.canMoveTo(world, MathHelper.floor_double(jumpPosX + 0.499D), MathHelper.floor_double(jumpPosY - 0.499D), MathHelper.floor_double(jumpPosZ + 0.499D), em) || !BlockMove.canMoveTo(world, MathHelper.floor_double(jumpPosX + 0.499D), MathHelper.floor_double(jumpPosY - 0.499D), MathHelper.floor_double(jumpPosZ - 0.499D), em) || !BlockMove.canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY + 0.499D), MathHelper.floor_double(jumpPosZ + 0.499D), em) || !BlockMove.canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY + 0.499D), MathHelper.floor_double(jumpPosZ - 0.499D), em) || !BlockMove.canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY - 0.499D), MathHelper.floor_double(jumpPosZ + 0.499D), em) || !BlockMove.canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY - 0.499D), MathHelper.floor_double(jumpPosZ - 0.499D), em)))
				{
					
					double eimp = 0.0005D * jumpdist2 * ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(blocky)).get(fsand.field_145814_a).mass;
					
					if (eimp > 0.5D)
					{
						/*if (!world.isRemote)
						{
							if (eimp > 3.5D)
								eimp = 3.5D;
							//writetoLog(""+Math.sqrt(jumpdist2)+"       "+eimp);
							Explosion var10 = new Explosion(world, fsand, jumpPosX, jumpPosY, jumpPosZ, (float) eimp);
							if (fsand.isBurning())
								var10.isFlaming = true;
							var10.impact = true;
							if (BPEventHandler.explosionQueueMap.containsKey(world.provider.dimensionId) && BPEventHandler.explosionQueueMap.get(world.provider.dimensionId) != null)
							{
								BPEventHandler.explosionQueueMap.get(world.provider.dimensionId).add(var10);
							}
							else
							{
								ModInfo.Log.info("Explosion could not be added to Queue in world: " + world.provider.dimensionId);
							}
						}*/
						
						fsand.motionX *= 0.7D;
						fsand.motionY *= 0.7D;
						fsand.motionZ *= 0.7D;
						fsand.velocityChanged = true;
					}
					
					if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).fragile > 0 && em > ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).strength)
					{
						Block block = fsand.func_145805_f();
						world.playSoundEffect(jumpPosX, jumpPosY, jumpPosZ, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
						
						fsand.dead--;
						if (!world.isRemote)
						{
							if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).fragile == 2)
							{
								fsand.posX = jumpPosX;
								fsand.posY = jumpPosY;
								fsand.posZ = jumpPosZ;
								dropFallingSand(fsand);
							}
							else if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).fragile == 1 && fsand.field_145810_d != null)
								BlockMove.dropItemsNBT(world, MathHelper.floor_double(jumpPosX), MathHelper.floor_double(jumpPosY), MathHelper.floor_double(jumpPosZ), fsand.field_145810_d);
						}
						return;
					}
					
					moveX = jumpPosX - fsand.posX;
					moveY = jumpPosY - fsand.posY;
					moveZ = jumpPosZ - fsand.posZ;
					
					fsand.noClip = false;
					
					break;
				}
				
				Entity collent = world.findNearestEntityWithinAABB(Entity.class, Sandbbox, fsand);
				
				if (collent != null)
				{
					if (collent instanceof EntityMovingBlock)
					{
						
						double m1 = (double) ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).mass;
						double m2 = ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(((EntityMovingBlock) collent).func_145805_f())).get(((EntityMovingBlock) collent).field_145814_a).mass;
						double smass = m1 + m2;
						double vv;
						
						double is = m1 * fsand.motionX + m2 * collent.motionX;
						vv = bSpeedR((0.98D * is) / smass);
						
						fsand.motionX = vv;
						collent.motionX = vv;
						
						is = m1 * fsand.motionZ + m2 * collent.motionZ;
						vv = bSpeedR((0.98D * is) / smass);
						
						fsand.motionZ = vv;
						collent.motionZ = vv;
						
						is = m1 * fsand.motionY + m2 * collent.motionY;
						vv = bSpeedR((0.98D * is) / smass);
						
						fsand.motionY = vv;
						collent.motionY = vv;
						
						fsand.velocityChanged = true;
						collent.velocityChanged = true;
						
						if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(((EntityMovingBlock) collent).func_145805_f())).get(((EntityMovingBlock) collent).field_145814_a).fragile > 0 && em > ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(((EntityMovingBlock) collent).func_145805_f())).get(((EntityMovingBlock) collent).field_145814_a).strength)
						{
							Block block = ((EntityMovingBlock) collent).func_145805_f();
							world.playSoundEffect(collent.posX, collent.posY, collent.posZ, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
							
							((EntityMovingBlock) collent).dead--;
							if (!world.isRemote)
							{
								if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(((EntityMovingBlock) collent).func_145805_f())).get(((EntityMovingBlock) collent).field_145814_a).fragile == 2)
									dropFallingSand(((EntityMovingBlock) collent));
								else if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(((EntityMovingBlock) collent).func_145805_f())).get(((EntityMovingBlock) collent).field_145814_a).fragile == 1 && ((EntityMovingBlock) collent).field_145810_d != null)
									BlockMove.dropItemsNBT(world, MathHelper.floor_double(collent.posX), MathHelper.floor_double(collent.posY), MathHelper.floor_double(collent.posZ), ((EntityMovingBlock) collent).field_145810_d);
							}
						}
						
						if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).fragile > 0 && em > ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).strength)
						{
							Block block = fsand.func_145805_f();
							world.playSoundEffect(jumpPosX, jumpPosY, jumpPosZ, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
							
							fsand.dead--;
							if (!world.isRemote)
							{
								if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).fragile == 2)
								{
									fsand.posX = jumpPosX;
									fsand.posY = jumpPosY;
									fsand.posZ = jumpPosZ;
									dropFallingSand(fsand);
								}
								else if (ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).fragile == 1 && fsand.field_145810_d != null)
									BlockMove.dropItemsNBT(world, MathHelper.floor_double(jumpPosX), MathHelper.floor_double(jumpPosY), MathHelper.floor_double(jumpPosZ), fsand.field_145810_d);
							}
							return;
						}
						
						fsand.noClip = false;
						
						moveX = jumpPosX - fsand.posX;
						moveY = jumpPosY - fsand.posY;
						moveZ = jumpPosZ - fsand.posZ;
						break;
					}
					else if (collent instanceof EntityLiving)
					{
						//entityCollide(world, fsand, collent, (double)((EntityLiving) collent).getMaxHealth() * 3.0D, true);
						
						double m1 = (double) ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).mass;
						double m2 = (double) ((EntityLiving) collent).getMaxHealth() * 3.0D;
						double smass = m1 + m2;
						double vv;
						double damage = fsand.motionX * fsand.motionX + fsand.motionY * fsand.motionY + fsand.motionZ * fsand.motionZ;
						
						double is = m1 * fsand.motionX + m2 * (collent.posX - collent.prevPosX);
						vv = bSpeedR((0.98D * is) / smass);
						damage -= vv * vv;
						fsand.motionX = vv;
						collent.motionX = vv;
						
						is = m1 * fsand.motionZ + m2 * (collent.posZ - collent.prevPosZ);
						vv = bSpeedR((0.98D * is) / smass);
						damage -= vv * vv;
						fsand.motionZ = vv;
						collent.motionZ = vv;
						
						if (fsand.motionY < 0 && collent.onGround)
						{
							if (fsand.motionY < -0.3D)
								vv = bSpeedR(0.5D * fsand.motionY);
							else
								vv = fsand.motionY;
						}
						else
						{
							is = m1 * fsand.motionY + m2 * (collent.posY - collent.prevPosY);
							vv = bSpeedR((0.98D * is) / smass);
						}
						
						damage -= vv * vv;
						fsand.motionY = vv;
						collent.motionY = vv;
						
						fsand.velocityChanged = true;
						collent.velocityChanged = true;
						
						int d = (int) (0.083D * m1 * damage);
						if (d > 0)
						{
							//if ( !world.isRemote ) writetoLog(""+d);
							if (d > 4)
							{
								world.playSoundAtEntity(collent, "damage.fallbig", 1.0F, 1.0F);
							}
							
							if (!world.isRemote)
								((EntityLiving) collent).attackEntityFrom(DamageSource.fallingBlock, d);
						}
						
						if (!world.isRemote)
						{
							if (collent instanceof EntityPlayerMP)
							{
								((EntityPlayerMP) collent).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(collent.getEntityId(), collent.motionX, collent.motionY, collent.motionZ));
							}
						}
						
						moveX = jumpPosX - fsand.posX;
						moveY = jumpPosY - fsand.posY;
						moveZ = jumpPosZ - fsand.posZ;
						break;
					}
					else if (collent instanceof EntityItem)
					{
						collent.motionX = fsand.motionX;
						collent.motionY = fsand.motionY;
						collent.motionZ = fsand.motionZ;
						collent.velocityChanged = true;
					}
					else
					{
						fsand.accelerationX -= fsand.motionX;
						fsand.accelerationY -= fsand.motionY;
						fsand.accelerationZ -= fsand.motionZ;
						//fsand.velocityChanged = true;
						
						moveX = jumpPosX - fsand.posX;
						moveY = jumpPosY - fsand.posY;
						moveZ = jumpPosZ - fsand.posZ;
						break;
					}
				}
			}
			else
			{
				Entity collent = world.findNearestEntityWithinAABB(Entity.class, Sandbbox, fsand);
				
				if (collent != null && (collent instanceof EntityLiving || collent instanceof EntityItem))
				{
					collent.motionX = collent.motionX * 0.2D + fsand.motionX * 0.8D;
					collent.motionY = collent.motionY * 0.2D + fsand.motionY * 0.8D;
					collent.motionZ = collent.motionZ * 0.2D + fsand.motionZ * 0.8D;
					collent.velocityChanged = true;
					if (!world.isRemote)
					{
						if (collent instanceof EntityPlayerMP)
						{
							((EntityPlayerMP) collent).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(collent.getEntityId(), collent.motionX, collent.motionY, collent.motionZ));
						}
					}
				}
			}
			
		}
		
		double density = 1.25D;
		
		if (fsand.media != Block.blockRegistry.getNameForObject(Blocks.air))
		{
			int colon = fsand.media.indexOf(":");
			ModInfo.Log.info("media = " + fsand.media + ":" + fsand.media.substring(colon +1) + ":" + Block.blockRegistry.getNameForObject(blocky));
			mt = Block.getBlockFromName(fsand.media.substring(colon +1))
					.getMaterial();
			if (mt.isLiquid())
			{
				if (mt == Material.lava)
				{
					density = 2000D;
					//fsand.accelerationY += 49.05D / (double)fsand.mass;
				}
				else
				{
					density = 1000D;
					//fsand.accelerationY += 24.525D / (double)fsand.mass;
				}
			}//TODO deal with tnt
			/*else if ( !world.isRemote && !(fsand instanceof EntityTNTPrimed) )
			{
				placeBlock( world, fsand, jumpPosX, jumpPosY, jumpPosZ, in, jn, kn);
				return;
			}*/
		}
		
		density = -0.5D * 0.8D * density / (double) ModConfig.blockMap.get(Block.blockRegistry.getNameForObject(fsand.func_145805_f())).get(fsand.field_145814_a).mass;
		double aaccX = density * fsand.motionX * Math.abs(fsand.motionX);
		double aaccY = density * fsand.motionY * Math.abs(fsand.motionY);
		double aaccZ = density * fsand.motionZ * Math.abs(fsand.motionZ);
		
		fsand.accelerationY -= 0.024525D;
		
		double mmot = fsand.motionX + aaccX;
		if ((fsand.motionX < 0 && mmot > 0) || (fsand.motionX > 0 && mmot < 0))
		{
			aaccX = -0.9D * fsand.motionX;
		}
		
		mmot = fsand.motionY + aaccY;
		if ((fsand.motionY < 0 && mmot > 0) || (fsand.motionY > 0 && mmot < 0))
		{
			aaccY = -0.9D * fsand.motionY;
		}
		
		mmot = fsand.motionZ + aaccZ;
		if ((fsand.motionZ < 0 && mmot > 0) || (fsand.motionZ > 0 && mmot < 0))
		{
			aaccZ = -0.9D * fsand.motionZ;
		}
		
		fsand.accelerationX = fsand.accelerationX + aaccX;
		fsand.accelerationY = fsand.accelerationY + aaccY;
		fsand.accelerationZ = fsand.accelerationZ + aaccZ;
		
		fsand.prevPosX = fsand.posX;
		fsand.prevPosY = fsand.posY;
		fsand.prevPosZ = fsand.posZ;
		
		moveEntity(world, fsand, moveX, moveY, moveZ);
		
		i = MathHelper.floor_double(fsand.posX);
		j = MathHelper.floor_double(fsand.posY);
		k = MathHelper.floor_double(fsand.posZ);
		
		if (fsand.onGround)
		{
			fsand.motionX *= 0.9D;
			fsand.motionZ *= 0.9D;
			//fsand.motionY *= -0.5D;
		}
		//TODO deal with tnt
		/*if ( fsand instanceof EntityTNTPrimed)
		{
		    if (!world.isRemote && ((EntityTNTPrimed)fsand).fuse-- <= 0 )
		    {
		        fsand.setDead();
		    	((EntityTNTPrimed)fsand).explode();
		    }
		    
		    world.spawnParticle("smoke", fsand.posX, fsand.posY + 0.5D, fsand.posZ, 0.0D, 0.0D, 0.0D);
		    
		}
		else*/if (fsand.onGround)
		{
			if (jumpdist2 < 0.05D && BlockMove.canMoveTo(world, i, j, k, em))
			{
				Block block = fsand.func_145805_f();							//Guessed on the method for getPlaceSound()
				world.playSoundEffect(fsand.posX, fsand.posY, fsand.posZ, block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				
				fsand.dead--;
				if (!world.isRemote)
				{
					world.setBlock(i, j, k, block, fsand.field_145814_a, 3);
					BlockMove.setBlockBPdata(world, i, j, k, fsand.bpdata);
					if (fsand.field_145810_d != null)
					{
						TileEntity tile = block.createTileEntity(world, fsand.field_145814_a);
						tile.readFromNBT(fsand.field_145810_d);
						world.setTileEntity(i, j, k, tile);
					}
					if (fsand.isBurning() && world.getBlock(i, j + 1, k) == Blocks.air)
						world.setBlock(i, j + 1, k, Blocks.fire, 0, 3);
					BPEventHandler.tickListMap.get(world.provider.dimensionId).scheduleBlockMoveUpdate(world, i, j, k, Block.blockRegistry.getNameForObject(block), fsand.field_145814_a, true);
					BlockMove.notifyMove(world, i, j, k);
				}
				
			}
		}
		
	}
	
	public static double bSpeedR(double speed)
	{
		return (double) ((int) (speed * 8000D)) / 8000D;
	}
	
	public static void dropFallingSand(EntityMovingBlock fsand)
	{
		if (fsand.field_145810_d != null)
			BlockMove.dropItemsNBT(fsand.worldObj, MathHelper.floor_double(fsand.posX), MathHelper.floor_double(fsand.posY), MathHelper.floor_double(fsand.posZ), fsand.field_145810_d);
		fsand.entityDropItem(new ItemStack(fsand.func_145805_f(), 1, fsand.func_145805_f().damageDropped(fsand.field_145814_a)), 0.0F);
	}
	
	public static Block readFallingSandID(NBTTagCompound nbt)
    {
		Block block = null;
		if (nbt.hasKey("TileID", 99))
		{
			block = Block.getBlockById(nbt.getInteger("TileID"));
		}
		else if (nbt.hasKey("Tile", 99))
		{
			block = Block.getBlockById(nbt.getByte("Tile") & 255);
		}
		ModInfo.Log.info("Returning " + Block.blockRegistry.getNameForObject(block) + " from NBT");
		return block;
    }
}