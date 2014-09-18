package blockphysics;

public class BTickListEntry
  implements Comparable
{
  public int xCoord;
  public int yCoord;
  public int zCoord;
  public boolean slide;
  public long scheduledTime;
  
  public BTickListEntry(int par1, int par2, int par3, boolean par4, long par5)
  {
    this.xCoord = par1;
    this.yCoord = par2;
    this.zCoord = par3;
    this.slide = par4;
    this.scheduledTime = par5;
  }
  
  public boolean equals(Object par1Obj)
  {
    if (!(par1Obj instanceof BTickListEntry)) {
      return false;
    }
    BTickListEntry var2 = (BTickListEntry)par1Obj;
    return (this.xCoord == var2.xCoord) && (this.yCoord == var2.yCoord) && (this.zCoord == var2.zCoord);
  }
  
  public int hashCode()
  {
    return this.xCoord * 1024 * 1024 + this.zCoord * 1024 + this.yCoord;
  }
  
  public int comparer(BTickListEntry par1BlockPhysicsTickListEntry)
  {
    return hashCode() > par1BlockPhysicsTickListEntry.hashCode() ? 1 : hashCode() < par1BlockPhysicsTickListEntry.hashCode() ? -1 : this.scheduledTime > par1BlockPhysicsTickListEntry.scheduledTime ? 1 : this.scheduledTime < par1BlockPhysicsTickListEntry.scheduledTime ? -1 : 0;
  }
  
  public int compareTo(Object par1Obj)
  {
    return comparer((BTickListEntry)par1Obj);
  }
}