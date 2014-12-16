package blargerist.cake.blockphysics;

import java.nio.ByteBuffer;


public class BlockDef      
{
	public int id;
	public int movenum;				// 0,1,2
	public int[] move = new int[2];	// 0-511
	public int moveflipnumber;		// 0-15
	public int movechanger;		//0 no 1 weather 2 mechanical 3 both
	public int supportingblock; // 0 no 1 yes
	public int	fragile;	// 0 no, 1 yes no drop, 2 yes drop
	public boolean trapping;	//0,1
	public int pushtype;      // 0 not pushable, 1 pushable by all, 2 pushable by piston, 3 pushable by explosion
	public boolean randomtick; //0,1
	public int tickrate;     //0-255
	public int placedmove;		//0,1
	public int mass;			//1-40000
	public int strength;		//0-64000
		
	public BlockDef(int id1, int movenum1, int move0, int move1, int moveflipnumber1, int movechanger1, int supportingblock1, int fragile1, boolean trapping1, int pushtype1, boolean randomtick1, int tickrate1, int placedmove1, int mass1, int strength1 )
	{
		id = id1;
		movenum = movenum1;	
		move[0] = move0;	
		move[1] = move1;
		moveflipnumber = moveflipnumber1;
		movechanger = movechanger1;
		supportingblock = supportingblock1;
		fragile = fragile1;
		trapping = trapping1;
		pushtype = pushtype1;
		randomtick = randomtick1;
		tickrate = tickrate1;
		placedmove = placedmove1;
		mass = mass1;
		strength = strength1;
	}


	public BlockDef(int id1) 
	{
		id = id1;
		movenum = 0;	
		move[0] = 0;	
		move[1] = 0;
		moveflipnumber = 0;
		movechanger = 0;
		supportingblock = 0;
		fragile = 0;
		trapping = false;
		pushtype = 0;
		randomtick = false;
		tickrate = 10;
		placedmove = 0;
		mass = 1500;
		strength = 64000;
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