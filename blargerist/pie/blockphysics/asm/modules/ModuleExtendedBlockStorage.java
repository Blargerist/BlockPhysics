package blargerist.pie.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;

import squeek.asmhelper.ASMHelper;
import blargerist.pie.blockphysics.ModInfo;
import blargerist.pie.blockphysics.asm.IClassTransformerModule;

public class ModuleExtendedBlockStorage implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]
		{ "net.minecraft.world.chunk.storage.ExtendedBlockStorage" };
	}

	@Override
	public String getModuleName()
	{
		return "transformExtendedBlockStorageClass";
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

		if (transformedName.equals("net.minecraft.world.chunk.storage.ExtendedBlockStorage"))
		{
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(IZ)V");
			if (methodNode != null)
			{
				setBlockBPdataArray(methodNode);
			} else
				throw new RuntimeException("Could not find <init>(IZ)V method in " + classNode.name);

			createGetBlockBPdata(classNode);
			createSetBlockBPdata(classNode);
			createGetBPdataArray(classNode);
			createSetBPdataArray(classNode);
			createblockBPdataArray(classNode);

			verifyMethodAdded(classNode, "getBlockBPdata", "(III)I");
			verifyMethodAdded(classNode, "setBlockBPdata", "(IIII)V");
			verifyMethodAdded(classNode, "getBPdataArray", "()[B");
			verifyMethodAdded(classNode, "setBPdataArray", "([B)V");
			
			verifyFieldAdded(classNode, "blockBPdataArray", "[B");
			
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

	public void setBlockBPdataArray(MethodNode method)
	{
		AbstractInsnNode targetNode = ASMHelper.findLastInstructionWithOpcode(method, RETURN);

		InsnList toInject = new InsnList();

		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new IntInsnNode(SIPUSH, 4096));
		toInject.add(new IntInsnNode(NEWARRAY, T_BYTE));
		toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B"));

		method.instructions.insertBefore(targetNode, toInject);
	}

	private void createGetBlockBPdata(ClassNode classNode)
	{
		/*
		 * Equivalent to adding method: public int getBlockBPdata(int par1, int
		 * par2, int par3) { return this.blockBPdataArray[par2 * 256 + par1 * 16
		 * + par3]; }
		 */

		MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "getBlockBPdata", "(III)I", null, null);

		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B");
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
	}

	private void createSetBlockBPdata(ClassNode classNode)
	{
		/*
		 * Equivalent to adding method: public void setBlockBPdata(int par1, int
		 * par2, int par3, int par4) { this.blockBPdataArray[par2 * 256 + par1 *
		 * 16 + par3] = (byte)par4; }
		 */

		MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "setBlockBPdata", "(IIII)V", null, null);

		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B");
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
	}

	private void createGetBPdataArray(ClassNode classNode)
	{
		/*
		 * Equivalent to adding method: public byte[] getBPdataArray() { return
		 * this.blockBPdataArray; }
		 */

		MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "getBPdataArray", "()[B", null, null);

		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B");
		mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	private void createSetBPdataArray(ClassNode classNode)
	{
		/*
		 * Equivalent to adding method: public void setBPdataArray(byte[] bytes)
		 * { this.blockBPdataArray = bytes; }
		 */

		MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "setBPdataArray", "([B)V", null, null);

		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(PUTFIELD, "net/minecraft/world/chunk/storage/ExtendedBlockStorage", "blockBPdataArray", "[B");
		mv.visitInsn(RETURN);
		mv.visitMaxs(2, 2);
		mv.visitEnd();
	}

	private void createblockBPdataArray(ClassNode classNode)
	{
		/*
		 * Equivlent to adding field private byte[] blockBPdataArray
		 */

		FieldVisitor fv = classNode.visitField(ACC_PRIVATE, "blockBPdataArray", "[B", null, null);

		fv.visitEnd();
	}
}