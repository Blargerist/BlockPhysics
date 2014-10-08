package blockphysics;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet23VehicleSpawn;
import net.minecraft.network.packet.Packet28EntityVelocity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid="BlockPhysics", name="BlockPhysics", version="0.8.0")
@NetworkMod(clientSideRequired=true, serverSideRequired=true, channels={"BlockPhysics0000", "BlockPhysics0001", "BlockPhysics0002"}, packetHandler = BPacketHandler.class, connectionHandler = BPacketHandler.class)
public class BlockPhysics
{
	public static final int version = 1000800;
	public static final String siteurl = "http://blockphysicssite.no-ip.org";
    public static final String updatecheckurl = "http://blockphysupdate.no-ip.org/lastversion162.html";
    public static Logger bpLogger;
    public static boolean warnerr = false;
    
	@Instance("BlockPhysics")
	public static BlockPhysics instance;
	public static File gameDir = new File(".");
	public static File bpjarFile = null;
	
	protected static Random rand = new Random();
	protected static int updateLCG = (new Random()).nextInt();
	public static boolean skipMove = false;
	
    public static final String conffile = "blockphysics.cfg";
	public static ByteArrayOutputStream cConf = new ByteArrayOutputStream();
	public static String cConfmd5;
		
    public static BlockDef[] blockDef = new BlockDef[512];
    public static MoveDef[] blockMoveDef = new MoveDef[512];
    public static BlockDef[][] blockSet = new BlockDef[4096][16];
    
	static int updateinterval;
    public static int fallRange;
    public static int fallRenderRange;
    public static int maxmovingblocks;
    public static long maxTickTime;
    
    public static boolean catapult;
    public static int explblstr;
    public static boolean explosionfire;
	public static int explosionQueue;
	public static int explosionInterval;
	
    static int lastversion;
    static boolean gui = false;
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
    
    final static double[][][] slideSpeedz = {
    	{{-0.125D,-0.005D,0D},{0D,-0.005D,-0.125D},{0D,-0.005D,0.125D},{0.125D,-0.005D,0D}},
    	{{-0.25D,-0.06D,0D},{0D,-0.06D,-0.25D},{0D,-0.06D,0.25D},{0.25D,-0.06D,0D}},
    	{{-0.3D,-0.25D,0D},{0D,-0.25D,-0.3D},{0D,-0.25D,0.3D},{0.3D,-0.25D,0D}},
    	{{-0.18D,-0.35D,0D},{0D,-0.35D,-0.18D},{0D,-0.35D,0.18D},{0.18D,-0.35D,0D}},
    	{{-0.05D,-0.15D,0D},{0D,-0.15D,-0.05D},{0D,-0.15D,0.05D},{0.05D,-0.15D,0D}}
	};
    
    @EventHandler
    public void preI(FMLPreInitializationEvent event)
    {
    	bpLogger = event.getModLog();
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	warnerr = false;
    	BlockPhysicsUtil.resetConfig();
		BlockPhysicsUtil.initConfig();
        BlockPhysicsUtil.checkForUpdate();
    	writetoLog("mod loaded, version: "+BlockPhysicsUtil.versiontostring(version)+" ( " + siteurl + " ).",1);
    	MinecraftForge.EVENT_BUS.register(new WorldUnloadHook());
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent sevent)
    {
    	sevent.registerServerCommand(new BCommands());
    }
    
    public static void writetoLog(String msg, int lvl)
    {
    	switch (lvl)
    	{
    		case 0:
    			bpLogger.log(Level.WARNING, msg);
    			warnerr = true;
    			break;
    		case 1:
    			bpLogger.log(Level.INFO, msg);
    			break;
    		default:
    			bpLogger.log(Level.FINE, msg);
    			
    	}
    }

    public static int prandnextint(int max)
    {
    	nextrand++;
    	if (nextrand > 99) nextrand = nextrand - 100;
    	return (int)prand[nextrand] % max;
    }
    
    public static void setSkipMove(long tickTime)
    {
    	
    	if ( tickTime > maxTickTime )
    	{
    		if ( skipMove == false ) 
    		{
    			writetoLog("Switching off physics ( "+tickTime+" ).",2 );
    			skipMove = true;
    		}
    	}
    	else 
    	{
    		if ( skipMove == true ) 
    		{
    			writetoLog("Physics are working again ( "+tickTime+" ).",2);
	    		skipMove = false;
	    		nextrand = rand.nextInt(100);
    		}
    	}
    }
    
    public static long setSkipMoveM(long tickTime)
    {
    	
    	if ( (tickTime / 1000000L) > maxTickTime )
    	{
    		if ( skipMove == false ) 
    		{
    			writetoLog("Switching off physics ( "+tickTime+" ).",2 );
    			skipMove = true;
    		}
    	}
    	else 
    	{
    		if ( skipMove == true ) 
    		{
    			writetoLog("Physics are working again ( "+tickTime+" ).",2);
	    		skipMove = false;
	    		nextrand = rand.nextInt(100);
    		}
    	}
    	
    	return tickTime;
    }

    public static boolean canBurn(int blid)
    {
    	if ( Block.blockFlammability[blid] != 0 ) return true;
    	if ( Block.blocksList[blid] == Block.netherrack ) return true;
    	return false;
    }
    
    public static boolean canSetMove(int blid)
    {
    	if (blid == 0) return false;
    	Material mt = Block.blocksList[blid].blockMaterial;
    	if ( !mt.isSolid() || mt.isLiquid() ) return false;	    	
		else return true;
    }
    
    public static boolean canMoveTo(World world, int i, int j, int k, int e)
    {
    	int l = world.getBlockId(i, j, k);
        if (l == 0) return true;
        if (l > 7 && l < 12) return true;		// water, lava
        if (l == 51) return true;				// fire
        
        Material mt = Block.blocksList[l].blockMaterial;
        if ( mt.isLiquid() ) return true; 
        int m = world.getBlockMetadata(i, j, k);
        if ( blockSet[l][m].fragile > 0 && e > blockSet[l][m].strength ) return true;
        
        return false;
    }
    
    public static boolean branch(World world, int i, int j, int k, int bid, int met)
    {
    	if (sameBlock(world.getBlockId(i+1 , j-1, k ),world.getBlockMetadata(i+1 , j-1, k ),bid,met)) return true;
    	if (sameBlock(world.getBlockId(i-1 , j-1, k ),world.getBlockMetadata(i-1 , j-1, k ),bid,met)) return true;
    	if (sameBlock(world.getBlockId(i , j-1, k+1 ),world.getBlockMetadata(i , j-1, k+1 ),bid,met)) return true;
    	if (sameBlock(world.getBlockId(i , j-1, k-1 ),world.getBlockMetadata(i , j-1, k-1 ),bid,met)) return true;
    	if (sameBlock(world.getBlockId(i+1 , j-1, k+1 ),world.getBlockMetadata(i+1 , j-1, k+1 ),bid,met)) return true;
    	if (sameBlock(world.getBlockId(i-1 , j-1, k-1 ),world.getBlockMetadata(i-1 , j-1, k-1 ),bid,met)) return true;
    	if (sameBlock(world.getBlockId(i-1 , j-1, k+1 ),world.getBlockMetadata(i-1 , j-1, k+1 ),bid,met)) return true;
    	if (sameBlock(world.getBlockId(i+1 , j-1, k-1 ),world.getBlockMetadata(i+1 , j-1, k-1 ),bid,met)) return true;
    	return false; 
    }
    
    public static boolean floating(World world, int i, int j, int k, int rad, int bid, int met)
    {
    	for (int jj = j-rad; jj <= j+rad; jj++ )
    	{
    		for (int ii = i-rad; ii <= i+rad; ii++ )
        	{
    			for (int kk = k-rad; kk <= k+rad; kk++ )
    	    	{
    				if (sameBlock(world.getBlockId(ii , jj, kk ),world.getBlockMetadata(ii , jj, kk ),bid,met)) return true;
    	    	}
        	}    		
    	}
    	return false; 
    }
    
    public static boolean hanging(World world, int i, int j, int k, int hang, int bid, int met)
    {
    	int b, m;
    	j++;
    	hang = j+hang;
    	for (int cc = j; cc < hang; cc++ )
    	{
    		b = world.getBlockId(i , cc, k );
    		m = world.getBlockMetadata(i , cc, k );
    		if (blockSet[b][m].supportingblock > 0 ) return true;
    		else if (!sameBlock(bid,met,b,m)) return false;
    		
    	}
    	return false; 
    }
    
    public static boolean attached(World world, int i, int j, int k, int att, int bid, int met)
    {
    	int b, m;
    	int cc;
    	for ( cc = 1; cc <= att; cc++ )
    	{
    		b = world.getBlockId(i + cc , j, k );
    		m = world.getBlockMetadata(i + cc , j, k );
    		if (blockSet[b][m].supportingblock > 0 ) return true;
    		else if (!sameBlock(bid,met,b,m)) break;
    	}
    	
    	for ( cc = 1; cc <= att; cc++ )
    	{
    		b = world.getBlockId(i - cc , j, k );
    		m = world.getBlockMetadata(i - cc , j, k );
    		if (blockSet[b][m].supportingblock > 0 ) return true;
    		else if (!sameBlock(bid,met,b,m)) break;
    	}
    	
    	for ( cc = 1; cc <= att; cc++ )
    	{
    		b = world.getBlockId(i ,j ,k + cc );
    		m = world.getBlockMetadata(i ,j ,k + cc );
    		if (blockSet[b][m].supportingblock > 0 ) return true;
    		else if (!sameBlock(bid,met,b,m)) break;
    	}
    	
    	for ( cc = 1; cc <= att; cc++ )
    	{
    		b = world.getBlockId(i ,j ,k - cc );
    		m = world.getBlockMetadata(i ,j ,k - cc );
    		if (blockSet[b][m].supportingblock > 0 ) return true;
    		else if (!sameBlock(bid,met,b,m)) break;
    	}
   	
    	return false; 
    }
        
    public static boolean ceiling(World world, int i, int j, int k)
    {
    	if ( blockSet[world.getBlockId(i-1, j, k)][world.getBlockMetadata(i-1, j, k)].supportingblock > 0 && blockSet[world.getBlockId(i + 1, j, k )][world.getBlockMetadata(i + 1, j, k )].supportingblock > 0 ) return true;
    	if ( blockSet[world.getBlockId(i, j, k-1)][world.getBlockMetadata(i, j, k-1)].supportingblock > 0 && blockSet[world.getBlockId(i, j, k + 1 )][world.getBlockMetadata(i, j, k + 1 )].supportingblock > 0 ) return true;
    	if ( blockSet[world.getBlockId(i-1, j, k-1)][world.getBlockMetadata(i-1, j, k-1)].supportingblock > 0 && blockSet[world.getBlockId(i + 1, j, k + 1 )][world.getBlockMetadata(i + 1, j, k + 1 )].supportingblock > 0 ) return true;
    	if ( blockSet[world.getBlockId(i-1, j, k+1)][world.getBlockMetadata(i-1, j, k+1)].supportingblock > 0 && blockSet[world.getBlockId(i + 1, j, k-1 )][world.getBlockMetadata(i + 1, j, k-1 )].supportingblock > 0 ) return true;
    	
    	return false;
    }
	
    public static boolean smallArc(World world, int i, int j, int k, int si)
    {
    	if (blockSet[world.getBlockId(i - 1, j, k)][world.getBlockMetadata(i - 1, j, k)].supportingblock > 0 && blockSet[world.getBlockId( i + 1, j, k )][world.getBlockMetadata( i + 1, j, k )].supportingblock > 0 )
    	{
    		if ( blockSet[world.getBlockId( i - 1, j - 1, k )][world.getBlockMetadata( i - 1, j - 1, k )].supportingblock > 0 || blockSet[world.getBlockId(i + 1, j - 1, k)][world.getBlockMetadata(i + 1, j - 1, k)].supportingblock > 0) return true;
    		if (si > 1)
    		{
    			int c;
    			for ( c = 2; c <= si; c++ )
    			{
    				if ( blockSet[world.getBlockId( i - c, j, k )][world.getBlockMetadata( i - c, j, k )].supportingblock > 0 )
    				{
    					if ( blockSet[world.getBlockId( i - c, j - 1, k )][world.getBlockMetadata( i - c, j - 1, k )].supportingblock > 0 ) return true;
    				}
    				else break;
    			}
    			    			
    			for ( c = 2; c <= si; c++ )
    			{
    				if ( blockSet[world.getBlockId( i + c, j, k )][world.getBlockMetadata( i + c, j, k )].supportingblock > 0 )
    				{
    					if ( blockSet[world.getBlockId( i + c, j - 1, k )][world.getBlockMetadata( i + c, j - 1, k )].supportingblock > 0 ) return true;
    				}
    				else break;
    			}
    		}
    		
    	}
    	
    	if (blockSet[world.getBlockId(i, j, k - 1)][world.getBlockMetadata(i, j, k - 1)].supportingblock > 0 && blockSet[world.getBlockId( i, j, k + 1)][world.getBlockMetadata( i, j, k + 1)].supportingblock > 0)
    	{
    		if (blockSet[world.getBlockId(i, j - 1, k - 1)][world.getBlockMetadata(i, j - 1, k - 1)].supportingblock > 0 || blockSet[world.getBlockId(i, j - 1, k + 1)][world.getBlockMetadata(i, j - 1, k + 1)].supportingblock > 0) return true;
    		if (si > 1)
    		{
    			int c;
    			for ( c = 2; c <= si; c++ )
    			{
    				if ( blockSet[world.getBlockId( i, j, k - c )][world.getBlockMetadata( i, j, k - c )].supportingblock > 0 )
    				{
    					if ( blockSet[world.getBlockId( i, j - 1, k - c )][world.getBlockMetadata( i, j - 1, k - c )].supportingblock > 0 ) return true;
    				}
    				else break;
    			}
    			    			
    			for ( c = 2; c <= si; c++ )
    			{
    				if ( blockSet[world.getBlockId( i, j, k + c )][world.getBlockMetadata( i, j, k + c )].supportingblock > 0 )
    				{
    					if ( blockSet[world.getBlockId( i, j - 1, k + c )][world.getBlockMetadata( i, j - 1, k + c )].supportingblock > 0 ) return true;
    				}
    				else break;
    			}
    		}
    	}
    	
    	if (blockSet[world.getBlockId(i - 1, j, k + 1)][world.getBlockMetadata(i - 1, j, k + 1)].supportingblock > 0 && blockSet[world.getBlockId( i + 1, j, k -1 )][world.getBlockMetadata( i + 1, j, k -1 )].supportingblock > 0)
    	{
    		if (blockSet[world.getBlockId(i - 1, j - 1, k +1)][world.getBlockMetadata(i - 1, j - 1, k +1)].supportingblock > 0 || blockSet[world.getBlockId(i + 1, j - 1, k -1)][world.getBlockMetadata(i + 1, j - 1, k -1)].supportingblock > 0) return true;
    		if (si > 1)
    		{
    			int c;
    			for ( c = 2; c <= si; c++ )
    			{
    				if ( blockSet[world.getBlockId( i - c, j, k + c )][world.getBlockMetadata( i - c, j, k + c )].supportingblock > 0 )
    				{
    					if ( blockSet[world.getBlockId( i - c, j - 1, k + c )][world.getBlockMetadata( i - c, j - 1, k + c )].supportingblock > 0 ) return true;
    				}
    				else break;
    			}
    			    			
    			for ( c = 2; c <= si; c++ )
    			{
    				if ( blockSet[world.getBlockId( i + c, j, k - c)][world.getBlockMetadata( i + c, j, k - c)].supportingblock > 0 )
    				{
    					if ( blockSet[world.getBlockId( i + c, j - 1, k - c )][world.getBlockMetadata( i + c, j - 1, k - c )].supportingblock > 0 ) return true;
    				}
    				else break;
    			}
    		}
    	}
    	
    	if (blockSet[world.getBlockId(i + 1, j, k + 1)][world.getBlockMetadata(i + 1, j, k + 1)].supportingblock > 0 && blockSet[world.getBlockId( i - 1, j, k - 1)][world.getBlockMetadata( i - 1, j, k - 1)].supportingblock > 0)
    	{
    		if (blockSet[world.getBlockId(i + 1, j - 1, k + 1)][world.getBlockMetadata(i + 1, j - 1, k + 1)].supportingblock > 0 || blockSet[world.getBlockId(i -1, j - 1, k - 1)][world.getBlockMetadata(i -1, j - 1, k - 1)].supportingblock > 0) return true;
    		if (si > 1)
    		{
    			int c;
    			for ( c = 2; c <= si; c++ )
    			{
    				if ( blockSet[world.getBlockId( i + c, j, k + c )][world.getBlockMetadata( i + c, j, k + c )].supportingblock > 0 )
    				{
    					if ( blockSet[world.getBlockId( i + c, j - 1, k + c )][world.getBlockMetadata( i + c, j - 1, k + c )].supportingblock > 0 ) return true;
    				}
    				else break;
    			}
    			    			
    			for ( c = 2; c <= si; c++ )
    			{
    				if ( blockSet[world.getBlockId( i - c, j, k - c )][world.getBlockMetadata( i - c, j, k - c )].supportingblock > 0 )
    				{
    					if ( blockSet[world.getBlockId( i - c, j - 1, k - c )][world.getBlockMetadata( i - c, j - 1, k - c )].supportingblock > 0 ) return true;
    				}
    				else break;
    			}
    		}
    	}
    	
    	return false;
    }
    
