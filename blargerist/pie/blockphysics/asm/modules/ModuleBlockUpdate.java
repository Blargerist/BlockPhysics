package blargerist.pie.blockphysics.asm.modules;

import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.tree.*;
import blargerist.pie.blockphysics.asm.IClassTransformerModule;
import squeek.asmhelper.ASMHelper;

public class ModuleBlockUpdate implements IClassTransformerModule
{
    @Override
    public String[] getClassesToTransform()
    {
        return new String[]{
        "net.minecraft.world.World",
        "net.minecraft.world.WorldServer"
        };
    }

    @Override
    public String getModuleName()
    {
        return "createBlockUpdateEvents";
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

        if (transformedName.equals("net.minecraft.world.World"))
        {
            MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_147460_e", "(IIILnet/minecraft/block/Block;)V");
            if (methodNode != null)
            {
                injectNeighborChangeHook(methodNode);
            }
            else
                throw new RuntimeException("Could not find canSpawnStructureAtCoords method in " + transformedName);
            
            return ASMHelper.writeClassToBytes(classNode);
        }
        else if (transformedName.equals("net.minecraft.world.WorldServer"))
        {
        	MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, "func_72835_b", "()V");
        	if (methodNode != null)
        	{
        		injectOnWorldServerTickHook(methodNode);
        	}
        	else
        		throw new RuntimeException("Could not find tick()V method in " + transformedName);
        	
        	return ASMHelper.writeClassToBytes(classNode);
        }
        return bytes;
    }

	private void injectNeighborChangeHook(MethodNode method)
	{
		InsnList toFind = new InsnList();
		toFind.add(new VarInsnNode(ALOAD, 5));
		toFind.add(new VarInsnNode(ALOAD, 0));
		toFind.add(new VarInsnNode(ILOAD, 1));
		toFind.add(new VarInsnNode(ILOAD, 2));
		toFind.add(new VarInsnNode(ILOAD, 3));
		toFind.add(new VarInsnNode(ALOAD, 4));
		toFind.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/block/Block", "func_149695_a", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V", false));
		
		AbstractInsnNode target = ASMHelper.find(method.instructions, toFind);
		
		if (target == null)
			throw new RuntimeException("Could not find target in World." + method.name + " patch");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new VarInsnNode(ILOAD, 1));
		toInject.add(new VarInsnNode(ILOAD, 2));
		toInject.add(new VarInsnNode(ILOAD, 3));
		toInject.add(new VarInsnNode(ALOAD, 5));
		toInject.add(new MethodInsnNode(INVOKESTATIC, "blargerist/pie/blockphysics/events/BPEventHandler", "onNeighborBlockChange", "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V", false));
		
		method.instructions.insertBefore(target, toInject);
	}
	
	private void injectOnWorldServerTickHook(MethodNode method)
	{
		AbstractInsnNode target = ASMHelper.findLastInstructionWithOpcode(method, RETURN);
		
		if (target == null)
			throw new RuntimeException("Could not find target in WorldServer." + method.name + " patch");
		
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(ALOAD, 0));
		toInject.add(new MethodInsnNode(INVOKESTATIC, "blargerist/pie/blockphysics/events/BPEventHandler", "onWorldServerTick", "(Lnet/minecraft/world/WorldServer;)V", false));
		
		method.instructions.insertBefore(target, toInject);
	}
}