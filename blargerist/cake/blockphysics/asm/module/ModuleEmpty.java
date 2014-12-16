package blargerist.cake.blockphysics.asm.module;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.tree.*;

import squeek.asmhelper.ASMHelper;
import squeek.asmhelper.ObfHelper;
import blargerist.cake.blockphysics.ModInfo;
import blargerist.cake.blockphysics.asm.IClassTransformerModule;

public class ModuleEmpty implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		""
		};
	}

	@Override
	public String getModuleName()
	{
		return "";
	}

	@Override
	public boolean canBeDisabled()
	{
		return true;
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		boolean isObfuscated = ObfHelper.isObfuscated();
		ClassNode classNode = ASMHelper.readClassFromBytes(bytes);

		if (transformedName.equals(""))
		{
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? "" : "", "");
			if (methodNode != null)
			{
				doStuff(methodNode);
			}
			else
				throw new RuntimeException("Could not find XXX method in XXX");

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

	public void doStuff(MethodNode method)
	{
		AbstractInsnNode firstNode = ASMHelper.findFirstInstruction(method);

		InsnList toInject = new InsnList();
	}
}