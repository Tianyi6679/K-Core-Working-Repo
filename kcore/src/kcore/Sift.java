package kcore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Sift extends graph{
	public ArrayList<edge> edgelist1 = new ArrayList<edge>();
	public HashSet<node> visited = new HashSet<node>();
	public Hashtable<edge,Integer> edgetable= new Hashtable<edge,Integer>();
	public double epsilon;
	public Sift(int _k, int[] _core, Hashtable<Integer, Integer> _fid, List<node> _nodelist, double epsilon) {
		super(_k, _core, _fid, _nodelist);
		visited.clear();
		this.epsilon = epsilon;
		int count = 0;
        for (node i:nodelist){
        	i.fake_NoN=i.NoN;
        	i.fake_neighbours.addAll(0,i.neighbours);
        	for (node j: i.neighbours){
        		if (!visited.contains(j)) {
        			edge e = new edge(i,j);
        			edgelist1.add(e);
        			edgelist.add(e);
        			edgetable.put(e, count);
        			count++;
        		}
        	}
        	visited.add(i);
        }
        /*
        File file = new File("email_30_sbv.txt");
        try {
			Scanner in = new Scanner(file);
			int index;
			double contri;
			while (in.hasNextLine()){
				index = in.nextInt();
				contri = in.nextDouble();
				edgelist.get(index).contribution2=contri;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */
        deleted = new HashSet<node>();
	}
	
	public void Calculate_Contribution(int budget){
		int previous=0;
		System.out.println("Contribution calculation starts");
		long starttime = System.currentTimeMillis();
		System.out.println("# of Permutation = " + 1/(epsilon*epsilon)*Math.log(edgelist1.size()));
		for (int i =1; i <=1/(epsilon*epsilon)*Math.log(edgelist1.size()); i++){
			for (int j = 0; j <edgelist1.size(); j++){
				previous = deleted.size();
				edge e = edgelist1.get(j);
				//if (!deleted.contains(e.x) && !deleted.contains(e.y)){
					e.x.fake_neighbours.remove(e.y);
					e.y.fake_neighbours.remove(e.x);
					e.x.fake_NoN--;
					e.y.fake_NoN--;
					if (e.x.fake_NoN< k) {
						if (deleted.add(e.x)) Delete(e.x);
					}
					if (e.y.fake_NoN< k) {
						if (deleted.add(e.x)) Delete(e.x);
					}
					e.contribution+=(deleted.size()-previous);
					e.occurance+=1;
				//}
				
			}
			deleted.clear();
			for (node n:nodelist){
				n.fake_neighbours.clear();
				n.fake_neighbours.addAll(0,n.neighbours);
				n.fake_NoN=n.NoN;
			}
			Collections.shuffle(edgelist1);
			if ((i+1)%1000==0) System.out.println("1000 permutations " + "Time: " + (System.currentTimeMillis()-starttime)/1000);
		}
		System.out.println("Time: " + (System.currentTimeMillis()-starttime)/1000);
	}
	public void Calculate_Contribution_Alt(int budget){
		int previous=0;
		System.out.println("Contribution calculation starts");
		System.out.println("# of Permutation = " +1/(epsilon*epsilon) *Math.log(edgelist1.size()));
		int count;
		for (int i =1; i <=1/(epsilon*epsilon)*Math.log(edgelist1.size()); i++){
			count = 0;
			for (int j = 0; j<budget; j++){
				previous = deleted.size();
				edge e = edgelist1.get(count);
				if (!deleted.contains(e.x) && !deleted.contains(e.y)){
					e.x.fake_neighbours.remove(e.y);
					e.y.fake_neighbours.remove(e.x);
					e.x.fake_NoN--;
					e.y.fake_NoN--;
					if (e.x.fake_NoN< k) {
						deleted.add(e.x);
						Delete(e.x);
					}
					if (e.y.fake_NoN< k) {
						deleted.add(e.y);
						Delete(e.y);
					}
					e.contribution+=(deleted.size()-previous);
					if ((deleted.size()-previous) == 0) j--;
					e.occurance+=1;
				}
				else j--;
				count++;
				if (count>=edgelist1.size()) break;
			}
			deleted.clear();
			for (node n:nodelist){
				n.fake_neighbours.clear();
				n.fake_neighbours.addAll(0,n.neighbours);
				n.fake_NoN=n.NoN;
			}
			Collections.shuffle(edgelist1);
			if ((i+1)%100==0) System.out.println("100 permutations");
		}
	}
	
	public void Delete(node n){
		List<node> cand=new ArrayList<node>();
		for (node i:n.fake_neighbours){
			if (i.fake_neighbours.remove(n))
				i.fake_NoN--;
			if (i.fake_NoN < k && !deleted.contains(i)){
				deleted.add(i);
				cand.add(i);
			}
		}
		for (node i: cand) Delete(i);
	}
	
	private int next(int p){
		while (deleted.contains(edgelist1.get(p).x) || deleted.contains(edgelist1.get(p).y)) {
			p--; 
			if (p<0) break;
		}
		return p;
	}
	public void Pick(int budget, boolean alt, boolean extra_info){
		node cand1;
		node cand2;
		updated= new HashSet<node>(nodelist.size());
		long starttime = System.currentTimeMillis();
		if (alt) Calculate_Contribution_Alt(budget);
		else Calculate_Contribution(budget);
		System.out.println("Time: " + (System.currentTimeMillis()-starttime)/1000);
		for (edge e:edgelist1) {
			if (e.occurance!=0) e.contribution/= (double)e.occurance;
			//System.out.println(e.occurance);
		}
		edgelist1.sort(edge.sort_by_contri());
		deleted.clear();
		int p = edgelist1.size();
		int unique_node = 0;
		Hashtable<Integer, Integer> mark = new Hashtable<Integer,Integer>();
		Hashtable<Integer, Integer> degree = new Hashtable<Integer, Integer>();
		for (int b=1; b<=budget; b++){
			p = next(p-1);
			if (p<0) {
				System.out.println("Result: "+ deleted.size()/((float)nodelist.size()+deleted.size())*100+"%");
				return;
			}
			//System.out.println(edgelist1.get(p).contribution + " " + edgelist1.get(p).contribution2+ " X: "+ edgelist1.get(p).x.index+ " Y: "+edgelist1.get(p).y.index);
			cand1=edgelist1.get(p).x;
			cand2=edgelist1.get(p).y;
			cand1.neighbours.remove(cand2);
        	cand2.neighbours.remove(cand1);
    		cand1.NoN--;
    		cand2.NoN--;

    		if (core[fid.get(cand2.index)] > core[fid.get(cand1.index)]) cand1.cd--;
    		else if (core[fid.get(cand2.index)] == core[fid.get(cand1.index)]) {
    			cand2.cd--;
    			cand1.cd--;
    		}
    		else cand2.cd--;
    		updated.clear();
    		int prev = deleted.size();
    		if (cand1.NoN < k){
    			//System.out.println("delete cand1");
    			deleted.add(cand1);
    			remove_node(cand1);
    		}
    		else if (core[fid.get(cand1.index)]> cand1.cd){
    			//System.out.println("update cand1");
    			updated.add(cand1);
    			update(cand1);
    		}
    		if (cand2.NoN < k){
    			//System.out.println("delete cand2");
    			deleted.add(cand2);
    			remove_node(cand2);
    		}
    		else if (core[fid.get(cand2.index)]> cand2.cd){
    			//System.out.println("update cand2");
    			updated.add(cand2);
    			update(cand2);
    		}
    		if (!mark.containsKey(cand1.index)){
    			mark.put(cand1.index, 1);
    			degree.put(cand1.index, cand1.fake_NoN);
    			unique_node ++;
    		}
    		else mark.replace(cand1.index, mark.get(cand1.index)+1);
    		if (!mark.containsKey(cand2.index)){
    			mark.put(cand2.index, 1 );
    			degree.put(cand2.index, cand2.fake_NoN);
    			unique_node ++;
    		}
    		else mark.replace(cand2.index, mark.get(cand2.index)+1);
    		if ((b)%100==0){
    			System.out.println("Result: "+ deleted.size());
    			System.out.println("Result: "+ deleted.size()/((float)nodelist.size()+deleted.size())*100+"%");
    		}
			
		}
		System.out.println("Time: " + (System.currentTimeMillis()-starttime));
		System.out.println("Unique Nodes: " + unique_node);
		if (extra_info){
			System.out.println("Start printing contribution");
			PrintStream out;
			try {
				out = new PrintStream(new FileOutputStream("gowalla_sensitivity.txt"));
				System.setOut(out);
				for (int i : mark.keySet())
				{
					System.out.println((double)mark.get(i)/degree.get(i));
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
}
