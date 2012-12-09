package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import algorithms.Node.Edge;

public class hits_SimpleWeight {
	private ArrayList<Node> graph;
	private Map<Node, Double> hubScores;
	private Map<Node, Double> authorityScores;

	public hits_SimpleWeight(ArrayList<Node> graph, int iteration) {
		this.graph = graph;
		this.hubScores = new HashMap<Node, Double>();
		this.authorityScores = new HashMap<Node, Double>();
		int numLinks = graph.size();
		for (int i = 0; i < numLinks; i++) {
			hubScores.put(graph.get(i), (double) 1);
			authorityScores.put(graph.get(i), (double) 1);
		}
		computeHITS(iteration);
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
				while (itr.hasNext()) {// p.incomingNeighbors is the set of
										// pages that link to p
					Edge e=itr.next();
					//System.out.println("vecini::" + to.toString());
					double auth = (Double) authorityScores.get(graph.get(i));
					auth += e.getWeight()* hubScores.get(e.getTo());
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
				while (itr.hasNext()) {// p.outgoingNeighbors is the set of
										// pages that p links to
					Edge e = itr.next();
					double hub = (Double) hubScores.get(graph.get(i));
					hub +=e.getWeight()* authorityScores.get(e.getFrom());
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

}
