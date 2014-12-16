package blargerist.cake.blockphysics.asm.module;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.BALOAD;
import static org.objectweb.asm.Opcodes.BASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.I2B;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IMUL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.T_BYTE;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import squeek.asmhelper.ASMHelper;
import blargerist.cake.blockphysics.ModInfo;
import blargerist.cake.blockphysics.asm.IClassTransformerModule;

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
		toInject.add(new FieldInsnNode(PUTFIELD, "ads", "blockBPdataArray", "[B"));

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
		mv.visitFieldInsn(GETFIELD, "ads", "blockBPdataArray", "[B");
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
		mv.visitFieldInsn(GETFIELD, "ads", "blockBPdataArray", "[B");
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
		mv.visitFieldInsn(GETFIELD, "ads", "blockBPdataArray", "[B");
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
		mv.visitFieldInsn(PUTFIELD, "ads", "blockBPdataArray", "[B");
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