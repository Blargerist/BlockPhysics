package blockphysics;

public class BlockDef
{
  public int id;
  public int movenum;
  public int[] move = new int[2];
  public int moveflipnumber;
  public int movechanger;
  public int supportingblock;
  public int fragile;
  public boolean trapping;
  public int pushtype;
  public boolean randomtick;
  public int tickrate;
  public int placedmove;
  public int mass;
  public int strength;
  
  public BlockDef(int id1, int movenum1, int move0, int move1, int moveflipnumber1, int movechanger1, int supportingblock1, int fragile1, boolean trapping1, int pushtype1, boolean randomtick1, int tickrate1, int placedmove1, int mass1, int strength1)
  {
    this.id = id1;
    this.movenum = movenum1;
    this.move[0] = move0;
    this.move[1] = move1;
    this.moveflipnumber = moveflipnumber1;
    this.movechanger = movechanger1;
    this.supportingblock = supportingblock1;
    this.fragile = fragile1;
    this.trapping = trapping1;
    this.pushtype = pushtype1;
    this.randomtick = randomtick1;
    this.tickrate = tickrate1;
    this.placedmove = placedmove1;
    this.mass = mass1;
    this.strength = strength1;
  }
  
  public BlockDef(int id1)
  {
    this.id = id1;
    this.movenum = 0;
    this.move[0] = 0;
    this.move[1] = 0;
    this.moveflipnumber = 0;
    this.movechanger = 0;
    this.supportingblock = 0;
    this.fragile = 0;
    this.trapping = false;
    this.pushtype = 0;
    this.randomtick = false;
    this.tickrate = 10;
    this.placedmove = 0;
    this.mass = 1500;
    this.strength = 64000;
  }
  
  public static void copyBlockDef(BlockDef def1, BlockDef def2)
  {
    def1.movenum = def2.movenum;
    def1.move[0] = def2.move[0];
    def1.move[1] = def2.move[1];
    def1.moveflipnumber = def2.moveflipnumber;
    def1.movechanger = def2.movechanger;
    def1.supportingblock = def2.supportingblock;
    def1.fragile = def2.fragile;
    def1.trapping = def2.trapping;
    def1.pushtype = def2.pushtype;
    def1.randomtick = def2.randomtick;
    def1.tickrate = def2.tickrate;
    def1.placedmove = def2.placedmove;
    def1.mass = def2.mass;
    def1.strength = def2.strength;
  }
}