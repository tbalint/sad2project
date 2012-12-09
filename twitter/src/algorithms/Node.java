package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class Node {
	public String name;
	public HashSet<Edge> inEdges;

	
	public HashSet<Edge> outEdges;
	private int numNodes = 0;

	public Node(String name) {
		this.name = name;
		this.inEdges = new HashSet<Edge>();
		this.outEdges = new HashSet<Edge>();
		numNodes++;
	}

	public Node addEdge(Node node) {
		Edge e = new Edge(this, node);
		this.outEdges.add(e);
		node.inEdges.add(e);
		return this;
	}
	
	public void increaseWeight(Node node){
		for (Edge edge:this.outEdges){
			if (node.equals(edge.getTo())){
				edge.increaseWeight(1);
				break;
			}
		}
	}

	@Override
	public String toString() {
		return name;
	}

	public int numberNodes() {
		return numNodes;
	}

	public static class Edge {
		private  Node from;
		private  Node to;
		private  int weight=1;

		public Edge(Node from, Node to) {
			this.from=from;
			this.to=to;
		}

		public String toString(){
			return from.name+"-->"+to.name;
		}
		@Override
		public boolean equals(Object obj) {
			Edge e = (Edge) obj;
			return e.getFrom() == getFrom() && e.getTo() == getTo();
		}

		public Node getTo() {
			return to;
		}

		

		public Node getFrom() {
			return from;
		}

		public void setWeight(int i){
			weight=i;
		}
		
		public int getWeight(){
			return weight;
		}
		public void increaseWeight(int i){
			weight+=i;
		}

	}
	public HashSet<Edge> getInEdges() {
		return inEdges;
	}

	public void setInEdges(HashSet<Edge> inEdges) {
		this.inEdges = inEdges;
	}

	public HashSet<Edge> getOutEdges() {
		return outEdges;
	}

	public void setOutEdges(HashSet<Edge> outEdges) {
		this.outEdges = outEdges;
	}


}