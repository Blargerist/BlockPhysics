package blockphysics;

import net.minecraft.world.Explosion;
import java.util.ArrayList;

public class ExplosionQueue
{
  private ArrayList explosionQueue;
  private int xinterv;
  
  public ExplosionQueue()
  {
    this.explosionQueue = new ArrayList();
  }
  
  public void add(Explosion expl)
  {
    if (this.explosionQueue.size() >= BlockPhysics.explosionQueue) {
      return;
    }
    this.explosionQueue.add(expl);
  }
  
  public void doNextExplosion()
  {
    this.xinterv += 1;
    if (this.xinterv < 0) {
      this.xinterv = 1000;
    }
    if (this.explosionQueue.isEmpty()) {
      return;
    }
    if (this.xinterv < BlockPhysics.explosionInterval) {
      return;
    }
    this.xinterv = 0;
    Explosion explosion = (Explosion)this.explosionQueue.remove(0);
    explosion.doExplosionA();
    explosion.doExplosionB(true);
  }
  
  public int getSize()
  {
    if (this.explosionQueue == null) {
      return 0;
    }
    return this.explosionQueue.size();
  }
  
  public void reset()
  {
    this.explosionQueue.clear();
  }
}