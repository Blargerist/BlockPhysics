package blargerist.cake.blockphysics.asm.module;

import static org.objectweb.asm.Opcodes.*;

import java.util.Map;

import net.minecraft.item.ItemStack;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import squeek.asmhelper.ASMHelper;
import squeek.asmhelper.ObfHelper;
import blargerist.cake.blockphysics.ModInfo;
import blargerist.cake.blockphysics.asm.IClassTransformerModule;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class ModuleAnvilChunkLoader implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.world.chunk.storage.AnvilChunkLoader"
		};
	}

	@Override
	public String getModuleName()
	{
		return "transformAnvilChunkLoaderClass";
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

		if (transformedName.equals("net.minecraft.world.chunk.storage.AnvilChunkLoader"))
		{
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? "a" : "writeChunkToNBT", "(Ladr;Labw;Lby;)V");
			if (methodNode != null)
			{
				transformWriteChunkToNBT(methodNode);
			}
			else
				throw new RuntimeException("Could not find writeChunkToNBT(Ladr;Labw;Lby;)V method in " + classNode.name);
			
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? "a" : "readChunkFromNBT", "(Labw;Lby;)Ladr;");
			if (methodNode != null)
			{
				transformReadChunkFromNBT(methodNode);
			}
			else
				throw new RuntimeException("Could not find readChunkFromNBT(Labw;Lby;)Ladr; method in " + classNode.name);

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

	private void transformReadChunkFromNBT(MethodNode methodNode)
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
		
		LocalVariableNode var1 = ASMHelper.findLocalVariableOfMethod(methodNode, "nbttagcompound1", "Lby;");
		ModInfo.Log.info("LocalVarr " + var1.name + " " + var1.desc + " " + var1.index + " set as var1");
		LocalVariableNode var2 = ASMHelper.findLocalVariableOfMethod(methodNode, "extendedblockstorage", "Lads;");
		ModInfo.Log.info("LocalVarr " + var2.name + " " + var2.desc + " " + var2.index + " set as var2");
		
		InsnList toFind = new InsnList();
		toFind.add(new MethodInsnNode(INVOKEVIRTUAL, "ads", "e", "()V"));
		
		AbstractInsnNode found = ASMHelper.find(methodNode.instructions, toFind);
		AbstractInsnNode target = ASMHelper.move(found, -1);
		
		if (found == null || target == null)
		{
			new RuntimeException("Unexpected instruction pattern in method: " + methodNode.name);
		}
		
		InsnList toInject = new InsnList();
		//TODO check this
		toInject.add(new VarInsnNode(ALOAD, var1.index));
		toInject.add(new LdcInsnNode("BPData"));
    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "by", "b", "(Ljava/lang/String;)Z"));
    	LabelNode l6 = new LabelNode();
    	toInject.add(new JumpInsnNode(IFEQ, l6));
    	toInject.add(new VarInsnNode(ALOAD, var2.index));
    	toInject.add(new VarInsnNode(ALOAD, var1.index));
    	toInject.add(new LdcInsnNode("BPData"));
    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "by", "j", "(Ljava/lang/String;)[B"));
    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "ads", "setBPdataArray", "([B)V"));
    	LabelNode l7 = new LabelNode();
    	toInject.add(new JumpInsnNode(GOTO, l7));
    	toInject.add(l6);
    	toInject.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
    	toInject.add(new VarInsnNode(ALOAD, var2.index));
    	toInject.add(new IntInsnNode(SIPUSH, 4096));
    	toInject.add(new IntInsnNode(NEWARRAY, T_BYTE));
    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "ads", "setBPdataArray", "([B)V"));
    	toInject.add(l7);
	}

	private void transformWriteChunkToNBT(MethodNode methodNode)
	{
		/*
		 * Equivalent to injecting 
		 * nbttagcompound1.setByteArray("BPData", extendedblockstorage.getBPdataArray());
		 * before nbttaglist.appendTag(nbttagcompound1); on line 271
		 */
		
		LocalVariableNode var1 = ASMHelper.findLocalVariableOfMethod(methodNode, "nbttagcompound1", "Lby;");
		ModInfo.Log.info("LocalVarr " + var1.name + " " + var1.desc + " " + var1.index + " set as var1");
		LocalVariableNode var2 = ASMHelper.findLocalVariableOfMethod(methodNode, "extendedblockstorage", "Lads;");
		ModInfo.Log.info("LocalVarr " + var2.name + " " + var2.desc + " " + var2.index + " set as var2");
		
		InsnList toFind = new InsnList();
		toFind.add(new MethodInsnNode(INVOKEVIRTUAL, "cg", "a", "(Lcl;)V"));
		
		AbstractInsnNode found = ASMHelper.find(methodNode.instructions, toFind);
		AbstractInsnNode target = ASMHelper.move(found, -2);
		
		if (found == null || target == null)
		{
			new RuntimeException("Unexpected instruction pattern in method: " + methodNode.name);
		}
		
		InsnList toInject = new InsnList();
		
		toInject.add(new VarInsnNode(ALOAD, var1.index));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new VarInsnNode(ALOAD, var2.index));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "ads", "getBPdataArray", "()[B"));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "by", "a", "(Ljava/lang/String;[B)V"));
		
		methodNode.instructions.insertBefore(target, toInject);
	}
}