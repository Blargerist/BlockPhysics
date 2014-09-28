/*	Copyright 2013 Dénes Derhán
*
*	This file is part of BlockPhysics.
*
*	BlockPhysics is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	BlockPhysics is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*
*	You should have received a copy of the GNU General Public License
*	along with BlockPhysics.  If not, see <http://www.gnu.org/licenses/>.
*/

package blockphysics;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class BTickList
{
	private ArrayList bticklist;
	
	public BTickList()
    {
		bticklist  = new ArrayList();
    }
    
    public void scheduleBlockMoveUpdate(World world, int par1, int par2, int par3, int par4, int meta, boolean par5)
    {
    	if ( BlockPhysics.blockSet[par4][meta].movenum == 0 ) return;
    	
    	if (bticklist.size() >= 10000) return;
		
    	byte var7 = 8;
    	        
        if (world.checkChunksExist(par1 - var7, par2 - var7, par3 - var7, par1 + var7, par2 + var7, par3 + var7))
        {
        	BTickListEntry var6 = new BTickListEntry(par1, par2, par3, par5, (long)BlockPhysics.blockSet[par4][meta].tickrate + world.getWorldInfo().getWorldTime());
        	this.bticklist.add(var6);            
        }
    }
    
    public void tickMoveUpdates(World world)
    {
    	if ( BlockPhysics.skipMove ) return;
    	
    	int siz = getSize();
    	if ( siz == 0 ) return;
    	else if (siz > 1000) siz = 1000;

        ChunkCoordIntPair chunk; 
        
        for (int var3 = 0; var3 < siz; ++var3)
        {
            BTickListEntry var4 = (BTickListEntry)bticklist.remove(0);
                		
        	if (var4.scheduledTime > world.getWorldInfo().getWorldTime() + 60 ) continue;
                        
            chunk = new ChunkCoordIntPair(var4.xCoord / 16, var4.zCoord / 16);
                        	
            BlockPhysics.tryToMove(world, var4.xCoord, var4.yCoord, var4.zCoord, world.getBlockId(var4.xCoord, var4.yCoord, var4.zCoord), world.getBlockMetadata(var4.xCoord, var4.yCoord, var4.zCoord), var4.slide);
        }
    }

	public int getSize() 
	{
		if (bticklist == null) return 0;
		return bticklist.size();		
	}
	
	public void reset() 
	{
		bticklist.clear();		
	}
}
