package kcore;

public class Pair {
	public final int x;
	public final int y;
	public Pair (int f, int s){
		x = f;
		y = s;
	}	
	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (!(o instanceof Pair)) return false;
		Pair other = (Pair) o;
		if (other.x == this.x && other.y == this.y) return true;
		if (other.x == this.y && other.y == this.x) return true;
		else return false;
	}
	@Override
    public int hashCode() {
		int result = x;
        result = 31 * result + y;
        return result;
    }
	
}
