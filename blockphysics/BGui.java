package blockphysics;

import net.minecraft.world.WorldSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import java.io.File;
import java.util.List;

public class BGui
  extends GuiScreen
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
  
  public void c() {}
  
  public void A_()
  {
    this.startTime = System.currentTimeMillis();
    
    this.catapult = true;
    this.explfire = true;
    
    this.buttonList.clear();
    this.buttonList.add(this.catapultPiston = new GuiButton(9, this.width / 2 - 155, 90, 150, 20, "Catapult Piston:"));
    this.buttonList.add(this.physPreset = new GuiButton(10, this.width / 2 - 155, 120, 150, 20, "Moving Blocks:"));
    this.buttonList.add(this.explPhys = new GuiButton(11, this.width / 2 + 5, 120, 150, 20, "Explosion Strength:"));
    this.buttonList.add(this.explFire = new GuiButton(12, this.width / 2 + 5, 90, 150, 20, "Explosion Fire:"));
    String btxt;
    if (this.create) {
      btxt = "Create New World";
    } else {
      btxt = "Convert World";
    }
    this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, btxt));
    this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, "Back"));
    
    updateButtonText();
    this.createClicked = false;
  }
  
  private void updateButtonText()
  {
    if (this.catapult) {
      this.catapultPiston.displayString = "Catapult Piston: ON";
    } else {
      this.catapultPiston.displayString = "Catapult Piston: OFF";
    }
    if (this.explfire) {
      this.explFire.displayString = "Explosion Fire: ON";
    } else {
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
  
  public void b() {}
  
  protected void displayGuiScreen(GuiButton par1GuiButton)
  {
    if (par1GuiButton.enabled) {
      if (par1GuiButton.id == 1)
      {
        this.mc.displayGuiScreen(this.parentGuiScreen);
      }
      else if (par1GuiButton.id == 0)
      {
        this.mc.displayGuiScreen((GuiScreen)null);
        if (this.createClicked) {
          return;
        }
        this.createClicked = true;
        
        BlockPhysicsUtil.resetConfig();
        
        this.mc.launchIntegratedServer(this.folderName, this.wName, this.worldSettings);
        
        File confF = new File(BlockPhysics.gameDir + File.separator + "saves" + File.separator + this.folderName + File.separator + "blockphysics.cfg");
        BlockPhysicsUtil.compConfig(confF, this.catapult, this.explfire, this.explphys, this.physics);
        
        BlockPhysicsUtil.loadConfig(confF);
      }
      else if (par1GuiButton.id == 9)
      {
        this.catapult = (!this.catapult);
        updateButtonText();
      }
      else if (par1GuiButton.id == 10)
      {
        this.physics += 1;
        if (this.physics > 4) {
          this.physics = 0;
        }
        updateButtonText();
      }
      else if (par1GuiButton.id == 11)
      {
        this.explphys += 1;
        if (this.explphys > 3) {
          this.explphys = 0;
        }
        updateButtonText();
      }
      else if (par1GuiButton.id == 12)
      {
        this.explfire = (!this.explfire);
        updateButtonText();
      }
    }
  }
  
  protected void mouseClicked(int par1, int par2, int par3)
  {
    if (System.currentTimeMillis() - this.startTime > 250L) {
      super.mouseClicked(par1, par2, par3);
    }
  }
  
  public void drawScreen(int par1, int par2, float par3)
  {
    //TODO no clue what this is supposed to do: e();
    if (this.create)
    {
    	drawCenteredString(this.fontRenderer, "Configure BlockPhysics for World:", this.width / 2, 20, 16777215);
    	drawCenteredString(this.fontRenderer, this.wName, this.width / 2, 50, 10526880);
    }
    else
    {
    	drawCenteredString(this.fontRenderer, "Convert World [ " + this.wName + " ] to BlockPhysics", this.width / 2, 20, 16777215);
    	drawCenteredString(this.fontRenderer, "Warning! Last chance to backup this world!", this.width / 2, 50, 10526880);
    }
    switch (this.physics)
    {
    case 0: 
    	drawString(this.fontRenderer, "Nothing falls.", this.width / 2 - 170, 150, 10526880);
    	drawString(this.fontRenderer, "explosions and pistons", this.width / 2 - 170, 160, 10526880);
    	drawString(this.fontRenderer, "can push blocks.", this.width / 2 - 170, 170, 10526880);
      break;
    case 1: 
    	drawString(this.fontRenderer, "Only sand and gravel will fall,", this.width / 2 - 170, 150, 10526880);
    	drawString(this.fontRenderer, "explosions and pistons", this.width / 2 - 170, 160, 10526880);
    	drawString(this.fontRenderer, "can push blocks.", this.width / 2 - 170, 170, 10526880);
      break;
    case 2: 
    	drawString(this.fontRenderer, "The block's behavior will be set", this.width / 2 - 170, 150, 10526880);
    	drawString(this.fontRenderer, "according to the global settings.", this.width / 2 - 170, 160, 10526880);
    	drawString(this.fontRenderer, "( .minecraft/config/", this.width / 2 - 170, 170, 10526880);
    	drawString(this.fontRenderer, "blockphysics/blockphysics.cfg )", this.width / 2 - 170, 180, 10526880);
      break;
    case 3: 
    	drawString(this.fontRenderer, "Only sand and gravel will slide,", this.width / 2 - 170, 150, 10526880);
    	drawString(this.fontRenderer, "various blocks will fall.", this.width / 2 - 170, 160, 10526880);
      break;
    }
    switch (this.explphys)
    {
    case 0: 
    	drawString(this.fontRenderer, "Explosions will not affect blocks,", this.width / 2 + 10, 150, 10526880);
    	drawString(this.fontRenderer, "only entities.", this.width / 2 + 10, 160, 10526880);
      break;
    case 1: 
    	drawString(this.fontRenderer, "The Explosion Strength will be set", this.width / 2 + 10, 150, 10526880);
    	drawString(this.fontRenderer, "according to the global settings.", this.width / 2 + 10, 160, 10526880);
    	drawString(this.fontRenderer, "( .minecraft/config/", this.width / 2 + 10, 170, 10526880);
    	drawString(this.fontRenderer, "blockphysics/blockphysics.cfg )", this.width / 2 + 10, 180, 10526880);
      break;
    case 2: 
    	drawString(this.fontRenderer, "Explosion Strength: 100", this.width / 2 + 10, 150, 10526880);
      break;
    case 3: 
    	drawString(this.fontRenderer, "Explosion Strength: 140", this.width / 2 + 10, 150, 10526880);
    }
    super.drawScreen(par1, par2, par3);
  }
}