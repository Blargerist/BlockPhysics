package blargerist.pie.blockphysics.util;

public class MoveDef
{
	public int id;
	public int movetype;		// 0  static, 1 fall, 2 slide + fall, 3 drop
	public int slidechance;		// chance to slide 0-100
	public boolean ceiling;		// 0,1
	public int smallarc;		// 0-6
	public int bigarc;			// 0-6
	public int corbel;			// 0-6
	public int ncorbel;			// 0-6
	public int hanging;			// 0-10
	public int attached;		// 0-6
	public int floatingradius;
	public String floatingblock;
	public int floatingmeta;
	public boolean branch;		// 0,1
	
	public MoveDef(int id1, int movetype1, int slidechance1, boolean ceiling1, int smallarc1, int bigarc1, int corbel1, int ncorbel1, int hanging1, int attached1, int floatingradius1, String floatingblock1, int floatingmeta1, boolean branch1)
	{
		id = id1;
		movetype = movetype1;
		slidechance = slidechance1;
		ceiling = ceiling1;
		smallarc = smallarc1;
		bigarc = bigarc1;
		corbel = corbel1;
		ncorbel = ncorbel1;
		hanging = hanging1;
		attached = attached1;
		floatingradius = floatingradius1;
		floatingblock = floatingblock1;
		floatingmeta = floatingmeta1;
		branch = branch1;
	}
	
	public MoveDef(int id1)
	{
		id = id1;
		movetype = 0;
		slidechance = 0;
		ceiling = false;
		smallarc = 0;
		bigarc = 0;
		corbel = 0;
		ncorbel = 0;
		hanging = 0;
		attached = 0;
		floatingradius = 0;
		floatingblock = null;
		floatingmeta = 0;
		branch = false;
	}
	
	public static void copyMoveDef(MoveDef def1, MoveDef def2)
	{
		def1.movetype = def2.movetype;
		def1.slidechance = def2.slidechance;
		def1.ceiling = def2.ceiling;
		def1.smallarc = def2.smallarc;
		def1.bigarc = def2.bigarc;
		def1.corbel = def2.corbel;
		def1.ncorbel = def2.ncorbel;
		def1.hanging = def2.hanging;
		def1.attached = def2.attached;
		def1.floatingradius = def2.floatingradius;
		def1.floatingblock = def2.floatingblock;
		def1.floatingmeta = def2.floatingmeta;
		def1.branch = def2.branch;
	}
}