package blargerist.pie.blockphysics.util;

import blargerist.pie.blockphysics.ModInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityMovingBlock extends Entity
{
    private Block field_145811_e;
    public int field_145814_a;
    public int field_145812_b;
    public boolean field_145813_c;
    private boolean field_145808_f;
    private boolean field_145809_g;
    private int field_145815_h;
    private float field_145816_i;
    public NBTTagCompound field_145810_d;
    /*TODO */
    public int bpdata;
    public byte slideDir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public String media;
    public byte dead;
    
    public EntityMovingBlock(World world)
    {
        super(world);
        this.field_145813_c = true;
        this.field_145815_h = 40;
        this.field_145816_i = 2.0F;
        //TODO
        this.preventEntitySpawning = true;
		this.setSize(0.996F, 0.996F);
		this.yOffset = this.height/2.0F;
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.accelerationX = 0;
		this.accelerationY = -0.024525;
		this.accelerationZ = 0;
		this.slideDir = 0;
		this.noClip = true;
		this.entityCollisionReduction = 0.8F;
		this.dead = 4;
		this.bpdata = 0;
		ModInfo.Log.info("EntityMovingBlock created badly");
    }
    
    public EntityMovingBlock(World world, double x, double y, double z, Block block)
    {
        this(world, x, y, z, block, 0);
    }
    
    public EntityMovingBlock(World world, double x, double y, double z, Block block, int meta)
    {
        super(world);
        this.field_145813_c = true;
        this.field_145815_h = 40;
        this.field_145816_i = 2.0F;
        this.field_145811_e = block;
        this.field_145814_a = meta;
        this.preventEntitySpawning = true;
		this.setSize(0.996F, 0.996F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(x, y, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        //TODO
        this.accelerationX = 0;
		this.accelerationY = -0.024525;
		this.accelerationZ = 0;
		this.slideDir = 0;
		this.noClip = true;
		this.entityCollisionReduction = 0.8F;
		this.dead = 4;
		this.bpdata = 0;
    }
    
    protected boolean canTriggerWalking()
    {
        return false;
    }
    
    protected void entityInit() {}
    
    public boolean canBeCollidedWith()
    {
        return false;
    }
    
    public void onUpdate()
    {
    	ModInfo.Log.info("onUpdateEntity: " + Block.blockRegistry.getNameForObject(this.field_145811_e));
    	if (Block.blockRegistry.getNameForObject(this.field_145811_e) == null || this.field_145811_e.getMaterial() == Material.air)
        {
    		return;
            //this.setDead();
        }
    	else
    	{
    	EntityMove.fallingSandUpdate(worldObj, this);
    	}
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float p_70069_1_)
    {
    	
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound nbtTabCompound)
    {
    	nbtTabCompound.setByte("Tile", (byte)Block.getIdFromBlock(this.field_145811_e));
    	nbtTabCompound.setInteger("TileID", Block.getIdFromBlock(this.field_145811_e));
    	nbtTabCompound.setByte("Data", (byte)this.field_145814_a);
    	nbtTabCompound.setByte("Time", (byte)this.field_145812_b);
    	nbtTabCompound.setBoolean("DropItem", this.field_145813_c);
    	nbtTabCompound.setBoolean("HurtEntities", this.field_145809_g);
    	nbtTabCompound.setFloat("FallHurtAmount", this.field_145816_i);
        nbtTabCompound.setInteger("FallHurtMax", this.field_145815_h);

        if (this.field_145810_d != null)
        {
        	nbtTabCompound.setTag("TileEntityData", this.field_145810_d);
        }
        //TODO
        nbtTabCompound.setTag("Acceleration", newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
        nbtTabCompound.setByte("BPData", (byte)this.bpdata);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        //this.field_145811_e = EntityMove.readFallingSandID(p_70037_1_);
    	if (p_70037_1_.hasKey("TileID", 99))
        {
            this.field_145811_e = Block.getBlockById(p_70037_1_.getInteger("TileID"));
        }
        else
        {
            this.field_145811_e = Block.getBlockById(p_70037_1_.getByte("Tile") & 255);
        }

        this.field_145814_a = p_70037_1_.getByte("Data") & 255;
        this.field_145812_b = p_70037_1_.getByte("Time") & 255;

        if (p_70037_1_.hasKey("HurtEntities", 99))
        {
            this.field_145809_g = p_70037_1_.getBoolean("HurtEntities");
            this.field_145816_i = p_70037_1_.getFloat("FallHurtAmount");
            this.field_145815_h = p_70037_1_.getInteger("FallHurtMax");
        }
        else if (this.field_145811_e == Blocks.anvil)
        {
            this.field_145809_g = true;
        }

        if (p_70037_1_.hasKey("DropItem", 99))
        {
            this.field_145813_c = p_70037_1_.getBoolean("DropItem");
        }

        if (p_70037_1_.hasKey("TileEntityData", 10))
        {
            this.field_145810_d = p_70037_1_.getCompoundTag("TileEntityData");
        }

        if (this.field_145811_e.getMaterial() == Material.air)
        {
            this.field_145811_e = Blocks.sand;
        }
        //TODO
        if (p_70037_1_.hasKey("Acceleration", 9))
			{
				NBTTagList Acceleration = p_70037_1_.getTagList("Acceleration", 9);
				this.accelerationX = Acceleration.func_150309_d(0);
				this.accelerationY = Acceleration.func_150309_d(1);
				this.accelerationZ = Acceleration.func_150309_d(2);
			}else
			{
				this.accelerationX = 9.0;
				this.accelerationY = 0.0;
				this.accelerationZ = 0.0;
			}
			if (p_70037_1_.hasKey("BPData"))
			{
				this.bpdata = p_70037_1_.getByte("BPData");
			}else
			{
				this.bpdata = 0;
			}
    }

    public void func_145806_a(boolean p_145806_1_)
    {
        this.field_145809_g = p_145806_1_;
    }

    public void addEntityCrashInfo(CrashReportCategory p_85029_1_)
    {
        super.addEntityCrashInfo(p_85029_1_);
        p_85029_1_.addCrashSection("Immitating block ID", Integer.valueOf(Block.blockRegistry.getIDForObject(this.field_145811_e)));
        p_85029_1_.addCrashSection("Immitating block data", Integer.valueOf(this.field_145814_a));
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public World func_145807_e()
    {
        return this.worldObj;
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire()
    {
    	//TODO
    	return this.isInWater();
    }

    public Block func_145805_f()
    {
        return this.field_145811_e;
    }
    
    //TODO
    @Override
	public AxisAlignedBB getBoundingBox()
    {
    	return this.boundingBox;
    }
    
    //TODO
    @Override
	public void setInWeb()
    {
    	
    }
    
    //TODO
    @Override
	public void moveEntity(double par1, double par2, double par3)
	{
		EntityMove.moveEntity(this.worldObj, this, par1, par2, par3);
	}
}