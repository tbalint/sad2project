package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//I'm using the same class Node
public class PageRank {

	private ArrayList<Node> graph;
	private Map<Node, Double> pageRank;
	private Map<Node, Double> newPageRank;
	private Double d;
	private int N;

	public PageRank(ArrayList<Node> graph) {
		this.graph = graph;
		this.N = graph.size();
		this.pageRank = new HashMap<Node, Double>();
		this.newPageRank = new HashMap<Node, Double>();
		this.d = 0.85;// damping factor which has to be between 0 and 1
		for (int i = 0; i < graph.size(); i++) {
			pageRank.put(graph.get(i), (double) (1 / graph.size()));// initialize
																	// the
																	// pageRank
		}
		computePageRank(20);
	}

	public void computePageRank(int iterator) {
		for (int i = 0; i < iterator; i++) {
			double dp = 0;
			for (int j = 0; j < N; j++)
				if (graph.get(j).getOutEdges().size() == 0) {
					dp += d * pageRank.get(graph.get(j));// get PageRank from
															// pages without
															// out-links
				}
			for (int j = 0; j < N; j++) {
				newPageRank.put(graph.get(j), dp + ((1 - d) / N));
				for (int k = 0; k < graph.get(j).getInEdges().size(); k++) {
					double npr = newPageRank.get(graph.get(j));
					npr += (d * pageRank.get(k))
							/ graph.get(k).getOutEdges().size();// get PageRank
																// from inlinks
					newPageRank.put(graph.get(j), npr);
				}
			}
			for (int j = 0; j < N; j++)
				pageRank.put(graph.get(j), newPageRank.get(graph.get(j)));
			// update pageRank with the new values

		}
	}
}
