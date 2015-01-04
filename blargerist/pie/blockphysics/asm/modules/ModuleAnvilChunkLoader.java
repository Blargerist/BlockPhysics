package blargerist.pie.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.ASMHelper;
import blargerist.pie.blockphysics.BlockPhysics;
import blargerist.pie.blockphysics.ModInfo;
import blargerist.pie.blockphysics.asm.IClassTransformerModule;

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
		
		ClassNode classNode = ASMHelper.readClassFromBytes(bytes);

		if (transformedName.equals("net.minecraft.world.chunk.storage.AnvilChunkLoader"))
		{
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_75820_a", "(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V");
			if (methodNode != null)
			{
				transformWriteChunkToNBT(methodNode);
			}
			else
				throw new RuntimeException("Could not find writeChunkToNBT method in " + classNode.name);
			
			methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_75823_a", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;");
			if (methodNode != null)
			{
				transformReadChunkFromNBT(methodNode);
			}
			else
				throw new RuntimeException("Could not find readChunkFromNBT method in " + classNode.name);

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
		
		InsnList toFind = new InsnList();
		toFind.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "func_76672_e", "()V", false));
		
		AbstractInsnNode found = ASMHelper.find(methodNode.instructions, toFind);
		AbstractInsnNode target = ASMHelper.move(found, -1);
		
		if (found == null || target == null)
		{
			new RuntimeException("Unexpected instruction pattern in method: " + methodNode.name);
		}
		
		InsnList toInject = new InsnList();
		//TODO check this
		toInject.add(new VarInsnNode(ALOAD, 11));
		toInject.add(new LdcInsnNode("BPData"));
    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74764_b", "(Ljava/lang/String;)Z", false));
    	LabelNode l6 = new LabelNode();
    	toInject.add(new JumpInsnNode(IFEQ, l6));
    	toInject.add(new VarInsnNode(ALOAD, 13));
    	toInject.add(new VarInsnNode(ALOAD, 11));
    	toInject.add(new LdcInsnNode("BPData"));
    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74770_j", "(Ljava/lang/String;)[B", false));
    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "setBPdataArray", "([B)V", false));
    	LabelNode l7 = new LabelNode();
    	toInject.add(new JumpInsnNode(GOTO, l7));
    	toInject.add(l6);
    	toInject.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
    	toInject.add(new VarInsnNode(ALOAD, 13));
    	toInject.add(new IntInsnNode(SIPUSH, 4096));
    	toInject.add(new IntInsnNode(NEWARRAY, T_BYTE));
    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "setBPdataArray", "([B)V", false));
    	toInject.add(l7);
    	
    	methodNode.instructions.insertBefore(target, toInject);
	}

	private void transformWriteChunkToNBT(MethodNode methodNode)
	{
		/*
		 * Equivalent to injecting 
		 * nbttagcompound1.setByteArray("BPData", extendedblockstorage.getBPdataArray());
		 * before nbttaglist.appendTag(nbttagcompound1); on line 271
		 */
		
		InsnList toFind = new InsnList();
		toFind.add(new VarInsnNode(ALOAD, 5));
		toFind.add(new VarInsnNode(ALOAD, 11));
		toFind.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagList", "func_74742_a", "(Lnet/minecraft/nbt/NBTBase;)V", false));
		
		AbstractInsnNode target = ASMHelper.find(methodNode.instructions, toFind);
		
		if (target == null)
		{
			new RuntimeException("Unexpected instruction pattern in method: " + methodNode.name);
		}
		
		InsnList toInject = new InsnList();
		
		toInject.add(new VarInsnNode(ALOAD, 11));
		toInject.add(new LdcInsnNode("BPData"));
		toInject.add(new VarInsnNode(ALOAD, 10));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "getBPdataArray", "()[B", false));
		toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/nbt/NBTTagCompound", "func_74773_a", "(Ljava/lang/String;[B)V", false));
		
		methodNode.instructions.insertBefore(target, toInject);
	}
}