    public static boolean bigArc(World world, int i, int j, int k, int bi)
    {
    	if( blockSet[world.getBlockId(i,j+1,k)][world.getBlockMetadata(i,j+1,k)].supportingblock == 0) return false;
    	
    	int c;
    	if (blockSet[world.getBlockId(i+1,j+1,k)][world.getBlockMetadata(i+1,j+1,k)].supportingblock > 0)
    	{
    		for ( c = 1; c <= bi; c++ )
			{
				if ( blockSet[world.getBlockId( i - c, j, k )][world.getBlockMetadata( i - c, j, k )].supportingblock > 0 )
				{
					if ( blockSet[world.getBlockId( i - c, j - 1, k )][world.getBlockMetadata( i - c, j - 1, k )].supportingblock > 0 ) return true;
				}
				else break;
			}
    	}
    	
    	if ( blockSet[world.getBlockId(i-1,j+1,k)][world.getBlockMetadata(i-1,j+1,k)].supportingblock > 0)
    	{
    		for ( c = 1; c <= bi; c++ )
			{
				if ( blockSet[world.getBlockId( i + c, j, k )][world.getBlockMetadata( i + c, j, k )].supportingblock > 0 )
				{
					if ( blockSet[world.getBlockId( i + c, j - 1, k )][world.getBlockMetadata( i + c, j - 1, k )].supportingblock > 0 ) return true;
				}
				else break;
			}
    	}
    	
    	if ( blockSet[world.getBlockId(i,j+1,k+1)][world.getBlockMetadata(i,j+1,k+1)].supportingblock > 0)
    	{
    		for ( c = 1; c <= bi; c++ )
			{
				if ( blockSet[world.getBlockId( i, j, k - c )][world.getBlockMetadata( i, j, k - c)].supportingblock > 0 )
				{
					if ( blockSet[world.getBlockId( i , j - 1, k - c)][world.getBlockMetadata( i , j - 1, k - c)].supportingblock > 0 ) return true;
				}
				else break;
			}
    	}
    	
    	if ( blockSet[world.getBlockId(i,j+1,k-1)][world.getBlockMetadata(i,j+1,k-1)].supportingblock > 0)
    	{
    		for ( c = 1; c <= bi; c++ )
			{
				if ( blockSet[world.getBlockId( i, j, k + c )][world.getBlockMetadata( i, j, k + c)].supportingblock > 0 )
				{
					if ( blockSet[world.getBlockId( i , j - 1, k + c)][world.getBlockMetadata( i , j - 1, k + c)].supportingblock > 0 ) return true;
				}
				else break;
			}
    	}
    	
    	if ( blockSet[world.getBlockId(i+1,j+1,k+1)][world.getBlockMetadata(i+1,j+1,k+1)].supportingblock > 0)
    	{
    		for ( c = 1; c <= bi; c++ )
			{
				if ( blockSet[world.getBlockId( i-c, j, k - c )][world.getBlockMetadata( i-c, j, k - c)].supportingblock > 0 )
				{
					if ( blockSet[world.getBlockId( i -c, j - 1, k - c)][world.getBlockMetadata( i-c , j - 1, k - c)].supportingblock > 0 ) return true;
				}
				else break;
			}
    	}
    	
    	if ( blockSet[world.getBlockId(i-1,j+1,k-1)][world.getBlockMetadata(i-1,j+1,k-1)].supportingblock > 0)
    	{
    		for ( c = 1; c <= bi; c++ )
			{
				if ( blockSet[world.getBlockId( i+c, j, k + c )][world.getBlockMetadata( i+c, j, k + c)].supportingblock > 0 )
				{
					if ( blockSet[world.getBlockId( i +c, j - 1, k + c)][world.getBlockMetadata( i+c , j - 1, k + c)].supportingblock > 0 ) return true;
				}
				else break;
			}
    	}
    	
    	if ( blockSet[world.getBlockId(i+1,j+1,k-1)][world.getBlockMetadata(i+1,j+1,k-1)].supportingblock > 0)
    	{
    		for ( c = 1; c <= bi; c++ )
			{
				if ( blockSet[world.getBlockId( i-c, j, k + c )][world.getBlockMetadata( i-c, j, k + c)].supportingblock > 0 )
				{
					if ( blockSet[world.getBlockId( i -c, j - 1, k + c)][world.getBlockMetadata( i-c , j - 1, k + c)].supportingblock > 0 ) return true;
				}
				else break;
			}
    	}
    	
    	if ( blockSet[world.getBlockId(i-1,j+1,k+1)][world.getBlockMetadata(i-1,j+1,k+1)].supportingblock > 0)
    	{
    		for ( c = 1; c <= bi; c++ )
			{
				if ( blockSet[world.getBlockId( i+c, j, k - c )][world.getBlockMetadata( i+c, j, k - c)].supportingblock > 0 )
				{
					if ( blockSet[world.getBlockId( i +c, j - 1, k - c)][world.getBlockMetadata( i+c , j - 1, k - c)].supportingblock > 0 ) return true;
				}
				else break;
			}
    	}
    	    	
    	return false;
    }
    
    public static boolean corbel(World world, int i, int j, int k, int ci, int blid, int meta)
    {
    	if( blockSet[blid][meta].supportingblock == 0) return false;
    	int c;
    	for ( c = 1; c <= ci; c++ )
		{
			if ( sameBlock( world.getBlockId( i+c, j, k ), world.getBlockMetadata( i+c, j, k), blid, meta ))
			{
				if ( sameBlock( world.getBlockId( i+c, j - 1, k ), world.getBlockMetadata( i+c, j - 1, k), blid, meta ) ) return true;
			}
			else break;
		}
    	
    	for ( c = 1; c <= ci; c++ )
		{
			if ( sameBlock( world.getBlockId( i-c, j, k ), world.getBlockMetadata( i-c, j, k), blid, meta ))
			{
				if ( sameBlock( world.getBlockId( i-c, j - 1, k ), world.getBlockMetadata( i-c, j - 1, k), blid, meta ) ) return true;
			}
			else break;
		}
    	
    	for ( c = 1; c <= ci; c++ )
		{
			if ( sameBlock( world.getBlockId( i, j, k+c ), world.getBlockMetadata( i, j, k+c), blid, meta ))
			{
				if ( sameBlock( world.getBlockId( i, j - 1, k+c ), world.getBlockMetadata( i, j - 1, k+c), blid, meta ) ) return true;
			}
			else break;
		}
    	
    	for ( c = 1; c <= ci; c++ )
		{
			if ( sameBlock( world.getBlockId( i, j, k-c ), world.getBlockMetadata( i, j, k-c), blid, meta ))
			{
				if ( sameBlock( world.getBlockId( i, j - 1, k-c ), world.getBlockMetadata( i, j - 1, k-c), blid, meta ) ) return true;
			}
			else break;
		}
    	
    	return false;
    }
    
    public static boolean ncorbel(World world, int i, int j, int k, int ni)
    {
    	int c;
    	for ( c = 1; c <= ni; c++ )
		{
			if ( blockSet[world.getBlockId(i - c, j, k)][world.getBlockMetadata(i - c, j, k)].supportingblock > 0)
			{
				if ( blockSet[world.getBlockId(i - c , j - 1, k)][world.getBlockMetadata(i - c , j - 1, k)].supportingblock > 0) return true;
			}
			else break;
		}
    	
    	for ( c = 1; c <= ni; c++ )
		{
			if ( blockSet[world.getBlockId(i + c, j, k)][world.getBlockMetadata(i + c, j, k)].supportingblock > 0)
			{
				if ( blockSet[world.getBlockId(i + c , j - 1, k)][world.getBlockMetadata(i + c , j - 1, k)].supportingblock > 0) return true;
			}
			else break;
		}
    	
    	for ( c = 1; c <= ni; c++ )
		{
			if ( blockSet[world.getBlockId(i, j, k + c)][world.getBlockMetadata(i, j, k + c)].supportingblock > 0)
			{
				if ( blockSet[world.getBlockId(i, j - 1, k + c)][world.getBlockMetadata(i , j - 1, k + c)].supportingblock > 0) return true;
			}
			else break;
		}
    	
    	for ( c = 1; c <= ni; c++ )
		{
			if ( blockSet[world.getBlockId(i, j, k - c)][world.getBlockMetadata(i, j, k - c)].supportingblock > 0)
			{
				if ( blockSet[world.getBlockId(i, j - 1, k - c)][world.getBlockMetadata(i, j - 1, k - c)].supportingblock > 0) return true;
			}
			else break;
		}
    	
    	return false;
    }
    
    public static boolean isFallingEmpty(World world, int i, int j, int k)
    {
        AxisAlignedBB Sandbbox;
        Sandbbox = AxisAlignedBB.getBoundingBox((float)i, (float)j, (float)k, (float)i + 1, (float)j + 1, (float)k + 1);
        List ls = world.getEntitiesWithinAABB(net.minecraft.entity.item.EntityFallingSand.class, Sandbbox);
        if (ls.size() != 0) return false;
        return true;
    }

	public static void notifyMove(World world, int i, int j, int k)
	{
		for (int i1 = i-1; i1 <= i + 1; i1++)
	  	{
	  		for (int j1 = j-1; j1 <= j + 1; j1++)
	  		{
	  			for (int k1 = k-1; k1 <= k + 1; k1++) world.moveTickList.scheduleBlockMoveUpdate(world, i1, j1, k1, world.getBlockId(i1, j1, k1), world.getBlockMetadata(i1, j1, k1), false);
	  		}					
	  	}
	}
	
