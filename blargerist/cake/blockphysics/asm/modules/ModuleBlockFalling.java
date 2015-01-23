package blargerist.cake.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.ASMHelper;
import blargerist.cake.blockphysics.BlockPhysics;
import blargerist.cake.blockphysics.ModInfo;
import blargerist.cake.blockphysics.asm.IClassTransformerModule;
import blargerist.cake.blockphysics.asm.RemoveMethodAdapter;

public class ModuleBlockFalling implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.block.BlockFalling",
		"net.minecraft.world.WorldServer",
		"net.minecraft.block.Block",
		"net.minecraft.entity.item.EntityFallingBlock",
		"net.minecraft.entity.Entity"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformBlockFallingClass";
	}

	@Override
	public boolean canBeDisabled()
	{
		return false;
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		ClassNode classNode = ASMHelper.readClassFromBytes(bytes);
		
		if (transformedName.equals("net.minecraft.block.BlockFalling"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(cr, 0);
			ClassVisitor cv = new RemoveMethodAdapter(ASM4, cw, "func_149674_a", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V");
			cr.accept(cv, 0);
			return cw.toByteArray();
		}
		else if (transformedName.equals("net.minecraft.world.WorldServer"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_72955_a", "(Z)Z");
            if (methodNode != null)
            {
                injectTickUpdatesHook(methodNode);
            }
            else
                throw new RuntimeException("Could not find tickUpdates method in " + transformedName);
            
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_147454_a", "(IIILnet/minecraft/block/Block;II)V");
            if (methodNode != null)
            {
                injectScheduleBlockUpdateWithPriorityHook(methodNode);
            }
            else
                throw new RuntimeException("Could not find scheduleBlockUpdateWithPriority method in " + transformedName);
            
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_147456_g", "()V");
            if (methodNode != null)
            {
            	injectfunc_147456_gHook(methodNode);
            }
            else
                throw new RuntimeException("Could not find func_147456_g method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		else if (transformedName.equals("net.minecraft.block.Block"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_149695_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V");
            if (methodNode != null)
            {
                transformOnNeighborBlockChange(methodNode);
            }
            else
                throw new RuntimeException("Could not find onNeighborBlockChange method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
		}
		else if (transformedName.equals("net.minecraft.entity.item.EntityFallingBlock"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_70071_h_", "()V");
            if (methodNode != null)
            {
                insertEntityFallingBlockOnUpdateHook(methodNode);
            }
            else
                throw new RuntimeException("Could not find onUpdate method in " + transformedName);
			
            methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;)V");
			if (methodNode != null)
			{
				insertFrustumNoClip1(methodNode);
			}
			else
				throw new RuntimeException("Could not find <init> method in " + transformedName);
			
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;DDDLnet/minecraft/block/Block;I)V");
			if (methodNode != null)
			{
				insertFrustumNoClip2(methodNode);
			}
			else
				throw new RuntimeException("Could not find <init> method in " + transformedName);
			
			return ASMHelper.writeClassToBytes(classNode);
		}
		else if (transformedName.equals("net.minecraft.entity.Entity"))
		{
			ModInfo.Log.info("Transforming class: " + transformedName);
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_70091_d", "(DDD)V");
			if (methodNode != null)
			{
				injectIfBlockFalling(methodNode);
			}
			else
				throw new RuntimeException("Could not find moveEntity method in " + transformedName);
			
			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}

	public void verifyMethodAdded(ClassNode classNode, String name, String desc)
	{
		MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, name, desc);
		if (methodNode != null)
		{
			ModInfo.Log.info("Successfully added method: " + methodNode.name + methodNode.desc + " in " + classNode.name);
		} else
			throw new RuntimeException("Could not create method: " + name + desc + " in " + classNode.name);
	}
	
	public void verifyFieldAdded(ClassNode classNode, String name, String desc)
	{
		FieldNode fieldNode = ASMHelper.findFieldNodeOfClass(classNode, name, desc);
		if (fieldNode != null)
		{
			ModInfo.Log.info("Successfully added field: " + fieldNode.name + fieldNode.desc + " in " + classNode.name);
		} else
			throw new RuntimeException("Could not create field: " + name + desc + " in " + classNode.name);
	}
	
	public void injectTickUpdatesHook(MethodNode method)
	{
		InsnList toFind = new InsnList();
		toFind.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/Block",  "func_149674_a", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V", false));
		AbstractInsnNode target = ASMHelper.find(method.instructions, toFind);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.tickUpdates");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 4));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/NextTickListEntry", "field_77183_a", "I"));
		toInject.add(new VarInsnNode(ALOAD, 4));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/NextTickListEntry", "field_77181_b", "I"));
		toInject.add(new VarInsnNode(ALOAD, 4));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/NextTickListEntry", "field_77182_c", "I"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/WorldServer", "field_73012_v", "Ljava/util/Random;"));
		toInject.add(new MethodInsnNode(INVOKESTATIC, "blargerist/cake/blockphysics/events/BPEventHandler", "onUpdateBlock", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V", false));
		
		method.instructions.insert(target, toInject);
	}
	
	public void injectScheduleBlockUpdateWithPriorityHook(MethodNode method)
	{
		InsnList toFind = new InsnList();
		toFind.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/Block",  "func_149674_a", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V", false));
		AbstractInsnNode target = ASMHelper.find(method.instructions, toFind);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.ScheduleBlockUpdateWithPriority");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ALOAD, 7));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/NextTickListEntry", "field_77183_a", "I"));
		toInject.add(new VarInsnNode(ALOAD, 7));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/NextTickListEntry", "field_77181_b", "I"));
		toInject.add(new VarInsnNode(ALOAD, 7));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/NextTickListEntry", "field_77182_c", "I"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/WorldServer", "field_73012_v", "Ljava/util/Random;"));
		toInject.add(new MethodInsnNode(INVOKESTATIC, "blargerist/cake/blockphysics/events/BPEventHandler", "onUpdateBlock", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V", false));
		
		method.instructions.insert(target, toInject);
	}
	
	public void injectfunc_147456_gHook(MethodNode method)
	{
		InsnList toFind = new InsnList();
		toFind.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/Block",  "func_149674_a", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V", false));
		AbstractInsnNode target = ASMHelper.find(method.instructions, toFind);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in WorldServer.func_147456_g");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ILOAD, 16));
		toInject.add(new VarInsnNode(ILOAD, 5));
		toInject.add(new InsnNode(IADD));
		toInject.add(new VarInsnNode(ILOAD, 18));
		toInject.add(new VarInsnNode(ALOAD, 13));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "func_76662_d", "()I", false));
		toInject.add(new InsnNode(IADD));
		toInject.add(new VarInsnNode(ILOAD, 17));
		toInject.add(new VarInsnNode(ILOAD, 6));
		toInject.add(new InsnNode(IADD));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/WorldServer", "field_73012_v", "Ljava/util/Random;"));
		toInject.add(new MethodInsnNode(INVOKESTATIC, "blargerist/cake/blockphysics/events/BPEventHandler", "onUpdateBlock", "(Lnet/minecraft/world/World;IIILjava/util/Random;)V", false));
		
		method.instructions.insert(target, toInject);
	}
	
	public void transformOnNeighborBlockChange(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in Block.onNeighborBlockChange");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ILOAD, 4));
		toInject.add(new VarInsnNode(ALOAD, 5));
		toInject.add(new MethodInsnNode(INVOKESTATIC, "blargerist/cake/blockphysics/events/BPEventHandler", "onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V", false));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void insertEntityFallingBlockOnUpdateHook(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findFirstInstruction(method);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.onUpdate");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKESTATIC, "blargerist/cake/blockphysics/util/EntityMove", "onUpdate", "(Lnet/minecraft/entity/item/EntityFallingBlock;)V", false));
		toInject.add(new InsnNode(RETURN));
		
		method.instructions.insertBefore(target, toInject);
	}

	public void insertFrustumNoClip1(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.<init>(World)");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70158_ak", "Z"));
		//toInject.add(new VarInsnNode(ALOAD, 0));
		//toInject.add(new InsnNode(ICONST_1));
		//toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70145_X", "Z"));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void insertFrustumNoClip2(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Unexpected instruction pattern in EntityFallingBlock.<init>(World;DDDBlock;I)");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70158_ak", "Z"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_1));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70145_X", "Z"));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	public void injectIfBlockFalling(MethodNode method)
	{
		InsnList toFind = new InsnList();
		toFind.add(new VarInsnNode(ALOAD, 0));
		toFind.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/Entity", "func_145775_I", "()V", false));
		
		AbstractInsnNode start = ASMHelper.find(method.instructions, toFind);
		AbstractInsnNode end = ASMHelper.move(start, 1);
		
		if (start == null || end == null)
			throw new RuntimeException("Unexpected instructions pattern in Entity.moveEntity");
		
		InsnList firstInject = new InsnList();
		firstInject.add(new VarInsnNode(ALOAD, 0));
		firstInject.add(new TypeInsnNode(INSTANCEOF, "net/minecraft/entity/item/EntityFallingBlock"));
		LabelNode label1 = new LabelNode();
		firstInject.add(new JumpInsnNode(IFEQ, label1));
		firstInject.add(new VarInsnNode(ALOAD, 0));
		firstInject.add(new MethodInsnNode(INVOKESTATIC, "blargerist/cake/blockphysics/util/EntityMove", "checkEntityBlockCollisions", "(Lnet/minecraft/entity/Entity;)V", false));
		LabelNode label2 = new LabelNode();
		firstInject.add(new JumpInsnNode(GOTO, label2));
		firstInject.add(label1);

		InsnList secondInject = new InsnList();
		secondInject.add(label2);
		
		method.instructions.insertBefore(start, firstInject);
		method.instructions.insert(end, secondInject);
	}
}