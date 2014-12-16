package blargerist.cake.blockphysics;

import java.io.File;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.WorldSettings;

public class BGui extends GuiScreen
{
	private boolean catapult = true;
    private int physics = 2;
    private int explphys = 1;
    private boolean explfire = true;
    private GuiButton catapultPiston;
    private GuiButton physPreset;
    private GuiButton explPhys;
    private GuiButton explFire;
    	
	private GuiScreen parentGuiScreen;
    private String folderName;
    private String wName;
    private long startTime;
    
    /** hardcore', 'creative' or 'survival */
    
    private boolean createClicked;
	private boolean create;
	private WorldSettings worldSettings;

    public BGui(GuiScreen par1GuiScreen, String folder, String name, WorldSettings ws, boolean create)
    {
        this.parentGuiScreen = par1GuiScreen;
        this.folderName = folder;
        this.wName = name;
        this.create = create;
        this.worldSettings = ws;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {

    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
    	startTime = System.currentTimeMillis();
    	
    	this.catapult = true;
        this.explfire = true;

    	this.buttonList.clear();
    	this.buttonList.add(this.catapultPiston = new GuiButton(9, this.width / 2 - 155, 90, 150, 20, "Catapult Piston:"));
	    this.buttonList.add(this.physPreset = new GuiButton(10, this.width / 2 - 155, 120, 150, 20, "Moving Blocks:"));
	    this.buttonList.add(this.explPhys = new GuiButton(11, this.width / 2 + 5, 120, 150, 20, "Explosion Strength:"));
	    this.buttonList.add(this.explFire = new GuiButton(12, this.width / 2 + 5, 90, 150, 20, "Explosion Fire:"));
	    String btxt;
	    if (create) btxt = "Create New World";
	    else btxt = "Convert World";
	    this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, btxt));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, "Back"));

        this.updateButtonText();
        this.createClicked = false;
    }

    private void updateButtonText()
    {
        if (this.catapult)
        {
            this.catapultPiston.displayString = "Catapult Piston: ON";
        }
        else
        {
        	this.catapultPiston.displayString = "Catapult Piston: OFF";
        }
        
        if (this.explfire)
        {
            this.explFire.displayString = "Explosion Fire: ON";
        }
        else
        {
        	this.explFire.displayString = "Explosion Fire: OFF";
        }
        
        switch (this.physics)
        {
        case 0: 
        	this.physPreset.displayString = "Moving Blocks: OFF";
        	break;
        case 1: 
        	this.physPreset.displayString = "Moving Blocks: Vanilla";
        	break;
        case 2: 
        	this.physPreset.displayString = "Moving Blocks: Default";
        	break;
        case 3: 
        	this.physPreset.displayString = "Moving Blocks: Easy";
        	break;
        case 4: 
        	this.physPreset.displayString = "Moving Blocks: Hard";	
        }
        
        switch (this.explphys)
        {
        case 0: 
        	this.explPhys.displayString = "Explosion Strength: OFF";
        	break;
        case 1: 
        	this.explPhys.displayString = "Explosion Strength: Default";
        	break;
        case 2: 
        	this.explPhys.displayString = "Explosion Strength: High";
        	break;
        case 3: 
        	this.explPhys.displayString = "Explosion Strength: Griefing";
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
    	if (par1GuiButton.enabled)
        {
        
    		if (par1GuiButton.id == 1)
            {
    			this.mc.displayGuiScreen(this.parentGuiScreen);
            }
            else if (par1GuiButton.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);

                if (this.createClicked)
                {
                    return;
                }

                this.createClicked = true;
                
                BlockPhysicsUtil.resetConfig();
            	
                this.mc.launchIntegratedServer(this.folderName, this.wName, worldSettings);
            	
            	File confF = new File(BlockPhysics.gameDir+File.separator +"saves" +File.separator +this.folderName +File.separator+BlockPhysics.conffile);
            	BlockPhysicsUtil.compConfig(confF, this.catapult, this.explfire, explphys, physics);
                
            	BlockPhysicsUtil.loadConfig(confF);
            }
            else if (par1GuiButton.id == 9)
            {
            	this.catapult = !this.catapult;
                this.updateButtonText();
            }
            else if (par1GuiButton.id == 10)
            {
                ++this.physics;
                if (this.physics > 4) this.physics = 0;
                this.updateButtonText();
            }
            else if (par1GuiButton.id == 11)
            {
                ++this.explphys;
                if (this.explphys > 3) this.explphys = 0;
                this.updateButtonText();
            }
            else if (par1GuiButton.id == 12)
            {
            	this.explfire = !this.explfire;
                this.updateButtonText();
            }
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
    	if  (System.currentTimeMillis() - startTime > 250L)	super.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        if (create)
        {
        	this.drawCenteredString(this.fontRenderer, "Configure BlockPhysics for World:" , this.width / 2, 20, 16777215);
            this.drawCenteredString(this.fontRenderer, wName, this.width / 2 , 50, 10526880);
        }
        else
        {
        	this.drawCenteredString(this.fontRenderer, "Convert World [ "+wName+" ] to BlockPhysics", this.width / 2, 20, 16777215);
            this.drawCenteredString(this.fontRenderer, "Warning! Last chance to backup this world!", this.width / 2 , 50, 10526880);
        }
        
        switch (this.physics)
        {
        case 0:
        	this.drawString(this.fontRenderer, "Nothing falls.", this.width / 2 -170, 150, 10526880);
        	this.drawString(this.fontRenderer, "explosions and pistons", this.width / 2 -170, 160, 10526880);
        	this.drawString(this.fontRenderer, "can push blocks.", this.width / 2 -170, 170, 10526880);
        	break;
        case 1: 
        	this.drawString(this.fontRenderer, "Only sand and gravel will fall,", this.width / 2 -170, 150, 10526880);
        	this.drawString(this.fontRenderer, "explosions and pistons", this.width / 2 -170, 160, 10526880);
        	this.drawString(this.fontRenderer, "can push blocks.", this.width / 2 -170, 170, 10526880);
        	break;
        case 2: 
        	this.drawString(this.fontRenderer, "The block's behavior will be set", this.width / 2 -170, 150, 10526880);
        	this.drawString(this.fontRenderer, "according to the global settings.", this.width / 2 -170, 160, 10526880);
        	this.drawString(this.fontRenderer, "( .minecraft/config/", this.width / 2-170, 170, 10526880);
        	this.drawString(this.fontRenderer, "blockphysics/"+BlockPhysics.conffile+" )", this.width / 2 - 170, 180, 10526880);
        	break;
        case 3: 
        	this.drawString(this.fontRenderer, "Only sand and gravel will slide,", this.width / 2 -170, 150, 10526880);
        	this.drawString(this.fontRenderer, "various blocks will fall.", this.width / 2 -170, 160, 10526880);
        	break;
        case 4:
        	
        }
        	
        switch (this.explphys)
        {
        case 0: 
        	this.drawString(this.fontRenderer, "Explosions will not affect blocks,", this.width / 2 + 10, 150, 10526880);
        	this.drawString(this.fontRenderer, "only entities.", this.width / 2 + 10, 160, 10526880);
        	break;
        case 1: 
        	this.drawString(this.fontRenderer, "The Explosion Strength will be set", this.width / 2 + 10, 150, 10526880);
        	this.drawString(this.fontRenderer, "according to the global settings.", this.width / 2 + 10, 160, 10526880);
        	this.drawString(this.fontRenderer, "( .minecraft/config/", this.width / 2 + 10, 170, 10526880);
        	this.drawString(this.fontRenderer, "blockphysics/"+BlockPhysics.conffile+" )", this.width / 2 + 10, 180, 10526880);
        	break;
        case 2: 
        	this.drawString(this.fontRenderer, "Explosion Strength: 100", this.width / 2 + 10, 150, 10526880);
        	break;
        case 3: 
        	this.drawString(this.fontRenderer, "Explosion Strength: 140", this.width / 2 + 10, 150, 10526880);
        }
       

        super.drawScreen(par1, par2, par3);
    }
}