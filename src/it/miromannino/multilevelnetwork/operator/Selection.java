package it.miromannino.multilevelnetwork.operator;

import it.miromannino.multilevelnetwork.model.NetworkLevel;
import it.miromannino.multilevelnetwork.model.Node;
import it.miromannino.multilevelnetwork.model.Path;
import it.miromannino.multilevelnetwork.model.PathSet;
import it.miromannino.multilevelnetwork.operator.pathpattern.*;

import java.util.Iterator;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class Selection {

	/**
	 * The selection operator. It return the set of paths that satisfy the path pattern and the path condition.
	 * @param nl the network level where we want to do the selection operator
	 * @param pattern the path pattern
	 * @param pc the path condition that determines if a path is correct or not
	 * @return a new path set with all the paths that satisfy the path pattern and the path condition
	 */
	public static PathSet select(NetworkLevel nl, PathPattern pattern, PathCondition pc) {
		if (nl == null) throw new NullPointerException("the network level must be not null");
		if (pattern == null) throw new NullPointerException("the path pattern must be not null");
		if (pc == null) throw new NullPointerException("the path condition must be not null");

		Automaton a = new Automaton(pattern);
		PathSet pathSet = new PathSet();
		Path currentPath = new Path(new Node[0]);

		/* Find the paths. The first time we have the currentNode to null, to indicate that for the first node the
		 * selection can use one of the nodes in the network level.
		 */
		select(nl, null, a.getInitialNode(), pathSet, currentPath, pc);

		return pathSet;

	}

	/**
	 * This is called only by the select operator, one can't call this directly, because is not easy to use.
	 * @param nl the network level
	 * @param currentNode the current node that determines the nodes that we can use to build the path.
	 *                    If null the nodes are the set of nodes in the network level
	 *                    If {@code n} the nodes are the set of {@code n}'s neighbors.
	 * @param currentAutomatonNode The current automaton node
	 * @param pathSet the path set that we're building
	 * @param currentPath the current path (we can do this only because it is a depth-first algorithm)
	 * @param pc the path condition
	 */
	private static void select(NetworkLevel nl,
							   Node currentNode,
							   AutomatonNode currentAutomatonNode,
							   PathSet pathSet,
							   Path currentPath,
							   PathCondition pc) {

		/* If it is a final automata node we need to add the path (a copy of it) in the path set, and then return,
		 * because we don't need to explore the possible action (it doesn't have any action).
		 */
		if (currentAutomatonNode.isFinalNode()) {
			if (pc.predicate(currentPath).equals(PathCondition.ConditionResult.True)) {
				if (!pathSet.containsPath(currentPath)) pathSet.addPath(new Path(currentPath));
			}
			return;
		}

		/* Explore the automaton node action
		 *  - if we find an epsilon action we must do the action, go to the destination node, and recall the select
		 *    method with this automaton node
		 *
		 *  - if we find a general node we can use any node to do the action
		 *  - if we find a specific node we can use only the specific node for the action
		 *    in these cases we need to recall the select method with the destination automaton node, and with the path
		 *    that contains the node that can be used to do the action.
		 */
		Iterator<AutomatonLink> alIt = currentAutomatonNode.getLinkIterator();
		while(alIt.hasNext()) {
			AutomatonLink al = alIt.next();
			if (al.getActionType().equals(AutomatonLinkActionType.Epsilon)) {
				select(nl, currentNode, al.getDestinationNode(), pathSet, currentPath, pc);
			} else {
				Iterator<Node> nIt = (currentNode == null) ? nl.getNodeIterator() : currentNode.getNeighborIterator();
				while(nIt.hasNext()) {
					Node n = nIt.next();
					if (al.getActionType().equals(AutomatonLinkActionType.SpecificNode)
							&& !al.getActionArgument().equals(n)) {
						continue;
					}

					/* here we are sure that the link is a GeneralNode,
					 * or it is a SpecificNode (and we have the correct node)
					 */

					//check if we saw the node
					if (currentPath.contains(n)) continue; //To prevent

					//the current node n is correct, we can append it to the current path and continue to explore
					currentPath.appendNode(n);

					/* If the predicate tells us that expanding the path we will have always a false predicate, we can
					 * avoid to explore, (we are following the branch and bound optimizations that you can see in the thesis)
					 */
					if (!pc.predicate(currentPath).equals(PathCondition.ConditionResult.DefinitelyFalse)) {
						//continue to explore
						select(nl, n, al.getDestinationNode(), pathSet, currentPath, pc);
					}

					/* remove it to have the original current path
					 * (this could be also done with copies, but is less efficient) */
					currentPath.removeLastNode();

				}

			}
		}

		return; //Only to visualize the end of the method

	}

}
