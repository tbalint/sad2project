package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageRank_LogarithmicWeight {
	private ArrayList<Node> graph;
	private Map<Node, Double> pageRank;
	private Map<Node, Double> newPageRank;
	private Double d;
	private int N;

	public PageRank_LogarithmicWeight(ArrayList<Node> graph, int iteration) {
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
		computePageRank(iteration);
	}

	public void computePageRank(int iterator) {
		for (int i = 0; i < iterator; i++) {
			double dp = 0;
			for (int j = 0; j < N; j++)
				if (graph.get(j).getOutEdges().size() == 0) {
					dp += d * pageRank.get(graph.get(j))/N;// get PageRank from
															// pages without
															// out-links
				}
			for (int j = 0; j < N; j++) {
				newPageRank.put(graph.get(j), dp + ((1 - d) / N));
				for (int k = 0; k < graph.get(j).getInEdges().size(); k++) {
					double npr = newPageRank.get(graph.get(j));
					Node.Edge e=(Node.Edge) (graph.get(j).getInEdges().toArray()[k]);
					Node n=e.getFrom();
					double w =1;
					if (e.getWeight()>1){
						w = Math.log10(e.getWeight());
					} else {
						w = 0.1;
					}
					npr += (d * pageRank.get(n) * w)
							/ graph.get(graph.indexOf(n)).getOutEdges().size();// get PageRank
																// from inlinks
					newPageRank.put(graph.get(j), npr);
				}
			}
			for (int j = 0; j < N; j++)
				pageRank.put(graph.get(j), newPageRank.get(graph.get(j)));
			// update pageRank with the new values

		}
	}
	public Map<Node, Double> getRank(){
		return pageRank;
	}
}
