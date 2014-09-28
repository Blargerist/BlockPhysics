/*	Copyright 2013 Dénes Derhán
*
*	This file is part of BlockPhysics.
*
*	BlockPhysics is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	BlockPhysics is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*
*	You should have received a copy of the GNU General Public License
*	along with BlockPhysics.  If not, see <http://www.gnu.org/licenses/>.
*/

package blockphysics;

import java.io.File;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityFallingSand;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

@SideOnly(Side.CLIENT)
public class BClient
{
	    

    public static void loadWorld(GuiScreen screen, String var2, String var3 )
    {
    	BlockPhysicsUtil.resetConfig();
		if (BlockPhysicsUtil.loadConfig(new File(BlockPhysics.gameDir, "saves"+File.separator +var2 +File.separator+BlockPhysics.conffile))) Minecraft.getMinecraft().launchIntegratedServer(var2, var3, (WorldSettings)null);
		else Minecraft.getMinecraft().displayGuiScreen(new BGui(screen, var2, var3, (WorldSettings)null, false));
    }
    
    public static void renderBlockSandFalling(RenderBlocks sandRenderBlocks, Block par1Block, World par2World, int par3, int par4, int par5, int par6)
    {
    	if ( !par1Block.renderAsNormalBlock() )
    	{
            if ( par1Block.getRenderType() == 18) sandRenderBlocks.renderBlockSandFalling(par1Block, par2World, par3, par4, par5, par6);
            else sandRenderBlocks.renderBlockAsItem(par1Block, par6, 0.8F);
    	}
    	else
    	{
	    	int colormult;
	    	if (par1Block instanceof BlockLeaves) colormult = colorLeaves(par2World, par3, par5, par6);
	    	else colormult = par1Block.colorMultiplier(par2World, par3, par4, par5);
	    	
	        float cm1 = (float)(colormult >> 16 & 255) / 255.0F;
	        float cm2 = (float)(colormult >> 8 & 255) / 255.0F;
	        float cm3 = (float)(colormult & 255) / 255.0F;
	    	
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
	
	        Tessellator var8 = Tessellator.instance;
	        
	        var8.setBrightness(par1Block.getMixedBrightnessForBlock(par2World, par3, par4, par5));
	        var8.startDrawingQuads();
	        
	        var8.setColorOpaque_F(var17, var20, var23);       
	        sandRenderBlocks.renderFaceYNeg(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(0, par6));
	
	        var8.setColorOpaque_F(var14, var15, var16);
	        sandRenderBlocks.renderFaceYPos(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(1, par6));
	
	        var8.setColorOpaque_F(var18, var21, var24);
	        sandRenderBlocks.renderFaceZNeg(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(2, par6));
	
	        var8.setColorOpaque_F(var18, var21, var24);
	        sandRenderBlocks.renderFaceZPos(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(3, par6));
	 
	        var8.setColorOpaque_F(var19, var22, var25);
	        sandRenderBlocks.renderFaceXNeg(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(4, par6));
	
	        var8.setColorOpaque_F(var19, var22, var25);
	        sandRenderBlocks.renderFaceXPos(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getIcon(5, par6));
	        
	        var8.draw();
    	}
    }
    
    public static int colorLeaves(World par1World, int par2, int par4, int var5)
    {
        
        if ((var5 & 3) == 1)
        {
            return ColorizerFoliage.getFoliageColorPine();
        }
        else if ((var5 & 3) == 2)
        {
            return ColorizerFoliage.getFoliageColorBirch();
        }
        else
        {
            int var6 = 0;
            int var7 = 0;
            int var8 = 0;

            for (int var9 = -1; var9 <= 1; ++var9)
            {
                for (int var10 = -1; var10 <= 1; ++var10)
                {
                    int var11 = par1World.getBiomeGenForCoords(par2 + var10, par4 + var9).getBiomeFoliageColor();
                    var6 += (var11 & 16711680) >> 16;
                    var7 += (var11 & 65280) >> 8;
                    var8 += var11 & 255;
                }
            }

            return (var6 / 9 & 255) << 16 | (var7 / 9 & 255) << 8 | var8 / 9 & 255;
        }
    }
    
    public static boolean cancelRender(EntityFallingSand par1EntityFallingSand)
	{
		//if ( par1EntityFallingSand.fallTime < 4 || par1EntityFallingSand.dead < 4 )
        //{
        	if (par1EntityFallingSand.worldObj.getBlockId(MathHelper.floor_double(par1EntityFallingSand.posX), MathHelper.floor_double(par1EntityFallingSand.posY), MathHelper.floor_double(par1EntityFallingSand.posZ)) != 0 ) return true;
        //}
		return false;
	}
}
