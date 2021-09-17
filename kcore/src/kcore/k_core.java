package kcore;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class k_core {
	public static void main(String[] args) {
		//initialization
		String name="C:/Users/nh54762/Desktop/research/ER_60k_1m .txt";
		File file= new File(name);
		List<node> graph=new ArrayList<node>();
		Hashtable<Integer, Integer> fid=new Hashtable<Integer, Integer>();
		System.out.println("start reading");
		//read edges and compute the degree of each vertex
		try{
			Scanner in = new Scanner(file);
            //String line = in.nextLine();
			int a,b,id;
			node n1,n2,u,v;
			id=0;
		while (in.hasNextLine())
		{
			try{
				a = in.nextInt();
				//System.out.println(a);
				b = in.nextInt();
				in.nextLine();
				//System.out.println(b);
				if (!fid.containsKey(a)){
					fid.put(a,id);
					id++;
					n1=new node();
					n1.index=a;
					graph.add(n1);
				}
				if (!fid.containsKey(b)){
					fid.put(b,id);
					id++;
					n2=new node();
					n2.index=b;
					graph.add(n2);
				}
				u=graph.get(fid.get(a));
				v=graph.get(fid.get(b));
				if (!u.edgelist.contains(v)) { u.edgelist.add(v); u.degree++;}
				if (!v.edgelist.contains(u)) { v.edgelist.add(u); v.degree++;}
			}
			catch (InputMismatchException e)
            {
                System.out.println("File not found1.");

            }
            catch (NoSuchElementException e)
            {
                System.out.println("File not found2");

            }
		}
		}
		catch (FileNotFoundException e)
        {
            System.out.println("File not found");
        }
		System.out.println("done!");
		System.out.println("start sorting");
		long startTime1 = System.currentTimeMillis();
		graph.sort(node.sort_by_degree());
		System.out.println("done!");
		int[] core= new int[fid.size()];
		int temp=0;
		node n1;
		System.out.println(graph.size());
		for (int i=0; i<graph.size(); i++){
			if( i%10000 ==0 ) System.out.println(i);
			core[fid.get(graph.get(i).index)]=graph.get(i).degree;
			for (int j=0; j<graph.get(i).edgelist.size(); j++){
				n1 = graph.get(i).edgelist.get(j);
				if (n1.degree > graph.get(i).degree){
					n1.degree--;
					temp=graph.indexOf(n1);
					if (temp!=0)
						while (temp>0 && graph.get(temp).degree<graph.get(temp-1).degree )
						{
							Collections.swap(graph, temp-1, temp);
							temp--;
						}
				}
			}
		}
		long stopTime1 = System.currentTimeMillis();
		System.out.println((stopTime1-startTime1)/1000);
		
		
		//long startTime2 = System.currentTimeMillis();
		List<node> subgraph= new ArrayList<node>();
		
		subgraph.clear();
		int targetC = 6;
		//int count;
		Collections.reverse(graph);
		//initialize subgraph
        for (node i: graph) if (core[fid.get(i.index)]>=targetC) 
        	{subgraph.add(i); i.neighbours.clear(); i.NoN=0;}
        for (node i: subgraph) 
        	for (node j: i.edgelist)
        		if (subgraph.contains(j)) {i.neighbours.add(j); i.NoN++;}
        for (node i: subgraph) { i.neighbours.sort(node.sort_by_NoN()); i.interested=true;}
		//System.out.println(subgraph.size());
		/*
        //random
        for (int i=0; i<budget; i++)
		{
			int rand1=ThreadLocalRandom.current().nextInt(0,subgraph.size());
			node n=subgraph.get(rand1);
			int rand2=ThreadLocalRandom.current().nextInt(0,subgraph.get(rand1).neighbours.size());
			node m=n.neighbours.get(rand2);
			m.neighbours.remove(n);
			m.NoN--;
			n.neighbours.remove(m);
			n.NoN--;
			//expend the subgraph
		}
		*/
		//random
		/*
		//degree
		//System.out.println(subgraph.size());
        subgraph.sort(node.sort_by_NoN());
        //System.out.println(count);
        int rand;
		for (int i=0; i<budget; i++)
		{
			node n=subgraph.get(subgraph.size()-1);
			//n.neighbours.sort(node.sort_by_NoN());
			rand=ThreadLocalRandom.current().nextInt(0,n.neighbours.size());
			node m=n.neighbours.get(rand);
			m.neighbours.remove(n);
			n.neighbours.remove(m);
			m.NoN--;
			n.NoN--;
			subgraph.sort(node.sort_by_NoN());
		}
		//degree
		 */
		//lower degree
        //System.out.println(subgraph.size());
        subgraph.sort(node.sort_by_NoN());
        graph k_core=new graph(targetC, core, fid, subgraph);
        try {
            FileOutputStream fileOut =
            new FileOutputStream("er.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(k_core);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved");
         } catch (IOException i) {
            i.printStackTrace();
         }
        //k_core.lowest_degree(30);
		//lower degree
		
    	
		//long stopTime2=System.currentTimeMillis();
		//System.out.println((stopTime2-startTime2)/1000);
		
        //for (node i: subgraph) {i.calculate_NoN(); System.out.println(i.NoN+" "+core[fid.get(i.index)]);}
       /*
        for (node i: graph) i.calculate_degree();
		graph.sort(node.sort_by_degree());
		for (int i=0; i<graph.size(); i++){
			core[fid.get(graph.get(i).index)]=graph.get(i).degree;
			for (int j=0; j<graph.get(i).edgelist.size(); j++){
				n1 = graph.get(i).edgelist.get(j);
				if (n1.degree > graph.get(i).degree) 
					n1.degree--;
				temp=graph.indexOf(n1);
				if (temp!=0)
					while (graph.get(temp).degree<graph.get(temp-1).degree && temp>0)
					{
						Collections.swap(graph, temp-1, temp);
						temp--;
					}
				}
		}
		*/
		/*
		for (int i=0; i<subgraph.size(); i++){
			core[fid.get(subgraph.get(i).index)] = subgraph.get(i).NoN;
			if (subgraph.get(i).NoN < targetC) total++;
			for (node j: subgraph.get(i).neighbours){
				if (j.NoN > subgraph.get(i).NoN) {
					j.NoN--;
					temp=subgraph.indexOf(j);
					if (temp == -1) System.out.println(temp);
					if (temp!=0)
						while (temp>0 && subgraph.get(temp).NoN < subgraph.get(temp-1).NoN )
						{
							Collections.swap(subgraph, temp-1, temp);
							temp--;
						}
					}	
			}
		}
		*/
	
		try{
		PrintStream out =new PrintStream(new FileOutputStream("er.txt"));
		System.setOut(out);
		for (node i:graph)
		{
			//if (core[fid.indexOf(i.index)]>=targetC)
			//count++
			//if (core[fid.indexOf(i.index)]>=40){
			//writer.println(i.index+ " " +core[fid.get(i.index)]);
			System.out.println(i.index+ " " +core[fid.get(i.index)]);
			//}
		}
		//System.out.println(count);
		} catch (IOException e){
			System.out.println("print fail");
		}
	
		//total=visited.size();
		
	
	}
	
}
