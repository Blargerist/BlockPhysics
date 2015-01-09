package blargerist.cake.blockphysics.network.proxies;

import blargerist.cake.blockphysics.ModInfo;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void initSounds()
    {
    }
    
    @Override
    public void initRenderers()
    {
    	ModInfo.Log.info("Initiating Renderers");
		//RenderingRegistry.registerEntityRenderingHandler(EntityMovingBlock.class, new RenderMovingBlock());
    }
}