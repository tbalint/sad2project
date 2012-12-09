package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import algorithms.Node.Edge;

public class hitsAlgorithm {

	private ArrayList<Node> graph;
	private Map<Node, Double> hubScores;
	private Map<Node, Double> authorityScores;

	public hitsAlgorithm(ArrayList<Node> graph) {
		this.graph = graph;
		this.hubScores = new HashMap<Node, Double>();
		this.authorityScores = new HashMap<Node, Double>();
		int numLinks = graph.size();
		for (int i = 0; i < numLinks; i++) {
			hubScores.put(graph.get(i), (double) 1);
			authorityScores.put(graph.get(i), (double) 1);
		}
		computeHITS(1);
	}

	public void computeHITS(int numIterations) {

		for (int k = 0; k < numIterations; k++) { // run the algorithm for k
													// steps
			double norm = 0;

			for (int i = 0; i < graph.size(); i++) { // update all authority
														// values first
				authorityScores.put(graph.get(i), (double) 0);
				//System.out.println(graph.get(i).toString());

				HashSet<Edge> inNodes = graph.get(i).getInEdges();
				java.util.Iterator<Edge> itr = inNodes.iterator();
				/*while (itr.hasNext()) {
					System.out.println("-->" + itr.next().toString());
				}*/
				Node to;
				while (itr.hasNext()) {// p.incomingNeighbors is the set of
										// pages that link to p
					to = itr.next().getTo();
					//System.out.println("vecini::" + to.toString());
					double auth = (Double) authorityScores.get(graph.get(i));
					auth += hubScores.get(to);
					authorityScores.put(graph.get(i), auth);
				}

				norm += Math.pow((Double) authorityScores.get(graph.get(i)), 2);// calculate
																				// the
																				// sum
																				// of
																				// the
																				// squared
																				// auth
																				// values
																				// to
																				// normalise
			}
			norm = Math.sqrt(norm);

			for (int i = 0; i < graph.size(); i++) { // update the auth scores
				Double auth = (Double) authorityScores.get(graph.get(i));
				auth = auth / norm;
				authorityScores.put(graph.get(i), auth);// normalize the auth
														// values
			}
			norm = 0;

			for (int i = 0; i < graph.size(); i++) { // then update all hub
														// values
				hubScores.put(graph.get(i), (double) 0);

				HashSet<Edge> outNodes = graph.get(i).outEdges;
				java.util.Iterator<Edge> itr = outNodes.iterator();
				/*while (itr.hasNext()) {
				System.out.println("-->" + itr.next().toString());
			}*/
				Node from;
				while (itr.hasNext()) {// p.outgoingNeighbors is the set of
										// pages that p links to
					from = itr.next().getFrom();
					double hub = (Double) hubScores.get(graph.get(i));
					hub += authorityScores.get(from);
					hubScores.put(graph.get(i), hub);
				}

				norm += Math.pow((Double) hubScores.get(graph.get(i)), 2);// calculate
																			// the
																			// sum
																			// of
																			// the
																			// squared
																			// auth
																			// values
																			// to
																			// normalise
			}
			norm = Math.sqrt(norm);

			for (int i = 0; i < graph.size(); i++) { // then update all hub
														// values
				Double hub = (Double) authorityScores.get(graph.get(i));
				hub = hub / norm;
				hubScores.put(graph.get(i), hub);// normalize the hub values
			}

		}

	}

	public String toString() {
		Iterator iterator = (Iterator) authorityScores.keySet().iterator();
		StringBuffer b=new StringBuffer();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			String value = null;
			int i = 0;
			while (i < graph.size()) {
				if (key.equals(graph.get(i).name)) {
					value = authorityScores.get(graph.get(i)).toString();
					break;
				}
				i++;
			}
			b.append(key.toString() + " " + value+"\n");
			//System.out.println(key.toString() + " " + value);
		}
		Iterator iterator2 = (Iterator) hubScores.keySet().iterator();
		b.append("\nhub\n");
		//System.out.println("hub");
		while (iterator2.hasNext()) {
			String key2 = iterator2.next().toString();
			String value2 = null;
			int i = 0;
			while (i < graph.size()) {
				if (key2.equals(graph.get(i).name)) {
					value2 = hubScores.get(graph.get(i)).toString();
					break;
				}
				i++;
			}
			b.append(key2.toString() + " " + value2+"\n");
			//System.out.println(key2.toString() + " " + value2);
		}
		return b.toString();
	}

	public static void main(String[] args) {
		Node unu = new Node("unu");
		Node doi = new Node("doi");
		Node trei = new Node("trei");
		Node patru = new Node("patru");

		unu.addEdge(doi);
		unu.addEdge(trei);
		unu.addEdge(patru);
		doi.addEdge(trei);
		doi.addEdge(patru);
		trei.addEdge(doi);
		trei.addEdge(patru);

		ArrayList<Node> graph = new ArrayList();
		graph.add(unu);
		graph.add(doi);
		graph.add(trei);
		graph.add(patru);

		hitsAlgorithm h = new hitsAlgorithm(graph);
		h.toString();

	}

}
