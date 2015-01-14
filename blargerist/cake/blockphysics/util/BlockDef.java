package blargerist.cake.blockphysics.util;

public class BlockDef
{
	public String id;
	public boolean canMove;
	public String moveDef;
	public int moveChanger;		//0 no 1 weather 2 mechanical 3 both
	public boolean supportiveBlock;
	public int fragile;	// 0 no, 1 yes no drop, 2 yes drop
	public boolean trapping;
	public int pushType;      // 0 not pushable, 1 pushable by all, 2 pushable by piston, 3 pushable by explosion
	public int mass;			//1-40000
	public int strength;		//0-64000
	public boolean hurts;

	public BlockDef(String id1, boolean canMove1, String moveDef1, Boolean supportiveBlock1, int fragile1, boolean trapping1, int mass1, int strength1, boolean hurts1)
	{
		id = id1;
		canMove = canMove1;
		moveDef = moveDef1;
		supportiveBlock = supportiveBlock1;
		fragile = fragile1;
		trapping = trapping1;
		mass = mass1;
		strength = strength1;
		hurts = hurts1;
	}

	public BlockDef(String id1)
	{
		id = id1;
		canMove = false;
		moveDef = null;
		supportiveBlock = false;
		fragile = 0;
		trapping = false;
		mass = 1500;
		strength = 64000;
		hurts = true;
	}

	public static void copyBlockDef(BlockDef def1, BlockDef def2)
	{
		def1.canMove = def2.canMove;
		def1.moveDef = def2.moveDef;
		def1.moveChanger = def2.moveChanger;
		def1.supportiveBlock = def2.supportiveBlock;
		def1.fragile = def2.fragile;
		def1.trapping = def2.trapping;
		def1.pushType = def2.pushType;
		def1.mass = def2.mass;
		def1.strength = def2.strength;
		def1.hurts = def2.hurts;
	}
}