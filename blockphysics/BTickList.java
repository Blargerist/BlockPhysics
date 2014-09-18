package blockphysics;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import java.util.ArrayList;

public class BTickList
{
  private ArrayList bticklist;
  
  public BTickList()
  {
    this.bticklist = new ArrayList();
  }
  
  public void scheduleBlockMoveUpdate(World world, int par1, int par2, int par3, int par4, int meta, boolean par5)
  {
    if (BlockPhysics.blockSet[par4][meta].movenum == 0) {
      return;
    }
    if (this.bticklist.size() >= 10000) {
      return;
    }
    byte var7 = 8;
    if (world.e(par1 - var7, par2 - var7, par3 - var7, par1 + var7, par2 + var7, par3 + var7))
    {
      BTickListEntry var6 = new BTickListEntry(par1, par2, par3, par5, BlockPhysics.blockSet[par4][meta].tickrate + world.N().g());
      this.bticklist.add(var6);
    }
  }
  
  public void tickMoveUpdates(World world)
  {
    if (BlockPhysics.skipMove) {
      return;
    }
    int siz = getSize();
    if (siz == 0) {
      return;
    }
    if (siz > 1000) {
      siz = 1000;
    }
    for (int var3 = 0; var3 < siz; var3++)
    {
      BTickListEntry var4 = (BTickListEntry)this.bticklist.remove(0);
      if (var4.scheduledTime <= world.N().g() + 60L)
      {
        ChunkCoordIntPair chunk = new ChunkCoordIntPair(var4.xCoord / 16, var4.zCoord / 16);
        
        BlockPhysics.tryToMove(world, var4.xCoord, var4.yCoord, var4.zCoord, world.a(var4.xCoord, var4.yCoord, var4.zCoord), world.h(var4.xCoord, var4.yCoord, var4.zCoord), var4.slide);
      }
    }
  }
  
  public int getSize()
  {
    if (this.bticklist == null) {
      return 0;
    }
    return this.bticklist.size();
  }
  
  public void reset()
  {
    this.bticklist.clear();
  }
}