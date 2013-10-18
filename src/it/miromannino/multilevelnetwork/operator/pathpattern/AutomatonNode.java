package it.miromannino.multilevelnetwork.operator.pathpattern;

import it.miromannino.multilevelnetwork.model.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class AutomatonNode {

	/**
	 * A list of neighbors with all the actions that can leads to them.
	 */
	List<AutomatonLink> neighborsLinks;

	/**
	 * Determines if the automata node is final or not.
	 */
	boolean finalNode;

	public AutomatonNode(boolean finalNode) {
		neighborsLinks = new ArrayList<AutomatonLink>();
	}

	public AutomatonNode() {
		this(false);
	}

	public void setFinalNode(boolean isFinalNode) {
		this.finalNode = isFinalNode;
	}

	public boolean isFinalNode() {
		return this.finalNode;
	}

	/**
	 * Determines if the automata node can do a particular action.
	 * @param actionType the action type
	 * @param actionArgument the argument (used for the type {@code AutomatonLinkActionType.SpecificNode})
	 * @return true if the node can do the action
	 */
	public boolean canDoAction(AutomatonLinkActionType actionType, Node actionArgument) {
		if (actionType == AutomatonLinkActionType.SpecificNode && actionArgument == null) throw new NullPointerException("the specified action type need an argument");
		for (AutomatonLink l : neighborsLinks) {
			if (l.actionArgument.equals(actionType) && l.actionArgument.equals(actionArgument)) return true;
		}
		return false;
	}

	/**
	 * Determines if the automaton node can do a particular action.
	 * @param actionType the action type (can't be the {@code AutomatonLinkActionType.SpecificNode})
	 * @return true if the node can do the action
	 */
	public boolean canDoAction(AutomatonLinkActionType actionType) {
		if (actionType == AutomatonLinkActionType.SpecificNode) throw new IllegalArgumentException("the specified action type need an argument");
		return canDoAction(actionType, null);
	}

	/**
	 * Return the iterator to explore the neighbors and the relative actions to reach them.
	 */
	public Iterator<AutomatonLink> getLinkIterator() {
		return neighborsLinks.iterator();
	}

	/**
	 * Add a link to the node, that can point to another node. Note that there are no controls for duplicates. This
	 * means that we can have two links with the same actionType that point to the same node. But we don't care
	 * about this because the automaton is constructed by an our method that take a well formed path pattern.
	 * @return
	 */
	public void addLink(AutomatonLink link) {
		this.neighborsLinks.add(link);
	}

}
