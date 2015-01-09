package blargerist.cake.blockphysics.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import blargerist.cake.blockphysics.ModInfo;

public class RemoveMethodAdapter extends ClassVisitor
{
	private String mName;
	private String mDesc;
	
	public RemoveMethodAdapter(int api, ClassVisitor cv, String mName, String mDesc)
	{
		super(api, cv);
		this.mName = mName;
		this.mDesc = mDesc;
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		ModInfo.Log.info("visitMethod Called for method: " + name + " " + desc);
		if (name.equals(mName) && desc.equals(mDesc))
		{
			ModInfo.Log.info("Returning null for method: " + name + " " + desc);
			// do not delegate to next visitor -> this removes the method
			return null;
		}
		return cv.visitMethod(access, name, desc, signature, exceptions);
	}
}
