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

import net.minecraft.world.Explosion;

public class ExplosionQueue
{
	private ArrayList explosionQueue;
	private int xinterv;
	
	public ExplosionQueue()
    {
        explosionQueue = new ArrayList();
    }
    
	public void add( Explosion expl)
    {
		if (explosionQueue.size() >= BlockPhysics.explosionQueue) 
    	{
    		//BlockPhysics.writetoLog("Skipping explosion...");
    		return;
    	}
    	explosionQueue.add(expl);
    }
	
	public void doNextExplosion()
    {
		xinterv++;
		if (xinterv < 0) xinterv = 1000;
		if ( explosionQueue.isEmpty() ) return;
		if ( xinterv < BlockPhysics.explosionInterval) return;
		xinterv = 0;
		Explosion explosion = (Explosion)explosionQueue.remove(0);
		explosion.doExplosionA();
		explosion.doExplosionB(true);
    }
	  

	public int getSize()
	{
		if (explosionQueue == null) return 0;
		return explosionQueue.size();
	}
	
	public void reset() 
	{
		explosionQueue.clear();		
	}
}