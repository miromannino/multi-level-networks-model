package it.miromannino.multilevelnetwork.operator;

import it.miromannino.multilevelnetwork.model.NetworkLevel;
import it.miromannino.multilevelnetwork.model.Node;
import it.miromannino.multilevelnetwork.model.Path;
import it.miromannino.multilevelnetwork.model.PathSet;

import java.util.HashMap;
import java.util.Iterator;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class Synthesis {

	/**
	 * Create a new network level (with the ID {@code resultLevelID}), taking all the nodes in the path set {@code ps}
	 * and connecting them looking the order of nodes in the paths (i.e. if we have the path (a -> b -> c) in the
	 * new network level we will have a -> b and b -> c).
	 * The data of nodes and arcs are taken from the network level {@code nl}. Note that the nodes in {@code ps}
	 * is nodes of {@code nl}.
	 *
	 * @param resultLevelID identification of the new network level
	 * @param ps the path set that contains paths of {@code nl}
	 */
	public static NetworkLevel synthesize(String resultLevelID, PathSet ps) {
		NetworkLevel risLevel = new NetworkLevel(resultLevelID);
		HashMap<Node, Node> oldToNew = new HashMap<Node, Node>();

		for (Path p : ps) {
			Node n1 = null, n2 = null;
			for (Node n : p) {

				if (!oldToNew.containsKey(n)) {
					Node newN = risLevel.addNewNode(n.getId(), n.getData());
					oldToNew.put(n, newN);
				}

				if (n1 == null) {
					n1 = n;
				} else {
					n2 = n;
					risLevel.addNewArc(oldToNew.get(n1), oldToNew.get(n2), n1.getNeighborLinkData(n2));
					n1 = n2;
				}
			}
		}

		return risLevel;

	}

}
