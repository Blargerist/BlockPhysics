package blockphysics;

import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.item.EntityFallingSand;

@SideOnly(Side.CLIENT)
public class BClient
{
  public static void loadWorld(GuiScreen screen, String var2, String var3)
  {
    
    if (BlockPhysicsUtil.loadConfig(new File(BlockPhysics.gameDir, "saves" + File.separator + var2 + File.separator + "blockphysics.cfg"))) {
      Minecraft.getMinecraft().launchIntegratedServer(var2, var3, (WorldSettings)null);
    } else {
      Minecraft.getMinecraft().displayGuiScreen(new BGui(screen, var2, var3, (WorldSettings)null, false));
    }
  }
  
  public static void renderBlockSandFalling(RenderBlocks sandRenderBlocks, Block block, World par2World, int par3, int par4, int par5, int par6)
  {
    if (!block.renderAsNormalBlock())
    {
      if (block.getRenderType() == 18) {
        sandRenderBlocks.renderBlockSandFalling(block, par2World, par3, par4, par5, par6);
      } else {
        sandRenderBlocks.renderBlockAsItem(block, par6, 0.8F);
      }
    }
    else
    {
      int colormult;
      if ((block instanceof BlockLeaves)) {
        colormult = colorLeaves(par2World, par3, par5, par6);
      } else {
        colormult = block.colorMultiplier(par2World, par3, par4, par5);
      }
      float cm1 = (colormult >> 16 & 0xFF) / 255.0F;
      float cm2 = (colormult >> 8 & 0xFF) / 255.0F;
      float cm3 = (colormult & 0xFF) / 255.0F;
      
      sandRenderBlocks.enableAO = false;
      boolean var9 = false;
      float var10 = 0.5F;
      float var11 = 1.0F;
      float var12 = 0.8F;
      float var13 = 0.6F;
      float var14 = var11 * cm1;
      float var15 = var11 * cm2;
      float var16 = var11 * cm3;
      float var17 = var10 * cm1;
      float var18 = var12 * cm1;
      float var19 = var13 * cm1;
      float var20 = var10 * cm2;
      float var21 = var12 * cm2;
      float var22 = var13 * cm2;
      float var23 = var10 * cm3;
      float var24 = var12 * cm3;
      float var25 = var13 * cm3;
      
      Tessellator tessellator = Tessellator.instance;
      
      tessellator.setBrightness(block.getMixedBrightnessForBlock(par2World, par3, par4, par5));
      tessellator.startDrawingQuads();
      
      tessellator.setColorOpaque_F(var17, var20, var23);
      sandRenderBlocks.renderFaceYNeg(block, -0.5D, -0.5D, -0.5D, block.getIcon(0, par6));
      
      tessellator.setColorOpaque_F(var14, var15, var16);
      sandRenderBlocks.renderFaceYPos(block, -0.5D, -0.5D, -0.5D, block.getIcon(1, par6));
      
      tessellator.setColorOpaque_F(var18, var21, var24);
      sandRenderBlocks.renderFaceZNeg(block, -0.5D, -0.5D, -0.5D, block.getIcon(2, par6));
      
      tessellator.setColorOpaque_F(var18, var21, var24);
      sandRenderBlocks.renderFaceZPos(block, -0.5D, -0.5D, -0.5D, block.getIcon(3, par6));
      
      tessellator.setColorOpaque_F(var19, var22, var25);
      sandRenderBlocks.renderFaceXNeg(block, -0.5D, -0.5D, -0.5D, block.getIcon(4, par6));
      
      tessellator.setColorOpaque_F(var19, var22, var25);
      sandRenderBlocks.renderFaceXPos(block, -0.5D, -0.5D, -0.5D, block.getIcon(5, par6));
      
      tessellator.draw();
    }
  }
  
  public static int colorLeaves(World world, int x, int z, int meta)
  {
    if ((meta & 0x3) == 1) {
      return ColorizerFoliage.getFoliageColorPine();
    }
    if ((meta & 0x3) == 2) {
      return ColorizerFoliage.getFoliageColorBirch();
    }
    //NOTE: Removed lots of color math, replaced with "return getFoliageColorBasic"
    return ColorizerFoliage.getFoliageColorBasic();
  }
  /* Unable to deobfuscate. //TODO
  public static boolean cancelRender(EntityFallingSand par1EntityFallingSand)
  {
    if (par1EntityFallingSand.q.a(MathHelper.c(par1EntityFallingSand.u), MathHelper.c(par1EntityFallingSand.v), MathHelper.c(par1EntityFallingSand.w)) != 0) {
      return true;
    }
    return false;
  }*/
}