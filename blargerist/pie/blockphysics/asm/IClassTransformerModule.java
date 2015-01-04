package blargerist.pie.blockphysics.asm;

import net.minecraft.launchwrapper.IClassTransformer;

public interface IClassTransformerModule extends IClassTransformer
{
	public String[] getClassesToTransform();
	public String getModuleName();
	public boolean canBeDisabled();
}