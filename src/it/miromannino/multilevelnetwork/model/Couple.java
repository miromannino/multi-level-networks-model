package it.miromannino.multilevelnetwork.model;

import java.util.*;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class Couple implements Iterable<CoupleLink> {

	NetworkLevel s, d;
	List<CoupleLink> M; //the w function (data of M elements) is in the CoupleLinks: we don't need it.

	/**
	 * Create a new Couple from the Network Level {@code s} to the Network Level {@code d}
	 */
	public Couple(NetworkLevel s, NetworkLevel d) {
		this.s = s;
		this.d = d;
		M = new ArrayList<CoupleLink>();
	}

	/**
	 * Add a new couple link
	 * @param departureNode the node where the couple link starts
	 * @param destinationNode the node where the couple link finish
	 * @param data the data associated with this couple link
	 * @param check check that the {@code departureNode} and the {@code destinationNode} is in the correct Network Levels.
	 *              With check enabled the method return an {@code IllegalArgumentException} if the nodes aren't in the Network Levels.
	 */
	public void addNewCoupleLink(Node departureNode, Node destinationNode, Object data, boolean check) {
		if (departureNode == null) throw new NullPointerException("departureNode must be not null");
		if (destinationNode == null) throw new NullPointerException("destinationNode must be not null");
		if (check) {
			if (!s.containsNode(departureNode)) throw new IllegalArgumentException("the departureNode must be in the s Network Level");
			if (!d.containsNode(destinationNode)) throw new IllegalArgumentException("the destinationNode must be in the d Network Level");
		}
		M.add(new CoupleLink(departureNode, destinationNode, data));
	}

	/**
	 * Add a new couple link
	 * @param departureNode the node where the couple link starts
	 * @param destinationNode the node where the couple link finish
	 * @param check check that the {@code departureNode} and the {@code destinationNode} is in the correct Network Levels.
	 *              With check enabled the method return an {@code IllegalArgumentException} if the nodes aren't in the Network Levels.
	 */
	public void addNewCoupleLink(Node departureNode, Node destinationNode, boolean check) {
		addNewCoupleLink(departureNode, destinationNode, null, check);
	}

	/**
	 * Add a new couple link (Note: no checks for the correctness of the nodes, they can be not in the correct Network Levels)
	 * @param departureNode the node where the couple link starts
	 * @param destinationNode the node where the couple link finish
	 */
	public void addNewCoupleLink(Node departureNode, Node destinationNode) {
		addNewCoupleLink(departureNode, destinationNode, null, false);
	}

	/**
	 * Add a new couple link (Note: no checks for the correctness of the nodes, they can be not in the correct Network Levels)
	 * @param departureNode the node where the couple link starts
	 * @param destinationNode the node where the couple link finish
	 * @param data the data associated with this couple link
	 */
	public void addNewCoupleLink(Node departureNode, Node destinationNode, Object data) {
		addNewCoupleLink(departureNode, destinationNode, data, false);
	}

	public Iterator<CoupleLink> iterator() {
		return M.iterator();
	}

	public NetworkLevel getSourceNetworkLevel() {
		return s;
	}

	public NetworkLevel getDestinationNetworkLevel() {
		return d;
	}

	public List<CoupleLink> getM() {
		return M;
	}

}
