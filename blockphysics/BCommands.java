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
	@Override
  public int getRequiredPermissionLevel()
  {
    return 1;
  }
  
	@Override
  public String getCommandUsage(ICommandSender par1ICommandSender)
  {
    return "/" + getCommandName();
  }
	
  @Override
  public List getCommandAliases()
  {
    return null;
  }
  
  @Override
  public String getCommandName()
  {
    return "bphys";
  }
  
  @Override
  public void processCommand(ICommandSender icommandsender, String[] astring)
  {
    if (astring.length == 0)
    {
      ((EntityPlayerMP)icommandsender).addChatMessage("/bphys off        swich off physics");
      ((EntityPlayerMP)icommandsender).addChatMessage("/bphys on         reload config and switch on physics");
      ((EntityPlayerMP)icommandsender).addChatMessage("/bphys confup   upload config");
      ((EntityPlayerMP)icommandsender).addChatMessage("/bphys stat       list statistics");
    }
    else if (astring[0].equals("on"))
    {
      File cf;
      if (MinecraftServer.getServer().isDedicatedServer()) {
        cf = new File(BlockPhysicsUtil.confDir(), "blockphysics.cfg");
      } else {
        cf = new File(BlockPhysics.gameDir + File.separator + "saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName() + File.separator + "blockphysics.cfg");
      }
      if (!cf.exists())
      {
        ((EntityPlayerMP)icommandsender).addChatMessage("BlockPhysics configuration loading error, can't find: " + cf);
        BlockPhysics.writetoLog("BlockPhysics configuration loading error, can't find: " + cf, 0);
        return;
      }
      BlockPhysicsUtil.resetConfig();
      BlockPhysicsUtil.loadConfig(cf);
      
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.channel = "BlockPhysics0001";
      packet.data = BlockPhysics.cConfmd5.getBytes();
      packet.length = packet.data.length;
      PacketDispatcher.sendPacketToAllPlayers(packet);
      BlockPhysics.writetoLog("Configuration md5 sent to all players.", 1);
      

      ((EntityPlayerMP)icommandsender).addChatMessage("BlockPhysics configuration loaded from: " + cf);
    }
    else if (astring[0].equals("off"))
    {
      for (int i = 0; i < MinecraftServer.getServer().worldServers.length; i++)
      {
        MinecraftServer.getServer().worldServers[i].moveTickList.reset();
        MinecraftServer.getServer().worldServers[i].explosionQueue.reset();
      }
      BlockPhysicsUtil.resetConfig();
      BlockPhysics.cConf.reset();
      BlockPhysics.cConfmd5 = "null";
      
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.channel = "BlockPhysics0001";
      packet.data = BlockPhysics.cConfmd5.getBytes();
      packet.length = packet.data.length;
      PacketDispatcher.sendPacketToAllPlayers(packet);
      BlockPhysics.writetoLog("Physics OFF. Configuration off sent to all player.", 1);
      ((EntityPlayerMP)icommandsender).addChatMessage("BlockPhysics OFF");
    }
    else if (astring[0].equals("confup"))
    {
      Packet250CustomPayload packet = new Packet250CustomPayload();
      packet.channel = "BlockPhysics0002";
      packet.data = new byte[] { 0 };
      packet.length = packet.data.length;
      PacketDispatcher.sendPacketToPlayer(packet, (Player)icommandsender);
    }
    else if (astring[0].equals("stat"))
    {
      if (astring.length == 1)
      {
        String lastv = "";
        ((EntityPlayerMP)icommandsender).addChatMessage(" ");
        if (BlockPhysics.lastversion > 1000800) {
          lastv = new String(" New version available: " + BlockPhysicsUtil.versiontostring(BlockPhysics.lastversion));
        }
        ((EntityPlayerMP)icommandsender).addChatMessage("BlockPhysics version: " + BlockPhysicsUtil.versiontostring(1000800) + lastv);
        if (BlockPhysics.cConfmd5 == "null") {
          ((EntityPlayerMP)icommandsender).addChatMessage("Physics are switched OFF.");
        } else {
          ((EntityPlayerMP)icommandsender).addChatMessage("Configuration loaded.");
        }
        if (BlockPhysics.skipMove == true) {
          ((EntityPlayerMP)icommandsender).addChatMessage("Server overload (physics not working)");
        } else {
          ((EntityPlayerMP)icommandsender).addChatMessage("Server normal.");
        }
        ((EntityPlayerMP)icommandsender).addChatMessage("Explosion queue /world: " + BlockPhysics.explosionQueue + ".  Ticks between expl.: " + BlockPhysics.explosionInterval);
        if (BlockPhysics.warnerr) {
          ((EntityPlayerMP)icommandsender).addChatMessage("There was at least one WARNING message since the startup!");
        } else {
          ((EntityPlayerMP)icommandsender).addChatMessage("No warnings in the log.");
        }
        int movetick = 0;
        int movingb = 0;
        int expl = 0;
        
        String worlds = "Dimensions: ";
        for (int i = 0; i < MinecraftServer.getServer().worldServers.length; i++)
        {
          worlds = worlds.concat("" + MinecraftServer.getServer().worldServers[i].t.i + ", ");
          movetick += MinecraftServer.getServer().worldServers[i].moveTickList.getSize();
          movingb += MinecraftServer.getServer().worldServers[i].getEntityTracker().movingblocks;
          expl += MinecraftServer.getServer().worldServers[i].explosionQueue.getSize();
        }
        ((EntityPlayerMP)icommandsender).addChatMessage(worlds);
        ((EntityPlayerMP)icommandsender).addChatMessage("Moving blocks:           " + movingb + " /" + BlockPhysics.maxmovingblocks * i);
        ((EntityPlayerMP)icommandsender).addChatMessage("Blocks waiting for tick: " + movetick + " /" + 10000 * i);
        ((EntityPlayerMP)icommandsender).addChatMessage("Queued explosions:       " + expl + " /" + BlockPhysics.explosionQueue * i);
        
        ((EntityPlayerMP)icommandsender).addChatMessage("Dimension stat: /bphys stat <dimension num.>");
      }
      else if (astring.length > 1)
      {
        try
        {
          int dim = Integer.parseInt(astring[1]);
          boolean found = false;
          for (int i = 0; i < MinecraftServer.getServer().worldServers.length; i++) {
            if (MinecraftServer.getServer().worldServers[i].provider.dimensionId == dim)
            {
              found = true;
              break;
            }
          }
          if (found)
          {
            ((EntityPlayerMP)icommandsender).addChatMessage(" ");
            ((EntityPlayerMP)icommandsender).addChatMessage(MinecraftServer.getServer().worldServers[i].provider.getDimensionName() + " (" + dim + ")");
            ((EntityPlayerMP)icommandsender).addChatMessage("Moving blocks:           " + MinecraftServer.getServer().worldServers[i].getEntityTracker().movingblocks + " /" + BlockPhysics.maxmovingblocks);
            ((EntityPlayerMP)icommandsender).addChatMessage("Blocks waiting for tick: " + MinecraftServer.getServer().worldServers[i].moveTickList.getSize() + " /" + 10000);
            ((EntityPlayerMP)icommandsender).addChatMessage("Queued explosions:       " + MinecraftServer.getServer().worldServers[i].explosionQueue.getSize() + " /" + BlockPhysics.explosionQueue);
          }
          else
          {
            ((EntityPlayerMP)icommandsender).addChatMessage("Dimesion: " + dim + " not found.");
          }
        }
        catch (Exception e)
        {
          ((EntityPlayerMP)icommandsender).addChatMessage("Usage: /bphys stat <dimension num.> ( For the OverWorld: /bphys stat 0 )");
        }
      }
    }
    else
    {
      ((EntityPlayerMP)icommandsender).addChatMessage("/bphys off        swich off physics");
      ((EntityPlayerMP)icommandsender).addChatMessage("/bphys on         reload config and switch on physics");
      ((EntityPlayerMP)icommandsender).addChatMessage("/bphys confup   upload config");
      ((EntityPlayerMP)icommandsender).addChatMessage("/bphys stat       list statistics");
    }
  }

@Override
public int compareTo(Object arg0)
{
	// TODO Auto-generated method stub
	return 0;
}
}