	public static boolean tryToMove(World world, int i, int j, int k, int blid, int meta, boolean contslide)
    {
		if ( world.isRemote ) return false;
		if ( blockSet[blid][meta].movenum == 0 ) return false;
		
		int players = world.playerEntities.size();
		if (players == 0) return false;
				
		if ( Block.blocksList[blid] instanceof BlockPistonBase && !canmove(world,i,j,k, (BlockPistonBase) Block.blocksList[blid]) ) return false;
		
		boolean outofRenderRange = true;
		boolean outofFallRange = true;

    	int ii;
    	for ( ii = 0; ii < players; ii++ )
    	{
    		EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(ii);
    		if ( Math.abs( i - MathHelper.floor_double(entityplayer.posX) ) <= fallRange && Math.abs( k - MathHelper.floor_double(entityplayer.posZ) ) <= fallRange )
    		{
    			outofFallRange = false;
    			break;
	    	}
     	}
    	
    	if ( outofFallRange ) return false;
    	    	
    	int move = 0;
		if (blockSet[blid][meta].movenum == 2)
		{
			if ( (getBlockBPdata( world,i, j, k) &15) >= blockSet[blid][meta].moveflipnumber) move = 1;
		}
		
		int movedefnum = blockSet[blid][meta].move[move];
		
		if ( blockMoveDef[movedefnum].floating[0] > 0 )
		{
			if (floating(world,i,j,k,blockMoveDef[movedefnum].floating[0],blockMoveDef[movedefnum].floating[1],blockMoveDef[movedefnum].floating[2])) return false;
			move = 1;
			movedefnum = blockSet[blid][meta].move[move];
			setBlockBPdata( world, i, j, k, 15);
		}
	
		if ( blockMoveDef[movedefnum].movetype == 3 )
		{
			if ( canMoveTo(world, i, j - 1, k, blockSet[blid][meta].mass/10) )
			{
				int sv = blockMoveDef[movedefnum].hanging;
				if ( sv > 0 && hanging(world, i, j, k, sv, blid, meta)) return false;
				sv = blockMoveDef[movedefnum].attached;
				if ( sv > 0 && attached(world, i, j, k, sv, blid, meta)) return false;
				sv = blockMoveDef[movedefnum].ncorbel;
				if ( sv > 0 && ncorbel(world, i, j, k, sv)) return false;
				sv = blockMoveDef[movedefnum].corbel;
				if ( sv > 0 && corbel(world, i, j, k, sv, blid, meta)) return false;
				if ( blockMoveDef[movedefnum].ceiling && ceiling(world, i, j, k)) return false;
				sv = blockMoveDef[movedefnum].smallarc;
				if ( sv > 0 && smallArc(world, i, j, k, sv)) return false;
		    	sv = blockMoveDef[movedefnum].bigarc;
				if ( sv > 0 && bigArc(world, i, j, k, sv)) return false;
				if (blockMoveDef[movedefnum].branch && branch(world, i, j, k, blid, meta) ) return false;
				
				Block block = Block.blocksList[blid];
				
				if ( block.hasTileEntity(meta) ) 
        		{
        			NBTTagCompound nnn = new NBTTagCompound("TileEntityData");
        			world.getBlockTileEntity(i, j, k).writeToNBT(nnn);
        			dropItemsNBT(world, i, j, k, nnn);            			
        			world.removeBlockTileEntity(i, j, k);
        		}
				
				block.dropBlockAsItem(world, i, j, k, meta, 0);
				
				world.setBlock(i, j, k, 0, 0, 3);
				
				world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
	    		
	            return true;
			}
			else return false;
		}
    	
    	
		for (int iii = ii; iii < players; iii++)
    	{
    		EntityPlayer entityplayer = (EntityPlayer)world.playerEntities.get(iii);

    		if( Math.abs( i - MathHelper.floor_double(entityplayer.posX) ) <= fallRenderRange && Math.abs( k - MathHelper.floor_double(entityplayer.posZ) ) <= fallRenderRange ) 
    		{	
    			if ( MathHelper.floor_double(entityplayer.posY) - j <= fallRenderRange )
				{
					outofRenderRange = false;
					break;
				}
    		}
    	}
    	    			
		if ( !outofRenderRange && ((WorldServer)world).getEntityTracker().movingblocks >= maxmovingblocks ) return false;
    			
		int movetype;
		if ( contslide ) movetype = 2;
		else movetype = blockMoveDef[movedefnum].movetype;
					
		if ( movetype == 0) return false;
		int ms = blockSet[blid][meta].mass/10;
		boolean canfall = canMoveTo(world, i, j - 1, k, ms);
    	
    	if ( !canfall )
		{
			if ( movetype == 1 ) return false;
			if ( blockMoveDef[movedefnum].slidechance != 100 && ( blockMoveDef[movedefnum].slidechance == 0 || blockMoveDef[movedefnum].slidechance < prandnextint(100) + 1 )) return false;
		}
	     	    	
    	if ( !contslide )
    	{
			int sv = blockMoveDef[movedefnum].hanging;
			if ( sv > 0 && hanging(world,i,j,k,sv,blid,meta)) return false;
			sv = blockMoveDef[movedefnum].attached;
			if ( sv > 0 && attached(world, i, j, k, sv, blid, meta)) return false;
			sv = blockMoveDef[movedefnum].ncorbel;
			if ( sv > 0 && ncorbel(world, i, j, k, sv)) return false;
			sv = blockMoveDef[movedefnum].corbel;
			if ( sv > 0 && corbel(world, i, j, k, sv, blid, meta)) return false;
			if ( blockMoveDef[movedefnum].ceiling && ceiling(world, i, j, k)) return false;
			sv = blockMoveDef[movedefnum].smallarc;
			if ( sv > 0 && smallArc(world, i, j, k, sv)) return false;
	    	sv = blockMoveDef[movedefnum].bigarc;
			if ( sv > 0 && bigArc(world, i, j, k, sv)) return false;
			if (blockMoveDef[movedefnum].branch && branch(world, i, j, k, blid, meta)) return false;
    	}
    	
    	boolean canslide[] = new boolean[4];
    	if ( movetype == 2 && !canfall )
    	{
    		canslide[0] = canMoveTo(world, i - 1, j - 1, k, ms);
			canslide[1] = canMoveTo(world, i, j - 1, k - 1, ms);
			canslide[2] = canMoveTo(world, i, j - 1, k + 1, ms);
			canslide[3] = canMoveTo(world, i + 1, j - 1, k, ms);
    		if (!(canslide[0] || canslide[1] || canslide[2] || canslide[3])) return false;
			
    		if (canslide[0]) canslide[0] = canMoveTo(world, i - 1, j, k, ms);
    		if (canslide[1]) canslide[1] = canMoveTo(world, i, j, k - 1, ms);
    		if (canslide[2]) canslide[2] = canMoveTo(world, i, j, k + 1, ms);
    		if (canslide[3]) canslide[3] = canMoveTo(world, i + 1, j, k, ms);
    		if (!(canslide[0] || canslide[1] || canslide[2] || canslide[3])) return false;
    	}    	
    	
    	if ( blid == 2 || blid == 60 || blid == 110 ) blid = 3;
    	
    	if ( outofRenderRange )
        {
			int bpdata = getBlockBPdata( world,i, j, k);
    		world.setBlock(i, j, k, 0, 0, 3);
			setBlockBPdata( world,i, j, k, 0);
			notifyMove(world, i, j, k);
			int jv = j;
			if (canfall)
        	{
    			for (; canMoveTo(world, i, jv - 1, k, ms) && jv > 0; jv--);
            	if (jv > 0) 
            	{
            		world.setBlock(i, jv, k, blid, meta, 3);
            		setBlockBPdata( world,i, jv, k, bpdata);
            		notifyMove(world, i, jv, k);
            	}
            }
        	else
        	{
        		byte slide[] = {0,0,0,0};
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
                if ( count > 1 ) rr = prandnextint(count);
                switch (slide[rr])
                {
                    case 0:
                        id = - 1;
                        break;
                    case 1:
                        kd = - 1;
                        break;
                    case 2:
                        kd = + 1;
                        break;
                    case 3:
                        id = + 1;
                        break;
                }
                int iv = i + id, kv = k + kd; 
                for (; canMoveTo(world, iv, jv - 1, kv, ms) && jv > 0; jv--);
            	if (jv > 0) 
            	{
            		world.setBlock(iv, jv, kv, blid, meta, 3);
            		setBlockBPdata( world,iv, jv, kv, bpdata);
            		notifyMove(world, iv, jv, kv);
            	}
        	}
			j++;
			tryToMove( world, i, j, k, world.getBlockId( i, j , k ), world.getBlockMetadata( i, j , k ),false );
			return true;
        }
    	
    	if ( canfall )
    	{
    		//int metadata = world.getBlockMetadata(i, j, k);
    		EntityFallingSand entityfallingsand = new EntityFallingSand(world, 0.5D + i, 0.5D + j, 0.5D + k, blid, meta);
    		if ( Block.blocksList[blid].hasTileEntity(meta) ) 
    		{
    			entityfallingsand.fallingBlockTileEntityData = new NBTTagCompound("TileEntityData");
    			world.getBlockTileEntity(i, j, k).writeToNBT(entityfallingsand.fallingBlockTileEntityData);
    			world.removeBlockTileEntity(i, j, k);
    		}
    		if (canBurn(blid) && world.getBlockId(i, j+1, k) == 51) entityfallingsand.setFire(60);
            entityfallingsand.bpdata = getBlockBPdata( world,i, j, k);
    		world.spawnEntityInWorld(entityfallingsand);
     	}
    	else
    	{
    		if (canslide[0]) canslide[0] = isFallingEmpty(world, i - 1, j, k);
    		if (canslide[1]) canslide[1] = isFallingEmpty(world, i, j, k - 1);
    		if (canslide[2]) canslide[2] = isFallingEmpty(world, i, j, k + 1);
    		if (canslide[3]) canslide[3] = isFallingEmpty(world, i + 1, j, k);
    		if (!(canslide[0] || canslide[1] || canslide[2] || canslide[3])) return false;	
    		
    		byte slide[] = {0,0,0,0};
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
            if ( count > 1 ) rr = prandnextint(count);
            switch (slide[rr])
            {
                case 0:
                    id = - 1;
                    break;
                case 1:
                    kd = - 1;
                    break;
                case 2:
                    kd = + 1;
                    break;
                case 3:
                    id = + 1;
                    break;
            }
            //int metadata = world.getBlockMetadata(i, j, k);
            EntityFallingSand entityfallingsand = new EntityFallingSand(world, 0.5D + i + 0.0625D * id, 0.5D + j - 0.0625D, 0.5D + k + 0.0625D * kd, blid, meta);
            if ( Block.blocksList[blid].hasTileEntity(meta) ) 
            {
            	entityfallingsand.fallingBlockTileEntityData = new NBTTagCompound("TileEntityData");
            	world.getBlockTileEntity(i, j, k).writeToNBT(entityfallingsand.fallingBlockTileEntityData);
            	world.removeBlockTileEntity(i, j, k);
            }
    		
            entityfallingsand.slideDir = (byte) (slide[rr]+1);
            if (canBurn(blid) && world.getBlockId(i, j+1, k) == 51) entityfallingsand.setFire(60);
            entityfallingsand.bpdata = getBlockBPdata( world,i, j, k);
            world.spawnEntityInWorld(entityfallingsand);
    	}
    	world.setBlock(i, j, k, 0, 0, 3);
    	setBlockBPdata( world,i, j, k, 0);
    	j++;
    	tryToMove(world, i, j, k, world.getBlockId(i, j, k), world.getBlockMetadata(i, j, k), false);
        return true;
    }
	
	public static void moveChangeMechanic(World world, int i, int j, int k, int  blockID, int radius, int strength)
	{
		int state,bid,m;
		for (int ii = i - radius; ii <= i + radius; ii++ )
		{
			for (int jj = j - radius; jj <= j + radius; jj++ )
			{
				for (int kk = k - radius; kk <= k + radius; kk++ )
				{
					bid = world.getBlockId( ii, jj, kk );
					m = world.getBlockMetadata(ii, jj, kk );
					if ( blockSet[bid][m].movenum == 2 && blockSet[bid][m].movechanger > 1 /*&& ( blockID == 0 || blockID == bid )*/ )
					{
						int bpd = getBlockBPdata( world, ii, jj, kk );
						state = bpd & 15;
						bpd = bpd - state;
						if (state < 15 )
						{
							state = state + prandnextint(4) + strength;
							if ( state > 15 ) state = 15;
							setBlockBPdata( world, ii, jj, kk, bpd + state);
						}
					}
				}
			}
		}
	}
	
