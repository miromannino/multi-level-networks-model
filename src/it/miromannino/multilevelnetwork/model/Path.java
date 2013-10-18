package it.miromannino.multilevelnetwork.model;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * The path representation
 *
 * Is an array of Nodes (we use the nodes, and not the ids, for efficiency)
 *
 * Remember that:
 * if we have a path "(a -> b -> c)" this has length 2, because we count only the number of arcs
 * if we have a path "(a)" this has length 0.
 * if we have a path "()" this has length -1.
 *
 * The position of a node is:
 * if we have the path "(a -> b -> c)" the position 0 doesn't exist, the position 1 has "a", ...
 *
 */
public class Path implements Iterable<Node> {

	public static final Path NULL_PATH = new Path();

	List<Node> nodes;

	/**
	 * Create a path with the nodes in the array {@code nodes}. This is a path of length {@code nodes.length - 1}.
	 */
	public Path(Node[] nodes) {
		if (nodes == null) throw new NullPointerException("nodes must be not null");
		for (Node n : nodes) if (n == null) throw new NullPointerException("all nodes in the array must be not null");
		this.nodes = new ArrayList<Node>(nodes.length);
		for (Node n : nodes) this.nodes.add(n);
	}

	/**
	 * Create an empty path of an initial capacity of {@code initialCapacity} nodes,
	 * this means that the ArrayList that stores this nodes can store efficiently a path with
	 * a length of {@code initialCapacity - 1}
	 */
	public Path(int initialCapacity) {
		if (initialCapacity < 0) throw new IllegalArgumentException("initialCapacity must be greater or equal than -1");
		this.nodes = new ArrayList<Node>(initialCapacity);
	}

	/**
	 * Create an empty path
	 */
	public Path() {
		this.nodes = new ArrayList<Node>();
	}

	public Path(Path path) {
		if (path == null) throw new NullPointerException("the path must be not null");
		this.nodes = new ArrayList<Node>(path.nodes);
	}

	public Node getNodeAtPosition(int pos) {
		if (pos < 1) throw new IllegalArgumentException("the position can't be negative or 0");
		if (pos > nodes.size())
			throw new IllegalArgumentException("the pos can't be less or equal than the current number of nodes in the path");
		return nodes.get(pos-1);
	}

	public void setNodeAtPosition(int pos, Node n) {
		if (pos < 1) throw new IllegalArgumentException("the position can't be negative or 0");
		if (pos > nodes.size())
			throw new IllegalArgumentException("the pos can't be less or equal than the current number of nodes in the path");
		if (n == null) throw new NullPointerException("n must be not null");
		nodes.set(pos-1, n);
	}

	/**
	 * Append a node to the end of the list
	 */
	public void appendNode(Node n) {
		if (n == null) throw new NullPointerException("n must be not null");
		nodes.add(n);
	}

	/**
	 * Remove the last node from the path
	 */
	public Node removeLastNode() {
		return nodes.remove(nodes.size() - 1);
	}

	@Override public Iterator<Node> iterator() {
		return nodes.iterator();
	}

	public ListIterator<Node> listIterator() {
		return nodes.listIterator();
	}

	public boolean contains(Node n) {
		for (Node nx : nodes) {
			if (nx.equals(n)) return true;
		}
		return false;
	}

	public int getLength() {
		return nodes.size() - 1;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for (int i = 0; i < nodes.size(); i++) {
			sb.append(nodes.get(i).toString());
			if (i < nodes.size() - 1) sb.append(" -> ");
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode(){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < nodes.size(); i++) {
			buffer.append(nodes.get(i).id);
		}
		return buffer.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		Path p = (Path)obj;
		if (this.nodes.size() != p.nodes.size()) return false;
		boolean equals = true;
		for (int i = 0; i < nodes.size(); i++) {
			if (!nodes.get(i).equals(p.nodes.get(i))) {
				equals = false;
				break;
			}
		}
		return equals;
	}

}
