package blargerist.pie.blockphysics.network.proxies;

import blargerist.pie.blockphysics.ModInfo;
import blargerist.pie.blockphysics.util.EntityMovingBlock;
import blargerist.pie.blockphysics.util.renderers.RenderMovingBlock;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void initSounds()
    {
    }
    
    @Override
    public void initRenderers()
    {
    	ModInfo.Log.info("Initiating Renderers");
		RenderingRegistry.registerEntityRenderingHandler(EntityMovingBlock.class, new RenderMovingBlock());
    }
}