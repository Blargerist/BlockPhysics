package blargerist.pie.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.ASMHelper;
import blargerist.pie.blockphysics.BlockPhysics;
import blargerist.pie.blockphysics.ModInfo;
import blargerist.pie.blockphysics.asm.IClassTransformerModule;

public class ModuleExplosion implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.world.Explosion"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformExplosionClass";
	}

	@Override
	public boolean canBeDisabled()
	{
		return true;
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		
		ClassNode classNode = ASMHelper.readClassFromBytes(bytes);

		if (transformedName.equals("net.minecraft.world.Explosion"))
		{
			createImpact(classNode);
			verifyFieldAdded(classNode, "impact", "Z");
			
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DDDF)V");
			if (methodNode != null)
			{
				transformInit(methodNode);
			}
			else
				throw new RuntimeException("Could not find <init> method in " + classNode.name);
			
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_77278_a", "()V");
			if (methodNode != null)
			{
				transformDoExplosionA(methodNode);
			}
			else
				throw new RuntimeException("Could not find doExplosionA method in " + classNode.name);
			
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_77279_a", "(Z)V");
			if (methodNode != null)
			{
				transformDoExplosionB(methodNode);
			}
			else
				throw new RuntimeException("Could not find doExplosionB method in " + classNode.name);

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
	
	private void createImpact(ClassNode classNode)
	{
		FieldVisitor fv;
        fv = classNode.visitField(ACC_PUBLIC, "impact", "Z", null, null);
        fv.visitEnd();
	}

	private void transformInit(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new InsnNode(ICONST_0));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/world/Explosion", "impact", "Z"));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	private void transformDoExplosionA(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findFirstInstruction(method);

		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/Entity", "field_70170_p", "Lnet/minecraft/world/World;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKESTATIC, "blargerist/pie/blockphysics/util/Explosions", "doExplosionA", "(Lnet/minecraft/world/World;Lnet/minecraft/world/Explosion;)V", false));
		toInject.add(new InsnNode(RETURN));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	private void transformDoExplosionB(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findFirstInstruction(method);

		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/Entity", "field_70170_p", "Lnet/minecraft/world/World;"));
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ILOAD, 1));
		toInject.add(new MethodInsnNode(INVOKESTATIC, "blargerist/pie/blockphysics/util/Explosions", "doExplosionB", "(Lnet/minecraft/world/World;Lnet/minecraft/world/Explosion;Z)V", false));
		toInject.add(new InsnNode(RETURN));
		
		method.instructions.insertBefore(target, toInject);
	}
}