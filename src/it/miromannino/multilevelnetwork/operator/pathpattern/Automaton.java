package it.miromannino.multilevelnetwork.operator.pathpattern;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class Automaton {

	AutomatonNode initialNode, finalNode;

	public Automaton(PathPattern p) {
		if (p == null) throw new NullPointerException("the path pattern must be not null");

		AutomatonNode[] ris = createAutomaton(p);
		initialNode = ris[0];
		finalNode = ris[1];
		finalNode.setFinalNode(true);
	}

	/**
	 * Create the automaton nodes for the path pattern {@code p}. Returns a couple of nodes: the first is the initial
	 * node for the current sub-automaton and the second is the final node.
	 * @return
	 */
	private AutomatonNode[] createAutomaton(PathPattern p) {
		AutomatonNode[] ris = new AutomatonNode[2];

		if (p instanceof PathPattern.EmptyPath) {
			AutomatonNode n = new AutomatonNode();
			ris[0] = n;
			ris[1] = n;
		}

		else if (p instanceof PathPattern.GraphNode) {
			PathPattern.GraphNode pgn = (PathPattern.GraphNode) p;
			AutomatonNode n1 = new AutomatonNode(), n2 = new AutomatonNode();
			n1.addLink(new AutomatonLink(n2, AutomatonLinkActionType.SpecificNode, pgn.getNode()));
			ris[0] = n1;
			ris[1] = n2;
		}

		else if (p instanceof PathPattern.GenericNode) {
			AutomatonNode n1 = new AutomatonNode(), n2 = new AutomatonNode();
			n1.addLink(new AutomatonLink(n2, AutomatonLinkActionType.GeneralNode));
			ris[0] = n1;
			ris[1] = n2;
		}

		else if (p instanceof PathPattern.OptionalGenericNode) {
			AutomatonNode n1 = new AutomatonNode(), n2 = new AutomatonNode();
			n1.addLink(new AutomatonLink(n2, AutomatonLinkActionType.GeneralNode));
			n1.addLink(new AutomatonLink(n2, AutomatonLinkActionType.Epsilon));
			ris[0] = n1;
			ris[1] = n2;
		}

		else if (p instanceof PathPattern.GenericPath) {
			AutomatonNode n1 = new AutomatonNode(), n2 = new AutomatonNode();
			n1.addLink(new AutomatonLink(n2, AutomatonLinkActionType.Epsilon));
			n1.addLink(new AutomatonLink(n1, AutomatonLinkActionType.GeneralNode));
			ris[0] = n1;
			ris[1] = n2;
		}

		else if (p instanceof PathPattern.Concatenation) {
			PathPattern.Concatenation pc = (PathPattern.Concatenation) p;
			AutomatonNode[] risFrom = createAutomaton(pc.fromPath);
			AutomatonNode[] risTo = createAutomaton(pc.toPath);
			ris[0] = risFrom[0];
			ris[1] = risTo[1];
			risFrom[1].addLink(new AutomatonLink(risTo[0], AutomatonLinkActionType.Epsilon));
		}

		else if (p instanceof PathPattern.Alternation) {
			PathPattern.Alternation pc = (PathPattern.Alternation) p;
			AutomatonNode[] firstRis = createAutomaton(pc.firstPath);
			AutomatonNode[] secondRis = createAutomaton(pc.secondPath);
			AutomatonNode n1 = new AutomatonNode(), n2 = new AutomatonNode();
			n1.addLink(new AutomatonLink(firstRis[0], AutomatonLinkActionType.Epsilon));
			n1.addLink(new AutomatonLink(secondRis[0], AutomatonLinkActionType.Epsilon));
			firstRis[1].addLink(new AutomatonLink(n2, AutomatonLinkActionType.Epsilon));
			secondRis[1].addLink(new AutomatonLink(n2, AutomatonLinkActionType.Epsilon));
			ris[0] = n1;
			ris[1] = n2;
		}

		else {
			assert(false);
		}

		return ris;
	}

	public AutomatonNode getInitialNode() {
		return this.initialNode;
	}

	public AutomatonNode getFinalNode() {
		return this.finalNode;
	}

}
