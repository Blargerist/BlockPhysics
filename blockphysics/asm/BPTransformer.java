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

package blockphysics.asm;

//import java.io.FileOutputStream;
//import java.io.IOException;
import java.util.Iterator;

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

public class BPTransformer implements net.minecraft.launchwrapper.IClassTransformer, Opcodes
{
	@Override
    public byte[] transform(String name, String mcpName, byte[] bytes)
    {
		if (name.equals("adq"))
        {
    		return transformChunk(bytes);
        }
    	else if (name.equals("adr"))
        {
    		return transformExtendedBlockStorage(bytes);
        }
    	else if (name.equals("aed"))
        {
    		return transformAnvilChunkLoader(bytes);
        }
    	else if (name.equals("anh"))
        {
    		return transformBlockChest(bytes);
        }
    	else if (name.equals("anv"))
        {
    		return transformBlockDispenser(bytes);
        }
    	else if (name.equals("aoh"))
        {
    		return transformBlockFurnace(bytes);
        }
    	else if (name.equals("ast"))
        {
    		return transformTileEntityPiston(bytes);
        }
    	else if (name.equals("blockphysics.BlockPhysics"))
        {
        	return transformBlockPhysics(bytes);
        }
    	else if (name.equals("aqw"))
        {
    		return transformBlock(bytes);
        }
    	else if (name.equals("ams"))
        {
    		return transformBlockAnvil(bytes);
        }
    	else if (name.equals("any"))
        {
    		return transformBlockDragonEgg(bytes);
	    }
    	else if (name.equals("aoc"))
        {
    		return transformBlockFarmland(bytes);
        }
    	else if (name.equals("asq"))
        {
    		return transformBlockPistonBase(bytes);
        }
    	else if (name.equals("amv"))
        {
    		return transformBlockRailBase(bytes);
        }
    	else if (name.equals("aqa"))
        {
    		return transformBlockRedstoneLight(bytes);
        }
    	else if (name.equals("aop"))
        {
    		return transformBlockSand(bytes);
        }
    	else if (name.equals("arb"))
        {
    		return transformBlockTNT(bytes);
        }
    	else if (name.equals("arm"))
        {
    		return transformBlockWeb(bytes);
        }
    	else if (name.equals("nm"))
        {
    		return transformEntity(bytes);
        }
    	else if (name.equals("sp"))
        {
    		return transformEntityBoat(bytes);
        }
    	else if (name.equals("sq") )
        {
    		return transformEntityFallingSand(bytes);
        }
    	else if (name.equals("ss"))
        {
        	return transformEntityMinecart(bytes);
        }
    	else if (name.equals("tb"))
        {
    		return transformEntityTNTPrimed(bytes);
        }
    	else if (name.equals("jl"))
        {
    		return transformEntityTracker(bytes);
        }
    	else if (name.equals("jw"))
        {
    		return transformEntityTrackerEntry(bytes);
        }
    	else if (name.equals("nz"))
        {
    		return transformEntityXPOrb(bytes);
        }
    	else if (name.equals("abq"))
        {
    		return transformExplosion(bytes);
	    }
    	else if (name.equals("auy"))
        {
    		return transformGuiCreateWorld(bytes);
        }
        else if (name.equals("awe"))
        {
    		return transformGuiSelectWorld(bytes);
        }
    	else if (name.equals("net.minecraft.server.MinecraftServer"))
        {	
			return transformMinecraftServer(bytes);
	    }
    	else if (name.equals("bct"))
        {
    		return transformNetClientHandler(bytes);
        }
    	else if (name.equals("bgl"))
        {
    		return transformRenderFallingSand(bytes);
        }
        else if (name.equals("abv"))
        {
        	return transformWorld(bytes);
	    }       
        else if (name.equals("jr") )
        {    		
			return transformWorldServer(bytes);
	    }
        else return bytes;
    }
    
