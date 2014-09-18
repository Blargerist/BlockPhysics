package blockphysics.asm;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BPTransformer
  implements IClassTransformer, Opcodes
{
  public byte[] transform(String name, String mcpName, byte[] bytes)
  {
    if (name.equals("adq")) {
      return transformChunk(bytes);
    }
    if (name.equals("adr")) {
      return transformExtendedBlockStorage(bytes);
    }
    if (name.equals("aed")) {
      return transformAnvilChunkLoader(bytes);
    }
    if (name.equals("anh")) {
      return transformBlockChest(bytes);
    }
    if (name.equals("anv")) {
      return transformBlockDispenser(bytes);
    }
    if (name.equals("aoh")) {
      return transformBlockFurnace(bytes);
    }
    if (name.equals("ast")) {
      return transformTileEntityPiston(bytes);
    }
    if (name.equals("blockphysics.BlockPhysics")) {
      return transformBlockPhysics(bytes);
    }
    if (name.equals("aqw")) {
      return transformBlock(bytes);
    }
    if (name.equals("ams")) {
      return transformBlockAnvil(bytes);
    }
    if (name.equals("any")) {
      return transformBlockDragonEgg(bytes);
    }
    if (name.equals("aoc")) {
      return transformBlockFarmland(bytes);
    }
    if (name.equals("asq")) {
      return transformBlockPistonBase(bytes);
    }
    if (name.equals("amv")) {
      return transformBlockRailBase(bytes);
    }
    if (name.equals("aqa")) {
      return transformBlockRedstoneLight(bytes);
    }
    if (name.equals("aop")) {
      return transformBlockSand(bytes);
    }
    if (name.equals("arb")) {
      return transformBlockTNT(bytes);
    }
    if (name.equals("arm")) {
      return transformBlockWeb(bytes);
    }
    if (name.equals("nm")) {
      return transformEntity(bytes);
    }
    if (name.equals("sp")) {
      return transformEntityBoat(bytes);
    }
    if (name.equals("sq")) {
      return transformEntityFallingSand(bytes);
    }
    if (name.equals("ss")) {
      return transformEntityMinecart(bytes);
    }
    if (name.equals("tb")) {
      return transformEntityTNTPrimed(bytes);
    }
    if (name.equals("jl")) {
      return transformEntityTracker(bytes);
    }
    if (name.equals("jw")) {
      return transformEntityTrackerEntry(bytes);
    }
    if (name.equals("nz")) {
      return transformEntityXPOrb(bytes);
    }
    if (name.equals("abq")) {
      return transformExplosion(bytes);
    }
    if (name.equals("auy")) {
      return transformGuiCreateWorld(bytes);
    }
    if (name.equals("awe")) {
      return transformGuiSelectWorld(bytes);
    }
    if (name.equals("net.minecraft.server.MinecraftServer")) {
      return transformMinecraftServer(bytes);
    }
    if (name.equals("bct")) {
      return transformNetClientHandler(bytes);
    }
    if (name.equals("bgl")) {
      return transformRenderFallingSand(bytes);
    }
    if (name.equals("abv")) {
      return transformWorld(bytes);
    }
    if (name.equals("jr")) {
      return transformWorldServer(bytes);
    }
    return bytes;
  }
  
  private byte[] transformTileEntityPiston(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching TileEntityPiston.class ........");
    
    boolean ok = false;boolean ok2 = false;boolean ok3 = false;boolean ok4 = false;boolean ok5 = false;
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("<init>")) && (m.desc.equals("(IIIZZ)V"))) {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            InsnList toInject = new InsnList();
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(1));
            toInject.add(new FieldInsnNode(181, "ast", "movingBlockTileEntityData", "Lbx;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(3));
            toInject.add(new FieldInsnNode(181, "ast", "bpmeta", "I"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            
            ok = true;
            break;
          }
        }
      } else if ((m.name.equals("f")) && (m.desc.equals("()V"))) {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("abv")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("g")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(IIII)V")))
          {
            InsnList toInject = new InsnList();
            
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "k", "Labv;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "l", "I"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "m", "I"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "n", "I"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "bpmeta", "I"));
            toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "setBlockBPdata", "(Labv;IIII)Z"));
            toInject.add(new InsnNode(87));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "movingBlockTileEntityData", "Lbx;"));
            LabelNode l1 = new LabelNode();
            toInject.add(new JumpInsnNode(198, l1));
            toInject.add(new FieldInsnNode(178, "aqw", "s", "[Laqw;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "a", "I"));
            toInject.add(new InsnNode(50));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "k", "Labv;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "b", "I"));
            toInject.add(new MethodInsnNode(182, "aqw", "createTileEntity", "(Labv;I)Lasm;"));
            toInject.add(new VarInsnNode(58, 1));
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "movingBlockTileEntityData", "Lbx;"));
            toInject.add(new MethodInsnNode(182, "asm", "a", "(Lbx;)V"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "k", "Labv;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "l", "I"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "m", "I"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "n", "I"));
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new MethodInsnNode(182, "abv", "a", "(IIILasm;)V"));
            toInject.add(l1);
            
            m.instructions.insert(m.instructions.get(index), toInject);
            
            ok2 = true;
            break;
          }
        }
      } else if ((m.name.equals("a")) && (m.desc.equals("(Lbx;)V"))) {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            InsnList toInject = new InsnList();
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("BPData"));
            toInject.add(new MethodInsnNode(182, "bx", "b", "(Ljava/lang/String;)Z"));
            LabelNode l1 = new LabelNode();
            toInject.add(new JumpInsnNode(153, l1));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("BPData"));
            toInject.add(new MethodInsnNode(182, "bx", "c", "(Ljava/lang/String;)B"));
            toInject.add(new FieldInsnNode(181, "ast", "bpmeta", "I"));
            toInject.add(l1);
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("TileEntityData"));
            toInject.add(new MethodInsnNode(182, "bx", "b", "(Ljava/lang/String;)Z"));
            LabelNode l0 = new LabelNode();
            toInject.add(new JumpInsnNode(153, l0));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("TileEntityData"));
            toInject.add(new MethodInsnNode(182, "bx", "l", "(Ljava/lang/String;)Lbx;"));
            toInject.add(new FieldInsnNode(181, "ast", "movingBlockTileEntityData", "Lbx;"));
            toInject.add(l0);
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            
            ok3 = true;
            break;
          }
        }
      } else if ((m.name.equals("b")) && (m.desc.equals("(Lbx;)V"))) {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            InsnList toInject = new InsnList();
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("BPData"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "bpmeta", "I"));
            toInject.add(new InsnNode(145));
            toInject.add(new MethodInsnNode(182, "bx", "a", "(Ljava/lang/String;B)V"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "movingBlockTileEntityData", "Lbx;"));
            LabelNode l0 = new LabelNode();
            toInject.add(new JumpInsnNode(198, l0));
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("TileEntityData"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "movingBlockTileEntityData", "Lbx;"));
            toInject.add(new MethodInsnNode(182, "bx", "a", "(Ljava/lang/String;Lbx;)V"));
            toInject.add(l0);
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            
            ok4 = true;
            break;
          }
        }
      } else if ((m.name.equals("h")) && (m.desc.equals("()V"))) {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("abv")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("g")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(IIII)V")))
          {
            InsnList toInject = new InsnList();
            
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "k", "Labv;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "l", "I"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "m", "I"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "n", "I"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "bpmeta", "I"));
            toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "setBlockBPdata", "(Labv;IIII)Z"));
            toInject.add(new InsnNode(87));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "movingBlockTileEntityData", "Lbx;"));
            LabelNode l0 = new LabelNode();
            toInject.add(new JumpInsnNode(198, l0));
            toInject.add(new FieldInsnNode(178, "aqw", "s", "[Laqw;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "a", "I"));
            toInject.add(new InsnNode(50));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "k", "Labv;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "b", "I"));
            toInject.add(new MethodInsnNode(182, "aqw", "createTileEntity", "(Labv;I)Lasm;"));
            toInject.add(new VarInsnNode(58, 1));
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "movingBlockTileEntityData", "Lbx;"));
            toInject.add(new MethodInsnNode(182, "asm", "a", "(Lbx;)V"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "k", "Labv;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "l", "I"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "m", "I"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "ast", "n", "I"));
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new MethodInsnNode(182, "abv", "a", "(IIILasm;)V"));
            toInject.add(l0);
            
            m.instructions.insert(m.instructions.get(index), toInject);
            
            ok5 = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2) && (ok3) && (ok4) && (ok5)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2 + ok3 + ok4 + ok5);
    }
    FieldVisitor fv = cw.visitField(1, "movingBlockTileEntityData", "Lbx;", null, null);
    fv.visitEnd();
    
    fv = cw.visitField(1, "bpmeta", "I", null, null);
    fv.visitEnd();
    
    cw.visitEnd();
    










    return cw.toByteArray();
  }
  
  private byte[] transformBlockFurnace(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockFurnace.class ............");
    
    boolean ok = false;
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;III)V"))) {
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 183) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("aoh")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("k")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(Labv;III)V")))
          {
            index -= 5;
            for (int i = 0; i < 6; i++) {
              m.instructions.remove(m.instructions.get(index));
            }
            ok = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlockDispenser(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockDispenser.class ..........");
    
    boolean ok = false;
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;III)V"))) {
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 183) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("anv")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("k")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(Labv;III)V")))
          {
            index -= 5;
            for (int i = 0; i < 6; i++) {
              m.instructions.remove(m.instructions.get(index));
            }
            ok = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlockChest(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockChest.class ..............");
    
    boolean ok = false;
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;III)V"))) {
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("anh")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("f_")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(Labv;III)V")))
          {
            index -= 5;
            for (int i = 0; i < 6; i++) {
              m.instructions.remove(m.instructions.get(index));
            }
            ok = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformAnvilChunkLoader(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching AnvilChunkLoader.class ........");
    
    boolean ok = false;boolean ok2 = false;boolean ok3 = false;boolean ok4 = false;boolean ok6 = false;boolean ok7 = false;
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Ladq;Labv;Lbx;)V")))
      {
        int var1 = 9;
        for (int index = 0; index < m.instructions.size(); index++)
        {
          if (ok3) {
            break;
          }
          if ((m.instructions.get(index).getOpcode() == 183) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("bx")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("<init>")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("()V"))) {
            for (int index2 = index; index2 < m.instructions.size(); index2++) {
              if (m.instructions.get(index2).getOpcode() == 58)
              {
                var1 = ((VarInsnNode)m.instructions.get(index2)).var;
                ok3 = true;
                break;
              }
            }
          }
        }
        int var2 = 11;
        for (int index = 0; index < m.instructions.size(); index++)
        {
          if (ok4) {
            break;
          }
          if ((m.instructions.get(index).getType() == 9) && (((LdcInsnNode)m.instructions.get(index)).cst.equals("Data"))) {
            for (int index2 = index; index2 < m.instructions.size(); index2++) {
              if (m.instructions.get(index2).getOpcode() == 25)
              {
                var2 = ((VarInsnNode)m.instructions.get(index2)).var;
                ok4 = true;
                break;
              }
            }
          }
        }
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("cf")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("a")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(Lck;)V")))
          {
            InsnList toInject = new InsnList();
            
            toInject.add(new VarInsnNode(25, var1));
            toInject.add(new LdcInsnNode("BPData"));
            toInject.add(new VarInsnNode(25, var2));
            toInject.add(new MethodInsnNode(182, "adr", "getBPdataArray", "()[B"));
            toInject.add(new MethodInsnNode(182, "bx", "a", "(Ljava/lang/String;[B)V"));
            
            m.instructions.insertBefore(m.instructions.get(index - 2), toInject);
            
            ok = true;
            break;
          }
        }
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Labv;Lbx;)Ladq;")))
      {
        int var1 = 11;
        for (int index = 0; index < m.instructions.size(); index++)
        {
          if (ok6) {
            break;
          }
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("cf")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("b")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(I)Lck;"))) {
            for (int index2 = index; index2 < m.instructions.size(); index2++) {
              if (m.instructions.get(index2).getOpcode() == 58)
              {
                var1 = ((VarInsnNode)m.instructions.get(index2)).var;
                ok6 = true;
                break;
              }
            }
          }
        }
        int var2 = 13;
        for (int index = 0; index < m.instructions.size(); index++)
        {
          if (ok7) {
            break;
          }
          if ((m.instructions.get(index).getOpcode() == 183) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("adr")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("<init>")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(IZ)V"))) {
            for (int index2 = index; index2 < m.instructions.size(); index2++) {
              if (m.instructions.get(index2).getOpcode() == 58)
              {
                var2 = ((VarInsnNode)m.instructions.get(index2)).var;
                ok7 = true;
                break;
              }
            }
          }
        }
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("adr")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("e")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("()V")))
          {
            InsnList toInject = new InsnList();
            
            toInject.add(new VarInsnNode(25, var1));
            toInject.add(new LdcInsnNode("BPData"));
            toInject.add(new MethodInsnNode(182, "bx", "b", "(Ljava/lang/String;)Z"));
            LabelNode l6 = new LabelNode();
            toInject.add(new JumpInsnNode(153, l6));
            toInject.add(new VarInsnNode(25, var2));
            toInject.add(new VarInsnNode(25, var1));
            toInject.add(new LdcInsnNode("BPData"));
            toInject.add(new MethodInsnNode(182, "bx", "j", "(Ljava/lang/String;)[B"));
            toInject.add(new MethodInsnNode(182, "adr", "setBPdataArray", "([B)V"));
            LabelNode l7 = new LabelNode();
            toInject.add(new JumpInsnNode(167, l7));
            toInject.add(l6);
            toInject.add(new FrameNode(3, 0, null, 0, null));
            toInject.add(new VarInsnNode(25, var2));
            toInject.add(new IntInsnNode(17, 4096));
            toInject.add(new IntInsnNode(188, 8));
            toInject.add(new MethodInsnNode(182, "adr", "setBPdataArray", "([B)V"));
            toInject.add(l7);
            
            m.instructions.insertBefore(m.instructions.get(index - 1), toInject);
            
            ok2 = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2) && (ok3) && (ok4) && (ok6) && (ok7)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2 + ok3 + ok4 + ok6 + ok7);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformExtendedBlockStorage(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching ExtendedBlockStorage.class ....");
    
    boolean ok = false;
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("<init>")) && (m.desc.equals("(IZ)V"))) {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            InsnList toInject = new InsnList();
            

            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new IntInsnNode(17, 4096));
            toInject.add(new IntInsnNode(188, 8));
            toInject.add(new FieldInsnNode(181, "adr", "blockBPdataArray", "[B"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            
            ok = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    
    MethodVisitor mv = cw.visitMethod(1, "getBlockBPdata", "(III)I", null, null);
    mv.visitCode();
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, "adr", "blockBPdataArray", "[B");
    mv.visitVarInsn(21, 2);
    mv.visitIntInsn(17, 256);
    mv.visitInsn(104);
    mv.visitVarInsn(21, 1);
    mv.visitIntInsn(16, 16);
    mv.visitInsn(104);
    mv.visitInsn(96);
    mv.visitVarInsn(21, 3);
    mv.visitInsn(96);
    mv.visitInsn(51);
    mv.visitInsn(172);
    mv.visitMaxs(4, 4);
    mv.visitEnd();
    
    mv = cw.visitMethod(1, "setBlockBPdata", "(IIII)V", null, null);
    mv.visitCode();
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, "adr", "blockBPdataArray", "[B");
    mv.visitVarInsn(21, 2);
    mv.visitIntInsn(17, 256);
    mv.visitInsn(104);
    mv.visitVarInsn(21, 1);
    mv.visitIntInsn(16, 16);
    mv.visitInsn(104);
    mv.visitInsn(96);
    mv.visitVarInsn(21, 3);
    mv.visitInsn(96);
    mv.visitVarInsn(21, 4);
    mv.visitInsn(145);
    mv.visitInsn(84);
    mv.visitInsn(177);
    mv.visitMaxs(4, 5);
    mv.visitEnd();
    
    mv = cw.visitMethod(1, "getBPdataArray", "()[B", null, null);
    mv.visitCode();
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, "adr", "blockBPdataArray", "[B");
    mv.visitInsn(176);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
    
    mv = cw.visitMethod(1, "setBPdataArray", "([B)V", null, null);
    mv.visitCode();
    mv.visitVarInsn(25, 0);
    mv.visitVarInsn(25, 1);
    mv.visitFieldInsn(181, "adr", "blockBPdataArray", "[B");
    mv.visitInsn(177);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
    


    FieldVisitor fv = cw.visitField(2, "blockBPdataArray", "[B", null, null);
    fv.visitEnd();
    
    cw.visitEnd();
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformChunk(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching Chunk.class ...................");
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    
    MethodVisitor mv = cw.visitMethod(1, "getBlockBPdata", "(III)I", null, null);
    mv.visitCode();
    mv.visitVarInsn(21, 2);
    mv.visitInsn(7);
    mv.visitInsn(122);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, "adq", "r", "[Ladr;");
    mv.visitInsn(190);
    Label l0 = new Label();
    mv.visitJumpInsn(161, l0);
    mv.visitInsn(3);
    mv.visitInsn(172);
    mv.visitLabel(l0);
    mv.visitFrame(3, 0, null, 0, null);
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, "adq", "r", "[Ladr;");
    mv.visitVarInsn(21, 2);
    mv.visitInsn(7);
    mv.visitInsn(122);
    mv.visitInsn(50);
    mv.visitVarInsn(58, 4);
    mv.visitVarInsn(25, 4);
    Label l1 = new Label();
    mv.visitJumpInsn(198, l1);
    mv.visitVarInsn(25, 4);
    mv.visitVarInsn(21, 1);
    mv.visitVarInsn(21, 2);
    mv.visitIntInsn(16, 15);
    mv.visitInsn(126);
    mv.visitVarInsn(21, 3);
    mv.visitMethodInsn(182, "adr", "getBlockBPdata", "(III)I");
    Label l2 = new Label();
    mv.visitJumpInsn(167, l2);
    mv.visitLabel(l1);
    mv.visitFrame(1, 1, new Object[] { "adr" }, 0, null);
    mv.visitInsn(3);
    mv.visitLabel(l2);
    mv.visitFrame(4, 0, null, 1, new Object[] { Opcodes.INTEGER });
    mv.visitInsn(172);
    mv.visitMaxs(4, 5);
    mv.visitEnd();
    

    mv = cw.visitMethod(1, "setBlockBPdata", "(IIII)Z", null, null);
    mv.visitCode();
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, "adq", "r", "[Ladr;");
    mv.visitVarInsn(21, 2);
    mv.visitInsn(7);
    mv.visitInsn(122);
    mv.visitInsn(50);
    mv.visitVarInsn(58, 5);
    mv.visitVarInsn(25, 5);
    Label lab0 = new Label();
    mv.visitJumpInsn(199, lab0);
    mv.visitInsn(3);
    mv.visitInsn(172);
    mv.visitLabel(lab0);
    mv.visitFrame(1, 1, new Object[] { "adr" }, 0, null);
    mv.visitVarInsn(25, 5);
    mv.visitVarInsn(21, 1);
    mv.visitVarInsn(21, 2);
    mv.visitIntInsn(16, 15);
    mv.visitInsn(126);
    mv.visitVarInsn(21, 3);
    mv.visitMethodInsn(182, "adr", "getBlockBPdata", "(III)I");
    mv.visitVarInsn(54, 6);
    mv.visitVarInsn(21, 6);
    mv.visitVarInsn(21, 4);
    Label lab1 = new Label();
    mv.visitJumpInsn(160, lab1);
    mv.visitInsn(3);
    mv.visitInsn(172);
    mv.visitLabel(lab1);
    mv.visitFrame(1, 1, new Object[] { Opcodes.INTEGER }, 0, null);
    mv.visitVarInsn(25, 0);
    mv.visitInsn(4);
    mv.visitFieldInsn(181, "adq", "l", "Z");
    mv.visitVarInsn(25, 5);
    mv.visitVarInsn(21, 1);
    mv.visitVarInsn(21, 2);
    mv.visitIntInsn(16, 15);
    mv.visitInsn(126);
    mv.visitVarInsn(21, 3);
    mv.visitVarInsn(21, 4);
    mv.visitMethodInsn(182, "adr", "setBlockBPdata", "(IIII)V");
    mv.visitInsn(4);
    mv.visitInsn(172);
    mv.visitMaxs(5, 7);
    mv.visitEnd();
    
    cw.visitEnd();
    
    System.out.println("OK");
    










    return cw.toByteArray();
  }
  
  private byte[] transformBlockPhysics(byte[] bytes)
  {
    try
    {
      Class.forName("org.bukkit.Bukkit");
      System.out.println("[BlockPhysics] Bukkit detected.");
    }
    catch (ClassNotFoundException e)
    {
      return bytes;
    }
    System.out.print("[BlockPhysics] Patching BlockPhysics.class ............");
    
    boolean ok = false;
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("tickBlocksRandomMove")) && (m.desc.equals("(Ljr;)V")))
      {
        m.instructions.clear();
        m.localVariables.clear();
        
        InsnList toInject = new InsnList();
        
        toInject.add(new FieldInsnNode(178, "blockphysics/BlockPhysics", "skipMove", "Z"));
        LabelNode l0 = new LabelNode();
        toInject.add(new JumpInsnNode(153, l0));
        toInject.add(new InsnNode(177));
        toInject.add(l0);
        toInject.add(new FrameNode(3, 0, null, 0, null));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "jr", "G", "Lgnu/trove/map/hash/TLongShortHashMap;"));
        toInject.add(new MethodInsnNode(182, "gnu/trove/map/hash/TLongShortHashMap", "iterator", "()Lgnu/trove/iterator/TLongShortIterator;"));
        toInject.add(new VarInsnNode(58, 1));
        LabelNode l1 = new LabelNode();
        toInject.add(l1);
        toInject.add(new FrameNode(1, 1, new Object[] { "gnu/trove/iterator/TLongShortIterator" }, 0, null));
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new MethodInsnNode(185, "gnu/trove/iterator/TLongShortIterator", "hasNext", "()Z"));
        LabelNode l2 = new LabelNode();
        toInject.add(new JumpInsnNode(153, l2));
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new MethodInsnNode(185, "gnu/trove/iterator/TLongShortIterator", "advance", "()V"));
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new MethodInsnNode(185, "gnu/trove/iterator/TLongShortIterator", "key", "()J"));
        toInject.add(new VarInsnNode(55, 2));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new InsnNode(87));
        toInject.add(new VarInsnNode(22, 2));
        toInject.add(new MethodInsnNode(184, "abv", "keyToX", "(J)I"));
        toInject.add(new VarInsnNode(54, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new InsnNode(87));
        toInject.add(new VarInsnNode(22, 2));
        toInject.add(new MethodInsnNode(184, "abv", "keyToZ", "(J)I"));
        toInject.add(new VarInsnNode(54, 5));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new IntInsnNode(16, 16));
        toInject.add(new InsnNode(104));
        toInject.add(new VarInsnNode(54, 6));
        toInject.add(new VarInsnNode(21, 5));
        toInject.add(new IntInsnNode(16, 16));
        toInject.add(new InsnNode(104));
        toInject.add(new VarInsnNode(54, 7));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(21, 5));
        toInject.add(new MethodInsnNode(182, "jr", "e", "(II)Ladq;"));
        toInject.add(new VarInsnNode(58, 8));
        toInject.add(new VarInsnNode(25, 8));
        toInject.add(new MethodInsnNode(182, "adq", "i", "()[Ladr;"));
        toInject.add(new VarInsnNode(58, 12));
        toInject.add(new VarInsnNode(25, 12));
        toInject.add(new InsnNode(190));
        toInject.add(new VarInsnNode(54, 9));
        toInject.add(new InsnNode(3));
        toInject.add(new VarInsnNode(54, 10));
        LabelNode l3 = new LabelNode();
        toInject.add(l3);
        toInject.add(new FrameNode(0, 12, new Object[] { "jr", "gnu/trove/iterator/TLongShortIterator", Opcodes.LONG, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, "adq", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, "[Ladr;" }, 0, new Object[0]));
        toInject.add(new VarInsnNode(21, 10));
        toInject.add(new VarInsnNode(21, 9));
        LabelNode l4 = new LabelNode();
        toInject.add(new JumpInsnNode(162, l4));
        toInject.add(new VarInsnNode(25, 12));
        toInject.add(new VarInsnNode(21, 10));
        toInject.add(new InsnNode(50));
        toInject.add(new VarInsnNode(58, 13));
        toInject.add(new VarInsnNode(25, 13));
        LabelNode l5 = new LabelNode();
        toInject.add(new JumpInsnNode(198, l5));
        toInject.add(new InsnNode(3));
        toInject.add(new VarInsnNode(54, 14));
        LabelNode l6 = new LabelNode();
        toInject.add(l6);
        toInject.add(new FrameNode(1, 2, new Object[] { "adr", Opcodes.INTEGER }, 0, null));
        toInject.add(new VarInsnNode(21, 14));
        toInject.add(new InsnNode(6));
        toInject.add(new JumpInsnNode(162, l5));
        toInject.add(new FieldInsnNode(178, "blockphysics/BlockPhysics", "updateLCG", "I"));
        toInject.add(new InsnNode(6));
        toInject.add(new InsnNode(104));
        toInject.add(new LdcInsnNode(new Integer(1013904223)));
        toInject.add(new InsnNode(96));
        toInject.add(new FieldInsnNode(179, "blockphysics/BlockPhysics", "updateLCG", "I"));
        toInject.add(new FieldInsnNode(178, "blockphysics/BlockPhysics", "updateLCG", "I"));
        toInject.add(new InsnNode(5));
        toInject.add(new InsnNode(122));
        toInject.add(new VarInsnNode(54, 11));
        toInject.add(new VarInsnNode(21, 11));
        toInject.add(new IntInsnNode(16, 15));
        toInject.add(new InsnNode(126));
        toInject.add(new VarInsnNode(54, 15));
        toInject.add(new VarInsnNode(21, 11));
        toInject.add(new IntInsnNode(16, 8));
        toInject.add(new InsnNode(122));
        toInject.add(new IntInsnNode(16, 15));
        toInject.add(new InsnNode(126));
        toInject.add(new VarInsnNode(54, 16));
        toInject.add(new VarInsnNode(21, 11));
        toInject.add(new IntInsnNode(16, 16));
        toInject.add(new InsnNode(122));
        toInject.add(new IntInsnNode(16, 15));
        toInject.add(new InsnNode(126));
        toInject.add(new VarInsnNode(54, 17));
        toInject.add(new VarInsnNode(25, 13));
        toInject.add(new VarInsnNode(21, 15));
        toInject.add(new VarInsnNode(21, 17));
        toInject.add(new VarInsnNode(21, 16));
        toInject.add(new MethodInsnNode(182, "adr", "a", "(III)I"));
        toInject.add(new VarInsnNode(54, 18));
        toInject.add(new VarInsnNode(25, 13));
        toInject.add(new VarInsnNode(21, 15));
        toInject.add(new VarInsnNode(21, 17));
        toInject.add(new VarInsnNode(21, 16));
        toInject.add(new MethodInsnNode(182, "adr", "b", "(III)I"));
        toInject.add(new VarInsnNode(54, 19));
        toInject.add(new FieldInsnNode(178, "blockphysics/BlockPhysics", "blockSet", "[[Lblockphysics/BlockDef;"));
        toInject.add(new VarInsnNode(21, 18));
        toInject.add(new InsnNode(50));
        toInject.add(new VarInsnNode(21, 19));
        toInject.add(new InsnNode(50));
        toInject.add(new FieldInsnNode(180, "blockphysics/BlockDef", "randomtick", "Z"));
        LabelNode l7 = new LabelNode();
        toInject.add(new JumpInsnNode(153, l7));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(21, 15));
        toInject.add(new VarInsnNode(21, 6));
        toInject.add(new InsnNode(96));
        toInject.add(new VarInsnNode(21, 17));
        toInject.add(new VarInsnNode(25, 13));
        toInject.add(new MethodInsnNode(182, "adr", "d", "()I"));
        toInject.add(new InsnNode(96));
        toInject.add(new VarInsnNode(21, 16));
        toInject.add(new VarInsnNode(21, 7));
        toInject.add(new InsnNode(96));
        toInject.add(new VarInsnNode(21, 18));
        toInject.add(new VarInsnNode(21, 19));
        toInject.add(new InsnNode(3));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "tryToMove", "(Labv;IIIIIZ)Z"));
        toInject.add(new InsnNode(87));
        toInject.add(l7);
        toInject.add(new FrameNode(0, 14, new Object[] { "jr", "gnu/trove/iterator/TLongShortIterator", Opcodes.LONG, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, "adq", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, "[Ladr;", "adr", Opcodes.INTEGER }, 0, new Object[0]));
        toInject.add(new IincInsnNode(14, 1));
        toInject.add(new JumpInsnNode(167, l6));
        toInject.add(l5);
        toInject.add(new FrameNode(0, 12, new Object[] { "jr", "gnu/trove/iterator/TLongShortIterator", Opcodes.LONG, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, "adq", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, "[Ladr;" }, 0, new Object[0]));
        toInject.add(new IincInsnNode(10, 1));
        toInject.add(new JumpInsnNode(167, l3));
        toInject.add(l4);
        toInject.add(new FrameNode(0, 2, new Object[] { "jr", "gnu/trove/iterator/TLongShortIterator" }, 0, new Object[0]));
        toInject.add(new JumpInsnNode(167, l1));
        toInject.add(l2);
        toInject.add(new FrameNode(2, 1, null, 0, null));
        toInject.add(new InsnNode(177));
        
        m.instructions.add(toInject);
        
        ok = true;
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformEntity(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching Entity.class ..................");
    
    boolean ok = false;
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("C")) && (m.desc.equals("()V"))) {
        for (int index = 0; index < m.instructions.size() - 1; index++) {
          if ((m.instructions.get(index).getType() == 9) && (((LdcInsnNode)m.instructions.get(index)).cst.equals(Double.valueOf(0.001D))))
          {
            ok = true;
            m.instructions.set(m.instructions.get(index), new LdcInsnNode(new Double("0.07")));
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformEntityMinecart(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching EntityMinecart.class ..........");
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    
    MethodVisitor mv = cw.visitMethod(1, "al", "()V", null, null);
    mv.visitCode();
    mv.visitInsn(177);
    mv.visitMaxs(0, 1);
    mv.visitEnd();
    
    cw.visitEnd();
    
    System.out.println("OK");
    










    return cw.toByteArray();
  }
  
  private byte[] transformEntityXPOrb(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching EntityXPOrb.class .............");
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    
    MethodVisitor mv = cw.visitMethod(1, "al", "()V", null, null);
    mv.visitCode();
    mv.visitInsn(177);
    mv.visitMaxs(0, 1);
    mv.visitEnd();
    
    cw.visitEnd();
    
    System.out.println("OK");
    










    return cw.toByteArray();
  }
  
  private byte[] transformEntityBoat(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching EntityBoat.class ..............");
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    
    MethodVisitor mv = cw.visitMethod(1, "al", "()V", null, null);
    mv.visitCode();
    mv.visitInsn(177);
    mv.visitMaxs(0, 1);
    mv.visitEnd();
    
    cw.visitEnd();
    
    System.out.println("OK");
    










    return cw.toByteArray();
  }
  
  private byte[] transformBlockWeb(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockWeb.class ................");
    boolean ok = false;
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIILnm;)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "arm", "cF", "I"));
        toInject.add(new VarInsnNode(25, 5));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onEntityCollidedWithBlock", "(Labv;IIIILnm;)V"));
        toInject.add(new InsnNode(177));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        
        ok = true;
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformWorld(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching World.class ...................");
    boolean ok = false;boolean ok2 = false;
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if (m.name.equals("<init>")) {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            InsnList toInject = new InsnList();
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new TypeInsnNode(187, "blockphysics/BTickList"));
            toInject.add(new InsnNode(89));
            toInject.add(new MethodInsnNode(183, "blockphysics/BTickList", "<init>", "()V"));
            toInject.add(new FieldInsnNode(181, "abv", "moveTickList", "Lblockphysics/BTickList;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new TypeInsnNode(187, "java/util/HashSet"));
            toInject.add(new InsnNode(89));
            toInject.add(new MethodInsnNode(183, "java/util/HashSet", "<init>", "()V"));
            toInject.add(new FieldInsnNode(181, "abv", "pistonMoveBlocks", "Ljava/util/HashSet;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new TypeInsnNode(187, "blockphysics/ExplosionQueue"));
            toInject.add(new InsnNode(89));
            toInject.add(new MethodInsnNode(183, "blockphysics/ExplosionQueue", "<init>", "()V"));
            toInject.add(new FieldInsnNode(181, "abv", "explosionQueue", "Lblockphysics/ExplosionQueue;"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
          }
        }
      } else if ((!m.name.equals("a")) || (!m.desc.equals("(Lnm;DDDFZZ)Labq;"))) {}
      for (int index = m.instructions.size() - 1; index > 0; index--) {
        if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("abq")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("a")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(Z)V")))
        {
          while ((m.instructions.get(index).getOpcode() != 182) || (m.instructions.get(index).getType() != 5) || (!((MethodInsnNode)m.instructions.get(index)).owner.equals("abq")) || (!((MethodInsnNode)m.instructions.get(index)).name.equals("a")) || (!((MethodInsnNode)m.instructions.get(index)).desc.equals("()V")))
          {
            m.instructions.remove(m.instructions.get(index));
            index--;
          }
          m.instructions.remove(m.instructions.get(index - 1));
          m.instructions.remove(m.instructions.get(index - 1));
          
          InsnList toInject = new InsnList();
          toInject.add(new VarInsnNode(25, 0));
          toInject.add(new FieldInsnNode(180, "abv", "explosionQueue", "Lblockphysics/ExplosionQueue;"));
          toInject.add(new VarInsnNode(25, 11));
          toInject.add(new MethodInsnNode(182, "blockphysics/ExplosionQueue", "add", "(Labq;)V"));
          
          m.instructions.insertBefore(m.instructions.get(index - 1), toInject);
          
          ok2 = true;
          break;
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    


    FieldVisitor fv = cw.visitField(1, "moveTickList", "Lblockphysics/BTickList;", null, null);
    fv.visitEnd();
    
    fv = cw.visitField(1, "pistonMoveBlocks", "Ljava/util/HashSet;", "Ljava/util/HashSet<Ljava/lang/String;>;", null);
    fv.visitEnd();
    
    fv = cw.visitField(1, "explosionQueue", "Lblockphysics/ExplosionQueue;", null, null);
    fv.visitEnd();
    
    cw.visitEnd();
    if ((ok) && (ok2)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformWorldServer(byte[] bytes)
  {
    boolean ok = false;boolean ok2 = false;
    
    boolean bukkit = false;
    try
    {
      Class.forName("org.bukkit.Bukkit");
      System.out.println("[BlockPhysics] Bukkit detected.");
      bukkit = true;
      ok2 = true;
    }
    catch (ClassNotFoundException e) {}
    System.out.print("[BlockPhysics] Patching WorldServer.class .............");
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("b")) && (m.desc.equals("()V"))) {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            InsnList toInject = new InsnList();
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "tickBlocksRandomMove", "(Ljr;)V"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "jr", "moveTickList", "Lblockphysics/BTickList;"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new MethodInsnNode(182, "blockphysics/BTickList", "tickMoveUpdates", "(Labv;)V"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "jr", "pistonMoveBlocks", "Ljava/util/HashSet;"));
            toInject.add(new MethodInsnNode(182, "java/util/HashSet", "clear", "()V"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "jr", "explosionQueue", "Lblockphysics/ExplosionQueue;"));
            toInject.add(new MethodInsnNode(182, "blockphysics/ExplosionQueue", "doNextExplosion", "()V"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            
            ok = true;
            break;
          }
        }
      } else if ((bukkit) || (!m.name.equals("a")) || (!m.desc.equals("(Lnm;DDDFZZ)Labq;"))) {}
      for (int index = m.instructions.size() - 1; index > 0; index--) {
        if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("abq")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("a")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(Z)V")))
        {
          while ((m.instructions.get(index).getOpcode() != 182) || (m.instructions.get(index).getType() != 5) || (!((MethodInsnNode)m.instructions.get(index)).owner.equals("abq")) || (!((MethodInsnNode)m.instructions.get(index)).name.equals("a")) || (!((MethodInsnNode)m.instructions.get(index)).desc.equals("()V")))
          {
            m.instructions.remove(m.instructions.get(index));
            index--;
          }
          m.instructions.remove(m.instructions.get(index - 1));
          m.instructions.remove(m.instructions.get(index - 1));
          
          InsnList toInject = new InsnList();
          toInject.add(new VarInsnNode(25, 0));
          toInject.add(new FieldInsnNode(180, "jr", "explosionQueue", "Lblockphysics/ExplosionQueue;"));
          toInject.add(new VarInsnNode(25, 11));
          toInject.add(new MethodInsnNode(182, "blockphysics/ExplosionQueue", "add", "(Labq;)V"));
          
          m.instructions.insertBefore(m.instructions.get(index - 1), toInject);
          
          ok2 = true;
          break;
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformExplosion(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching Explosion.class ...............");
    boolean ok = false;boolean ok2 = false;boolean ok3 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    


    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("<init>")) && (m.desc.equals("(Labv;Lnm;DDDF)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new InsnNode(3));
        toInject.add(new FieldInsnNode(181, "abq", "impact", "Z"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
            break;
          }
        }
      }
      else if ((m.name.equals("a")) && (m.desc.equals("()V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "abq", "k", "Labv;"));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "doExplosionA", "(Labv;Labq;)V"));
        toInject.add(new InsnNode(177));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok2 = true;
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Z)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "abq", "k", "Labv;"));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(21, 1));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "doExplosionB", "(Labv;Labq;Z)V"));
        toInject.add(new InsnNode(177));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok3 = true;
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    

    FieldVisitor fv = cw.visitField(1, "impact", "Z", null, null);
    fv.visitEnd();
    
    cw.visitEnd();
    if ((ok) && (ok2) && (ok3)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2 + ok3);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformMinecraftServer(byte[] bytes)
  {
    boolean bukkit = false;
    try
    {
      Class.forName("org.bukkit.Bukkit");
      System.out.println("[BlockPhysics] Bukkit detected.");
      bukkit = true;
    }
    catch (ClassNotFoundException e) {}
    System.out.print("[BlockPhysics] Patching MinecraftServer.class .........");
    boolean ok = false;
    boolean ok2 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("run")) && (m.desc.equals("()V"))) {
        if (bukkit)
        {
          for (int index = 0; index < m.instructions.size(); index++) {
            if ((m.instructions.get(index).getType() == 5) && (m.instructions.get(index).getOpcode() == 184) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("java/lang/System")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("nanoTime")))
            {
              ok = true;
              break;
            }
          }
          if (ok) {
            while (index < m.instructions.size())
            {
              if (m.instructions.get(index).getOpcode() == 101)
              {
                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "setSkipMoveM", "(J)J"));
                
                m.instructions.insert(m.instructions.get(index), toInject);
                
                ok2 = true;
                break;
              }
              index++;
            }
          }
        }
        else
        {
          for (int index = 0; index < m.instructions.size(); index++) {
            if ((m.instructions.get(index).getType() == 5) && (m.instructions.get(index).getOpcode() == 182) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("net/minecraft/server/MinecraftServer")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("s")))
            {
              ok = true;
              break;
            }
          }
          if (ok) {
            while (index > 0)
            {
              if ((m.instructions.get(index).getOpcode() == 22) && (m.instructions.get(index).getType() == 2) && (((VarInsnNode)m.instructions.get(index)).var == 3))
              {
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(22, 7));
                toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "setSkipMove", "(J)V"));
                
                m.instructions.insertBefore(m.instructions.get(index), toInject);
                
                ok2 = true;
                break;
              }
              index--;
            }
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformEntityTrackerEntry(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching EntityTrackerEntry.class ......");
    boolean ok = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("c")) && (m.desc.equals("()Lex;"))) {
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getType() == 3) && (m.instructions.get(index).getOpcode() == 193) && (((TypeInsnNode)m.instructions.get(index)).desc.equals("sq")))
          {
            while (m.instructions.get(index).getOpcode() != 153) {
              index++;
            }
            index++;
            while (m.instructions.get(index).getOpcode() != 176) {
              m.instructions.remove(m.instructions.get(index));
            }
            InsnList toInject = new InsnList();
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "jw", "a", "Lnm;"));
            toInject.add(new TypeInsnNode(192, "sq"));
            toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "spawnFallingSandPacket", "(Lsq;)Ldc;"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformEntityTracker(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching EntityTracker.class ...........");
    boolean ok = false;
    boolean ok2 = false;
    boolean ok3 = false;
    boolean ok4 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("<init>")) && (m.desc.equals("(Ljr;)V")))
      {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            InsnList toInject = new InsnList();
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(3));
            toInject.add(new FieldInsnNode(181, "jl", "movingblocks", "I"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
            break;
          }
        }
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Lnm;)V")))
      {
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getType() == 3) && (m.instructions.get(index).getOpcode() == 193) && (((TypeInsnNode)m.instructions.get(index)).desc.equals("tb")))
          {
            m.instructions.remove(m.instructions.get(index - 1));
            while ((m.instructions.get(index).getType() != 3) || (m.instructions.get(index).getOpcode() != 193))
            {
              m.instructions.remove(m.instructions.get(index - 1));
              ok2 = true;
            }
          }
        }
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getType() == 3) && (m.instructions.get(index).getOpcode() == 193) && (((TypeInsnNode)m.instructions.get(index)).desc.equals("sq")))
          {
            while (m.instructions.get(index).getOpcode() != 167)
            {
              index++;
              if ((m.instructions.get(index).getType() == 1) && (m.instructions.get(index).getOpcode() == 16) && (((IntInsnNode)m.instructions.get(index)).operand == 20))
              {
                m.instructions.set(m.instructions.get(index), new IntInsnNode(16, 40));
                ok3 = true;
              }
            }
            InsnList toInject = new InsnList();
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(89));
            toInject.add(new FieldInsnNode(180, "jl", "movingblocks", "I"));
            toInject.add(new InsnNode(4));
            toInject.add(new InsnNode(96));
            toInject.add(new FieldInsnNode(181, "jl", "movingblocks", "I"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            break;
          }
        }
      }
      else if ((m.name.equals("b")) && (m.desc.equals("(Lnm;)V")))
      {
        InsnList toInject = new InsnList();
        
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new TypeInsnNode(193, "sq"));
        LabelNode l3 = new LabelNode();
        toInject.add(new JumpInsnNode(153, l3));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new InsnNode(89));
        toInject.add(new FieldInsnNode(180, "jl", "movingblocks", "I"));
        toInject.add(new InsnNode(4));
        toInject.add(new InsnNode(100));
        toInject.add(new FieldInsnNode(181, "jl", "movingblocks", "I"));
        toInject.add(l3);
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok4 = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    


    FieldVisitor fv = cw.visitField(1, "movingblocks", "I", null, null);
    fv.visitEnd();
    
    cw.visitEnd();
    if ((ok) && (ok2) && (ok3) && (ok4)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2 + ok3 + ok4);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformEntityTNTPrimed(byte[] bytes)
  {
    boolean bukkit = false;
    try
    {
      Class.forName("org.bukkit.Bukkit");
      System.out.println("[BlockPhysics] Bukkit detected.");
      bukkit = true;
    }
    catch (ClassNotFoundException e) {}
    System.out.print("[BlockPhysics] Patching EntityTNTPrimed.class .........");
    boolean ok = false;boolean ok2 = false;boolean ok5 = false;boolean ok6 = false;boolean ok7 = false;boolean ok8 = false;boolean ok9 = false;boolean ok10 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    
    classNode.superName = "sq";
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("<init>")) && (m.desc.equals("(Labv;)V")))
      {
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 183) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("nm")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("<init>")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(Labv;)V")))
          {
            m.instructions.set(m.instructions.get(index), new MethodInsnNode(183, "sq", "<init>", "(Labv;)V"));
            ok = true;
            break;
          }
        }
      }
      else if ((m.name.equals("<init>")) && (m.desc.equals("(Labv;DDDLoe;)V")))
      {
        InsnList toInject = new InsnList();
        
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(24, 2));
        toInject.add(new VarInsnNode(24, 4));
        toInject.add(new VarInsnNode(24, 6));
        toInject.add(new IntInsnNode(16, 46));
        toInject.add(new InsnNode(3));
        toInject.add(new MethodInsnNode(183, "sq", "<init>", "(Labv;DDDII)V"));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new IntInsnNode(16, 80));
        toInject.add(new FieldInsnNode(181, "tb", "a", "I"));
        if (bukkit)
        {
          toInject.add(new VarInsnNode(25, 0));
          toInject.add(new LdcInsnNode(new Float("4.0")));
          toInject.add(new FieldInsnNode(181, "tb", "yield", "F"));
          toInject.add(new VarInsnNode(25, 0));
          toInject.add(new InsnNode(3));
          toInject.add(new FieldInsnNode(181, "tb", "isIncendiary", "Z"));
        }
        toInject.add(new InsnNode(177));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        
        ok2 = true;
      }
      else if ((m.name.equals("K")) && (m.desc.equals("()Z")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new InsnNode(3));
        toInject.add(new InsnNode(172));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok5 = true;
      }
      else if ((m.name.equals("l_")) && (m.desc.equals("()V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "tb", "q", "Labv;"));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "fallingSandUpdate", "(Labv;Lsq;)V"));
        toInject.add(new InsnNode(177));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok6 = true;
      }
      else if ((m.name.equals("b")) && (m.desc.equals("(Lbx;)V")))
      {
        InsnList toInject = new InsnList();
        
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new MethodInsnNode(183, "sq", "b", "(Lbx;)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok7 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Lbx;)V")))
      {
        InsnList toInject = new InsnList();
        
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new MethodInsnNode(183, "sq", "a", "(Lbx;)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok8 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("d")) && (m.desc.equals("()V")))
      {
        m.access = 1;
        ok9 = true;
        if (bukkit) {
          for (int index = 0; index < m.instructions.size(); index++) {
            if ((m.instructions.get(index).getOpcode() == 184) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.contains("entity/CraftEntity")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("getEntity")))
            {
              String pclass = ((MethodInsnNode)m.instructions.get(index)).owner.replace("entity/CraftEntity", "");
              m.instructions.set(m.instructions.get(index), new MethodInsnNode(183, pclass + "entity/CraftTNTPrimed", "<init>", "(L" + pclass + "CraftServer;Ltb;)V"));
              
              InsnList toInject = new InsnList();
              
              toInject.add(new TypeInsnNode(187, pclass + "entity/CraftTNTPrimed"));
              toInject.add(new InsnNode(89));
              
              m.instructions.insert(m.instructions.get(index - 3), toInject);
              
              ok10 = true;
              break;
            }
          }
        } else {
          for (int index = 0; index < m.instructions.size(); index++) {
            if (m.instructions.get(index).getOpcode() == 1)
            {
              m.instructions.set(m.instructions.get(index), new VarInsnNode(25, 0));
              ok10 = true;
              break;
            }
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2) && (ok5) && (ok6) && (ok7) && (ok8) && (ok9)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2 + ok5 + ok6 + ok7 + ok8 + ok9);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformEntityFallingSand(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching EntityFallingSand.class .......");
    boolean ok = false;
    boolean ok2 = false;
    boolean ok3 = false;
    boolean ok4 = false;
    boolean ok5 = false;
    boolean ok6 = false;
    boolean ok7 = false;
    boolean ok8 = false;
    boolean ok9 = false;boolean ok10 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("<init>")) && (m.desc.equals("(Labv;)V")))
      {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            InsnList toInject = new InsnList();
            
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(4));
            toInject.add(new FieldInsnNode(181, "sq", "m", "Z"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new LdcInsnNode(new Float("0.996")));
            toInject.add(new LdcInsnNode(new Float("0.996")));
            toInject.add(new MethodInsnNode(182, "sq", "a", "(FF)V"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "sq", "P", "F"));
            toInject.add(new InsnNode(13));
            toInject.add(new InsnNode(110));
            toInject.add(new FieldInsnNode(181, "sq", "N", "F"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(14));
            toInject.add(new FieldInsnNode(181, "sq", "x", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(14));
            toInject.add(new FieldInsnNode(181, "sq", "y", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(14));
            toInject.add(new FieldInsnNode(181, "sq", "z", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(14));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationX", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new LdcInsnNode(new Double("-0.024525")));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationY", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(14));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationZ", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(3));
            toInject.add(new FieldInsnNode(181, "sq", "slideDir", "B"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(4));
            toInject.add(new FieldInsnNode(181, "sq", "Z", "Z"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new LdcInsnNode(new Float("0.8")));
            toInject.add(new FieldInsnNode(181, "sq", "aa", "F"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(7));
            toInject.add(new FieldInsnNode(181, "sq", "dead", "B"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(3));
            toInject.add(new FieldInsnNode(181, "sq", "bpdata", "I"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok10 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("<init>")) && (m.desc.equals("(Labv;DDDII)V")))
      {
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("sq")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("a")))
          {
            m.instructions.set(m.instructions.get(index - 1), new LdcInsnNode(new Float("0.996")));
            m.instructions.set(m.instructions.get(index - 2), new LdcInsnNode(new Float("0.996")));
            ok = true;
            break;
          }
        }
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            InsnList toInject = new InsnList();
            
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(14));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationX", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new LdcInsnNode(new Double("-0.024525")));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationY", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(14));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationZ", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(3));
            toInject.add(new FieldInsnNode(181, "sq", "slideDir", "B"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(4));
            toInject.add(new FieldInsnNode(181, "sq", "Z", "Z"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new LdcInsnNode(new Float("0.8")));
            toInject.add(new FieldInsnNode(181, "sq", "aa", "F"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(7));
            toInject.add(new FieldInsnNode(181, "sq", "dead", "B"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(3));
            toInject.add(new FieldInsnNode(181, "sq", "bpdata", "I"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok2 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("K")) && (m.desc.equals("()Z")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new InsnNode(3));
        toInject.add(new InsnNode(172));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok9 = true;
      }
      else if ((m.name.equals("l_")) && (m.desc.equals("()V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "sq", "q", "Labv;"));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "fallingSandUpdate", "(Labv;Lsq;)V"));
        toInject.add(new InsnNode(177));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok3 = true;
      }
      else if ((m.name.equals("b")) && (m.desc.equals("(F)V")))
      {
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(new InsnNode(177));
        ok4 = true;
      }
      else if ((m.name.equals("b")) && (m.desc.equals("(Lbx;)V")))
      {
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            InsnList toInject = new InsnList();
            
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("Acceleration"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(6));
            toInject.add(new IntInsnNode(188, 7));
            toInject.add(new InsnNode(89));
            toInject.add(new InsnNode(3));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "sq", "accelerationX", "D"));
            toInject.add(new InsnNode(82));
            toInject.add(new InsnNode(89));
            toInject.add(new InsnNode(4));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "sq", "accelerationY", "D"));
            toInject.add(new InsnNode(82));
            toInject.add(new InsnNode(89));
            toInject.add(new InsnNode(5));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "sq", "accelerationZ", "D"));
            toInject.add(new InsnNode(82));
            toInject.add(new MethodInsnNode(182, "sq", "a", "([D)Lcf;"));
            toInject.add(new MethodInsnNode(182, "bx", "a", "(Ljava/lang/String;Lck;)V"));
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("BPData"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new FieldInsnNode(180, "sq", "bpdata", "I"));
            toInject.add(new InsnNode(145));
            toInject.add(new MethodInsnNode(182, "bx", "a", "(Ljava/lang/String;B)V"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok5 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Lbx;)V")))
      {
        while ((m.instructions.get(0).getType() != 9) || (!((LdcInsnNode)m.instructions.get(0)).cst.equals("Data")))
        {
          m.instructions.remove(m.instructions.get(0));
          ok6 = true;
        }
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "readFallingSandID", "(Lbx;)I"));
        toInject.add(new FieldInsnNode(181, "sq", "a", "I"));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(25, 1));
        
        m.instructions.insertBefore(m.instructions.get(0), toInject);
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            toInject = new InsnList();
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("Acceleration"));
            toInject.add(new MethodInsnNode(182, "bx", "b", "(Ljava/lang/String;)Z"));
            LabelNode l4 = new LabelNode();
            toInject.add(new JumpInsnNode(153, l4));
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("Acceleration"));
            toInject.add(new MethodInsnNode(182, "bx", "m", "(Ljava/lang/String;)Lcf;"));
            toInject.add(new VarInsnNode(58, 2));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new VarInsnNode(25, 2));
            toInject.add(new InsnNode(3));
            toInject.add(new MethodInsnNode(182, "cf", "b", "(I)Lck;"));
            toInject.add(new TypeInsnNode(192, "ca"));
            toInject.add(new FieldInsnNode(180, "ca", "a", "D"));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationX", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new VarInsnNode(25, 2));
            toInject.add(new InsnNode(4));
            toInject.add(new MethodInsnNode(182, "cf", "b", "(I)Lck;"));
            toInject.add(new TypeInsnNode(192, "ca"));
            toInject.add(new FieldInsnNode(180, "ca", "a", "D"));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationY", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new VarInsnNode(25, 2));
            toInject.add(new InsnNode(5));
            toInject.add(new MethodInsnNode(182, "cf", "b", "(I)Lck;"));
            toInject.add(new TypeInsnNode(192, "ca"));
            toInject.add(new FieldInsnNode(180, "ca", "a", "D"));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationZ", "D"));
            LabelNode l5 = new LabelNode();
            toInject.add(new JumpInsnNode(167, l5));
            toInject.add(l4);
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(14));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationX", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(14));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationY", "D"));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(14));
            toInject.add(new FieldInsnNode(181, "sq", "accelerationZ", "D"));
            toInject.add(l5);
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("BPData"));
            toInject.add(new MethodInsnNode(182, "bx", "b", "(Ljava/lang/String;)Z"));
            LabelNode l7 = new LabelNode();
            toInject.add(new JumpInsnNode(153, l7));
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new VarInsnNode(25, 1));
            toInject.add(new LdcInsnNode("BPData"));
            toInject.add(new MethodInsnNode(182, "bx", "c", "(Ljava/lang/String;)B"));
            toInject.add(new FieldInsnNode(181, "sq", "bpdata", "I"));
            LabelNode l8 = new LabelNode();
            toInject.add(new JumpInsnNode(167, l8));
            toInject.add(l7);
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new InsnNode(3));
            toInject.add(new FieldInsnNode(181, "sq", "bpdata", "I"));
            toInject.add(l8);
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            
            ok7 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("au")) && (m.desc.equals("()Z")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new MethodInsnNode(182, "sq", "ae", "()Z"));
        toInject.add(new InsnNode(172));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok8 = true;
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    


    FieldVisitor fv = cw.visitField(1, "accelerationX", "D", null, null);
    fv.visitEnd();
    
    fv = cw.visitField(1, "accelerationY", "D", null, null);
    fv.visitEnd();
    
    fv = cw.visitField(1, "accelerationZ", "D", null, null);
    fv.visitEnd();
    
    fv = cw.visitField(1, "bpdata", "I", null, null);
    fv.visitEnd();
    
    fv = cw.visitField(1, "slideDir", "B", null, null);
    fv.visitEnd();
    
    fv = cw.visitField(1, "media", "I", null, null);
    fv.visitEnd();
    
    fv = cw.visitField(1, "dead", "B", null, null);
    fv.visitEnd();
    


    MethodVisitor mv = cw.visitMethod(1, "D", "()Lasu;", null, null);
    mv.visitCode();
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, "sq", "E", "Lasu;");
    mv.visitInsn(176);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
    
    mv = cw.visitMethod(1, "al", "()V", null, null);
    mv.visitCode();
    mv.visitInsn(177);
    mv.visitMaxs(0, 1);
    mv.visitEnd();
    
    mv = cw.visitMethod(1, "d", "(DDD)V", null, null);
    mv.visitCode();
    mv.visitVarInsn(25, 0);
    mv.visitFieldInsn(180, "sq", "q", "Labv;");
    mv.visitVarInsn(25, 0);
    mv.visitVarInsn(24, 1);
    mv.visitVarInsn(24, 3);
    mv.visitVarInsn(24, 5);
    mv.visitMethodInsn(184, "blockphysics/BlockPhysics", "moveEntity", "(Labv;Lsq;DDD)V");
    mv.visitInsn(177);
    mv.visitMaxs(8, 7);
    mv.visitEnd();
    if (!ok8)
    {
      mv = cw.visitMethod(1, "au", "()Z", null, null);
      mv.visitCode();
      mv.visitVarInsn(25, 0);
      mv.visitMethodInsn(182, "sq", "ae", "()Z");
      mv.visitInsn(172);
      mv.visitMaxs(0, 0);
      mv.visitEnd();
      
      ok8 = true;
    }
    cw.visitEnd();
    if ((ok) && (ok2) && (ok3) && (ok4) && (ok5) && (ok6) && (ok7) && (ok8) && (ok9) && (ok10)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2 + ok3 + ok4 + ok5 + ok6 + ok7 + ok8 + ok9 + ok10);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformRenderFallingSand(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching RenderFallingSand.class .......");
    boolean ok = false;boolean ok2 = false;boolean ok4 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Lsq;DDDFF)V")))
      {
        InsnList toInject = new InsnList();
        
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new MethodInsnNode(184, "blockphysics/BClient", "cancelRender", "(Lsq;)Z"));
        LabelNode l0 = new LabelNode();
        toInject.add(new JumpInsnNode(153, l0));
        toInject.add(new InsnNode(177));
        toInject.add(l0);
        
        m.instructions.insertBefore(m.instructions.getFirst(), toInject);
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 180) && (m.instructions.get(index).getType() == 4) && (((FieldInsnNode)m.instructions.get(index)).owner.equals("sq")) && (((FieldInsnNode)m.instructions.get(index)).name.equals("a")) && (((FieldInsnNode)m.instructions.get(index)).desc.equals("I")))
          {
            index += 3;
            ok = true;
            break;
          }
        }
        while ((m.instructions.get(index).getType() != 5) || (!((MethodInsnNode)m.instructions.get(index)).name.equals("glPushMatrix")))
        {
          m.instructions.remove(m.instructions.get(index));
          ok2 = true;
        }
        for (index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("bfo")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("a")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(Laqw;Labv;IIII)V")))
          {
            m.instructions.set(m.instructions.get(index), new MethodInsnNode(184, "blockphysics/BClient", "renderBlockSandFalling", "(Lbfo;Laqw;Labv;IIII)V"));
            ok4 = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2) && (ok4)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2 + ok4);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformNetClientHandler(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching NetClientHandler.class ........");
    boolean ok = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Ldc;)V")))
      {
        InsnList toInject = new InsnList();
        
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "bct", "i", "Lbda;"));
        toInject.add(new VarInsnNode(24, 2));
        toInject.add(new VarInsnNode(24, 4));
        toInject.add(new VarInsnNode(24, 6));
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "createFallingsand", "(Labv;DDDLdc;)Lsq;"));
        toInject.add(new VarInsnNode(58, 8));
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new InsnNode(4));
        toInject.add(new FieldInsnNode(181, "dc", "k", "I"));
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 183) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("sq")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("<init>")))
          {
            while ((m.instructions.get(index).getType() != 4) || (m.instructions.get(index).getOpcode() != 181) || (!((FieldInsnNode)m.instructions.get(index)).owner.equals("dc")) || (!((FieldInsnNode)m.instructions.get(index)).name.equals("k"))) {
              index++;
            }
            while ((m.instructions.get(index).getType() != 3) || (!((TypeInsnNode)m.instructions.get(index)).desc.equals("sq")))
            {
              m.instructions.remove(m.instructions.get(index));
              index--;
              ok = true;
            }
            if (!ok) {
              break;
            }
            m.instructions.remove(m.instructions.get(index));
            m.instructions.insertBefore(m.instructions.get(index), toInject); break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformGuiSelectWorld(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching GuiSelectWorld.class ..........");
    boolean ok = false;boolean ok2 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("A_")) && (m.desc.equals("()V")))
      {
        InsnList toInject = new InsnList();
        
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new InsnNode(3));
        toInject.add(new FieldInsnNode(181, "awe", "d", "Z"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
            break;
          }
        }
      }
      else if ((m.name.equals("e")) && (m.desc.equals("(I)V")))
      {
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("ats")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("a")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(Ljava/lang/String;Ljava/lang/String;Lacc;)V")))
          {
            InsnList toInject = new InsnList();
            toInject.add(new VarInsnNode(25, 0));
            toInject.add(new VarInsnNode(25, 2));
            toInject.add(new VarInsnNode(25, 3));
            toInject.add(new MethodInsnNode(184, "blockphysics/BClient", "loadWorld", "(Lawb;Ljava/lang/String;Ljava/lang/String;)V"));
            
            m.instructions.insert(m.instructions.get(index), toInject);
            while (m.instructions.get(index).getType() != 7)
            {
              m.instructions.remove(m.instructions.get(index));
              index--;
            }
            ok2 = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformGuiCreateWorld(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching GuiCreateWorld.class ..........");
    boolean ok = false;boolean ok2 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("A_")) && (m.desc.equals("()V")))
      {
        InsnList toInject = new InsnList();
        
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new InsnNode(3));
        toInject.add(new FieldInsnNode(181, "auy", "v", "Z"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
            break;
          }
        }
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Lauq;)V")))
      {
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("ats")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("a")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(Ljava/lang/String;Ljava/lang/String;Lacc;)V")))
          {
            m.instructions.remove(m.instructions.get(index));
            
            InsnList toInject = new InsnList();
            
            toInject.add(new InsnNode(4));
            toInject.add(new MethodInsnNode(183, "blockphysics/BGui", "<init>", "(Lawb;Ljava/lang/String;Ljava/lang/String;Lacc;Z)V"));
            toInject.add(new MethodInsnNode(182, "ats", "a", "(Lawb;)V"));
            
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            while ((m.instructions.get(index).getOpcode() != 180) || (m.instructions.get(index).getType() != 4) || (!((FieldInsnNode)m.instructions.get(index)).owner.equals("auy")) || (!((FieldInsnNode)m.instructions.get(index)).name.equals("f")) || (!((FieldInsnNode)m.instructions.get(index)).desc.equals("Lats;"))) {
              index--;
            }
            toInject = new InsnList();
            
            toInject.add(new TypeInsnNode(187, "blockphysics/BGui"));
            toInject.add(new InsnNode(89));
            toInject.add(new VarInsnNode(25, 0));
            
            m.instructions.insert(m.instructions.get(index), toInject);
            
            ok2 = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlock(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching Block.class ...................");
    boolean ok2 = false;
    boolean ok3 = false;
    boolean ok4 = false;
    boolean ok5 = false;
    boolean ok6 = false;
    boolean ok7 = false;
    boolean ok8 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("g")) && (m.desc.equals("(Labv;IIII)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(21, 5));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "aqw", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onBlockDestroyedByPlayer", "(Labv;IIIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok2 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIII)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "aqw", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok3 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("b")) && (m.desc.equals("(Labv;IIILnm;)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "aqw", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok4 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIILnm;)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "aqw", "cF", "I"));
        toInject.add(new VarInsnNode(25, 5));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onEntityCollidedWithBlock", "(Labv;IIIILnm;)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok5 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("k")) && (m.desc.equals("(Labv;IIII)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "aqw", "cF", "I"));
        toInject.add(new VarInsnNode(21, 5));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onPostBlockPlaced", "(Labv;IIIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok6 = true;
            break;
          }
        }
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIILnm;F)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "aqw", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok7 = true;
            break;
          }
        }
      }
    }
    Iterator<FieldNode> fields = classNode.fields.iterator();
    while (fields.hasNext())
    {
      FieldNode f = (FieldNode)fields.next();
      if ((f.name.equals("blockFlammability")) && (f.desc.equals("[I")))
      {
        f.access = 9;
        ok8 = true;
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok2) && (ok3) && (ok4) && (ok5) && (ok6) && (ok7) && (ok8)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok2 + ok3 + ok4 + ok5 + ok6 + ok7 + ok8);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlockRailBase(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockRailBase.class ...........");
    boolean ok = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIII)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "amv", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlockPistonBase(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockPistonBase.class .........");
    boolean ok = false;boolean ok2 = false;boolean ok3 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIII)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "asq", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
            break;
          }
        }
      }
      else if ((m.name.equals("k")) && (m.desc.equals("(Labv;III)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "asq", "a", "Z"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "updatePistonState", "(Labv;IIILasq;Z)V"));
        toInject.add(new InsnNode(177));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok2 = true;
      }
      else if ((m.name.equals("b")) && (m.desc.equals("(Labv;IIIII)Z")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(21, 5));
        toInject.add(new VarInsnNode(21, 6));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "asq", "a", "Z"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onBlockPistonEventReceived", "(Labv;IIIIILasq;Z)Z"));
        toInject.add(new InsnNode(172));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok3 = true;
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2) && (ok3)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2 + ok3);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlockSand(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockSand.class ...............");
    boolean ok = false;
    boolean ok2 = false;
    boolean ok3 = false;
    boolean ok4 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIII)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "aop", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
        toInject.add(new InsnNode(177));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok = true;
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIILjava/util/Random;)V")))
      {
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.insert(new InsnNode(177));
        ok2 = true;
      }
      else if ((m.name.equals("k")) && (m.desc.equals("(Labv;III)V")))
      {
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.insert(new InsnNode(177));
        ok3 = true;
      }
      else if ((m.name.equals("a_")) && (m.desc.equals("(Labv;III)Z")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new VarInsnNode(21, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new InsnNode(3));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "canMoveTo", "(Labv;IIII)Z"));
        toInject.add(new InsnNode(172));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok4 = true;
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2) && (ok3) && (ok4)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2 + ok3 + ok4);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlockRedstoneLight(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockRedstoneLight.class ......");
    boolean ok = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIII)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "aqa", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlockTNT(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockTNT.class ................");
    boolean ok = false;
    boolean ok2 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIII)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "arb", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
            break;
          }
        }
      }
      else if ((m.name.equals("g")) && (m.desc.equals("(Labv;IIII)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(21, 5));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "arb", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onBlockDestroyedByPlayer", "(Labv;IIIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok2 = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlockFarmland(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockFarmland.class ...........");
    boolean ok = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIILnm;F)V")))
      {
        InsnList toInject = new InsnList();
        
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "aoc", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
        for (int index = m.instructions.size() - 1; index >= 0; index--) {
          if (m.instructions.get(index).getOpcode() == 177)
          {
            m.instructions.insertBefore(m.instructions.get(index), toInject);
            ok = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if (ok) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlockAnvil(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockAnvil.class ..............");
    boolean ok = false;boolean ok2 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Lsq;)V")))
      {
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.insert(new InsnNode(177));
        ok = true;
      }
      else if ((m.name.equals("a_")) && (m.desc.equals("(Labv;IIII)V")))
      {
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.insert(new InsnNode(177));
        ok2 = true;
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2)) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2);
    }
    return cw.toByteArray();
  }
  
  private byte[] transformBlockDragonEgg(byte[] bytes)
  {
    System.out.print("[BlockPhysics] Patching BlockDragonEgg.class ..........");
    boolean ok = false;boolean ok2 = false;boolean ok3 = false;boolean ok4 = false;
    
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept(classNode, 0);
    

    Iterator<MethodNode> methods = classNode.methods.iterator();
    while (methods.hasNext())
    {
      MethodNode m = (MethodNode)methods.next();
      if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIII)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 2));
        toInject.add(new VarInsnNode(21, 3));
        toInject.add(new VarInsnNode(21, 4));
        toInject.add(new VarInsnNode(25, 0));
        toInject.add(new FieldInsnNode(180, "any", "cF", "I"));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
        toInject.add(new InsnNode(177));
        
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.add(toInject);
        ok = true;
      }
      else if ((m.name.equals("a")) && (m.desc.equals("(Labv;IIILjava/util/Random;)V")))
      {
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.insert(new InsnNode(177));
        ok2 = true;
      }
      else if ((m.name.equals("k")) && (m.desc.equals("(Labv;III)V")))
      {
        m.instructions.clear();
        m.localVariables.clear();
        m.instructions.insert(new InsnNode(177));
        ok3 = true;
      }
      else if ((m.name.equals("m")) && (m.desc.equals("(Labv;III)V")))
      {
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(25, 1));
        toInject.add(new VarInsnNode(21, 6));
        toInject.add(new VarInsnNode(21, 7));
        toInject.add(new VarInsnNode(21, 8));
        toInject.add(new MethodInsnNode(184, "blockphysics/BlockPhysics", "notifyMove", "(Labv;III)V"));
        for (int index = 0; index < m.instructions.size(); index++) {
          if ((m.instructions.get(index).getOpcode() == 182) && (m.instructions.get(index).getType() == 5) && (((MethodInsnNode)m.instructions.get(index)).owner.equals("abv")) && (((MethodInsnNode)m.instructions.get(index)).name.equals("i")) && (((MethodInsnNode)m.instructions.get(index)).desc.equals("(III)Z")))
          {
            if (m.instructions.get(index + 1).getOpcode() == 87) {
              m.instructions.insert(m.instructions.get(index + 1), toInject);
            } else {
              m.instructions.insert(m.instructions.get(index), toInject);
            }
            ok4 = true;
            break;
          }
        }
      }
    }
    ClassWriter cw = new ClassWriter(1);
    classNode.accept(cw);
    if ((ok) && (ok2) && ((ok3 & ok4))) {
      System.out.println("OK");
    } else {
      System.out.println("Failed." + ok + ok2 + ok3 + ok4);
    }
    return cw.toByteArray();
  }
}