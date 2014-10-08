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

import java.util.Iterator;

import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.network.packet.Packet23VehicleSpawn;

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
    	else if (name.equals("blockphysics.BlockPhysics")) //WHY!?!?!?!?
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
            			/*
            			 * Equivalent to injecting
            			 * this.movingBlockTileEntityData = null;
		 				 * this.bpmeta = 0;
		 				 * right before return statement
            			 */
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
            			
            			/*
            			 * Equivalent to injecting
            			 * blockphysics.BlockPhysics.setBlockBPdata(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.bpmeta);
            			 * if (this.movingBlockTileEntityData != null)
            			 * {
            			 * 		Block block = Block.blocksList[this.storedBlockID];
            			 * 		TileEntity tileEntity = Block.createTileEntity(this.worldObj, this.storedMetaData);
            			 * 		tileEntity.readFromNBT(this.movingBlockTileEntityData);
            			 * 		World.setBlockTileEntity(xCoord, yCoord, zCoord, tileEntity);
            			 * }
            			 * after this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID);
            			 */
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
            			/*
            			 * Equivalent to injecting
            			 * if (par1NBTTagCompound.hasKey("BPData")
            			 * {
            			 * 		this.bpmeta = par1NBTTagCompound.getByte("BPData");
            			 * }
            			 * if (part1NBTTagCompound.hasKey("TileEntityData")
            			 * {
            			 * 		this.movingBlockTileEntityData = par1NBTTagCompound.getCompoundTag("TileEntityData");
            			 * }
            			 * before end return statement
            			 */
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
            			/* Equivalent to injecting
            			 * par1NBTTagCompound.setByte("BPData", (byte)this.bpmeta);
            			 * if (this.movingBlockTileEntityData != null)
            			 * {
            			 * 		par1NBTTagCompound.setCompoundTag("TileEntityData", this.movingBlockTileEntityData);
            			 * }
            			 * before end return statement
            			 */
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
            			/*
            			 * Equivalent to injecting
            			 * blockphysics.BlockPhysics.setBlockBPdata(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.bpmeta);
            			 * if (this.movingBlockTileEntityData != null)
            			 * {
            			 * 		Block block = Block.blocksList[this.storedBlockID];
            			 * 		TileEntity tileEntity = block.createTileEntity(this.worldObj, this.storedMetaData);
            			 * 		tileEntity.readFromNBT(this.movingBlockTileEntityData);
            			 * 		this.worldObj.setBlockTileEntity(this.xCoord, this.yCoord, this.zCoord, tileEntity);
            			 * }
            			 * after this.worldObj.notifyBlockOfNeighborChange
            			 */
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
       
        return cw.toByteArray();
	}

	private byte[] transformBlockFurnace(byte[] bytes)
	{
		
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
            			/*
            			 * Equivalent to removing entire method call
            			 * this.setDefaultDirection(par1World, par2, par3, par4);
            			 */
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
       
        return cw.toByteArray();
	}

	private byte[] transformBlockDispenser(byte[] bytes)
	{

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
            			/*
            			 * Equivalent to removing entire method call
            			 * this.setDispenserDefaultDirection(par1World, par2, par3, par4);
            			 */
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
       
        return cw.toByteArray();
	}

	private byte[] transformBlockChest(byte[] bytes)
	{
		
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
            			/*
            			 * Equivalent to removing entire method call
            			 * this.unifyAdjacentChests(par1World, par2, par3, par4);
            			 */
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
       
        return cw.toByteArray();
	}

	private byte[] transformAnvilChunkLoader(byte[] bytes)
	{
    	
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
                    			/*
                    			 * Gets the NBTTagCompound created on line 250 and sets it as var1
                    			 */
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
                    			/*
                    			 * Gets variable extendedblockstorage from line 259
                    			 * and sets it as var2
                    			 */
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
            			/*
            			 * Equivalent to injecting 
            			 * nbttagcompound1.setByteArray("BPData", extendedblockstorage.getBPdataArray());
            			 * before nbttaglist.appendTag(nbttagcompound1); on line 271
            			 */
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
        		/*
        		 * method = net/minecraft/world/chunk/storage/AnvilChunkLoader/readChunkFromNBT(World, NBTTagCompound)Chunk
        		 */
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
                    			/*
                    			 * Gets nbttagcompound1 from line 373
                    			 * and sets it as var1
                    			 */
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
                    			/*
                    			 * Gets extendedblockstorage from line 375
                    			 * and sets it as var2
                    			 */
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
            			/*
            			 * Equivalent to injecting
            			 * if (nbttagcompound1.hasKey("BPData"))
            			 * {
            			 * 		extendedblockstorage.setBPdataArray(nbttagcompound1.getByteArray("BPData"));
            			 * }
            			 * else
            			 * {
            			 * 		extendedblockstorage.setBPdataArray(new byte[4096]);
            			 * }
            			 * before extendedblockstorage.removeInvalidBlocks(); at line 391
            			 */
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
        
        return cw.toByteArray();
	}

	private byte[] transformExtendedBlockStorage(byte[] bytes) {
		
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
            			/*
            			 * Equivalent to injecting
            			 * this.blockBPdataArray = new byte[4096];
            			 * before end return statement
            			 */
            			
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
        
        /*
         * Equivalent to adding method:
         * public int getBlockBPdata(int par1, int par2, int par3)
         * {
         * 		return this.blockBPdataArray[par2 * 256 + par1 * 16 + par3];
         * }
         */
        
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
        
        /*
         * Equivalent to adding method:
         * public void setBlockBPdata(int par1, int par2, int par3, int par4)
         * {
         * 		this.blockBPdataArray[par2 * 256 + par1 * 16 + par3] = (byte)par4;
         * }
         */
        
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
		
		/*
		 * Equivalent to adding method:
		 * public byte[] getBPdataArray()
		 * {
		 * 		return this.blockBPdataArray;
		 * }
		 */
		
		mv = cw.visitMethod(ACC_PUBLIC, "getBPdataArray", "()[B", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "adr", "blockBPdataArray", "[B");
		mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
		
		/*
		 * Equivalent to adding method:
		 * public void setBPdataArray(byte[] bytes)
		 * {
		 * 		this.blockBPdataArray = bytes;
		 * }
		 */
		
		mv = cw.visitMethod(ACC_PUBLIC, "setBPdataArray", "([B)V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(PUTFIELD, "adr", "blockBPdataArray", "[B");
		mv.visitInsn(RETURN);
		mv.visitMaxs(2, 2);
		mv.visitEnd();

        FieldVisitor fv;
        
        /*
         * Equivlent to adding field
         * private byte[] blockBPdataArray
         */
    	
        fv = cw.visitField(ACC_PRIVATE, "blockBPdataArray", "[B", null, null);
        fv.visitEnd();
        
    	cw.visitEnd();

        if (ok ) System.out.println("OK");
        else System.out.println("Failed."+ok);
       
        return cw.toByteArray();
	}

	private byte[] transformChunk(byte[] bytes) {
		
		System.out.print("[BlockPhysics] Patching Chunk.class ...................");
       
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        
        /*
         * Equivalent to adding method:
         * public int getBlockBPdata(int par1, int par2, int par3)
         * {
         * 		if (par2 >> 4 >= this.storageArrays.length)
		 * 			{
		 * 				return 0;
		 * 			}
		 * 
		 * 		ExtendedBlockArray blockStorage = this.storageArrays[par2 >> 4];
		 * 
		 * 		if (blockStorage != null)
		 * 		{
		 * 			return blockStorage.getBlockBPdata(par1, par2 & 15, par3);
		 * 		}
		 * 		else
		 * 		{
         * 			return 0;
         * 		}
         * }
         */
        
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

		/*
		 * Equivalent to adding method:
		 * public boolean setBlockBPdata(int par1, int par2, int par3, int par4)
		 * {
		 *	 ExtendedBlockStorage blockStorage = this.storageArrays[par2 >> 4];
		 *	
		 *	 if (blockStorage == null
		 *	 {
		 *		 return false;
		 *	 }
		 *	 int blockBPdata = blockStorage.getBlockBPdata(par1, par2 & 15, par3);
		 *	 
		 *	 if (blockBPdata == par4)
		 *	 {
		 *		 return false;
		 *	 }
		 *	 this.isModified = true;
		 *	 blockStorage.setBlockBPdata(par1, par2 & 15, par3, par4);
		 * 	 return true;
		 * }
		 */
		
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
       
        return cw.toByteArray();       
	}

	private byte[] transformBlockPhysics(byte[] bytes)
    {
    	//TODO
		//Add Bukkit/Cauldron support
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
       
        return cw.toByteArray();
    }

    private byte[] transformEntity(byte[] bytes)
    {
    	
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
        				/*
        				 * Equivalent to changing all 0.001D to 0.07D
        				 * into doBlockCollisions()V
        				 */
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
       
        return cw.toByteArray();
    }
    
    private byte[] transformEntityMinecart(byte[] bytes)
    {
    	
    	System.out.print("[BlockPhysics] Patching EntityMinecart.class ..........");
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);        
        
        /*
         * Equivalent to adding return statement to the start of
         * EntityMinecart.setInWeb()
         */
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "al", "()V", null, null);
        mv.visitCode();
        mv.visitInsn(RETURN);
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
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);        
        
        /*
         * Equivalent to adding return statement to the start of
         * EntityXPOrb.setInWeb()
         */
        
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "al", "()V", null, null);
        mv.visitCode();
        mv.visitInsn(RETURN);
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
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);        
        
        /*
         * Equivalent to adding return statement to the start of
         * EntityBoat.setInWeb()
         */
        
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "al", "()V", null, null);
        mv.visitCode();
        mv.visitInsn(RETURN);
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
                
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	if (m.name.equals("a") && m.desc.equals("(Labv;IIILnm;)V") )
        	{
        		/*
        		 * Equivalent to clearing all code within the method onEntityCollidedWithBlock(World;IIIEntity;)V
        		 * and replacing with:
        		 * blockphysics.BlockPhysics.onEntityCollidedWithBlock(world, par2, par3, par4, this.blockID, entity);
        		 */
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
        
       
        return cw.toByteArray();
    }
    
    private byte[] transformWorld(byte[] bytes)
    {
    	
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
            			
            			/*
            			 * Equivalent to injecting:
            			 * this.moveTickList = new blockphysics.BTickList();
            			 * this.pistonMoveBlocks = new java.util.HashSet();
            			 * this.explosionQueue = new blockphysics.ExplosionQueue();
            			 * Before final return statement
            			 */
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
        		{ //^ start from bottom
        			if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abq") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Z)V"))
        			{
        				//^if Explosion.doExplosionB(Z)V

        				while ( !(m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abq") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("()V")) )
        				{
        					/*
        					 * Remove all instructions starting from and including explosion.doExplosionB(true);
        					 * and moving up until explosion.doExplosionA();
        					 */
        					m.instructions.remove(m.instructions.get(index));
        					index--;
        				}
        				/*
        				 * Remove explosion.doExplosionA(); and the ALOAD 11 above it
        				 */
        				m.instructions.remove(m.instructions.get(index-1));
        				m.instructions.remove(m.instructions.get(index-1));
        				
        				/*
        				 * Equivalent to injecting
        				 * this.explosionQueue.add(explosion);
        				 * insert before 1 index above where explosion.doExplosionA()'s ALOAD11 was
        				 */
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
    	
    	/*
    	 * Equivalent to adding 3 fields:
    	 * public blockphysics.BTickList moveTickList;
    	 * public java.util.HashSet<String> pistonMoveBlocks;
    	 * public blockphysics.ExplosionQueue explosionQueue;
    	 */
    	fv = cw.visitField(ACC_PUBLIC, "moveTickList", "Lblockphysics/BTickList;", null, null);
    	fv.visitEnd();
    	
    	fv = cw.visitField(ACC_PUBLIC, "pistonMoveBlocks", "Ljava/util/HashSet;", "Ljava/util/HashSet<Ljava/lang/String;>;", null);
    	fv.visitEnd();
    	
    	fv = cw.visitField(ACC_PUBLIC, "explosionQueue", "Lblockphysics/ExplosionQueue;", null, null);
    	fv.visitEnd();
   	
    	cw.visitEnd();
    	
        if ( ok && ok2 ) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2);
        
       
        return cw.toByteArray();
    }
    
    private byte[] transformWorldServer(byte[] bytes)
    {
    	
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
            			/*
            			 * Equivalent to injecting
            			 * blockphysics.BlockPhysics.tickBlockRandomMove(this);
            			 * this.moveTickList(this);
            			 * this.pistonMoveBlocks.clear();
            			 * this.explosionQueue.doNextExplosion();
            			 * before end return statement
            			 */
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
        				//^if Explosion.doExplosionB(Z)V

        				while ( !(m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abq") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("()V")) )
        				{
        					/*
        					 * Remove all instructions starting from and including explosion.doExplosionB(true);
        					 * and moving up until explosion.doExplosionA();
        					 */
        					m.instructions.remove(m.instructions.get(index));
        					index--;
        				}
        				/*
        				 * Remove explosion.doExplosionA(); and the ALOAD 11 above it
        				 */
        				m.instructions.remove(m.instructions.get(index-1));
        				m.instructions.remove(m.instructions.get(index-1));
        				
        				/*
        				 * Equivalent to injecting
        				 * this.explosionQueue.add(explosion);
        				 * insert before 1 index above where explosion.doExplosionA()'s ALOAD11 was
        				 */
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
        
       
        return cw.toByteArray();
    }
   
    private byte[] transformExplosion(byte[] bytes)
    {
    	
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
        		/*
        		 * Equivalent to injecting
        		 * this.impact = false;
        		 * before end RETURN statement
        		 */
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
        		/* Method = doExplosionA()V
        		 * Equivalent to removing all instructions and variables then injecting
        		 * blockphysics.BlockPhysics.doExplosionA(this.worldObj, this);
        		 */
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
        		/* Method = doExplosionB(Z)V
        		 * Equivalent to removing all instructions and variables then injecting
        		 * blockphysics.BlockPhysics.doExplosionB(this.worldObj, this, par1);
        		 */
        		
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
        
        /*
         * Equivalent to injecting field
         * public boolean impact;
         */
        FieldVisitor fv;
        fv = cw.visitField(ACC_PUBLIC, "impact", "Z", null, null);
        fv.visitEnd();
        
        cw.visitEnd();
        
        if (ok && ok2 && ok3) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok3);
        
       
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
        		/* Method = MinecraftServer.run()V
        		 * Bukkit shit that I don't care about.
        		 */
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
	        			/*
	        			 * Checks if method contains a call to MinecraftServer.tick ()V line 476
	        			 */
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
								/*
								 * Equivalent to injecting
								 * blockphysics.BlockPhysics.setSkipMove(l);
								 * before instance of index j (line 471)
								 */
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

       
        return cw.toByteArray();
    }
    
    private byte[] transformEntityTrackerEntry(byte[] bytes)
    {
    	
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
        	{ //method = getPacketForThisEntity ()Packet;
        		for (int index = 0; index < m.instructions.size(); index++)
                {
        			if ( m.instructions.get(index).getType() == AbstractInsnNode.TYPE_INSN && m.instructions.get(index).getOpcode() == INSTANCEOF && ((TypeInsnNode) m.instructions.get(index)).desc.equals("sq"))
                    { //^if instance of EntityFallingSand
    					while ( m.instructions.get(index).getOpcode() != IFEQ )
    					{
    						index++;
    					}
    					index++;
    					
    					while ( m.instructions.get(index).getOpcode() != ARETURN )
    					{
    						m.instructions.remove(m.instructions.get(index));
    						/*
    						 * Deletes everything in quotations
    						 *                 else if (this.myEntity instanceof EntityFallingSand)
                			 * {
                    		 * """EntityFallingSand entityfallingsand = (EntityFallingSand)this.myEntity;"""
                    		 * return """new Packet23VehicleSpawn(this.myEntity, 70, entityfallingsand.blockID | entityfallingsand.metadata << 16);"""
                    		 * So everything except ARETURN after IFEQ
    						 */
    					}
    					
    					/*
    					 * Equivalent to injecting
    					 * blockphysics.BlockPhysics.spawnFallingSandPacket((EntityFallingSand)this.myEntity);
    					 * before instruction after IFEQ
    					 */
    					
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
    	
       
        return cw.toByteArray();
    }
        	
    private byte[] transformEntityTracker(byte[] bytes)
    {
    	
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
        	{//Method = <init>(WorldServer)V
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			/*
            			 * Equivalent to injecting
            			 * this.movingblocks = 0;
            			 * before end return statement
            			 */
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
        	else if (m.name.equals("a") && m.desc.equals("(Lnm;)V") )//TODO doing this makes no sense
        	{//Method = addEntityToTracker(Entity)V
        		for (int index = 0; index < m.instructions.size(); index++)
                {
        			if ( m.instructions.get(index).getType() == AbstractInsnNode.TYPE_INSN && m.instructions.get(index).getOpcode() == INSTANCEOF && ((TypeInsnNode) m.instructions.get(index)).desc.equals("tb"))
                    {//InstanceOf EntityTNTPrimed
        				m.instructions.remove(m.instructions.get(index-1));
    					/*
    					 * else if (par1Entity instanceof EntityTNTPrimed)
        				 * {
            			 * this.addEntityToTracker(par1Entity, 160, 10, true);
        				 * }
        				 * else if (par1Entity instanceof EntityFallingSand)
        				 * Removes both ALOAD par1Entity before "instanceof"s and whole line this.addEntityToTracker(par1Entity, 160, 10, true);
    					 */
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
                    {//instanceof EntityFallingSand
    					while ( m.instructions.get(index).getOpcode() != GOTO )
    					{
    						index++;
    						if (m.instructions.get(index).getType() == AbstractInsnNode.INT_INSN && m.instructions.get(index).getOpcode() == BIPUSH && ((IntInsnNode) m.instructions.get(index)).operand == 20)
    						{
    							/*
    							 * replace 20 with 40 on line 167
    							 * this.addEntityToTracker(par1Entity, 160, 20, true);
    							 */
    							m.instructions.set(m.instructions.get(index),new IntInsnNode(BIPUSH, 40));
    							ok3 = true;
    						}
    					}
    					/*
    					 * Equivalent to injecting
    					 * this.movingblocks += 1; or perhaps this.movingBlocks++;
    					 * before end bracket on line 168
    					 */
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
            {//Method = removeEntityFromAllTrackingPlayers(Entity)V
            	InsnList toInject = new InsnList();
            	/*
            	 * Equivalent to injecting
            	 * if (par1Entity instanceof EntityFallingSand)
            	 * {
            	 * 		this.movingblocks -= 1; or this.movingblocks--;
            	 * }
            	 * before end return statement
            	 */
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
        	
        /*
         * Equivalent to adding field
         * public int movingblocks;
         */
    	fv = cw.visitField(ACC_PUBLIC, "movingblocks", "I", null, null);
        fv.visitEnd();
        
        cw.visitEnd();

        if (ok && ok2 && ok3 && ok4) System.out.println("OK");
        else System.out.println("Failed."+ok+ok2+ok3+ok4);
        
       
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
		catch(ClassNotFoundException e) 
		{
  	     
  	    }

    	System.out.print("[BlockPhysics] Patching EntityTNTPrimed.class .........");
        boolean ok = false, ok2 = false, ok5 = false, ok6 = false, ok7 = false, ok8 = false, ok9 = false, ok10 = false;
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        /*
         * change superclass to EntityFallingSand
         */
        classNode.superName = "sq";
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("<init>") && m.desc.equals("(Labv;)V") )
        	{//Method = <init>(World)V
        		for (int index = 0; index < m.instructions.size(); index++)
                {
    				if (m.instructions.get(index).getOpcode() == INVOKESPECIAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("nm") && ((MethodInsnNode) m.instructions.get(index)).name.equals("<init>") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Labv;)V"))
                    {
    					/*
    					 * Replace invokespecial at line 18
    					 * super(par1World);
    					 * with
    					 * super(par1World); which is really EntityFallingSand<init>(world);
    					 */
    					m.instructions.set(m.instructions.get(index),new MethodInsnNode(INVOKESPECIAL, "sq", "<init>", "(Labv;)V"));
    					ok = true;
        				break;
            		}
                }
        	}
        	else if (m.name.equals("<init>") && m.desc.equals("(Labv;DDDLoe;)V") )
        	{// Method = <init>(World, double, double, double, EntityLivingBase)V
				InsnList toInject = new InsnList();
	 			
				/*
				 * Equivalent to clearing all instructions and local vars then injecting
				 * super(par1World, par2, par4, par6, 46, 0);
				 * this.fuse = 80;
				 * RETURN
				 *///TODO may want to add this.tntPlacedBy back in, depending on what it's used for
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
				{//BUKKIT CRAP
					toInject.add(new VarInsnNode(ALOAD, 0));
					toInject.add(new LdcInsnNode(new Float("4.0")));
					toInject.add(new FieldInsnNode(PUTFIELD, "tb", "yield", "F"));
					toInject.add(new VarInsnNode(ALOAD, 0));
					toInject.add(new InsnNode(ICONST_0));
					toInject.add(new FieldInsnNode(PUTFIELD, "tb", "isIncendiary", "Z"));
				}//BUKKIT CRAP^

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
        	{//Method = onUpdate ()V
        		/*
        		 * Equivalent to clearing all instructions/variables, and injecting
        		 * blockphysics.BlockPhysics.fallingSandUpdate(this.worldObj, this);
        		 */
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
            {//Method = writeEntityToNBT(NBTTagCompound)V
            	InsnList toInject = new InsnList();
            	
            	/*
            	 * Equivalent to injecting
            	 * super.writeEntityToNBT(par1NBTTagCompound);
            	 * before end return statement
            	 */
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
            {//Method = readEntityFromNBT(NBTTagCompound)V
            	InsnList toInject = new InsnList();
            	
            	/*
            	 * Equivalent to injecting
            	 * super.readEntityFromNBT(par1NBTTagCompound);
            	 * before end return statement
            	 */
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
            {//Method = explode()V
            	m.access = ACC_PUBLIC;//Set access to public
    			ok9 = true;
    			if (bukkit)
    			{//BUKKIT CRAP
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
    			}//BUKKIT CRAP^
    			else 
    			{
    				for (int index = 0; index < m.instructions.size(); index++ )
    				{//TODO I assume this may be to fix an error from the changing of the superClass, but I'm unsure	
    					if (m.instructions.get(index).getOpcode() == ACONST_NULL )
	    				{
    						/*
    						 * Changes first ACONST_NULL to: ALOAD, 0
    						 */
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
        	{//Method = <init> (World)V
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			/*
            			 * Equivalent to injecting
            			 * this.preventEntitySpawn = true;
            			 * this.setSize(0.996, 0.996);
            			 * this.yOffset = this.height/2.0f;
            			 * this.motionX = 0;
            			 * this.motionY = 0;
            			 * this.motionZ = 0;
            			 * this.accelerationX = 0;
            			 * this.accelerationY = -0.024525;
            			 * this.accelerationZ = 0;
            			 * this.slideDir = 0;
            			 * this.noClip = true;
            			 * this.entityCollisionReduction = 0.8;
            			 * this.dead = 4;
            			 * this.bpdata = 0;
            			 * before end return statement
            			 */
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
        			/*
        			 * Equivalent to changing line 60: this.setSize(0.98F, 0.98F);
        			 * to this.setSize(0.996f, 0.996f);
        			 */
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
            			/*
            			 * Equivalent to injecting
            			 * this.accelerationX = 0;
            			 * this.accelerationY = -0.024525;
            			 * this.accelerationZ = 0;
            			 * this.slideDir = 0;
            			 * this.noClip = true;
            			 * this.entityCollisionReduction = 0.8;
            			 * this.dead = 4;
            			 * this.bpdata = 0;
            			 * before end return statement
            			 */
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
        	{//Method = canBeCollidedWith()Z
        		/*
        		 * Equivalent to removing all instructions and variables then injecting
        		 * return false;
        		 */
        		InsnList toInject = new InsnList();
        		toInject.add(new InsnNode(ICONST_0));
        		toInject.add(new InsnNode(IRETURN));
        		
        		m.instructions.clear();
        		m.localVariables.clear();
        		m.instructions.add(toInject);
        		ok9 = true;
        	}
        	else if (m.name.equals("l_") && m.desc.equals("()V") )
        	{//method = onUpdate()V
        		/*
        		 * Equivalent to removing all instructions and variables then injecting
        		 * blockphysics.BlockPhysics.fallingSandUpdate(worldObj, this);
        		 * RETURN
        		 */
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
        	{//Method = fall(F)V
        		/*
        		 * Equivalent to removing all instructions and variables then injecting
        		 * RETURN
        		 */
        		m.instructions.clear();
        		m.localVariables.clear();
        		m.instructions.add(new InsnNode(RETURN));
        		ok4 = true;
        	}
        	else if (m.name.equals("b") && m.desc.equals("(Lbx;)V") )
        	{//Method = writeEntityToNBT(NBTTagCompound)V
        		for (int index = m.instructions.size() - 1; index >= 0; index--)
                {
            		if (m.instructions.get(index).getOpcode() == RETURN ) 
            		{
            			/*
            			 * Equivalent to injecting
            			 * par1NBTTagCompound.setTag("Acceleration", newDoubleNBTList(new double[3] {this.accelerationX, this.accelerationY, this.accelerationZ}));
            			 * par1NBTTagCompound.setByte("BPData", (byte)this.bpdata);
            			 * before end return statement
            			 */
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
        	{//Method = readEntityFromNBT(NBTTagCompound)V
        		
        		while( !(m.instructions.get(0).getType() == AbstractInsnNode.LDC_INSN && ((LdcInsnNode) m.instructions.get(0)).cst.equals("Data")) )
				{
        			/*
        			 * Remove everything above line 263 & the ALOAD 0 and ALOAD 1 for line 263
        			 */
					m.instructions.remove(m.instructions.get(0));
					ok6 = true;
				}
        		/*
        		 * Equivlalent to injecting
        		 * this.blockID = blockphysics.BlockPhysics.readFallingSandID(par1NBTTagCompound);
        		 * ALOAD 0
        		 * ALOAD 1
        		 * before LDC_INSN "Data"
        		 */
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
            			/*
            			 * Equivalent to injecting
            			 * if (par1NBTTagCompound.hasKey("Acceleration"))
            			 * {
            			 * 	Acceleration[] = par1NBTTagLCompound.getTagList("Acceleration");
            			 * 	this.accelerationX = Acceleration[0];
            			 * 	this.accelerationY = Acceleration[1];
            			 * 	this.accelerationZ = Acceleration[2];
            			 * 	break;
            			 * }else
            			 * {
            			 * 	this.accelerationX = 9.0;
            			 * 	this.accelerationY = 0.0;
            			 * 	this.accelerationZ = 0.0;
            			 * }
            			 * if (par1NBTTagCompound.hasKey("BPData")
            			 * {
            			 * 	this.bpdata = par1NBTTagCompound.getByte("BPData");
            			 * 	break;
            			 * }else
            			 * {
            			 * 	this.bpdata = 0;
            			 * }
            			 * before end return statement
            			 */
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
        	{//Method = canRenderOnFire()Z
        		/*
        		 * Equivalent to clearing all instructions and variables then injecting
        		 * return this.inWater();
        		 *///TODO Shouldn't this be return !this.inWater();? Shouldn't be on fire in water afterall....
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
        /*
         * Equivalent to adding fields
         * public double accelerationX;
         * public double accelerationY;
         * public double accelerationZ;
         * public int bpdata;
         * public byte slideDir;
         * public int media;
         * public byte dead;
         */

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
        
        /*
         * Equivalent to adding method
         * public AxisAllignedBB getBoundingBox()
         * {
         * 		return this.boundingBox;
         * }
         */
        mv = cw.visitMethod(ACC_PUBLIC, "D", "()Lasu;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "sq", "E", "Lasu;");
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        
        /*
         * Equivalent to adding method
         * public void setInWeb()
         * {
         * 		blank
         * }
         */
        mv = cw.visitMethod(ACC_PUBLIC, "al", "()V", null, null);
        mv.visitCode();
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 1);
        mv.visitEnd();
        
        /*
         * Equivalent to adding method
         * public void moveEntity(double par1, double par2, double par3)
         * {
         * 		blockphysics.BlockPhysics.moveEntity(this.worldObj, this, par1, par2, par3);
         * }
         */
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
        
        if (!ok8)//If it managed to edit canRenderOnFire()Z
        {
        	/*
        	 * Equivalent to adding method
        	 * public boolean canRenderOnFire()
        	 * {
        	 * 		return this.inWater();
        	 * }
        	 *///TODO As before, shouldnt this be the opposite return?
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
       
       
        return cw.toByteArray();
    }
    
    private byte[] transformRenderFallingSand(byte[] bytes)
    {
    	
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
        	{//Method = doRenderFallingSand(EntityFallingSand, double, double, double, float, float)V
        		InsnList toInject = new InsnList();
        		/*
        		 * Equivalent to injecting
        		 * if (blockphysics.BClient.cancelRender(par1EntityFallingSand))
        		 * {
        		 * 		return;
        		 * }
        		 * before first instruction
        		 */
	           	
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
    					/*
    					 * Find first mention of getfield EntityFallingSand.blockID and add +3 to index
    					 */
    					index = index + 3;
    					ok = true;
    					break;
            		}
    			}	
        		
				while (!( m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).name.equals("glPushMatrix")))
				{
					/*
					 * Remove entirety of line 36 if statement
					 */
					m.instructions.remove(m.instructions.get(index));
					ok2 = true;
				}
              
        		for (index = 0; index < m.instructions.size(); index++)
                {
    				if ( m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("bfo") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Laqw;Labv;IIII)V"))
                    {
    					/*
    					 * Find invokevirtual RenderBlocks.renderBlockSandFalling(Block, World, int, int, int, int)V
    					 * replace with: blockphysics.BlockPhysics.renderBlockSandFalling(RenderBlocks, Block, World, int, int, int, int)V
    					 */
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
        
        return cw.toByteArray();
    }
    
    private byte[] transformNetClientHandler(byte[] bytes)
    {
    	
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
        	{//Method = handleVehicleSpawn(Packet23VehicleSpawn)V
            	InsnList toInject = new InsnList();
            	/*
            	 * Equivalent to injecting
            	 * blockphysics.BlockPhysics.createFallingSand(this.worldClient, 2d, 4d, 6d, par1Packet23VehicleSpawn);
            	 * ASTORE 8 ^
            	 * par1Packet23VehicleSpawn.throwerEntityId = 1; TODO Why does it delete this, then readd it instantly? Could've simply replaced the ICONST_0 instead.
            	 * at line 461, after removing line 461 and 462
            	 */
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
                    {//Line 461, second last injection 
    					while( m.instructions.get(index).getType() != AbstractInsnNode.FIELD_INSN || m.instructions.get(index).getOpcode() != PUTFIELD || !((FieldInsnNode) m.instructions.get(index)).owner.equals("dc") || !((FieldInsnNode) m.instructions.get(index)).name.equals("k"))
        				{//PUTFIELD Packet23VehicleSpawn.throwerEntityId Line 462
    						index++;
        				}
    					
    					while( m.instructions.get(index).getType() != AbstractInsnNode.TYPE_INSN || !((TypeInsnNode) m.instructions.get(index)).desc.equals("sq"))
        				{
    						/*
    						 * Delete everything between(and including) above and first instruction line 461
    						 */
    						m.instructions.remove(m.instructions.get(index));
    						index--;
    						ok = true;
        				}
    					
    					if (ok) 
    					{
    						/*
    						 * Remove first instruction line 461 then inject
    						 */
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
        
       
        return cw.toByteArray();
    }
    
    private byte[] transformGuiSelectWorld(byte[] bytes)
    {
    	
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
        	{//Method = initGui()V
            	InsnList toInject = new InsnList();
            	/*
            	 * Equivalent to injecting
            	 * this.selected = false;
            	 * before end return statement
            	 */
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
            {//Method = selectWorld(int)V
        		
    			for (int index = 0; index < m.instructions.size(); index++)
                {
        			if (m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN  && ((MethodInsnNode) m.instructions.get(index)).owner.equals("ats") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Ljava/lang/String;Ljava/lang/String;Lacc;)V"))
                    {//Minecraft.launchIntegratedServer(String, String, WorldSettings)V line 226
        				/*
        				 * Eqiuivalent to injecting
        				 * blockphysics.BClient.loadWorld(this, s, s1);
        				 * then removing line 226
        				 */
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
       
        return cw.toByteArray();
    }
      
    
    private byte[] transformGuiCreateWorld(byte[] bytes)
    {
    	
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
            {//Method = initGui()V
            	InsnList toInject = new InsnList();
            	/*
            	 * Equivalent to injecting
            	 * this.createClicked = false;
            	 * before end return statement
            	 */
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
            {//Method = actionPerformed(GuiButton)V
        		for (int index = 0; index < m.instructions.size(); index++)
                {
        			if (m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN && ((MethodInsnNode) m.instructions.get(index)).owner.equals("ats") && ((MethodInsnNode) m.instructions.get(index)).name.equals("a") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(Ljava/lang/String;Ljava/lang/String;Lacc;)V"))
                    {//Minecraft.launchIntegratedServer(String, String, WorldSettings)V
        				m.instructions.remove(m.instructions.get(index));
        				/*
        				 * Remove instruction at index then injecting before
        				 * Minecraft.displayGuiScreen(new blockphysics.BGui(this.folderName, this.textboxWorldName.getText().trim(), worldsettings, true));
        				 */
        				InsnList toInject = new InsnList();
        				
        				toInject.add(new InsnNode(ICONST_1));
        				toInject.add(new MethodInsnNode(INVOKESPECIAL, "blockphysics/BGui", "<init>", "(Lawb;Ljava/lang/String;Ljava/lang/String;Lacc;Z)V"));
        				toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "ats", "a", "(Lawb;)V"));
        				
        				m.instructions.insertBefore(m.instructions.get(index),toInject);
        				
        				while( m.instructions.get(index).getOpcode() != GETFIELD || m.instructions.get(index).getType() != AbstractInsnNode.FIELD_INSN || !((FieldInsnNode) m.instructions.get(index)).owner.equals("auy") || !((FieldInsnNode) m.instructions.get(index)).name.equals("f") || !((FieldInsnNode) m.instructions.get(index)).desc.equals("Lats;"))
        				{//GETFIELD Minecraft GuiCreateWorld.mc line 241 2nd instruction
        					index--;
        				}
        				
        				toInject = new InsnList();
        				/*
        				 * Equivalent to injecting
        				 *TODO Don't really know yet....
        				 * after second instruction line 241
        				 */
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
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();     	
        	
        	if (m.name.equals("g") && m.desc.equals("(Labv;IIII)V") )
            {//Method = onBlockDestroyedByPlayer(World, int, int, int, int)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5, this.blockID);
        		 * before end return statement
        		 */
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
            {//Method = onNeighborBlockChange(World, int, int, int, int)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
        		 * before end return statement
        		 */
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
            {//Method = onEntityWalking(World, int, int, int, Entity)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
        		 * before end return statement
        		 */
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
            {//Method = onEntityCollidedWithBlock(World, int, int, int, Entity)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onEntityCollidedWithBlock(par1World, par2, par3, par4, this.blockID, par5Entity);
        		 * before end return statement
        		 */
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
            {//Method = onPostBlockPlaced(World, int, int, int, int)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onPostBlockPlaced(par1World, par2, par3, par4, this.blockID, par5);
        		 * before end return statement
        		 */
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
            {//Method = onFallenUpon(World, int, int, int, Entity, float)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
        		 * before end return statement
        		 */
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
        		/*
        		 * Equivalent to changing Block.blockFlammability field to public and static
        		 */
        		f.access = ACC_PUBLIC + ACC_STATIC;
        		ok8 = true;
        		break;
            }
        }
        
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw); 
        
        if (ok2 && ok3 && ok4 && ok5 && ok6 && ok7 && ok8) System.out.println("OK");
        else System.out.println("Failed."+ok2+ok3+ok4+ok5+ok6+ok7+ok8);

        return cw.toByteArray();
    }
    
    
    private byte[] transformBlockRailBase(byte[] bytes)
    {
    	
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
            {//Method = onNeighborBlockChange(World, int, int, int, int)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
        		 * before end return statement
        		 */
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
        
      
        return cw.toByteArray();
    }
    
    private byte[] transformBlockPistonBase(byte[] bytes)
    {
    	
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
            {//Method = onNeighborBlockChange(World, int, int, int, int)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
        		 * before end return statement
        		 */
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
            {//Method = updatePistonState(World, int, int, int)V
        		/*
        		 * Equivalent to clearing all instructions and variables then injecting
        		 * blockphysics.BlockPhysics.updatePistonState(par1World, par2, par3, par4, this, this.isSticky);
        		 * RETURN
        		 */
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
            {//Method = onBlockEventReceived(World, int, int, int, int, int)Z
        		/*
        		 * Equivalent to clearing all instructions and variables then injecting
        		 * blockphysics.BlockPhysics.onBlockPistonEventReceived(par1World, par2, par3, par4, par5, par6, this, this.isSticky);
        		 * IRETURN^
        		 */
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
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();

        	if (m.name.equals("a") && m.desc.equals("(Labv;IIII)V") )
            {//Method = onNeighborBlockChange(World, int, int, int, int)V
        		/*
        		 * Equivalent to clearing all instructions and variables then injecting
        		 * blockphysics.BlockPhysics.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
        		 * RETURN
        		 */
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
            {//Method = updateTick(World, int, int, int, Random)V
        		/*
        		 * Equivalent to clearing all instructions and variables then injecting
        		 * RETURN
        		 */
        		m.instructions.clear();
        		m.localVariables.clear();
            	m.instructions.insert(new InsnNode(RETURN));
            	ok2 = true;
            }
        	else if (m.name.equals("k") && m.desc.equals("(Labv;III)V") )
            {//Method = tryToFall(World, int, int, int)V
        		/*
        		 * Equivalent to clearing all instructions and variables, then injecting
        		 * RETURN
        		 */
        		m.instructions.clear();
        		m.localVariables.clear();
        		m.instructions.insert(new InsnNode(RETURN));
            	ok3 = true;
            }
            else if (m.name.equals("a_") && m.desc.equals("(Labv;III)Z") )
            {//Method = canFallBelow(World, int, int, int)Z
            	/*
            	 * Equivalent to clearing all instructions and variables then injecting
            	 * blockphysics.BlockPhysics.canMoveTo(par1World, par2, par3, par4, 0);
            	 * IRETURN^
            	 */
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
       
        return cw.toByteArray();
    }
    
    private byte[] transformBlockRedstoneLight(byte[] bytes)
    {
    	
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
            {//Method = onNeighborBlockChange(World, int, int, int, int)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
        		 * before end return statement
        		 */
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
        
        MethodNode m;
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
        	m = methods.next();
        	
        	if (m.name.equals("a") && m.desc.equals("(Labv;IIII)V") )
            {//Method = onNeighborBlockChange(World, int, int, int, int)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
        		 * before end return statement
        		 */
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
            {//Method = onBlockDestroyedByPlayer(World, int, int, int, int)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5, this.blockID);
        		 * before end return statement
        		 */
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
        
        
        return cw.toByteArray();
    }
    
    private byte[] transformBlockFarmland(byte[] bytes)
    {
    	
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
            {//Method = onFallenUpon(World, int, int, int, Entity, float)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
        		 * before end return statement
        		 */
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
        
        
        return cw.toByteArray();
    }
    
    private byte[] transformBlockAnvil(byte[] bytes)
    {
    	
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
            {//Method = onStartFalling(EntityFallingSand)V
        		/*
        		 * Equivalent to clearing all instructions and variables, then injecting
        		 * RETURN
        		 */
            	m.instructions.clear();
            	m.localVariables.clear();
            	m.instructions.insert(new InsnNode(RETURN));
            	ok = true;
            }
        	else if (m.name.equals("a_") && m.desc.equals("(Labv;IIII)V") )
            {//Method = onFinishFalling(World, int, int, int, int)V
        		/*
        		 * Equivalent to removing all instructions and variables then injecting
        		 * RETURN
        		 */
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
        
        return cw.toByteArray();
    }

    private byte[] transformBlockDragonEgg(byte[] bytes)
    {
    	
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
            {//Method = onNeighborBlockChange(World, int, int, int, int)V
        		/*
        		 * Equivalent to clearing all instructions and variables then injecting
        		 * blockphysics.BlockPhysics.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
        		 * RETURN
        		 */
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
            {//Method = updateTick(World, int, int, int, Random)V
        		/*
        		 * Equivalent to clearing all instructions and variables then injecting
        		 * RETURN
        		 */
            	m.instructions.clear();
            	m.localVariables.clear();
            	m.instructions.insert(new InsnNode(RETURN));
            	ok2 = true;
            }
        	else if (m.name.equals("k") && m.desc.equals("(Labv;III)V") )
            {//Method = fallIfPossible(World, int, int, int)V
        		/*
        		 * Equivalent to clearing all instructions and variables then injecting
        		 * RETURN
        		 */
            	m.instructions.clear();
            	m.localVariables.clear();
            	m.instructions.insert(new InsnNode(RETURN));
            	ok3 = true;
            }
        	else if (m.name.equals("m") && m.desc.equals("(Labv;III)V") )
            {//Method = teleportNearby(World, int, int, int)V
        		/*
        		 * Equivalent to injecting
        		 * blockphysics.BlockPhysics.notifyMove(par1World, i1, k1, j1);
        		 */
            	InsnList toInject = new InsnList();
            	toInject.add(new VarInsnNode(ALOAD, 1));
            	toInject.add(new VarInsnNode(ILOAD, 6));
            	toInject.add(new VarInsnNode(ILOAD, 7));
            	toInject.add(new VarInsnNode(ILOAD, 8));
            	toInject.add(new MethodInsnNode(INVOKESTATIC, "blockphysics/BlockPhysics", "notifyMove", "(Labv;III)V"));

            	for (int index = 0; index < m.instructions.size(); index++)
                {
        			if (m.instructions.get(index).getOpcode() == INVOKEVIRTUAL && m.instructions.get(index).getType() == AbstractInsnNode.METHOD_INSN  && ((MethodInsnNode) m.instructions.get(index)).owner.equals("abv") && ((MethodInsnNode) m.instructions.get(index)).name.equals("i") && ((MethodInsnNode) m.instructions.get(index)).desc.equals("(III)Z"))
                    {//World.setBlockToAir(int, int, int)Z
        				//INVOKEVIRTUAL on line 111
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
        
        
        return cw.toByteArray();
    }
}