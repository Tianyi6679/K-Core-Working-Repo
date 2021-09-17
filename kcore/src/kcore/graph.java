package kcore;

import java.time.Year;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
public class graph implements java.io.Serializable{
	public int k;
	//public transient int step;
	public Hashtable<Integer, Integer> fid;
	public int[] core;
	public transient List<node> nodelist;
	public transient List<edge> edgelist;
	public transient HashSet<node> vulnerable, deleted, updated; 
	public transient HashSet<edge> ee; 
	public transient HashSet<node> en;
	public transient HashMap<Pair,edge> edgemap = new HashMap();
	public graph(int _k, int [] _core, Hashtable<Integer, Integer> _fid, List<node> _nodelist){
		k=_k;
		core=_core;
		fid=_fid;
		nodelist=_nodelist;
		edgelist = new ArrayList<edge>();
	}

	public int vulnerableN(node n)
	{
		int count=0;
		for(node m: n.neighbours)
			if (vulnerable.contains(m) && core[fid.get(m.index)]==core[fid.get(n.index)]) count++; 
		return count;
	}
	
	public void dfs(node n){
		List<node> cand=new ArrayList<node>();
		int core_i = 0;
		int core_n = core[fid.get(n.index)];
		for (node i:n.neighbours){
			core_i = core[fid.get(i.index)];
			if (!vulnerable.contains(i) && core_i == core_n && cd(i) - vulnerableN(i) < core_i) { 
				vulnerable.add(i);
				cand.add(i);
			}
		}
		for (node i:cand) dfs(i);
		return;
	}
	public void dfs_n(node n){
		List<node> cand=new ArrayList<node>();
		int core_i = 0;
		int core_n = core[fid.get(n.index)];
		for (node i:n.neighbours){
			core_i = core[fid.get(i.index)];
			//i.vn++;
			if (!vulnerable.contains(i) && core_i == core_n && (i.cd - vulnerableN(i)) < core_i) { 
				vulnerable.add(i);
				if (i.cd == core_i && !en.contains(i) ) en.add(i);
				cand.add(i);
			}
		}
		for (node i:cand) dfs_n(i);
		return;
	}
	/*
	public void dfs_e(node n){
		List<node> cand=new ArrayList<node>();
		cand.clear();
		int core_i = 0;
		int core_n = core[fid.get(n.index)];
		for (node i:n.neighbours){
			core_i = core[fid.get(i.index)];
			if (!vulnerable.contains(i) && core_i == core_n && (i.NoN - vulnerableN(i)) < core_i) { 
				ee.add(edgemap.get(new Pair(i.index,n.index)));
				ee.add(edgemap.get(new Pair(n.index,i.index)));
				vulnerable.add(i);
				cand.add(i);
			}
		}
		for (node i:cand) dfs_e(i);
		return;
	}*/
	
