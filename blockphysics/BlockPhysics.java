package blockphysics;

import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockFire;
import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.AABBPool;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraft.network.packet.Packet23VehicleSpawn;
import net.minecraft.network.packet.Packet28EntityVelocity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.entity.EntityTracker;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetServerHandler;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventBus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(modid="BlockPhysics", name="BlockPhysics", version="0.8.0")
@NetworkMod(clientSideRequired=true, serverSideRequired=true, channels={"BlockPhysics0000", "BlockPhysics0001", "BlockPhysics0002"}, packetHandler=BPacketHandler.class, connectionHandler=BPacketHandler.class)
public class BlockPhysics
{
  public static final int version = 1000800;
  public static final String siteurl = "http://blockphysicssite.no-ip.org";
  public static final String updatecheckurl = "http://blockphysupdate.no-ip.org/lastversion162.html";
  public static Logger bpLogger;
  public static boolean warnerr = false;
  @Mod.Instance("BlockPhysics")
  public static BlockPhysics instance;
  public static File gameDir = new File(".");
  public static File bpjarFile = null;
  protected static Random rand = new Random();
  protected static int updateLCG = new Random().nextInt();
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
  static final byte[] prand = { 71, 65, 10, 40, 75, 97, 98, 39, 50, 47, 52, 65, 22, 32, 46, 86, 84, 22, 10, 41, 45, 15, 65, 67, 91, 28, 83, 49, 83, 13, 77, 89, 90, 38, 67, 69, 36, 30, 1, 41, 30, 79, 87, 95, 48, 14, 42, 8, 19, 22, 73, 84, 99, 7, 7, 72, 15, 63, 94, 34, 27, 31, 79, 85, 62, 68, 11, 86, 10, 83, 54, 4, 74, 78, 45, 26, 56, 7, 45, 25, 58, 90, 79, 68, 21, 62, 1, 89, 32, 5, 17, 65, 59, 34, 71, 87, 0, 39, 5, 32, 43, 64, 78, 27, 24, 2, 53, 37, 63, 57, 31, 51, 51, 49, 47, 42, 11, 31, 26, 41, 2, 10, 60, 1, 21, 45, 14, 18, 76, 75, 29, 32, 36, 60, 88, 51, 62, 55, 57, 3, 20, 80, 2, 0, 35, 42, 57, 34, 56, 89, 53, 42, 57, 55, 61, 11, 1, 26, 74, 35, 33, 17, 8, 16, 98, 5, 35, 76, 53, 58, 95, 55, 98, 70, 60, 24, 12, 39, 93, 43, 35, 66, 78, 87, 25, 20, 68, 33, 84, 6, 23, 13, 24, 20, 30, 0, 46, 49, 38, 76, 70, 49, 85, 31, 72, 18, 50, 88, 4, 18, 75, 96, 43, 28, 93, 38, 21, 71, 69, 19, 53, 91, 48, 29, 88, 89, 37, 59, 68, 93, 66, 44, 99, 40, 31, 27, 56, 46, 12, 92, 60, 30, 76, 26, 9, 99, 36, 77, 70, 9, 29, 12, 66, 3, 33, 43, 19, 3, 97, 81, 67, 72, 97, 32, 28, 96, 62, 71, 74, 61, 80, 93, 61, 57, 46, 18, 34, 79, 18, 48, 86, 94, 61, 6, 97, 17, 81, 68, 51, 29, 17, 92, 82, 62, 91, 39, 36, 64, 41, 85, 56, 66, 13, 59, 69, 37, 3, 76, 2, 28, 21, 36, 54, 49, 64, 87, 63, 23, 10, 78, 23, 8, 74, 54, 33, 86, 25, 44, 83, 6, 14, 3, 50, 38, 73, 5, 65, 55, 9, 15, 82, 82, 22, 99, 22, 2, 52, 81, 16, 27, 90, 75, 67, 60, 40, 52, 0, 29, 73, 26, 69, 5, 44, 50, 4, 0, 59, 82, 40, 17, 75, 12, 13, 99, 73, 72, 4, 25, 29, 55, 77, 80, 46, 74, 92, 44, 85, 88, 48, 84, 71, 90, 91, 6, 7, 78, 97, 20, 45, 11, 24, 4, 34, 59, 92, 80, 30, 40, 33, 7, 37, 43, 8, 13, 14, 54, 84, 12, 23, 86, 56, 9, 89, 73, 53, 8, 9, 93, 81, 85, 96, 28, 20, 14, 64, 80, 19, 51, 79, 16, 82, 19, 16, 38, 21, 63, 83, 98, 69, 77, 81, 77, 42, 35, 95, 58, 1, 94, 72, 15, 95, 48, 6, 44, 98, 91, 52, 67, 27, 96, 47, 88, 96, 15, 90, 25, 50, 61, 47, 66, 94, 16, 64, 87, 39, 58, 52, 47, 41, 58, 63, 70, 54, 37, 94, 23, 70, 11, 24, 95 };
  static final double[][][] slideSpeedz = { { { -0.125D, -0.005D, 0.0D }, { 0.0D, -0.005D, -0.125D }, { 0.0D, -0.005D, 0.125D }, { 0.125D, -0.005D, 0.0D } }, { { -0.25D, -0.06D, 0.0D }, { 0.0D, -0.06D, -0.25D }, { 0.0D, -0.06D, 0.25D }, { 0.25D, -0.06D, 0.0D } }, { { -0.3D, -0.25D, 0.0D }, { 0.0D, -0.25D, -0.3D }, { 0.0D, -0.25D, 0.3D }, { 0.3D, -0.25D, 0.0D } }, { { -0.18D, -0.35D, 0.0D }, { 0.0D, -0.35D, -0.18D }, { 0.0D, -0.35D, 0.18D }, { 0.18D, -0.35D, 0.0D } }, { { -0.05D, -0.15D, 0.0D }, { 0.0D, -0.15D, -0.05D }, { 0.0D, -0.15D, 0.05D }, { 0.05D, -0.15D, 0.0D } } };
  
  @Mod.EventHandler
  public void preI(FMLPreInitializationEvent event)
  {
    bpLogger = event.getModLog();
  }
  
  @Mod.EventHandler
  public void load(FMLInitializationEvent event)
  {
    warnerr = false;
    BlockPhysicsUtil.resetConfig();
    BlockPhysicsUtil.initConfig();
    BlockPhysicsUtil.checkForUpdate();
    writetoLog("mod loaded, version: " + BlockPhysicsUtil.versiontostring(1000800) + " ( " + "http://blockphysicssite.no-ip.org" + " ).", 1);
    MinecraftForge.EVENT_BUS.register(new WorldUnloadHook());
  }
  
