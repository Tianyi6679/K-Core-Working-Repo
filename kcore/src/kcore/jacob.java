package kcore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class jacob {
	public static void main(String[] args) {
		//initialization
		String name="C:/Users/nh54762/Desktop/research/web-Stanford.txt";
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
		graph k_core;
		try {
	         FileInputStream fileIn = new FileInputStream("stanford.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         k_core = (graph) in.readObject();
	         in.close();
	         fileIn.close();
	      } catch (IOException i) {
	         i.printStackTrace();
	         return;
	      } catch (ClassNotFoundException c) {
	         System.out.println("Graph class not found");
	         c.printStackTrace();
	         return;
	      }
		List<node> subgraph = new ArrayList<node>();
		List<edge> edgelist = new ArrayList<edge>();
		subgraph.clear();
		edgelist.clear();
        int targetC=8; 
        for (node i: graph) if (k_core.core[k_core.fid.get(i.index)]>=targetC){
        	subgraph.add(i);
        	i.neighbourset.clear();
        	i.NoN=0;
        }
        k_core = new graph(targetC, k_core.core, k_core.fid, subgraph);
        for (node i: subgraph) 
        	for (node j: i.edgelist)
        		if (subgraph.contains(j)) {
        			i.neighbourset.add(j); 
        			i.neighbours.add(j);
        			i.NoN++;
        		}
        HashSet<node> visited = new HashSet<node>();
        visited.clear();
        
        for (node i:subgraph){
        	for (node j: i.neighbourset){
        		if (!visited.contains(j)) edgelist.add(new edge(i,j));
        	}
        	visited.add(i);
        }
        k_core.edgelist=edgelist;
        System.out.println(subgraph.size());
        System.out.println(edgelist.size());
        k_core.jacob(400);
	}
	
}
