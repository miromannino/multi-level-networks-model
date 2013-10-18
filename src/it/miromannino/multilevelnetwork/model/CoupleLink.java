package it.miromannino.multilevelnetwork.model;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class CoupleLink {

	/**
	 * The node where the couple link came from
	 * */
	protected Node departureNode;

	/**
	 * The node where the couple link points
	 * */
	protected Node destinationNode;

	protected Object data;

	public CoupleLink(Node departureNode, Node destinationNode, Object data) {
		if (departureNode == null) throw new NullPointerException("departureNode must be not null");
		if (destinationNode == null) throw new NullPointerException("destinationNode must be not null");

		this.departureNode = departureNode;
		this.destinationNode = destinationNode;
		this.data = data;
	}

	public CoupleLink(Node departureNode, Node destinationNode) {
		this(departureNode, destinationNode, null);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Node getDepartureNode() {
		return departureNode;
	}

	public Node getDestinationNode() {
		return destinationNode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		return ((CoupleLink)obj).departureNode.equals(this.departureNode)
				&& ((CoupleLink)obj).destinationNode.equals(this.destinationNode);
	}

	@Override
	public String toString() {
		return "(" + departureNode.toString() + ", " + destinationNode.toString() + ")";
	}

}
