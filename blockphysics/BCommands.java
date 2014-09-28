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

import java.io.File;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

public class BCommands  extends CommandBase
{

	public int getRequiredPermissionLevel()
    {
        return 1;
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "/" + this.getCommandName();
    }

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
		else if ( astring[0].equals("on"))
		{
			File cf;
			if ( MinecraftServer.getServer().isDedicatedServer() )	cf = new File(BlockPhysicsUtil.confDir(), BlockPhysics.conffile);
			else cf = new File(BlockPhysics.gameDir+File.separator +"saves" +File.separator +Minecraft.getMinecraft().getIntegratedServer().getFolderName() +File.separator+BlockPhysics.conffile);
			
			if (!cf.exists())
			{
				((EntityPlayerMP)icommandsender). addChatMessage("BlockPhysics configuration loading error, can't find: "+cf);
				BlockPhysics.writetoLog("BlockPhysics configuration loading error, can't find: "+cf, 0);
				return;
			}
			
			BlockPhysicsUtil.resetConfig();
			BlockPhysicsUtil.loadConfig(cf);
			
			Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = "BlockPhysics0001";
            packet.data = BlockPhysics.cConfmd5.getBytes();
            packet.length = packet.data.length;
            PacketDispatcher.sendPacketToAllPlayers(packet);
            BlockPhysics.writetoLog("Configuration md5 sent to all players.",1);
            
            
			((EntityPlayerMP)icommandsender). addChatMessage("BlockPhysics configuration loaded from: "+cf);
		}
		else if ( astring[0].equals("off"))
		{
			for ( int i=0; i < MinecraftServer.getServer().worldServers.length; i++ )
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
            BlockPhysics.writetoLog("Physics OFF. Configuration off sent to all player.",1);
            ((EntityPlayerMP)icommandsender).addChatMessage("BlockPhysics OFF");
		}
		else if ( astring[0].equals("confup"))
		{
			Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = "BlockPhysics0002";
            packet.data = new byte[]{(byte)0};   //asking client to upload conf.
            packet.length = packet.data.length;
            PacketDispatcher.sendPacketToPlayer(packet, (Player)((EntityPlayerMP)icommandsender));
		}
		else if ( astring[0].equals("stat"))
		{
			if ( astring.length == 1)
			{
				String lastv = "";
				((EntityPlayerMP)icommandsender).addChatMessage(" ");
				if (BlockPhysics.lastversion > BlockPhysics.version) lastv = new String(" New version available: "+BlockPhysicsUtil.versiontostring(BlockPhysics.lastversion));
	            ((EntityPlayerMP)icommandsender).addChatMessage("BlockPhysics version: "+BlockPhysicsUtil.versiontostring(BlockPhysics.version)+lastv );
	            if (BlockPhysics.cConfmd5 == "null") ((EntityPlayerMP)icommandsender).addChatMessage("Physics are switched OFF." );
	            else ((EntityPlayerMP)icommandsender).addChatMessage("Configuration loaded.");
	            if (BlockPhysics.skipMove == true) ((EntityPlayerMP)icommandsender).addChatMessage("Server overload (physics not working)" );
	            else ((EntityPlayerMP)icommandsender).addChatMessage("Server normal." );
	            ((EntityPlayerMP)icommandsender).addChatMessage("Explosion queue /world: "+BlockPhysics.explosionQueue+".  Ticks between expl.: "+BlockPhysics.explosionInterval );
	            if (BlockPhysics.warnerr) ((EntityPlayerMP)icommandsender).addChatMessage("There was at least one WARNING message since the startup!");
				else ((EntityPlayerMP)icommandsender).addChatMessage("No warnings in the log.");
	            int movetick = 0;
	            int movingb = 0;
	            int expl = 0;
	            int i;
	            String worlds = "Dimensions: ";
	            for (i=0; i < MinecraftServer.getServer().worldServers.length; i++ )
	            {
	            	worlds = worlds.concat( ""+MinecraftServer.getServer().worldServers[i].provider.dimensionId+", ");
	            	movetick += MinecraftServer.getServer().worldServers[i].moveTickList.getSize();
	            	movingb += MinecraftServer.getServer().worldServers[i].getEntityTracker().movingblocks;
	            	expl += MinecraftServer.getServer().worldServers[i].explosionQueue.getSize();
	        	}	           
	            
	            ((EntityPlayerMP)icommandsender).addChatMessage(worlds);
	            ((EntityPlayerMP)icommandsender).addChatMessage("Moving blocks:           "+movingb+" /"+BlockPhysics.maxmovingblocks*i );
	            ((EntityPlayerMP)icommandsender).addChatMessage("Blocks waiting for tick: "+movetick+" /"+10000*i );
	            ((EntityPlayerMP)icommandsender).addChatMessage("Queued explosions:       "+expl+" /"+BlockPhysics.explosionQueue*i );
	            //((EntityPlayerMP)icommandsender).addChatMessage(" ");
	            ((EntityPlayerMP)icommandsender).addChatMessage("Dimension stat: /bphys stat <dimension num.>");
			}
			else if (astring.length > 1 )
			{
				try
                {
					int dim = Integer.parseInt(astring[1]);
					boolean found = false;
					int i;
					for ( i=0; i < MinecraftServer.getServer().worldServers.length; i++ )
		            {
						if (MinecraftServer.getServer().worldServers[i].provider.dimensionId == dim)
						{
							found = true;
							break;
						}
		            }
					
					if (found)
					{
						((EntityPlayerMP)icommandsender).addChatMessage(" ");
						((EntityPlayerMP)icommandsender).addChatMessage(MinecraftServer.getServer().worldServers[i].provider.getDimensionName()+" ("+dim+")");
						((EntityPlayerMP)icommandsender).addChatMessage("Moving blocks:           "+MinecraftServer.getServer().worldServers[i].getEntityTracker().movingblocks+" /"+BlockPhysics.maxmovingblocks );
			            ((EntityPlayerMP)icommandsender).addChatMessage("Blocks waiting for tick: "+MinecraftServer.getServer().worldServers[i].moveTickList.getSize()+" /"+10000 );
			            ((EntityPlayerMP)icommandsender).addChatMessage("Queued explosions:       "+MinecraftServer.getServer().worldServers[i].explosionQueue.getSize()+" /"+BlockPhysics.explosionQueue );
					}
					else
					{
						((EntityPlayerMP)icommandsender).addChatMessage("Dimesion: "+dim+" not found.");
					}
					
                }
				catch (Exception e)
                {
					((EntityPlayerMP)icommandsender).addChatMessage("Usage: /bphys stat <dimension num.> ( For the OverWorld: /bphys stat 0 )");
                }
			}
		}
		/*else if ( astring[0].equals("clear"))
		{
			if (astring.length > 1 )
			{
				if ( astring[1].equals("all"))
				{
					for ( int i=0; i < MinecraftServer.getServer().worldServers.length; i++ )
		            {
						MinecraftServer.getServer().worldServers[i].moveTickList.reset();
						MinecraftServer.getServer().worldServers[i].explosionQueue.clear();
		            }
				}
				else
				{
					try
	                {
						int dim = Integer.parseInt(astring[1]);
						boolean found = false;
						int i;
						for ( i=0; i < MinecraftServer.getServer().worldServers.length; i++ )
			            {
							if (MinecraftServer.getServer().worldServers[i].provider.dimensionId == dim)
							{
								found = true;
								break;
							}
			            }
						
						if (found)
						{
							((EntityPlayerMP)icommandsender).addChatMessage(" ");
							((EntityPlayerMP)icommandsender).addChatMessage(""+MinecraftServer.getServer().worldServers[i].provider.getDimensionName()+" ("+dim+")");
							((EntityPlayerMP)icommandsender).addChatMessage("Moving blocks:           "+MinecraftServer.getServer().worldServers[i].getEntityTracker().movingblocks+" /"+BlockPhysics.maxmovingblocks );
				            ((EntityPlayerMP)icommandsender).addChatMessage("Blocks waiting for tick: "+MinecraftServer.getServer().worldServers[i].moveTickListSize()+" /"+10000 );
				            ((EntityPlayerMP)icommandsender).addChatMessage("Queued explosions:       "+MinecraftServer.getServer().worldServers[i].explQueueSize()+" /"+BlockPhysics.explosionQueue );
						}
						else
						{
							((EntityPlayerMP)icommandsender).addChatMessage("Dimesion: "+dim+" not found.");
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
				((EntityPlayerMP)icommandsender).addChatMessage("Usage: /bphys clear < dimension num. | all >");
			}
		}*/
		else
		{	
			((EntityPlayerMP)icommandsender).addChatMessage("/bphys off        swich off physics");
			((EntityPlayerMP)icommandsender).addChatMessage("/bphys on         reload config and switch on physics");
			((EntityPlayerMP)icommandsender).addChatMessage("/bphys confup   upload config");
			((EntityPlayerMP)icommandsender).addChatMessage("/bphys stat       list statistics");
		}
	}
}
