package kcore;

import java.util.Comparator;

public class edge {
	public node x;
	public node y;
	public double jacob;
	public double contribution;
	public double contribution2;
	public int occurance;
	public edge(node _x, node _y){
		this.x=_x;
		this.y=_y;
	}
	public static Comparator<edge> sort_by_jacob(){
		Comparator<edge> cmp = new Comparator<edge>() {
			@Override
			public int compare(edge n1, edge n2){
				if (n1.jacob > n2.jacob) return 1;
				if (n1.jacob < n2.jacob) return -1;
				return 0;
			}
		};
		return cmp;
	}
	public static Comparator<edge> sort_by_contri(){
		Comparator<edge> cmp = new Comparator<edge>() {
			@Override
			public int compare(edge n1, edge n2){
				if (n1.contribution > n2.contribution) return 1;
				if (n1.contribution < n2.contribution) return -1;
				return 0;
			}
		};
		return cmp;
	}
}