  @Mod.EventHandler
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
    nextrand += 1;
    if (nextrand > 99) {
      nextrand -= 100;
    }
    return prand[nextrand] % max;
  }
  
  public static void setSkipMove(long tickTime)
  {
    if (tickTime > maxTickTime)
    {
      if (!skipMove)
      {
        writetoLog("Switching off physics ( " + tickTime + " ).", 2);
        skipMove = true;
      }
    }
    else if (skipMove == true)
    {
      writetoLog("Physics are working again ( " + tickTime + " ).", 2);
      skipMove = false;
      nextrand = rand.nextInt(100);
    }
  }
  
  public static long setSkipMoveM(long tickTime)
  {
    if (tickTime / 1000000L > maxTickTime)
    {
      if (!skipMove)
      {
        writetoLog("Switching off physics ( " + tickTime + " ).", 2);
        skipMove = true;
      }
    }
    else if (skipMove == true)
    {
      writetoLog("Physics are working again ( " + tickTime + " ).", 2);
      skipMove = false;
      nextrand = rand.nextInt(100);
    }
    return tickTime;
  }
  
  public static boolean canBurn(int blid)
  {
    if (Block.blockFlammability[blid] != 0) {
      return true;
    }
    if (Block.s[blid] == Block.bg) {
      return true;
    }
    return false;
  }
  
  public static boolean canSetMove(int blid)
  {
    if (blid == 0) {
      return false;
    }
    Material mt = Block.s[blid].cU;
    if ((!mt.a()) || (mt.d())) {
      return false;
    }
    return true;
  }
  
  public static boolean canMoveTo(World world, int i, int j, int k, int e)
  {
    int l = world.a(i, j, k);
    if (l == 0) {
      return true;
    }
    if ((l > 7) && (l < 12)) {
      return true;
    }
    if (l == 51) {
      return true;
    }
    Material mt = Block.s[l].cU;
    if (mt.d()) {
      return true;
    }
    int m = world.h(i, j, k);
    if ((blockSet[l][m].fragile > 0) && (e > blockSet[l][m].strength)) {
      return true;
    }
    return false;
  }
  
  public static boolean branch(World world, int i, int j, int k, int bid, int met)
  {
    if (sameBlock(world.a(i + 1, j - 1, k), world.h(i + 1, j - 1, k), bid, met)) {
      return true;
    }
    if (sameBlock(world.a(i - 1, j - 1, k), world.h(i - 1, j - 1, k), bid, met)) {
      return true;
    }
    if (sameBlock(world.a(i, j - 1, k + 1), world.h(i, j - 1, k + 1), bid, met)) {
      return true;
    }
    if (sameBlock(world.a(i, j - 1, k - 1), world.h(i, j - 1, k - 1), bid, met)) {
      return true;
    }
    if (sameBlock(world.a(i + 1, j - 1, k + 1), world.h(i + 1, j - 1, k + 1), bid, met)) {
      return true;
    }
    if (sameBlock(world.a(i - 1, j - 1, k - 1), world.h(i - 1, j - 1, k - 1), bid, met)) {
      return true;
    }
    if (sameBlock(world.a(i - 1, j - 1, k + 1), world.h(i - 1, j - 1, k + 1), bid, met)) {
      return true;
    }
    if (sameBlock(world.a(i + 1, j - 1, k - 1), world.h(i + 1, j - 1, k - 1), bid, met)) {
      return true;
    }
    return false;
  }
  
  public static boolean floating(World world, int i, int j, int k, int rad, int bid, int met)
  {
    for (int jj = j - rad; jj <= j + rad; jj++) {
      for (int ii = i - rad; ii <= i + rad; ii++) {
        for (int kk = k - rad; kk <= k + rad; kk++) {
          if (sameBlock(world.a(ii, jj, kk), world.h(ii, jj, kk), bid, met)) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public static boolean hanging(World world, int i, int j, int k, int hang, int bid, int met)
  {
    j++;
    hang = j + hang;
    for (int cc = j; cc < hang; cc++)
    {
      int b = world.a(i, cc, k);
      int m = world.h(i, cc, k);
      if (blockSet[b][m].supportingblock > 0) {
        return true;
      }
      if (!sameBlock(bid, met, b, m)) {
        return false;
      }
    }
    return false;
  }
  
  public static boolean attached(World world, int i, int j, int k, int att, int bid, int met)
  {
    for (int cc = 1; cc <= att; cc++)
    {
      int b = world.a(i + cc, j, k);
      int m = world.h(i + cc, j, k);
      if (blockSet[b][m].supportingblock > 0) {
        return true;
      }
      if (!sameBlock(bid, met, b, m)) {
        break;
      }
    }
    for (cc = 1; cc <= att; cc++)
    {
      int b = world.a(i - cc, j, k);
      int m = world.h(i - cc, j, k);
      if (blockSet[b][m].supportingblock > 0) {
        return true;
      }
      if (!sameBlock(bid, met, b, m)) {
        break;
      }
    }
    for (cc = 1; cc <= att; cc++)
    {
      int b = world.a(i, j, k + cc);
      int m = world.h(i, j, k + cc);
      if (blockSet[b][m].supportingblock > 0) {
        return true;
      }
      if (!sameBlock(bid, met, b, m)) {
        break;
      }
    }
    for (cc = 1; cc <= att; cc++)
    {
      int b = world.a(i, j, k - cc);
      int m = world.h(i, j, k - cc);
      if (blockSet[b][m].supportingblock > 0) {
        return true;
      }
      if (!sameBlock(bid, met, b, m)) {
        break;
      }
    }
    return false;
  }
  
  public static boolean ceiling(World world, int i, int j, int k)
  {
    if ((blockSet[world.a(i - 1, j, k)][world.h(i - 1, j, k)].supportingblock > 0) && (blockSet[world.a(i + 1, j, k)][world.h(i + 1, j, k)].supportingblock > 0)) {
      return true;
    }
    if ((blockSet[world.a(i, j, k - 1)][world.h(i, j, k - 1)].supportingblock > 0) && (blockSet[world.a(i, j, k + 1)][world.h(i, j, k + 1)].supportingblock > 0)) {
      return true;
    }
    if ((blockSet[world.a(i - 1, j, k - 1)][world.h(i - 1, j, k - 1)].supportingblock > 0) && (blockSet[world.a(i + 1, j, k + 1)][world.h(i + 1, j, k + 1)].supportingblock > 0)) {
      return true;
    }
    if ((blockSet[world.a(i - 1, j, k + 1)][world.h(i - 1, j, k + 1)].supportingblock > 0) && (blockSet[world.a(i + 1, j, k - 1)][world.h(i + 1, j, k - 1)].supportingblock > 0)) {
      return true;
    }
    return false;
  }
  
  public static boolean smallArc(World world, int i, int j, int k, int si)
  {
    if ((blockSet[world.a(i - 1, j, k)][world.h(i - 1, j, k)].supportingblock > 0) && (blockSet[world.a(i + 1, j, k)][world.h(i + 1, j, k)].supportingblock > 0))
    {
      if ((blockSet[world.a(i - 1, j - 1, k)][world.h(i - 1, j - 1, k)].supportingblock > 0) || (blockSet[world.a(i + 1, j - 1, k)][world.h(i + 1, j - 1, k)].supportingblock > 0)) {
        return true;
      }
      if (si > 1)
      {
        for (int c = 2; c <= si; c++)
        {
          if (blockSet[world.a(i - c, j, k)][world.h(i - c, j, k)].supportingblock <= 0) {
            break;
          }
          if (blockSet[world.a(i - c, j - 1, k)][world.h(i - c, j - 1, k)].supportingblock > 0) {
            return true;
          }
        }
        for (c = 2; c <= si; c++)
        {
          if (blockSet[world.a(i + c, j, k)][world.h(i + c, j, k)].supportingblock <= 0) {
            break;
          }
          if (blockSet[world.a(i + c, j - 1, k)][world.h(i + c, j - 1, k)].supportingblock > 0) {
            return true;
          }
        }
      }
    }
    if ((blockSet[world.a(i, j, k - 1)][world.h(i, j, k - 1)].supportingblock > 0) && (blockSet[world.a(i, j, k + 1)][world.h(i, j, k + 1)].supportingblock > 0))
    {
      if ((blockSet[world.a(i, j - 1, k - 1)][world.h(i, j - 1, k - 1)].supportingblock > 0) || (blockSet[world.a(i, j - 1, k + 1)][world.h(i, j - 1, k + 1)].supportingblock > 0)) {
        return true;
      }
      if (si > 1)
      {
        for (int c = 2; c <= si; c++)
        {
          if (blockSet[world.a(i, j, k - c)][world.h(i, j, k - c)].supportingblock <= 0) {
            break;
          }
          if (blockSet[world.a(i, j - 1, k - c)][world.h(i, j - 1, k - c)].supportingblock > 0) {
            return true;
          }
        }
        for (c = 2; c <= si; c++)
        {
          if (blockSet[world.a(i, j, k + c)][world.h(i, j, k + c)].supportingblock <= 0) {
            break;
          }
          if (blockSet[world.a(i, j - 1, k + c)][world.h(i, j - 1, k + c)].supportingblock > 0) {
            return true;
          }
        }
      }
    }
    if ((blockSet[world.a(i - 1, j, k + 1)][world.h(i - 1, j, k + 1)].supportingblock > 0) && (blockSet[world.a(i + 1, j, k - 1)][world.h(i + 1, j, k - 1)].supportingblock > 0))
    {
      if ((blockSet[world.a(i - 1, j - 1, k + 1)][world.h(i - 1, j - 1, k + 1)].supportingblock > 0) || (blockSet[world.a(i + 1, j - 1, k - 1)][world.h(i + 1, j - 1, k - 1)].supportingblock > 0)) {
        return true;
      }
      if (si > 1)
      {
        for (int c = 2; c <= si; c++)
        {
          if (blockSet[world.a(i - c, j, k + c)][world.h(i - c, j, k + c)].supportingblock <= 0) {
            break;
          }
          if (blockSet[world.a(i - c, j - 1, k + c)][world.h(i - c, j - 1, k + c)].supportingblock > 0) {
            return true;
          }
        }
        for (c = 2; c <= si; c++)
        {
          if (blockSet[world.a(i + c, j, k - c)][world.h(i + c, j, k - c)].supportingblock <= 0) {
            break;
          }
          if (blockSet[world.a(i + c, j - 1, k - c)][world.h(i + c, j - 1, k - c)].supportingblock > 0) {
            return true;
          }
        }
      }
    }
    if ((blockSet[world.a(i + 1, j, k + 1)][world.h(i + 1, j, k + 1)].supportingblock > 0) && (blockSet[world.a(i - 1, j, k - 1)][world.h(i - 1, j, k - 1)].supportingblock > 0))
    {
      if ((blockSet[world.a(i + 1, j - 1, k + 1)][world.h(i + 1, j - 1, k + 1)].supportingblock > 0) || (blockSet[world.a(i - 1, j - 1, k - 1)][world.h(i - 1, j - 1, k - 1)].supportingblock > 0)) {
        return true;
      }
      if (si > 1)
      {
        for (int c = 2; c <= si; c++)
        {
          if (blockSet[world.a(i + c, j, k + c)][world.h(i + c, j, k + c)].supportingblock <= 0) {
            break;
          }
          if (blockSet[world.a(i + c, j - 1, k + c)][world.h(i + c, j - 1, k + c)].supportingblock > 0) {
            return true;
          }
        }
        for (c = 2; c <= si; c++)
        {
          if (blockSet[world.a(i - c, j, k - c)][world.h(i - c, j, k - c)].supportingblock <= 0) {
            break;
          }
          if (blockSet[world.a(i - c, j - 1, k - c)][world.h(i - c, j - 1, k - c)].supportingblock > 0) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public static boolean bigArc(World world, int i, int j, int k, int bi)
  {
    if (blockSet[world.a(i, j + 1, k)][world.h(i, j + 1, k)].supportingblock == 0) {
      return false;
    }
    if (blockSet[world.a(i + 1, j + 1, k)][world.h(i + 1, j + 1, k)].supportingblock > 0) {
      for (int c = 1; c <= bi; c++)
      {
        if (blockSet[world.a(i - c, j, k)][world.h(i - c, j, k)].supportingblock <= 0) {
          break;
        }
        if (blockSet[world.a(i - c, j - 1, k)][world.h(i - c, j - 1, k)].supportingblock > 0) {
          return true;
        }
      }
    }
    if (blockSet[world.a(i - 1, j + 1, k)][world.h(i - 1, j + 1, k)].supportingblock > 0) {
      for (int c = 1; c <= bi; c++)
      {
        if (blockSet[world.a(i + c, j, k)][world.h(i + c, j, k)].supportingblock <= 0) {
          break;
        }
        if (blockSet[world.a(i + c, j - 1, k)][world.h(i + c, j - 1, k)].supportingblock > 0) {
          return true;
        }
      }
    }
    if (blockSet[world.a(i, j + 1, k + 1)][world.h(i, j + 1, k + 1)].supportingblock > 0) {
      for (int c = 1; c <= bi; c++)
      {
        if (blockSet[world.a(i, j, k - c)][world.h(i, j, k - c)].supportingblock <= 0) {
          break;
        }
        if (blockSet[world.a(i, j - 1, k - c)][world.h(i, j - 1, k - c)].supportingblock > 0) {
          return true;
        }
      }
    }
    if (blockSet[world.a(i, j + 1, k - 1)][world.h(i, j + 1, k - 1)].supportingblock > 0) {
      for (int c = 1; c <= bi; c++)
      {
        if (blockSet[world.a(i, j, k + c)][world.h(i, j, k + c)].supportingblock <= 0) {
          break;
        }
        if (blockSet[world.a(i, j - 1, k + c)][world.h(i, j - 1, k + c)].supportingblock > 0) {
          return true;
        }
      }
    }
    if (blockSet[world.a(i + 1, j + 1, k + 1)][world.h(i + 1, j + 1, k + 1)].supportingblock > 0) {
      for (int c = 1; c <= bi; c++)
      {
        if (blockSet[world.a(i - c, j, k - c)][world.h(i - c, j, k - c)].supportingblock <= 0) {
          break;
        }
        if (blockSet[world.a(i - c, j - 1, k - c)][world.h(i - c, j - 1, k - c)].supportingblock > 0) {
          return true;
        }
      }
    }
    if (blockSet[world.a(i - 1, j + 1, k - 1)][world.h(i - 1, j + 1, k - 1)].supportingblock > 0) {
      for (int c = 1; c <= bi; c++)
      {
        if (blockSet[world.a(i + c, j, k + c)][world.h(i + c, j, k + c)].supportingblock <= 0) {
          break;
        }
        if (blockSet[world.a(i + c, j - 1, k + c)][world.h(i + c, j - 1, k + c)].supportingblock > 0) {
          return true;
        }
      }
    }
    if (blockSet[world.a(i + 1, j + 1, k - 1)][world.h(i + 1, j + 1, k - 1)].supportingblock > 0) {
      for (int c = 1; c <= bi; c++)
      {
        if (blockSet[world.a(i - c, j, k + c)][world.h(i - c, j, k + c)].supportingblock <= 0) {
          break;
        }
        if (blockSet[world.a(i - c, j - 1, k + c)][world.h(i - c, j - 1, k + c)].supportingblock > 0) {
          return true;
        }
      }
    }
    if (blockSet[world.a(i - 1, j + 1, k + 1)][world.h(i - 1, j + 1, k + 1)].supportingblock > 0) {
      for (int c = 1; c <= bi; c++)
      {
        if (blockSet[world.a(i + c, j, k - c)][world.h(i + c, j, k - c)].supportingblock <= 0) {
          break;
        }
        if (blockSet[world.a(i + c, j - 1, k - c)][world.h(i + c, j - 1, k - c)].supportingblock > 0) {
          return true;
        }
      }
    }
    return false;
  }
  
  public static boolean corbel(World world, int i, int j, int k, int ci, int blid, int meta)
  {
    if (blockSet[blid][meta].supportingblock == 0) {
      return false;
    }
    for (int c = 1; c <= ci; c++)
    {
      if (!sameBlock(world.a(i + c, j, k), world.h(i + c, j, k), blid, meta)) {
        break;
      }
      if (sameBlock(world.a(i + c, j - 1, k), world.h(i + c, j - 1, k), blid, meta)) {
        return true;
      }
    }
    for (c = 1; c <= ci; c++)
    {
      if (!sameBlock(world.a(i - c, j, k), world.h(i - c, j, k), blid, meta)) {
        break;
      }
      if (sameBlock(world.a(i - c, j - 1, k), world.h(i - c, j - 1, k), blid, meta)) {
        return true;
      }
    }
    for (c = 1; c <= ci; c++)
    {
      if (!sameBlock(world.a(i, j, k + c), world.h(i, j, k + c), blid, meta)) {
        break;
      }
      if (sameBlock(world.a(i, j - 1, k + c), world.h(i, j - 1, k + c), blid, meta)) {
        return true;
      }
    }
    for (c = 1; c <= ci; c++)
    {
      if (!sameBlock(world.a(i, j, k - c), world.h(i, j, k - c), blid, meta)) {
        break;
      }
      if (sameBlock(world.a(i, j - 1, k - c), world.h(i, j - 1, k - c), blid, meta)) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean ncorbel(World world, int i, int j, int k, int ni)
  {
    for (int c = 1; c <= ni; c++)
    {
      if (blockSet[world.a(i - c, j, k)][world.h(i - c, j, k)].supportingblock <= 0) {
        break;
      }
      if (blockSet[world.a(i - c, j - 1, k)][world.h(i - c, j - 1, k)].supportingblock > 0) {
        return true;
      }
    }
    for (c = 1; c <= ni; c++)
    {
      if (blockSet[world.a(i + c, j, k)][world.h(i + c, j, k)].supportingblock <= 0) {
        break;
      }
      if (blockSet[world.a(i + c, j - 1, k)][world.h(i + c, j - 1, k)].supportingblock > 0) {
        return true;
      }
    }
    for (c = 1; c <= ni; c++)
    {
      if (blockSet[world.a(i, j, k + c)][world.h(i, j, k + c)].supportingblock <= 0) {
        break;
      }
      if (blockSet[world.a(i, j - 1, k + c)][world.h(i, j - 1, k + c)].supportingblock > 0) {
        return true;
      }
    }
    for (c = 1; c <= ni; c++)
    {
      if (blockSet[world.a(i, j, k - c)][world.h(i, j, k - c)].supportingblock <= 0) {
        break;
      }
      if (blockSet[world.a(i, j - 1, k - c)][world.h(i, j - 1, k - c)].supportingblock > 0) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean isFallingEmpty(World world, int i, int j, int k)
  {
    AxisAlignedBB Sandbbox = AxisAlignedBB.a(i, j, k, i + 1.0F, j + 1.0F, k + 1.0F);
    List ls = world.a(EntityFallingSand.class, Sandbbox);
    if (ls.size() != 0) {
      return false;
    }
    return true;
  }
  
  public static void notifyMove(World world, int i, int j, int k)
  {
    for (int i1 = i - 1; i1 <= i + 1; i1++) {
      for (int j1 = j - 1; j1 <= j + 1; j1++) {
        for (int k1 = k - 1; k1 <= k + 1; k1++) {
          world.moveTickList.scheduleBlockMoveUpdate(world, i1, j1, k1, world.a(i1, j1, k1), world.h(i1, j1, k1), false);
        }
      }
    }
  }
  
  public static boolean tryToMove(World world, int i, int j, int k, int blid, int meta, boolean contslide)
  {
    if (world.I) {
      return false;
    }
    if (blockSet[blid][meta].movenum == 0) {
      return false;
    }
    int players = world.h.size();
    if (players == 0) {
      return false;
    }
    if (((Block.s[blid] instanceof BlockPistonBase)) && (!canmove(world, i, j, k, (BlockPistonBase)Block.s[blid]))) {
      return false;
    }
    boolean outofRenderRange = true;
    boolean outofFallRange = true;
    for (int ii = 0; ii < players; ii++)
    {
      EntityPlayer entityplayer = (EntityPlayer)world.h.get(ii);
      if ((Math.abs(i - MathHelper.c(entityplayer.u)) <= fallRange) && (Math.abs(k - MathHelper.c(entityplayer.w)) <= fallRange))
      {
        outofFallRange = false;
        break;
      }
    }
    if (outofFallRange) {
      return false;
    }
    int move = 0;
    if (blockSet[blid][meta].movenum == 2) {
      if ((getBlockBPdata(world, i, j, k) & 0xF) >= blockSet[blid][meta].moveflipnumber) {
        move = 1;
      }
    }
    int movedefnum = blockSet[blid][meta].move[move];
    if (blockMoveDef[movedefnum].floating[0] > 0)
    {
      if (floating(world, i, j, k, blockMoveDef[movedefnum].floating[0], blockMoveDef[movedefnum].floating[1], blockMoveDef[movedefnum].floating[2])) {
        return false;
      }
      move = 1;
      movedefnum = blockSet[blid][meta].move[move];
      setBlockBPdata(world, i, j, k, 15);
    }
    if (blockMoveDef[movedefnum].movetype == 3)
    {
      if (canMoveTo(world, i, j - 1, k, blockSet[blid][meta].mass / 10))
      {
        int sv = blockMoveDef[movedefnum].hanging;
        if ((sv > 0) && (hanging(world, i, j, k, sv, blid, meta))) {
          return false;
        }
        sv = blockMoveDef[movedefnum].attached;
        if ((sv > 0) && (attached(world, i, j, k, sv, blid, meta))) {
          return false;
        }
        sv = blockMoveDef[movedefnum].ncorbel;
        if ((sv > 0) && (ncorbel(world, i, j, k, sv))) {
          return false;
        }
        sv = blockMoveDef[movedefnum].corbel;
        if ((sv > 0) && (corbel(world, i, j, k, sv, blid, meta))) {
          return false;
        }
        if ((blockMoveDef[movedefnum].ceiling) && (ceiling(world, i, j, k))) {
          return false;
        }
        sv = blockMoveDef[movedefnum].smallarc;
        if ((sv > 0) && (smallArc(world, i, j, k, sv))) {
          return false;
        }
        sv = blockMoveDef[movedefnum].bigarc;
        if ((sv > 0) && (bigArc(world, i, j, k, sv))) {
          return false;
        }
        if ((blockMoveDef[movedefnum].branch) && (branch(world, i, j, k, blid, meta))) {
          return false;
        }
        Block block = Block.s[blid];
        if (block.hasTileEntity(meta))
        {
          NBTTagCompound nnn = new NBTTagCompound("TileEntityData");
          world.r(i, j, k).b(nnn);
          dropItemsNBT(world, i, j, k, nnn);
          world.s(i, j, k);
        }
        block.c(world, i, j, k, meta, 0);
        
        world.f(i, j, k, 0, 0, 3);
        
        world.a(i + 0.5F, j + 0.5F, k + 0.5F, block.cS.a(), (block.cS.c() + 1.0F) / 2.0F, block.cS.d() * 0.8F);
        
        return true;
      }
      return false;
    }
    for (int iii = ii; iii < players; iii++)
    {
      EntityPlayer entityplayer = (EntityPlayer)world.h.get(iii);
      if ((Math.abs(i - MathHelper.c(entityplayer.u)) <= fallRenderRange) && (Math.abs(k - MathHelper.c(entityplayer.w)) <= fallRenderRange)) {
        if (MathHelper.c(entityplayer.v) - j <= fallRenderRange)
        {
          outofRenderRange = false;
          break;
        }
      }
    }
    if ((!outofRenderRange) && (((WorldServer)world).q().movingblocks >= maxmovingblocks)) {
      return false;
    }
    int movetype;
    int movetype;
    if (contslide) {
      movetype = 2;
    } else {
      movetype = blockMoveDef[movedefnum].movetype;
    }
    if (movetype == 0) {
      return false;
    }
    int ms = blockSet[blid][meta].mass / 10;
    boolean canfall = canMoveTo(world, i, j - 1, k, ms);
    if (!canfall)
    {
      if (movetype == 1) {
        return false;
      }
      if ((blockMoveDef[movedefnum].slidechance != 100) && ((blockMoveDef[movedefnum].slidechance == 0) || (blockMoveDef[movedefnum].slidechance < prandnextint(100) + 1))) {
        return false;
      }
    }
    if (!contslide)
    {
      int sv = blockMoveDef[movedefnum].hanging;
      if ((sv > 0) && (hanging(world, i, j, k, sv, blid, meta))) {
        return false;
      }
      sv = blockMoveDef[movedefnum].attached;
      if ((sv > 0) && (attached(world, i, j, k, sv, blid, meta))) {
        return false;
      }
      sv = blockMoveDef[movedefnum].ncorbel;
      if ((sv > 0) && (ncorbel(world, i, j, k, sv))) {
        return false;
      }
      sv = blockMoveDef[movedefnum].corbel;
      if ((sv > 0) && (corbel(world, i, j, k, sv, blid, meta))) {
        return false;
      }
      if ((blockMoveDef[movedefnum].ceiling) && (ceiling(world, i, j, k))) {
        return false;
      }
      sv = blockMoveDef[movedefnum].smallarc;
      if ((sv > 0) && (smallArc(world, i, j, k, sv))) {
        return false;
      }
      sv = blockMoveDef[movedefnum].bigarc;
      if ((sv > 0) && (bigArc(world, i, j, k, sv))) {
        return false;
      }
      if ((blockMoveDef[movedefnum].branch) && (branch(world, i, j, k, blid, meta))) {
        return false;
      }
    }
    boolean[] canslide = new boolean[4];
    if ((movetype == 2) && (!canfall))
    {
      canslide[0] = canMoveTo(world, i - 1, j - 1, k, ms);
      canslide[1] = canMoveTo(world, i, j - 1, k - 1, ms);
      canslide[2] = canMoveTo(world, i, j - 1, k + 1, ms);
      canslide[3] = canMoveTo(world, i + 1, j - 1, k, ms);
      if ((canslide[0] == 0) && (canslide[1] == 0) && (canslide[2] == 0) && (canslide[3] == 0)) {
        return false;
      }
      if (canslide[0] != 0) {
        canslide[0] = canMoveTo(world, i - 1, j, k, ms);
      }
      if (canslide[1] != 0) {
        canslide[1] = canMoveTo(world, i, j, k - 1, ms);
      }
      if (canslide[2] != 0) {
        canslide[2] = canMoveTo(world, i, j, k + 1, ms);
      }
      if (canslide[3] != 0) {
        canslide[3] = canMoveTo(world, i + 1, j, k, ms);
      }
      if ((canslide[0] == 0) && (canslide[1] == 0) && (canslide[2] == 0) && (canslide[3] == 0)) {
        return false;
      }
    }
    if ((blid == 2) || (blid == 60) || (blid == 110)) {
      blid = 3;
    }
    if (outofRenderRange)
    {
      int bpdata = getBlockBPdata(world, i, j, k);
      world.f(i, j, k, 0, 0, 3);
      setBlockBPdata(world, i, j, k, 0);
      notifyMove(world, i, j, k);
      int jv = j;
      if (canfall)
      {
        while ((canMoveTo(world, i, jv - 1, k, ms)) && (jv > 0)) {
          jv--;
        }
        if (jv > 0)
        {
          world.f(i, jv, k, blid, meta, 3);
          setBlockBPdata(world, i, jv, k, bpdata);
          notifyMove(world, i, jv, k);
        }
      }
      else
      {
        byte[] slide = { 0, 0, 0, 0 };
        byte count = 0;
        for (byte si = 0; si < 4; si = (byte)(si + 1)) {
          if (canslide[si] != 0)
          {
            slide[count] = si;
            count = (byte)(count + 1);
          }
        }
        int id = 0;
        int kd = 0;
        int rr = 0;
        if (count > 1) {
          rr = prandnextint(count);
        }
        switch (slide[rr])
        {
        case 0: 
          id = -1;
          break;
        case 1: 
          kd = -1;
          break;
        case 2: 
          kd = 1;
          break;
        case 3: 
          id = 1;
        }
        int iv = i + id;int kv = k + kd;
        while ((canMoveTo(world, iv, jv - 1, kv, ms)) && (jv > 0)) {
          jv--;
        }
        if (jv > 0)
        {
          world.f(iv, jv, kv, blid, meta, 3);
          setBlockBPdata(world, iv, jv, kv, bpdata);
          notifyMove(world, iv, jv, kv);
        }
      }
      j++;
      tryToMove(world, i, j, k, world.a(i, j, k), world.h(i, j, k), false);
      return true;
    }
    if (canfall)
    {
      EntityFallingSand entityfallingsand = new EntityFallingSand(world, 0.5D + i, 0.5D + j, 0.5D + k, blid, meta);
      if (Block.s[blid].hasTileEntity(meta))
      {
        entityfallingsand.e = new NBTTagCompound("TileEntityData");
        world.r(i, j, k).b(entityfallingsand.e);
        world.s(i, j, k);
      }
      if ((canBurn(blid)) && (world.a(i, j + 1, k) == 51)) {
        entityfallingsand.d(60);
      }
      entityfallingsand.bpdata = getBlockBPdata(world, i, j, k);
      world.d(entityfallingsand);
    }
    else
    {
      if (canslide[0] != 0) {
        canslide[0] = isFallingEmpty(world, i - 1, j, k);
      }
      if (canslide[1] != 0) {
        canslide[1] = isFallingEmpty(world, i, j, k - 1);
      }
      if (canslide[2] != 0) {
        canslide[2] = isFallingEmpty(world, i, j, k + 1);
      }
      if (canslide[3] != 0) {
        canslide[3] = isFallingEmpty(world, i + 1, j, k);
      }
      if ((canslide[0] == 0) && (canslide[1] == 0) && (canslide[2] == 0) && (canslide[3] == 0)) {
        return false;
      }
      byte[] slide = { 0, 0, 0, 0 };
      byte count = 0;
      for (byte si = 0; si < 4; si = (byte)(si + 1)) {
        if (canslide[si] != 0)
        {
          slide[count] = si;
          count = (byte)(count + 1);
        }
      }
      int id = 0;
      int kd = 0;
      int rr = 0;
      if (count > 1) {
        rr = prandnextint(count);
      }
      switch (slide[rr])
      {
      case 0: 
        id = -1;
        break;
      case 1: 
        kd = -1;
        break;
      case 2: 
        kd = 1;
        break;
      case 3: 
        id = 1;
      }
      EntityFallingSand entityfallingsand = new EntityFallingSand(world, 0.5D + i + 0.0625D * id, 0.5D + j - 0.0625D, 0.5D + k + 0.0625D * kd, blid, meta);
      if (Block.s[blid].hasTileEntity(meta))
      {
        entityfallingsand.e = new NBTTagCompound("TileEntityData");
        world.r(i, j, k).b(entityfallingsand.e);
        world.s(i, j, k);
      }
      entityfallingsand.slideDir = ((byte)(slide[rr] + 1));
      if ((canBurn(blid)) && (world.a(i, j + 1, k) == 51)) {
        entityfallingsand.d(60);
      }
      entityfallingsand.bpdata = getBlockBPdata(world, i, j, k);
      world.d(entityfallingsand);
    }
    world.f(i, j, k, 0, 0, 3);
    setBlockBPdata(world, i, j, k, 0);
    j++;
    tryToMove(world, i, j, k, world.a(i, j, k), world.h(i, j, k), false);
    return true;
  }
  
  public static void moveChangeMechanic(World world, int i, int j, int k, int blockID, int radius, int strength)
  {
    for (int ii = i - radius; ii <= i + radius; ii++) {
      for (int jj = j - radius; jj <= j + radius; jj++) {
        for (int kk = k - radius; kk <= k + radius; kk++)
        {
          int bid = world.a(ii, jj, kk);
          int m = world.h(ii, jj, kk);
          if ((blockSet[bid][m].movenum == 2) && (blockSet[bid][m].movechanger > 1))
          {
            int bpd = getBlockBPdata(world, ii, jj, kk);
            int state = bpd & 0xF;
            bpd -= state;
            if (state < 15)
            {
              state = state + prandnextint(4) + strength;
              if (state > 15) {
                state = 15;
              }
              setBlockBPdata(world, ii, jj, kk, bpd + state);
            }
          }
        }
      }
    }
  }
  
  public static void updatePistonState(World par1World, int par2, int par3, int par4, BlockPistonBase par1block, boolean isSticky)
  {
    if (par1World.I) {
      return;
    }
    int var5 = par1World.h(par2, par3, par4);
    int var6 = BlockPistonBase.d(var5);
    if (var6 > 5) {
      var6 = 0;
    }
    int vv = 0;
    if (par1block.d(par1World, par2, par3, par4, var6)) {
      vv = 8;
    }
    if (vv == (var5 & 0x8)) {
      return;
    }
    int i2 = par2 + s.b[var6];
    int j2 = par3 + s.c[var6];
    int k2 = par4 + s.d[var6];
    if ((par1World.a(i2, j2, k2) == Block.ah.cF) || (par1World.pistonMoveBlocks.contains("" + par2 + "." + par3 + "." + par4))) {
      return;
    }
    if (vv == 8)
    {
      int ext = 0;
      if ((catapult) && (!skipMove) && (((WorldServer)par1World).q().movingblocks < maxmovingblocks))
      {
        boolean catapultpowered = false;
        boolean catapultprecise = true;
        int[] power = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        int xpw = par2 - s.b[var6];
        int ypw = par3 - s.c[var6];
        int zpw = par4 - s.d[var6];
        if (Block.s[par1World.a(xpw, ypw, zpw)] == Block.U)
        {
          TileEntityDispenser tileentitydispenser = (TileEntityDispenser)par1World.r(xpw, ypw, zpw);
          if (tileentitydispenser != null) {
            for (int pp = 0; pp < 9; pp++)
            {
              ItemStack powstack = tileentitydispenser.a(pp);
              if ((catapultprecise) && ((pp == 0) || (pp == 2) || (pp == 6) || (pp == 8)) && ((powstack == null) || (powstack.b() != Item.r) || (powstack.b < 4))) {
                catapultprecise = false;
              }
              if ((powstack != null) && (powstack.b() == Item.aE))
              {
                catapultpowered = true;
                power[pp] = powstack.b;
              }
            }
          }
        }
        if (catapultpowered)
        {
          ext = canExtend(par1World, par2, par3, par4, var6, par1block, true);
          if (ext == 0) {
            return;
          }
          double pspX = 0.0D;double pspY = 0.0D;double pspZ = 0.0D;
          switch (var6)
          {
          case 0: 
            pspX = -power[0] / 3.0D + power[2] / 3.0D - power[6] / 3.0D + power[8] / 3.0D - power[3] / 2.0D + power[5] / 2.0D;
            pspY = -(power[4] + power[0] / 3.0D + power[6] / 3.0D + power[2] / 3.0D + power[8] / 3.0D + power[1] / 2.0D + power[7] / 2.0D + power[3] / 2.0D + power[5] / 2.0D);
            pspZ = -power[0] / 3.0D - power[2] / 3.0D + power[6] / 3.0D + power[8] / 3.0D - power[1] / 2.0D + power[7] / 2.0D;
            break;
          case 1: 
            pspX = -power[0] / 3.0D + power[2] / 3.0D - power[6] / 3.0D + power[8] / 3.0D - power[3] / 2.0D + power[5] / 2.0D;
            pspY = power[4] + power[0] / 3.0D + power[6] / 3.0D + power[2] / 3.0D + power[8] / 3.0D + power[1] / 2.0D + power[7] / 2.0D + power[3] / 2.0D + power[5] / 2.0D;
            pspZ = -power[0] / 3.0D - power[2] / 3.0D + power[6] / 3.0D + power[8] / 3.0D - power[1] / 2.0D + power[7] / 2.0D;
            break;
          case 2: 
            pspX = power[0] / 3.0D - power[2] / 3.0D + power[6] / 3.0D - power[8] / 3.0D + power[3] / 2.0D - power[5] / 2.0D;
            pspY = power[0] / 3.0D - power[6] / 3.0D + power[2] / 3.0D - power[8] / 3.0D + power[1] / 2.0D - power[7] / 2.0D;
            pspZ = -(power[4] + power[0] / 3.0D + power[6] / 3.0D + power[2] / 3.0D + power[8] / 3.0D + power[1] / 2.0D + power[7] / 2.0D + power[3] / 2.0D + power[5] / 2.0D);
            break;
          case 3: 
            pspX = -(power[0] / 3.0D - power[2] / 3.0D + power[6] / 3.0D - power[8] / 3.0D + power[3] / 2.0D - power[5] / 2.0D);
            pspY = power[0] / 3.0D - power[6] / 3.0D + power[2] / 3.0D - power[8] / 3.0D + power[1] / 2.0D - power[7] / 2.0D;
            pspZ = power[4] + power[0] / 3.0D + power[6] / 3.0D + power[2] / 3.0D + power[8] / 3.0D + power[1] / 2.0D + power[7] / 2.0D + power[3] / 2.0D + power[5] / 2.0D;
            break;
          case 4: 
            pspX = -(power[4] + power[0] / 3.0D + power[6] / 3.0D + power[2] / 3.0D + power[8] / 3.0D + power[1] / 2.0D + power[7] / 2.0D + power[3] / 2.0D + power[5] / 2.0D);
            pspY = power[0] / 3.0D - power[6] / 3.0D + power[2] / 3.0D - power[8] / 3.0D + power[1] / 2.0D - power[7] / 2.0D;
            pspZ = -(power[0] / 3.0D - power[2] / 3.0D + power[6] / 3.0D - power[8] / 3.0D + power[3] / 2.0D - power[5] / 2.0D);
            break;
          case 5: 
            pspX = power[4] + power[0] / 3.0D + power[6] / 3.0D + power[2] / 3.0D + power[8] / 3.0D + power[1] / 2.0D + power[7] / 2.0D + power[3] / 2.0D + power[5] / 2.0D;
            pspY = power[0] / 3.0D - power[6] / 3.0D + power[2] / 3.0D - power[8] / 3.0D + power[1] / 2.0D - power[7] / 2.0D;
            pspZ = power[0] / 3.0D - power[2] / 3.0D + power[6] / 3.0D - power[8] / 3.0D + power[3] / 2.0D - power[5] / 2.0D;
          }
          pspX *= 40.0D;
          pspY *= 40.0D;
          pspZ *= 40.0D;
          
          double dirX = 1.0D;
          if (pspX < 0.0D) {
            dirX = -1.0D;
          }
          double dirY = 1.0D;
          if (pspY < 0.0D) {
            dirY = -1.0D;
          }
          double dirZ = 1.0D;
          if (pspZ < 0.0D) {
            dirZ = -1.0D;
          }
          double error = 1.0D;
          if (catapultprecise) {
            error = 0.0D;
          }
          int sticky = 0;
          if (isSticky) {
            sticky = 1;
          }
          int xx = par2 + ext * s.b[var6];
          int yy = par3 + ext * s.c[var6];
          int zz = par4 + ext * s.d[var6];
          
          int blid = par1World.a(xx, yy, zz);
          int meta = par1World.h(xx, yy, zz);
          if (blockSet[blid][meta].fragile > 0)
          {
            if (Block.s[blid].hasTileEntity(meta))
            {
              NBTTagCompound nnn = new NBTTagCompound("TileEntityData");
              par1World.r(xx, yy, zz).b(nnn);
              dropItemsNBT(par1World, xx, yy, zz, nnn);
              par1World.s(xx, yy, zz);
            }
            if (blockSet[blid][meta].fragile == 2) {
              Block.s[blid].c(par1World, xx, yy, zz, meta, 0);
            }
            par1World.f(xx, yy, zz, 0, 0, 3);
          }
          int sstick = 1;
          if (ext == 1) {
            sstick = 0;
          }
          int bxx = par2 + (ext + sticky * sstick) * s.b[var6];
          int byy = par3 + (ext + sticky * sstick) * s.c[var6];
          int bzz = par4 + (ext + sticky * sstick) * s.d[var6];
          
          double smass = 0.0D;
          for (int i = ext - 1; i > sticky; i--)
          {
            xx -= s.b[var6];
            yy -= s.c[var6];
            zz -= s.d[var6];
            int blid2 = par1World.a(xx, yy, zz);
            meta = par1World.h(xx, yy, zz);
            Block bb = Block.s[blid2];
            if ((bb == Block.z) || (bb == Block.aE) || (bb == Block.bD)) {
              blid2 = Block.A.cF;
            }
            smass += blockSet[blid2][meta].mass;
          }
          Iterator iterator;
          if (canMoveTo(par1World, bxx + s.b[var6], byy + s.c[var6], bzz + s.d[var6], 0))
          {
            AxisAlignedBB axisalignedbb = AxisAlignedBB.a(bxx, byy, bzz, bxx + 1, byy + 1, bzz + 1);
            List list = par1World.b(null, axisalignedbb);
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
              if (((Entity)iterator.next()).ac < 6) {
                iterator.remove();
              }
            }
            if (!list.isEmpty())
            {
              for (Iterator iterator = list.iterator(); iterator.hasNext();)
              {
                Entity entity = (Entity)iterator.next();
                if ((entity instanceof EntityFallingSand)) {
                  smass += blockSet[((EntityFallingSand)entity).a][((EntityFallingSand)entity).b].mass;
                } else if ((entity instanceof EntityLiving)) {
                  smass += ((EntityLiving)entity).aS() * 3.0D;
                } else {
                  smass += 25.0D;
                }
              }
              for (iterator = list.iterator(); iterator.hasNext();)
              {
                Entity entity = (Entity)iterator.next();
                
                double sx = MathHelper.a(Math.abs(pspX) / smass) * dirX + error * rand.nextGaussian() * 0.02D;
                double sy = MathHelper.a(Math.abs(pspY) / smass) * dirY + error * rand.nextGaussian() * 0.02D;
                double sz = MathHelper.a(Math.abs(pspZ) / smass) * dirZ + error * rand.nextGaussian() * 0.02D;
                entity.b(entity.u + s.b[var6], entity.v + s.c[var6], entity.w + s.d[var6]);
                entity.g(bSpeedR(sx), bSpeedR(sy), bSpeedR(sz));
                entity.J = true;
                if ((entity instanceof EntityPlayerMP)) {
                  ((EntityPlayerMP)entity).a.b(new Packet28EntityVelocity(entity.k, bSpeedR(sx), bSpeedR(sy), bSpeedR(sz)));
                }
              }
            }
          }
          xx = par2 + ext * s.b[var6];
          yy = par3 + ext * s.c[var6];
          zz = par4 + ext * s.d[var6];
          
          pspX = MathHelper.a(Math.abs(pspX) / smass) * dirX + error * rand.nextGaussian() * 0.02D;
          pspY = MathHelper.a(Math.abs(pspY) / smass) * dirY + error * rand.nextGaussian() * 0.02D;
          pspZ = MathHelper.a(Math.abs(pspZ) / smass) * dirZ + error * rand.nextGaussian() * 0.02D;
          for (int i = ext - 1; i > sticky; i--)
          {
            xx -= s.b[var6];
            yy -= s.c[var6];
            zz -= s.d[var6];
            int blid2 = par1World.a(xx, yy, zz);
            meta = par1World.h(xx, yy, zz);
            Block bb = Block.s[blid2];
            if ((bb == Block.z) || (bb == Block.aE) || (bb == Block.bD)) {
              blid2 = Block.A.cF;
            }
            if ((bb == Block.ae) || (bb == Block.aa)) {
              meta &= 0x7;
            }
            EntityFallingSand entityfallingsand = new EntityFallingSand(par1World, 0.5D + xx + s.b[var6], 0.5D + yy + s.c[var6], 0.5D + zz + s.d[var6], blid2, meta);
            entityfallingsand.x = bSpeedR(pspX + error * rand.nextGaussian() * 0.01D);
            entityfallingsand.y = bSpeedR(pspY + error * rand.nextGaussian() * 0.01D);
            entityfallingsand.z = bSpeedR(pspZ + error * rand.nextGaussian() * 0.01D);
            if (canBurn(blid2))
            {
              if ((i == ext - 1) && (blid == 51)) {
                entityfallingsand.d(60);
              }
              if (par1World.a(xx, yy + 1, zz) == 51) {
                entityfallingsand.d(60);
              }
            }
            entityfallingsand.bpdata = getBlockBPdata(par1World, xx, yy, zz);
            if (Block.s[blid2].hasTileEntity(meta))
            {
              entityfallingsand.e = new NBTTagCompound("TileEntityData");
              par1World.r(xx, yy, zz).b(entityfallingsand.e);
              par1World.s(xx, yy, zz);
            }
            par1World.f(xx, yy, zz, 0, 0, 3);
            setBlockBPdata(par1World, xx, yy, zz, 0);
            par1World.d(entityfallingsand);
          }
          if (sticky == 1)
          {
            par1World.d(par2, par3, par4, par1block.cF, 3, var6);
            pistonMoveMark(par1World, par2, par3, par4, 2, var6);
          }
          else
          {
            par1World.d(par2, par3, par4, par1block.cF, 2, var6);
            pistonMoveMark(par1World, par2, par3, par4, 1, var6);
          }
        }
        else
        {
          ext = canExtend(par1World, par2, par3, par4, var6, par1block, false);
          if (ext == 0) {
            return;
          }
          par1World.d(par2, par3, par4, par1block.cF, ext + 1, var6);
          pistonMoveMark(par1World, par2, par3, par4, ext, var6);
        }
      }
      else
      {
        ext = canExtend(par1World, par2, par3, par4, var6, par1block, false);
        if (ext == 0) {
          return;
        }
        par1World.d(par2, par3, par4, par1block.cF, ext + 1, var6);
        pistonMoveMark(par1World, par2, par3, par4, ext, var6);
      }
      par1World.b(par2, par3, par4, var5 | 0x8, 0);
    }
    else
    {
      if (isSticky)
      {
        int xx = par2 + 2 * s.b[var6];
        int yy = par3 + 2 * s.c[var6];
        int zz = par4 + 2 * s.d[var6];
        int blid2 = par1World.a(xx, yy, zz);
        int meta = par1World.h(xx, yy, zz);
        boolean pull = true;
        Block bb = Block.s[blid2];
        boolean empty = (blid2 == 0) || ((blid2 > 7) && (blid2 < 12)) || (blid2 == 51) || (bb.cU.d());
        if ((par1World.pistonMoveBlocks.contains("" + xx + "." + yy + "." + zz)) || (empty) || ((blockSet[blid2][meta].pushtype != 1) && (blockSet[blid2][meta].pushtype != 2))) {
          pull = false;
        } else if (((bb == Block.ae) || (bb == Block.aa)) && (!canmove(par1World, xx, yy, zz, par1block))) {
          pull = false;
        }
        if (pull)
        {
          par1World.d(par2, par3, par4, par1block.cF, 1, var6);
          pistonMoveMark(par1World, par2, par3, par4, 2, var6);
        }
        else
        {
          par1World.d(par2, par3, par4, par1block.cF, 0, var6);
          pistonMoveMark(par1World, par2, par3, par4, 1, var6);
        }
      }
      else
      {
        par1World.d(par2, par3, par4, par1block.cF, 0, var6);
        pistonMoveMark(par1World, par2, par3, par4, 1, var6);
      }
      par1World.b(par2, par3, par4, var5 & 0x7, 0);
    }
  }
  
  private static int canExtend(World par0World, int par1, int par2, int par3, int par4, BlockPistonBase par1block, boolean catp)
  {
    if (par0World.pistonMoveBlocks.contains("" + par1 + "." + par2 + "." + par3)) {
      return 0;
    }
    for (int var8 = 1; var8 < 14; var8++)
    {
      par1 += s.b[par4];
      par2 += s.c[par4];
      par3 += s.d[par4];
      if (par0World.pistonMoveBlocks.contains("" + par1 + "." + par2 + "." + par3)) {
        return 0;
      }
      int blid = par0World.a(par1, par2, par3);
      Block bb = Block.s[blid];
      boolean empty = (blid == 0) || ((blid > 7) && (blid < 12)) || (blid == 51) || (bb.cU.d());
      if (empty)
      {
        if ((par2 <= 0) || (par2 >= par0World.R() - 1)) {
          return 0;
        }
        return var8;
      }
      int meta = par0World.h(par1, par2, par3);
      if (blockSet[blid][meta].pushtype == 0)
      {
        if (blockSet[blid][meta].fragile == 0) {
          return 0;
        }
        if (blockSet[blid][meta].strength > 10) {
          return 0;
        }
        return var8;
      }
      if (catp)
      {
        if (blockSet[blid][meta].pushtype == 2) {
          return 0;
        }
      }
      else if (blockSet[blid][meta].pushtype == 3) {
        return 0;
      }
      if (((bb == Block.ae) || (bb == Block.aa)) && (!canmove(par0World, par1, par2, par3, par1block))) {
        return 0;
      }
    }
    return 0;
  }
  
  public static boolean canmove(World world, int i, int j, int k, BlockPistonBase par1block)
  {
    int orient = BlockPistonBase.d(world.h(i, j, k));
    if (orient > 5) {
      orient = 0;
    }
    int i2 = i + s.b[orient];
    int j2 = j + s.c[orient];
    int k2 = k + s.d[orient];
    
    int blid = world.a(i2, j2, k2);
    if ((blid != Block.ah.cF) && (blid != Block.af.cF)) {
      return true;
    }
    int orient2 = BlockPistonBase.d(world.h(i2, j2, k2));
    if (orient2 > 5) {
      orient2 = 0;
    }
    if ((blid == Block.af.cF) && (orient == orient2)) {
      return false;
    }
    if (blid == Block.ah.cF)
    {
      TileEntity var7 = world.r(i2, j2, k2);
      if ((var7 instanceof TileEntityPiston)) {
        if (((TileEntityPiston)var7).c() == orient) {
          return false;
        }
      }
    }
    return true;
  }
  
  static void pistonMoveMark(World world, int i, int j, int k, int lngth, int orient)
  {
    int io = s.b[orient];
    int jo = s.c[orient];
    int ko = s.d[orient];
    for (int l = 0; l <= lngth; l++)
    {
      world.pistonMoveBlocks.add("" + i + "." + j + "." + k);
      i += io;
      j += jo;
      k += ko;
    }
  }
  
  public static boolean onBlockPistonEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6, BlockPistonBase par1block, boolean isSticky)
  {
    if (par5 > 1)
    {
      par5--;
      int xx = par2 + par5 * s.b[par6];
      int yy = par3 + par5 * s.c[par6];
      int zz = par4 + par5 * s.d[par6];
      if (!par1World.I)
      {
        int blid = par1World.a(xx, yy, zz);
        int meta = par1World.h(xx, yy, zz);
        if (blockSet[blid][meta].fragile > 0)
        {
          if (Block.s[blid].hasTileEntity(meta))
          {
            NBTTagCompound nnn = new NBTTagCompound("TileEntityData");
            par1World.r(xx, yy, zz).b(nnn);
            dropItemsNBT(par1World, xx, yy, zz, nnn);
            par1World.s(xx, yy, zz);
          }
          if (blockSet[blid][meta].fragile == 2) {
            Block.s[blid].c(par1World, xx, yy, zz, meta, 0);
          }
          par1World.f(xx, yy, zz, 0, 0, 3);
        }
      }
      for (int i = par5; i > 1; i--)
      {
        int xxf = xx - s.b[par6];
        int yyf = yy - s.c[par6];
        int zzf = zz - s.d[par6];
        
        int var12 = par1World.a(xxf, yyf, zzf);
        int var13 = par1World.h(xxf, yyf, zzf);
        int bpmeta = getBlockBPdata(par1World, xxf, yyf, zzf);
        
        Block bb = Block.s[var12];
        if ((bb == Block.ae) || (bb == Block.aa)) {
          var13 &= 0x7;
        }
        TileEntityPiston tePiston = new TileEntityPiston(var12, var13, par6, true, false);
        tePiston.bpmeta = bpmeta;
        if (Block.s[var12].hasTileEntity(var13))
        {
          tePiston.movingBlockTileEntityData = new NBTTagCompound("TileEntityData");
          par1World.r(xxf, yyf, zzf).b(tePiston.movingBlockTileEntityData);
          par1World.s(xxf, yyf, zzf);
        }
        par1World.f(xx, yy, zz, Block.ah.cF, var13, 2);
        par1World.a(xx, yy, zz, tePiston);
        xx -= s.b[par6];
        yy -= s.c[par6];
        zz -= s.d[par6];
      }
      par1World.f(par2 + s.b[par6], par3 + s.c[par6], par4 + s.d[par6], Block.ah.cF, par6 | (isSticky ? 8 : 0), 2);
      par1World.a(par2 + s.b[par6], par3 + s.c[par6], par4 + s.d[par6], BlockPistonMoving.a(Block.af.cF, par6 | (isSticky ? 8 : 0), par6, true, true));
      par1World.f(par2, par3, par4, par1block.cF, par6 | 0x8, 2);
      
      par1World.a(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D, "tile.piston.out", 0.5F, rand.nextFloat() * 0.25F + 0.6F);
    }
    else
    {
      par1World.f(par2, par3, par4, Block.ah.cF, par6, 2);
      par1World.a(par2, par3, par4, BlockPistonMoving.a(par1block.cF, par6, par6, false, true));
      if (par5 == 0)
      {
        par1World.f(par2 + s.b[par6], par3 + s.c[par6], par4 + s.d[par6], 0, 0, 3);
      }
      else
      {
        int var8 = par2 + s.b[par6] * 2;
        int var9 = par3 + s.c[par6] * 2;
        int var10 = par4 + s.d[par6] * 2;
        int var11 = par1World.a(var8, var9, var10);
        int var12 = par1World.h(var8, var9, var10);
        int bpmeta = getBlockBPdata(par1World, var8, var9, var10);
        
        TileEntityPiston tePiston = new TileEntityPiston(var11, var12, par6, false, false);
        tePiston.bpmeta = bpmeta;
        if (Block.s[var11].hasTileEntity(var12))
        {
          tePiston.movingBlockTileEntityData = new NBTTagCompound("TileEntityData");
          par1World.r(var8, var9, var10).b(tePiston.movingBlockTileEntityData);
          par1World.s(var8, var9, var10);
        }
        par2 += s.b[par6];
        par3 += s.c[par6];
        par4 += s.d[par6];
        par1World.f(par2, par3, par4, Block.ah.cF, var12, 0);
        par1World.a(par2, par3, par4, tePiston);
        par1World.f(var8, var9, var10, 0, 0, 3);
      }
      par1World.a(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D, "tile.piston.in", 0.5F, rand.nextFloat() * 0.15F + 0.6F);
    }
    return true;
  }
  
  public static void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int meta, int blockID)
  {
    if (!par1World.I)
    {
      meta &= 0xF;
      if ((blockSet[blockID][meta].movenum == 2) && (blockSet[blockID][meta].movechanger > 1)) {
        moveChangeMechanic(par1World, par2, par3, par4, blockID, 1, 0);
      }
      notifyMove(par1World, par2, par3, par4);
    }
  }
  
  public static void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int blockID)
  {
    if (par1World.I) {
      return;
    }
    par1World.moveTickList.scheduleBlockMoveUpdate(par1World, par2, par3, par4, blockID, par1World.h(par2, par3, par4), false);
  }
  
  public static void onEntityCollidedWithBlock(World world, int par1, int par2, int par3, int blockID, Entity par5Entity)
  {
    if (blockSet[blockID][world.h(par1, par2, par3)].trapping) {
      par5Entity.al();
    }
  }
  
  public static void onPostBlockPlaced(World par1World, int par2, int par3, int par4, int blockID, int meta)
  {
    if (!par1World.I)
    {
      meta &= 0xF;
      if (blockSet[blockID][meta].movenum == 2) {
        setBlockBPdata(par1World, par2, par3, par4, 15 * blockSet[blockID][meta].placedmove);
      }
      notifyMove(par1World, par2, par3, par4);
    }
  }
  
  public static void fallingSandUpdate(World world, EntityFallingSand fsand)
  {
    fsand.c += 1;
    if (fsand.c < 3)
    {
      if (fsand.c == 1) {
        fsand.b &= 0xF;
      }
      return;
    }
    int i = MathHelper.c(fsand.u);
    int j = MathHelper.c(fsand.v);
    int k = MathHelper.c(fsand.w);
    if (fsand.c == 4) {
      notifyMove(world, i, j, k);
    }
    if ((!world.I) && (fsand.dead < 4))
    {
      EntityFallingSand tmp95_94 = fsand;tmp95_94.dead = ((byte)(tmp95_94.dead - 1));
      if (fsand.dead <= 0) {
        fsand.w();
      }
      return;
    }
    fsand.Z = true;
    fsand.F = false;
    if ((j < -3) || (fsand.c > 600))
    {
      fsand.w();
      if (!world.I) {
        dropFallingSand(fsand);
      }
    }
    fsand.media = world.a(i, j, k);
    if (fsand.slideDir != 0) {
      if (fsand.c < 8)
      {
        int stime = fsand.c - 3;
        int sdir = fsand.slideDir - 1;
        if (stime == 0) {
          switch (sdir)
          {
          case 0: 
            fsand.b(i - 0.0625D + 0.5D, j - 0.0625D + 0.5D, k + 0.5D); break;
          case 1: 
            fsand.b(i + 0.5D, j - 0.0625D + 0.5D, k - 0.0625D + 0.5D); break;
          case 2: 
            fsand.b(i + 0.5D, j - 0.0625D + 0.5D, k + 0.0625D + 0.5D); break;
          case 3: 
            fsand.b(i + 0.0625D + 0.5D, j - 0.0625D + 0.5D, k + 0.5D); break;
          }
        }
        fsand.x = slideSpeedz[stime][sdir][0];
        fsand.y = slideSpeedz[stime][sdir][1];
        fsand.z = slideSpeedz[stime][sdir][2];
        fsand.accelerationX = 0.0D;
        fsand.accelerationY = 0.0D;
        fsand.accelerationZ = 0.0D;
      }
      else
      {
        fsand.slideDir = 0;
      }
    }
    if (fsand.x > 3.9D) {
      fsand.x = 3.9D;
    } else if (fsand.x < -3.9D) {
      fsand.x = -3.9D;
    }
    if (fsand.y > 3.9D) {
      fsand.y = 3.9D;
    } else if (fsand.y < -3.9D) {
      fsand.y = -3.9D;
    }
    if (fsand.z > 3.9D) {
      fsand.z = 3.9D;
    } else if (fsand.z < -3.9D) {
      fsand.z = -3.9D;
    }
    double cmotionX = fsand.x;
    double cmotionY = fsand.y;
    double cmotionZ = fsand.z;
    
    double caccelerationX = fsand.accelerationX;
    double caccelerationY = fsand.accelerationY;
    double caccelerationZ = fsand.accelerationZ;
    
    fsand.accelerationX = 0.0D;
    fsand.accelerationY = 0.0D;
    fsand.accelerationZ = 0.0D;
    if (fsand.slideDir == 0)
    {
      fsand.x = bSpeedR(fsand.x + caccelerationX);
      fsand.y = bSpeedR(fsand.y + caccelerationY);
      fsand.z = bSpeedR(fsand.z + caccelerationZ);
    }
    double moveX = cmotionX + caccelerationX * 0.5D;
    double moveY = cmotionY + caccelerationY * 0.5D;
    double moveZ = cmotionZ + caccelerationZ * 0.5D;
    
    double axisaligned_maxmove = MathHelper.a(MathHelper.a(moveX, moveZ), moveY);
    double blockofsZ;
    double blockofsX;
    double blockofsY;
    double blockofsZ;
    if (axisaligned_maxmove != 0.0D)
    {
      double blockofsX = 0.498D * moveX / axisaligned_maxmove;
      double blockofsY = 0.498D * moveY / axisaligned_maxmove;
      blockofsZ = 0.498D * moveZ / axisaligned_maxmove;
    }
    else
    {
      blockofsX = 0.0D;
      blockofsY = 0.0D;
      blockofsZ = 0.0D;
    }
    double djumpdist2 = blockofsX * blockofsX + blockofsY * blockofsY + blockofsZ * blockofsZ;
    double jumpdist2 = moveX * moveX + moveY * moveY + moveZ * moveZ;
    
    int mass = blockSet[fsand.a][fsand.b].mass;
    int em = mass / 10 + (int)(0.5D * mass * jumpdist2);
    if ((fsand.ae()) && (jumpdist2 > 4.0D) && (Block.s[fsand.a] != Block.bg)) {
      fsand.A();
    }
    AxisAlignedBB Sandbbox = null;
    int ii;
    int ii;
    if (djumpdist2 == 0.0D) {
      ii = 0;
    } else {
      ii = (int)Math.ceil(MathHelper.a(jumpdist2 / djumpdist2));
    }
    double jumpPosX = 0.0D;
    double jumpPosY = 0.0D;
    double jumpPosZ = 0.0D;
    
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
        jumpPosX = fsand.u + moveX;
        jumpPosY = fsand.v + moveY;
        jumpPosZ = fsand.w + moveZ;
        
        Sandbbox = fsand.E.c();
        Sandbbox.d(moveX, moveY, moveZ);
      }
      else if (i1 == 1)
      {
        jumpPosX = fsand.u + blockofsX;
        jumpPosY = fsand.v + blockofsY;
        jumpPosZ = fsand.w + blockofsZ;
        
        Sandbbox = fsand.E.c();
        Sandbbox.d(blockofsX, blockofsY, blockofsZ);
      }
      else
      {
        jumpPosX += blockofsX;
        jumpPosY += blockofsY;
        jumpPosZ += blockofsZ;
        
        Sandbbox.d(blockofsX, blockofsY, blockofsZ);
      }
      in = MathHelper.c(jumpPosX);
      jn = MathHelper.c(jumpPosY);
      kn = MathHelper.c(jumpPosZ);
      if ((jp != jn) || (ip != in) || (kp != kn))
      {
        int bidn = world.a(in, jn, kn);
        int metan = world.h(in, jn, kn);
        if (blockSet[bidn][metan].fragile > 0)
        {
          Block block = Block.s[bidn];
          if (!world.I) {
            if (blockSet[bidn][metan].fragile > 0)
            {
              if (Block.s[bidn].hasTileEntity(metan))
              {
                NBTTagCompound nnn = new NBTTagCompound("TileEntityData");
                world.r(in, jn, kn).b(nnn);
                dropItemsNBT(world, in, jn, kn, nnn);
                world.s(in, jn, kn);
              }
              if (blockSet[bidn][metan].fragile == 2) {
                Block.s[bidn].c(world, in, jn, kn, metan, 0);
              }
              world.f(in, jn, kn, 0, 0, 3);
            }
          }
          bidn = 0;
          
          world.a(in + 0.5F, jn + 0.5F, kn + 0.5F, block.cS.a(), (block.cS.c() + 1.0F) / 2.0F, block.cS.d() * 0.8F);
          
          double sl = 1.0D - blockSet[bidn][metan].strength / 64000.0D;
          fsand.x *= sl;
          fsand.y *= sl;
          fsand.z *= sl;
        }
        if ((fsand.ae()) && (bidn == 0))
        {
          world.a("largesmoke", (float)jumpPosX + rand.nextFloat(), (float)jumpPosY + rand.nextFloat(), (float)jumpPosZ + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
          world.a("flame", (float)jumpPosX + rand.nextFloat(), (float)jumpPosY + rand.nextFloat(), (float)jumpPosZ + rand.nextFloat(), 0.0D, 0.2D, 0.0D);
        }
        if (fsand.media != bidn)
        {
          if (bidn != 0)
          {
            Material mt = Block.s[bidn].cU;
            if (mt.d())
            {
              if (mt == Material.i)
              {
                if (canBurn(fsand.a)) {
                  fsand.d(60);
                } else {
                  fsand.d(1);
                }
                world.a(fsand, "random.fizz", 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
              }
              else
              {
                fsand.A();
                world.a(fsand, "random.splash", 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
              }
            }
            else if (bidn == Block.aw.cF) {
              world.a(fsand, "random.fizz", 0.5F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
            }
          }
          fsand.media = bidn;
          if ((fsand.slideDir == 0) && (bidn != 51))
          {
            moveX = jumpPosX - fsand.u;
            moveY = jumpPosY - fsand.v;
            moveZ = jumpPosZ - fsand.w;
            
            break;
          }
        }
        ip = in;
        jp = jn;
        kp = kn;
      }
      if (fsand.slideDir == 0)
      {
        if ((!canMoveTo(world, MathHelper.c(jumpPosX + 0.499D), MathHelper.c(jumpPosY + 0.499D), MathHelper.c(jumpPosZ + 0.499D), em)) || (!canMoveTo(world, MathHelper.c(jumpPosX + 0.499D), MathHelper.c(jumpPosY + 0.499D), MathHelper.c(jumpPosZ - 0.499D), em)) || (!canMoveTo(world, MathHelper.c(jumpPosX + 0.499D), MathHelper.c(jumpPosY - 0.499D), MathHelper.c(jumpPosZ + 0.499D), em)) || (!canMoveTo(world, MathHelper.c(jumpPosX + 0.499D), MathHelper.c(jumpPosY - 0.499D), MathHelper.c(jumpPosZ - 0.499D), em)) || (!canMoveTo(world, MathHelper.c(jumpPosX - 0.499D), MathHelper.c(jumpPosY + 0.499D), MathHelper.c(jumpPosZ + 0.499D), em)) || (!canMoveTo(world, MathHelper.c(jumpPosX - 0.499D), MathHelper.c(jumpPosY + 0.499D), MathHelper.c(jumpPosZ - 0.499D), em)) || (!canMoveTo(world, MathHelper.c(jumpPosX - 0.499D), MathHelper.c(jumpPosY - 0.499D), MathHelper.c(jumpPosZ + 0.499D), em)) || (!canMoveTo(world, MathHelper.c(jumpPosX - 0.499D), MathHelper.c(jumpPosY - 0.499D), MathHelper.c(jumpPosZ - 0.499D), em)))
        {
          double eimp = 0.0005D * jumpdist2 * blockSet[fsand.a][fsand.b].mass;
          if (eimp > 0.5D)
          {
            if (!world.I)
            {
              if (eimp > 3.5D) {
                eimp = 3.5D;
              }
              Explosion var10 = new Explosion(world, fsand, jumpPosX, jumpPosY, jumpPosZ, (float)eimp);
              if (fsand.ae()) {
                var10.a = true;
              }
              var10.impact = true;
              world.explosionQueue.add(var10);
            }
            fsand.x *= 0.7D;
            fsand.y *= 0.7D;
            fsand.z *= 0.7D;
            fsand.J = true;
          }
          if ((blockSet[fsand.a][fsand.b].fragile > 0) && (em > blockSet[fsand.a][fsand.b].strength))
          {
            Block block = Block.s[fsand.a];
            world.a(jumpPosX, jumpPosY, jumpPosZ, block.cS.a(), (block.cS.c() + 1.0F) / 2.0F, block.cS.d() * 0.8F); EntityFallingSand 
            
              tmp2293_2292 = fsand;tmp2293_2292.dead = ((byte)(tmp2293_2292.dead - 1));
            if (!world.I) {
              if (blockSet[fsand.a][fsand.b].fragile == 2)
              {
                fsand.u = jumpPosX;
                fsand.v = jumpPosY;
                fsand.w = jumpPosZ;
                dropFallingSand(fsand);
              }
              else if ((blockSet[fsand.a][fsand.b].fragile == 1) && (fsand.e != null))
              {
                dropItemsNBT(world, MathHelper.c(jumpPosX), MathHelper.c(jumpPosY), MathHelper.c(jumpPosZ), fsand.e);
              }
            }
            return;
          }
          moveX = jumpPosX - fsand.u;
          moveY = jumpPosY - fsand.v;
          moveZ = jumpPosZ - fsand.w;
          
          fsand.Z = false;
          
          break;
        }
        Entity collent = world.a(Entity.class, Sandbbox, fsand);
        if (collent != null)
        {
          if ((collent instanceof EntityFallingSand))
          {
            double m1 = blockSet[fsand.a][fsand.b].mass;
            double m2 = blockSet[((EntityFallingSand)collent).a][((EntityFallingSand)collent).b].mass;
            double smass = m1 + m2;
            

            double is = m1 * fsand.x + m2 * collent.x;
            double vv = bSpeedR(0.98D * is / smass);
            
            fsand.x = vv;
            collent.x = vv;
            
            is = m1 * fsand.z + m2 * collent.z;
            vv = bSpeedR(0.98D * is / smass);
            
            fsand.z = vv;
            collent.z = vv;
            
            is = m1 * fsand.y + m2 * collent.y;
            vv = bSpeedR(0.98D * is / smass);
            
            fsand.y = vv;
            collent.y = vv;
            
            fsand.J = true;
            collent.J = true;
            if ((blockSet[((EntityFallingSand)collent).a][((EntityFallingSand)collent).b].fragile > 0) && (em > blockSet[((EntityFallingSand)collent).a][((EntityFallingSand)collent).b].strength))
            {
              Block block = Block.s[((EntityFallingSand)collent).a];
              world.a(collent.u, collent.v, collent.w, block.cS.a(), (block.cS.c() + 1.0F) / 2.0F, block.cS.d() * 0.8F); EntityFallingSand 
              
                tmp2791_2788 = ((EntityFallingSand)collent);tmp2791_2788.dead = ((byte)(tmp2791_2788.dead - 1));
              if (!world.I) {
                if (blockSet[((EntityFallingSand)collent).a][((EntityFallingSand)collent).b].fragile == 2) {
                  dropFallingSand((EntityFallingSand)collent);
                } else if ((blockSet[((EntityFallingSand)collent).a][((EntityFallingSand)collent).b].fragile == 1) && (((EntityFallingSand)collent).e != null)) {
                  dropItemsNBT(world, MathHelper.c(collent.u), MathHelper.c(collent.v), MathHelper.c(collent.w), ((EntityFallingSand)collent).e);
                }
              }
            }
            if ((blockSet[fsand.a][fsand.b].fragile > 0) && (em > blockSet[fsand.a][fsand.b].strength))
            {
              Block block = Block.s[fsand.a];
              world.a(jumpPosX, jumpPosY, jumpPosZ, block.cS.a(), (block.cS.c() + 1.0F) / 2.0F, block.cS.d() * 0.8F); EntityFallingSand 
              
                tmp3015_3014 = fsand;tmp3015_3014.dead = ((byte)(tmp3015_3014.dead - 1));
              if (!world.I) {
                if (blockSet[fsand.a][fsand.b].fragile == 2)
                {
                  fsand.u = jumpPosX;
                  fsand.v = jumpPosY;
                  fsand.w = jumpPosZ;
                  dropFallingSand(fsand);
                }
                else if ((blockSet[fsand.a][fsand.b].fragile == 1) && (fsand.e != null))
                {
                  dropItemsNBT(world, MathHelper.c(jumpPosX), MathHelper.c(jumpPosY), MathHelper.c(jumpPosZ), fsand.e);
                }
              }
              return;
            }
            fsand.Z = false;
            
            moveX = jumpPosX - fsand.u;
            moveY = jumpPosY - fsand.v;
            moveZ = jumpPosZ - fsand.w;
            break;
          }
          if ((collent instanceof EntityLiving))
          {
            double m1 = blockSet[fsand.a][fsand.b].mass;
            double m2 = ((EntityLiving)collent).aS() * 3.0D;
            double smass = m1 + m2;
            
            double damage = fsand.x * fsand.x + fsand.y * fsand.y + fsand.z * fsand.z;
            
            double is = m1 * fsand.x + m2 * (collent.u - collent.r);
            double vv = bSpeedR(0.98D * is / smass);
            damage -= vv * vv;
            fsand.x = vv;
            collent.x = vv;
            
            is = m1 * fsand.z + m2 * (collent.w - collent.t);
            vv = bSpeedR(0.98D * is / smass);
            damage -= vv * vv;
            fsand.z = vv;
            collent.z = vv;
            if ((fsand.y < 0.0D) && (collent.F))
            {
              if (fsand.y < -0.3D) {
                vv = bSpeedR(0.5D * fsand.y);
              } else {
                vv = fsand.y;
              }
            }
            else
            {
              is = m1 * fsand.y + m2 * (collent.v - collent.s);
              vv = bSpeedR(0.98D * is / smass);
            }
            damage -= vv * vv;
            fsand.y = vv;
            collent.y = vv;
            
            fsand.J = true;
            collent.J = true;
            
            int d = (int)(0.083D * m1 * damage);
            if (d > 0)
            {
              if (d > 4) {
                world.a(collent, "damage.fallbig", 1.0F, 1.0F);
              }
              if (!world.I) {
                ((EntityLiving)collent).a(DamageSource.n, d);
              }
            }
            if (!world.I) {
              if ((collent instanceof EntityPlayerMP)) {
                ((EntityPlayerMP)collent).a.b(new Packet28EntityVelocity(collent.k, collent.x, collent.y, collent.z));
              }
            }
            moveX = jumpPosX - fsand.u;
            moveY = jumpPosY - fsand.v;
            moveZ = jumpPosZ - fsand.w;
            break;
          }
          if ((collent instanceof EntityItem))
          {
            collent.x = fsand.x;
            collent.y = fsand.y;
            collent.z = fsand.z;
            collent.J = true;
          }
          else
          {
            fsand.accelerationX -= fsand.x;
            fsand.accelerationY -= fsand.y;
            fsand.accelerationZ -= fsand.z;
            

            moveX = jumpPosX - fsand.u;
            moveY = jumpPosY - fsand.v;
            moveZ = jumpPosZ - fsand.w;
            break;
          }
        }
      }
      else
      {
        Entity collent = world.a(Entity.class, Sandbbox, fsand);
        if ((collent != null) && (((collent instanceof EntityLiving)) || ((collent instanceof EntityItem))))
        {
          collent.x = (collent.x * 0.2D + fsand.x * 0.8D);
          collent.y = (collent.y * 0.2D + fsand.y * 0.8D);
          collent.z = (collent.z * 0.2D + fsand.z * 0.8D);
          collent.J = true;
          if (!world.I) {
            if ((collent instanceof EntityPlayerMP)) {
              ((EntityPlayerMP)collent).a.b(new Packet28EntityVelocity(collent.k, collent.x, collent.y, collent.z));
            }
          }
        }
      }
    }
    double density = 1.25D;
    if (fsand.media != 0)
    {
      Material mt = Block.s[fsand.media].cU;
      if (mt.d())
      {
        if (mt == Material.i) {
          density = 2000.0D;
        } else {
          density = 1000.0D;
        }
      }
      else if ((!world.I) && (!(fsand instanceof EntityTNTPrimed)))
      {
        placeBlock(world, fsand, jumpPosX, jumpPosY, jumpPosZ, in, jn, kn);
        return;
      }
    }
    density = -0.4D * density / blockSet[fsand.a][fsand.b].mass;
    double aaccX = density * fsand.x * Math.abs(fsand.x);
    double aaccY = density * fsand.y * Math.abs(fsand.y);
    double aaccZ = density * fsand.z * Math.abs(fsand.z);
    
    fsand.accelerationY -= 0.024525D;
    
    double mmot = fsand.x + aaccX;
    if (((fsand.x < 0.0D) && (mmot > 0.0D)) || ((fsand.x > 0.0D) && (mmot < 0.0D))) {
      aaccX = -0.9D * fsand.x;
    }
    mmot = fsand.y + aaccY;
    if (((fsand.y < 0.0D) && (mmot > 0.0D)) || ((fsand.y > 0.0D) && (mmot < 0.0D))) {
      aaccY = -0.9D * fsand.y;
    }
    mmot = fsand.z + aaccZ;
    if (((fsand.z < 0.0D) && (mmot > 0.0D)) || ((fsand.z > 0.0D) && (mmot < 0.0D))) {
      aaccZ = -0.9D * fsand.z;
    }
    fsand.accelerationX += aaccX;
    fsand.accelerationY += aaccY;
    fsand.accelerationZ += aaccZ;
    
    fsand.r = fsand.u;
    fsand.s = fsand.v;
    fsand.t = fsand.w;
    
    moveEntity(world, fsand, moveX, moveY, moveZ);
    
    i = MathHelper.c(fsand.u);
    j = MathHelper.c(fsand.v);
    k = MathHelper.c(fsand.w);
    if (fsand.F)
    {
      fsand.x *= 0.9D;
      fsand.z *= 0.9D;
    }
    if ((fsand instanceof EntityTNTPrimed))
    {
      if (!world.I) {
        if (((EntityTNTPrimed)fsand).a-- <= 0)
        {
          fsand.w();
          ((EntityTNTPrimed)fsand).d();
        }
      }
      world.a("smoke", fsand.u, fsand.v + 0.5D, fsand.w, 0.0D, 0.0D, 0.0D);
    }
    else if (fsand.F)
    {
      if ((jumpdist2 < 0.05D) && (canMoveTo(world, i, j, k, em)))
      {
        Block block = Block.s[fsand.a];
        world.a(fsand.u, fsand.v, fsand.w, block.cS.b(), (block.cS.c() + 1.0F) / 2.0F, block.cS.d() * 0.8F); EntityFallingSand 
        
          tmp4526_4525 = fsand;tmp4526_4525.dead = ((byte)(tmp4526_4525.dead - 1));
        if (!world.I)
        {
          world.f(i, j, k, fsand.a, fsand.b, 3);
          setBlockBPdata(world, i, j, k, fsand.bpdata);
          if (fsand.e != null)
          {
            TileEntity tile = Block.s[fsand.a].createTileEntity(world, fsand.b);
            tile.a(fsand.e);
            world.a(i, j, k, tile);
          }
          if ((fsand.ae()) && (world.a(i, j + 1, k) == 0)) {
            world.f(i, j + 1, k, Block.aw.cF, 0, 3);
          }
          world.moveTickList.scheduleBlockMoveUpdate(world, i, j, k, fsand.a, fsand.b, true);
          notifyMove(world, i, j, k);
        }
      }
    }
  }
  
  protected static void placeBlock(World world, EntityFallingSand fsand, double jumpPosX, double jumpPosY, double jumpPosZ, int i, int j, int k)
  {
    double dist2 = 100.0D;
    double s1 = 0.0D;double s2 = 0.0D;double s3 = 0.0D;
    int x = 0;
    int y = 0;
    int z = 0;
    for (int ii = i - 1; ii < i + 2; ii++) {
      for (int jj = j - 1; jj < j + 2; jj++) {
        for (int kk = k - 1; kk < k + 2; kk++) {
          if (canMoveTo(world, ii, jj, kk, 0))
          {
            s1 = 0.5D + ii - jumpPosX;
            s2 = 0.5D + jj - jumpPosY;
            s3 = 0.5D + kk - jumpPosZ;
            double dist22 = s1 * s1 + s2 * s2 + s3 * s3;
            if (dist22 < dist2)
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
    fsand.w();
    if (dist2 < 100.0D)
    {
      world.f(x, y, z, fsand.a, fsand.b, 3);
      setBlockBPdata(world, x, y, z, fsand.bpdata);
      if (fsand.e != null)
      {
        TileEntity tile = Block.s[fsand.a].createTileEntity(world, fsand.b);
        tile.a(fsand.e);
        world.a(x, y, z, tile);
      }
      world.moveTickList.scheduleBlockMoveUpdate(world, x, y, z, fsand.a, fsand.b, true);
    }
    else
    {
      fsand.u = (0.5D + i);
      fsand.v = (0.5D + j);
      fsand.w = (0.5D + k);
      dropFallingSand(fsand);
    }
  }
  
  public static void doExplosionA(World world, Explosion explosion)
  {
    if (world.I) {
      return;
    }
    float var1 = explosion.g;
    HashSet var2 = new HashSet();
    if (!explosion.impact)
    {
      explosion.g *= 2.0F;
      int var3 = MathHelper.c(explosion.c - explosion.g - 1.0D);
      int var4 = MathHelper.c(explosion.c + explosion.g + 1.0D);
      int var5 = MathHelper.c(explosion.d - explosion.g - 1.0D);
      int var27 = MathHelper.c(explosion.d + explosion.g + 1.0D);
      int var7 = MathHelper.c(explosion.e - explosion.g - 1.0D);
      int var28 = MathHelper.c(explosion.e + explosion.g + 1.0D);
      List var9 = world.b(explosion.f, AxisAlignedBB.a().a(var3, var5, var7, var4, var27, var28));
      Vec3 var29 = world.V().a(explosion.c, explosion.d, explosion.e);
      for (int var11 = 0; var11 < var9.size(); var11++)
      {
        Entity var30 = (Entity)var9.get(var11);
        double var13 = var30.f(explosion.c, explosion.d, explosion.e) / explosion.g;
        if (var13 <= 1.0D)
        {
          double var15 = var30.u - explosion.c;
          double var17 = var30.v + var30.f() - explosion.d;
          double var19 = var30.w - explosion.e;
          double var32 = MathHelper.a(var15 * var15 + var17 * var17 + var19 * var19);
          if (var32 != 0.0D)
          {
            var15 /= var32;
            var17 /= var32;
            var19 /= var32;
            double var31 = world.a(var29, var30.E);
            double var33 = (1.0D - var13) * var31;
            if ((var30 instanceof EntityFallingSand))
            {
              var33 *= MathHelper.a(1500.0D / blockSet[((EntityFallingSand)var30).a][((EntityFallingSand)var30).b].mass);
              if ((((EntityFallingSand)var30).a == Block.ar.cF) && (!(var30 instanceof EntityTNTPrimed)))
              {
                EntityTNTPrimed entitytnt = new EntityTNTPrimed(world, var30.u, var30.v, var30.w, null);
                entitytnt.x = bSpeedR(var30.x + var15 * var33);
                entitytnt.y = bSpeedR(var30.y + var17 * var33);
                entitytnt.z = bSpeedR(var30.z + var19 * var33);
                entitytnt.a = (20 + prandnextint(40));
                world.d(entitytnt);
                var30.w();
              }
              else
              {
                var30.x = bSpeedR(var30.x + var15 * var33);
                var30.y = bSpeedR(var30.y + var17 * var33);
                var30.z = bSpeedR(var30.z + var19 * var33);
                var30.J = true;
              }
            }
            else
            {
              var30.a(DamageSource.a(explosion), (int)((var33 * var33 + var33) / 2.0D * 8.0D * explosion.g + 1.0D));
              double var36 = EnchantmentProtection.a(var30, var33);
              var30.x = bSpeedR(var30.x + var15 * var36);
              var30.y = bSpeedR(var30.y + var17 * var36);
              var30.z = bSpeedR(var30.z + var19 * var36);
              var30.J = true;
              if ((var30 instanceof EntityPlayer)) {
                explosion.b().put((EntityPlayer)var30, world.V().a(var15 * var33, var17 * var33, var19 * var33));
              }
            }
          }
        }
      }
      explosion.g = (var1 * explblstr / 100.0F);
    }
    for (int var3 = 0; var3 < 16; var3++) {
      for (int var4 = 0; var4 < 16; var4++) {
        for (int var5 = 0; var5 < 16; var5++) {
          if ((var3 == 0) || (var3 == 15) || (var4 == 0) || (var4 == 15) || (var5 == 0) || (var5 == 15))
          {
            double var6 = var3 / 15.0F * 2.0F - 1.0F;
            double var8 = var4 / 15.0F * 2.0F - 1.0F;
            double var10 = var5 / 15.0F * 2.0F - 1.0F;
            double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
            var6 /= var12;
            var8 /= var12;
            var10 /= var12;
            float var14 = explosion.g * (0.7F + world.s.nextFloat() * 0.6F);
            double var15 = explosion.c;
            double var17 = explosion.d;
            double var19 = explosion.e;
            for (float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F)
            {
              int var22 = MathHelper.c(var15);
              int var23 = MathHelper.c(var17);
              int var24 = MathHelper.c(var19);
              int var25 = world.a(var22, var23, var24);
              if (var25 > 0)
              {
                int m = world.h(var22, var23, var24);
                Block var26 = Block.s[var25];
                float var27 = explosion.f != null ? explosion.f.a(explosion, world, var24, var22, var23, var26) : var26.a(explosion.f);
                var14 -= (var27 + 0.3F) * var21;
                if (var14 > 0.0F)
                {
                  if ((!skipMove) && (((WorldServer)world).q().movingblocks < maxmovingblocks) && ((blockSet[var25][m].pushtype == 1) || (blockSet[var25][m].pushtype == 3)))
                  {
                    double d6 = var22 + 0.5F - explosion.c;
                    double d8 = var23 + 0.5F - explosion.d;
                    double d10 = var24 + 0.5F - explosion.e;
                    double d11 = MathHelper.a(d6 * d6 + d8 * d8 + d10 * d10);
                    d6 /= d11;
                    d8 /= d11;
                    d10 /= d11;
                    if (var25 == 1) {
                      var25 = 4;
                    } else if ((var25 == 2) || (var25 == 60) || (var25 == 110)) {
                      var25 = 3;
                    }
                    double sm = MathHelper.a(1500.0D / blockSet[var25][m].mass);
                    double d7 = 0.5D * sm / (d11 / explosion.g + 0.1D);
                    
                    d7 *= (rand.nextFloat() * rand.nextFloat() + 0.6F);
                    d6 *= d7;
                    d8 *= d7;
                    d10 *= d7;
                    


                    int bpdata = 0;
                    if ((blockSet[var25][m].movenum == 2) && (blockSet[var25][m].movechanger > 1)) {
                      bpdata = 15;
                    } else {
                      bpdata = getBlockBPdata(world, var22, var23, var24);
                    }
                    int meta = world.h(var22, var23, var24);
                    if ((!explosion.impact) && (var25 == Block.ar.cF))
                    {
                      EntityTNTPrimed entitytnt = new EntityTNTPrimed(world, var22 + 0.5F, var23 + 0.5F, var24 + 0.5F, null);
                      entitytnt.x = bSpeedR(d6 - rand.nextGaussian() * 0.05D);
                      entitytnt.y = bSpeedR(d8 - rand.nextGaussian() * 0.05D);
                      entitytnt.z = bSpeedR(d10 - rand.nextGaussian() * 0.05D);
                      entitytnt.a = (20 + prandnextint(40));
                      world.d(entitytnt);
                    }
                    else
                    {
                      EntityFallingSand entityfallingsand = new EntityFallingSand(world, var22 + 0.5F, var23 + 0.5F, var24 + 0.5F, var25, meta);
                      entityfallingsand.x = bSpeedR(d6 - rand.nextGaussian() * 0.05D);
                      entityfallingsand.y = bSpeedR(d8 - rand.nextGaussian() * 0.05D);
                      entityfallingsand.z = bSpeedR(d10 - rand.nextGaussian() * 0.05D);
                      if ((explosionfire) && ((!explosion.impact) || (explosion.a)) && (prandnextint(5) == 0) && (canBurn(var25))) {
                        entityfallingsand.d(60);
                      }
                      entityfallingsand.bpdata = bpdata;
                      if (Block.s[var25].hasTileEntity(meta))
                      {
                        entityfallingsand.e = new NBTTagCompound("TileEntityData");
                        world.r(var22, var23, var24).b(entityfallingsand.e);
                        world.s(var22, var23, var24);
                      }
                      world.d(entityfallingsand);
                    }
                    if ((explosionfire) && ((!explosion.impact) || (explosion.a)))
                    {
                      int k2 = world.a(var22, var23 - 1, var24);
                      if ((Block.t[k2] != 0) && (prandnextint(5) == 0)) {
                        world.f(var22, var23, var24, Block.aw.cF, 0, 3);
                      } else {
                        world.f(var22, var23, var24, 0, 0, 3);
                      }
                    }
                    else
                    {
                      world.f(var22, var23, var24, 0, 0, 3);
                    }
                  }
                  else
                  {
                    Block block = Block.s[var25];
                    if (block.a(explosion)) {
                      block.a(world, var22, var23, var24, world.h(var22, var23, var24), 1.0F / explosion.g, 0);
                    }
                    block.onBlockExploded(world, var22, var23, var24, explosion);
                    if ((explosionfire) && ((!explosion.impact) || (explosion.a)))
                    {
                      int k2 = world.a(var22, var23 - 1, var24);
                      if ((Block.t[k2] != 0) && (prandnextint(5) == 0)) {
                        world.f(var22, var23, var24, Block.aw.cF, 0, 3);
                      } else {
                        world.f(var22, var23, var24, 0, 0, 3);
                      }
                    }
                    else
                    {
                      world.f(var22, var23, var24, 0, 0, 3);
                    }
                  }
                  setBlockBPdata(world, var22, var23, var24, 0);
                  if (!explosion.impact) {
                    var2.add(new ChunkPosition(var22, var23, var24));
                  }
                }
              }
              var15 += var6 * var21;
              var17 += var8 * var21;
              var19 += var10 * var21;
            }
          }
        }
      }
    }
    explosion.h.addAll(var2);
    
    moveChangeMechanic(world, MathHelper.c(explosion.c), MathHelper.c(explosion.d), MathHelper.c(explosion.e), 0, 2, 12);
  }
  
  public static void doExplosionB(World world, Explosion explosion, boolean par1)
  {
    if (explosion.impact) {
      return;
    }
    world.a(explosion.c, explosion.d, explosion.e, "random.explode", 4.0F, (1.0F + (world.s.nextFloat() - world.s.nextFloat()) * 0.2F) * 0.7F);
    if ((explosion.g >= 2.0F) && (explosion.b)) {
      world.a("hugeexplosion", explosion.c, explosion.d, explosion.e, 1.0D, 0.0D, 0.0D);
    } else {
      world.a("largeexplode", explosion.c, explosion.d, explosion.e, 1.0D, 0.0D, 0.0D);
    }
    if (explosion.b)
    {
      Iterator var2 = explosion.h.iterator();
      while (var2.hasNext())
      {
        ChunkPosition var3 = (ChunkPosition)var2.next();
        int var4 = var3.a;
        int var5 = var3.b;
        int var6 = var3.c;
        int var7 = world.a(var4, var5, var6);
        if (par1)
        {
          double var8 = var4 + world.s.nextFloat();
          double var10 = var5 + world.s.nextFloat();
          double var12 = var6 + world.s.nextFloat();
          double var14 = var8 - explosion.c;
          double var16 = var10 - explosion.d;
          double var18 = var12 - explosion.e;
          double var20 = MathHelper.a(var14 * var14 + var16 * var16 + var18 * var18);
          var14 /= var20;
          var16 /= var20;
          var18 /= var20;
          double var22 = 0.5D / (var20 / explosion.g + 0.1D);
          var22 *= (world.s.nextFloat() * world.s.nextFloat() + 0.3F);
          var14 *= var22;
          var16 *= var22;
          var18 *= var22;
          world.a("explode", (var8 + explosion.c * 1.0D) / 2.0D, (var10 + explosion.d * 1.0D) / 2.0D, (var12 + explosion.e * 1.0D) / 2.0D, var14, var16, var18);
          world.a("smoke", var8, var10, var12, var14, var16, var18);
        }
      }
    }
  }
  
  public static void tickBlocksRandomMove(WorldServer wserver)
  {
    if (skipMove) {
      return;
    }
    Iterator var3 = wserver.G.iterator();
    while (var3.hasNext())
    {
      ChunkCoordIntPair var4 = (ChunkCoordIntPair)var3.next();
      int var5 = var4.a * 16;
      int var6 = var4.b * 16;
      Chunk var7 = wserver.e(var4.a, var4.b);
      




      ExtendedBlockStorage[] var19 = var7.i();
      int var9 = var19.length;
      for (int var10 = 0; var10 < var9; var10++)
      {
        ExtendedBlockStorage var21 = var19[var10];
        if (var21 != null) {
          for (int var20 = 0; var20 < 3; var20++)
          {
            updateLCG = updateLCG * 3 + 1013904223;
            int var13 = updateLCG >> 2;
            int var14 = var13 & 0xF;
            int var15 = var13 >> 8 & 0xF;
            int var16 = var13 >> 16 & 0xF;
            int var17 = var21.a(var14, var16, var15);
            int m = var21.b(var14, var16, var15);
            if (blockSet[var17][m].randomtick) {
              tryToMove(wserver, var14 + var5, var16 + var21.d(), var15 + var6, var17, m, false);
            }
          }
        }
      }
    }
  }
  
  public static EntityFallingSand createFallingsand(World world, double var2, double var4, double var6, Packet23VehicleSpawn par1Packet23VehicleSpawn)
  {
    EntityFallingSand var8 = new EntityFallingSand(world, var2, var4, var6, par1Packet23VehicleSpawn.k & 0xFFF, par1Packet23VehicleSpawn.k >> 16);
    if ((par1Packet23VehicleSpawn.k >> 15 & 0x1) == 1) {
      var8.d(60);
    }
    var8.slideDir = ((byte)(par1Packet23VehicleSpawn.k >> 12 & 0x7));
    return var8;
  }
  
  public static Packet23VehicleSpawn spawnFallingSandPacket(EntityFallingSand ent)
  {
    int burn = 0;
    if (ent.ae()) {
      burn = 32768;
    }
    int slide = ent.slideDir << 12;
    
    return new Packet23VehicleSpawn(ent, 70, ent.a | ent.b << 16 | burn | slide);
  }
  
  public static int readFallingSandID(NBTTagCompound nbt)
  {
    int bid;
    int bid;
    if (nbt.b("TileID"))
    {
      bid = nbt.e("TileID");
    }
    else
    {
      int bid;
      if (nbt.b("BlockID")) {
        bid = nbt.d("BlockID");
      } else {
        bid = nbt.c("Tile") & 0xFF;
      }
    }
    if ((bid < 1) || (bid > 4095)) {
      bid = 3;
    }
    return bid;
  }
  
  public static double bSpeedR(double speed)
  {
    return (int)(speed * 8000.0D) / 8000.0D;
  }
  
  public static void moveEntity(World world, EntityFallingSand fsand, double par1, double par3, double par5)
  {
    double d11;
    if (fsand.Z)
    {
      fsand.E.d(par1, par3, par5);
      fsand.u = ((fsand.E.a + fsand.E.d) / 2.0D);
      fsand.v = (fsand.E.b + fsand.N - fsand.X);
      fsand.w = ((fsand.E.c + fsand.E.f) / 2.0D);
    }
    else
    {
      fsand.X *= 0.4F;
      double d3 = fsand.u;
      double d4 = fsand.v;
      double d5 = fsand.w;
      
      double d6 = par1;
      double d7 = par3;
      double d8 = par5;
      AxisAlignedBB axisalignedbb = fsand.E.c();
      
      List list = world.a(fsand, fsand.E.a(par1, par3, par5));
      for (int i = 0; i < list.size(); i++) {
        par3 = ((AxisAlignedBB)list.get(i)).b(fsand.E, par3);
      }
      fsand.E.d(0.0D, par3, 0.0D);
      if ((!fsand.L) && (d7 != par3))
      {
        par5 = 0.0D;
        par3 = 0.0D;
        par1 = 0.0D;
      }
      boolean flag1 = (fsand.F) || ((d7 != par3) && (d7 < 0.0D));
      for (int j = 0; j < list.size(); j++) {
        par1 = ((AxisAlignedBB)list.get(j)).a(fsand.E, par1);
      }
      fsand.E.d(par1, 0.0D, 0.0D);
      if ((!fsand.L) && (d6 != par1))
      {
        par5 = 0.0D;
        par3 = 0.0D;
        par1 = 0.0D;
      }
      for (j = 0; j < list.size(); j++) {
        par5 = ((AxisAlignedBB)list.get(j)).c(fsand.E, par5);
      }
      fsand.E.d(0.0D, 0.0D, par5);
      if ((!fsand.L) && (d8 != par5))
      {
        par5 = 0.0D;
        par3 = 0.0D;
        par1 = 0.0D;
      }
      if ((fsand.Y > 0.0F) && (flag1) && (fsand.X < 0.05F) && ((d6 != par1) || (d8 != par5)))
      {
        double d12 = par1;
        double d10 = par3;
        double d11 = par5;
        par1 = d6;
        par3 = fsand.Y;
        par5 = d8;
        AxisAlignedBB axisalignedbb1 = fsand.E.c();
        fsand.E.d(axisalignedbb);
        list = world.a(fsand, fsand.E.a(d6, par3, d8));
        for (int k = 0; k < list.size(); k++) {
          par3 = ((AxisAlignedBB)list.get(k)).b(fsand.E, par3);
        }
        fsand.E.d(0.0D, par3, 0.0D);
        if ((!fsand.L) && (d7 != par3))
        {
          par5 = 0.0D;
          par3 = 0.0D;
          par1 = 0.0D;
        }
        for (k = 0; k < list.size(); k++) {
          par1 = ((AxisAlignedBB)list.get(k)).a(fsand.E, par1);
        }
        fsand.E.d(par1, 0.0D, 0.0D);
        if ((!fsand.L) && (d6 != par1))
        {
          par5 = 0.0D;
          par3 = 0.0D;
          par1 = 0.0D;
        }
        for (k = 0; k < list.size(); k++) {
          par5 = ((AxisAlignedBB)list.get(k)).c(fsand.E, par5);
        }
        fsand.E.d(0.0D, 0.0D, par5);
        if ((!fsand.L) && (d8 != par5))
        {
          par5 = 0.0D;
          par3 = 0.0D;
          par1 = 0.0D;
        }
        if ((!fsand.L) && (d7 != par3))
        {
          par5 = 0.0D;
          par3 = 0.0D;
          par1 = 0.0D;
        }
        else
        {
          par3 = -fsand.Y;
          for (k = 0; k < list.size(); k++) {
            par3 = ((AxisAlignedBB)list.get(k)).b(fsand.E, par3);
          }
          fsand.E.d(0.0D, par3, 0.0D);
        }
        if (d12 * d12 + d11 * d11 >= par1 * par1 + par5 * par5)
        {
          par1 = d12;
          par3 = d10;
          par5 = d11;
          fsand.E.d(axisalignedbb1);
        }
      }
      fsand.u = ((fsand.E.a + fsand.E.d) / 2.0D);
      fsand.v = (fsand.E.b + fsand.N - fsand.X);
      fsand.w = ((fsand.E.c + fsand.E.f) / 2.0D);
      fsand.G = ((d6 != par1) || (d8 != par5));
      fsand.H = (d7 != par3);
      fsand.F = ((d7 != par3) && (d7 < 0.0D));
      fsand.I = ((fsand.G) || (fsand.H));
      if (d6 != par1) {
        fsand.x = 0.0D;
      }
      if (d7 != par3) {
        fsand.y = 0.0D;
      }
      if (d8 != par5) {
        fsand.z = 0.0D;
      }
      double d12 = fsand.u - d3;
      double d10 = fsand.v - d4;
      d11 = fsand.w - d5;
    }
  }
  
  public static void dropItemsNBT(World world, int i, int j, int k, NBTTagCompound tileEntityData)
  {
    if (tileEntityData == null) {
      return;
    }
    NBTTagList nbttaglist = tileEntityData.m("Items");
    for (int tl = 0; tl < nbttaglist.c(); tl++)
    {
      ItemStack itemstack = ItemStack.a((NBTTagCompound)nbttaglist.b(tl));
      if (itemstack != null)
      {
        float f = rand.nextFloat() * 0.8F + 0.1F;
        float f1 = rand.nextFloat() * 0.8F + 0.1F;
        EntityItem entityitem;
        for (float f2 = rand.nextFloat() * 0.8F + 0.1F; itemstack.b > 0; world.d(entityitem))
        {
          int k1 = rand.nextInt(21) + 10;
          if (k1 > itemstack.b) {
            k1 = itemstack.b;
          }
          itemstack.b -= k1;
          entityitem = new EntityItem(world, i + f, j + f1, k + f2, new ItemStack(itemstack.d, k1, itemstack.k()));
          if (itemstack.p()) {
            entityitem.d().d((NBTTagCompound)itemstack.q().b());
          }
        }
      }
    }
  }
  
  public static void dropFallingSand(EntityFallingSand fsand)
  {
    if (fsand.e != null) {
      dropItemsNBT(fsand.q, MathHelper.c(fsand.u), MathHelper.c(fsand.v), MathHelper.c(fsand.w), fsand.e);
    }
    fsand.a(new ItemStack(fsand.a, 1, Block.s[fsand.a].a(fsand.b)), 0.0F);
  }
  
  public static int getBlockBPdata(World world, int par1, int par2, int par3)
  {
    if ((par1 >= -30000000) && (par3 >= -30000000) && (par1 < 30000000) && (par3 < 30000000))
    {
      if (par2 < 0) {
        return 0;
      }
      if (par2 >= 256) {
        return 0;
      }
      Chunk chunk = world.e(par1 >> 4, par3 >> 4);
      par1 &= 0xF;
      par3 &= 0xF;
      return chunk.getBlockBPdata(par1, par2, par3);
    }
    return 0;
  }
  
  public static boolean setBlockBPdata(World world, int par1, int par2, int par3, int par4)
  {
    if ((par1 >= -30000000) && (par3 >= -30000000) && (par1 < 30000000) && (par3 < 30000000))
    {
      if (par2 < 0) {
        return false;
      }
      if (par2 >= 256) {
        return false;
      }
      Chunk chunk = world.e(par1 >> 4, par3 >> 4);
      int j1 = par1 & 0xF;
      int k1 = par3 & 0xF;
      return chunk.setBlockBPdata(j1, par2, k1, par4);
    }
    return false;
  }
  
  public static boolean sameBlock(int id1, int meta1, int id2, int meta2)
  {
    if (id1 != id2) {
      return false;
    }
    if ((id1 > 173) || (id1 == 43) || (id1 == 44) || (id1 == 97)) {
      if (meta1 != meta2) {
        return false;
      }
    }
    return true;
  }
}