	public int cd(node n){
		int count=0;
		int core_n = core[fid.get(n.index)];
		for(node i :n.neighbours)
			if (core[fid.get(i.index)] >= core_n) count++;
		return count;
	}
	public void update(node n){
		List<node> cand=new ArrayList<node>();
		core[fid.get(n.index)]--;
		for (node i:n.neighbours)
			if (core[fid.get(i.index)] == core[fid.get(n.index)]+1){
				i.cd--; 
				if (!updated.contains(i) && i.cd < core[fid.get(i.index)])
				{ 
					updated.add(i);
					cand.add(i);
				}
			}
			else if (core[fid.get(i.index)] == core[fid.get(n.index)]) n.cd++;
		for (node i:cand){
			update(i);
		}
	}
	public void remove_node(node n){
		List<node> cand=new ArrayList<node>();
		nodelist.remove(n);
		core[fid.get(n.index)]--;
		for (node i: n.neighbours){
			if (i.neighbours.remove(n)){
				i.NoN--;
			}
			if (core[fid.get(i.index)] == core[fid.get(n.index)]+1) {
				i.cd--; 
				if (!deleted.contains(i) && i.NoN < k){
					deleted.add(i);
					cand.add(i);
				}
			}
		}
		for (node i:cand){
			remove_node(i);
		}
		return;
	}
	public void equiedge(int budget){
		int count = 0;
		int temp = 0;
		deleted = new HashSet<node>();
	    HashSet<node> cand=new HashSet<node>();
	    deleted.clear();
	    cand.clear();
	    long startTime2 = System.currentTimeMillis();
		for (int b=0; b<budget; b++)
		{
			int rand1=ThreadLocalRandom.current().nextInt(0,nodelist.size());
			node n=nodelist.get(rand1);
			int rand2=ThreadLocalRandom.current().nextInt(0,nodelist.get(rand1).neighbours.size());
			node m=n.neighbours.get(rand2);
			m.neighbours.remove(n);
			m.NoN--;
			n.neighbours.remove(m);
			n.NoN--;
			if ((b+1)%100 == 0){
				nodelist.sort(node.sort_by_NoN());
				for (node i: nodelist){
					core[fid.get(i.index)] = i.NoN;
					if (i.NoN < k)
					{
	    				deleted.add(i);
	    				cand.add(i);
	    			}
					for (node j:i.neighbours){
						if (j.NoN > i.NoN){ 
							j.NoN--;
							temp=nodelist.indexOf(j);
							if (temp!=0)
								while (temp>0 && nodelist.get(temp).NoN<nodelist.get(temp-1).NoN )
								{
									Collections.swap(nodelist, temp-1, temp);
									temp--;
								}
						}
					}
				}
				for(node i: cand) {
	    			nodelist.remove(i);
	    			for (node j :i.neighbours){
	    				j.neighbours.remove(i);
	    			}
	    		}
	    		cand.clear();
				for(node i: nodelist) i.calculate_NoN();
				System.out.println("Result: "+deleted.size());
    			System.out.println("Time: " + (System.currentTimeMillis()-startTime2)/1000);
    			System.out.println("Result: "+deleted.size()/((float)nodelist.size()+deleted.size())*100+"%");

	 		}
			
		}
	 }	
	 public void equinode(int budget){
		 	node cand1=nodelist.get(0);
	        node cand2=cand1.neighbours.get(0);
	        int max;
	        vulnerable=new HashSet<node>();
	        en=new HashSet<node>(nodelist.size());
	        updated= new HashSet<node>(nodelist.size());
	        deleted= new HashSet<node>(nodelist.size());
	        vulnerable.clear();
	        deleted.clear();
	        long startTime = System.currentTimeMillis();
	        for (int b=0; b<budget; b++){
	        	max=-1;
	        	en.clear();
	        	nodelist.sort(node.sort_by_NoN());
	        	
	        	for (node i: nodelist)
	        		if(core[fid.get(i.index)] == i.cd && !en.contains(i) ){
	        				en.add(i);
	        				vulnerable.add(i);
	     	    		   	dfs_n(i);
	     	    		   	if (vulnerable.size() > max ){
	     	    		   		cand1 = i;
	     	    		   		for (node j : i.neighbours) if (core[fid.get(j.index)] >= core[fid.get(i.index)]) cand2 = j;
	     	    		   		max = vulnerable.size();
	     	    		   		if (max==nodelist.size()) { vulnerable.clear(); break;}
	     	    		   	}
	     	    		   	vulnerable.clear();
	     	    		   	//for (node j: nodelist) j.vn =0;
	        	}
	        	cand1.neighbours.remove(cand2);
	        	cand2.neighbours.remove(cand1);
	    		cand1.NoN--;
	    		cand2.NoN--;
	    		cand1.cd--;
	    		if (core[fid.get(cand2.index)] == core[fid.get(cand1.index)]) cand2.cd--;
	    		updated.clear();
	    		
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
	    		//nodelist.sort(node.sort_by_NoN());
	    		//System.out.println(nodelist.size()+" "+max+" "+cand1.index+" "+cand2.index);
	    		if ((b+1)%100==0){
	    			System.out.println("Result: "+deleted.size());
	    			System.out.println("Time: " + (System.currentTimeMillis()-startTime)/1000);
	    			System.out.println("Result: "+deleted.size()/((float)nodelist.size()+deleted.size())*100+"%");
	    		}
	        }
	 }
	 
