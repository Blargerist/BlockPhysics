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
    
    this.i.clear();
    this.i.add(this.catapultPiston = new GuiButton(9, this.g / 2 - 155, 90, 150, 20, "Catapult Piston:"));
    this.i.add(this.physPreset = new GuiButton(10, this.g / 2 - 155, 120, 150, 20, "Moving Blocks:"));
    this.i.add(this.explPhys = new GuiButton(11, this.g / 2 + 5, 120, 150, 20, "Explosion Strength:"));
    this.i.add(this.explFire = new GuiButton(12, this.g / 2 + 5, 90, 150, 20, "Explosion Fire:"));
    String btxt;
    String btxt;
    if (this.create) {
      btxt = "Create New World";
    } else {
      btxt = "Convert World";
    }
    this.i.add(new GuiButton(0, this.g / 2 - 155, this.h - 28, 150, 20, btxt));
    this.i.add(new GuiButton(1, this.g / 2 + 5, this.h - 28, 150, 20, "Back"));
    
    updateButtonText();
    this.createClicked = false;
  }
  
  private void updateButtonText()
  {
    if (this.catapult) {
      this.catapultPiston.f = "Catapult Piston: ON";
    } else {
      this.catapultPiston.f = "Catapult Piston: OFF";
    }
    if (this.explfire) {
      this.explFire.f = "Explosion Fire: ON";
    } else {
      this.explFire.f = "Explosion Fire: OFF";
    }
    switch (this.physics)
    {
    case 0: 
      this.physPreset.f = "Moving Blocks: OFF";
      break;
    case 1: 
      this.physPreset.f = "Moving Blocks: Vanilla";
      break;
    case 2: 
      this.physPreset.f = "Moving Blocks: Default";
      break;
    case 3: 
      this.physPreset.f = "Moving Blocks: Easy";
      break;
    case 4: 
      this.physPreset.f = "Moving Blocks: Hard";
    }
    switch (this.explphys)
    {
    case 0: 
      this.explPhys.f = "Explosion Strength: OFF";
      break;
    case 1: 
      this.explPhys.f = "Explosion Strength: Default";
      break;
    case 2: 
      this.explPhys.f = "Explosion Strength: High";
      break;
    case 3: 
      this.explPhys.f = "Explosion Strength: Griefing";
    }
  }
  
  public void b() {}
  
  protected void a(GuiButton par1GuiButton)
  {
    if (par1GuiButton.h) {
      if (par1GuiButton.g == 1)
      {
        this.f.a(this.parentGuiScreen);
      }
      else if (par1GuiButton.g == 0)
      {
        this.f.a((GuiScreen)null);
        if (this.createClicked) {
          return;
        }
        this.createClicked = true;
        
        BlockPhysicsUtil.resetConfig();
        
        this.f.a(this.folderName, this.wName, this.worldSettings);
        
        File confF = new File(BlockPhysics.gameDir + File.separator + "saves" + File.separator + this.folderName + File.separator + "blockphysics.cfg");
        BlockPhysicsUtil.compConfig(confF, this.catapult, this.explfire, this.explphys, this.physics);
        
        BlockPhysicsUtil.loadConfig(confF);
      }
      else if (par1GuiButton.g == 9)
      {
        this.catapult = (!this.catapult);
        updateButtonText();
      }
      else if (par1GuiButton.g == 10)
      {
        this.physics += 1;
        if (this.physics > 4) {
          this.physics = 0;
        }
        updateButtonText();
      }
      else if (par1GuiButton.g == 11)
      {
        this.explphys += 1;
        if (this.explphys > 3) {
          this.explphys = 0;
        }
        updateButtonText();
      }
      else if (par1GuiButton.g == 12)
      {
        this.explfire = (!this.explfire);
        updateButtonText();
      }
    }
  }
  
  protected void a(int par1, int par2, int par3)
  {
    if (System.currentTimeMillis() - this.startTime > 250L) {
      super.a(par1, par2, par3);
    }
  }
  
  public void a(int par1, int par2, float par3)
  {
    e();
    if (this.create)
    {
      a(this.o, "Configure BlockPhysics for World:", this.g / 2, 20, 16777215);
      a(this.o, this.wName, this.g / 2, 50, 10526880);
    }
    else
    {
      a(this.o, "Convert World [ " + this.wName + " ] to BlockPhysics", this.g / 2, 20, 16777215);
      a(this.o, "Warning! Last chance to backup this world!", this.g / 2, 50, 10526880);
    }
    switch (this.physics)
    {
    case 0: 
      b(this.o, "Nothing falls.", this.g / 2 - 170, 150, 10526880);
      b(this.o, "explosions and pistons", this.g / 2 - 170, 160, 10526880);
      b(this.o, "can push blocks.", this.g / 2 - 170, 170, 10526880);
      break;
    case 1: 
      b(this.o, "Only sand and gravel will fall,", this.g / 2 - 170, 150, 10526880);
      b(this.o, "explosions and pistons", this.g / 2 - 170, 160, 10526880);
      b(this.o, "can push blocks.", this.g / 2 - 170, 170, 10526880);
      break;
    case 2: 
      b(this.o, "The block's behavior will be set", this.g / 2 - 170, 150, 10526880);
      b(this.o, "according to the global settings.", this.g / 2 - 170, 160, 10526880);
      b(this.o, "( .minecraft/config/", this.g / 2 - 170, 170, 10526880);
      b(this.o, "blockphysics/blockphysics.cfg )", this.g / 2 - 170, 180, 10526880);
      break;
    case 3: 
      b(this.o, "Only sand and gravel will slide,", this.g / 2 - 170, 150, 10526880);
      b(this.o, "various blocks will fall.", this.g / 2 - 170, 160, 10526880);
      break;
    }
    switch (this.explphys)
    {
    case 0: 
      b(this.o, "Explosions will not affect blocks,", this.g / 2 + 10, 150, 10526880);
      b(this.o, "only entities.", this.g / 2 + 10, 160, 10526880);
      break;
    case 1: 
      b(this.o, "The Explosion Strength will be set", this.g / 2 + 10, 150, 10526880);
      b(this.o, "according to the global settings.", this.g / 2 + 10, 160, 10526880);
      b(this.o, "( .minecraft/config/", this.g / 2 + 10, 170, 10526880);
      b(this.o, "blockphysics/blockphysics.cfg )", this.g / 2 + 10, 180, 10526880);
      break;
    case 2: 
      b(this.o, "Explosion Strength: 100", this.g / 2 + 10, 150, 10526880);
      break;
    case 3: 
      b(this.o, "Explosion Strength: 140", this.g / 2 + 10, 150, 10526880);
    }
    super.a(par1, par2, par3);
  }
}