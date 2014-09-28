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

import java.nio.ByteBuffer;


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
	public int[] floating;
	public boolean branch;		// 0,1
	
	public MoveDef( int id1, int movetype1, int slidechance1, boolean ceiling1, int smallarc1, int bigarc1, int corbel1, int ncorbel1, int hanging1, int attached1, int floating1[], boolean branch1 )
	{
		id = id1;
		movetype = movetype1;
		slidechance = slidechance1;
		ceiling	= ceiling1;
		smallarc = smallarc1;
		bigarc = bigarc1;
		corbel = corbel1;
		ncorbel = ncorbel1;
		hanging = hanging1;
		attached = attached1;
		int s = floating1.length;
		floating = new int[s];
		for (int i=0; i < s; i++) floating[i] = floating1[i];
		branch = branch1;
	}
	
	public MoveDef(int id1)
	{
		id = id1;
		movetype = 0;
		slidechance = 0;
		ceiling	= false;
		smallarc = 0;
		bigarc = 0;
		corbel = 0;
		ncorbel = 0;
		hanging = 0;
		attached = 0;
		floating = new int[1];
		floating[0] = 0;
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
		int s = def2.floating.length;
		def1.floating = new int[s];
		for (int i=0; i < s; i++) def1.floating[i] = def2.floating[i];
		def1.branch = def2.branch;
	}
}

