package kcore;

import java.util.Comparator;
import java.util.Hashtable;

public class Sortbycore implements Comparator<node>{
	int[] core;
	Hashtable<Integer, Integer> fid;
	public Sortbycore(int[] core, Hashtable<Integer, Integer> fid){ this.core = core; this.fid = fid;}
	public int compare(node a, node b){
		return core[fid.get(a.index)] - core[fid.get(b.index)];
	}
	
}