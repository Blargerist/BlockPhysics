package blargerist.pie.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.ASMHelper;
import blargerist.pie.blockphysics.ModInfo;
import blargerist.pie.blockphysics.asm.IClassTransformerModule;

public class ModuleEntityFallingBlock implements IClassTransformerModule
{
	private static boolean addCanRenderOnFire;
	private static boolean verifyCanRenderOnFire = false;
	
	@Override
	public String[] getClassesToTransform()
	{
		return new String[]{
		"net.minecraft.entity.item.EntityFallingBlock"
		};
	}
	
	@Override
	public String getModuleName()
	{
		return "transformEntityFallingBlockClass";
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

		if (transformedName.equals("net.minecraft.entity.item.EntityFallingBlock"))
		{
			createAccelerationX(classNode);
			createAccelerationY(classNode);
			createAccelerationZ(classNode);
			createbpdata(classNode);
			createSlideDir(classNode);
			createMedia(classNode);
			createDead(classNode);
			
			verifyFieldAdded(classNode, "accelerationX", "D");
			verifyFieldAdded(classNode, "accelerationY", "D");
			verifyFieldAdded(classNode, "accelerationZ", "D");
			verifyFieldAdded(classNode, "bpdata", "I");
			verifyFieldAdded(classNode, "slideDir", "B");
			verifyFieldAdded(classNode, "media", "I");
			verifyFieldAdded(classNode, "dead", "B");
			
			MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "<init>", "(Lnet/minecraft/world/World;)V");
			if (methodNode != null)
			{
				transformInit(methodNode);
			}
			else
				throw new RuntimeException("Could not find <init>(World)V method in " + classNode.name);
			
			createGetBoundingBox(classNode);
			createSetInWeb(classNode);
			createMoveEntity(classNode);
			createCanRenderOnFire(classNode);
			
			verifyMethodAdded(classNode, "getBoundingBox", "()Lnet/minecraft/util/AxisAlignedBB;");
			verifyMethodAdded(classNode, "setInWeb", "()V");
			verifyMethodAdded(classNode, "moveEntity", "(DDD)V");
			
			if (verifyCanRenderOnFire)
			verifyMethodAdded(classNode, "canRenderOnFire", "()Z");
			
			return ASMHelper.writeClassToBytes(classNode);
		}
		return bytes;
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
	
