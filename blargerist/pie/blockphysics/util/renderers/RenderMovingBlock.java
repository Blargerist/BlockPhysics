package blargerist.pie.blockphysics.util.renderers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import blargerist.pie.blockphysics.BClient;
import blargerist.pie.blockphysics.ModInfo;
import blargerist.pie.blockphysics.util.EntityMovingBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMovingBlock extends Render
{
    private final RenderBlocks field_147920_a = new RenderBlocks();
    private static final String __OBFID = "CL_00000994";

    public RenderMovingBlock()
    {
        this.shadowSize = 0.5F;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityMovingBlock p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
		ModInfo.Log.info("Render Called");
    	//TODO
    	if (blargerist.pie.blockphysics.BClient.cancelRender(p_76986_1_) || p_76986_1_.func_145805_f() == null)
   		{
    		ModInfo.Log.info("Render Cancelled");
   				return;
   		}
        World world = p_76986_1_.func_145807_e();
        Block block = p_76986_1_.func_145805_f();
        int i = MathHelper.floor_double(p_76986_1_.posX);
        int j = MathHelper.floor_double(p_76986_1_.posY);
        int k = MathHelper.floor_double(p_76986_1_.posZ);
        
            GL11.glPushMatrix();
            GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
            this.bindEntityTexture(p_76986_1_);
            GL11.glDisable(GL11.GL_LIGHTING);
            Tessellator tessellator;

            if (block instanceof BlockAnvil)
            {
                this.field_147920_a.blockAccess = world;
                tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.setTranslation((double)((float)(-i) - 0.5F), (double)((float)(-j) - 0.5F), (double)((float)(-k) - 0.5F));
                this.field_147920_a.renderBlockAnvilMetadata((BlockAnvil)block, i, j, k, p_76986_1_.field_145814_a);
                tessellator.setTranslation(0.0D, 0.0D, 0.0D);
                tessellator.draw();
            }
            else if (block instanceof BlockDragonEgg)
            {
                this.field_147920_a.blockAccess = world;
                tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.setTranslation((double)((float)(-i) - 0.5F), (double)((float)(-j) - 0.5F), (double)((float)(-k) - 0.5F));
                this.field_147920_a.renderBlockDragonEgg((BlockDragonEgg)block, i, j, k);
                tessellator.setTranslation(0.0D, 0.0D, 0.0D);
                tessellator.draw();
            }
            else
            {
                this.field_147920_a.setRenderBoundsFromBlock(block);
                this.field_147920_a.renderBlockSandFalling(block, world, i, j, k, p_76986_1_.field_145814_a);
                //BClient.renderBlockSandFalling(this.field_147920_a, block, world, i, j, k, p_76986_1_.field_145814_a);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityMovingBlock p_110775_1_)
    {
        return TextureMap.locationBlocksTexture;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityMovingBlock)p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityMovingBlock)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}