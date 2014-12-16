package blargerist.cake.blockphysics.asm.module;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import squeek.asmhelper.ASMHelper;
import squeek.asmhelper.ObfHelper;
import blargerist.cake.blockphysics.ModInfo;
import blargerist.cake.blockphysics.asm.IClassTransformerModule;

public class ModuleChunk implements IClassTransformerModule
{
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.world.chunk.Chunk"
		};
	}
	
	@Override
	public String getModuleName()
	{
		return "transformChunkClass";
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

		if (transformedName.equals("net.minecraft.world.chunk.Chunk"))
		{
			createGetBlockBPdata(classNode);
			createSetBlockBPdata(classNode);
			
			verifyMethodAdded(classNode, "getBlockBPdata", "(III)I");
			verifyMethodAdded(classNode, "setBlockBPdata", "(IIII)Z");

			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
	}
	
	public void verifyMethodAdded(ClassNode classNode, String name, String desc)
	{
		MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, name, desc);
		if (methodNode != null)
		{
			ModInfo.Log.info("Successfully added method: " + methodNode.name + methodNode.desc);
		}
		else
			throw new RuntimeException("Could not create method -getBlockBPdata(int, int, int)int- in Chunk");
	}
	
	public String getObfuscatedMethodName()
	{
		getClass();
		return "";
	}
	
	public void createGetBlockBPdata(ClassNode classNode)
	{
        /*
         * Equivalent to adding method:
         * public int getBlockBPdata(int par1, int par2, int par3)
         * {
         * 		if (par2 >> 4 >= this.storageArrays.length)
		 * 			{
		 * 				return 0;
		 * 			}
		 * 
		 * 		ExtendedBlockStorage blockStorage = this.storageArrays[par2 >> 4];
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
		
		MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "getBlockBPdata", "(III)I", null, null);
		mv.visitCode();
		mv.visitVarInsn(ILOAD, 2);
		mv.visitInsn(ICONST_4);
		mv.visitInsn(ISHR);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "adr", "r", "[Lads;");
		mv.visitInsn(ARRAYLENGTH);
		Label label1 = new Label();
		mv.visitJumpInsn(IF_ICMPLT, label1);
		mv.visitInsn(ICONST_0);
		mv.visitInsn(IRETURN);
		mv.visitLabel(label1);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "adr", "r", "[Lads;");
		mv.visitVarInsn(ILOAD, 2);
		mv.visitInsn(ICONST_4);
		mv.visitInsn(ISHR);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, 4);
		mv.visitVarInsn(ALOAD, 4);
		Label label2 = new Label();
		mv.visitJumpInsn(IFNULL, label2);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitVarInsn(ILOAD, 1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitIntInsn(BIPUSH, 15);
		mv.visitInsn(IAND);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitMethodInsn(INVOKEVIRTUAL, "ads", "getBlockBPdata", "(III)I");
		Label label3 = new Label();
		mv.visitJumpInsn(GOTO, label3);
		mv.visitLabel(label2);
		mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {"ads"}, 0, null);
		mv.visitInsn(ICONST_0);
		mv.visitLabel(label3);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {Opcodes.INTEGER});
		mv.visitInsn(IRETURN);
		mv.visitMaxs(4, 5);
		mv.visitEnd();
	}
	
	public void createSetBlockBPdata(ClassNode classNode)
	{
		/*
		 * Equivalent to adding method:
		 * public boolean setBlockBPdata(int par1, int par2, int par3, int par4)
		 * {
		 *	 ExtendedBlockStorage blockStorage = this.storageArrays[par2 >> 4];
		 *	
		 *	 if (blockStorage == null)
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
		
		MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "setBlockBPdata", "(IIII)Z", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "adr", "r", "[Lads;");
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
		mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {"ads"}, 0, null);
		mv.visitVarInsn(ALOAD, 5);
		mv.visitVarInsn(ILOAD, 1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitIntInsn(BIPUSH, 15);
		mv.visitInsn(IAND);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitMethodInsn(INVOKEVIRTUAL, "ads", "getBlockBPdata", "(III)I");
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
		mv.visitFieldInsn(PUTFIELD, "adr", "l", "Z");
		mv.visitVarInsn(ALOAD, 5);
		mv.visitVarInsn(ILOAD, 1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitIntInsn(BIPUSH, 15);
		mv.visitInsn(IAND);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitVarInsn(ILOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, "ads", "setBlockBPdata", "(IIII)V");
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IRETURN);
		mv.visitMaxs(5, 7);
		mv.visitEnd();
	}
}