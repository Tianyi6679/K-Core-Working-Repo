package kcore;
import java.util.*;

public class node implements java.io.Serializable {
	public int index;
	public int degree;
	public int NoN;
	public boolean interested;
	public int fake_NoN;
	public int cd=0;
	public int vn=0;
	public ArrayList<node> edgelist = new ArrayList<node>();
	public ArrayList<node> neighbours= new ArrayList<node>();
	public ArrayList<node> fake_neighbours = new ArrayList<node>();
	public HashSet<node> neighbourset= new HashSet<node>();
	public void calculate_degree() { degree=edgelist.size(); return;}
	public void calculate_NoN() { NoN=neighbours.size(); return;}
	public static Comparator<node> sort_by_degree(){
		Comparator<node> cmp = new Comparator<node>() {
			@Override
			public int compare(node n1, node n2){
				if (n1.degree > n2.degree) return 1;
				if (n1.degree < n2.degree) return -1;
				return 0;
			}
		};
		return cmp;
	}
	public static Comparator<node> sort_by_NoN(){
		Comparator<node> cmp = new Comparator<node>() {
			@Override
			public int compare(node n1, node n2){
				if (n1.NoN > n2.NoN) return 1;
				if (n1.NoN < n2.NoN) return -1;
				return 0;
			}
		};
		return cmp;
	}
}

