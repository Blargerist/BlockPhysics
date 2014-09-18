package blockphysics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.NetHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.NetServerHandler;
import net.minecraft.server.MinecraftServer;

public class BPacketHandler
  implements IConnectionHandler, IPacketHandler
{
  public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player playerEntity)
  {
    if ((playerEntity instanceof EntityPlayerMP))
    {
      if (packet.a.equals("BlockPhysics0001")) {
        if (packet.c[0] == 0)
        {
          Packet250CustomPayload packet2 = new Packet250CustomPayload();
          packet2.a = "BlockPhysics0000";
          packet2.c = BlockPhysics.cConf.toByteArray();
          packet2.b = packet2.c.length;
          PacketDispatcher.sendPacketToPlayer(packet2, playerEntity);
          BlockPhysics.writetoLog("Configuration sent to player: " + ((EntityPlayerMP)playerEntity).bu, 1);
        }
        else
        {
          ((EntityPlayerMP)playerEntity).a.c("BlockPhysics configuration sync error, try to connect again!");
          BlockPhysics.writetoLog("Error sending configuration to player: " + ((EntityPlayerMP)playerEntity).bu + " Disconnecting...", 0);
        }
      }
      if (packet.a.equals("BlockPhysics0002")) {
        if (packet.c[0] == 0)
        {
          File cf;
          File cf;
          if (MinecraftServer.F().V()) {
            cf = new File(BlockPhysicsUtil.confDir(), "blockphysics.cfg");
          } else {
            cf = new File(BlockPhysics.gameDir + File.separator + "saves" + File.separator + Minecraft.w().C().L() + File.separator + "blockphysics.cfg");
          }
          byte[] conf = new byte[packet.c.length - 1];
          System.arraycopy(packet.c, 1, conf, 0, packet.c.length - 1);
          
          BlockPhysicsUtil.resetConfig();
          if (BlockPhysicsUtil.loadConfig(conf))
          {
            boolean succ = true;
            try
            {
              BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(conf)));
              BufferedWriter bw = new BufferedWriter(new FileWriter(cf, false));
              for (String s = ""; (s = br.readLine()) != null;)
              {
                bw.write(s);
                bw.newLine();
              }
              br.close();
              bw.close();
            }
            catch (IOException e)
            {
              succ = false;
            }
            if (succ)
            {
              BlockPhysics.writetoLog(((EntityPlayerMP)playerEntity).c_() + " successfully uploaded a new config.", 1);
              ((EntityPlayerMP)playerEntity).a("Configuration upload successfull.");
              Packet250CustomPayload packet2 = new Packet250CustomPayload();
              packet2.a = "BlockPhysics0001";
              packet2.c = BlockPhysics.cConfmd5.getBytes();
              packet2.b = packet2.c.length;
              PacketDispatcher.sendPacketToAllPlayers(packet2);
              BlockPhysics.writetoLog("Configuration md5 sent to all players.", 1);
            }
            else
            {
              BlockPhysics.writetoLog("Can not save config file.", 0);
              ((EntityPlayerMP)playerEntity).a("Configuration upload failed.");
              if (!cf.exists())
              {
                ((EntityPlayerMP)playerEntity).a("BlockPhysics configuration loading error, can't find: " + cf);
                return;
              }
              BlockPhysicsUtil.resetConfig();
              BlockPhysicsUtil.loadConfig(cf);
              
              Packet250CustomPayload packet2 = new Packet250CustomPayload();
              packet2.a = "BlockPhysics0001";
              packet2.c = BlockPhysics.cConfmd5.getBytes();
              packet2.b = packet2.c.length;
              PacketDispatcher.sendPacketToAllPlayers(packet2);
              BlockPhysics.writetoLog("Configuration md5 sent to all players.", 1);
              

              ((EntityPlayerMP)playerEntity).a("BlockPhysics configuration loaded from: " + cf);
            }
          }
          else
          {
            BlockPhysics.writetoLog(((EntityPlayerMP)playerEntity).c_() + "'s config uploaded failed.", 0);
            ((EntityPlayerMP)playerEntity).a("Configuration upload failed.");
            if (!cf.exists())
            {
              ((EntityPlayerMP)playerEntity).a("BlockPhysics configuration loading error, can't find: " + cf);
              return;
            }
            BlockPhysicsUtil.resetConfig();
            BlockPhysicsUtil.loadConfig(cf);
            
            Packet250CustomPayload packet2 = new Packet250CustomPayload();
            packet2.a = "BlockPhysics0001";
            packet2.c = BlockPhysics.cConfmd5.getBytes();
            packet2.b = packet2.c.length;
            PacketDispatcher.sendPacketToAllPlayers(packet2);
            BlockPhysics.writetoLog("Configuration md5 sent to all players.", 1);
            

            ((EntityPlayerMP)playerEntity).a("BlockPhysics configuration loaded from: " + cf);
          }
        }
      }
    }
    else if (!Minecraft.w().A())
    {
      if (packet.a.equals("BlockPhysics0000"))
      {
        if (BlockPhysicsUtil.md5Sum(packet.c).equals(BlockPhysics.cConfmd5))
        {
          BlockPhysicsUtil.resetConfig();
          BlockPhysicsUtil.loadConfig(packet.c);
          
          File conf = new File(BlockPhysics.gameDir, "config" + File.separator + "blockphysics" + File.separator + "servers");
          if (!conf.exists()) {
            conf.mkdir();
          }
          conf = new File(conf, BlockPhysics.cConfmd5 + ".cfg");
          try
          {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(packet.c)));
            BufferedWriter bw = new BufferedWriter(new FileWriter(conf, false));
            for (String s = ""; (s = br.readLine()) != null;)
            {
              bw.write(s);
              bw.newLine();
            }
            br.close();
            bw.close();
          }
          catch (IOException e) {}
        }
        else
        {
          BlockPhysics.writetoLog("Received inconsistent configuration!", 0);
          Packet250CustomPayload packet2 = new Packet250CustomPayload();
          packet2.a = "BlockPhysics0001";
          packet2.c = new byte[] { 1 };
          packet2.b = packet2.c.length;
          PacketDispatcher.sendPacketToServer(packet2);
        }
      }
      else if (packet.a.equals("BlockPhysics0001"))
      {
        String cmd5 = new String(packet.c);
        if (cmd5.equals("null"))
        {
          BlockPhysicsUtil.resetConfig();
          BlockPhysics.cConf.reset();
          BlockPhysics.cConfmd5 = "null";
          BlockPhysics.writetoLog("Physics OFF.", 1);
          return;
        }
        File conf = new File(BlockPhysics.gameDir, "config" + File.separator + "blockphysics" + File.separator + "servers" + File.separator + cmd5 + ".cfg");
        BlockPhysicsUtil.resetConfig();
        if ((!conf.exists()) || (!BlockPhysicsUtil.loadConfig(conf)) || (!BlockPhysics.cConfmd5.equals(cmd5)))
        {
          BlockPhysicsUtil.resetConfig();
          
          BlockPhysics.cConfmd5 = cmd5;
          Packet250CustomPayload packet2 = new Packet250CustomPayload();
          packet2.a = "BlockPhysics0001";
          packet2.c = new byte[] { 0 };
          packet2.b = packet2.c.length;
          PacketDispatcher.sendPacketToServer(packet2);
          BlockPhysics.writetoLog("Requesting configuration.", 1);
        }
      }
      if (packet.a.equals("BlockPhysics0002")) {
        if (packet.c[0] == 0)
        {
          File cf = new File(BlockPhysicsUtil.confDir(), "blockphysics.upload.cfg");
          if (cf.exists())
          {
            BlockPhysicsUtil.loadConfig(cf);
            
            byte[] conf = BlockPhysics.cConf.toByteArray();
            
            Packet250CustomPayload packet2 = new Packet250CustomPayload();
            packet2.a = "BlockPhysics0002";
            
            packet2.c = new byte[conf.length + 1];
            
            System.arraycopy(new byte[] { 0 }, 0, packet2.c, 0, 1);
            System.arraycopy(conf, 0, packet2.c, 1, conf.length);
            
            packet2.b = packet2.c.length;
            PacketDispatcher.sendPacketToServer(packet2);
            BlockPhysics.writetoLog("Uploading configuration.", 1);
            
            ((EntityPlayerSP)playerEntity).a("Uploading : " + BlockPhysicsUtil.confDir() + "\\blockphysics.upload.cfg");
          }
          else
          {
            ((EntityPlayerSP)playerEntity).a("Can't find: " + BlockPhysicsUtil.confDir() + "\\blockphysics.upload.cfg");
          }
        }
      }
    }
  }
  
  public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
  {
    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.a = "BlockPhysics0001";
    packet.c = BlockPhysics.cConfmd5.getBytes();
    packet.b = packet.c.length;
    PacketDispatcher.sendPacketToPlayer(packet, player);
    BlockPhysics.writetoLog("Configuration md5 sent to player: " + ((EntityPlayerMP)player).bu, 1);
  }
  
  public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
  {
    return null;
  }
  
  public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {}
  
  public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {}
  
  public void connectionClosed(INetworkManager manager) {}
  
  public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {}
}