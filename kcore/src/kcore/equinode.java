package kcore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;


public class equinode {
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
			b = in.nextInt();
			in.nextLine();
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
		/*
		int max_core = 1;
		int[] number = new int[1000];
		for (int i=0; i < k_core.core.length; i++){ 
			number[k_core.core[i]]++;
			if ( k_core.core[i]>max_core) max_core = k_core.core[i];
		}
		for (int i=0; i <= max_core; i++) System.out.println(i + " " + number[i]);
		*/
		
		
		long stopTime1 = System.currentTimeMillis();
		System.out.println((stopTime1-startTime1)/1000);
		List<node> subgraph= new ArrayList<node>();
		int targetC=10; 
		Collections.reverse(graph);
		for (node i: graph) if (k_core.core[k_core.fid.get(i.index)]>=targetC) 
			subgraph.add(i);
		int count = 0;
		for (node i: subgraph) 
			for (node j: i.edgelist)
				if (subgraph.contains(j)) 
				{
					i.neighbours.add(j); 
					i.NoN++;
					count++;
					if (k_core.core[k_core.fid.get(i.index)]<= k_core.core[k_core.fid.get(j.index)]) i.cd++;
				}
		for (node i: subgraph) { 
		        	i.neighbours.sort(node.sort_by_NoN());
		}
		System.out.println("edge: "+ count/2);
		System.out.println("node: "+ subgraph.size());
		Sift algorithm = new Sift(targetC,k_core.core,k_core.fid,subgraph,0.05);
		//System.out.println("edges: " + algorithm.edgelist1.size());
		//System.out.println("nodes: " + algorithm.nodelist.size());
		algorithm.Pick(400,true,false);
		//graph algorithm=new graph(targetC, k_core.core, k_core.fid, subgraph);
		//algorithm.step = 6;
		//System.out.println("budget: "+ (int)(count/2*0.003));
		//algorithm.equinode(400);
		
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
		try{
			PrintStream out =new PrintStream(new FileOutputStream("emailtest.txt"));
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
	*/
	}

}

