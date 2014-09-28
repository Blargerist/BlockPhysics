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
      if (packet.channel.equals("BlockPhysics0001")) {
        if (packet.data[0] == 0)
        {
          Packet250CustomPayload packet2 = new Packet250CustomPayload();
          packet2.channel = "BlockPhysics0000";
          packet2.data = BlockPhysics.cConf.toByteArray();
          packet2.length = packet2.data.length;
          PacketDispatcher.sendPacketToPlayer(packet2, playerEntity);
          BlockPhysics.writetoLog("Configuration sent to player: " + ((EntityPlayerMP)playerEntity).username, 1);
        }
        else
        {
          ((EntityPlayerMP)playerEntity).playerNetServerHandler.kickPlayerFromServer("BlockPhysics configuration sync error, try to connect again!");
          BlockPhysics.writetoLog("Error sending configuration to player: " + ((EntityPlayerMP)playerEntity).username + " Disconnecting...", 0);
        }
      }
      if (packet.channel.equals("BlockPhysics0002")) {
        if (packet.data[0] == 0)
        {
          File cf;
          if (MinecraftServer.getServer().isDedicatedServer()) {
            cf = new File(BlockPhysicsUtil.confDir(), "blockphysics.cfg");
          } else {
            cf = new File(BlockPhysics.gameDir + File.separator + "saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName() + File.separator + "blockphysics.cfg");
          }
          byte[] conf = new byte[packet.data.length - 1];
          System.arraycopy(packet.data, 1, conf, 0, packet.data.length - 1);
          
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
              BlockPhysics.writetoLog(((EntityPlayerMP)playerEntity).getCommandSenderName() + " successfully uploaded a new config.", 1);
              ((EntityPlayerMP)playerEntity).addChatMessage("Configuration upload successfull.");
              Packet250CustomPayload packet2 = new Packet250CustomPayload();
              packet2.channel = "BlockPhysics0001";
              packet2.data = BlockPhysics.cConfmd5.getBytes();
              packet2.length = packet2.data.length;
              PacketDispatcher.sendPacketToAllPlayers(packet2);
              BlockPhysics.writetoLog("Configuration md5 sent to all players.", 1);
            }
            else
            {
              BlockPhysics.writetoLog("Can not save config file.", 0);
              ((EntityPlayerMP)playerEntity).addChatMessage("Configuration upload failed.");
              if (!cf.exists())
              {
                ((EntityPlayerMP)playerEntity).addChatMessage("BlockPhysics configuration loading error, can't find: " + cf);
                return;
              }
              BlockPhysicsUtil.resetConfig();
              BlockPhysicsUtil.loadConfig(cf);
              
              Packet250CustomPayload packet2 = new Packet250CustomPayload();
              packet2.channel = "BlockPhysics0001";
              packet2.data = BlockPhysics.cConfmd5.getBytes();
              packet2.length = packet2.data.length;
              PacketDispatcher.sendPacketToAllPlayers(packet2);
              BlockPhysics.writetoLog("Configuration md5 sent to all players.", 1);
              

              ((EntityPlayerMP)playerEntity).addChatMessage("BlockPhysics configuration loaded from: " + cf);
            }
          }
          else
          {
            BlockPhysics.writetoLog(((EntityPlayerMP)playerEntity).getCommandSenderName() + "'s config uploaded failed.", 0);
            ((EntityPlayerMP)playerEntity).addChatMessage("Configuration upload failed.");
            if (!cf.exists())
            {
              ((EntityPlayerMP)playerEntity).addChatMessage("BlockPhysics configuration loading error, can't find: " + cf);
              return;
            }
            BlockPhysicsUtil.resetConfig();
            BlockPhysicsUtil.loadConfig(cf);
            
            Packet250CustomPayload packet2 = new Packet250CustomPayload();
            packet2.channel = "BlockPhysics0001";
            packet2.data = BlockPhysics.cConfmd5.getBytes();
            packet2.length = packet2.data.length;
            PacketDispatcher.sendPacketToAllPlayers(packet2);
            BlockPhysics.writetoLog("Configuration md5 sent to all players.", 1);
            

            ((EntityPlayerMP)playerEntity).addChatMessage("BlockPhysics configuration loaded from: " + cf);
          }
        }
      }
    }
    else if (!Minecraft.getMinecraft().isIntegratedServerRunning())
    {
      if (packet.channel.equals("BlockPhysics0000"))
      {
        if (BlockPhysicsUtil.md5Sum(packet.data).equals(BlockPhysics.cConfmd5))
        {
          BlockPhysicsUtil.resetConfig();
          BlockPhysicsUtil.loadConfig(packet.data);
          
          File conf = new File(BlockPhysics.gameDir, "config" + File.separator + "blockphysics" + File.separator + "servers");
          if (!conf.exists()) {
            conf.mkdir();
          }
          conf = new File(conf, BlockPhysics.cConfmd5 + ".cfg");
          try
          {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(packet.data)));
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
          packet2.channel = "BlockPhysics0001";
          packet2.data = new byte[] { 1 };
          packet2.length = packet2.data.length;
          PacketDispatcher.sendPacketToServer(packet2);
        }
      }
      else if (packet.channel.equals("BlockPhysics0001"))
      {
        String cmd5 = new String(packet.data);
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
          packet2.channel = "BlockPhysics0001";
          packet2.data = new byte[] { 0 };
          packet2.length = packet2.data.length;
          PacketDispatcher.sendPacketToServer(packet2);
          BlockPhysics.writetoLog("Requesting configuration.", 1);
        }
      }
      if (packet.channel.equals("BlockPhysics0002")) {
        if (packet.data[0] == 0)
        {
          File cf = new File(BlockPhysicsUtil.confDir(), "blockphysics.upload.cfg");
          if (cf.exists())
          {
            BlockPhysicsUtil.loadConfig(cf);
            
            byte[] conf = BlockPhysics.cConf.toByteArray();
            
            Packet250CustomPayload packet2 = new Packet250CustomPayload();
            packet2.channel = "BlockPhysics0002";
            
            packet2.data = new byte[conf.length + 1];
            
            System.arraycopy(new byte[] { 0 }, 0, packet2.data, 0, 1);
            System.arraycopy(conf, 0, packet2.data, 1, conf.length);
            
            packet2.length = packet2.data.length;
            PacketDispatcher.sendPacketToServer(packet2);
            BlockPhysics.writetoLog("Uploading configuration.", 1);
            
            ((EntityPlayerSP)playerEntity).addChatMessage("Uploading : " + BlockPhysicsUtil.confDir() + "\\blockphysics.upload.cfg");
          }
          else
          {
            ((EntityPlayerSP)playerEntity).addChatMessage("Can't find: " + BlockPhysicsUtil.confDir() + "\\blockphysics.upload.cfg");
          }
        }
      }
    }
  }
  
  public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
  {
    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.channel = "BlockPhysics0001";
    packet.data = BlockPhysics.cConfmd5.getBytes();
    packet.length = packet.data.length;
    PacketDispatcher.sendPacketToPlayer(packet, player);
    BlockPhysics.writetoLog("Configuration md5 sent to player: " + ((EntityPlayerMP)player).username, 1);
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