package it.miromannino.multilevelnetwork.model;

import java.util.HashMap;
import java.util.Iterator;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class Node {

	/** The readable identification for the node. The id is immutable. */
	protected String id;

	/** Node data i.e. that instead of d(n) we have n.getData() */
	protected Object data;

	protected HashMap<Node, Link> links;

	public Node(String id, Object data, Link[] links) {
		if (id == null) throw new NullPointerException("id must be not null");
		if (!id.matches("^[a-zA-Z][0-9a-zA-Z]*$"))
			throw new IllegalArgumentException("the id doesn't have the correct format ([a-zA-Z][0-9a-zA-Z]*)");

		this.id = id;
		this.data = data;

		if (links != null) {
			this.links = new HashMap<Node, Link>(links.length);
			for (Link l : links) {
				if (l != null) this.links.put(l.destinationNode, l);
			}
		} else {
			this.links = new HashMap<Node, Link>();
		}
	}

	public Node(String id, Object data) {
		this(id, data, null);
	}

	public Node(String id) {
		this(id, null, null);
	}

	public String getId() {
		return id;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * Create a link to the node {@code n}. This link can contains a data.
	 * @param n the destination node
	 * @param data the data for the link that we're creating
	 * @return true if the link is inserted, false if not (because it exists yet). In the last case the data of the
	 * existent link is not changed.
	 */
	public boolean addNeighbor(Node n, Object data) {
		if (n == null) throw new NullPointerException("n must be not null");
		if (this.links.containsKey(n)) return false;
		this.links.put(n, new Link(n, data));
		return true;
	}

	/**
	 * Create a link to the node {@code n}.
	 * @param n the destination node
	 * @return true if the link is inserted, false if not (because it exists yet).
	 */
	public boolean addNeighbor(Node n) {
		return this.addNeighbor(n, null);
	}

	/**
	 * Removes the neighbor {@code n} from the node
	 * @return the link associated to the neighbor, null if the neighbor doesn't exist.
	 */
	public Link removeNeighbor(Node n) {
		if (n == null) throw new NullPointerException("n must be not null");
		return this.links.remove(n);
	}

	/**
	 * Determines if the neighbor exists
	 * @param n
	 * @return
	 */
	public boolean hasNeighbor(Node n) {
		if (n == null) throw new NullPointerException("n must be not null");
		return this.links.containsKey(n);
	}

	/**
	 * Return the data associated to the link that connect the node to te neighbor {@code n}.
	 * @return the data, null if the neighbor doesn't exist, or if the link has no data.
	 */
	public Object getNeighborLinkData(Node n) {
		if (n == null) throw new NullPointerException("n must be not null");
		Link l = this.links.get(n);
		if (l == null) return null;
		return l.getData();
	}

	/**
	 * Set the data associated to the link that connect the node to te neighbor {@code n}.
	 * @return If the neighbor doesn't exist it returns false, true in the opposite case.
	 */
	public boolean setNeighborLinkData(Node n, Object data) {
		if (n == null) throw new NullPointerException("n must be not null");
		Link l = this.links.get(n);
		if (l == null) return false;
		l.setData(data);
		return true;
	}

	public Iterator<Link> getLinksIterator() {
		return links.values().iterator();
	}

	public Iterator<Node> getNeighborIterator() {
		return links.keySet().iterator();
	}

//	@Override
//	public int hashCode(){
//		return this.id.hashCode();
//	}

	@Override
	public boolean equals(Object obj) {
		return this == obj;
//		if (this == obj) return true;
//		if (obj == null) return false;
//		if (this.getClass() != obj.getClass()) return false;
//		return ((Node)obj).id.equals(this.id);
	}

	@Override
	public String toString() {
		return this.id;
	}

}
