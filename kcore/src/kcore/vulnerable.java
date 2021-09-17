package kcore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class vulnerable {
	public static void main(String[] args) {
		//initialization
		String name="C:/Users/nh54762/Desktop/com-amazon.ungraph.txt";
		File file= new File(name);
		List<node> graph=new ArrayList<node>();
		Hashtable<Integer, Integer> fid=new Hashtable<Integer, Integer>();
		System.out.println("vulnerable");
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
		for (int i=0; i<graph.size(); i++){
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
		//vulnerable 
				List<node> subgraph= new ArrayList<node>();
		        int targetC=6; 
				//initialize subgraph
		        Collections.reverse(graph);
		        for (node i: graph) if (core[fid.get(i.index)] >= targetC) 
		        	subgraph.add(i);
		        for (node i: subgraph) 
		        	for (node j: i.edgelist)
		        		if (subgraph.contains(j)) {i.neighbours.add(j); i.NoN++;}
		        for (node i: subgraph) { i.neighbours.sort(node.sort_by_NoN());} 
		        graph k_core=new graph(targetC,core,fid,subgraph);
		        k_core.vulnerable(30);    
			}
}
