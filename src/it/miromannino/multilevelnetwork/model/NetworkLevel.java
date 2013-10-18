package it.miromannino.multilevelnetwork.model;

import java.util.HashMap;
import java.util.Iterator;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class NetworkLevel {

	/*
	 * The readable identification for the network level
	 * */
	String id;

	/**
	 * The set of nodes (we can't use the HashSet because it doesn't have the get method).
	 * */
	HashMap<String, Node> N;

	public NetworkLevel(String id) {
		this.id = id;
		N = new HashMap<String, Node>();
	}

	public String getId() {
		return id;
	}

	/**
	 * Add a node to the network level, creating a node with the specified id and data. The method return the new node
	 * that is created and added to the network level.
	 * @param id the node identificator (must be a valid node identificator)
	 * @param data the node data
	 * @return the node that is created and inserted, null if not (e.g. exists a node with the same id in {@code N}).
	 */
	public Node addNewNode(String id, Object data) {
		Node node = new Node(id, data);

		//If there is a node with the same id, this is a duplicate
		if (N.containsKey(node.id)) return null;

		N.put(node.id, node);
		return node;
	}

	public Node addNewNode(String id) {
		return addNewNode(id, null);
	}

	/**
	 * Add an arc to the network level
	 * @param fromNode the node where the arc starts, must be not null and be in the NetworkLevel
	 * @param toNode the node where the arc finishes, must be not null and be in the NetworkLevel
	 * @param data the data of the arc that connect the {@code fromNode} node or the {@code toNode} node
	 * @throws IllegalArgumentException if the {@code fromNode} node or the {@code toNode} node aren't in {@code N}
	 * @return true if the arc is inserted, false if not (it is yet in the network).
	 */
	public boolean addNewArc(Node fromNode, Node toNode, Object data) {
		if (fromNode == null) throw new NullPointerException("fromNode must be not null");
		if (toNode == null) throw new NullPointerException("toNode must be not null");
		if (!N.containsValue(fromNode))
			throw new IllegalArgumentException("the fromNode doesn't exist in the network level");
		if (!N.containsValue(toNode))
			throw new IllegalArgumentException("the toNode doesn't exist in the network level");

		return fromNode.addNeighbor(toNode, data);
	}

	/**
	 * Add an arc to the network level
	 * @param fromNode the node where the arc starts, must be not null and be in the NetworkLevel
	 * @param toNode the node where the arc finishes, must be not null and be in the NetworkLevel
	 * @throws IllegalArgumentException if the {@code fromNode} node or the {@code toNode} node aren't in {@code N}
	 * @return true if the arc is inserted, false if not (it is yet in the network).
	 */
	public boolean addNewArc(Node fromNode, Node toNode) {
		return addNewArc(fromNode, toNode, null);
	}

	public boolean containsNode(Node n) {
		if (n == null) throw new NullPointerException("the node must be not null");
		return this.N.containsValue(n);
	}

	public boolean containsArc(Node fromNode, Node toNode) {
		if (fromNode == null) throw new NullPointerException("fromNode must be not null");
		if (toNode == null) throw new NullPointerException("toNode must be not null");
		if (!this.N.containsKey(fromNode)) return false;
		return fromNode.hasNeighbor(toNode);
	}

	public Iterator<Node> getNodeIterator() {
		return N.values().iterator();
	}

	public ArcIterator getArcIterator() {
		return new ArcIterator(N.values().iterator());
	}

}
