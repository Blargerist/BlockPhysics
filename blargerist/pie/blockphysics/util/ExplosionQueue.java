package blargerist.pie.blockphysics.util;

import java.util.ArrayList;
import blargerist.pie.blockphysics.ModConfig;
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
		if (explosionQueue.size() >= ModConfig.explosionQueue) 
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
		if ( xinterv < ModConfig.explosionInterval) return;
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