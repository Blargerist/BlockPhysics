package blargerist.cake.blockphysics.util;

public class MoveDef
{
	public String id;
	public int moveType;		// 0  static, 1 fall, 2 slide + fall, 3 drop
	public int slideChance;		// chance to slide 0-100
	public boolean ceiling;		// 0,1
	public int smallArc;		// 0-6
	public int bigArc;			// 0-6
	public int corbel;			// 0-6
	public int nCorbel;			// 0-6
	public int hanging;			// 0-10
	public int attached;		// 0-6
	public int floatingRadius;
	public String floatingBlock;
	public int floatingMeta;
	public boolean branch;		// 0,1
	public int tree;
	
	public MoveDef(String id1, int moveType1, int slideChance1, boolean ceiling1, int smallArc1, int bigArc1, int corbel1, int nCorbel1, int hanging1, int attached1, int floatingRadius1, String floatingBlock1, int floatingMeta1, boolean branch1, int tree1)
	{
		id = id1;
		moveType = moveType1;
		slideChance = slideChance1;
		ceiling = ceiling1;
		smallArc = smallArc1;
		bigArc = bigArc1;
		corbel = corbel1;
		nCorbel = nCorbel1;
		hanging = hanging1;
		attached = attached1;
		floatingRadius = floatingRadius1;
		floatingBlock = floatingBlock1;
		floatingMeta = floatingMeta1;
		branch = branch1;
		tree = tree1;
	}
	
	public MoveDef(String id1)
	{
		id = id1;
		moveType = 0;
		slideChance = 0;
		ceiling = false;
		smallArc = 0;
		bigArc = 0;
		corbel = 0;
		nCorbel = 0;
		hanging = 0;
		attached = 0;
		floatingRadius = 0;
		floatingBlock = null;
		floatingMeta = 0;
		branch = false;
		tree = 0;
	}
	
	public static void copyMoveDef(MoveDef def1, MoveDef def2)
	{
		def1.moveType = def2.moveType;
		def1.slideChance = def2.slideChance;
		def1.ceiling = def2.ceiling;
		def1.smallArc = def2.smallArc;
		def1.bigArc = def2.bigArc;
		def1.corbel = def2.corbel;
		def1.nCorbel = def2.nCorbel;
		def1.hanging = def2.hanging;
		def1.attached = def2.attached;
		def1.floatingRadius = def2.floatingRadius;
		def1.floatingBlock = def2.floatingBlock;
		def1.floatingMeta = def2.floatingMeta;
		def1.branch = def2.branch;
		def1.tree = def2.tree;
	}
}