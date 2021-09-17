package kcore;

public class vertice {
	private int degree;
	private int index;
	public vertice(){degree=0; index=0;}
	public void degree_up() {degree++;}	
	public void degree_down() {degree--;}
	public int get_degree(){return degree;}
	public int get_index() {return index;}
	public void set_index(int i) { index=i;}
}


