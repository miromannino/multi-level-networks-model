package it.miromannino.multilevelnetwork.operator;

import it.miromannino.multilevelnetwork.model.NetworkLevel;
import it.miromannino.multilevelnetwork.model.Node;
import it.miromannino.multilevelnetwork.model.Path;
import it.miromannino.multilevelnetwork.model.PathSet;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class Aggregation {

	/**
	 * This node can be used by the c function to generate new nodes.
	 * This node is static and it's only one because it only serves as annotation.
	 * After the c function has created the path, the aggregate operator create a new corresponding node.
	 */
	public static final Node NewNode = new Node("GenericNewNode");

	/**
	 * The c function. It create the aggregate path from an existing path.
	 * This can create new nodes using only the {@code Aggregation.NewNode}. These nodes will be translated.
	 */
	public abstract static class CFunction {

		public abstract Path cFunction(Path p);

	}

	/**
	 * It have a callback method: the {@code changeData}. It takes the current path (a path of the input path set), and
	 * takes the relative aggregate path. One can modify the data, doing some operations.
	 *
	 * e.g.
	 *
	 * If we have the current path (a -> b), and the data in the arc is 10, and
	 * if the operator is called to do aggregate paths like (a -> %),
	 * one can do the SUM aggregate operation in this way:
	 *
	 *   take the aggregatePath arc data of (a -> %)
	 *     - if null we initialize it with the currentPath arc data of (a -> b)
	 *     - if not null we update it with the currentPath arc data of (a -> b)
	 *
	 */
	public abstract static class Assignments {

		public abstract void changeData(Path aggregatePath, Path currentPath);
	}


	/**
	 * A path set {@code ps} is a set of paths of a network level. The nodes data and the links data are taken directly
	 * from the node (we don't need the network level reference as described in the thesis).
	 * @param resultLevelID the ID of the new network level that will be created as result
	 * @param ps a path set of paths of a network level
	 * @param cFunc the c function that modify the current path to create a new path (that can have new nodes)
	 * @param a the assignments that change the data in the new nodes, and do operations like the SUM, AVG, etc.
	 * @param newNodeID the new nodes prefix (e.g. if it is "nn" we will have the new nodes with names: "nn1", "nn2", ...)
	 * @return a new network level, that is the synthesis of the H set, like you can read in the thesis.
	 */
	public static NetworkLevel aggregate(String resultLevelID, PathSet ps, CFunction cFunc, Assignments a, String newNodeID) {

		NetworkLevel ris = new NetworkLevel(resultLevelID);
		HashMap<Node, Node> oldToNew = new HashMap<Node, Node>();
		List<Path> H = new LinkedList<Path>();
		int newNodeIDNum = 1;

		for (Path p : ps) {
			Path p2 = cFunc.cFunction(p);
			Path pn = new Path();

			//Create a new path pn with all new nodes (but we don't modify any NewNode)
			for (Node n : p2) {
				if (n.equals(NewNode)) {
					pn.appendNode(NewNode);
				} else if (oldToNew.containsKey(n)) {
					pn.appendNode(oldToNew.get(n));
				} else {
					Node nn = ris.addNewNode(n.getId(), n.getData());
					pn.appendNode(nn);
					oldToNew.put(n, nn);
				}
			}

			Path phf = null; //the path that is equal to pn (is different in the NewNodes)
			for (Path ph : H) {
				if (ph.getLength() == pn.getLength()) {
					boolean eq = true;

					//is equal (we don't check the NewNodes)
					for (int i = 1; i <= ph.getLength() + 1; i++) {
						if (!pn.getNodeAtPosition(i).equals(NewNode)
								&& !pn.getNodeAtPosition(i).equals(ph.getNodeAtPosition(i))) {
							eq = false;
							break;
						}
					}

					//if equal we have found it
					if (eq == true) {
						phf = ph;
						break;
					}
				}
			}

			//we don't found any equal path in H
			if (phf == null) {

				//we need to create the NewNodes
				for (int i = 1; i <= pn.getLength() + 1; i++) {
					if (pn.getNodeAtPosition(i).equals(NewNode)) {
						Node nn = ris.addNewNode(newNodeID + newNodeIDNum);
						newNodeIDNum++;
						pn.setNodeAtPosition(i, nn);
					}
				}

				//add the path in H
				H.add(pn);

				//copy all the arc data
				for (int i = 1; i <= p2.getLength(); i++) {
					Node n1 = p2.getNodeAtPosition(i);
					Node n2 = p2.getNodeAtPosition(i+1);
					Node n1n = pn.getNodeAtPosition(i);
					Node n2n = pn.getNodeAtPosition(i+1);
					n1n.addNeighbor(n2n, n1.getNeighborLinkData(n2));
				}

				phf = pn;

			}

			a.changeData(phf, p);

		}

		return ris;

	}

}
