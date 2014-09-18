package blockphysics;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.minecraftforge.common.ForgeVersion;

public class BlockPhysicsUtil
{
  public static void initConfig()
  {
    HashSet<String> oldconf = new HashSet();
    oldconf.add("2cdd08c50dd3de54f67d5a2cb88244c8");
    oldconf.add("a1c97344fc93b7575734fd8ccf6b5b8c");
    oldconf.add("63ee06a1d6bd1d3ab9f25a7f65b0bcf0");
    oldconf.add("91340554077602ed31b5d64697233067");
    oldconf.add("4e8495b508a21137afa8b318fab2d9d8");
    
    File cf = new File(confDir(), "blockphysics.cfg");
    if (cf.exists())
    {
      loadConfig(cf);
      if (oldconf.contains(md5Sum(BlockPhysics.cConf.toByteArray())))
      {
        BlockPhysics.writetoLog("Old default configuration detected, updating...", 1);
        cf.delete();
        copyFromjar("config/blockphysics/blockphysics.cfg", cf);
        loadConfig(cf);
      }
    }
    else
    {
      copyFromjar("config/blockphysics/blockphysics.cfg", cf);
      loadConfig(cf);
    }
  }
  
  public static void copyFromjar(String string, File cf)
  {
    if ((cf != null) && (BlockPhysics.bpjarFile != null) && (BlockPhysics.bpjarFile.exists())) {
      try
      {
        JarFile jarFile = new JarFile(BlockPhysics.bpjarFile);
        Enumeration entries = jarFile.entries();
        while (entries.hasMoreElements())
        {
          JarEntry jarEntry = (JarEntry)entries.nextElement();
          if (jarEntry.getName().equals(string))
          {
            byte[] buffer = new byte[4096];
            InputStream jarIS = jarFile.getInputStream(jarEntry);
            FileOutputStream outstr = new FileOutputStream(cf, true);
            for (;;)
            {
              int nRead = jarIS.read(buffer, 0, buffer.length);
              if (nRead <= 0) {
                break;
              }
              outstr.write(buffer, 0, nRead);
            }
            outstr.close();
            jarIS.close();
          }
        }
        jarFile.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public static void resetConfig()
  {
    BlockPhysics.updateinterval = 48;
    BlockPhysics.fallRange = 60;
    BlockPhysics.fallRenderRange = 30;
    BlockPhysics.maxmovingblocks = 300;
    BlockPhysics.maxTickTime = 1850L;
    
    BlockPhysics.catapult = false;
    BlockPhysics.explblstr = 0;
    BlockPhysics.explosionfire = false;
    BlockPhysics.explosionQueue = 200;
    BlockPhysics.explosionInterval = 1;
    
    BlockPhysics.blockMoveDef[0] = new MoveDef(0);
    BlockPhysics.blockDef[0] = new BlockDef(0);
    for (int co = 1; co < 512; co++) {
      BlockPhysics.blockMoveDef[co] = BlockPhysics.blockMoveDef[0];
    }
    for (int co = 1; co < 512; co++) {
      BlockPhysics.blockDef[co] = BlockPhysics.blockDef[0];
    }
    for (int coo = 0; coo < 16; coo++) {
      for (int co = 0; co < 4096; co++) {
        BlockPhysics.blockSet[co][coo] = BlockPhysics.blockDef[0];
      }
    }
    int[] f = new int[1];
    f[0] = 0;
    BlockPhysics.blockMoveDef[1] = new MoveDef(1, 1, 60, false, 0, 0, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[2] = new MoveDef(2, 1, 50, false, 2, 2, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[3] = new MoveDef(3, 1, 45, true, 0, 1, 1, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[4] = new MoveDef(4, 1, 40, false, 2, 3, 2, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[5] = new MoveDef(5, 1, 30, true, 0, 3, 0, 3, 0, 0, f, false);
    BlockPhysics.blockMoveDef[6] = new MoveDef(6, 2, 90, false, 0, 0, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[7] = new MoveDef(7, 2, 70, false, 0, 0, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[8] = new MoveDef(8, 1, 50, true, 1, 1, 1, 1, 0, 0, f, false);
    BlockPhysics.blockMoveDef[9] = new MoveDef(9, 1, 60, false, 1, 1, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[10] = new MoveDef(10, 1, 0, false, 0, 0, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[11] = new MoveDef(11, 3, 0, false, 0, 0, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[12] = new MoveDef(12, 1, 0, false, 1, 1, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[13] = new MoveDef(13, 3, 0, false, 1, 1, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[14] = new MoveDef(14, 1, 30, false, 0, 0, 0, 0, 5, 5, f, false);
    BlockPhysics.blockMoveDef[15] = new MoveDef(15, 1, 30, false, 0, 0, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[16] = new MoveDef(16, 2, 100, false, 0, 0, 0, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[17] = new MoveDef(17, 1, 60, false, 0, 0, 0, 0, 1, 1, f, false);
    BlockPhysics.blockMoveDef[18] = new MoveDef(18, 1, 40, true, 0, 3, 4, 0, 0, 0, f, false);
    BlockPhysics.blockMoveDef[19] = new MoveDef(19, 1, 70, true, 0, 0, 0, 6, 0, 0, f, true);
    BlockPhysics.blockMoveDef[401] = new MoveDef(401, 1, 70, true, 0, 0, 0, 0, 1, 2, f, false);
    BlockPhysics.blockMoveDef[402] = new MoveDef(402, 1, 30, true, 0, 0, 0, 5, 0, 0, f, false);
    
    f = new int[3];
    f[0] = 3;
    f[1] = 17;
    f[2] = 0;
    BlockPhysics.blockMoveDef[403] = new MoveDef(403, 1, 70, false, 0, 0, 0, 0, 0, 0, f, false);
    
    BlockPhysics.blockDef[1] = new BlockDef(1, 1, 1, 0, 0, 0, 0, 0, true, 1, true, 5, 0, 1500, 64000);
    BlockPhysics.blockDef[2] = new BlockDef(2, 2, 0, 2, 15, 2, 1, 0, false, 1, true, 4, 1, 2500, 64000);
    BlockPhysics.blockDef[3] = new BlockDef(3, 2, 0, 3, 15, 2, 1, 0, false, 1, true, 4, 1, 1500, 64000);
    BlockPhysics.blockDef[4] = new BlockDef(4, 2, 0, 4, 15, 2, 1, 0, false, 1, true, 4, 1, 2500, 64000);
    BlockPhysics.blockDef[5] = new BlockDef(5, 2, 0, 5, 15, 2, 1, 0, false, 1, true, 4, 1, 500, 64000);
    BlockPhysics.blockDef[6] = new BlockDef(6, 1, 6, 0, 0, 0, 0, 0, true, 1, true, 5, 0, 1700, 64000);
    BlockPhysics.blockDef[7] = new BlockDef(7, 1, 7, 0, 0, 0, 0, 0, true, 1, true, 5, 0, 1500, 64000);
    BlockPhysics.blockDef[8] = new BlockDef(8, 1, 8, 0, 0, 0, 1, 0, false, 1, true, 4, 0, 2500, 64000);
    BlockPhysics.blockDef[9] = new BlockDef(9, 2, 0, 9, 15, 2, 1, 0, false, 1, true, 4, 1, 2100, 64000);
    BlockPhysics.blockDef[10] = new BlockDef(10, 1, 10, 0, 0, 0, 0, 0, false, 1, true, 4, 0, 1700, 64000);
    BlockPhysics.blockDef[11] = new BlockDef(11, 0, 0, 0, 0, 0, 0, 0, false, 1, false, 4, 0, 1500, 64000);
    BlockPhysics.blockDef[12] = new BlockDef(12, 0, 0, 0, 0, 0, 1, 0, false, 0, false, 4, 0, 1500, 64000);
    BlockPhysics.blockDef[13] = new BlockDef(13, 0, 0, 0, 0, 0, 1, 0, false, 1, false, 4, 0, 1500, 64000);
    BlockPhysics.blockDef[14] = new BlockDef(14, 0, 0, 0, 0, 0, 0, 1, false, 0, false, 4, 0, 10, 0);
    BlockPhysics.blockDef[15] = new BlockDef(15, 0, 0, 0, 0, 0, 0, 2, false, 0, false, 4, 0, 10, 0);
    BlockPhysics.blockDef[16] = new BlockDef(16, 0, 0, 0, 0, 0, 0, 1, false, 1, false, 4, 0, 40, 10);
    
    BlockPhysics.blockDef[17] = new BlockDef(17, 1, 11, 0, 0, 0, 0, 1, false, 0, true, 4, 0, 100, 200);
    
    BlockPhysics.blockDef[18] = new BlockDef(18, 0, 0, 0, 0, 0, 0, 1, false, 1, false, 4, 0, 10, 100);
    BlockPhysics.blockDef[19] = new BlockDef(19, 1, 12, 0, 0, 0, 1, 0, false, 2, true, 4, 0, 2500, 64000);
    BlockPhysics.blockDef[401] = new BlockDef(401, 1, 9, 0, 0, 0, 1, 0, false, 1, true, 4, 0, 700, 64000);
    BlockPhysics.blockDef[402] = new BlockDef(402, 1, 9, 0, 0, 0, 1, 0, false, 1, true, 4, 0, 2500, 64000);
    BlockPhysics.blockDef[403] = new BlockDef(403, 0, 0, 0, 0, 0, 0, 1, true, 0, false, 4, 0, 1, 50);
    BlockPhysics.blockDef[404] = new BlockDef(404, 0, 0, 0, 0, 0, 1, 1, false, 1, false, 4, 0, 125, 1000);
    BlockPhysics.blockDef[405] = new BlockDef(405, 0, 0, 0, 0, 0, 1, 1, false, 1, false, 4, 0, 500, 2000);
    BlockPhysics.blockDef[406] = new BlockDef(406, 0, 0, 0, 0, 0, 1, 0, false, 1, false, 4, 0, 3000, 64000);
    BlockPhysics.blockDef[407] = new BlockDef(407, 2, 0, 18, 10, 2, 1, 1, false, 1, false, 4, 1, 500, 1000);
    BlockPhysics.blockDef[408] = new BlockDef(408, 2, 0, 14, 5, 2, 0, 1, false, 1, true, 4, 1, 10, 100);
    BlockPhysics.blockDef[409] = new BlockDef(409, 0, 0, 0, 0, 0, 1, 0, false, 1, false, 4, 0, 3000, 64000);
    BlockPhysics.blockDef[410] = new BlockDef(410, 1, 6, 0, 0, 0, 0, 1, true, 1, true, 5, 0, 30, 60);
    BlockPhysics.blockDef[411] = new BlockDef(411, 0, 0, 0, 0, 0, 0, 1, false, 1, false, 4, 0, 1000, 500);
    BlockPhysics.blockDef[412] = new BlockDef(412, 0, 0, 0, 0, 0, 0, 0, false, 2, false, 4, 0, 100, 64000);
    BlockPhysics.blockDef[413] = new BlockDef(413, 1, 8, 0, 0, 0, 1, 0, false, 1, true, 4, 0, 6000, 64000);
    BlockPhysics.blockDef[414] = new BlockDef(414, 1, 5, 0, 0, 0, 1, 0, false, 1, true, 4, 0, 1500, 64000);
    BlockPhysics.blockDef[415] = new BlockDef(415, 1, 1, 0, 0, 0, 0, 0, false, 1, true, 4, 0, 1500, 64000);
    BlockPhysics.blockDef[416] = new BlockDef(416, 1, 14, 0, 0, 0, 0, 2, false, 1, true, 4, 0, 200, 100);
    BlockPhysics.blockDef[417] = new BlockDef(417, 1, 16, 0, 0, 0, 0, 1, false, 1, true, 4, 0, 50, 20);
    BlockPhysics.blockDef[418] = new BlockDef(418, 1, 17, 0, 0, 0, 0, 1, false, 1, true, 4, 0, 50, 20);
    BlockPhysics.blockDef[419] = new BlockDef(419, 2, 0, 2, 7, 2, 1, 0, false, 1, true, 4, 1, 400, 64000);
    BlockPhysics.blockDef[420] = new BlockDef(420, 2, 0, 8, 15, 2, 1, 0, false, 1, true, 4, 1, 1000, 64000);
    BlockPhysics.blockDef[421] = new BlockDef(421, 1, 16, 0, 0, 0, 0, 2, false, 1, true, 4, 0, 50, 100);
    
    BlockPhysics.blockDef[422] = new BlockDef(422, 1, 1, 0, 0, 0, 0, 1, true, 1, true, 5, 0, 30, 60);
    BlockPhysics.blockDef[423] = new BlockDef(423, 1, 1, 0, 0, 0, 0, 1, false, 1, true, 4, 0, 125, 1000);
    
    BlockPhysics.blockDef[424] = new BlockDef(424, 0, 0, 0, 0, 0, 0, 2, false, 0, false, 4, 0, 50, 250);
    BlockPhysics.blockDef[425] = new BlockDef(425, 0, 0, 0, 0, 0, 0, 2, false, 0, false, 4, 0, 50, 500);
    BlockPhysics.blockDef[426] = new BlockDef(426, 1, 2, 0, 0, 0, 1, 0, false, 1, true, 4, 0, 3000, 64000);
    BlockPhysics.blockDef[427] = new BlockDef(427, 1, 1, 0, 0, 0, 0, 0, false, 1, true, 4, 0, 3000, 64000);
    BlockPhysics.blockDef[428] = new BlockDef(428, 2, 0, 8, 1, 2, 1, 1, false, 1, true, 4, 1, 500, 1000);
    BlockPhysics.blockDef[429] = new BlockDef(429, 2, 0, 17, 1, 2, 0, 1, false, 1, true, 4, 1, 50, 20);
    BlockPhysics.blockDef[430] = new BlockDef(430, 0, 0, 0, 0, 0, 1, 0, false, 1, false, 4, 0, 6000, 64000);
    BlockPhysics.blockDef[431] = new BlockDef(431, 1, 15, 0, 0, 0, 0, 0, false, 1, true, 4, 0, 6000, 64000);
    BlockPhysics.blockDef[432] = new BlockDef(432, 2, 403, 401, 15, 0, 0, 1, false, 1, true, 4, 1, 40, 10);
    BlockPhysics.blockDef[433] = new BlockDef(433, 2, 19, 0, 15, 0, 1, 1, false, 1, true, 4, 1, 500, 2000);
    BlockPhysics.blockDef[434] = new BlockDef(434, 2, 0, 402, 15, 2, 1, 1, false, 1, true, 4, 1, 125, 1000);
    BlockPhysics.blockDef[435] = new BlockDef(435, 1, 14, 0, 0, 0, 0, 1, true, 0, true, 4, 0, 1, 50);
    BlockPhysics.blockDef[436] = new BlockDef(436, 2, 19, 402, 15, 0, 1, 1, false, 1, true, 4, 1, 500, 2000);
  }
  
  public static File confDir()
  {
    File mcdir = new File(BlockPhysics.gameDir, "config" + File.separator + "blockphysics");
    if (!mcdir.exists()) {
      mcdir.mkdir();
    }
    return mcdir;
  }
  
  static String versiontostring(int vv)
  {
    String vs = Integer.toString(vv);
    return Integer.toString(Integer.parseInt(vs.substring(1, 3))) + "." + Integer.toString(Integer.parseInt(vs.substring(3, 5))) + "." + Integer.toString(Integer.parseInt(vs.substring(5)));
  }
  
  static class ButtonListener
    implements ActionListener
  {
    Frame frame;
    
    ButtonListener(Frame fr)
    {
      this.frame = fr;
    }
    
    public void actionPerformed(ActionEvent e)
    {
      String cmd = e.getActionCommand();
      if (cmd == "BPUPD")
      {
        try
        {
          URI uri = new URI("http://blockphysicssite.no-ip.org");
          
          Desktop.getDesktop().browse(uri);
          this.frame.dispose();
        }
        catch (Exception exx) {}
      }
      else if (cmd == "CPURL")
      {
        StringSelection ss = new StringSelection("http://blockphysicssite.no-ip.org");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
      }
    }
  }
  
  public static boolean loadConfig(byte[] buff)
  {
    if (buff == null)
    {
      BlockPhysics.writetoLog("Can not load config file. It does not exists.", 0);
      return false;
    }
    try
    {
      BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buff)));
      if (loadConfig(bufferedreader)) {
        BlockPhysics.writetoLog("Configuration loaded.", 1);
      }
      bufferedreader.close();
      
      bufferedreader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buff)));
      if (copyConftoBuff(bufferedreader))
      {
        bufferedreader.close();
        return true;
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static boolean loadConfig(File cfile)
  {
    if ((cfile == null) || (!cfile.exists()))
    {
      BlockPhysics.writetoLog("Can not load config file. It does not exists.", 0);
      return false;
    }
    try
    {
      BufferedReader bufferedreader = new BufferedReader(new FileReader(cfile));
      if (loadConfig(bufferedreader)) {
        BlockPhysics.writetoLog("Configuration loaded from: " + cfile, 1);
      }
      bufferedreader.close();
      
      bufferedreader = new BufferedReader(new FileReader(cfile));
      if (copyConftoBuff(bufferedreader))
      {
        bufferedreader.close();
        return true;
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static boolean copyConftoBuff(BufferedReader brr)
  {
    if (brr == null) {
      return false;
    }
    String[][] opttranslations = { { "bookcase", "b:47" }, { "stonebrick", "b:98" }, { "grass", "b:2" }, { "dirt", "b:3" }, { "mossstone", "b:48" }, { "mossycobblestone", "b:48" }, { "cobblestone", "b:4" }, { "woodenplank", "b:5" }, { "bedrock", "b:7" }, { "sandstone", "b:24" }, { "soulsand", "b:88" }, { "gravel", "b:13" }, { "goldore", "b:14" }, { "ironore", "b:15" }, { "coalore", "b:16" }, { "leaves", "b:18" }, { "sponge", "b:19" }, { "glass", "b:20" }, { "lapislazuliore", "b:21" }, { "lapislazuliblock", "b:22" }, { "goldblock", "b:41" }, { "ironblock", "b:42" }, { "obsidian", "b:49" }, { "diamondore", "b:56" }, { "diamondblock", "b:57" }, { "farmland", "b:60" }, { "redstoneoreglowing", "b:74" }, { "snowblock", "b:80" }, { "clayblock", "b:82" }, { "netherrack", "b:87" }, { "glowstone", "b:89" }, { "netherbrick", "b:112" }, { "endstone", "b:121" }, { "redstoneore", "b:73" }, { "sand", "b:12" }, { "stone", "b:1" }, { "ice", "b:79" }, { "wood", "b:17" }, { "brick", "b:45" }, { "wool", "b:35" }, { "tnt", "b:46" }, { "maxticktime", "mt" }, { "maxmovingblocks", "mb" }, { "explosionstrength", "es" }, { "explosionfire", "ef" }, { "explosioninterval", "ei" }, { "explosionqueue", "eq" }, { "catapult", "cp" }, { "updateinterval", "up" }, { "fallrange", "fr" }, { "fallrenderrange", "rr" }, { "randomtick", "a" }, { "movechanger", "c" }, { "blockdef", "d" }, { "slidechance", "e" }, { "moveflipnumber", "f" }, { "inceiling", "g" }, { "ceiling", "g" }, { "supportingblock", "h" }, { "tickrate", "i" }, { "insmallarc", "k" }, { "smallarc", "k" }, { "placedmove", "l" }, { "movenum", "n" }, { "movedef", "o" }, { "pushtype", "p" }, { "inbigarc", "q" }, { "bigarc", "q" }, { "fragile", "r" }, { "mass", "s" }, { "trapping", "t" }, { "inncorbel", "u" }, { "ncorbel", "u" }, { "hanging", "w" }, { "attached", "$" }, { "floating", "~" }, { "branch", "!" }, { "enddef", "x" }, { "movetype", "y" }, { "incorbel", "z" }, { "corbel", "z" }, { "block", "b" }, { "move", "m" }, { "strength", "j" } };
    























































































    BlockPhysics.cConf.reset();
    try
    {
      String nl = new String("\n");
      for (String s = ""; (s = brr.readLine()) != null;)
      {
        s = s.replaceAll("#.*", "");
        s = s.replaceAll("\\s", "");
        s = s.toLowerCase();
        for (int cc = 0; cc < opttranslations.length; cc++) {
          s = s.replaceAll(opttranslations[cc][0], Matcher.quoteReplacement(opttranslations[cc][1]));
        }
        if (s.length() != 0)
        {
          String[] as = s.split(":");
          if (as[0].equals("b"))
          {
            if (Integer.parseInt(as[(as.length - 1)]) != 0)
            {
              BlockPhysics.cConf.write(s.getBytes("UTF-8"));
              BlockPhysics.cConf.write(nl.getBytes("UTF-8"));
            }
          }
          else
          {
            BlockPhysics.cConf.write(s.getBytes("UTF-8"));
            BlockPhysics.cConf.write(nl.getBytes("UTF-8"));
          }
        }
      }
      BlockPhysics.cConfmd5 = md5Sum(BlockPhysics.cConf.toByteArray());
      
      return true;
    }
    catch (Exception exception)
    {
      BlockPhysics.writetoLog("Failed to load config file.", 0);
      exception.printStackTrace();
    }
    return false;
  }
  
  public static boolean loadConfig(BufferedReader bufferedreader)
  {
    if (bufferedreader == null) {
      return false;
    }
    String[][] opttranslations = { { "bookcase", "b:47" }, { "stonebrick", "b:98" }, { "grass", "b:2" }, { "dirt", "b:3" }, { "mossstone", "b:48" }, { "mossycobblestone", "b:48" }, { "cobblestone", "b:4" }, { "woodenplank", "b:5" }, { "bedrock", "b:7" }, { "sandstone", "b:24" }, { "soulsand", "b:88" }, { "gravel", "b:13" }, { "goldore", "b:14" }, { "ironore", "b:15" }, { "coalore", "b:16" }, { "leaves", "b:18" }, { "sponge", "b:19" }, { "glass", "b:20" }, { "lapislazuliore", "b:21" }, { "lapislazuliblock", "b:22" }, { "goldblock", "b:41" }, { "ironblock", "b:42" }, { "obsidian", "b:49" }, { "diamondore", "b:56" }, { "diamondblock", "b:57" }, { "farmland", "b:60" }, { "redstoneoreglowing", "b:74" }, { "snowblock", "b:80" }, { "clayblock", "b:82" }, { "netherrack", "b:87" }, { "glowstone", "b:89" }, { "netherbrick", "b:112" }, { "endstone", "b:121" }, { "redstoneore", "b:73" }, { "sand", "b:12" }, { "stone", "b:1" }, { "ice", "b:79" }, { "wood", "b:17" }, { "brick", "b:45" }, { "wool", "b:35" }, { "tnt", "b:46" }, { "maxticktime", "mt" }, { "maxmovingblocks", "mb" }, { "explosionstrength", "es" }, { "explosionfire", "ef" }, { "explosioninterval", "ei" }, { "explosionqueue", "eq" }, { "catapult", "cp" }, { "updateinterval", "up" }, { "fallrange", "fr" }, { "fallrenderrange", "rr" }, { "randomtick", "a" }, { "movechanger", "c" }, { "blockdef", "d" }, { "slidechance", "e" }, { "moveflipnumber", "f" }, { "inceiling", "g" }, { "ceiling", "g" }, { "supportingblock", "h" }, { "tickrate", "i" }, { "insmallarc", "k" }, { "smallarc", "k" }, { "placedmove", "l" }, { "movenum", "n" }, { "movedef", "o" }, { "pushtype", "p" }, { "inbigarc", "q" }, { "bigarc", "q" }, { "fragile", "r" }, { "mass", "s" }, { "trapping", "t" }, { "inncorbel", "u" }, { "ncorbel", "u" }, { "hanging", "w" }, { "attached", "$" }, { "floating", "~" }, { "branch", "!" }, { "enddef", "x" }, { "movetype", "y" }, { "incorbel", "z" }, { "corbel", "z" }, { "block", "b" }, { "move", "m" }, { "strength", "j" } };
    try
    {
      boolean doneblockassign = false;
      boolean doneblockdef = false;
      int line = 0;
      for (String s = ""; (s = bufferedreader.readLine()) != null;)
      {
        String sout = new String(s);
        line++;
        try
        {
          s = s.replaceAll("#.*", "");
          s = s.replaceAll("\\s", "");
          s = s.toLowerCase();
          for (int cc = 0; cc < opttranslations.length; cc++) {
            s = s.replaceAll(opttranslations[cc][0], Matcher.quoteReplacement(opttranslations[cc][1]));
          }
          String[] as = s.split(":");
          if (as[0].equals("mt"))
          {
            int vv = Integer.parseInt(as[1]);
            if ((vv < 0) || (vv > 2000)) {
              BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 2000.)", 0);
            } else {
              BlockPhysics.maxTickTime = vv;
            }
          }
          else if (as[0].equals("mb"))
          {
            int vv = Integer.parseInt(as[1]);
            if ((vv < 0) || (vv > 1000)) {
              BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 1000.)", 0);
            } else {
              BlockPhysics.maxmovingblocks = vv;
            }
          }
          else if (as[0].equals("es"))
          {
            int vv = Integer.parseInt(as[1]);
            if ((vv < 0) || (vv > 200)) {
              BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 200.)", 0);
            } else {
              BlockPhysics.explblstr = vv;
            }
          }
          else if (as[0].equals("ei"))
          {
            int vv = Integer.parseInt(as[1]);
            if ((vv < 1) || (vv > 100)) {
              BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 1 - 100.)", 0);
            } else {
              BlockPhysics.explosionInterval = vv;
            }
          }
          else if (as[0].equals("eq"))
          {
            int vv = Integer.parseInt(as[1]);
            if ((vv < 1) || (vv > 1000)) {
              BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 1 - 1000.)", 0);
            } else {
              BlockPhysics.explosionQueue = vv;
            }
          }
          else if (as[0].equals("ef"))
          {
            int vv = Integer.parseInt(as[1]);
            if ((vv < 0) || (vv > 1)) {
              BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 1.)", 0);
            } else if (vv == 0) {
              BlockPhysics.explosionfire = false;
            } else {
              BlockPhysics.explosionfire = true;
            }
          }
          else if (as[0].equals("cp"))
          {
            int vv = Integer.parseInt(as[1]);
            if ((vv < 0) || (vv > 1)) {
              BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 1.)", 0);
            } else if (vv == 0) {
              BlockPhysics.catapult = false;
            } else {
              BlockPhysics.catapult = true;
            }
          }
          else if (as[0].equals("up"))
          {
            int vv = Integer.parseInt(as[1]);
            if ((vv < 0) || (vv > 1000)) {
              BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 1000.)", 0);
            } else {
              BlockPhysics.updateinterval = vv;
            }
          }
          else if (as[0].equals("fr"))
          {
            int vv = Integer.parseInt(as[1]);
            if ((vv < 0) || (vv > 1000)) {
              BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 1 - 1000.)", 0);
            } else {
              BlockPhysics.fallRange = vv;
            }
          }
          else if (as[0].equals("rr"))
          {
            int vv = Integer.parseInt(as[1]);
            if ((vv < 0) || (vv > 1000)) {
              BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 1 - 1000.)", 0);
            } else {
              BlockPhysics.fallRenderRange = vv;
            }
          }
          else if (as[0].equals("b"))
          {
            String[] bl = as[1].split(",");
            int vv2 = Integer.parseInt(as[2]);
            for (int bb = 0; bb < Integer.valueOf(bl.length).intValue(); bb++)
            {
              String[] bll = bl[bb].split("/");
              if (bll.length == 1)
              {
                bll = bl[bb].split("-");
                int from = 0;int to = 0;
                if (bll.length == 1)
                {
                  from = Integer.parseInt(bll[0]);
                  to = 1 + from;
                }
                if (bll.length > 1)
                {
                  from = Integer.parseInt(bll[0]);
                  to = 1 + Integer.parseInt(bll[1]);
                }
                for (int vv = Integer.valueOf(from).intValue(); vv < Integer.valueOf(to).intValue(); vv++) {
                  if (aqw.s[vv] != null) {
                    if ((vv < 1) || (vv > 4095))
                    {
                      BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( Block Id range is 1-4095.)", 0);
                    }
                    else if ((vv2 < 0) || (vv2 > 511))
                    {
                      BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( blockdef range is 0 - 511.)", 0);
                    }
                    else
                    {
                      boolean canmove = BlockPhysics.canSetMove(vv);
                      boolean moving = (BlockPhysics.blockDef[vv2].pushtype == 1) || (BlockPhysics.blockDef[vv2].pushtype == 3) || ((BlockPhysics.blockMoveDef[BlockPhysics.blockDef[vv2].move[0]].movetype != 3) && (BlockPhysics.blockMoveDef[BlockPhysics.blockDef[vv2].move[0]].movetype != 0)) || ((BlockPhysics.blockMoveDef[BlockPhysics.blockDef[vv2].move[1]].movetype != 3) && (BlockPhysics.blockMoveDef[BlockPhysics.blockDef[vv2].move[1]].movetype != 0));
                      if ((!canmove) && (moving))
                      {
                        BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( the block with blockID: " + vv + " can not be set to move ( It is a liquid or a non solid block. )", 0);
                      }
                      else
                      {
                        doneblockassign = true;
                        for (int co = 0; co < 16; co++) {
                          BlockPhysics.blockSet[vv][co] = BlockPhysics.blockDef[vv2];
                        }
                      }
                    }
                  }
                }
              }
              else
              {
                int vv = Integer.parseInt(bll[0]);
                int vm = Integer.parseInt(bll[1]);
                if (aqw.s[vv] != null) {
                  if ((vv < 1) || (vv > 4095))
                  {
                    BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( Block Id range is 1-4095.)", 0);
                  }
                  else if ((vm < 0) || (vm > 15))
                  {
                    BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( SubBlock range is 0-15.)", 0);
                  }
                  else if ((vv2 < 0) || (vv2 > 511))
                  {
                    BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( blockdef range is 0 - 511.)", 0);
                  }
                  else
                  {
                    boolean canmove = BlockPhysics.canSetMove(vv);
                    boolean moving = (BlockPhysics.blockDef[vv2].pushtype == 1) || (BlockPhysics.blockDef[vv2].pushtype == 3) || ((BlockPhysics.blockMoveDef[BlockPhysics.blockDef[vv2].move[0]].movetype != 3) && (BlockPhysics.blockMoveDef[BlockPhysics.blockDef[vv2].move[0]].movetype != 0)) || ((BlockPhysics.blockMoveDef[BlockPhysics.blockDef[vv2].move[1]].movetype != 3) && (BlockPhysics.blockMoveDef[BlockPhysics.blockDef[vv2].move[1]].movetype != 0));
                    if ((!canmove) && (moving))
                    {
                      BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( the block with blockID: " + vv + "/" + vm + " can not be set to move ( It is a liquid or a non solid block. )", 0);
                    }
                    else
                    {
                      doneblockassign = true;
                      BlockPhysics.blockSet[vv][vm] = BlockPhysics.blockDef[vv2];
                    }
                  }
                }
              }
            }
          }
          else
          {
            int defnum;
            String t;
            String t;
            if (as[0].equals("d"))
            {
              defnum = Integer.parseInt(as[1]);
              if ((doneblockassign) || (defnum < 20) || (defnum > 400))
              {
                if (doneblockassign) {
                  BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " Block definitions must be done before the block assignments! ", 0);
                } else {
                  BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (User settable blockdef range is 20 - 400.)", 0);
                }
                for (t = ""; (t = bufferedreader.readLine()) != null;)
                {
                  sout = new String(t);
                  line++;
                  BlockPhysics.writetoLog("Skipping line " + line + " : " + sout, 0);
                  t = t.replaceAll("#.*", "");
                  t = t.replaceAll("\\s", "");
                  t = t.toLowerCase();
                  for (int cc = 0; cc < opttranslations.length; cc++) {
                    t = t.replaceAll(opttranslations[cc][0], Matcher.quoteReplacement(opttranslations[cc][1]));
                  }
                  String[] at = t.split(":");
                  if (at[0].equals("x"))
                  {
                    break;doneblockdef = true;
                    BlockPhysics.blockDef[defnum] = new BlockDef(defnum);
                    for (t = ""; (t = bufferedreader.readLine()) != null;)
                    {
                      sout = new String(t);
                      line++;
                      t = t.replaceAll("#.*", "");
                      t = t.replaceAll("\\s", "");
                      t = t.toLowerCase();
                      for (int cc = 0; cc < opttranslations.length; cc++) {
                        t = t.replaceAll(opttranslations[cc][0], Matcher.quoteReplacement(opttranslations[cc][1]));
                      }
                      String[] at = t.split(":");
                      if (at[0].equals("x")) {
                        break;
                      }
                      if (at[0].equals("d"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 511)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (blockdef number range is 0 - 511.)", 0);
                        } else {
                          BlockDef.copyBlockDef(BlockPhysics.blockDef[defnum], BlockPhysics.blockDef[vv]);
                        }
                      }
                      else if (at[0].equals("n"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 2))
                        {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (movenum range is 0 - 2.)", 0);
                        }
                        else
                        {
                          BlockPhysics.blockDef[defnum].movenum = vv;
                          if (vv == 0)
                          {
                            BlockPhysics.blockDef[defnum].move[0] = 0;
                            BlockPhysics.blockDef[defnum].move[1] = 0;
                            BlockPhysics.blockDef[defnum].moveflipnumber = 0;
                            BlockPhysics.blockDef[defnum].movechanger = 0;
                            BlockPhysics.blockDef[defnum].randomtick = false;
                            BlockPhysics.blockDef[defnum].tickrate = 10;
                            BlockPhysics.blockDef[defnum].placedmove = 0;
                          }
                          else if (vv == 1)
                          {
                            BlockPhysics.blockDef[defnum].move[1] = 0;
                            BlockPhysics.blockDef[defnum].moveflipnumber = 0;
                            BlockPhysics.blockDef[defnum].movechanger = 0;
                            BlockPhysics.blockDef[defnum].randomtick = true;
                            BlockPhysics.blockDef[defnum].placedmove = 0;
                          }
                        }
                      }
                      else if (at[0].equals("m"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        int vv2 = Integer.parseInt(at[2]);
                        if ((vv < 0) || (vv > 1)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (move number range is 0 - 1.)", 0);
                        } else if ((vv2 < 0) || (vv2 > 511)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (movedef number range is 0 - 511.)", 0);
                        } else {
                          BlockPhysics.blockDef[defnum].move[vv] = vv2;
                        }
                      }
                      else if (at[0].equals("f"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 15)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 15.)", 0);
                        } else {
                          BlockPhysics.blockDef[defnum].moveflipnumber = vv;
                        }
                      }
                      else if (at[0].equals("c"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 3)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 3.)", 0);
                        } else {
                          BlockPhysics.blockDef[defnum].movechanger = vv;
                        }
                      }
                      else if (at[0].equals("h"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 1)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 1.)", 0);
                        } else {
                          BlockPhysics.blockDef[defnum].supportingblock = vv;
                        }
                      }
                      else if (at[0].equals("r"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 2)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 2.)", 0);
                        } else {
                          BlockPhysics.blockDef[defnum].fragile = vv;
                        }
                      }
                      else if (at[0].equals("t"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 1)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 1.)", 0);
                        } else if (vv == 1) {
                          BlockPhysics.blockDef[defnum].trapping = true;
                        } else {
                          BlockPhysics.blockDef[defnum].trapping = false;
                        }
                      }
                      else if (at[0].equals("p"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 3)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 3.)", 0);
                        } else {
                          BlockPhysics.blockDef[defnum].pushtype = vv;
                        }
                      }
                      else if (at[0].equals("a"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 1)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 1.)", 0);
                        } else if (vv == 1) {
                          BlockPhysics.blockDef[defnum].randomtick = true;
                        } else {
                          BlockPhysics.blockDef[defnum].randomtick = false;
                        }
                      }
                      else if (at[0].equals("i"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 255)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 255.)", 0);
                        } else {
                          BlockPhysics.blockDef[defnum].tickrate = vv;
                        }
                      }
                      else if (at[0].equals("l"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 1)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 1.)", 0);
                        } else if ((vv == 1) && (BlockPhysics.blockDef[defnum].movenum != 2)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( movenum must be 2)", 0);
                        } else {
                          BlockPhysics.blockDef[defnum].placedmove = vv;
                        }
                      }
                      else if (at[0].equals("s"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 1) || (vv > 40000)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 1 - 40000.)", 0);
                        } else {
                          BlockPhysics.blockDef[defnum].mass = vv;
                        }
                      }
                      else if (at[0].equals("j"))
                      {
                        int vv = Integer.parseInt(at[1]);
                        if ((vv < 0) || (vv > 64000)) {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " ( range is 0 - 64000.)", 0);
                        } else {
                          BlockPhysics.blockDef[defnum].strength = vv;
                        }
                      }
                      else if (t.trim().length() != 0)
                      {
                        BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout, 0);
                      }
                    }
                  }
                }
              }
            }
            else
            {
              int defnum;
              String t;
              String t;
              if (as[0].equals("o"))
              {
                defnum = Integer.parseInt(as[1]);
                if ((doneblockdef == true) || (doneblockassign == true) || (defnum < 20) || (defnum > 400))
                {
                  if ((doneblockdef) || (doneblockassign)) {
                    BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " movedefs must be done before the blockdefs and block assignments! ", 0);
                  } else {
                    BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (User settable movedef number range is 20 - 400.)", 0);
                  }
                  for (t = ""; (t = bufferedreader.readLine()) != null;)
                  {
                    sout = new String(t);
                    line++;
                    BlockPhysics.writetoLog("Skipping line " + line + " : " + sout, 0);
                    t = t.replaceAll("#.*", "");
                    t = t.replaceAll("\\s", "");
                    t = t.toLowerCase();
                    for (int cc = 0; cc < opttranslations.length; cc++) {
                      t = t.replaceAll(opttranslations[cc][0], Matcher.quoteReplacement(opttranslations[cc][1]));
                    }
                    String[] at = t.split(":");
                    if (at[0].equals("x"))
                    {
                      break;BlockPhysics.blockMoveDef[defnum] = new MoveDef(defnum);
                      for (t = ""; (t = bufferedreader.readLine()) != null;)
                      {
                        sout = new String(t);
                        line++;
                        t = t.replaceAll("#.*", "");
                        t = t.replaceAll("\\s", "");
                        t = t.toLowerCase();
                        for (int cc = 0; cc < opttranslations.length; cc++) {
                          t = t.replaceAll(opttranslations[cc][0], Matcher.quoteReplacement(opttranslations[cc][1]));
                        }
                        String[] at = t.split(":");
                        if (at[0].equals("x")) {
                          break;
                        }
                        if (at[0].equals("o"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 511)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (movedef range is 0 - 511.)", 0);
                          } else {
                            MoveDef.copyMoveDef(BlockPhysics.blockMoveDef[defnum], BlockPhysics.blockMoveDef[vv]);
                          }
                        }
                        else if (at[0].equals("y"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 3)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (movetype range is 0 - 3.)", 0);
                          } else {
                            BlockPhysics.blockMoveDef[defnum].movetype = vv;
                          }
                        }
                        else if (at[0].equals("e"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 100)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (slidechance range is 0 - 100.)", 0);
                          } else {
                            BlockPhysics.blockMoveDef[defnum].slidechance = vv;
                          }
                        }
                        else if (at[0].equals("g"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 1)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (range is 0 - 1.)", 0);
                          } else if (vv == 0) {
                            BlockPhysics.blockMoveDef[defnum].ceiling = false;
                          } else {
                            BlockPhysics.blockMoveDef[defnum].ceiling = true;
                          }
                        }
                        else if (at[0].equals("k"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 6)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (range is 0 - 6.)", 0);
                          } else {
                            BlockPhysics.blockMoveDef[defnum].smallarc = vv;
                          }
                        }
                        else if (at[0].equals("q"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 6)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (range is 0 - 6.)", 0);
                          } else {
                            BlockPhysics.blockMoveDef[defnum].bigarc = vv;
                          }
                        }
                        else if (at[0].equals("z"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 6)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (range is 0 - 6.)", 0);
                          } else {
                            BlockPhysics.blockMoveDef[defnum].corbel = vv;
                          }
                        }
                        else if (at[0].equals("u"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 6)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (range is 0 - 6.)", 0);
                          } else {
                            BlockPhysics.blockMoveDef[defnum].ncorbel = vv;
                          }
                        }
                        else if (at[0].equals("w"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 10)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (range is 0 - 10.)", 0);
                          } else {
                            BlockPhysics.blockMoveDef[defnum].hanging = vv;
                          }
                        }
                        else if (at[0].equals("$"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 6)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (range is 0 - 6.)", 0);
                          } else {
                            BlockPhysics.blockMoveDef[defnum].attached = vv;
                          }
                        }
                        else if (at[0].equals("!"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if ((vv < 0) || (vv > 1)) {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (range is 0 - 1.)", 0);
                          } else if (vv == 0) {
                            BlockPhysics.blockMoveDef[defnum].branch = false;
                          } else {
                            BlockPhysics.blockMoveDef[defnum].branch = true;
                          }
                        }
                        else if (at[0].equals("~"))
                        {
                          int vv = Integer.parseInt(at[1]);
                          if (vv == 0)
                          {
                            int[] f = new int[1];
                            f[0] = 0;
                            BlockPhysics.blockMoveDef[defnum].floating = f;
                          }
                          else if ((vv < 1) || (vv > 6) || (at.length != 4) || (Integer.parseInt(at[2]) < 1) || (Integer.parseInt(at[2]) > 4095) || (Integer.parseInt(at[3]) < 0) || (Integer.parseInt(at[3]) > 15))
                          {
                            BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout + " (syntax floating:radius:blockId:metadata  radius 0-6)", 0);
                          }
                          else
                          {
                            int[] f = new int[3];
                            f[0] = vv;
                            f[1] = Integer.parseInt(at[2]);
                            f[2] = Integer.parseInt(at[3]);
                            
                            BlockPhysics.blockMoveDef[defnum].floating = f;
                          }
                        }
                        else if (t.trim().length() != 0)
                        {
                          BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout, 0);
                        }
                      }
                    }
                  }
                }
              }
              else if (s.trim().length() != 0)
              {
                BlockPhysics.writetoLog("Skipping bad option in line " + line + " : " + sout, 0);
              }
            }
          }
        }
        catch (Exception exception1)
        {
          BlockPhysics.writetoLog(new StringBuilder().append("Skipping bad option in line ").append(line).append(" : ").toString() + sout, 0);
        }
      }
      return true;
    }
    catch (Exception exception)
    {
      BlockPhysics.writetoLog("Failed to load config file.", 0);
      exception.printStackTrace();
    }
    return false;
  }
  
  public static void blockphysicsnotify(String message)
  {
    JFrame frame;
    try
    {
      frame = new JFrame("BlockPhysics");
    }
    catch (Exception e)
    {
      BlockPhysics.writetoLog("No Gui.", 2);
      return;
    }
    Color bgcolor = new Color(194, 205, 234);
    Color txcolor = new Color(50, 50, 50);
    frame.setLocation(20, 20);
    JLabel label = new JLabel(message);
    JPanel panel = new JPanel();
    panel.setBackground(bgcolor);
    label.setForeground(txcolor);
    label.setFont(new Font("Dialog", 1, 14));
    panel.add(label);
    if (Desktop.isDesktopSupported())
    {
      Desktop desktop = Desktop.getDesktop();
      JButton button;
      if (desktop.isSupported(Desktop.Action.BROWSE))
      {
        JButton button = new JButton("Open Website");
        button.setActionCommand("BPUPD");
      }
      else
      {
        button = new JButton("Copy URL to Clipboard");
        button.setActionCommand("CPURL");
      }
      button.setFont(new Font("Dialog", 1, 14));
      button.addActionListener(new ButtonListener(frame));
      panel.add(button);
    }
    frame.setDefaultCloseOperation(2);
    frame.getContentPane().add(panel);
    frame.pack();
    frame.setFocusableWindowState(false);
    frame.setVisible(true);
    frame.setFocusableWindowState(true);
  }
  
  static void checkForUpdate()
  {
    BlockPhysics.lastversion = 1000800;
    int lastcheck = 0;
    int currenthours = (int)(System.currentTimeMillis() / 3600000L);
    int messageid = 0;int messageid2 = 0;
    int interval = BlockPhysics.updateinterval;
    if (interval < 1) {
      interval = 168;
    } else if (interval < 8) {
      interval = 8;
    } else if (interval > 168) {
      interval = 168;
    }
    String messageurl = "http://blockphysupdate.no-ip.org/message.html";
    String message = "nope";
    boolean updchk = true;
    
    File updatecheck = new File(confDir(), "updatecheck.txt");
    try
    {
      if (updatecheck.exists())
      {
        BufferedReader bufferedreader2 = new BufferedReader(new FileReader(updatecheck));
        for (String s = ""; (s = bufferedreader2.readLine()) != null;) {
          try
          {
            String[] as = s.split(":");
            if (as[0].equals("lastcheck"))
            {
              lastcheck = Integer.parseInt(as[1]);
              if (lastcheck > currenthours) {
                lastcheck = 0;
              }
            }
            if (as[0].equals("lastversion")) {
              BlockPhysics.lastversion = Integer.parseInt(as[1]);
            }
            if (as[0].equals("messageid")) {
              messageid = Integer.parseInt(as[1]);
            }
          }
          catch (Exception exception1)
          {
            BlockPhysics.writetoLog("Skipping bad option: " + s, 0);
          }
        }
        bufferedreader2.close();
      }
      if (currenthours - lastcheck > interval)
      {
        lastcheck = currenthours;
        try
        {
          URL url = new URL(messageurl);
          HttpURLConnection.setFollowRedirects(true);
          
          HttpURLConnection uconn = (HttpURLConnection)url.openConnection();
          uconn.setConnectTimeout(1500);
          uconn.setReadTimeout(1500);
          uconn.setRequestMethod("GET");
          uconn.setRequestProperty("Content-Type", "text/plain");
          uconn.setRequestProperty("charset", "utf-8");
          try
          {
            uconn.connect();
          }
          catch (SocketTimeoutException excep)
          {
            return;
          }
          BufferedReader bufferedreader2 = new BufferedReader(new InputStreamReader(uconn.getInputStream()));
          for (String s = ""; (s = bufferedreader2.readLine()) != null;) {
            try
            {
              String[] as = s.split(":");
              if ((as.length > 1) && (as[1].equals("message")))
              {
                messageid2 = Integer.parseInt(as[2]);
                if (as.length > 3) {
                  message = as[3];
                }
                if (messageid != messageid2)
                {
                  messageid = messageid2;
                  if (message.contentEquals("count"))
                  {
                    String cou = "000";
                    if (as.length > 5) {
                      cou = as[4];
                    }
                    String plt = "c";
                    try
                    {
                      Class.forName("net.minecraft.client.gui.GuiMainMenu");
                    }
                    catch (ClassNotFoundException e)
                    {
                      plt = "s";
                    }
                    try
                    {
                      Class.forName("org.bukkit.Bukkit");
                      plt = "m";
                    }
                    catch (ClassNotFoundException e) {}
                    URL url2 = new URL("http://blockphysupdate.no-ip.org/count.html?" + cou + "x162x" + ForgeVersion.getVersion().replace('.', 'd') + "xbp" + 1000800 + "x" + plt);
                    
                    HttpURLConnection uconn2 = (HttpURLConnection)url2.openConnection();
                    uconn2.setConnectTimeout(1500);
                    uconn2.setReadTimeout(1500);
                    
                    uconn2.setInstanceFollowRedirects(true);
                    uconn2.setRequestMethod("GET");
                    uconn2.setRequestProperty("Content-Type", "text/plain");
                    uconn2.setRequestProperty("charset", "utf-8");
                    try
                    {
                      uconn2.connect();
                    }
                    catch (SocketTimeoutException excep) {}
                    BufferedReader bufferedreader3 = new BufferedReader(new InputStreamReader(uconn2.getInputStream()));
                    for (String sc = ""; (sc = bufferedreader3.readLine()) != null;) {}
                    bufferedreader3.close();
                  }
                  else if (!message.contentEquals("nope"))
                  {
                    blockphysicsnotify(message);
                    updchk = false;
                  }
                }
                break;
              }
            }
            catch (Exception exception1) {}
          }
          bufferedreader2.close();
        }
        catch (Exception exception)
        {
          return;
        }
        if ((BlockPhysics.updateinterval > 0) && (updchk))
        {
          BlockPhysics.writetoLog("Checking for updates...", 1);
          try
          {
            URL url = new URL("http://blockphysupdate.no-ip.org/lastversion162.html?1000800");
            HttpURLConnection.setFollowRedirects(true);
            
            HttpURLConnection uconn = (HttpURLConnection)url.openConnection();
            uconn.setConnectTimeout(2500);
            uconn.setReadTimeout(1500);
            try
            {
              uconn.connect();
            }
            catch (SocketTimeoutException excep)
            {
              BlockPhysics.writetoLog("Failed to connect to http://blockphysupdate.no-ip.org/lastversion162.html", 2);
              return;
            }
            BufferedReader bufferedreader2 = new BufferedReader(new InputStreamReader(uconn.getInputStream()));
            for (String s = ""; (s = bufferedreader2.readLine()) != null;) {
              try
              {
                String[] as = s.split(":");
                if (as[1].equals("lastversion"))
                {
                  BlockPhysics.lastversion = Integer.parseInt(as[2]);
                  break;
                }
              }
              catch (Exception exception1) {}
            }
            bufferedreader2.close();
          }
          catch (Exception exception)
          {
            BlockPhysics.writetoLog("Failed to download http://blockphysupdate.no-ip.org/lastversion162.html", 2);
            return;
          }
          if (BlockPhysics.lastversion > 1000800)
          {
            blockphysicsnotify("New version of the BlockPhysics mod is available! (v" + versiontostring(BlockPhysics.lastversion) + ") ");
            BlockPhysics.writetoLog("New version " + versiontostring(BlockPhysics.lastversion) + " of the BlockPhysics mod is available, go to " + "http://blockphysicssite.no-ip.org" + " to download.", 1);
          }
        }
        BufferedWriter bufferedwriter2 = new BufferedWriter(new FileWriter(updatecheck));
        bufferedwriter2.write("lastcheck:" + Integer.toString(lastcheck));
        bufferedwriter2.newLine();
        bufferedwriter2.write("lastversion:" + Integer.toString(BlockPhysics.lastversion));
        bufferedwriter2.newLine();
        bufferedwriter2.write("messageid:" + Integer.toString(messageid));
        bufferedwriter2.newLine();
        bufferedwriter2.close();
      }
    }
    catch (Exception exception)
    {
      BlockPhysics.writetoLog("Failed to load BlockPhysics update file.", 0);
      exception.printStackTrace();
    }
  }
  
  public static String md5Sum(byte[] data)
  {
    if (data == null) {
      return "null";
    }
    try
    {
      MessageDigest var3 = MessageDigest.getInstance("MD5");
      var3.update(data);
      String s = new BigInteger(1, var3.digest()).toString(16);
      while (s.length() < 32) {
        s = "0" + s;
      }
      return s;
    }
    catch (NoSuchAlgorithmException var4)
    {
      throw new RuntimeException(var4);
    }
  }
  
  public static void compConfig(File confF, boolean cata, boolean explfire, int explphys, int physics)
  {
    if (confF == null) {
      return;
    }
    String[][] opttranslations = { { "bookcase", "b:47" }, { "stonebrick", "b:98" }, { "grass", "b:2" }, { "dirt", "b:3" }, { "mossstone", "b:48" }, { "mossycobblestone", "b:48" }, { "cobblestone", "b:4" }, { "woodenplank", "b:5" }, { "bedrock", "b:7" }, { "sandstone", "b:24" }, { "soulsand", "b:88" }, { "gravel", "b:13" }, { "goldore", "b:14" }, { "ironore", "b:15" }, { "coalore", "b:16" }, { "leaves", "b:18" }, { "sponge", "b:19" }, { "glass", "b:20" }, { "lapislazuliore", "b:21" }, { "lapislazuliblock", "b:22" }, { "goldblock", "b:41" }, { "ironblock", "b:42" }, { "obsidian", "b:49" }, { "diamondore", "b:56" }, { "diamondblock", "b:57" }, { "farmland", "b:60" }, { "redstoneoreglowing", "b:74" }, { "snowblock", "b:80" }, { "clayblock", "b:82" }, { "netherrack", "b:87" }, { "glowstone", "b:89" }, { "netherbrick", "b:112" }, { "endstone", "b:121" }, { "redstoneore", "b:73" }, { "sand", "b:12" }, { "stone", "b:1" }, { "ice", "b:79" }, { "wood", "b:17" }, { "brick", "b:45" }, { "wool", "b:35" }, { "tnt", "b:46" }, { "maxticktime", "mt" }, { "maxmovingblocks", "mb" }, { "explosionstrength", "es" }, { "explosionfire", "ef" }, { "explosioninterval", "ei" }, { "explosionqueue", "eq" }, { "catapult", "cp" }, { "updateinterval", "up" }, { "fallrange", "fr" }, { "fallrenderrange", "rr" }, { "randomtick", "a" }, { "movechanger", "c" }, { "blockdef", "d" }, { "slidechance", "e" }, { "moveflipnumber", "f" }, { "inceiling", "g" }, { "ceiling", "g" }, { "supportingblock", "h" }, { "tickrate", "i" }, { "insmallarc", "k" }, { "smallarc", "k" }, { "placedmove", "l" }, { "movenum", "n" }, { "movedef", "o" }, { "pushtype", "p" }, { "inbigarc", "q" }, { "bigarc", "q" }, { "fragile", "r" }, { "mass", "s" }, { "trapping", "t" }, { "inncorbel", "u" }, { "ncorbel", "u" }, { "hanging", "w" }, { "attached", "$" }, { "floating", "~" }, { "branch", "!" }, { "enddef", "x" }, { "movetype", "y" }, { "incorbel", "z" }, { "corbel", "z" }, { "block", "b" }, { "move", "m" }, { "strength", "j" } };
    try
    {
      BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(confF, false));
      BufferedReader bufferedreader = new BufferedReader(new FileReader(new File(confDir(), "blockphysics.cfg")));
      



      HashSet<String> exc = new HashSet();
      exc.add("up");
      exc.add("fr");
      exc.add("rr");
      exc.add("mb");
      exc.add("mt");
      exc.add("ei");
      exc.add("eq");
      if (explphys == 1) {
        exc.add("es");
      }
      for (String s = ""; (s = bufferedreader.readLine()) != null;)
      {
        String sout = new String(s);
        
        s = s.replaceAll("#.*", "");
        s = s.replaceAll("\\s", "");
        s = s.toLowerCase();
        for (int cc = 0; cc < opttranslations.length; cc++) {
          s = s.replaceAll(opttranslations[cc][0], Matcher.quoteReplacement(opttranslations[cc][1]));
        }
        String[] as = s.split(":");
        if (exc.contains(new String(as[0])))
        {
          bufferedwriter.write(sout);
          bufferedwriter.newLine();
        }
      }
      bufferedreader.close();
      if (cata) {
        bufferedwriter.write("catapult:1                  # Enable / disable catapult piston ( 1 enabled, 0 disabled).");
      } else {
        bufferedwriter.write("catapult:0                  # Enable / disable catapult piston ( 1 enabled, 0 disabled).");
      }
      bufferedwriter.newLine();
      switch (explphys)
      {
      case 0: 
        bufferedwriter.write("explosionstrength:0        # Explosion strength modifier 0 - 200 original:100, affects only blocks.");
        bufferedwriter.newLine();
        break;
      case 2: 
        bufferedwriter.write("explosionstrength:100        # Explosion strength modifier 0 - 200 original:100, affects only blocks.");
        bufferedwriter.newLine();
        break;
      case 3: 
        bufferedwriter.write("explosionstrength:140        # Explosion strength modifier 0 - 200 original:100, affects only blocks.");
        bufferedwriter.newLine();
      }
      if (explfire) {
        bufferedwriter.write("explosionfire:1             # Can explosions cause fire?, 1 yes/ 0 no.");
      } else {
        bufferedwriter.write("explosionfire:0             # Can explosions cause fire?, 1 yes/ 0 no.");
      }
      bufferedwriter.newLine();
      bufferedwriter.newLine();
      
      bufferedwriter.close();
      switch (physics)
      {
      case 0: 
        copyFromjar("config/blockphysics/blockphysics.off.cfg", confF);
        break;
      case 1: 
        copyFromjar("config/blockphysics/blockphysics.vanilla.cfg", confF);
        break;
      case 2: 
        bufferedreader = new BufferedReader(new FileReader(new File(confDir(), "blockphysics.cfg")));
        bufferedwriter = new BufferedWriter(new FileWriter(confF, true));
        
        exc.add("cp");
        exc.add("es");
        exc.add("ef");
        for (s = ""; (s = bufferedreader.readLine()) != null;)
        {
          String sout = new String(s);
          
          s = s.replaceAll("#.*", "");
          s = s.replaceAll("\\s", "");
          s = s.toLowerCase();
          for (int cc = 0; cc < opttranslations.length; cc++) {
            s = s.replaceAll(opttranslations[cc][0], Matcher.quoteReplacement(opttranslations[cc][1]));
          }
          String[] as = s.split(":");
          if (!exc.contains(new String(as[0])))
          {
            bufferedwriter.write(sout);
            bufferedwriter.newLine();
          }
        }
        bufferedreader.close();
        bufferedwriter.close();
        
        break;
      case 3: 
        copyFromjar("config/blockphysics/blockphysics.easy.cfg", confF);
        break;
      case 4: 
        copyFromjar("config/blockphysics/blockphysics.hard.cfg", confF);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}