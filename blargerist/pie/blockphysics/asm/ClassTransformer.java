package blargerist.pie.blockphysics.asm;

import java.util.ArrayList;
import java.util.List;
import blargerist.pie.blockphysics.asm.modules.ModuleAnvilChunkLoader;
import blargerist.pie.blockphysics.asm.modules.ModuleBlockUpdate;
import blargerist.pie.blockphysics.asm.modules.ModuleChunk;
import blargerist.pie.blockphysics.asm.modules.ModuleEntityFallingBlock;
import blargerist.pie.blockphysics.asm.modules.ModuleExplosion;
import blargerist.pie.blockphysics.asm.modules.ModuleExtendedBlockStorage;
import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer
{
	private static final List<IClassTransformerModule> transformerModules = new ArrayList<IClassTransformerModule>();
	static
	{
		registerTransformerModule(new ModuleBlockUpdate());
		registerTransformerModule(new ModuleChunk());
		registerTransformerModule(new ModuleExtendedBlockStorage());
		registerTransformerModule(new ModuleAnvilChunkLoader());
		//registerTransformerModule(new ModuleEntityFallingBlock());
		registerTransformerModule(new ModuleExplosion());
	}

	public static void registerTransformerModule(IClassTransformerModule transformerModule)
	{
		transformerModules.add(transformerModule);
	}

	public static void disableTransformerModule(String name)
	{
		IClassTransformerModule moduleToRemove = null;
		for (IClassTransformerModule transformerModule : transformerModules)
		{
			if (transformerModule.getModuleName().equals(name))
			{
				moduleToRemove = transformerModule;
				break;
			}
		}
		if (moduleToRemove != null)
			transformerModules.remove(moduleToRemove);
	}

	public static IClassTransformerModule[] getTransformerModules()
	{
		return transformerModules.toArray(new IClassTransformerModule[0]);
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		for (IClassTransformerModule transformerModule : transformerModules)
		{
			for (String classToTransform : transformerModule.getClassesToTransform())
			{
				if (classToTransform.equals(transformedName))
				{
					basicClass = transformerModule.transform(name, transformedName, basicClass);
				}
			}
		}
		return basicClass;
	}
}