	 public void vulnerable(int budget){
		 node cand1=nodelist.get(0);
	     node cand2=cand1.neighbours.get(0);
	     int max;
	     int temp = 0;
	     vulnerable = new HashSet<node>();
	     deleted = new HashSet<node>();
	     HashSet<node> cand=new HashSet<node>();
	     vulnerable.clear();   
	     deleted.clear();
	     cand.clear();
	     long startTime2 = System.currentTimeMillis();
	     for (int b=0; b<budget; b++){
	       max=-1;
	       for (node i: nodelist){
	    	   if(core[fid.get(i.index)] == cd(i)){
	    		   vulnerable.add(i);
	    		   dfs(i);
	    		   if (vulnerable.size() > max ){
	    			   cand1 = i;
	    			   for (node j : i.neighbours) if (core[fid.get(j.index)] >= core[fid.get(i.index)]) cand2 = j;
	    			   max = vulnerable.size();
	    		   }
	    		  vulnerable.clear();
	    	   }
	       }
	      
	        	cand1.neighbours.remove(cand2);
	        	cand2.neighbours.remove(cand1);
	        	cand1.NoN--;
	        	cand2.NoN--;
	   
	 
	    		nodelist.sort(node.sort_by_NoN());
	    		for (node i: nodelist){
	    			core[fid.get(i.index)] = i.NoN;
	    			if (i.NoN < k) 
	    			{
	    				deleted.add(i);
	    				cand.add(i);
	    			}
	    			for (node j:i.neighbours){
	    				if (j.NoN > i.NoN){ 
	    					j.NoN--;
	    					temp=nodelist.indexOf(j);
	    					if (temp!=0)
	    						while (temp>0 && nodelist.get(temp).NoN<nodelist.get(temp-1).NoN )
	    						{
	    							Collections.swap(nodelist, temp-1, temp);
	    							temp--;
	    						}
	    				}
	    			}
	    		}
	    		if (!cand.isEmpty()) System.out.println("delete");
	    		for(node i: cand) {
	    			nodelist.remove(i);
	    			for (node j :i.neighbours){
	    				j.neighbours.remove(i);
	    			}
	    		}
	    		cand.clear();
	    		for(node i: nodelist) i.calculate_NoN();
	    		System.out.println(max+" "+cand1.index+" "+cand2.index);
	    		if ((b+1)%budget==0){
	    			long stopTime2=System.currentTimeMillis();
	    			System.out.println("Time: "+Long.toString((stopTime2-startTime2)/1000)+" Result: "+deleted.size());
	    		}
	         }
	     //for (node i: deleted) System.out.println(core[fid.get(i.index)]);
	 }
	 public void lowest_degree(int budget){
		 //int total=0;
		 deleted=new HashSet<node>();
		 updated= new HashSet<node>(nodelist.size());
		 deleted.clear();
		 long startTime2 = System.currentTimeMillis();
		 for (int b=0; b<budget; b++){
			node cand1=nodelist.get(0);
			node cand2=cand1.neighbours.get(0);
			cand1.neighbours.remove(cand2);
        	cand2.neighbours.remove(cand1);
    		cand1.NoN--;
    		cand2.NoN--;
    		cand1.cd--;
    		if (core[fid.get(cand2.index)] == core[fid.get(cand1.index)]) cand2.cd--;
    		updated.clear();
    		
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

			nodelist.sort(node.sort_by_NoN());
			if ((b+1)%100==0){
    			//System.out.println("Time: "+Long.toString((stopTime2-startTime2)/1000)+" "+(b+1)/4400+"% Result: "+deleted.size()/(float)12836*100+"%");
    			System.out.println("Result: "+ deleted.size());
    			System.out.println("Time: " + (System.currentTimeMillis()-startTime2)/1000);
    			System.out.println("Result: "+deleted.size()/((float)nodelist.size()+deleted.size())*100+"%");

			}
		}
	
	 }
	 public void jacob(int budget){
	     int numerator=0;
	     int denominator=0;
	     HashSet<node> intersection= new HashSet<node>();
	     deleted=new HashSet<node>();
		 deleted.clear();
		 long startTime2 = System.currentTimeMillis();
	        for (edge e:edgelist){
	        	intersection.addAll(e.x.neighbourset);
	        	intersection.retainAll(e.y.neighbourset);
	        	numerator=intersection.size();
	        	denominator=e.x.neighbourset.size()+e.y.neighbourset.size()-numerator*2;
	        	e.jacob=numerator/denominator;
	        	intersection.clear();
	        }
	    	edgelist.sort(edge.sort_by_jacob());
	    	node n,m;
	    	int b=0;
	    	int i=0;
	    	while (b<budget){
	    		while (deleted.contains(edgelist.get(i).x) || deleted.contains(edgelist.get(i).y)) i++;
	    		b++;
	    		n = edgelist.get(i).x;
	    		m = edgelist.get(i).y;
	    		i++;
	    		//System.out.println(n.index+" "+m.index);
	    		if (n.neighbours.remove(m)) { n.NoN--;}
	    		if (m.neighbours.remove(n)) { m.NoN--;}
	    		if (m.NoN<k) {
	    			deleted.add(m);
	    			remove_node(m);
	    		}
	    		if (n.NoN<k) {
	    			deleted.add(n);
	    			remove_node(n);
	    		}
				if ((b+1)%100==0){
					//System.out.println((b+1)/4400+"% Result: "+deleted.size()/(float)12836*100+"%");
					System.out.println("Result: "+deleted.size());
	    			System.out.println("Time: " + (System.currentTimeMillis()-startTime2)/1000);
	    			System.out.println("Result: "+deleted.size()/((float)nodelist.size()+deleted.size())*100+"%");
	    		}
	    	}
	 }
}
