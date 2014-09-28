package blockphysics;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.WorldEvent.Unload;

public class WorldUnloadHook
{
  @ForgeSubscribe
  public void worldUnload(WorldEvent.Unload event)
  {
    event.world.moveTickList = null;
    event.world.pistonMoveBlocks = null;
    event.world.explosionQueue = null;
  }
}