	public static void updatePistonState(World par1World, int par2, int par3, int par4, BlockPistonBase par1block, boolean isSticky)
    {   
    	if (par1World.isRemote) return;
		int var5 = par1World.getBlockMetadata(par2, par3, par4);
        int var6 = par1block.getOrientation(var5);
        if ( var6 > 5 ) var6 = 0;
        
        int vv = 0;
        if ( par1block.isIndirectlyPowered(par1World, par2, par3, par4, var6)) vv = 8;

        if (vv == (var5 & 8)) return;
        
        int i2 = par2 + Facing.offsetsXForSide[var6];
    	int j2 = par3 + Facing.offsetsYForSide[var6];
    	int k2 = par4 + Facing.offsetsZForSide[var6];
    	
    	if ( par1World.getBlockId(i2, j2, k2) == Block.pistonMoving.blockID || par1World.pistonMoveBlocks.contains(""+par2+"."+par3+"."+par4)) return;

    	int blid2, meta;
               
        if ( vv == 8 )  //extending
        {
            int ext = 0;
   		
    		if (catapult && !skipMove && ((WorldServer)par1World).getEntityTracker().movingblocks < maxmovingblocks)
    		{
    			boolean catapultpowered = false;
    			boolean catapultprecise = true;
    			int[] power = {0,0,0,0,0,0,0,0,0};
    			int xpw = par2 - Facing.offsetsXForSide[var6];
    			int ypw = par3 - Facing.offsetsYForSide[var6];
    			int zpw = par4 - Facing.offsetsZForSide[var6];
    			
    			if ( Block.blocksList[par1World.getBlockId(xpw,ypw,zpw)] == Block.dispenser )
    			{
    	        	TileEntityDispenser tileentitydispenser = (TileEntityDispenser)par1World.getBlockTileEntity(xpw, ypw, zpw);	
    	        	if (tileentitydispenser != null)
    	        	{
    	        		ItemStack powstack;
    	        		for ( int pp = 0; pp < 9; pp++)
    	        		{
    	        			powstack = tileentitydispenser.getStackInSlot(pp);
    	        			
    	        			if ( catapultprecise && (pp == 0 || pp == 2 || pp == 6 || pp == 8) && ( powstack == null || powstack.getItem() != Item.ingotGold || powstack.stackSize < 4 ) ) catapultprecise = false;
	                		
    	        			if ( powstack != null && powstack.getItem() == Item.redstone)
    	        			{
    	        				catapultpowered = true;
    	        				power[pp] = powstack.stackSize;
    	        			}
    	        		}
    	        	}
    	    	}
    			
    			if ( catapultpowered )
    			{
    				ext = canExtend(par1World, par2, par3, par4, var6, par1block, true);
    	        	if ( ext == 0 ) return;
    				
    				double pspX = 0, pspY = 0, pspZ = 0;
    				switch ( var6 )
            		{
            			case 0:
            			{
            				pspX = -(double)power[0] / 3D + (double)power[2] / 3D - (double)power[6] / 3D + (double)power[8] / 3D - power[3] / 2D + power[5] / 2D;
            				pspY = -( (double)power[4] + (double)power[0] / 3D + (double)power[6] / 3D + (double)power[2] / 3D + (double)power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D);
            				pspZ = -(double)power[0] / 3D - (double)power[2] / 3D + (double)power[6] / 3D + (double)power[8] / 3D - power[1] / 2D + power[7] / 2D;
            				break;
            			}
            			case 1:
            			{
            				pspX = -(double)power[0] / 3D + (double)power[2] / 3D - (double)power[6] / 3D + (double)power[8] / 3D - power[3] / 2D + power[5] / 2D;
            				pspY = ( (double)power[4] + (double)power[0] / 3D + (double)power[6] / 3D + (double)power[2] / 3D + (double)power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D);
            				pspZ = -(double)power[0] / 3D - (double)power[2] / 3D + (double)power[6] / 3D + (double)power[8] / 3D - power[1] / 2D + power[7] / 2D;
            				break;
            			}
            			case 2:   	// - Z
            			{
            				pspX = (double)power[0] / 3D - (double)power[2] / 3D + (double)power[6] / 3D - (double)power[8] / 3D + power[3] / 2D - power[5] / 2D;
            				pspY = (double)power[0] / 3D - (double)power[6] / 3D + (double)power[2] / 3D - (double)power[8] / 3D + power[1] / 2D - power[7] / 2D;
            				pspZ = -( (double)power[4] + (double)power[0] / 3D + (double)power[6] / 3D + (double)power[2] / 3D + (double)power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D);
            				break;
            			}
            			case 3:		// + Z
            			{
            				pspX = -( (double)power[0] / 3D - (double)power[2] / 3D + (double)power[6] / 3D - (double)power[8] / 3D + power[3] / 2D - power[5] / 2D );
            				pspY = (double)power[0] / 3D - (double)power[6] / 3D + (double)power[2] / 3D - (double)power[8] / 3D + power[1] / 2D - power[7] / 2D;
            				pspZ = (double)power[4] + (double)power[0] / 3D + (double)power[6] / 3D + (double)power[2] / 3D + (double)power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D;
            				break;
            			}
            			case 4:		// - X
            			{
            				pspX = -( (double)power[4] + (double)power[0] / 3D + (double)power[6] / 3D + (double)power[2] / 3D + (double)power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D);
            				pspY = (double)power[0] / 3D - (double)power[6] / 3D + (double)power[2] / 3D - (double)power[8] / 3D + power[1] / 2D - power[7] / 2D;
            				pspZ = -( (double)power[0] / 3D - (double)power[2] / 3D + (double)power[6] / 3D - (double)power[8] / 3D + power[3] / 2D - power[5] / 2D );
            				break;
            			}
            			case 5:		// + X
            			{
            				pspX = (double)power[4] + (double)power[0] / 3D + (double)power[6] / 3D + (double)power[2] / 3D + (double)power[8] / 3D + power[1] / 2D + power[7] / 2D + power[3] / 2D + power[5] / 2D;
            				pspY = (double)power[0] / 3D - (double)power[6] / 3D + (double)power[2] / 3D - (double)power[8] / 3D + power[1] / 2D - power[7] / 2D;
            				pspZ = (double)power[0] / 3D - (double)power[2] / 3D + (double)power[6] / 3D - (double)power[8] / 3D + power[3] / 2D - power[5] / 2D;
            				break;
            			}
            		}	
    				
    				pspX = pspX * 40D;
	            	pspY = pspY * 40D;
	            	pspZ = pspZ * 40D;
	            	
	            	double dirX = 1D;
	            	if (pspX < 0) dirX = -1D;
	            	double dirY = 1D;
	            	if (pspY < 0) dirY = -1D;
	            	double dirZ = 1D;
	            	if (pspZ < 0) dirZ = -1D;
	            	
    	            double error = 1D;
    	        	if ( catapultprecise ) error = 0D;
    	        	        				
    				int sticky = 0;
    	    		if ( isSticky ) sticky = 1;
    	    		
    				int xx = par2 + ext * Facing.offsetsXForSide[var6];
    	            int yy = par3 + ext * Facing.offsetsYForSide[var6];
    	            int zz = par4 + ext * Facing.offsetsZForSide[var6];
    	        	            	            
	            	int blid = par1World.getBlockId(xx, yy, zz);
	            	meta = par1World.getBlockMetadata(xx, yy, zz);
	            		            	
	            	if ( blockSet[blid][meta].fragile > 0 )
	            	{
	            		if ( Block.blocksList[blid].hasTileEntity(meta) ) 
	            		{
	            			NBTTagCompound nnn = new NBTTagCompound("TileEntityData");
	            			par1World.getBlockTileEntity(xx, yy, zz).writeToNBT(nnn);
	            			dropItemsNBT(par1World, xx, yy, zz, nnn);            			
	            			par1World.removeBlockTileEntity(xx, yy, zz);
	            		}
	            	
	            		if ( blockSet[blid][meta].fragile == 2 ) Block.blocksList[blid].dropBlockAsItem( par1World, xx, yy, zz, meta, 0 );
	            		
	            		par1World.setBlock(xx, yy, zz, 0, 0, 3);
	            	}
	            		            	
    	            int sstick = 1;
    	            if (ext == 1) sstick = 0;
    	            
    	            int bxx = par2 + (ext + sticky * sstick) * Facing.offsetsXForSide[var6];
    	            int byy = par3 + (ext + sticky * sstick) * Facing.offsetsYForSide[var6];
    	            int bzz = par4 + (ext + sticky * sstick) * Facing.offsetsZForSide[var6];
    	            
    	            double smass = 0;
    	            
    	            for (int i = ext-1; i > sticky; i--)
    	            {
    	            	xx -= Facing.offsetsXForSide[var6]; 
    	                yy -= Facing.offsetsYForSide[var6];
    	                zz -= Facing.offsetsZForSide[var6];
    	                blid2 = par1World.getBlockId(xx, yy, zz );
    	                meta = par1World.getBlockMetadata(xx, yy, zz);
    	                Block bb = Block.blocksList[blid2];
    	                if ( bb == Block.grass || bb == Block.crops || bb == Block.mycelium ) blid2 = Block.dirt.blockID;
    	                smass += BlockPhysics.blockSet[blid2][meta].mass;
    	            }

    	            if ( canMoveTo(par1World, bxx + Facing.offsetsXForSide[var6], byy + Facing.offsetsYForSide[var6], bzz + Facing.offsetsZForSide[var6], 0) )
    	            {
	    	            AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(bxx, byy, bzz, bxx + 1, byy + 1, bzz + 1 );
	    	            List list = par1World.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
	    	            for (Iterator iterator = list.iterator(); iterator.hasNext(); )
	    	            {
    	            		if ( ((Entity)iterator.next()).ticksExisted < 6  ) iterator.remove();
    	            	}	
	    	            	    	            
	    	            if (!list.isEmpty())
	    	            {
	    	            	Entity entity;
	    	                
	    	            	for (Iterator iterator = list.iterator(); iterator.hasNext(); )
	    	                {
	    	            		entity = (Entity)iterator.next();
	    	            		if (entity instanceof EntityFallingSand ) smass += BlockPhysics.blockSet[((EntityFallingSand)entity).blockID][((EntityFallingSand)entity).metadata].mass;
	    	            		else if (entity instanceof EntityLiving ) smass += ((double)((EntityLiving)entity).func_110138_aP() * 3D);
	    	            		else smass += 25D;
	    	                }	    	            	
	    	            	
	    	            	double sx,sy,sz;
	    	            	for (Iterator iterator = list.iterator(); iterator.hasNext(); )
	    	                {
	    	                    entity = (Entity)iterator.next();	//optimize!

	    	                    sx = (double)MathHelper.sqrt_double(Math.abs(pspX) / smass) * dirX + (error * rand.nextGaussian() * 0.02D);
	    	                    sy = (double)MathHelper.sqrt_double(Math.abs(pspY) / smass) * dirY + (error * rand.nextGaussian() * 0.02D);
	    	                    sz = (double)MathHelper.sqrt_double(Math.abs(pspZ) / smass) * dirZ + (error * rand.nextGaussian() * 0.02D);
	    	                    entity.setPosition(entity.posX + (double)Facing.offsetsXForSide[var6], entity.posY + (double)Facing.offsetsYForSide[var6], entity.posZ + (double)Facing.offsetsZForSide[var6]);
	    	                    entity.addVelocity(bSpeedR(sx), bSpeedR(sy), bSpeedR(sz));
	    	                    entity.velocityChanged = true;
	    	                    if ( entity instanceof net.minecraft.entity.player.EntityPlayerMP  )
	    			            {	
	    	                    	((EntityPlayerMP)entity).playerNetServerHandler.sendPacketToPlayer(new Packet28EntityVelocity(entity.entityId, bSpeedR(sx), bSpeedR(sy), bSpeedR(sz)));
	     			            }
	    	                }
	    	            }
    	            }
    	            
    	            xx = par2 + ext * Facing.offsetsXForSide[var6];
    	            yy = par3 + ext * Facing.offsetsYForSide[var6];
    	            zz = par4 + ext * Facing.offsetsZForSide[var6];
 
    	            pspX = (double)MathHelper.sqrt_double(Math.abs(pspX) / smass) * dirX + (error * rand.nextGaussian() * 0.02D);
    	            pspY = (double)MathHelper.sqrt_double(Math.abs(pspY) / smass) * dirY + (error * rand.nextGaussian() * 0.02D);
    	            pspZ = (double)MathHelper.sqrt_double(Math.abs(pspZ) / smass) * dirZ + (error * rand.nextGaussian() * 0.02D);
    	            
    	            for (int i = ext-1; i > sticky; i--)
    	            {
    	            	xx -= Facing.offsetsXForSide[var6]; 
    	                yy -= Facing.offsetsYForSide[var6];
    	                zz -= Facing.offsetsZForSide[var6];
    	                blid2 = par1World.getBlockId(xx, yy, zz );
    	                meta = par1World.getBlockMetadata(xx , yy, zz);
    	                Block bb = Block.blocksList[blid2];
    	                if ( bb == Block.grass || bb == Block.crops || bb == Block.mycelium ) blid2 = Block.dirt.blockID;
    	                if ( bb == Block.pistonBase || bb == Block.pistonStickyBase ) meta = meta & 7;   	                
    	                
    	                EntityFallingSand entityfallingsand = new EntityFallingSand(par1World, 0.5D + xx + Facing.offsetsXForSide[var6], 0.5D + yy + Facing.offsetsYForSide[var6], 0.5D + zz + Facing.offsetsZForSide[var6], blid2, meta);
	                  	entityfallingsand.motionX = bSpeedR(pspX + ( error * rand.nextGaussian() * 0.01D));
    	                entityfallingsand.motionY = bSpeedR(pspY + ( error * rand.nextGaussian() * 0.01D));
    	                entityfallingsand.motionZ = bSpeedR(pspZ + ( error * rand.nextGaussian() * 0.01D));
    	                if (canBurn(blid2) )
    	                {
    	                	if ( i == ext - 1 && blid == 51 ) entityfallingsand.setFire(60);
    	                	if ( par1World.getBlockId(xx, yy+1, zz) == 51 ) entityfallingsand.setFire(60);
    	                }
    	                entityfallingsand.bpdata = getBlockBPdata( par1World, xx, yy, zz );
    	                if ( Block.blocksList[blid2].hasTileEntity(meta) ) 
    	                {
    	                	entityfallingsand.fallingBlockTileEntityData = new NBTTagCompound("TileEntityData");
    	                	par1World.getBlockTileEntity(xx, yy, zz).writeToNBT(entityfallingsand.fallingBlockTileEntityData);
    	                	par1World.removeBlockTileEntity(xx, yy, zz);
    	                }
    	                par1World.setBlock( xx, yy, zz, 0, 0, 3 );
    	                setBlockBPdata( par1World, xx, yy, zz, 0 );
    	                par1World.spawnEntityInWorld(entityfallingsand);
    	            }
   	                           
    	            if ( sticky == 1 ) 
    	            {
    	            	par1World.addBlockEvent(par2, par3, par4, par1block.blockID, 3, var6);   // var5??
    	            	pistonMoveMark(par1World, par2, par3, par4, 2, var6);
    	            }
    	            else
    	            {
    	            	par1World.addBlockEvent(par2, par3, par4, par1block.blockID, 2, var6);
    	            	pistonMoveMark(par1World, par2, par3, par4, 1, var6);
    	            }
    			}
    			else
    			{
    				ext = canExtend(par1World, par2, par3, par4, var6, par1block, false);
    				if ( ext == 0 )	return;
    				par1World.addBlockEvent(par2, par3, par4, par1block.blockID, ext+1, var6);
    				pistonMoveMark(par1World, par2, par3, par4, ext, var6);
    			}
    		}
    		else
    		{	
    			ext = canExtend(par1World, par2, par3, par4, var6, par1block, false);
    			if ( ext == 0 ) return;
    			par1World.addBlockEvent(par2, par3, par4, par1block.blockID, ext+1, var6);
    			pistonMoveMark(par1World, par2, par3, par4, ext, var6);
    		}
    		
    		par1World.setBlockMetadataWithNotify(par2, par3, par4, (var5 | 8), 0);
        }
        else
        {
        	
        	if ( isSticky )
        	{
        		int xx = par2 + 2 * Facing.offsetsXForSide[var6];
        		int yy = par3 + 2 * Facing.offsetsYForSide[var6];
        		int zz = par4 + 2 * Facing.offsetsZForSide[var6];
        		blid2 = par1World.getBlockId(xx, yy, zz);
        		meta = par1World.getBlockMetadata(xx, yy, zz);
        		boolean pull = true;
        		Block bb = Block.blocksList[blid2];
        		boolean empty = (blid2 == 0 || ( blid2 > 7 && blid2 < 12) || blid2 == 51 || bb.blockMaterial.isLiquid() );
        		if (par1World.pistonMoveBlocks.contains(""+xx+"."+yy+"."+zz) || empty || (blockSet[blid2][meta].pushtype != 1 && blockSet[blid2][meta].pushtype != 2) ) pull = false;
                else if ( ( bb == Block.pistonBase || bb == Block.pistonStickyBase ) && !canmove(par1World, xx, yy, zz, par1block)) pull = false;
                                
                if ( pull )
                {
                	par1World.addBlockEvent(par2, par3, par4, par1block.blockID, 1, var6);
                	pistonMoveMark(par1World, par2, par3, par4, 2, var6);
                }
                else
                {
                	par1World.addBlockEvent(par2, par3, par4, par1block.blockID, 0, var6);
                	pistonMoveMark(par1World, par2, par3, par4, 1, var6);
                }
          	}
        	else
        	{
        		par1World.addBlockEvent(par2, par3, par4, par1block.blockID, 0, var6);
        		pistonMoveMark(par1World, par2, par3, par4, 1, var6);
        	}
        	
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, var5 & 7, 0);
        }
    }
	
	private static int canExtend(World par0World, int par1, int par2, int par3, int par4, BlockPistonBase par1block, boolean catp)
    {	
    	if (par0World.pistonMoveBlocks.contains(""+par1+"."+par2+"."+par3)) return 0;
    	int var8, blid, meta;
        boolean empty;
        //TileEntity ent;
        for ( var8 = 1; var8 < 14; var8++ )
        {
        	
        	par1 += Facing.offsetsXForSide[par4];
            par2 += Facing.offsetsYForSide[par4];
            par3 += Facing.offsetsZForSide[par4];
            
            if (par0World.pistonMoveBlocks.contains(""+par1+"."+par2+"."+par3)) return 0;
            
            blid = par0World.getBlockId(par1, par2, par3);
            Block bb = Block.blocksList[blid];
    		empty = (blid == 0 || ( blid > 7 && blid < 12) || blid == 51 || bb.blockMaterial.isLiquid() );
    		if ( empty )
			{
				
				if (par2 <= 0 || par2 >= par0World.getHeight() - 1) return 0;
				else return var8;
				
			}
    		
    		meta = par0World.getBlockMetadata(par1, par2, par3);            
            if ( blockSet[blid][meta].pushtype == 0 ) 
            {
            	if (blockSet[blid][meta].fragile == 0) return 0;
            	else if (blockSet[blid][meta].strength > 10) return 0;
            	else return var8;
            }
            if (catp)
            {
            	if ( blockSet[blid][meta].pushtype == 2 ) return 0;
            }
            else
            {
            	if ( blockSet[blid][meta].pushtype == 3 ) return 0;
            }
            
            
            //ent =  par0World.getBlockTileEntity(par1, par2, par3);
            //if ( ent != null ) return 0;
                        
            if ( (bb == Block.pistonBase || bb == Block.pistonStickyBase) && !canmove(par0World, par1, par2, par3, par1block)) return 0;
        }
        return 0;
    }

    public static boolean canmove(World world, int i, int j, int k, BlockPistonBase par1block)
    {
    	int orient = par1block.getOrientation(world.getBlockMetadata(i, j, k));
    	if ( orient > 5 ) orient = 0;
    	int i2 = i + Facing.offsetsXForSide[orient];
    	int j2 = j + Facing.offsetsYForSide[orient];
    	int k2 = k + Facing.offsetsZForSide[orient];
    	
    	int blid = world.getBlockId(i2, j2, k2);
    	
    	if ( blid != Block.pistonMoving.blockID && blid != Block.pistonExtension.blockID ) return true;
    	
    	int orient2 = par1block.getOrientation(world.getBlockMetadata(i2, j2, k2) );
    	if ( orient2 > 5 ) orient2 = 0;
    	
    	if ( blid == Block.pistonExtension.blockID && orient == orient2 ) return false;
    	if ( blid == Block.pistonMoving.blockID )
    	{
    		TileEntity var7 = world.getBlockTileEntity(i2, j2, k2);
            if (var7 instanceof TileEntityPiston)
            {
            	if ( ((TileEntityPiston)var7).getPistonOrientation() == orient ) 
            	{
            		return false;
            	}
            }
    	}
    	return true;
    }
    
    static void pistonMoveMark(World world, int i, int j, int k, int lngth, int orient )
    {
    	int io = Facing.offsetsXForSide[orient];
    	int jo = Facing.offsetsYForSide[orient];
    	int ko = Facing.offsetsZForSide[orient];
    	for (int l = 0; l <= lngth; l++)
    	{
    		world.pistonMoveBlocks.add(""+i+"."+j+"."+k);
    		i = i + io;
    		j = j + jo;
    		k = k + ko;
    	}
    }
    
    public static boolean onBlockPistonEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6, BlockPistonBase par1block, boolean isSticky)
    {
    	if ( par5 > 1 )	//extending
        {
            par5--;
            int xx = par2 + par5 * Facing.offsetsXForSide[par6];
            int yy = par3 + par5 * Facing.offsetsYForSide[par6];
            int zz = par4 + par5 * Facing.offsetsZForSide[par6];
        	
            if (!par1World.isRemote)
            {
            	int blid = par1World.getBlockId(xx, yy, zz);
            	int meta = par1World.getBlockMetadata(xx, yy, zz);
            	            	
            	if ( blockSet[blid][meta].fragile > 0 )
            	{
            		if ( Block.blocksList[blid].hasTileEntity(meta) ) 
            		{
            			NBTTagCompound nnn = new NBTTagCompound("TileEntityData");
            			par1World.getBlockTileEntity(xx, yy, zz).writeToNBT(nnn);
            			dropItemsNBT(par1World, xx, yy, zz, nnn);            			
            			par1World.removeBlockTileEntity(xx, yy, zz);
            		}
            	
            		if ( blockSet[blid][meta].fragile == 2 ) Block.blocksList[blid].dropBlockAsItem( par1World, xx, yy, zz, meta, 0 );
            		
            		par1World.setBlock(xx, yy, zz, 0, 0, 3);
            	}
            }
        	
            for (int i = par5; i > 1; i--)
            {
            	int xxf =  xx - Facing.offsetsXForSide[par6];
            	int yyf =  yy - Facing.offsetsYForSide[par6];
            	int zzf =  zz - Facing.offsetsZForSide[par6];
            	
            	int var12 = par1World.getBlockId(xxf, yyf, zzf);
                int var13 = par1World.getBlockMetadata(xxf, yyf, zzf);
                int bpmeta = getBlockBPdata(par1World, xxf, yyf, zzf);
                
                Block bb = Block.blocksList[var12];
                
                if ( bb == Block.pistonBase || bb == Block.pistonStickyBase )  var13 = var13 & 7;       
                
                TileEntityPiston tePiston = new TileEntityPiston(var12, var13, par6, true, false);
                tePiston.bpmeta = bpmeta;
                
                if ( Block.blocksList[var12].hasTileEntity(var13) )
                {
                	tePiston.movingBlockTileEntityData = new NBTTagCompound("TileEntityData");
                	par1World.getBlockTileEntity(xxf, yyf, zzf).writeToNBT(tePiston.movingBlockTileEntityData);
                	par1World.removeBlockTileEntity(xxf, yyf, zzf);
                }
                
                par1World.setBlock(xx, yy, zz, Block.pistonMoving.blockID, var13, 2);
                par1World.setBlockTileEntity(xx, yy, zz, tePiston);
                xx -= Facing.offsetsXForSide[par6];
                yy -= Facing.offsetsYForSide[par6];
                zz -= Facing.offsetsZForSide[par6];
            }
            par1World.setBlock(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6], Block.pistonMoving.blockID, par6 | (isSticky ? 8 : 0), 2);
            par1World.setBlockTileEntity(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6], BlockPistonMoving.getTileEntity(Block.pistonExtension.blockID, par6 | (isSticky ? 8 : 0), par6, true, true));
            par1World.setBlock(par2, par3, par4, par1block.blockID, par6 | 8, 2);
           
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "tile.piston.out", 0.5F, rand.nextFloat() * 0.25F + 0.6F);
        }
        else				//retracting  (0 normal, 1 sticky )
        {
            par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, par6, 2);
            par1World.setBlockTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(par1block.blockID, par6, par6, false, true));

            if ( par5 == 0 )    
            {
            	par1World.setBlock(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6], 0, 0, 3);
            }
            else
            {
            	int var8 = par2 + Facing.offsetsXForSide[par6] * 2;
                int var9 = par3 + Facing.offsetsYForSide[par6] * 2;
                int var10 = par4 + Facing.offsetsZForSide[par6] * 2;
                int var11 = par1World.getBlockId(var8, var9, var10);
                int var12 = par1World.getBlockMetadata(var8, var9, var10);
                int bpmeta = getBlockBPdata(par1World, var8, var9, var10);
                
                TileEntityPiston tePiston = new TileEntityPiston(var11, var12, par6, false, false);
                tePiston.bpmeta = bpmeta;
                
                if ( Block.blocksList[var11].hasTileEntity(var12) )
                {
                	tePiston.movingBlockTileEntityData = new NBTTagCompound("TileEntityData");
                	par1World.getBlockTileEntity(var8, var9, var10).writeToNBT(tePiston.movingBlockTileEntityData);
                	par1World.removeBlockTileEntity(var8, var9, var10);
                }
                               
                par2 += Facing.offsetsXForSide[par6];
                par3 += Facing.offsetsYForSide[par6];
                par4 += Facing.offsetsZForSide[par6];
                par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, var12, 0);
                par1World.setBlockTileEntity(par2, par3, par4, tePiston);
                par1World.setBlock(var8, var9, var10, 0, 0, 3);
            }
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "tile.piston.in", 0.5F, rand.nextFloat() * 0.15F + 0.6F);
        }
    	return true;
    }
	
    public static void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int meta, int blockID)
    {
		if (!par1World.isRemote) 
		{
			meta &= 15;
			if ( blockSet[blockID][meta].movenum == 2 && blockSet[blockID][meta].movechanger > 1 ) moveChangeMechanic(par1World, par2, par3, par4, blockID, 1, 0);
			notifyMove(par1World, par2, par3, par4);
		}
    }
    
    public static void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int blockID)
    {
    	if (par1World.isRemote ) return;
    	par1World.moveTickList.scheduleBlockMoveUpdate(par1World, par2, par3, par4, blockID, par1World.getBlockMetadata(par2, par3, par4), false);
    }
    
    public static void onEntityCollidedWithBlock( World world, int par1, int par2, int par3, int blockID, Entity par5Entity)
    {
     	if ( blockSet[blockID][world.getBlockMetadata(par1, par2, par3)].trapping ) par5Entity.setInWeb();
	}
    
    public static void onPostBlockPlaced(World par1World, int par2, int par3, int par4, int blockID, int meta)
    {
    	if (!par1World.isRemote)
    	{
    		meta = meta & 15;
    		if ( blockSet[blockID][meta].movenum == 2 ) setBlockBPdata( par1World,par2, par3, par4, 15 * blockSet[blockID][meta].placedmove);
    		notifyMove(par1World, par2, par3, par4);
    	}
    }
   
    public static void fallingSandUpdate(World world, EntityFallingSand fsand)
    {
	    fsand.fallTime++;
    	if (fsand.fallTime < 3) 
    	{
    		if (fsand.fallTime == 1) fsand.metadata &= 15;
    		return;
    	}
    	    	
    	int i = MathHelper.floor_double(fsand.posX);
    	int j = MathHelper.floor_double(fsand.posY);
    	int k = MathHelper.floor_double(fsand.posZ);
    	
    	if (fsand.fallTime == 4) notifyMove(world,i,j,k);
    	
    	if (!world.isRemote && fsand.dead < 4 )
	    {
	    	fsand.dead--;
	    	if (fsand.dead <= 0) fsand.setDead();
	    	return;
	    }
    	Material mt;
    	fsand.noClip = true;
    	fsand.onGround = false;
    	
    	if ( j < -3  || fsand.fallTime > 600)
        {
        	fsand.setDead();
            if ( !world.isRemote ) dropFallingSand( fsand );
        }   	    	    	    				
   	    
    	fsand.media = world.getBlockId(i,j, k);

        if (fsand.slideDir != 0)
        {
        	if (fsand.fallTime < 8)
        	{
	        	int stime = fsand.fallTime - 3;
	        	int sdir = fsand.slideDir - 1;
	        	
        		if (stime == 0)
	        	{
	        		switch ( sdir )
	    	    	{
    	    			case 0:	fsand.setPosition((double)i - 0.0625D  + 0.5D, (double)j - 0.0625D  + 0.5D, (double)k  + 0.5D); break;
    	    			case 1:	fsand.setPosition((double)i  + 0.5D, (double)j - 0.0625D  + 0.5D, (double)k - 0.0625D  + 0.5D); break;
    	    			case 2: fsand.setPosition((double)i  + 0.5D, (double)j - 0.0625D  + 0.5D, (double)k + 0.0625D  + 0.5D); break;
    	    			case 3:	fsand.setPosition((double)i + 0.0625D  + 0.5D, (double)j - 0.0625D  + 0.5D, (double)k  + 0.5D); break;
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
        
        if (fsand.motionX > 3.9D) fsand.motionX = 3.9D;
        else if (fsand.motionX < -3.9D) fsand.motionX = -3.9D;
        if (fsand.motionY > 3.9D) fsand.motionY = 3.9D;
        else if (fsand.motionY < -3.9D) fsand.motionY = -3.9D;
        if (fsand.motionZ > 3.9D) fsand.motionZ = 3.9D;
        else if (fsand.motionZ < -3.9D) fsand.motionZ = -3.9D;
        
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
	
    	if ( fsand.slideDir == 0 )
        {
    		fsand.motionX = bSpeedR(fsand.motionX + caccelerationX);
    		fsand.motionY = bSpeedR(fsand.motionY + caccelerationY);
    		fsand.motionZ = bSpeedR(fsand.motionZ + caccelerationZ);
        }
    	
    	double moveX = cmotionX + caccelerationX * 0.5D; 
        double moveY = cmotionY + caccelerationY * 0.5D;
        double moveZ = cmotionZ + caccelerationZ * 0.5D;
        
        double axisaligned_maxmove = MathHelper.abs_max(MathHelper.abs_max(moveX, moveZ),moveY);
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
        
        double djumpdist2 = blockofsX * blockofsX + blockofsY * blockofsY + blockofsZ *blockofsZ;
        double jumpdist2 = moveX * moveX + moveY * moveY + moveZ * moveZ;
       
        int mass = BlockPhysics.blockSet[fsand.blockID][fsand.metadata].mass;
        int em = mass / 10 + (int)(0.5D * (double)mass * jumpdist2 );
        //if (!world.isRemote) writetoLog(";"+em);
        
        if ( fsand.isBurning() && jumpdist2 > 4D && Block.blocksList[fsand.blockID] != Block.netherrack ) fsand.extinguish();
        
        AxisAlignedBB Sandbbox = null;
        	
        int ii;
        if ( djumpdist2 == 0 ) ii = 0;
        else ii = ( int ) Math.ceil((double)MathHelper.sqrt_double(jumpdist2/djumpdist2));
        
    	double jumpPosX = 0;
    	double jumpPosY = 0;
    	double jumpPosZ = 0;

    	int in = 0;
    	int jn = 0;
    	int kn = 0;
    	int ip = i;
    	int jp = j;
    	int kp = k;
    			
    	for (int i1 = 1; i1 <= ii ; i1++)
    	{
    		if (i1 == ii)
    		{
    			jumpPosX = fsand.posX + moveX;
				jumpPosY = fsand.posY + moveY;
				jumpPosZ = fsand.posZ + moveZ;
    				
				Sandbbox = fsand.boundingBox.copy();
	            Sandbbox.offset(moveX,moveY,moveZ);
    		}
    		else if (i1 == 1)
    		{
    			jumpPosX = fsand.posX + blockofsX;
		    	jumpPosY = fsand.posY + blockofsY;
		    	jumpPosZ = fsand.posZ + blockofsZ;
		    	
		    	Sandbbox = fsand.boundingBox.copy();
		    	Sandbbox.offset(blockofsX,blockofsY,blockofsZ);
    		}
    		else
    		{
    			jumpPosX += blockofsX;
    			jumpPosY += blockofsY;
    			jumpPosZ += blockofsZ;
    			
    			Sandbbox.offset(blockofsX,blockofsY,blockofsZ);
    		}
    		
    		//writetoLog(";"+world.isRemote+";ID: ;"+fsand.entityId+";slidedir: ;"+fsand.slideDir+";onground: ;"+fsand.onGround+";mass: ;"+blockSet[fsand.blockID][fsand.metadata].mass+"; falltime: ;"+fsand.fallTime+";pos: ;"+jumpPosX+"; "+jumpPosY+"; "+jumpPosZ+";speed: ;"+fsand.motionX+"; "+fsand.motionY+"; "+fsand.motionZ+"; acceleration: ;"+fsand.accelerationX+"; "+fsand.accelerationY+"; "+fsand.accelerationZ);
        	
    		in = MathHelper.floor_double(jumpPosX);
		    jn = MathHelper.floor_double(jumpPosY);
		    kn = MathHelper.floor_double(jumpPosZ);
		    
		    if (jp != jn || ip != in || kp != kn)
			{
	        	int bidn = world.getBlockId(in, jn, kn);
				int metan = world.getBlockMetadata(in, jn, kn);
	        	
				
				if (  blockSet[bidn][metan].fragile > 0 ) 
				{
	        		Block block = Block.blocksList[bidn];
    	    		
	        		if (  !world.isRemote )
	        		{
	        			if ( blockSet[bidn][metan].fragile > 0 )
	                	{
	                		if ( Block.blocksList[bidn].hasTileEntity(metan) ) 
	                		{
	                			NBTTagCompound nnn = new NBTTagCompound("TileEntityData");
	                			world.getBlockTileEntity(in, jn, kn).writeToNBT(nnn);
	                			dropItemsNBT(world, in, jn, kn, nnn);            			
	                			world.removeBlockTileEntity(in, jn, kn);
	                		}
	                	
	                		if ( blockSet[bidn][metan].fragile == 2 ) Block.blocksList[bidn].dropBlockAsItem( world, in, jn, kn, metan, 0 );
	                		
	                		world.setBlock(in, jn, kn, 0, 0, 3);
	                	}
	        		}
	        		bidn = 0;
	        		
	        		world.playSoundEffect((double)((float)in + 0.5F), (double)((float)jn + 0.5F), (double)((float)kn + 0.5F), block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
	        		
	        		double sl = 1D - (double)blockSet[bidn][metan].strength / 64000D;
	        		fsand.motionX *= sl;
	        		fsand.motionY *= sl;
	        		fsand.motionZ *= sl;
				}
	    		
	        	if ( fsand.isBurning() && bidn == 0 ) 
	        	{
	        		world.spawnParticle("largesmoke", (float)jumpPosX + rand.nextFloat(),(float)jumpPosY + rand.nextFloat(), (float)jumpPosZ + rand.nextFloat(), 0D, 0D, 0D);
	            	world.spawnParticle("flame", (float)jumpPosX + rand.nextFloat(), (float)jumpPosY + rand.nextFloat(), (float)jumpPosZ + rand.nextFloat(), 0D, 0.2D, 0D);
	        	}
	        	
	        	if (fsand.media != bidn)
	        	{        		
	        		if (bidn != 0)
	        		{
		        		mt = Block.blocksList[bidn].blockMaterial;
		        		if ( mt.isLiquid() )
		        		{
		        			if ( mt == Material.lava )
		        			{
		        				if (canBurn(fsand.blockID)) fsand.setFire(60);
			        			else fsand.setFire(1);
			        			world.playSoundAtEntity(fsand, "random.fizz", 1F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
		        			}
		        			else
		        			{
		        				fsand.extinguish();
			        			world.playSoundAtEntity(fsand, "random.splash", 1F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
		        			}	        			
		        		}
		        		else if ( bidn == Block.fire.blockID )
		        		{
		        			world.playSoundAtEntity(fsand, "random.fizz", 0.5F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
		        		}
	        		}
	        		
	        		fsand.media = bidn;
	        		
	        		if ( fsand.slideDir == 0 && bidn != 51)
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
		    
		    if ( fsand.slideDir == 0 )
		    {
		    
			    if ( (!canMoveTo(world, MathHelper.floor_double(jumpPosX + 0.499D), MathHelper.floor_double(jumpPosY +  0.499D),MathHelper.floor_double(jumpPosZ +  0.499D), em) || !canMoveTo(world, MathHelper.floor_double(jumpPosX +  0.499D), MathHelper.floor_double(jumpPosY + 0.499D),MathHelper.floor_double(jumpPosZ -  0.499D), em) || !canMoveTo(world, MathHelper.floor_double(jumpPosX + 0.499D), MathHelper.floor_double(jumpPosY - 0.499D),MathHelper.floor_double(jumpPosZ + 0.499D), em) || !canMoveTo(world, MathHelper.floor_double(jumpPosX + 0.499D), MathHelper.floor_double(jumpPosY - 0.499D),MathHelper.floor_double(jumpPosZ - 0.499D), em) || !canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY + 0.499D), MathHelper.floor_double(jumpPosZ + 0.499D), em) || !canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY + 0.499D),MathHelper.floor_double(jumpPosZ - 0.499D), em) || !canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY - 0.499D),MathHelper.floor_double(jumpPosZ + 0.499D), em) || !canMoveTo(world, MathHelper.floor_double(jumpPosX - 0.499D), MathHelper.floor_double(jumpPosY - 0.499D),MathHelper.floor_double(jumpPosZ - 0.499D), em)))
	    		{
			    	
					double eimp = 0.0005D * jumpdist2 * BlockPhysics.blockSet[fsand.blockID][fsand.metadata].mass;
	    			
	    			if ( eimp > 0.5D )
	    			{
	    				if ( !world.isRemote ) 
						{
		    				if ( eimp > 3.5D ) eimp = 3.5D;
	    					//writetoLog(""+Math.sqrt(jumpdist2)+"       "+eimp);
		    				Explosion var10 = new Explosion( world, fsand, jumpPosX, jumpPosY, jumpPosZ, (float) eimp );
		    			    if ( fsand.isBurning() ) var10.isFlaming = true;
		    			    var10.impact = true;
		    			    world.explosionQueue.add(var10);
		    			}
	    				
	    			    fsand.motionX *= 0.7D;
				    	fsand.motionY *= 0.7D;
				    	fsand.motionZ *= 0.7D;
				    	fsand.velocityChanged = true;
	    			}
	    			
	    			if ( blockSet[fsand.blockID][fsand.metadata].fragile > 0 && em > blockSet[fsand.blockID][fsand.metadata].strength)
	    			{
	    				Block block = Block.blocksList[fsand.blockID];
	    	    		world.playSoundEffect(jumpPosX, jumpPosY, jumpPosZ, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
	    	    		
	    	    		fsand.dead--;
	    	    		if (!world.isRemote)
	    	    		{
	    	    			if ( blockSet[fsand.blockID][fsand.metadata].fragile == 2 ) 
	    	    			{
	    	    				fsand.posX = jumpPosX;
	    	    				fsand.posY = jumpPosY;
	    	    				fsand.posZ = jumpPosZ;
	    	    				dropFallingSand(fsand);
	    	    			}
	    	    			else if( blockSet[fsand.blockID][fsand.metadata].fragile == 1 && fsand.fallingBlockTileEntityData != null ) dropItemsNBT(world, MathHelper.floor_double(jumpPosX),  MathHelper.floor_double(jumpPosY),  MathHelper.floor_double(jumpPosZ), fsand.fallingBlockTileEntityData);
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
			    
			    if ( collent != null )
		        {
			    	if ( collent instanceof EntityFallingSand)
			    	{
			    		
			    		double m1 = (double)BlockPhysics.blockSet[fsand.blockID][fsand.metadata].mass;
			        	double m2 = BlockPhysics.blockSet[((EntityFallingSand)collent).blockID][((EntityFallingSand)collent).metadata].mass;
			    		double smass =  m1 + m2;
			    		double vv;
			    		
			    		double is = m1 * fsand.motionX + m2 * collent.motionX;
			    		vv = bSpeedR( (0.98D * is) / smass );
			    		
			    		fsand.motionX = vv;
			    		collent.motionX = vv;
			    		
			    		is = m1 * fsand.motionZ + m2 * collent.motionZ;
			    		vv = bSpeedR( (0.98D * is) / smass );
			    		
			    		fsand.motionZ = vv;
			    		collent.motionZ = vv;
			    		
			    		is = m1 * fsand.motionY + m2 * collent.motionY;
			    		vv = bSpeedR( (0.98D * is) / smass );
			    		
			    		fsand.motionY = vv;
			    		collent.motionY = vv;

			    		fsand.velocityChanged = true;
			    		collent.velocityChanged = true;
			    		
			    		if ( blockSet[((EntityFallingSand)collent).blockID][((EntityFallingSand)collent).metadata].fragile > 0 && em > blockSet[((EntityFallingSand)collent).blockID][((EntityFallingSand)collent).metadata].strength)
		    			{
		    				Block block = Block.blocksList[((EntityFallingSand)collent).blockID];
		    	    		world.playSoundEffect(collent.posX, collent.posY, collent.posZ, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
		    	    		
		    	    		((EntityFallingSand)collent).dead--;
		    	    		if (!world.isRemote)
		    	    		{
		    	    			if ( blockSet[((EntityFallingSand)collent).blockID][((EntityFallingSand)collent).metadata].fragile == 2 ) dropFallingSand(((EntityFallingSand)collent));
		    	    			else if( blockSet[((EntityFallingSand)collent).blockID][((EntityFallingSand)collent).metadata].fragile == 1 && ((EntityFallingSand)collent).fallingBlockTileEntityData != null ) dropItemsNBT(world, MathHelper.floor_double(collent.posX),  MathHelper.floor_double(collent.posY),  MathHelper.floor_double(collent.posZ), ((EntityFallingSand)collent).fallingBlockTileEntityData);
		    	    		}
		    			}
			    		
			    		if ( blockSet[fsand.blockID][fsand.metadata].fragile > 0 && em > blockSet[fsand.blockID][fsand.metadata].strength)
		    			{
		    				Block block = Block.blocksList[fsand.blockID];
		    	    		world.playSoundEffect(jumpPosX, jumpPosY, jumpPosZ, block.stepSound.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
		    	    		
		    	    		fsand.dead--;
		    	    		if (!world.isRemote)
		    	    		{
		    	    			if ( blockSet[fsand.blockID][fsand.metadata].fragile == 2 ) 
		    	    			{
		    	    				fsand.posX = jumpPosX;
		    	    				fsand.posY = jumpPosY;
		    	    				fsand.posZ = jumpPosZ;
		    	    				dropFallingSand(fsand);
		    	    			}
		    	    			else if( blockSet[fsand.blockID][fsand.metadata].fragile == 1 && fsand.fallingBlockTileEntityData != null ) dropItemsNBT(world, MathHelper.floor_double(jumpPosX),  MathHelper.floor_double(jumpPosY),  MathHelper.floor_double(jumpPosZ), fsand.fallingBlockTileEntityData);
		    	    		}
		    				return;
		    			}

			    		fsand.noClip = false;
		    			
				    	moveX = jumpPosX - fsand.posX;
			    		moveY = jumpPosY - fsand.posY;
			    		moveZ = jumpPosZ - fsand.posZ;
				    	break;
			    	}
			    	else if ( collent instanceof EntityLiving )
			    	{
			    		//entityCollide(world, fsand, collent, (double)((EntityLiving) collent).getMaxHealth() * 3.0D, true);
			    		
			    		double m1 = (double)BlockPhysics.blockSet[fsand.blockID][fsand.metadata].mass;
			        	double m2 = (double)((EntityLiving) collent).func_110138_aP() * 3.0D;
			        	double smass =  m1 + m2;
			    		double vv;
			    		double damage = fsand.motionX*fsand.motionX + fsand.motionY*fsand.motionY + fsand.motionZ*fsand.motionZ;
			    		
			    		double is = m1 * fsand.motionX + m2 * ( collent.posX - collent.prevPosX );
			    		vv = bSpeedR( (0.98D * is) / smass );
			    		damage -= vv * vv;
			    		fsand.motionX = vv;
			    		collent.motionX = vv;
			    		
			    		is = m1 * fsand.motionZ + m2 * ( collent.posZ - collent.prevPosZ );
			    		vv = bSpeedR( (0.98D * is) / smass );
			    		damage -= vv * vv;
			    		fsand.motionZ = vv;
			    		collent.motionZ = vv;
			    		
			    		if ( fsand.motionY < 0 && collent.onGround) 
			    		{
			    			if (fsand.motionY < - 0.3D) vv = bSpeedR(0.5D * fsand.motionY);
			    			else vv = fsand.motionY;
			    		}
			    		else
			    		{
			    			is = m1 * fsand.motionY + m2 * ( collent.posY - collent.prevPosY );
			    			vv = bSpeedR( (0.98D * is) / smass );
			    		}
			    		
			    		damage -= vv * vv;
			    		fsand.motionY = vv;
			    		collent.motionY = vv;
			    		
			    		fsand.velocityChanged = true;
			    		collent.velocityChanged = true;
			    		
			    		int d = (int)(0.083D * m1 * damage);
						if ( d > 0 )
						{
							//if ( !world.isRemote ) writetoLog(""+d);
							if ( d > 4 )
				            {
				                world.playSoundAtEntity(collent, "damage.fallbig", 1.0F, 1.0F);
				            }
				           
				    		if ( !world.isRemote ) ((EntityLiving)collent).attackEntityFrom(DamageSource.fallingBlock, d);	
						}				    		
			    		
			    		if ( !world.isRemote )
			            {
				            if ( collent instanceof EntityPlayerMP  )
				            {	
				            	((EntityPlayerMP)collent).playerNetServerHandler.sendPacketToPlayer(new Packet28EntityVelocity(collent.entityId, collent.motionX, collent.motionY, collent.motionZ));
				            }
				        }    	

		    			moveX = jumpPosX - fsand.posX;
			    		moveY = jumpPosY - fsand.posY;
			    		moveZ = jumpPosZ - fsand.posZ;
		    			break;
			    	}
			    	else if ( collent instanceof EntityItem )
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
			    
			    if ( collent != null && ( collent instanceof EntityLiving || collent instanceof EntityItem ) )
		        {
			    	collent.motionX = collent.motionX * 0.2D + fsand.motionX * 0.8D;
			    	collent.motionY = collent.motionY * 0.2D + fsand.motionY * 0.8D;
			    	collent.motionZ = collent.motionZ * 0.2D + fsand.motionZ * 0.8D;
			    	collent.velocityChanged = true;
			    	if ( !world.isRemote )
		            {
			            if ( collent instanceof EntityPlayerMP  )
			            {	
			            	((EntityPlayerMP)collent).playerNetServerHandler.sendPacketToPlayer(new Packet28EntityVelocity(collent.entityId, collent.motionX, collent.motionY, collent.motionZ));
			            }
			        }  
		        }
		    }
		    
		    
    	}
    	
    	double density = 1.25D;
    			
    	if ( fsand.media != 0 )
    	{
    		mt = Block.blocksList[fsand.media].blockMaterial;
    		if ( mt.isLiquid() )
			{
				if ( mt == Material.lava)
				{
					density = 2000D;
	    			//fsand.accelerationY += 49.05D / (double)fsand.mass;
				}
				else
				{
					density = 1000D;
		    		//fsand.accelerationY += 24.525D / (double)fsand.mass;
				}
			}
			else if ( !world.isRemote && !(fsand instanceof EntityTNTPrimed) )
			{
				placeBlock( world, fsand, jumpPosX, jumpPosY, jumpPosZ, in, jn, kn);
				return;
			}
    	}
    	    	
    	density = -0.5D * 0.8D * density / (double)BlockPhysics.blockSet[fsand.blockID][fsand.metadata].mass;    	   	
    	double aaccX = density * fsand.motionX * Math.abs(fsand.motionX);
    	double aaccY = density * fsand.motionY * Math.abs(fsand.motionY);
    	double aaccZ = density * fsand.motionZ * Math.abs(fsand.motionZ);
    	
    	fsand.accelerationY -= 0.024525D;
    	
    	double mmot = fsand.motionX + aaccX;
    	if ( (fsand.motionX < 0 && mmot > 0) || (fsand.motionX > 0 && mmot < 0) )
    	{
    		aaccX = -0.9D * fsand.motionX;
    	}
    	
    	mmot = fsand.motionY + aaccY;
    	if ( (fsand.motionY < 0 && mmot > 0) || (fsand.motionY > 0 && mmot < 0))
    	{
    		aaccY = -0.9D * fsand.motionY;
    	}
    	
    	mmot = fsand.motionZ + aaccZ;
    	if ( (fsand.motionZ < 0 && mmot > 0) || (fsand.motionZ > 0 && mmot < 0) )
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
		
		if ( fsand instanceof EntityTNTPrimed)
    	{
            if (!world.isRemote && ((EntityTNTPrimed)fsand).fuse-- <= 0 )
            {
                fsand.setDead();
            	((EntityTNTPrimed)fsand).explode();
            }
            
            world.spawnParticle("smoke", fsand.posX, fsand.posY + 0.5D, fsand.posZ, 0.0D, 0.0D, 0.0D);
            
    	}
    	else if ( fsand.onGround )
    	{
    		if ( jumpdist2 < 0.05D && canMoveTo(world, i, j, k, em) )
    		{
    			Block block = Block.blocksList[fsand.blockID];
	    		world.playSoundEffect(fsand.posX, fsand.posY, fsand.posZ, block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
	    		
	    		fsand.dead--;
	    		if (!world.isRemote)
	    		{
	    			world.setBlock(i, j, k, fsand.blockID, fsand.metadata, 3);
	    			setBlockBPdata( world,i, j, k, fsand.bpdata);
	    			if (fsand.fallingBlockTileEntityData != null)
	    			{
	    				TileEntity tile = Block.blocksList[fsand.blockID].createTileEntity(world, fsand.metadata);
	    				tile.readFromNBT(fsand.fallingBlockTileEntityData);
	    				world.setBlockTileEntity(i, j, k, tile);
	    			}
	    			if (fsand.isBurning() && world.getBlockId(i, j+1, k) == 0) world.setBlock(i, j +1, k, Block.fire.blockID, 0, 3);
	    			world.moveTickList.scheduleBlockMoveUpdate(world, i, j, k, fsand.blockID, fsand.metadata, true);
		    		notifyMove(world, i,j,k);
	    		}
	    		
    		}
    	}

    }
    
    protected static void placeBlock(World world, EntityFallingSand fsand, double jumpPosX, double jumpPosY, double jumpPosZ,  int i, int j, int k)
    {
	    double dist2 = 100;
	    double dist22, s1 = 0, s2 = 0, s3 = 0;
    	int x = 0;
    	int y = 0;
    	int z = 0;
    	
	    for (int ii = i-1; ii < i+2; ii++)
	    {
	    	for (int jj = j-1; jj < j+2; jj++)
		    {
	    		for (int kk = k-1; kk < k+2; kk++)
			    {
	    			if (canMoveTo(world, ii, jj, kk, 0))
	    			{
	    				s1 = ((0.5D + (double)ii) - jumpPosX);
	    				s2 = ((0.5D + (double)jj) - jumpPosY);
	    				s3 = ((0.5D + (double)kk) - jumpPosZ);
	    				dist22 = s1 * s1 + s2 * s2 + s3 * s3;
	    				if (dist22 < dist2 )
	    				{
	    					dist2 = dist22;
	    					x = ii;
	    					y = jj;
	    					z = kk;
	    				}
	    			}
			    }
		    }
	    }
    	
	    fsand.setDead();
    	//if (world.isRemote) return; 
    	
    	if ( dist2 < 100 ) 
    	{
    		world.setBlock(x, y, z, fsand.blockID, fsand.metadata, 3);
    		setBlockBPdata( world,x, y, z, fsand.bpdata);
    		if (fsand.fallingBlockTileEntityData != null)
			{
				TileEntity tile = Block.blocksList[fsand.blockID].createTileEntity(world, fsand.metadata);
				tile.readFromNBT(fsand.fallingBlockTileEntityData);
				world.setBlockTileEntity(x, y, z, tile);
			}
    		world.moveTickList.scheduleBlockMoveUpdate(world, x, y, z, fsand.blockID, fsand.metadata, true);
    	}
    	else 
    	{
    		fsand.posX = 0.5D + (double)i;
    		fsand.posY = 0.5D + (double)j;
    		fsand.posZ = 0.5D + (double)k;
    		dropFallingSand ( fsand );
    	}
    }
    
    public static void doExplosionA(World world, Explosion explosion)
    {
    	if ( world.isRemote ) return;
    	float var1 = explosion.explosionSize;
        HashSet var2 = new HashSet();
        int var3;
        int var4;
        int var5;
        double var15;
        double var17;
        double var19;

        if ( !explosion.impact )
    	{ 
    		explosion.explosionSize *= 2.0F;
	        var3 = MathHelper.floor_double(explosion.explosionX - (double)explosion.explosionSize - 1.0D);
	        var4 = MathHelper.floor_double(explosion.explosionX + (double)explosion.explosionSize + 1.0D);
	        var5 = MathHelper.floor_double(explosion.explosionY - (double)explosion.explosionSize - 1.0D);
	        int var27 = MathHelper.floor_double(explosion.explosionY + (double)explosion.explosionSize + 1.0D);
	        int var7 = MathHelper.floor_double(explosion.explosionZ - (double)explosion.explosionSize - 1.0D);
	        int var28 = MathHelper.floor_double(explosion.explosionZ + (double)explosion.explosionSize + 1.0D);
	        List var9 = world.getEntitiesWithinAABBExcludingEntity(explosion.exploder, AxisAlignedBB.getAABBPool().getAABB((double)var3, (double)var5, (double)var7, (double)var4, (double)var27, (double)var28));
	        Vec3 var29 = world.getWorldVec3Pool().getVecFromPool(explosion.explosionX, explosion.explosionY, explosion.explosionZ);

	        for (int var11 = 0; var11 < var9.size(); ++var11)
	        {
	            Entity var30 = (Entity)var9.get(var11);
	            double var13 = var30.getDistance(explosion.explosionX, explosion.explosionY, explosion.explosionZ) / (double)explosion.explosionSize;
	
	            if (var13 <= 1.0D)
	            {
	                var15 = var30.posX - explosion.explosionX;
	                var17 = var30.posY + (double)var30.getEyeHeight() - explosion.explosionY;
	                var19 = var30.posZ - explosion.explosionZ;
	                double var32 = (double)MathHelper.sqrt_double(var15 * var15 + var17 * var17 + var19 * var19);
	
	                if (var32 != 0.0D)
	                {
	                    var15 /= var32;
	                    var17 /= var32;
	                    var19 /= var32;
	                    double var31 = (double)world.getBlockDensity(var29, var30.boundingBox);
	                    double var33 = (1.0D - var13) * var31;
	                    
	                    if ( var30 instanceof EntityFallingSand )
	                    {
	                    	var33 *= (double)MathHelper.sqrt_double( 1500D / (double)BlockPhysics.blockSet[((EntityFallingSand) var30).blockID][((EntityFallingSand) var30).metadata].mass);
	                    	if (((EntityFallingSand)var30).blockID == Block.tnt.blockID && !(var30 instanceof EntityTNTPrimed))
		                    {
		                    	EntityTNTPrimed entitytnt = new EntityTNTPrimed(world, var30.posX, var30.posY, var30.posZ, null);
	    		                entitytnt.motionX = bSpeedR(var30.motionX + var15 * var33);
	    		                entitytnt.motionY = bSpeedR(var30.motionY + var17 * var33);
	    		                entitytnt.motionZ = bSpeedR(var30.motionZ + var19 * var33);
	    		                entitytnt.fuse = 20 + prandnextint(40);
	    		                world.spawnEntityInWorld(entitytnt);
	    		                var30.setDead();
		                    }
	                    	else
	                    	{
	                    		var30.motionX = bSpeedR(var30.motionX + var15 * var33);
			                    var30.motionY = bSpeedR(var30.motionY + var17 * var33);
			                    var30.motionZ = bSpeedR(var30.motionZ + var19 * var33);
			                    var30.velocityChanged = true;
	                    	}
	                    }
	                    else
	                    {
		                    var30.attackEntityFrom(DamageSource.setExplosionSource(explosion), (float)((int)((var33 * var33 + var33) / 2.0D * 8.0D * (double)explosion.explosionSize + 1.0D)));
		                    double var36 = EnchantmentProtection.func_92092_a(var30, var33);
							var30.motionX = bSpeedR(var30.motionX + var15 * var36);
		                    var30.motionY = bSpeedR(var30.motionY + var17 * var36);
		                    var30.motionZ = bSpeedR(var30.motionZ + var19 * var36);
		                    var30.velocityChanged = true;

		                    if (var30 instanceof EntityPlayer)
		                    {
		                        explosion.func_77277_b().put((EntityPlayer)var30, world.getWorldVec3Pool().getVecFromPool(var15 * var33, var17 * var33, var19 * var33));
							}
	                    }
	                }
	            }
	        }
	
	        explosion.explosionSize = var1 * (float) explblstr / 100F;
    	}
        
        
        for (var3 = 0; var3 < 16; ++var3)
        {
            for (var4 = 0; var4 < 16; ++var4)
            {
                for (var5 = 0; var5 < 16; ++var5)
                {
                    if (var3 == 0 || var3 == 16 - 1 || var4 == 0 || var4 == 16 - 1 || var5 == 0 || var5 == 16 - 1)
                    {
                        double var6 = (double)((float)var3 / ((float)16 - 1.0F) * 2.0F - 1.0F);
                        double var8 = (double)((float)var4 / ((float)16 - 1.0F) * 2.0F - 1.0F);
                        double var10 = (double)((float)var5 / ((float)16 - 1.0F) * 2.0F - 1.0F);
                        double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                        var6 /= var12;
                        var8 /= var12;
                        var10 /= var12;
                        float var14 = explosion.explosionSize * (0.7F + world.rand.nextFloat() * 0.6F);
                        var15 = explosion.explosionX;
                        var17 = explosion.explosionY;
                        var19 = explosion.explosionZ;

                        for (float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F)
                        {
                            int var22 = MathHelper.floor_double(var15);
                            int var23 = MathHelper.floor_double(var17);
                            int var24 = MathHelper.floor_double(var19);
                            int var25 = world.getBlockId(var22, var23, var24);
                            
                            if (var25 > 0 )
                            {
                            	int m = world.getBlockMetadata(var22, var23, var24);
                            	Block var26 = Block.blocksList[var25];
                                float var27 = explosion.exploder != null ? explosion.exploder.func_82146_a(explosion, world, var24, var22, var23, var26) : var26.getExplosionResistance(explosion.exploder);
                                var14 -= (var27 + 0.3F) * var21;
                            	
	                            if (var14 > 0.0F)
	                            {
	                            	if ( !skipMove && ((WorldServer)world).getEntityTracker().movingblocks < maxmovingblocks && ( blockSet[var25][m].pushtype == 1 || blockSet[var25][m].pushtype == 3 ) )
	            	            	{	            	            		
	                            		/*double speed = (double)MathHelper.sqrt_double( exploder.motionX * exploder.motionX + exploder.motionY * exploder.motionY + exploder.motionZ * exploder.motionZ );
	                            		double d6 = var22 + 0.5F - explosionX - (exploder.motionX / speed) * 4;
	            	                    double d8 = var23 + 0.5F - explosionY - (exploder.motionY / speed) * 4;
	            	                    double d10 = var24 + 0.5F - explosionZ - (exploder.motionZ / speed) * 4;*/
	                            		double d6 = var22 + 0.5F - explosion.explosionX;
	            	                    double d8 = var23 + 0.5F - explosion.explosionY;
	            	                    double d10 = var24 + 0.5F - explosion.explosionZ;
	            	                    double d11 = (double)MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
	            	                    d6 /= d11;
	            	                    d8 /= d11;
	            	                    d10 /= d11;
	            	                    
	            	                    if (var25 == 1) var25 = 4;
	            	                    else if ( var25 == 2 || var25 == 60 || var25 == 110 ) var25 = 3;
	            	                    
	            	                    double sm = (double)MathHelper.sqrt_double( 1500D / (double)BlockPhysics.blockSet[var25][m].mass);
	            	                    double d7 = (0.5D * sm) / (d11 / (double)explosion.explosionSize + 0.10000000000000001D);

	            	                    d7 *= rand.nextFloat() * rand.nextFloat() + 0.6F;
	            	                    d6 *= d7;
	            	                    d8 *= d7;
	            	                    d10 *= d7;
	            	                    
	            	                    
	            	                    
	            	                    int bpdata = 0;
	            	                    if ( blockSet[var25][m].movenum == 2 && blockSet[var25][m].movechanger > 1 ) bpdata = 15;
	            		                else bpdata = getBlockBPdata( world,var22, var23, var24);
	            		                	
	            		                int meta = world.getBlockMetadata(var22, var23, var24);
										
	            	                    if ( !explosion.impact && var25 == Block.tnt.blockID )
	            	                    {
	            	                    	EntityTNTPrimed entitytnt = new EntityTNTPrimed(world, (float)var22 + 0.5F, (float)var23 + 0.5F, (float)var24 + 0.5F, null);
		            		                entitytnt.motionX = bSpeedR(d6 - rand.nextGaussian() * 0.05D);
		            		                entitytnt.motionY = bSpeedR(d8 - rand.nextGaussian() * 0.05D);
		            		                entitytnt.motionZ = bSpeedR(d10 - rand.nextGaussian() * 0.05D);
		            		                entitytnt.fuse = 20 + prandnextint(40);
		            		                world.spawnEntityInWorld(entitytnt);
	            	                    }
	            	                    else
	            	                    {
		            	                    EntityFallingSand entityfallingsand = new EntityFallingSand(world, (float)var22 + 0.5F, (float)var23 + 0.5F, (float)var24 + 0.5F, var25, meta);
		            		                entityfallingsand.motionX = bSpeedR(d6 - rand.nextGaussian() * 0.05D);
		            		                entityfallingsand.motionY = bSpeedR(d8 - rand.nextGaussian() * 0.05D);
		            		                entityfallingsand.motionZ = bSpeedR(d10 - rand.nextGaussian() * 0.05D);
		            		                if ( explosionfire && ( !explosion.impact || explosion.isFlaming ) && prandnextint(5) == 0 && canBurn(var25) ) entityfallingsand.setFire(60);
		            		                entityfallingsand.bpdata = bpdata;
		            		                if ( Block.blocksList[var25].hasTileEntity(meta) ) 
		                	                {
		            		                	entityfallingsand.fallingBlockTileEntityData = new NBTTagCompound("TileEntityData");
		            		                	world.getBlockTileEntity(var22, var23, var24).writeToNBT(entityfallingsand.fallingBlockTileEntityData);
		                	                	world.removeBlockTileEntity(var22, var23, var24);
		                	                }
		            		                world.spawnEntityInWorld(entityfallingsand);
	            	                    }   
	            		                
	            	                    if ( explosionfire && ( !explosion.impact || explosion.isFlaming ) )
	            	        	        {
		            	            		int k2 = world.getBlockId(var22, var23 - 1, var24);
		            	            		if (Block.opaqueCubeLookup[k2] && prandnextint(5) == 0)
		            		                {
		            	            			 world.setBlock(var22, var23, var24, Block.fire.blockID ,0 ,3);
		            		                }
		            	            		else 
		            	            		{
		            	            			world.setBlock(var22, var23, var24, 0, 0, 3);
		            	            		}
	            	        	        }
		            	            	else 
	            	            		{
		            	            		world.setBlock(var22, var23, var24, 0, 0, 3);
	            	            		}
	            	                }
	            	            	else
	            	            	{
	            	                    Block block = Block.blocksList[var25];
	            	            		if (block.canDropFromExplosion(explosion)) block.dropBlockAsItemWithChance(world, var22, var23, var24, world.getBlockMetadata(var22, var23, var24), 1.0F / explosion.explosionSize, 0);
	            	            		block.onBlockExploded(world, var22, var23, var24, explosion);

	            	            		if ( explosionfire && ( !explosion.impact || explosion.isFlaming ) )
	            	        	        {
		            	            		int k2 = world.getBlockId(var22, var23 - 1, var24);
		            	            		if (Block.opaqueCubeLookup[k2] && prandnextint(5) == 0)
		            		                {
		            		                    world.setBlock(var22, var23, var24, Block.fire.blockID ,0 ,3);
		            		                }
		            	            		else 
		            	            		{
		            	                        world.setBlock(var22, var23, var24, 0, 0, 3);
		            	            		}
	            	        	        }
		            	            	else 
	            	            		{
		            	            		world.setBlock(var22, var23, var24, 0, 0, 3);
	            	            		}
	            	            	}
	                            	setBlockBPdata( world,var22, var23, var24, 0);
	                            	if ( !explosion.impact ) var2.add(new ChunkPosition(var22, var23, var24));
	                            }
                            }
                            var15 += var6 * (double)var21;
                            var17 += var8 * (double)var21;
                            var19 += var10 * (double)var21;
                        }
                    }
                }
            }
        }

        explosion.affectedBlockPositions.addAll(var2);
   	
        moveChangeMechanic(world, MathHelper.floor_double(explosion.explosionX), MathHelper.floor_double(explosion.explosionY), MathHelper.floor_double(explosion.explosionZ), 0, 2, 12);
    }
    
    public static void doExplosionB(World world, Explosion explosion, boolean par1)
    {
    	if (explosion.impact) return;
    	
    	world.playSoundEffect(explosion.explosionX, explosion.explosionY, explosion.explosionZ, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        if (explosion.explosionSize >= 2.0F && explosion.isSmoking)
        {
            world.spawnParticle("hugeexplosion", explosion.explosionX, explosion.explosionY, explosion.explosionZ, 1.0D, 0.0D, 0.0D);
        }
        else
        {
            world.spawnParticle("largeexplode", explosion.explosionX, explosion.explosionY, explosion.explosionZ, 1.0D, 0.0D, 0.0D);
        }
	
		
        ChunkPosition var3;
        int var4;
        int var5;
        int var6;
        int var7;
		
		if (explosion.isSmoking)
        {
            Iterator var2 = explosion.affectedBlockPositions.iterator();

			while (var2.hasNext())
			{
				var3 = (ChunkPosition)var2.next();
				var4 = var3.x;
				var5 = var3.y;
				var6 = var3.z;
				var7 = world.getBlockId(var4, var5, var6);

				if (par1)
				{
					double var8 = (double)((float)var4 + world.rand.nextFloat());
					double var10 = (double)((float)var5 + world.rand.nextFloat());
					double var12 = (double)((float)var6 + world.rand.nextFloat());
					double var14 = var8 - explosion.explosionX;
					double var16 = var10 - explosion.explosionY;
					double var18 = var12 - explosion.explosionZ;
					double var20 = (double)MathHelper.sqrt_double(var14 * var14 + var16 * var16 + var18 * var18);
					var14 /= var20;
					var16 /= var20;
					var18 /= var20;
					double var22 = 0.5D / (var20 / (double)explosion.explosionSize + 0.1D);
					var22 *= (double)(world.rand.nextFloat() * world.rand.nextFloat() + 0.3F);
					var14 *= var22;
					var16 *= var22;
					var18 *= var22;
					world.spawnParticle("explode", (var8 + explosion.explosionX * 1.0D) / 2.0D, (var10 + explosion.explosionY * 1.0D) / 2.0D, (var12 + explosion.explosionZ * 1.0D) / 2.0D, var14, var16, var18);
					world.spawnParticle("smoke", var8, var10, var12, var14, var16, var18);
				}
			}
		}
    }
    
    public static void tickBlocksRandomMove(WorldServer wserver)
    {
        if ( skipMove ) return;
        
        Iterator var3 = wserver.activeChunkSet.iterator();
        
        while (var3.hasNext())
        {
            ChunkCoordIntPair var4 = (ChunkCoordIntPair)var3.next();
            int var5 = var4.chunkXPos * 16;
            int var6 = var4.chunkZPos * 16;
            Chunk var7 = wserver.getChunkFromChunkCoords(var4.chunkXPos, var4.chunkZPos);

            int var9;
            int var10;
            int var13;
            
            ExtendedBlockStorage[] var19 = var7.getBlockStorageArray();
            var9 = var19.length;

            for (var10 = 0; var10 < var9; ++var10)
            {
                ExtendedBlockStorage var21 = var19[var10];

                if (var21 != null )
                {
                    for (int var20 = 0; var20 < 3; ++var20)
                    {
                    	updateLCG = updateLCG * 3 + 1013904223;
                        var13 = updateLCG >> 2;
                        int var14 = var13 & 15;
                        int var15 = var13 >> 8 & 15;
                        int var16 = var13 >> 16 & 15;
                        int var17 = var21.getExtBlockID(var14, var16, var15);
                        int m = var21.getExtBlockMetadata(var14, var16, var15);
                        
                        if (BlockPhysics.blockSet[var17][m].randomtick)
                        {
                        	BlockPhysics.tryToMove(wserver, var14 + var5, var16 + var21.getYLocation(), var15 + var6, var17, m, false);
                        }
                    }
                }
            }
        }
    }
    
    public static EntityFallingSand createFallingsand(World world, double var2, double var4, double var6, Packet23VehicleSpawn par1Packet23VehicleSpawn )
    {
    	EntityFallingSand var8 = new EntityFallingSand(world, var2, var4, var6, par1Packet23VehicleSpawn.throwerEntityId & 4095, par1Packet23VehicleSpawn.throwerEntityId >> 16);
        if (((par1Packet23VehicleSpawn.throwerEntityId >> 15) & 1) == 1) ((EntityFallingSand)var8).setFire(60);
        ((EntityFallingSand)var8).slideDir = (byte) ((par1Packet23VehicleSpawn.throwerEntityId >> 12) & 7);
        return var8;
    }
    
    public static Packet23VehicleSpawn spawnFallingSandPacket(EntityFallingSand ent)
    {
	    int burn = 0;
	    if (ent.isBurning()) burn = 32768; 
	    int slide = ((int)ent.slideDir ) << 12;
    	
	    return new Packet23VehicleSpawn(ent, 70, ent.blockID | ent.metadata << 16 | burn | slide);
    }
    

    public static int readFallingSandID(NBTTagCompound nbt)
    {
    	int bid;
        if (nbt.hasKey("TileID")) bid = nbt.getInteger("TileID");
    	else if (nbt.hasKey("BlockID")) bid = (int)nbt.getShort("BlockID");
		else bid = nbt.getByte("Tile") & 255;
    	if (bid < 1 || bid > 4095 ) bid = 3;
		return bid;
    }
    
    public static double bSpeedR(double speed)
    {
    	return (double)((int)(speed * 8000D)) / 8000D;
    }
    
    public static void moveEntity(World world, EntityFallingSand fsand, double par1, double par3, double par5)
    {
        if (fsand.noClip)
        {
            fsand.boundingBox.offset(par1, par3, par5);
            fsand.posX = (fsand.boundingBox.minX + fsand.boundingBox.maxX) / 2.0D;
            fsand.posY = fsand.boundingBox.minY + (double)fsand.yOffset - (double)fsand.ySize;
            fsand.posZ = (fsand.boundingBox.minZ + fsand.boundingBox.maxZ) / 2.0D;
        }
        else
        {
            fsand.ySize *= 0.4F;
            double d3 = fsand.posX;
            double d4 = fsand.posY;
            double d5 = fsand.posZ;

            double d6 = par1;
            double d7 = par3;
            double d8 = par5;
            AxisAlignedBB axisalignedbb = fsand.boundingBox.copy();

            List list = world.getCollidingBoundingBoxes(fsand, fsand.boundingBox.addCoord(par1, par3, par5));

            for (int i = 0; i < list.size(); ++i)
            {
                par3 = ((AxisAlignedBB)list.get(i)).calculateYOffset(fsand.boundingBox, par3);
            }

            fsand.boundingBox.offset(0.0D, par3, 0.0D);

            if (!fsand.field_70135_K && d7 != par3)
            {
                par5 = 0.0D;
                par3 = 0.0D;
                par1 = 0.0D;
            }

            boolean flag1 = fsand.onGround || d7 != par3 && d7 < 0.0D;
            int j;

            for (j = 0; j < list.size(); ++j)
            {
                par1 = ((AxisAlignedBB)list.get(j)).calculateXOffset(fsand.boundingBox, par1);
            }

            fsand.boundingBox.offset(par1, 0.0D, 0.0D);

            if (!fsand.field_70135_K && d6 != par1)
            {
                par5 = 0.0D;
                par3 = 0.0D;
                par1 = 0.0D;
            }

            for (j = 0; j < list.size(); ++j)
            {
                par5 = ((AxisAlignedBB)list.get(j)).calculateZOffset(fsand.boundingBox, par5);
            }

            fsand.boundingBox.offset(0.0D, 0.0D, par5);

            if (!fsand.field_70135_K && d8 != par5)
            {
                par5 = 0.0D;
                par3 = 0.0D;
                par1 = 0.0D;
            }

            double d10;
            double d11;
            int k;
            double d12;

            if (fsand.stepHeight > 0.0F && flag1 && fsand.ySize < 0.05F && (d6 != par1 || d8 != par5))
            {
                d12 = par1;
                d10 = par3;
                d11 = par5;
                par1 = d6;
                par3 = (double)fsand.stepHeight;
                par5 = d8;
                AxisAlignedBB axisalignedbb1 = fsand.boundingBox.copy();
                fsand.boundingBox.setBB(axisalignedbb);
                list = world.getCollidingBoundingBoxes(fsand, fsand.boundingBox.addCoord(d6, par3, d8));

                for (k = 0; k < list.size(); ++k)
                {
                    par3 = ((AxisAlignedBB)list.get(k)).calculateYOffset(fsand.boundingBox, par3);
                }

                fsand.boundingBox.offset(0.0D, par3, 0.0D);

                if (!fsand.field_70135_K && d7 != par3)
                {
                    par5 = 0.0D;
                    par3 = 0.0D;
                    par1 = 0.0D;
                }

                for (k = 0; k < list.size(); ++k)
                {
                    par1 = ((AxisAlignedBB)list.get(k)).calculateXOffset(fsand.boundingBox, par1);
                }

                fsand.boundingBox.offset(par1, 0.0D, 0.0D);

                if (!fsand.field_70135_K && d6 != par1)
                {
                    par5 = 0.0D;
                    par3 = 0.0D;
                    par1 = 0.0D;
                }

                for (k = 0; k < list.size(); ++k)
                {
                    par5 = ((AxisAlignedBB)list.get(k)).calculateZOffset(fsand.boundingBox, par5);
                }

                fsand.boundingBox.offset(0.0D, 0.0D, par5);

                if (!fsand.field_70135_K && d8 != par5)
                {
                    par5 = 0.0D;
                    par3 = 0.0D;
                    par1 = 0.0D;
                }

                if (!fsand.field_70135_K && d7 != par3)
                {
                    par5 = 0.0D;
                    par3 = 0.0D;
                    par1 = 0.0D;
                }
                else
                {
                    par3 = (double)(-fsand.stepHeight);

                    for (k = 0; k < list.size(); ++k)
                    {
                        par3 = ((AxisAlignedBB)list.get(k)).calculateYOffset(fsand.boundingBox, par3);
                    }

                    fsand.boundingBox.offset(0.0D, par3, 0.0D);
                }

                if (d12 * d12 + d11 * d11 >= par1 * par1 + par5 * par5)
                {
                    par1 = d12;
                    par3 = d10;
                    par5 = d11;
                    fsand.boundingBox.setBB(axisalignedbb1);
                }
            }

            fsand.posX = (fsand.boundingBox.minX + fsand.boundingBox.maxX) / 2.0D;
            fsand.posY = fsand.boundingBox.minY + (double)fsand.yOffset - (double)fsand.ySize;
            fsand.posZ = (fsand.boundingBox.minZ + fsand.boundingBox.maxZ) / 2.0D;
            fsand.isCollidedHorizontally = d6 != par1 || d8 != par5;
            fsand.isCollidedVertically = d7 != par3;
            fsand.onGround = d7 != par3 && d7 < 0.0D;
            fsand.isCollided = fsand.isCollidedHorizontally || fsand.isCollidedVertically;
            //fsand.updateFallState(par3, fsand.onGround);

            if (d6 != par1)
            {
                fsand.motionX = 0.0D;
            }

            if (d7 != par3)
            {
                fsand.motionY = 0.0D;
            }

            if (d8 != par5)
            {
                fsand.motionZ = 0.0D;
            }

            d12 = fsand.posX - d3;
            d10 = fsand.posY - d4;
            d11 = fsand.posZ - d5;
        }
    }

	public static void dropItemsNBT(World world, int i, int j, int k, NBTTagCompound tileEntityData)
	{
		if ( tileEntityData == null ) return;
		
		NBTTagList nbttaglist = tileEntityData.getTagList("Items");
		
		for (int tl = 0; tl < nbttaglist.tagCount(); ++tl)
        {
            
            ItemStack itemstack = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbttaglist.tagAt(tl));
		
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
	                entityitem = new EntityItem(world, (double)((float)i + f), (double)((float)j + f1), (double)((float)k + f2), new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));
	                
	                if (itemstack.hasTagCompound())
	                {
	                    entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
	                }
	            }
	        }
        }
	}
	
	public static void dropFallingSand( EntityFallingSand fsand )
	{
		if ( fsand.fallingBlockTileEntityData != null ) dropItemsNBT(fsand.worldObj, MathHelper.floor_double(fsand.posX), MathHelper.floor_double(fsand.posY), MathHelper.floor_double(fsand.posZ), fsand.fallingBlockTileEntityData );
		fsand.entityDropItem(new ItemStack(fsand.blockID, 1, Block.blocksList[fsand.blockID].damageDropped(fsand.metadata)), 0.0F);
	}
	
	public static int getBlockBPdata(World world, int par1, int par2, int par3)
    {
        if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000)
        {
            if (par2 < 0)
            {
                return 0;
            }
            else if (par2 >= 256)
            {
                return 0;
            }
            else
            {
                Chunk chunk = world.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
                par1 &= 15;
                par3 &= 15;
                return chunk.getBlockBPdata(par1, par2, par3);
            }
        }
        else
        {
            return 0;
        }
    }
	
	public static boolean setBlockBPdata( World world, int par1, int par2, int par3, int par4 )
    {
        if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000)
        {
            if (par2 < 0)
            {
                return false;
            }
            else if (par2 >= 256)
            {
                return false;
            }
            else
            {
                Chunk chunk = world.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
                int j1 = par1 & 15;
                int k1 = par3 & 15;
                return chunk.setBlockBPdata(j1, par2, k1, par4);
            }
        }
        else
        {
            return false;
        }
    }

	public static boolean sameBlock(int id1, int meta1, int id2, int meta2)
	{
		if (id1 != id2) return false;
		if (id1 > 173 || id1 == 43 || id1 == 44 || id1 == 97)
		{
			if (meta1 != meta2) return false;
		}
		return true;
	}

}
