package kcore;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.Math;
public class resilience {
	
	public static void main(String[] args) {
	//initialization
	String name="C:/Users/nh54762/Desktop/research/com-youtube.ungraph.txt";
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
         FileInputStream fileIn = new FileInputStream("youtube.ser");
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
	
	
	
	long stopTime1 = System.currentTimeMillis();
	System.out.println((stopTime1-startTime1)/1000);
	List<node> subgraph= new ArrayList<node>();
	int targetC=40; 
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
	Collections.sort(subgraph, new Sortbycore(k_core.core, k_core.fid));
	int r = 20;
	double avgx = 0;
	double avgy = 0;
	ArrayList<Integer> x = new ArrayList<Integer>();
	ArrayList<node> topnodes = new ArrayList<node>();
	ArrayList<Integer> y = new ArrayList<Integer>();
	Sift algorithm = new Sift(targetC,k_core.core,k_core.fid,subgraph,0.05);
	//graph algorithm=new graph(targetC, k_core.core, k_core.fid, subgraph);
	for (int i= 0; i <subgraph.size()*r*0.01; i++) {
		node n = subgraph.get(i);
		topnodes.add(n);
		x.add(algorithm.core[algorithm.fid.get(n.index)]);
		avgx +=algorithm.core[algorithm.fid.get(n.index)];
	}
	System.out.println("avg: "+avgx);
	avgx = avgx/x.size();
	System.out.println("after: " + avgx);
	algorithm.Pick((int)(count/2*0.005),true,false);
	for (node i: algorithm.deleted) {
		System.out.println(algorithm.core[algorithm.fid.get(i.index)]);
	}
	//algorithm.equiedge(300);
	for (node i : topnodes){
		y.add(algorithm.core[algorithm.fid.get(i.index)]);
		avgy += algorithm.core[algorithm.fid.get(i.index)];
		//System.out.println(i.index + " " + algorithm.core[algorithm.fid.get(i.index)]);
	}
	
	System.out.println("avg: "+avgy);
	avgy = avgy/y.size();
	System.out.println("after: " + avgy);
	
	double sxy = 0.0;
	double sx = 0.0;
	double sy = 0.0;
	double stdx = 0.0;
	double stdy = 0.0;
	
	for (int i=0; i <x.size(); i++){
		int rx = x.get(i);
		int ry = y.get(i);
		stdx += (rx - avgx)*(rx - avgx);
		stdy += (ry - avgy)*(ry - avgy);
		sxy += (rx-avgx)*(ry-avgy);
	}
	
	stdx = Math.sqrt(stdx/(x.size()));
	stdy = Math.sqrt(stdy/(y.size()));
	
	double cov = sxy / (x.size()-1);
	double rankcor = cov/stdx/stdy;
	System.out.println("arguments : "+stdx+" "+stdy+" "+cov);
	System.out.println("result: " + rankcor);
	//graph algorithm=new graph(targetC, k_core.core, k_core.fid, subgraph);
	//System.out.println("budget: "+ (int)(count/2*0.003));
	//algorithm.equiedge(500);
	}
	
	
}
