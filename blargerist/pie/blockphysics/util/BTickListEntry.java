package blargerist.pie.blockphysics.util;

public class BTickListEntry implements Comparable
{
   	/** X position this tick is occuring at */
    public int xCoord;

    /** Y position this tick is occuring at */
    public int yCoord;

    /** Z position this tick is occuring at */
    public int zCoord;

    public boolean slide;

    /** Time this tick is scheduled to occur at */
    public long scheduledTime;

    public BTickListEntry(int par1, int par2, int par3, boolean par4, long par5)
    {
        	this.xCoord = par1;
            this.yCoord = par2;
            this.zCoord = par3;
            this.slide = par4;
            this.scheduledTime = par5;
    }

    @Override
	public boolean equals(Object par1Obj)
    {
        if (!(par1Obj instanceof BTickListEntry))
        {
            return false;
        }
        else
        {
        	BTickListEntry var2 = (BTickListEntry)par1Obj;
            return this.xCoord == var2.xCoord && this.yCoord == var2.yCoord && this.zCoord == var2.zCoord;
        }
    }

    @Override
	public int hashCode()
    {
        return this.xCoord * 1024 * 1024 + this.zCoord * 1024 + this.yCoord;
    }
    
    /**
     * Compares this tick entry to another tick entry for sorting purposes.
     */
    public int comparer(BTickListEntry par1BlockPhysicsTickListEntry)
    {
        return this.scheduledTime < par1BlockPhysicsTickListEntry.scheduledTime ? -1 : (this.scheduledTime > par1BlockPhysicsTickListEntry.scheduledTime ? 1 : (this.hashCode() < par1BlockPhysicsTickListEntry.hashCode() ? -1 : (this.hashCode() > par1BlockPhysicsTickListEntry.hashCode() ? 1 : 0)));
    }

    @Override
	public int compareTo(Object par1Obj)
    {
        return this.comparer((BTickListEntry)par1Obj);
    }
}