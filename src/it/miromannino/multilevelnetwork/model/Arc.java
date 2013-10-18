package it.miromannino.multilevelnetwork.model;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class Arc {

	/**
	 * The from node and to node are the nodes that the arc connects.
	 * E.g. the arc from A to B has the from = A and to = B. We don't use the id, that are strings, for efficiency.
	 * */
	protected Node fromNode, toNode;

	protected Object data;

	public Arc(Node from, Node to, Object data) {
		if (from == null || to == null) throw new NullPointerException("from and to must be not null");

		this.fromNode = from;
		this.toNode = to;
		this.data = data;
	}

	public Arc(Node from, Node to) {
		this(from, to, null);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Node getFromNode() {
		return fromNode;
	}

	public Node getToNode() {
		return toNode;
	}

	@Override
	public int hashCode(){
		StringBuffer b = new StringBuffer();
		b.append(fromNode.id);
		b.append(toNode.id);
		return b.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		Arc otherArc = (Arc)obj;
		return (fromNode.equals(otherArc.fromNode) && toNode.equals(otherArc.toNode));
	}

	@Override
	public String toString() {
		return this.fromNode + " -> " + this.toNode;
	}

}
