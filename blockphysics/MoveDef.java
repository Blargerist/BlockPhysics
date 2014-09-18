package blockphysics;

public class MoveDef
{
  public int id;
  public int movetype;
  public int slidechance;
  public boolean ceiling;
  public int smallarc;
  public int bigarc;
  public int corbel;
  public int ncorbel;
  public int hanging;
  public int attached;
  public int[] floating;
  public boolean branch;
  
  public MoveDef(int id1, int movetype1, int slidechance1, boolean ceiling1, int smallarc1, int bigarc1, int corbel1, int ncorbel1, int hanging1, int attached1, int[] floating1, boolean branch1)
  {
    this.id = id1;
    this.movetype = movetype1;
    this.slidechance = slidechance1;
    this.ceiling = ceiling1;
    this.smallarc = smallarc1;
    this.bigarc = bigarc1;
    this.corbel = corbel1;
    this.ncorbel = ncorbel1;
    this.hanging = hanging1;
    this.attached = attached1;
    int s = floating1.length;
    this.floating = new int[s];
    for (int i = 0; i < s; i++) {
      this.floating[i] = floating1[i];
    }
    this.branch = branch1;
  }
  
  public MoveDef(int id1)
  {
    this.id = id1;
    this.movetype = 0;
    this.slidechance = 0;
    this.ceiling = false;
    this.smallarc = 0;
    this.bigarc = 0;
    this.corbel = 0;
    this.ncorbel = 0;
    this.hanging = 0;
    this.attached = 0;
    this.floating = new int[1];
    this.floating[0] = 0;
    this.branch = false;
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
    int s = def2.floating.length;
    def1.floating = new int[s];
    for (int i = 0; i < s; i++) {
      def1.floating[i] = def2.floating[i];
    }
    def1.branch = def2.branch;
  }
}