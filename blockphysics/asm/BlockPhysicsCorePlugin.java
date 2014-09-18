package blockphysics.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import java.io.File;
import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"blockphysics.asm"})
@IFMLLoadingPlugin.MCVersion("1.6.2")
public class BlockPhysicsCorePlugin
  implements IFMLLoadingPlugin
{
  public String[] getLibraryRequestClass()
  {
    return null;
  }
  
  public String[] getASMTransformerClass()
  {
    return new String[] { "blockphysics.asm.BPTransformer" };
  }
  
  public String getModContainerClass()
  {
    return null;
  }
  
  public String getSetupClass()
  {
    return null;
  }
  
  public void injectData(Map<String, Object> data)
  {
    blockphysics.BlockPhysics.gameDir = (File)data.get("mcLocation");
    blockphysics.BlockPhysics.bpjarFile = (File)data.get("coremodLocation");
  }
}