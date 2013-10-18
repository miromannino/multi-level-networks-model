package it.miromannino.multilevelnetwork.model;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */

public class Link {

	/**
	 * The node where the link points
	 * E.g. if we have the node A, the link to B means that we have an arc from A to B
	 * */
	protected Node destinationNode;

	protected Object data;

	public Link(Node destinationNode, Object data) {
		if (destinationNode == null) throw new NullPointerException("destinationNode must be not null");

		this.destinationNode = destinationNode;
		this.data = data;
	}

	public Link(Node destinationNode) {
		this(destinationNode, null);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Node getDestinationNode() {
		return destinationNode;
	}

	@Override
	public int hashCode(){
		return destinationNode.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		return ((Link)obj).destinationNode.equals(this.destinationNode);
	}

	@Override
	public String toString() {
		return destinationNode.toString();
	}

}