	public void verifyMethodAdded(ClassNode classNode, String name, String desc)
	{
		MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, name, desc);
		if (methodNode != null)
		{
			ModInfo.Log.info("Successfully added method: " + methodNode.name + methodNode.desc + " in " + classNode.name);
		} else
			throw new RuntimeException("Could not create method: " + name + desc + " in " + classNode.name);
	}
	
	public void createAccelerationX(ClassNode classNode)
	{
		FieldVisitor fv = classNode.visitField(ACC_PUBLIC, "accelerationX", "D", null, null);
        fv.visitEnd();
	}
	
	public void createAccelerationY(ClassNode classNode)
	{
		FieldVisitor fv = classNode.visitField(ACC_PUBLIC, "accelerationY", "D", null, null);
        fv.visitEnd();
	}
	
	public void createAccelerationZ(ClassNode classNode)
	{
		FieldVisitor fv = classNode.visitField(ACC_PUBLIC, "accelerationZ", "D", null, null);
        fv.visitEnd();
	}
	
	public void createbpdata(ClassNode classNode)
	{
		FieldVisitor fv = classNode.visitField(ACC_PUBLIC, "bpdata", "I", null, null);
        fv.visitEnd();
	}
	
	public void createSlideDir(ClassNode classNode)
	{
		FieldVisitor fv = classNode.visitField(ACC_PUBLIC, "slideDir", "B", null, null);
        fv.visitEnd();
	}
	
	public void createMedia(ClassNode classNode)
	{
		FieldVisitor fv = classNode.visitField(ACC_PUBLIC, "media", "I", null, null);
        fv.visitEnd();
	}
	
	public void createDead(ClassNode classNode)
	{
		FieldVisitor fv = classNode.visitField(ACC_PUBLIC, "dead", "B", null, null);
        fv.visitEnd();
	}
	
	public void transformInit(MethodNode method)
	{
		/*
		 * Equivalent to injecting
		 * this.preventEntitySpawning = true;
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
		
		AbstractInsnNode endReturn = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new InsnNode(ICONST_1));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70156_m", "Z"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new LdcInsnNode(new Float("0.996")));
    	toInject.add(new LdcInsnNode(new Float("0.996")));
    	toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/item/EntityFallingBlock", "func_70105_a", "(FF)V"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70131_O", "F"));
    	toInject.add(new InsnNode(FCONST_2));
    	toInject.add(new InsnNode(FDIV));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70129_M", "F"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new InsnNode(DCONST_0));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "x", "D"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new InsnNode(DCONST_0));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "y", "D"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new InsnNode(DCONST_0));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "z", "D"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new InsnNode(DCONST_0));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationX", "D"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new LdcInsnNode(new Double("-0.024525")));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationY", "D"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new InsnNode(DCONST_0));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "accelerationZ", "D"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new InsnNode(ICONST_0));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "slideDir", "B"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new InsnNode(ICONST_1));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "Z", "Z"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new LdcInsnNode(new Float("0.8")));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "aa", "F"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new InsnNode(ICONST_4));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "dead", "B"));
    	toInject.add(new VarInsnNode(ALOAD, 0));
    	toInject.add(new InsnNode(ICONST_0));
    	toInject.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/item/EntityFallingBlock", "bpdata", "I"));
	}
	
	public void createGetBoundingBox(ClassNode classNode)
	{
		/*
         * Equivalent to adding method
         * public AxisAlignedBB getBoundingBox()
         * {
         * 		return this.boundingBox;
         * }
         */
		
        MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "getBoundingBox", "()Lnet/minecraft/util/AxisAlignedBB;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70121_D", "Lnet/minecraft/util/AxisAlignedBB;");
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
	}
	
	public void createSetInWeb(ClassNode classNode)
	{
		/*
         * Equivalent to adding method
         * public void setInWeb()
         * {
         * 		blank
         * }
         */
		
        MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "setInWeb", "()V", null, null);
        mv.visitCode();
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 1);
        mv.visitEnd();
	}
	
	public void createMoveEntity(ClassNode classNode)
	{
		/*
         * Equivalent to adding method
         * public void moveEntity(double par1, double par2, double par3)
         * {
         * 		blockphysics.BlockPhysics.moveEntity(this.worldObj, this, par1, par2, par3);
         * }
         */
		
        MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "moveEntity", "(DDD)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/item/EntityFallingBlock", "field_70170_p", "Lnet/minecraft/world/World;");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(DLOAD, 1);
        mv.visitVarInsn(DLOAD, 3);
        mv.visitVarInsn(DLOAD, 5);
        mv.visitMethodInsn(INVOKESTATIC, "blargerist/cake/blockphysics/util/EntityMove", "moveEntity", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/item/EntityFallingBlock;DDD)V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(8, 7);
        mv.visitEnd();
	}
	
	public void createCanRenderOnFire(ClassNode classNode)
	{
		if (addCanRenderOnFire)
		{
			/*
        	 * Equivalent to adding method
        	 * public boolean canRenderOnFire()
        	 * {
        	 * 		return this.isInWater();
        	 * }
        	 */ 
			
        	MethodVisitor mv = classNode.visitMethod(ACC_PUBLIC, "canRenderOnFire", "()Z", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/item/EntityFallingBlock", "func_70090_H", "()Z");
            mv.visitInsn(IRETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
            
            addCanRenderOnFire = false;
            verifyCanRenderOnFire = true;
		}
	}
}