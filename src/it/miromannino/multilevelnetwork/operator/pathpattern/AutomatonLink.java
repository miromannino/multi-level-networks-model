package it.miromannino.multilevelnetwork.operator.pathpattern;

import it.miromannino.multilevelnetwork.model.Node;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */

public class AutomatonLink {

	/**
	 * The node where the link points
	 * E.g. if we have the node A, the link to B means that we have an arc from A to B
	 * */
	protected AutomatonNode destinationNode;

	protected AutomatonLinkActionType actionType;

	protected Node actionArgument;

	/**
	 * Create a new automaton link that points to the {@code destinationNode}.
	 * @param destinationNode the destination node, where the link points to.
	 * @param actionType the action type, (one of those in {@code AutomatonLinkActionType})
	 * @param actionArgument the action argument, used only with the action type {@code SpecificNode}
	 */
	public AutomatonLink(AutomatonNode destinationNode, AutomatonLinkActionType actionType, Node actionArgument) {
		if (destinationNode == null) throw new NullPointerException("destinationNode must be not null");
		if (actionType == AutomatonLinkActionType.SpecificNode && actionArgument == null) throw new NullPointerException("the specified action type need an argument");

		this.destinationNode = destinationNode;
		this.actionType = actionType;
		this.actionArgument = actionArgument;
	}

	/**
	 * Create a new automaton link that points to the {@code destinationNode}.
	 * @param destinationNode the destination node, where the link points to.
	 * @param actionType the action type, (one of those in {@code AutomatonLinkActionType}, but can't be the {@code SpecifiNode})
	 */
	public AutomatonLink(AutomatonNode destinationNode, AutomatonLinkActionType actionType) {
		if (actionType == AutomatonLinkActionType.SpecificNode) throw new IllegalArgumentException("the specified action type need an argument");
		this.destinationNode = destinationNode;
		this.actionType = actionType;
	}

	/**
	 * Return the destination node, the automaton node where the link points to.
	 */
	public AutomatonNode getDestinationNode() {
		return destinationNode;
	}

	/**
	 * Return the action type, one of those in {@code AutomatonLinkActionType}
	 */
	public AutomatonLinkActionType getActionType() {
		return actionType;
	}

	/**
	 * Return the action argument, that is not null if the action type is {@code SpecificNode}. It is the node
	 * that is needed to do the action from the node that has the this link, to ne destination node of this link.
	 */
	public Node getActionArgument() {
		return actionArgument;
	}

}