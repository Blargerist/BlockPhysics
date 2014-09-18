package blockphysics;

import net.minecraft.command.ICommandSender;
import net.minecraft.world.WorldProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.packet.Packet250CustomPayload;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import net.minecraft.entity.EntityTracker;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.CommandBase;

public class BCommands
  extends CommandBase
{
  public int a()
  {
    return 1;
  }
  
  public String c(ICommandSender par1ICommandSender)
  {
    return "/" + c();
  }
  
  public List b()
  {
    return null;
  }
  
  public String c()
  {
    return "bphys";
  }
  
  public void b(ICommandSender icommandsender, String[] astring)
  {
    if (astring.length == 0)
    {
      ((EntityPlayerMP)icommandsender).a("/bphys off        swich off physics");
      ((EntityPlayerMP)icommandsender).a("/bphys on         reload config and switch on physics");
      ((EntityPlayerMP)icommandsender).a("/bphys confup   upload config");
      ((EntityPlayerMP)icommandsender).a("/bphys stat       list statistics");
    }
    else if (astring[0].equals("on"))
    {
      File cf;
      File cf;
      if (MinecraftServer.F().V()) {
        cf = new File(BlockPhysicsUtil.confDir(), "blockphysics.cfg");
      } else {
        cf = new File(BlockPhysics.gameDir + File.separator + "saves" + File.separator + Minecraft.w().C().L() + File.separator + "blockphysics.cfg");
      }
      if (!cf.exists())
      {
        ((EntityPlayerMP)icommandsender).a("BlockPhysics configuration loading error, can't find: " + cf);
        BlockPhysics.writetoLog("BlockPhysics configuration loading error, can't find: " + cf, 0);
        return;
      }
      BlockPhysicsUtil.resetConfig();
      BlockPhysicsUtil.loadConfig(cf);
      
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.a = "BlockPhysics0001";
      packet.c = BlockPhysics.cConfmd5.getBytes();
      packet.b = packet.c.length;
      PacketDispatcher.sendPacketToAllPlayers(packet);
      BlockPhysics.writetoLog("Configuration md5 sent to all players.", 1);
      

      ((EntityPlayerMP)icommandsender).a("BlockPhysics configuration loaded from: " + cf);
    }
    else if (astring[0].equals("off"))
    {
      for (int i = 0; i < MinecraftServer.F().b.length; i++)
      {
        MinecraftServer.F().b[i].moveTickList.reset();
        MinecraftServer.F().b[i].explosionQueue.reset();
      }
      BlockPhysicsUtil.resetConfig();
      BlockPhysics.cConf.reset();
      BlockPhysics.cConfmd5 = "null";
      
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.a = "BlockPhysics0001";
      packet.c = BlockPhysics.cConfmd5.getBytes();
      packet.b = packet.c.length;
      PacketDispatcher.sendPacketToAllPlayers(packet);
      BlockPhysics.writetoLog("Physics OFF. Configuration off sent to all player.", 1);
      ((EntityPlayerMP)icommandsender).a("BlockPhysics OFF");
    }
    else if (astring[0].equals("confup"))
    {
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.a = "BlockPhysics0002";
      packet.c = new byte[] { 0 };
      packet.b = packet.c.length;
      PacketDispatcher.sendPacketToPlayer(packet, (Player)icommandsender);
    }
    else if (astring[0].equals("stat"))
    {
      if (astring.length == 1)
      {
        String lastv = "";
        ((EntityPlayerMP)icommandsender).a(" ");
        if (BlockPhysics.lastversion > 1000800) {
          lastv = new String(" New version available: " + BlockPhysicsUtil.versiontostring(BlockPhysics.lastversion));
        }
        ((EntityPlayerMP)icommandsender).a("BlockPhysics version: " + BlockPhysicsUtil.versiontostring(1000800) + lastv);
        if (BlockPhysics.cConfmd5 == "null") {
          ((EntityPlayerMP)icommandsender).a("Physics are switched OFF.");
        } else {
          ((EntityPlayerMP)icommandsender).a("Configuration loaded.");
        }
        if (BlockPhysics.skipMove == true) {
          ((EntityPlayerMP)icommandsender).a("Server overload (physics not working)");
        } else {
          ((EntityPlayerMP)icommandsender).a("Server normal.");
        }
        ((EntityPlayerMP)icommandsender).a("Explosion queue /world: " + BlockPhysics.explosionQueue + ".  Ticks between expl.: " + BlockPhysics.explosionInterval);
        if (BlockPhysics.warnerr) {
          ((EntityPlayerMP)icommandsender).a("There was at least one WARNING message since the startup!");
        } else {
          ((EntityPlayerMP)icommandsender).a("No warnings in the log.");
        }
        int movetick = 0;
        int movingb = 0;
        int expl = 0;
        
        String worlds = "Dimensions: ";
        for (int i = 0; i < MinecraftServer.F().b.length; i++)
        {
          worlds = worlds.concat("" + MinecraftServer.F().b[i].t.i + ", ");
          movetick += MinecraftServer.F().b[i].moveTickList.getSize();
          movingb += MinecraftServer.F().b[i].q().movingblocks;
          expl += MinecraftServer.F().b[i].explosionQueue.getSize();
        }
        ((EntityPlayerMP)icommandsender).a(worlds);
        ((EntityPlayerMP)icommandsender).a("Moving blocks:           " + movingb + " /" + BlockPhysics.maxmovingblocks * i);
        ((EntityPlayerMP)icommandsender).a("Blocks waiting for tick: " + movetick + " /" + 10000 * i);
        ((EntityPlayerMP)icommandsender).a("Queued explosions:       " + expl + " /" + BlockPhysics.explosionQueue * i);
        
        ((EntityPlayerMP)icommandsender).a("Dimension stat: /bphys stat <dimension num.>");
      }
      else if (astring.length > 1)
      {
        try
        {
          int dim = Integer.parseInt(astring[1]);
          boolean found = false;
          for (int i = 0; i < MinecraftServer.F().b.length; i++) {
            if (MinecraftServer.F().b[i].t.i == dim)
            {
              found = true;
              break;
            }
          }
          if (found)
          {
            ((EntityPlayerMP)icommandsender).a(" ");
            ((EntityPlayerMP)icommandsender).a(MinecraftServer.F().b[i].t.l() + " (" + dim + ")");
            ((EntityPlayerMP)icommandsender).a("Moving blocks:           " + MinecraftServer.F().b[i].q().movingblocks + " /" + BlockPhysics.maxmovingblocks);
            ((EntityPlayerMP)icommandsender).a("Blocks waiting for tick: " + MinecraftServer.F().b[i].moveTickList.getSize() + " /" + 10000);
            ((EntityPlayerMP)icommandsender).a("Queued explosions:       " + MinecraftServer.F().b[i].explosionQueue.getSize() + " /" + BlockPhysics.explosionQueue);
          }
          else
          {
            ((EntityPlayerMP)icommandsender).a("Dimesion: " + dim + " not found.");
          }
        }
        catch (Exception e)
        {
          ((EntityPlayerMP)icommandsender).a("Usage: /bphys stat <dimension num.> ( For the OverWorld: /bphys stat 0 )");
        }
      }
    }
    else
    {
      ((EntityPlayerMP)icommandsender).a("/bphys off        swich off physics");
      ((EntityPlayerMP)icommandsender).a("/bphys on         reload config and switch on physics");
      ((EntityPlayerMP)icommandsender).a("/bphys confup   upload config");
      ((EntityPlayerMP)icommandsender).a("/bphys stat       list statistics");
    }
  }
}