    private byte[] transformTileEntityPiston(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/TileEntityPiston.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
		
    	System.out.print("[BlockPhysics] Patching TileEntityPiston.class ........");
        
        boolean ok = false, ok2 = false, ok3 = false, ok4 = false, ok5 = false;
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();        	
        	if (m.name.equals("<init>") && m.desc.equals("(IIIZZ)V") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			InsnList toInject = new InsnList();
            			toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new InsnNode(ACONST_NULL));
                		toInject.add(new FieldInsnNode(PUTFIELD, "ast", "movingBlockTileEntityData", "Lbx;"));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new InsnNode(ICONST_0));
                		toInject.add(new FieldInsnNode(PUTFIELD, "ast", "bpmeta", "I"));

            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			
            			ok = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("f") && m.desc.equals("()V") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
        		{
            		if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abv") && ((MethodInsnNode) m.instructions.get(index)).name.equals("g") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(IIII)V"))
            		{
            			InsnList toInject = new InsnList();
            			
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "k", "Labv;"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "l", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "m", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "n", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "bpmeta", "I"));
            			toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "setBlockBPdata", "(Labv;IIII)Z"));
            			toInject.add(new InsnNode(POP));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "movingBlockTileEntityData", "Lbx;"));
            			LabelNode l1 = new LabelNode();
            			toInject.add(new JumpInsnNode(IFNULL, l1));
            			toInject.add(new FieldInsnNode(GETSTATIC, "aqw", "s", "[Laqw;"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "a", "I"));
            			toInject.add(new InsnNode(AALOAD));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "k", "Labv;"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "b", "I"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "aqw", "createTileEntity", "(Labv;I)Lasm;"));
            			toInject.add(new VarInsnNode(ASTORE, 1));
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "movingBlockTileEntityData", "Lbx;"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "asm", "a", "(Lbx;)V"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "k", "Labv;"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "l", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "m", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "n", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "abv", "a", "(IIILasm;)V"));
            			toInject.add(l1);
                		
            			m.instructions.insert(m.instructions.get(index),toInject);
            			
            			ok2 = true;
            			break;
            		}
                }
        	}
        	else if (m.name.equals("a") && m.desc.equals("(Lbx;)V") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			InsnList toInject = new InsnList();
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new LdcInsnNode("BPData"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "b", "(Ljava/lang/String;)Z"));
            			LabelNode l1 = new LabelNode();
            			toInject.add(new JumpInsnNode(IFEQ, l1));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new LdcInsnNode("BPData"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "c", "(Ljava/lang/String;)B"));
            			toInject.add(new FieldInsnNode(PUTFIELD, "ast", "bpmeta", "I"));
            			toInject.add(l1);
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new LdcInsnNode("TileEntityData"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "b", "(Ljava/lang/String;)Z"));
            			LabelNode l0 = new LabelNode();
            			toInject.add(new JumpInsnNode(IFEQ, l0));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new LdcInsnNode("TileEntityData"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "l", "(Ljava/lang/String;)Lbx;"));
            			toInject.add(new FieldInsnNode(PUTFIELD, "ast", "movingBlockTileEntityData", "Lbx;"));
            			toInject.add(l0);
                		
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			
            			ok3 = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("b") && m.desc.equals("(Lbx;)V") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			InsnList toInject = new InsnList();
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new LdcInsnNode("BPData"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "bpmeta", "I"));
            			toInject.add(new InsnNode(I2B));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "a", "(Ljava/lang/String;B)V"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "movingBlockTileEntityData", "Lbx;"));
            			LabelNode l0 = new LabelNode();
            			toInject.add(new JumpInsnNode(IFNULL, l0));
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new LdcInsnNode("TileEntityData"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "movingBlockTileEntityData", "Lbx;"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "a", "(Ljava/lang/String;Lbx;)V"));
            			toInject.add(l0);
                		
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			
            			ok4 = true;
            			break;
            		}
                }
            }
            else if (m.name.equals("h") && m.desc.equals("()V") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
        		{
            		if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abv") && ((MethodInsnNode) m.instructions.get(index)).name.equals("g") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(IIII)V"))
            		{
            			InsnList toInject = new InsnList();
            			
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "k", "Labv;"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "l", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "m", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "n", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "bpmeta", "I"));
            			toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "setBlockBPdata", "(Labv;IIII)Z"));
            			toInject.add(new InsnNode(POP));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "movingBlockTileEntityData", "Lbx;"));
            			LabelNode l0 = new LabelNode();
            			toInject.add(new JumpInsnNode(IFNULL, l0));
            			toInject.add(new FieldInsnNode(GETSTATIC, "aqw", "s", "[Laqw;"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "a", "I"));
            			toInject.add(new InsnNode(AALOAD));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "k", "Labv;"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "b", "I"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "aqw", "createTileEntity", "(Labv;I)Lasm;"));
            			toInject.add(new VarInsnNode(ASTORE, 1));
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "movingBlockTileEntityData", "Lbx;"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "asm", "a", "(Lbx;)V"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "k", "Labv;"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "l", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "m", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "ast", "n", "I"));
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "abv", "a", "(IIILasm;)V"));
            			toInject.add(l0);
            			
            			m.instructions.insert(m.instructions.get(index),toInject);
            			
            			ok5 = true;
            			break;
            		}
                }
        	}
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2 && ok3 && ok4 && ok5) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok3+ok4+ok5);
        
        FieldVisitor fv;
    	
        fv = cw.visitField(ACC_PUBLIC, "movingBlockTileEntityData", "Lbx;", null, null);
        fv.visitEnd();
        
        fv = cw.visitField(ACC_PUBLIC, "bpmeta", "I", null, null);
        fv.visitEnd();
        
    	cw.visitEnd();
    	
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/TileEntityPiston.mod.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
	}

	private byte[] transformBlockFurnace(byte[] bytes)
	{
		/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockFurnace.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
		
		System.out.print("[BlockPhysics] Patching BlockFurnace.class ............");
        
        boolean ok = false;
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();        	
        	if (m.name.equals("a") && m.desc.equals("(Labv;III)V") )
        	{
        		for (int index = 0; index < m.instructions.size(); index++)
        		{
            		if ( m.instructions.get(index).getOpcode() == INVOKESPECIAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("aoh") && ((MethodInsnNode) m.instructions.get(index)).name.equals("k") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Labv;III)V"))
            		{
            			index = index - 5;
            			for ( int i = 0; i < 6; i++) m.instructions.remove(m.instructions.get(index));
            			
            			ok = true;
            			break;
            		}
                }
        	}
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok) System.out.println("OK");
        else System.out.println("Failed."+ok);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockFurnace.mod.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
	}

	private byte[] transformBlockDispenser(byte[] bytes)
	{
		/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockDispenser.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/

		System.out.print("[BlockPhysics] Patching BlockDispenser.class ..........");
        
        boolean ok = false;
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();        	
        	if (m.name.equals("a") && m.desc.equals("(Labv;III)V") )
        	{
        		for (int index = 0; index < m.instructions.size(); index++)
        		{
            		if ( m.instructions.get(index).getOpcode() == INVOKESPECIAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("anv") && ((MethodInsnNode) m.instructions.get(index)).name.equals("k") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Labv;III)V"))
            		{
            			index = index - 5;
            			for ( int i = 0; i < 6; i++) m.instructions.remove(m.instructions.get(index));
            			
            			ok = true;
            			break;
            		}
                }
        	}
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok) System.out.println("OK");
        else System.out.println("Failed."+ok);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockDispenser.mod.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
	}

	private byte[] transformBlockChest(byte[] bytes)
	{
		/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockChest.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/

		System.out.print("[BlockPhysics] Patching BlockChest.class ..............");
        
        boolean ok = false;
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();        	
        	if (m.name.equals("a") && m.desc.equals("(Labv;III)V") )
        	{
        		for (int index = 0; index < m.instructions.size(); index++)
        		{
            		if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("anh") && ((MethodInsnNode) m.instructions.get(index)).name.equals("f_") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Labv;III)V"))
            		{
            			index = index - 5;
            			for ( int i = 0; i < 6; i++) m.instructions.remove(m.instructions.get(index));
            			
            			ok = true;
            			break;
            		}
                }
        	}
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok) System.out.println("OK");
        else System.out.println("Failed."+ok);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockChest.mod.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
	}

	private byte[] transformAnvilChunkLoader(byte[] bytes)
	{
		/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/AnvilChunkLoader.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
		System.out.print("[BlockPhysics] Patching AnvilChunkLoader.class ........");
        
        boolean ok = false, ok2 = false, ok3 = false, ok4 = false, ok6 = false, ok7 = false;
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();        	
        	if (m.name.equals("a") && m.desc.equals("(Ladq;Labv;Lbx;)V") )
        	{
        		int var1 = 9;
        		for (int index = 0; index < m.instructions.size(); index++)
        		{
            		if (ok3) break;
        			if ( m.instructions.get(index).getOpcode() == INVOKESPECIAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("bx") && ((MethodInsnNode) m.instructions.get(index)).name.equals("<init>") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("()V"))
            		{
            			for (int index2 = index; index2 < m.instructions.size(); index2++)
                		{
                    		if ( m.instructions.get(index2).getOpcode() == ASTORE )
                    		{
                    			var1 = ((VarInsnNode) m.instructions.get(index2)).var;
                    			ok3 = true;
                    			break;
                    		}
                		}
            		}
        		}
        		
        		int var2 = 11;
        		for (int index = 0; index < m.instructions.size(); index++)
        		{
            		if (ok4) break;
        			if ( m.instructions.get(index).getType() == AbstractInsnNode.LDC_INSN && ((LdcInsnNode) m.instructions.get(index)).cst.equals("Data") )
            		{
            			for (int index2 = index; index2 < m.instructions.size(); index2++)
                		{
                    		if ( m.instructions.get(index2).getOpcode() == ALOAD )
                    		{
                    			var2 = ((VarInsnNode) m.instructions.get(index2)).var;
                    			ok4 = true;
                    			break;
                    		}
                		}
            		}
        		}
        		
        		
        		for (int index = 0; index < m.instructions.size(); index++)
        		{
            		if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("cf") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Lck;)V"))
            		{
            			InsnList toInject = new InsnList();
            			
            			toInject.add(new VarInsnNode(ALOAD, var1));
            			toInject.add(new LdcInsnNode("BPData"));
            			toInject.add(new VarInsnNode(ALOAD, var2));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "adr", "getBPdataArray", "()[B"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "a", "(Ljava/lang/String;[B)V"));
                		
            			m.instructions.insertBefore(m.instructions.get(index - 2 ),toInject);
            			
            			ok = true;
            			break;
            		}
                }
        	}
        	else if (m.name.equals("a") && m.desc.equals("(Labv;Lbx;)Ladq;") )
        	{
        		int var1 = 11;
        		for (int index = 0; index < m.instructions.size(); index++)
        		{

            		if (ok6) break;
        			if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("cf") && ((MethodInsnNode) m.instructions.get(index)).name.equals("b") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(I)Lck;"))
            		{
            			for (int index2 = index; index2 < m.instructions.size(); index2++)
                		{
                    		if ( m.instructions.get(index2).getOpcode() == ASTORE )
                    		{
                    			var1 = ((VarInsnNode) m.instructions.get(index2)).var;
                    			ok6 = true;
                    			break;
                    		}
                		}
            		}
        		}
        		
        		int var2 = 13;
        		for (int index = 0; index < m.instructions.size(); index++)
        		{
            		if (ok7) break;
        			if ( m.instructions.get(index).getOpcode() == INVOKESPECIAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("adr") && ((MethodInsnNode) m.instructions.get(index)).name.equals("<init>") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(IZ)V") )
            		{
            			for (int index2 = index; index2 < m.instructions.size(); index2++)
                		{
                    		if ( m.instructions.get(index2).getOpcode() == ASTORE )
                    		{
                    			var2 = ((VarInsnNode) m.instructions.get(index2)).var;
                    			ok7 = true;
                    			break;
                    		}
                		}
            		}
        		}
        		
        		for (int index = 0; index < m.instructions.size(); index++)
        		{
            		if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("adr") && ((MethodInsnNode) m.instructions.get(index)).name.equals("e") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("()V"))
            		{
            			InsnList toInject = new InsnList();
            			
            			toInject.add(new VarInsnNode(ALOAD, var1));
            			toInject.add(new LdcInsnNode("BPData"));
                    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "b", "(Ljava/lang/String;)Z"));
                    	LabelNode l6 = new LabelNode();
                    	toInject.add(new JumpInsnNode(IFEQ, l6));
                    	toInject.add(new VarInsnNode(ALOAD, var2));
                    	toInject.add(new VarInsnNode(ALOAD, var1));
                    	toInject.add(new LdcInsnNode("BPData"));
                    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "j", "(Ljava/lang/String;)[B"));
                    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "adr", "setBPdataArray", "([B)V"));
                    	LabelNode l7 = new LabelNode();
                    	toInject.add(new JumpInsnNode(GOTO, l7));
                    	toInject.add(l6);
                    	toInject.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                    	toInject.add(new VarInsnNode(ALOAD, var2));
                    	toInject.add(new IntInsnNode(SIPUSH, 4096));
                    	toInject.add(new IntInsnNode(NEWARRAY, T_BYTE));
                    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "adr", "setBPdataArray", "([B)V"));
                    	toInject.add(l7);
                		
            			m.instructions.insertBefore(m.instructions.get(index - 1 ),toInject);
            			
            			ok2 = true;
            			break;
            		}
                }
        	}
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2&&ok3&&ok4&&ok6&&ok7) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok3+ok4+ok6+ok7);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/AnvilChunkLoader.mod.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
	}

	private byte[] transformExtendedBlockStorage(byte[] bytes) {
		/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/ExtendedBlockStorage.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
		
		System.out.print("[BlockPhysics] Patching ExtendedBlockStorage.class ....");
        
        boolean ok = false;
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("<init>") && m.desc.equals("(IZ)V") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			InsnList toInject = new InsnList();
            			
            			//toInject.add(new FrameNode(Opcodes.F_FULL, 3, new Object[] {"abx", Opcodes.INTEGER, Opcodes.INTEGER}, 0, new Object[] {}));
		        		toInject.add(new VarInsnNode(ALOAD, 0));
		        		toInject.add(new IntInsnNode(SIPUSH, 4096));
		        		toInject.add(new IntInsnNode(NEWARRAY, T_BYTE));
		        		toInject.add(new FieldInsnNode(PUTFIELD, "adr", "blockBPdataArray", "[B"));
		        		
		        		m.instructions.insertBefore(m.instructions.get(index),toInject);
            			
            			ok = true;
            			break;
            		}
                }
        	}
        }
		
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getBlockBPdata", "(III)I", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "adr", "blockBPdataArray", "[B");
        mv.visitVarInsn(ILOAD, 2);
        mv.visitIntInsn(SIPUSH, 256);
        mv.visitInsn(IMUL);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitIntInsn(BIPUSH, 16);
        mv.visitInsn(IMUL);
        mv.visitInsn(IADD);
        mv.visitVarInsn(ILOAD, 3);
        mv.visitInsn(IADD);
        mv.visitInsn(BALOAD);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(4, 4);
        mv.visitEnd();
        
		mv = cw.visitMethod(ACC_PUBLIC, "setBlockBPdata", "(IIII)V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "adr", "blockBPdataArray", "[B");
		mv.visitVarInsn(ILOAD, 2);
		mv.visitIntInsn(SIPUSH, 256);
		mv.visitInsn(IMUL);
		mv.visitVarInsn(ILOAD, 1);
		mv.visitIntInsn(BIPUSH, 16);
		mv.visitInsn(IMUL);
		mv.visitInsn(IADD);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitInsn(IADD);
		mv.visitVarInsn(ILOAD, 4);
		mv.visitInsn(I2B);
		mv.visitInsn(BASTORE);
		mv.visitInsn(RETURN);
		mv.visitMaxs(4, 5);
		mv.visitEnd();
		
		mv = cw.visitMethod(ACC_PUBLIC, "getBPdataArray", "()[B", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "adr", "blockBPdataArray", "[B");
		mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
			
		mv = cw.visitMethod(ACC_PUBLIC, "setBPdataArray", "([B)V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(PUTFIELD, "adr", "blockBPdataArray", "[B");
		mv.visitInsn(RETURN);
		mv.visitMaxs(2, 2);
		mv.visitEnd();

        FieldVisitor fv;
    	
        fv = cw.visitField(ACC_PRIVATE, "blockBPdataArray", "[B", null, null);
        fv.visitEnd();
        
    	cw.visitEnd();

        if (ok ) System.out.println("OK");
        else System.out.println("Failed."+ok);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/ExtendedBlockStorage.mod.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
	}

	private byte[] transformChunk(byte[] bytes) {
		/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/Chunk.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
		
		System.out.print("[BlockPhysics] Patching Chunk.class ...................");
       
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getBlockBPdata", "(III)I", null, null);
		mv.visitCode();
		mv.visitVarInsn(ILOAD, 2);
		mv.visitInsn(ICONST_4);
		mv.visitInsn(ISHR);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "adq", "r", "[Ladr;");
		mv.visitInsn(ARRAYLENGTH);
		Label l0 = new Label();
		mv.visitJumpInsn(IF_ICMPLT, l0);
		mv.visitInsn(ICONST_0);
		mv.visitInsn(IRETURN);
		mv.visitLabel(l0);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "adq", "r", "[Ladr;");
		mv.visitVarInsn(ILOAD, 2);
		mv.visitInsn(ICONST_4);
		mv.visitInsn(ISHR);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, 4);
		mv.visitVarInsn(ALOAD, 4);
		Label l1 = new Label();
		mv.visitJumpInsn(IFNULL, l1);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitVarInsn(ILOAD, 1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitIntInsn(BIPUSH, 15);
		mv.visitInsn(IAND);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitMethodInsn(INVOKEVIRTUAL, "adr", "getBlockBPdata", "(III)I");
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		mv.visitLabel(l1);
		mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {"adr"}, 0, null);
		mv.visitInsn(ICONST_0);
		mv.visitLabel(l2);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {Opcodes.INTEGER});
		mv.visitInsn(IRETURN);
		mv.visitMaxs(4, 5);
		mv.visitEnd();

		
		mv = cw.visitMethod(ACC_PUBLIC, "setBlockBPdata", "(IIII)Z", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "adq", "r", "[Ladr;");
		mv.visitVarInsn(ILOAD, 2);
		mv.visitInsn(ICONST_4);
		mv.visitInsn(ISHR);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, 5);
		mv.visitVarInsn(ALOAD, 5);
		Label lab0 = new Label();
		mv.visitJumpInsn(IFNONNULL, lab0);
		mv.visitInsn(ICONST_0);
		mv.visitInsn(IRETURN);
		mv.visitLabel(lab0);
		mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {"adr"}, 0, null);
		mv.visitVarInsn(ALOAD, 5);
		mv.visitVarInsn(ILOAD, 1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitIntInsn(BIPUSH, 15);
		mv.visitInsn(IAND);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitMethodInsn(INVOKEVIRTUAL, "adr", "getBlockBPdata", "(III)I");
		mv.visitVarInsn(ISTORE, 6);
		mv.visitVarInsn(ILOAD, 6);
		mv.visitVarInsn(ILOAD, 4);
		Label lab1 = new Label();
		mv.visitJumpInsn(IF_ICMPNE, lab1);
		mv.visitInsn(ICONST_0);
		mv.visitInsn(IRETURN);
		mv.visitLabel(lab1);
		mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {Opcodes.INTEGER}, 0, null);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(ICONST_1);
		mv.visitFieldInsn(PUTFIELD, "adq", "l", "Z");
		mv.visitVarInsn(ALOAD, 5);
		mv.visitVarInsn(ILOAD, 1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitIntInsn(BIPUSH, 15);
		mv.visitInsn(IAND);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitVarInsn(ILOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, "adr", "setBlockBPdata", "(IIII)V");
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IRETURN);
		mv.visitMaxs(5, 7);
		mv.visitEnd();

		cw.visitEnd();

        System.out.println("OK");
                
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/Chunk.mod.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();       
	}

	private byte[] transformBlockPhysics(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockPhysics.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	try 
		{				
			Class.forName("org.bukkit.Bukkit");
			System.out.println("[BlockPhysics] Bukkit detected.");
	   	}
		catch(ClassNotFoundException e) 
		{
  	     return bytes;
  	    }
     	
    	System.out.print("[BlockPhysics] Patching BlockPhysics.class ............");
       
        boolean ok = false;
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	if ( m.name.equals("tickBlocksRandomMove") && m.desc.equals("(Ljr;)V") )
        	{
        		m.instructions.clear();
        		m.localVariables.clear();
        		
        		InsnList toInject = new InsnList();
        		        	
        		toInject.add(new FieldInsnNode(GETSTATIC, "blockphysics/BlockPhysics", "skipMove", "Z"));
        		LabelNode l0 = new LabelNode();
        		toInject.add(new JumpInsnNode(IFEQ, l0));
        		toInject.add(new InsnNode(RETURN));
        		toInject.add(l0);
        		toInject.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        		toInject.add(new VarInsnNode(ALOAD, 0));
        		toInject.add(new FieldInsnNode(GETFIELD, "jr", "G", "Lgnu/trove/map/hash/TLongShortHashMap;"));
        		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "gnu/trove/map/hash/TLongShortHashMap", "iterator", "()Lgnu/trove/iterator/TLongShortIterator;"));
        		toInject.add(new VarInsnNode(ASTORE, 1));
        		LabelNode l1 = new LabelNode();
        		toInject.add(l1);
        		toInject.add(new FrameNode(Opcodes.F_APPEND,1, new Object[] {"gnu/trove/iterator/TLongShortIterator"}, 0, null));
        		toInject.add(new VarInsnNode(ALOAD, 1));
        		toInject.add(new MethodInsnNode(INVOKEINTERFACE, "gnu/trove/iterator/TLongShortIterator", "hasNext", "()Z"));
        		LabelNode l2 = new LabelNode();
        		toInject.add(new JumpInsnNode(IFEQ, l2));
        		toInject.add(new VarInsnNode(ALOAD, 1));
        		toInject.add(new MethodInsnNode(INVOKEINTERFACE, "gnu/trove/iterator/TLongShortIterator", "advance", "()V"));
        		toInject.add(new VarInsnNode(ALOAD, 1));
        		toInject.add(new MethodInsnNode(INVOKEINTERFACE, "gnu/trove/iterator/TLongShortIterator", "key", "()J"));
        		toInject.add(new VarInsnNode(LSTORE, 2));
        		toInject.add(new VarInsnNode(ALOAD, 0));
        		toInject.add(new InsnNode(POP));
        		toInject.add(new VarInsnNode(LLOAD, 2));
        		toInject.add(new MethodInsnNode(INVOKESTATIC, "abv", "keyToX", "(J)I"));
        		toInject.add(new VarInsnNode(ISTORE, 4));
        		toInject.add(new VarInsnNode(ALOAD, 0));
        		toInject.add(new InsnNode(POP));
        		toInject.add(new VarInsnNode(LLOAD, 2));
        		toInject.add(new MethodInsnNode(INVOKESTATIC, "abv", "keyToZ", "(J)I"));
        		toInject.add(new VarInsnNode(ISTORE, 5));
        		toInject.add(new VarInsnNode(ILOAD, 4));
        		toInject.add(new IntInsnNode(BIPUSH, 16));
        		toInject.add(new InsnNode(IMUL));
        		toInject.add(new VarInsnNode(ISTORE, 6));
        		toInject.add(new VarInsnNode(ILOAD, 5));
        		toInject.add(new IntInsnNode(BIPUSH, 16));
        		toInject.add(new InsnNode(IMUL));
        		toInject.add(new VarInsnNode(ISTORE, 7));
        		toInject.add(new VarInsnNode(ALOAD, 0));
        		toInject.add(new VarInsnNode(ILOAD, 4));
        		toInject.add(new VarInsnNode(ILOAD, 5));
        		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "jr", "e", "(II)Ladq;"));
        		toInject.add(new VarInsnNode(ASTORE, 8));
        		toInject.add(new VarInsnNode(ALOAD, 8));
        		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "adq", "i", "()[Ladr;"));
        		toInject.add(new VarInsnNode(ASTORE, 12));
        		toInject.add(new VarInsnNode(ALOAD, 12));
        		toInject.add(new InsnNode(ARRAYLENGTH));
        		toInject.add(new VarInsnNode(ISTORE, 9));
        		toInject.add(new InsnNode(ICONST_0));
        		toInject.add(new VarInsnNode(ISTORE, 10));
        		LabelNode l3 = new LabelNode();
        		toInject.add(l3);
        		toInject.add(new FrameNode(Opcodes.F_FULL, 12, new Object[] {"jr", "gnu/trove/iterator/TLongShortIterator", Opcodes.LONG, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, "adq", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, "[Ladr;"}, 0, new Object[] {}));
        		toInject.add(new VarInsnNode(ILOAD, 10));
        		toInject.add(new VarInsnNode(ILOAD, 9));
        		LabelNode l4 = new LabelNode();
        		toInject.add(new JumpInsnNode(IF_ICMPGE, l4));
        		toInject.add(new VarInsnNode(ALOAD, 12));
        		toInject.add(new VarInsnNode(ILOAD, 10));
        		toInject.add(new InsnNode(AALOAD));
        		toInject.add(new VarInsnNode(ASTORE, 13));
        		toInject.add(new VarInsnNode(ALOAD, 13));
        		LabelNode l5 = new LabelNode();
        		toInject.add(new JumpInsnNode(IFNULL, l5));
        		toInject.add(new InsnNode(ICONST_0));
        		toInject.add(new VarInsnNode(ISTORE, 14));
        		LabelNode l6 = new LabelNode();
        		toInject.add(l6);
        		toInject.add(new FrameNode(Opcodes.F_APPEND,2, new Object[] {"adr", Opcodes.INTEGER}, 0, null));
        		toInject.add(new VarInsnNode(ILOAD, 14));
        		toInject.add(new InsnNode(ICONST_3));
        		toInject.add(new JumpInsnNode(IF_ICMPGE, l5));
        		toInject.add(new FieldInsnNode(GETSTATIC, "blockphysics/BlockPhysics", "updateLCG", "I"));
        		toInject.add(new InsnNode(ICONST_3));
        		toInject.add(new InsnNode(IMUL));
        		toInject.add(new LdcInsnNode(new Integer(1013904223)));
        		toInject.add(new InsnNode(IADD));
        		toInject.add(new FieldInsnNode(PUTSTATIC, "blockphysics/BlockPhysics", "updateLCG", "I"));
        		toInject.add(new FieldInsnNode(GETSTATIC, "blockphysics/BlockPhysics", "updateLCG", "I"));
        		toInject.add(new InsnNode(ICONST_2));
        		toInject.add(new InsnNode(ISHR));
        		toInject.add(new VarInsnNode(ISTORE, 11));
        		toInject.add(new VarInsnNode(ILOAD, 11));
        		toInject.add(new IntInsnNode(BIPUSH, 15));
        		toInject.add(new InsnNode(IAND));
        		toInject.add(new VarInsnNode(ISTORE, 15));
        		toInject.add(new VarInsnNode(ILOAD, 11));
        		toInject.add(new IntInsnNode(BIPUSH, 8));
        		toInject.add(new InsnNode(ISHR));
        		toInject.add(new IntInsnNode(BIPUSH, 15));
        		toInject.add(new InsnNode(IAND));
        		toInject.add(new VarInsnNode(ISTORE, 16));
        		toInject.add(new VarInsnNode(ILOAD, 11));
        		toInject.add(new IntInsnNode(BIPUSH, 16));
        		toInject.add(new InsnNode(ISHR));
        		toInject.add(new IntInsnNode(BIPUSH, 15));
        		toInject.add(new InsnNode(IAND));
        		toInject.add(new VarInsnNode(ISTORE, 17));
        		toInject.add(new VarInsnNode(ALOAD, 13));
        		toInject.add(new VarInsnNode(ILOAD, 15));
        		toInject.add(new VarInsnNode(ILOAD, 17));
        		toInject.add(new VarInsnNode(ILOAD, 16));
        		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "adr", "a", "(III)I"));
        		toInject.add(new VarInsnNode(ISTORE, 18));
        		toInject.add(new VarInsnNode(ALOAD, 13));
        		toInject.add(new VarInsnNode(ILOAD, 15));
        		toInject.add(new VarInsnNode(ILOAD, 17));
        		toInject.add(new VarInsnNode(ILOAD, 16));
        		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "adr", "b", "(III)I"));
        		toInject.add(new VarInsnNode(ISTORE, 19));
        		toInject.add(new FieldInsnNode(GETSTATIC, "blockphysics/BlockPhysics", "blockSet", "[[Lblockphysics/BlockDef;"));
        		toInject.add(new VarInsnNode(ILOAD, 18));
        		toInject.add(new InsnNode(AALOAD));
        		toInject.add(new VarInsnNode(ILOAD, 19));
        		toInject.add(new InsnNode(AALOAD));
        		toInject.add(new FieldInsnNode(GETFIELD, "blockphysics/BlockDef", "randomtick", "Z"));
        		LabelNode l7 = new LabelNode();
        		toInject.add(new JumpInsnNode(IFEQ, l7));
        		toInject.add(new VarInsnNode(ALOAD, 0));
        		toInject.add(new VarInsnNode(ILOAD, 15));
        		toInject.add(new VarInsnNode(ILOAD, 6));
        		toInject.add(new InsnNode(IADD));
        		toInject.add(new VarInsnNode(ILOAD, 17));
        		toInject.add(new VarInsnNode(ALOAD, 13));
        		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "adr", "d", "()I"));
        		toInject.add(new InsnNode(IADD));
        		toInject.add(new VarInsnNode(ILOAD, 16));
        		toInject.add(new VarInsnNode(ILOAD, 7));
        		toInject.add(new InsnNode(IADD));
        		toInject.add(new VarInsnNode(ILOAD, 18));
        		toInject.add(new VarInsnNode(ILOAD, 19));
        		toInject.add(new InsnNode(ICONST_0));
        		toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "tryToMove", "(Labv;IIIIIZ)Z"));
        		toInject.add(new InsnNode(POP));
        		toInject.add(l7);
        		toInject.add(new FrameNode(Opcodes.F_FULL, 14, new Object[] {"jr", "gnu/trove/iterator/TLongShortIterator", Opcodes.LONG, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, "adq", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, "[Ladr;", "adr", Opcodes.INTEGER}, 0, new Object[] {}));
        		toInject.add(new IincInsnNode(14, 1));
        		toInject.add(new JumpInsnNode(GOTO, l6));
        		toInject.add(l5);
        		toInject.add(new FrameNode(Opcodes.F_FULL, 12, new Object[] {"jr", "gnu/trove/iterator/TLongShortIterator", Opcodes.LONG, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, "adq", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, "[Ladr;"}, 0, new Object[] {}));
        		toInject.add(new IincInsnNode(10, 1));
        		toInject.add(new JumpInsnNode(GOTO, l3));
        		toInject.add(l4);
        		toInject.add(new FrameNode(Opcodes.F_FULL, 2, new Object[] {"jr", "gnu/trove/iterator/TLongShortIterator"}, 0, new Object[] {}));
        		toInject.add(new JumpInsnNode(GOTO, l1));
        		toInject.add(l2);
        		toInject.add(new FrameNode(Opcodes.F_CHOP,1, null, 0, null));
        		toInject.add(new InsnNode(RETURN));
      		        		
        		m.instructions.add(toInject);

        		ok = true;
        		break;
    	   	}
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok) System.out.println("OK");
        else System.out.println("Failed."+ok);
                
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockPhysics.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }

    private byte[] transformEntity(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/Entity.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching Entity.class ..................");
        
        boolean ok = false;
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if ( m.name.equals("C") && m.desc.equals("()V") )
        	{
        		for (int index = 0; index < m.instructions.size() - 1 ; index++ )
        		{
        			if ( m.instructions.get(index).getType() == AbstractInsnNode.LDC_INSN && ((LdcInsnNode)m.instructions.get(index)).cst.equals(0.001D) )
        			{
        				ok = true;
        				m.instructions.set( m.instructions.get(index), new LdcInsnNode(new Double("0.07")));
        			}
        		}
    	   	}
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);        
        
        if (ok) System.out.println("OK");
        else System.out.println("Failed."+ok);
                
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/Entity.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformEntityMinecart(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/EntityMinecart.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching EntityMinecart.class ..........");
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);        
        
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "al", "()V", null, null);
        mv.visitCode();
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 1);
        mv.visitEnd();
        
        cw.visitEnd();
        
        System.out.println("OK");
                
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/EntityMinecart.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformEntityXPOrb(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/EntityXPOrb.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching EntityXPOrb.class .............");
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);        
        
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "al", "()V", null, null);
        mv.visitCode();
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 1);
        mv.visitEnd();
        
        cw.visitEnd();         
        
        System.out.println("OK");
                
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/EntityXPOrb.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformEntityBoat(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/EntityBoat.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching EntityBoat.class ..............");
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);        
        
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "al", "()V", null, null);
        mv.visitCode();
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 1);
        mv.visitEnd();
        
        cw.visitEnd();         
        
        System.out.println("OK");
                
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/EntityBoat.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformBlockWeb(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockWeb.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching BlockWeb.class ................");
        boolean ok = false;        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
                
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	if (m.name.equals("a") && m.desc.equals("(Labv;IIILnm;)V") )
        	{
    			InsnList toInject = new InsnList();
    			toInject.add(new VarInsnNode(ALOAD, 1));
    			toInject.add(new VarInsnNode(ILOAD, 2));
    			toInject.add(new VarInsnNode(ILOAD, 3));
    			toInject.add(new VarInsnNode(ILOAD, 4));
    			toInject.add(new VarInsnNode(ALOAD, 0));
    			toInject.add(new FieldInsnNode(GETFIELD, "arm", "cF", "I"));
    			toInject.add(new VarInsnNode(ALOAD, 5));
    			toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onEntityCollidedWithBlock", "(Labv;IIIILnm;)V"));
    			toInject.add(new InsnNode(RETURN));
            	
    			m.instructions.clear();
    			m.localVariables.clear();
    			m.instructions.add(toInject);

        		ok = true;
    			break;
        	}
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if ( ok ) System.out.println("OK");
        else System.out.println("Failed."+ok);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockWeb.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformWorld(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/World.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching World.class ...................");
        boolean ok = false, ok2 = false;        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
               
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("<init>") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
        		   		InsnList toInject = new InsnList();
		    			toInject.add(new VarInsnNode(ALOAD, 0));
		    		    toInject.add(new TypeInsnNode(NEW, "blockphysics/BTickList"));
		    		    toInject.add(new InsnNode(DUP));
		    		    toInject.add(new MethodInsnNode(INVOKESPECIAL, "blockphysics/BTickList", "<init>", "()V"));
		    		    toInject.add(new FieldInsnNode(PUTFIELD, "abv", "moveTickList", "Lblockphysics/BTickList;"));
		    		    toInject.add(new VarInsnNode(ALOAD, 0));
		    		    toInject.add(new TypeInsnNode(NEW, "java/util/HashSet"));
		    		    toInject.add(new InsnNode(DUP));
		    		    toInject.add(new MethodInsnNode(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V"));
		    		    toInject.add(new FieldInsnNode(PUTFIELD, "abv", "pistonMoveBlocks", "Ljava/util/HashSet;"));
		    		    toInject.add(new VarInsnNode(ALOAD, 0));
		    		    toInject.add(new TypeInsnNode(NEW, "blockphysics/ExplosionQueue"));
		    		    toInject.add(new InsnNode(DUP));
		    		    toInject.add(new MethodInsnNode(INVOKESPECIAL, "blockphysics/ExplosionQueue", "<init>", "()V"));
		    		    toInject.add(new FieldInsnNode(PUTFIELD, "abv", "explosionQueue", "Lblockphysics/ExplosionQueue;"));

		    			m.instructions.insertBefore(m.instructions.get(index),toInject);
		    			ok = true;
            		}
                }
        	}
        	else if ( m.name.equals("a") && m.desc.equals("(Lnm;DDDFZZ)Labq;") );
        	{
        		for ( int index = m.instructions.size()-1; index > 0; index--)
        		{
        			if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abq") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Z)V"))
        			{

        				while ( !(m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abq") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("()V")) )
        				{
        					m.instructions.remove(m.instructions.get(index));
        					index--;
        				}
        				m.instructions.remove(m.instructions.get(index-1));
        				m.instructions.remove(m.instructions.get(index-1));
        				
        				InsnList toInject = new InsnList();
        				toInject.add(new VarInsnNode(ALOAD, 0));
        				toInject.add(new FieldInsnNode(GETFIELD, "abv", "explosionQueue", "Lblockphysics/ExplosionQueue;"));
        				toInject.add(new VarInsnNode(ALOAD, 11));
        				toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "blockphysics/ExplosionQueue", "add", "(Labq;)V"));

            			m.instructions.insertBefore(m.instructions.get(index-1),toInject);
            			
            			ok2 = true;
            			break;
        			}
        		}
        	}
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
    	FieldVisitor fv;
    	
    	fv = cw.visitField(ACC_PUBLIC, "moveTickList", "Lblockphysics/BTickList;", null, null);
    	fv.visitEnd();
    	
    	fv = cw.visitField(ACC_PUBLIC, "pistonMoveBlocks", "Ljava/util/HashSet;", "Ljava/util/HashSet<Ljava/lang/String;>;", null);
    	fv.visitEnd();
    	
    	fv = cw.visitField(ACC_PUBLIC, "explosionQueue", "Lblockphysics/ExplosionQueue;", null, null);
    	fv.visitEnd();
   	
    	cw.visitEnd();
    	
        if ( ok && ok2 ) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/World.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformWorldServer(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/WorldServer.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	boolean ok = false, ok2 = false; 
    	
    	boolean bukkit = false;
    	try 
		{				
			Class.forName("org.bukkit.Bukkit");
			System.out.println("[BlockPhysics] Bukkit detected.");
			bukkit = true;
			ok2 = true;
	   	}
		catch(ClassNotFoundException e) 
		{
  	    
  	    }

    	System.out.print("[BlockPhysics] Patching WorldServer.class .............");
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("b") && m.desc.equals("()V") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			InsnList toInject = new InsnList();
            			toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "tickBlocksRandomMove", "(Ljr;)V"));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new FieldInsnNode(GETFIELD, "jr", "moveTickList", "Lblockphysics/BTickList;"));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "blockphysics/BTickList", "tickMoveUpdates", "(Labv;)V"));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new FieldInsnNode(GETFIELD, "jr", "pistonMoveBlocks", "Ljava/util/HashSet;"));
                		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "java/util/HashSet", "clear", "()V"));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new FieldInsnNode(GETFIELD, "jr", "explosionQueue", "Lblockphysics/ExplosionQueue;"));
                		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "blockphysics/ExplosionQueue", "doNextExplosion", "()V"));

            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			
            			ok = true;
            			break;
            		}
                }
            }
        	else if ( !bukkit && m.name.equals("a") && m.desc.equals("(Lnm;DDDFZZ)Labq;") );
        	{
        		for ( int index = m.instructions.size()-1; index > 0; index--)
        		{
        			if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abq") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Z)V"))
        			{

        				while ( !(m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abq") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("()V")) )
        				{
        					m.instructions.remove(m.instructions.get(index));
        					index--;
        				}
        				m.instructions.remove(m.instructions.get(index-1));
        				m.instructions.remove(m.instructions.get(index-1));
        				
        				InsnList toInject = new InsnList();
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "jr", "explosionQueue", "Lblockphysics/ExplosionQueue;"));
            			toInject.add(new VarInsnNode(ALOAD, 11));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "blockphysics/ExplosionQueue", "add", "(Labq;)V"));

            			m.instructions.insertBefore(m.instructions.get(index-1),toInject);
            			
            			ok2 = true;
            			break;
        			}
        		}
        	}
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if ( ok && ok2 ) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/WorldServer.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
   
    private byte[] transformExplosion(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/Explosion.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching Explosion.class ...............");
        boolean ok = false, ok2 = false, ok3 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
               
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();

        	if ((m.name.equals("<init>") && m.desc.equals("(Labv;Lnm;DDDF)V")))
        	{
        		InsnList toInject = new InsnList();
				toInject.add(new VarInsnNode(ALOAD, 0));
				toInject.add(new InsnNode(ICONST_0));
				toInject.add(new FieldInsnNode(PUTFIELD, "abq", "impact", "Z"));

				for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok = true;
            			break;
            		}
                }
        	}
        	else if ((m.name.equals("a") && m.desc.equals("()V")))
        	{
        		InsnList toInject = new InsnList();
				toInject.add(new VarInsnNode(ALOAD, 0));
				toInject.add(new FieldInsnNode(GETFIELD, "abq", "k", "Labv;"));
				toInject.add(new VarInsnNode(ALOAD, 0));
				toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "doExplosionA", "(Labv;Labq;)V"));
				toInject.add(new InsnNode(RETURN));

				m.instructions.clear();
	    		m.localVariables.clear();
				m.instructions.add(toInject);
        		ok2 = true;
        	}
        	else if (m.name.equals("a") && m.desc.equals("(Z)V"))
        	{
        		
        		InsnList toInject = new InsnList();
				toInject.add(new VarInsnNode(ALOAD, 0));
				toInject.add(new FieldInsnNode(GETFIELD, "abq", "k", "Labv;"));
				toInject.add(new VarInsnNode(ALOAD, 0));
				toInject.add(new VarInsnNode(ILOAD, 1));
				toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "doExplosionB", "(Labv;Labq;Z)V"));
				toInject.add(new InsnNode(RETURN));
				
				m.instructions.clear();
				m.localVariables.clear();
	    		m.instructions.add(toInject);
        		ok3 = true;
        	}
        	
        }
               
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);   
        
        FieldVisitor fv;
        fv = cw.visitField(ACC_PUBLIC, "impact", "Z", null, null);
        fv.visitEnd();
        
        cw.visitEnd();
        
        if (ok && ok2 && ok3) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok3);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/Explosion.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformMinecraftServer(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/MinecraftServer.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	boolean bukkit = false;
    	try 
		{				
			Class.forName("org.bukkit.Bukkit");
			System.out.println("[BlockPhysics] Bukkit detected.");
			bukkit = true;
	   	}
		catch(ClassNotFoundException e) 
		{
  	    
  	    }

    	System.out.print("[BlockPhysics] Patching MinecraftServer.class .........");
        boolean ok = false;
        boolean ok2 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();

        	if (m.name.equals("run") && m.desc.equals("()V") )
        	{
        		if (bukkit)
        		{
	        		int index;
	        		for (index = 0; index < m.instructions.size(); index++)
	        	    {
						if ( m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && m.instructions.get(index).getOpcode() == INVOKESTATIC && ((MethodInsnNode) m.instructions.get(index)).owner.equals("java/lang/System") && ((MethodInsnNode) m.instructions.get(index)).name.equals("nanoTime"))
						{
							ok = true;
							break;
						}
		            }
		        	
		        	if (ok)
		        	{
		        		while ( index < m.instructions.size() )
			            {
							if ( m.instructions.get(index).getOpcode() == LSUB )
							{
								InsnList toInject = new InsnList();
			        			toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "setSkipMoveM", "(J)J"));
			        			
			        			m.instructions.insert(m.instructions.get(index),toInject);
			        			
			        			ok2 = true;
			        			break;
							}
							index++;
						}
		        	}
        		}
	        	else
	        	{
		        	int index;
	        		for (index = 0; index < m.instructions.size(); index++)
		            {
	    				if ( m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) m.instructions.get(index)).owner.equals("net/minecraft/server/MinecraftServer") && ((MethodInsnNode) m.instructions.get(index)).name.equals("s"))
	    				{
							ok = true;
							break;
						}
		            }	
							
					if (ok)
					{
						while ( index > 0 )
			            {

							if ( m.instructions.get(index).getOpcode() == LLOAD &&  m.instructions.get(index).getType() == AbstractInsnNode.VAR_INSN &&  ((VarInsnNode)m.instructions.get(index)).var == 3)
							{
								InsnList toInject = new InsnList();
			        			toInject.add(new VarInsnNode(LLOAD, 7));
			        			toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "setSkipMove", "(J)V"));
			        			
			        			m.instructions.insertBefore(m.instructions.get(index),toInject);
			        			
			        			ok2 = true;
			        			break;
							}
							index--;
						}
					}
	        	}
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
    	
        if (ok && ok2) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2);

        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/MinecraftServer.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformEntityTrackerEntry(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/EntityTrackerEntry.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching EntityTrackerEntry.class ......");
        boolean ok = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();

        	if (m.name.equals("c") && m.desc.equals("()Lex;") )
        	{
        		for (int index = 0; index < m.instructions.size(); index++)
                {
        			if ( m.instructions.get(index).getType() == AbstractInsnNode.TYPE_INSN && m.instructions.get(index).getOpcode() == INSTANCEOF && ((TypeInsnNode) m.instructions.get(index)).desc.equals("sq"))
                    {
    					while ( m.instructions.get(index).getOpcode() != IFEQ )
    					{
    						index++;
    					}
    					index++;
    					
    					while ( m.instructions.get(index).getOpcode() != ARETURN )
    					{
    						m.instructions.remove(m.instructions.get(index));
    					}
    					
    					InsnList toInject = new InsnList();
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "jw", "a", "Lnm;"));
            			toInject.add(new TypeInsnNode(CHECKCAST, "sq"));
            			toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "spawnFallingSandPacket", "(Lsq;)Ldc;"));
            			
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok = true;
            			break;
                    }
                }
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
    	if (ok) System.out.println("OK");
        else System.out.println("Failed."+ok);
    	
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/EntityTrackerEntry.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
        	
    private byte[] transformEntityTracker(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/EntityTracker.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching EntityTracker.class ...........");
        boolean ok = false; 
        boolean	ok2 = false; 
        boolean	ok3 = false; 
        boolean	ok4 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("<init>") && m.desc.equals("(Ljr;)V") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			InsnList toInject = new InsnList();
            			toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(ICONST_0));
                    	toInject.add(new FieldInsnNode( PUTFIELD, "jl", "movingblocks", "I"));
                    	
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok = true;
            			break;
            		}
                }
        	}
        	else if (m.name.equals("a") && m.desc.equals("(Lnm;)V") )
        	{
        		for (int index = 0; index < m.instructions.size(); index++)
                {
        			if ( m.instructions.get(index).getType() == AbstractInsnNode.TYPE_INSN && m.instructions.get(index).getOpcode() == INSTANCEOF && ((TypeInsnNode) m.instructions.get(index)).desc.equals("tb"))
                    {
        				m.instructions.remove(m.instructions.get(index-1));
    					
        				while ( m.instructions.get(index).getType() != AbstractInsnNode.TYPE_INSN || m.instructions.get(index).getOpcode() != INSTANCEOF )
    					{
    						m.instructions.remove(m.instructions.get(index-1));
        					ok2 = true;
    					}
            			break;
                    }
                }
        		
        		for (int index = 0; index < m.instructions.size(); index++)
                {
        			if ( m.instructions.get(index).getType() == AbstractInsnNode.TYPE_INSN && m.instructions.get(index).getOpcode() == INSTANCEOF && ((TypeInsnNode) m.instructions.get(index)).desc.equals("sq"))
                    {
    					while ( m.instructions.get(index).getOpcode() != GOTO )
    					{
    						index++;
    						if (m.instructions.get(index).getType() == AbstractInsnNode.INT_INSN && m.instructions.get(index).getOpcode() == BIPUSH && ((IntInsnNode) m.instructions.get(index)).operand == 20)
    						{
    							m.instructions.set(m.instructions.get(index),new IntInsnNode(BIPUSH, 40));
    							ok3 = true;
    						}
    					}

    					InsnList toInject = new InsnList();
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new InsnNode(DUP));
            			toInject.add(new FieldInsnNode( GETFIELD, "jl", "movingblocks", "I"));
            			toInject.add(new InsnNode(ICONST_1));
            			toInject.add(new InsnNode(IADD));
                    	toInject.add(new FieldInsnNode( PUTFIELD, "jl", "movingblocks", "I"));
                    	
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			break;
                    }
                }
            }
        	else if (m.name.equals("b") && m.desc.equals("(Lnm;)V") )
            {
            	InsnList toInject = new InsnList();
            	
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new TypeInsnNode(INSTANCEOF, "sq"));
            	LabelNode l3 = new LabelNode();
            	toInject.add(new JumpInsnNode(IFEQ, l3));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new InsnNode(DUP));
            	toInject.add(new FieldInsnNode(GETFIELD, "jl", "movingblocks", "I"));
            	toInject.add(new InsnNode(ICONST_1));
            	toInject.add(new InsnNode(ISUB));
            	toInject.add(new FieldInsnNode(PUTFIELD, "jl", "movingblocks", "I"));
            	toInject.add(l3);
            	
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok4 = true;
            			break;
            		}
                }
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);

        FieldVisitor fv;
        	
    	fv = cw.visitField(ACC_PUBLIC, "movingblocks", "I", null, null);
        fv.visitEnd();
        
        cw.visitEnd();

        if (ok && ok2 && ok3 && ok4) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok3+ok4);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/EntityTracker.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformEntityTNTPrimed(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/EntityTNTPrimed.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	boolean bukkit = false;
    	try 
		{				
			Class.forName("org.bukkit.Bukkit");
			System.out.println("[BlockPhysics] Bukkit detected.");
			bukkit = true;
	   	}
		catch(ClassNotFoundException e) 
		{
  	     
  	    }

    	System.out.print("[BlockPhysics] Patching EntityTNTPrimed.class .........");
        boolean ok = false, ok2 = false, ok5 = false, ok6 = false, ok7 = false, ok8 = false, ok9 = false, ok10 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        classNode.superName = "sq";
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("<init>") && m.desc.equals("(Labv;)V") )
        	{
        		for (int index = 0; index < m.instructions.size(); index++)
                {
    				if (m.instructions.get(index).getOpcode() == INVOKESPECIAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("nm") && ((MethodInsnNode) m.instructions.get(index)).name.equals("<init>") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Labv;)V"))
                    {
    					m.instructions.set(m.instructions.get(index),new MethodInsnNode(INVOKESPECIAL, "sq", "<init>", "(Labv;)V"));
    					ok = true;
        				break;
            		}
                }
        	}
        	else if (m.name.equals("<init>") && m.desc.equals("(Labv;DDDLoe;)V") )
        	{
				InsnList toInject = new InsnList();
	 			
				toInject.add(new VarInsnNode(ALOAD, 0));
				toInject.add(new VarInsnNode(ALOAD, 1));
				toInject.add(new VarInsnNode(DLOAD, 2));
				toInject.add(new VarInsnNode(DLOAD, 4));
				toInject.add(new VarInsnNode(DLOAD, 6));
				toInject.add(new IntInsnNode(BIPUSH, 46));
				toInject.add(new InsnNode(ICONST_0));
				toInject.add(new MethodInsnNode(INVOKESPECIAL, "sq", "<init>", "(Labv;DDDII)V"));
				toInject.add(new VarInsnNode(ALOAD, 0));
				toInject.add(new IntInsnNode(BIPUSH, 80));
				toInject.add(new FieldInsnNode(PUTFIELD, "tb", "a", "I"));
				
				if (bukkit)
				{
					toInject.add(new VarInsnNode(ALOAD, 0));
					toInject.add(new LdcInsnNode(new Float("4.0")));
					toInject.add(new FieldInsnNode(PUTFIELD, "tb", "yield", "F"));
					toInject.add(new VarInsnNode(ALOAD, 0));
					toInject.add(new InsnNode(ICONST_0));
					toInject.add(new FieldInsnNode(PUTFIELD, "tb", "isIncendiary", "Z"));
				}

				toInject.add(new InsnNode(RETURN));

				m.instructions.clear();
				m.localVariables.clear();
	    		m.instructions.add(toInject);
	
				ok2 = true;
        	}
        	else if (m.name.equals("K") && m.desc.equals("()Z") )
        	{
        		InsnList toInject = new InsnList();
        		toInject.add(new InsnNode(ICONST_0));
        		toInject.add(new InsnNode(IRETURN));
        		
        		m.instructions.clear();
        		m.localVariables.clear();
        		m.instructions.add(toInject);
        		ok5 = true;
        	}
        	else if (m.name.equals("l_") && m.desc.equals("()V") )
        	{
        		InsnList toInject = new InsnList();
        		toInject.add(new VarInsnNode(ALOAD, 0));
        		toInject.add(new FieldInsnNode(GETFIELD, "tb", "q", "Labv;"));
        		toInject.add(new VarInsnNode(ALOAD, 0));
        		toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "fallingSandUpdate", "(Labv;Lsq;)V"));
        		toInject.add(new InsnNode(RETURN));
        		
        		m.instructions.clear();
        		m.localVariables.clear();
        		m.instructions.add(toInject);
        		ok6 = true;
            }
        	else if (m.name.equals("b") && m.desc.equals("(Lbx;)V") )
            {
            	InsnList toInject = new InsnList();
            	
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new MethodInsnNode(INVOKESPECIAL, "sq", "b", "(Lbx;)V"));
           	
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok7 = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("a") && m.desc.equals("(Lbx;)V") )
            {
            	InsnList toInject = new InsnList();
            	
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new MethodInsnNode(INVOKESPECIAL, "sq", "a", "(Lbx;)V"));
           	
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok8 = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("d") && m.desc.equals("()V") )
            {
            	m.access = ACC_PUBLIC;
    			ok9 = true;
    			if (bukkit)
    			{
	    			for (int index = 0; index < m.instructions.size(); index++ )
	    			{
	    				if (m.instructions.get(index).getOpcode() == INVOKESTATIC && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.contains("entity/CraftEntity") && ((MethodInsnNode) m.instructions.get(index)).name.equals("getEntity") )
	    				{
	    					String pclass = ((MethodInsnNode) m.instructions.get(index)).owner.replace("entity/CraftEntity", "");
	    					m.instructions.set(m.instructions.get(index), new MethodInsnNode(INVOKESPECIAL, pclass+"entity/CraftTNTPrimed", "<init>", "(L"+pclass+"CraftServer;Ltb;)V"));
	
	    					InsnList toInject = new InsnList();
	    	    				    	    			
	    	    			toInject.add(new TypeInsnNode(NEW, pclass+"entity/CraftTNTPrimed"));
	    	    			toInject.add(new InsnNode(DUP));
	
	    	    			m.instructions.insert(m.instructions.get(index-3),toInject);
	    	    			
	    	    			ok10 = true;
	    	    			break;
	    				}	
	    			}
    			}
    			else 
    			{
    				for (int index = 0; index < m.instructions.size(); index++ )
    				{	
    					if (m.instructions.get(index).getOpcode() == ACONST_NULL )
	    				{
    						m.instructions.set(m.instructions.get(index),new VarInsnNode(ALOAD, 0));
     						ok10 = true;
    						break;
	    				}
	    			}
	    		}
            }       	
        }
        
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2 && ok5 && ok6 && ok7 && ok8 && ok9) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+  ok5+  ok6+  ok7+  ok8+ ok9);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/EntityTNTPrimed.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
     
    private byte[] transformEntityFallingSand(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/EntityFallingSand.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching EntityFallingSand.class .......");
        boolean ok = false;
        boolean ok2 = false;
        boolean ok3 = false;
        boolean ok4 = false;
        boolean ok5 = false;
        boolean ok6 = false;
        boolean ok7 = false;
        boolean ok8 = false;
        boolean ok9 = false, ok10 = false;
                
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	        	
        	if (m.name.equals("<init>") && m.desc.equals("(Labv;)V") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			InsnList toInject = new InsnList();
             			
            			toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(ICONST_1));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "m", "Z"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new LdcInsnNode(new Float("0.996")));
                    	toInject.add(new LdcInsnNode(new Float("0.996")));
                    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "sq", "a", "(FF)V"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new FieldInsnNode(GETFIELD, "sq", "P", "F"));
                    	toInject.add(new InsnNode(FCONST_2));
                    	toInject.add(new InsnNode(FDIV));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "N", "F"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(DCONST_0));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "x", "D"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(DCONST_0));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "y", "D"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(DCONST_0));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "z", "D"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(DCONST_0));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationX", "D"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new LdcInsnNode(new Double("-0.024525")));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationY", "D"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(DCONST_0));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationZ", "D"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(ICONST_0));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "slideDir", "B"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(ICONST_1));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "Z", "Z"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new LdcInsnNode(new Float("0.8")));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "aa", "F"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(ICONST_4));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "dead", "B"));
                    	toInject.add(new VarInsnNode(ALOAD, 0));
                    	toInject.add(new InsnNode(ICONST_0));
                    	toInject.add(new FieldInsnNode(PUTFIELD, "sq", "bpdata", "I"));
                    	                    	
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok10 = true;
            			break;
            		}
                }
        	}
        	else if (m.name.equals("<init>") && m.desc.equals("(Labv;DDDII)V") )
        	{
        		for (int index = 0; index < m.instructions.size(); index++)
                {
    				if (m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("sq") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a"))
                    {
    					m.instructions.set(m.instructions.get(index-1), new LdcInsnNode(new Float("0.996")));
    					m.instructions.set(m.instructions.get(index-2), new LdcInsnNode(new Float("0.996")));
    					ok = true;
        				break;
            		}
                }

            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			InsnList toInject = new InsnList();
             			
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new InsnNode(DCONST_0));
            			toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationX", "D"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new LdcInsnNode(new Double("-0.024525")));
            			toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationY", "D"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new InsnNode(DCONST_0));
            			toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationZ", "D"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new InsnNode(ICONST_0));
            			toInject.add(new FieldInsnNode(PUTFIELD, "sq", "slideDir", "B"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new InsnNode(ICONST_1));
            			toInject.add(new FieldInsnNode(PUTFIELD, "sq", "Z", "Z"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new LdcInsnNode(new Float("0.8")));
            			toInject.add(new FieldInsnNode(PUTFIELD, "sq", "aa", "F"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new InsnNode(ICONST_4));
            			toInject.add(new FieldInsnNode(PUTFIELD, "sq", "dead", "B"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new InsnNode(ICONST_0));
            			toInject.add(new FieldInsnNode(PUTFIELD, "sq", "bpdata", "I"));

            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok2 = true;
            			break;
            		}
                }
        	}
        	else if (m.name.equals("K") && m.desc.equals("()Z") )
        	{
        		InsnList toInject = new InsnList();
        		toInject.add(new InsnNode(ICONST_0));
        		toInject.add(new InsnNode(IRETURN));
        		
        		m.instructions.clear();
        		m.localVariables.clear();
        		m.instructions.add(toInject);
        		ok9 = true;
        	}
        	else if (m.name.equals("l_") && m.desc.equals("()V") )
        	{
        		InsnList toInject = new InsnList();
    			toInject.add(new VarInsnNode(ALOAD, 0));
    			toInject.add(new FieldInsnNode(GETFIELD, "sq", "q", "Labv;"));
    			toInject.add(new VarInsnNode(ALOAD, 0));
    			toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "fallingSandUpdate", "(Labv;Lsq;)V"));
    			toInject.add(new InsnNode(RETURN));
    			
    			m.instructions.clear();
    			m.localVariables.clear();
    			m.instructions.add(toInject);
    			ok3 = true;
        	}
        	else if (m.name.equals("b") && m.desc.equals("(F)V") )
        	{
        		m.instructions.clear();
        		m.localVariables.clear();
        		m.instructions.add(new InsnNode(RETURN));
        		ok4 = true;
        	}
        	else if (m.name.equals("b") && m.desc.equals("(Lbx;)V") )
        	{
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			InsnList toInject = new InsnList();
            			
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new LdcInsnNode("Acceleration"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new InsnNode(ICONST_3));
            			toInject.add(new IntInsnNode(NEWARRAY, T_DOUBLE));
            			toInject.add(new InsnNode(DUP));
            			toInject.add(new InsnNode(ICONST_0));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "sq", "accelerationX", "D"));
            			toInject.add(new InsnNode(DASTORE));
            			toInject.add(new InsnNode(DUP));
            			toInject.add(new InsnNode(ICONST_1));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "sq", "accelerationY", "D"));
            			toInject.add(new InsnNode(DASTORE));
            			toInject.add(new InsnNode(DUP));
            			toInject.add(new InsnNode(ICONST_2));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "sq", "accelerationZ", "D"));
            			toInject.add(new InsnNode(DASTORE));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "sq", "a", "([D)Lcf;"));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "a", "(Ljava/lang/String;Lck;)V"));
            			toInject.add(new VarInsnNode(ALOAD, 1));
            			toInject.add(new LdcInsnNode("BPData"));
            			toInject.add(new VarInsnNode(ALOAD, 0));
            			toInject.add(new FieldInsnNode(GETFIELD, "sq", "bpdata", "I"));
            			toInject.add(new InsnNode(I2B));
            			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "a", "(Ljava/lang/String;B)V"));
            			
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok5 = true;
            			break;
            		}
                }        		
        	}
        	else if (m.name.equals("a") && m.desc.equals("(Lbx;)V") )
        	{
        		
        		while( !(m.instructions.get(0).getType() == AbstractInsnNode.LDC_INSN && ((LdcInsnNode) m.instructions.get(0)).cst.equals("Data")) )
				{
					m.instructions.remove(m.instructions.get(0));
					ok6 = true;
				}
        		
        		InsnList toInject = new InsnList();
    			toInject.add(new VarInsnNode(ALOAD, 0));
    			toInject.add(new VarInsnNode(ALOAD, 1));
    			toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "readFallingSandID", "(Lbx;)I"));
    			toInject.add(new FieldInsnNode(PUTFIELD, "sq", "a", "I"));
    			toInject.add(new VarInsnNode(ALOAD, 0));
    			toInject.add(new VarInsnNode(ALOAD, 1));

    			m.instructions.insertBefore(m.instructions.get(0),toInject);
       		
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			toInject = new InsnList();
            			toInject.add(new VarInsnNode(ALOAD, 1));
                		toInject.add(new LdcInsnNode("Acceleration"));
                		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "b", "(Ljava/lang/String;)Z"));
                		LabelNode l4 = new LabelNode();
                		toInject.add(new JumpInsnNode(IFEQ, l4));
                		toInject.add(new VarInsnNode(ALOAD, 1));
                		toInject.add(new LdcInsnNode("Acceleration"));
                		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "m", "(Ljava/lang/String;)Lcf;"));
                		toInject.add(new VarInsnNode(ASTORE, 2));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new VarInsnNode(ALOAD, 2));
                		toInject.add(new InsnNode(ICONST_0));
                		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "cf", "b", "(I)Lck;"));
                		toInject.add(new TypeInsnNode(CHECKCAST, "ca"));
                		toInject.add(new FieldInsnNode(GETFIELD, "ca", "a", "D"));
                		toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationX", "D"));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new VarInsnNode(ALOAD, 2));
                		toInject.add(new InsnNode(ICONST_1));
                		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "cf", "b", "(I)Lck;"));
                		toInject.add(new TypeInsnNode(CHECKCAST, "ca"));
                		toInject.add(new FieldInsnNode(GETFIELD, "ca", "a", "D"));
                		toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationY", "D"));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new VarInsnNode(ALOAD, 2));
                		toInject.add(new InsnNode(ICONST_2));
                		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "cf", "b", "(I)Lck;"));
                		toInject.add(new TypeInsnNode(CHECKCAST, "ca"));
                		toInject.add(new FieldInsnNode(GETFIELD, "ca", "a", "D"));
                		toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationZ", "D"));
                		LabelNode l5 = new LabelNode();
                		toInject.add(new JumpInsnNode(GOTO, l5));
                		toInject.add(l4);
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new InsnNode(DCONST_0));
                		toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationX", "D"));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new InsnNode(DCONST_0));
                		toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationY", "D"));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new InsnNode(DCONST_0));
                		toInject.add(new FieldInsnNode(PUTFIELD, "sq", "accelerationZ", "D"));
                		toInject.add(l5);
            			toInject.add(new VarInsnNode(ALOAD, 1));
                		toInject.add(new LdcInsnNode("BPData"));
                		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "b", "(Ljava/lang/String;)Z"));
                		LabelNode l7 = new LabelNode();
                		toInject.add(new JumpInsnNode(IFEQ, l7));
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new VarInsnNode(ALOAD, 1));
                		toInject.add(new LdcInsnNode("BPData"));
                		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "bx", "c", "(Ljava/lang/String;)B"));
                		toInject.add(new FieldInsnNode(PUTFIELD, "sq", "bpdata", "I"));
                		LabelNode l8 = new LabelNode();
                		toInject.add(new JumpInsnNode(GOTO, l8));
                		toInject.add(l7);
                		toInject.add(new VarInsnNode(ALOAD, 0));
                		toInject.add(new InsnNode(ICONST_0));
                		toInject.add(new FieldInsnNode(PUTFIELD, "sq", "bpdata", "I"));
                		toInject.add(l8);
                		
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			
            			ok7 = true;
            			break;
            		}
                }
        	}
        	else if (m.name.equals("au") && m.desc.equals("()Z") )
        	{
        		InsnList toInject = new InsnList();
    			toInject.add(new VarInsnNode(ALOAD, 0));
    			toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "sq", "ae", "()Z"));
    			toInject.add(new InsnNode(IRETURN));
    			
        		m.instructions.clear();
    			m.localVariables.clear();
    			m.instructions.add(toInject);
        		ok8 = true;
        	}
        	
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        FieldVisitor fv;

        fv = cw.visitField(ACC_PUBLIC, "accelerationX", "D", null, null);
        fv.visitEnd();

        fv = cw.visitField(ACC_PUBLIC, "accelerationY", "D", null, null);
        fv.visitEnd();

        fv = cw.visitField(ACC_PUBLIC, "accelerationZ", "D", null, null);
        fv.visitEnd();

        fv = cw.visitField(ACC_PUBLIC, "bpdata", "I", null, null);
        fv.visitEnd();

        fv = cw.visitField(ACC_PUBLIC, "slideDir", "B", null, null);
        fv.visitEnd();

        fv = cw.visitField(ACC_PUBLIC, "media", "I", null, null);
        fv.visitEnd();
        
        fv = cw.visitField(ACC_PUBLIC, "dead", "B", null, null);
        fv.visitEnd();
        
        MethodVisitor mv;
        
        mv = cw.visitMethod(ACC_PUBLIC, "D", "()Lasu;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "sq", "E", "Lasu;");
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        mv = cw.visitMethod(ACC_PUBLIC, "al", "()V", null, null);
        mv.visitCode();
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 1);
        mv.visitEnd();
        
        mv = cw.visitMethod(ACC_PUBLIC, "d", "(DDD)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "sq", "q", "Labv;");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(DLOAD, 1);
        mv.visitVarInsn(DLOAD, 3);
        mv.visitVarInsn(DLOAD, 5);
        mv.visitMethodInsn(INVOKESTATIC, "blockphysics/BlockPhysics", "moveEntity", "(Labv;Lsq;DDD)V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(8, 7);
        mv.visitEnd();
        
        if (!ok8)
        {
        	mv = cw.visitMethod(ACC_PUBLIC, "au", "()Z", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, "sq", "ae", "()Z");
            mv.visitInsn(IRETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        	        	
        	ok8 = true;
        }

        cw.visitEnd();

        if (ok && ok2 && ok3 && ok4 && ok5 && ok6 && ok7 && ok8 && ok9 && ok10) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok3+ok4+ok5+ok6+ok7+ok8+ok9+ok10);
       
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/EntityFallingSand.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformRenderFallingSand(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/RenderFallingSand.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching RenderFallingSand.class .......");
        boolean ok = false, ok2 = false, ok4 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("a") && m.desc.equals("(Lsq;DDDFF)V") )
        	{
        		InsnList toInject = new InsnList();
	           	
        	    toInject.add(new VarInsnNode(ALOAD, 1));
        	    toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BClient", "cancelRender", "(Lsq;)Z"));
        		LabelNode l0 = new LabelNode();
        		toInject.add(new JumpInsnNode(IFEQ, l0));
        		toInject.add(new InsnNode(RETURN));
        		toInject.add(l0);
        		
        		m.instructions.insertBefore(m.instructions.getFirst(),toInject);
        		
        		int index;
        		for (index = 0; index < m.instructions.size(); index++)
                {
    				if (m.instructions.get(index).getOpcode() == GETFIELD && m.instructions.get(index).getType() == AbstractInsnNode.FIELD_INSN && ((FieldInsnNode) m.instructions.get(index)).owner.equals("sq") && ((FieldInsnNode) m.instructions.get(index)).name.equals("a") && ((FieldInsnNode) m.instructions.get(index)).desc.equals("I"))
                    {
    					index = index + 3;
    					ok = true;
    					break;
            		}
    			}	
        		
				while (!( m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).name.equals("glPushMatrix")))
				{
					m.instructions.remove(m.instructions.get(index));
					ok2 = true;
				}
              
        		for (index = 0; index < m.instructions.size(); index++)
                {
    				if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("bfo") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Laqw;Labv;IIII)V"))
                    {
    					m.instructions.set(m.instructions.get(index), new MethodInsnNode(INVOKESTATIC, "blockphysics/BClient", "renderBlockSandFalling", "(Lbfo;Laqw;Labv;IIII)V"));
    					ok4 = true;
    					break;
                    }
                }    			
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2 && ok4) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok4);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/RenderFallingSand.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformNetClientHandler(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/NetClientHandler.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching NetClientHandler.class ........");
        boolean ok = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("a") && m.desc.equals("(Ldc;)V") )
        	{
            	InsnList toInject = new InsnList();
            	           	
        	    toInject.add(new VarInsnNode(ALOAD, 0));
        	    toInject.add(new FieldInsnNode(GETFIELD, "bct", "i", "Lbda;"));
        	    toInject.add(new VarInsnNode(DLOAD, 2));
        	    toInject.add(new VarInsnNode(DLOAD, 4));
        	    toInject.add(new VarInsnNode(DLOAD, 6));
        	    toInject.add(new VarInsnNode(ALOAD, 1));
        	    toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "createFallingsand", "(Labv;DDDLdc;)Lsq;"));
        	    toInject.add(new VarInsnNode(ASTORE, 8));
        	    toInject.add(new VarInsnNode(ALOAD, 1));
        	    toInject.add(new InsnNode(ICONST_1));
        	    toInject.add(new FieldInsnNode(PUTFIELD, "dc", "k", "I"));

    			for (int index = 0; index < m.instructions.size(); index++)
                {
    				if (m.instructions.get(index).getOpcode() == INVOKESPECIAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("sq") && ((MethodInsnNode) m.instructions.get(index)).name.equals("<init>"))
                    {
    					while( m.instructions.get(index).getType() != AbstractInsnNode.FIELD_INSN || m.instructions.get(index).getOpcode() != PUTFIELD || !((FieldInsnNode) m.instructions.get(index)).owner.equals("dc") || !((FieldInsnNode) m.instructions.get(index)).name.equals("k"))
        				{
    						index++;
        				}
    					
    					while( m.instructions.get(index).getType() != AbstractInsnNode.TYPE_INSN || !((TypeInsnNode) m.instructions.get(index)).desc.equals("sq"))
        				{
    						m.instructions.remove(m.instructions.get(index));
    						index--;
    						ok = true;
        				}
    					
    					if (ok) 
    					{
    						m.instructions.remove(m.instructions.get(index));
    						m.instructions.insertBefore(m.instructions.get(index),toInject);
    					}

    					break;
            		}
                }
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok) System.out.println("OK");
        else System.out.println("Failed."+ok);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/NetClientHandler.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformGuiSelectWorld(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/GuiSelectWorld.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching GuiSelectWorld.class ..........");
        boolean ok = false, ok2 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	            
        	if (m.name.equals("A_") && m.desc.equals("()V") )
        	{
            	InsnList toInject = new InsnList();
            	           	
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new InsnNode(ICONST_0));
            	toInject.add(new FieldInsnNode(PUTFIELD, "awe", "d", "Z"));
            	      	
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("e") && m.desc.equals("(I)V") )
            {
        		
    			for (int index = 0; index < m.instructions.size(); index++)
                {
        			if (m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN  && ((MethodInsnNode) m.instructions.get(index)).owner.equals("ats") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Ljava/lang/String;Ljava/lang/String;Lacc;)V"))
                    {
        				InsnList toInject = new InsnList();
        				toInject.add(new VarInsnNode(ALOAD, 0));
        				toInject.add(new VarInsnNode(ALOAD, 2));
        				toInject.add(new VarInsnNode(ALOAD, 3));
        				toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BClient", "loadWorld", "(Lawb;Ljava/lang/String;Ljava/lang/String;)V"));
        				
        				m.instructions.insert(m.instructions.get(index),toInject);
            			
        				while( m.instructions.get(index).getType() != AbstractInsnNode.JUMP_INSN)
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
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/GuiSelectWorld.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
      
    
    private byte[] transformGuiCreateWorld(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/GuiCreateWorld.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching GuiCreateWorld.class ..........");
        boolean ok = false, ok2 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("A_") && m.desc.equals("()V") )
            {
            	InsnList toInject = new InsnList();
            	           	
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new InsnNode(ICONST_0));
            	toInject.add(new FieldInsnNode(PUTFIELD, "auy", "v", "Z"));
            	            	
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("a") && m.desc.equals("(Lauq;)V") )
            {
        		for (int index = 0; index < m.instructions.size(); index++)
                {
        			if (m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("ats") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Ljava/lang/String;Ljava/lang/String;Lacc;)V"))
                    {
        				m.instructions.remove(m.instructions.get(index));
        				
        				InsnList toInject = new InsnList();
        				
        				toInject.add(new InsnNode(ICONST_1));
        				toInject.add(new MethodInsnNode(INVOKESPECIAL, "blockphysics/BGui", "<init>", "(Lawb;Ljava/lang/String;Ljava/lang/String;Lacc;Z)V"));
        				toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "ats", "a", "(Lawb;)V"));
        				
        				m.instructions.insertBefore(m.instructions.get(index),toInject);
        				
        				while( m.instructions.get(index).getOpcode() != GETFIELD || m.instructions.get(index).getType() != AbstractInsnNode.FIELD_INSN || !((FieldInsnNode) m.instructions.get(index)).owner.equals("auy") || !((FieldInsnNode) m.instructions.get(index)).name.equals("f") || !((FieldInsnNode) m.instructions.get(index)).desc.equals("Lats;"))
        				{
        					index--;
        				}
        				
        				toInject = new InsnList();
        				
        				toInject.add(new TypeInsnNode(NEW, "blockphysics/BGui"));
        				toInject.add(new InsnNode(DUP));
        				toInject.add(new VarInsnNode(ALOAD, 0));
        				
        				m.instructions.insert(m.instructions.get(index),toInject);
        				
        				ok2 = true;
        				break;
            		}
                }
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2);

        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/GuiCreateWorld.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        return cw.toByteArray();
    }
    
    private byte[] transformBlock(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/Block.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
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
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();     	
        	
        	if (m.name.equals("g") && m.desc.equals("(Labv;IIII)V") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ILOAD, 5));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "aqw", "cF", "I"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onBlockDestroyedByPlayer", "(Labv;IIIII)V"));
                
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN )
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok2 = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("a") && m.desc.equals("(Labv;IIII)V") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "aqw", "cF", "I"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
                
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN )
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok3 = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("b") && m.desc.equals("(Labv;IIILnm;)V") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "aqw", "cF", "I"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
                
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN )
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok4 = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("a") && m.desc.equals("(Labv;IIILnm;)V") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "aqw", "cF", "I"));
            	toInject.add(new VarInsnNode(ALOAD, 5));
            	toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onEntityCollidedWithBlock", "(Labv;IIIILnm;)V"));
                
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN )
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok5 = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("k") && m.desc.equals("(Labv;IIII)V") )
            {
        		InsnList toInject = new InsnList();
        		toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "aqw", "cF", "I"));
            	toInject.add(new VarInsnNode(ILOAD, 5));
            	toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onPostBlockPlaced", "(Labv;IIIII)V"));
            	 
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN )
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok6 = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("a") && m.desc.equals("(Labv;IIILnm;F)V") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "aqw", "cF", "I"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
                
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN )
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok7 = true;
            			break;
            		}
                }
            }
        }
               
        FieldNode f;
        Iterator<FieldNode> fields = classNode.fields.iterator();
        while(fields.hasNext())
        {
        	f = fields.next();     	
        	
        	if (f.name.equals("blockFlammability") && f.desc.equals("[I") )
            {
        		f.access = ACC_PUBLIC + ACC_STATIC;
        		ok8 = true;
        		break;
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw); 
        
        if (ok2 && ok3 && ok4 && ok5 && ok6 && ok7 && ok8) System.out.println("OK");
        else System.out.println("Failed."+ok2+ok3+ok4+ok5+ok6+ok7+ok8);

        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/Block.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        return cw.toByteArray();
    }
    
    
    private byte[] transformBlockRailBase(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockRailBase.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching BlockRailBase.class ...........");
        boolean ok = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	if (m.name.equals("a") && m.desc.equals("(Labv;IIII)V") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "amv", "cF", "I"));
            	toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
                
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok = true;
            			break;
            		}
                }
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok) System.out.println("OK");
        else System.out.println("Failed."+ok);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockRailBase.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
      
        return cw.toByteArray();
    }
    
    private byte[] transformBlockPistonBase(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockPistonBase.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching BlockPistonBase.class .........");
        boolean ok = false, ok2 = false, ok3 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
                
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("a") && m.desc.equals("(Labv;IIII)V") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "asq", "cF", "I"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
                
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("k") && m.desc.equals("(Labv;III)V") )
            {
        		InsnList toInject = new InsnList();
        		toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "asq", "a", "Z"));
            	toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "updatePistonState", "(Labv;IIILasq;Z)V"));
            	toInject.add(new InsnNode(RETURN));
            	
            	m.instructions.clear();
            	m.localVariables.clear();
            	m.instructions.add(toInject);
            	ok2 = true;
            }
        	else if (m.name.equals("b") && m.desc.equals("(Labv;IIIII)Z") )
            {
        		InsnList toInject = new InsnList();
        		toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ILOAD, 5));
            	toInject.add(new VarInsnNode(ILOAD, 6));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "asq", "a", "Z"));
            	toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onBlockPistonEventReceived", "(Labv;IIIIILasq;Z)Z"));
            	toInject.add(new InsnNode(IRETURN));
            	
            	m.instructions.clear();
            	m.localVariables.clear();
            	m.instructions.add(toInject);
            	ok3 = true;
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2 && ok3) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok3);

        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockPistonBase.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        return cw.toByteArray();
    }
    
    private byte[] transformBlockSand(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockSand.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching BlockSand.class ...............");
        boolean ok = false;
        boolean ok2 = false;
        boolean ok3 = false;
        boolean ok4 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();

        	if (m.name.equals("a") && m.desc.equals("(Labv;IIII)V") )
            {
        		InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "aop", "cF", "I"));
            	toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
            	toInject.add(new InsnNode(RETURN));
        		
        		m.instructions.clear();
        		m.localVariables.clear();
        		m.instructions.add(toInject);
        		ok = true;
            }
        	else if (m.name.equals("a") && m.desc.equals("(Labv;IIILjava/util/Random;)V") )
            {
        		m.instructions.clear();
        		m.localVariables.clear();
            	m.instructions.insert(new InsnNode(RETURN));
            	ok2 = true;
            }
        	else if (m.name.equals("k") && m.desc.equals("(Labv;III)V") )
            {
        		m.instructions.clear();
        		m.localVariables.clear();
        		m.instructions.insert(new InsnNode(RETURN));
            	ok3 = true;
            }
            else if (m.name.equals("a_") && m.desc.equals("(Labv;III)Z") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new VarInsnNode(ILOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new InsnNode(ICONST_0));
            	toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "canMoveTo", "(Labv;IIII)Z"));
            	toInject.add(new InsnNode(IRETURN));
        		
        		m.instructions.clear();
        		m.localVariables.clear();
        		m.instructions.add(toInject);
        		ok4 = true;
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2 && ok3 && ok4) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok3+ok4);

        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockSand.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
        return cw.toByteArray();
    }
    
    private byte[] transformBlockRedstoneLight(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockRedstoneLight.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching BlockRedstoneLight.class ......");
        boolean ok = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("a") && m.desc.equals("(Labv;IIII)V") )
            {
            	InsnList toInject = new InsnList();
              	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "aqa", "cF", "I"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
                
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok = true;
            			break;
            		}
                }
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
       
        if (ok) System.out.println("OK");
        else System.out.println("Failed."+ok);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockRedstoneLight.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        return cw.toByteArray();
    }
    
    private byte[] transformBlockTNT(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockTNT.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching BlockTNT.class ................");
        boolean ok = false;
        boolean ok2 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("a") && m.desc.equals("(Labv;IIII)V") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "arb", "cF", "I"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
                
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok = true;
            			break;
            		}
                }
            }
        	else if (m.name.equals("g") && m.desc.equals("(Labv;IIII)V") )
            {
        		InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ILOAD, 5));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "arb", "cF", "I"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onBlockDestroyedByPlayer", "(Labv;IIIII)V"));
                
                for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok2 = true;
            			break;
            		}
                }
            }      
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2);

        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockTNT.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        
        return cw.toByteArray();
    }
    
    private byte[] transformBlockFarmland(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockFarmland.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching BlockFarmland.class ...........");
        boolean ok = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("a") && m.desc.equals("(Labv;IIILnm;F)V") )
            {
            	InsnList toInject = new InsnList();

            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "aoc", "cF", "I"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
                
            	for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			m.instructions.insertBefore(m.instructions.get(index),toInject);
            			ok = true;
            			break;
            		}
                }
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok) System.out.println("OK");
        else System.out.println("Failed."+ok);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockFarmland.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        return cw.toByteArray();
    }
    
    private byte[] transformBlockAnvil(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockAnvil.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching BlockAnvil.class ..............");
        boolean ok = false, ok2 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("a") && m.desc.equals("(Lsq;)V") )
            {
            	m.instructions.clear();
            	m.localVariables.clear();
            	m.instructions.insert(new InsnNode(RETURN));
            	ok = true;
            }
        	else if (m.name.equals("a_") && m.desc.equals("(Labv;IIII)V") )
            {
            	m.instructions.clear();
            	m.localVariables.clear();
            	m.instructions.insert(new InsnNode(RETURN));
            	ok2 = true;
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2);

        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockAnvil.mod.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        return cw.toByteArray();
    }

    private byte[] transformBlockDragonEgg(byte[] bytes)
    {
    	/*try
	    {
	     	FileOutputStream fos = new FileOutputStream("d:/BlockDragonEgg.orig.class");
	     	fos.write(bytes);
			fos.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}*/
    	
    	System.out.print("[BlockPhysics] Patching BlockDragonEgg.class ..........");
        boolean ok = false, ok2 = false, ok3 = false, ok4 = false;
       
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("a") && m.desc.equals("(Labv;IIII)V") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 2));
            	toInject.add(new VarInsnNode(ILOAD, 3));
            	toInject.add(new VarInsnNode(ILOAD, 4));
            	toInject.add(new VarInsnNode(ALOAD, 0));
            	toInject.add(new FieldInsnNode(GETFIELD, "any", "cF", "I"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "onNeighborBlockChange", "(Labv;IIII)V"));
                toInject.add(new InsnNode(RETURN));
                
                m.instructions.clear();
                m.localVariables.clear();
                m.instructions.add(toInject);
                ok = true;
            	
            }
        	else if (m.name.equals("a") && m.desc.equals("(Labv;IIILjava/util/Random;)V") )
            {
            	m.instructions.clear();
            	m.localVariables.clear();
            	m.instructions.insert(new InsnNode(RETURN));
            	ok2 = true;
            }
        	else if (m.name.equals("k") && m.desc.equals("(Labv;III)V") )
            {
            	m.instructions.clear();
            	m.localVariables.clear();
            	m.instructions.insert(new InsnNode(RETURN));
            	ok3 = true;
            }
        	else if (m.name.equals("m") && m.desc.equals("(Labv;III)V") )
            {
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 6));
            	toInject.add(new VarInsnNode(ILOAD, 7));
            	toInject.add(new VarInsnNode(ILOAD, 8));
            	toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "notifyMove", "(Labv;III)V"));

            	for (int index = 0; index < m.instructions.size(); index++)
                {
        			if (m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN  && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abv") && ((MethodInsnNode) m.instructions.get(index)).name.equals("i") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(III)Z"))
                    {
        				
        				if ( m.instructions.get(index+1).getOpcode() == POP)
        				{
        					m.instructions.insert(m.instructions.get(index+1),toInject);
        				}
        				else m.instructions.insert(m.instructions.get(index),toInject);
            			
        				ok4 = true;
        				break;
            		}
                }
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        if (ok && ok2 && ok3 & ok4) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok3+ok4);
        
        /*try
        {
        	FileOutputStream fos = new FileOutputStream("d:/BlockDragonEgg.class");
        	fos.write(cw.toByteArray());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        return cw.toByteArray();
    }

}
