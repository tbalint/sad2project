package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.Data;
import algorithms.Node.Edge;
import algorithms.PageRank;
import algorithms.PageRank_LogarithmicWeight;
import algorithms.PageRank_SimpleWeight;
import algorithms.PageRank_WeightedByEdgeSet;
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
		System.out.println("Start creating graph");
		ArrayList<Node> graph=Tweets2Graph();
		System.out.println("Stop creating graph");
		hitsAlgorithm h = new hitsAlgorithm(graph,0);
		hits_SimpleWeight hits_simple=new hits_SimpleWeight(graph,0);
		hits_LogarithmicWeight hits_log = new hits_LogarithmicWeight(graph,0);
		hits_WeightedByEdgeSet hits_edges = new hits_WeightedByEdgeSet(graph,0);
		
		PageRank p = new PageRank(graph,0);
		PageRank_SimpleWeight p_simple=new PageRank_SimpleWeight(graph,0);
		PageRank_LogarithmicWeight p_log = new PageRank_LogarithmicWeight(graph,0);
		PageRank_WeightedByEdgeSet p_edges = new PageRank_WeightedByEdgeSet(graph,0);

		/*Graph2DOTLangouge(graph, h.getAuth(), h.getHub(),"output\\hits.gv");
		Graph2DOTLangouge(graph, hits_simple.getAuth(), hits_simple.getHub(), "output\\hits_simple.gv");
		Graph2DOTLangouge(graph, hits_log.getAuth(), hits_log.getHub(), "output\\hits_log.gv");
		Graph2DOTLangouge(graph, hits_edges.getAuth(), hits_edges.getHub(), "output\\hits_edges.gv");
		
		Graph2DOTLangouge(graph, p.getRank(),null, "output\\pagerank.gv");
		Graph2DOTLangouge(graph, p_simple.getRank(),null, "output\\pagerank_simple.gv");
		Graph2DOTLangouge(graph, p_log.getRank(),null, "output\\pagerank_log.gv");
		Graph2DOTLangouge(graph, p_edges.getRank(),null, "output\\pagerank_edges.gv");
        Tweets2DOTLanguage();*/
        System.out.println("Start loop");
        for (int i=1; i<41;i++){
        	System.out.println("Iteration: "+i);
        	h = new hitsAlgorithm(graph,i);
        	Scores2TXT(h.getAuth(),h.getHub(),"output\\hits-"+i+".txt");
        	hits_simple = new hits_SimpleWeight(graph,i);
        	Scores2TXT(hits_simple.getAuth(),hits_simple.getHub(),"output\\hits_simple-"+i+".txt");
        	hits_log = new hits_LogarithmicWeight(graph,i);
        	Scores2TXT(hits_log.getAuth(),hits_simple.getHub(),"output\\hits_log-"+i+".txt");
        	hits_edges = new hits_WeightedByEdgeSet(graph,i);
        	Scores2TXT(hits_edges.getAuth(),hits_edges.getHub(),"output\\hits_edges-"+i+".txt");
        	
        	
        	p = new PageRank(graph,i);
        	Scores2TXT(p.getRank(),null,"output\\pagerank-"+i+".txt");
        	p_simple = new PageRank_SimpleWeight(graph,i);
        	Scores2TXT(p_simple.getRank(),null,"output\\pagerank_simple-"+i+".txt");
        	p_log = new PageRank_LogarithmicWeight(graph,i);
        	Scores2TXT(p_log.getRank(),null,"output\\pagerank_log-"+i+".txt");
        	p_edges = new PageRank_WeightedByEdgeSet(graph,i);
        	Scores2TXT(p_edges.getRank(),null,"output\\pagerank_edges-"+i+".txt");
        }
	}
	
	public static void Scores2TXT(Map<Node,Double> arg0, Map<Node,Double> arg1,String fileName ){
		StringBuffer buf = new StringBuffer();
		boolean isArg0=(arg0!=null);
		boolean isArg1=(arg1!=null);
		for (Node n : arg0.keySet()){
			buf.append(n.name+" \t ");
			if (isArg0){
				buf.append(" \t "+arg0.get(n));
			}
			if (isArg1){
				buf.append(" \t "+arg1.get(n));
			}
			buf.append("\n");
		}
		try {
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(fileName));
			outputStream.write(buf.toString());
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void Graph2DOTLangouge(ArrayList<Node> graph, Map<Node,Double> arg0, Map<Node,Double> arg1,String fileName ){
		StringBuffer buf = new StringBuffer();
		boolean isArg0=(arg0!=null);
		boolean isArg1=(arg1!=null);
		buf.append("digraph graphname {\n");
		for (Node n : graph){
			buf.append(n.name + " [label=\""+n.name);
			if (isArg0){
				buf.append(" : "+arg0.get(n));
			}
			if (isArg1){
				buf.append(" , "+arg1.get(n));
			}
			buf.append("\"]\n");
			for (Node.Edge e:n.getOutEdges()){
				buf.append(n.name+" -> "+e.getTo().name+" [label=\""+e.getWeight()+"\"]\n");
			}
		}
		buf.append("}");
		try {
			BufferedWriter outputStream = new BufferedWriter(new FileWriter(fileName));
			outputStream.write(buf.toString());
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void Tweets2DOTLanguage(){
		
		//Data.loadFile("twitter20MB-reduced.ser");
		Data.loadFile("input\\twitter250MB-reduced.ser");
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
					String tmp=null;
					String tmp2=null;
					
					for (String n:graph){
						if (tmp!=null && tmp2!=null){
							break;
						}
						
						if (id1.equals(n)){
							tmp=n;
						} else if (id2.equals(n)) {
							tmp2=n;
						}
					}
					if (tmp==null && tmp2==null){
						graph.add(id1);
						graph.add(id2);
					} else if (tmp==null){
						graph.add(id1);
					} else if (tmp2==null){
						graph.add(id2);
					} else {
					}
					buf.append(id1+" -> "+id2+"\n");
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
		Data.loadFile("input\\twitter205MB-reduced.ser");
		Data d=Data.get();
		List<Node> graph=new ArrayList<Node>(100000);
		int i=0;
		for (long key : d.retweets.keySet()){
			data.Retweet r =d.retweets.get(key);
			i++;
			if (i%1000==0){
				System.out.println(i+" - "+graph.size());
			}
			String id2=r.originalUser.screenName;
			String id1 =r.user.screenName;
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
					graph.add(tmp);
				} else if (tmp2==null){
					tmp2=new Node(id2);
					tmp.addEdge(tmp2);
					graph.add(tmp2);
				} else {
					tmp.increaseWeight(tmp2);
				}
			}}
		}
		
		return (ArrayList<Node>) graph;
	}

}
