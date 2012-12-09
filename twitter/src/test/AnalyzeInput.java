package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import data.Data;
import algorithms.Node.Edge;
import algorithms.PageRank;
import algorithms.hitsAlgorithm;
import algorithms.Node;
import algorithms.hits_LogarithmicWeight;
import algorithms.hits_SimpleWeight;
import algorithms.hits_WeightedByEdgeSet;

public class AnalyzeInput {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Node> graph=Tweets2Graph();
		//hitsAlgorithm h = new hitsAlgorithm(graph);
		//PageRank p = new PageRank(graph);
		//hits_SimpleWeight hits_simple=new hits_SimpleWeight(graph,10);
		//hits_LogarithmicWeight hits_log = new hits_LogarithmicWeight(graph,10);
		//hits_WeightedByEdgeSet hits_edges = new hits_WeightedByEdgeSet(graph,10);
		File file = new File("src\\output.txt");
		
		FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);

            //out.writeObject(h.toString());
           // out.writeObject(hits_simple.toString());
            out.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }

	}
	
	public static void Tweets2DOTLanguage(){
		Data d=Data.get();
		List<String> graph=new ArrayList<String>();
		StringBuffer buf=new StringBuffer();
		buf.append("digraph graphname {\n");
		for (long key : d.retweets.keySet()){
			data.Retweet r =d.retweets.get(key);
			
			String id2=r.originalUser.screenName;
			String id1 =r.user.screenName;
			//long id=r.tweetId;
			if (!id1.equals(id2)){
				if (graph.isEmpty()){
					buf.append(id1+" -> "+id2+"\n");
					graph.add(id1);
					graph.add(id2);
				} else {
					for (String n:graph){
						if (id1.equals(n)){
							buf.append(id1+" -> "+id2+"\n");
							graph.add(id2);
							break;
						} else if (id2.equals(n)) {
							buf.append(id1+" -> "+id2+"\n");
							graph.add(id1);
							break;
						} else {
							buf.append(id1+" -> "+id2+"\n");
							graph.add(id1);
							graph.add(id2);
							break;
							
						}
					}
				}
			}
		}
		buf.append("}");
		try {
			BufferedWriter outputStream = new BufferedWriter(new FileWriter("src\\example.gv"));
			outputStream.write(buf.toString());
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static ArrayList<Node> Tweets2Graph(){
		Data d=Data.get();
		List<Node> graph=new ArrayList<Node>();

		for (long key : d.retweets.keySet()){
			data.Retweet r =d.retweets.get(key);
			
			String id2=r.originalUser.screenName;
			String id1 =r.user.screenName;
			long id=r.tweetId;
			if (!id1.equals(id2)){
			if (graph.isEmpty()){
				Node tmp=new Node(id1);
				Node tmp2=new Node(id2);
				tmp.addEdge(tmp2);
				graph.add(tmp);
				graph.add(tmp2);
			} else {
				Node tmp=null;
				Node tmp2=null;
				
				
				for (Node n:graph){
					if (tmp!=null && tmp2!=null){
						break;
					}
					
					if (id1.equals(n.name)){
						tmp=n;
					} else if (id2.equals(n.name)) {
						tmp2=n;
					}
				}
				if (tmp==null && tmp2==null){
					tmp=new Node(id1);
					tmp2=new Node(id2);
					tmp.addEdge(tmp2);
					graph.add(tmp);
					graph.add(tmp2);
				} else if (tmp==null){
					tmp=new Node(id1);
					tmp.addEdge(tmp2);
				} else if (tmp2==null){
					tmp2=new Node(id2);
					tmp.addEdge(tmp2);
				} else {
					tmp.increaseWeight(tmp2);
				}
			}}
		}
		
		return (ArrayList<Node>) graph;
	}

}
