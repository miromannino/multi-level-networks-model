package it.miromannino.multilevelnetwork.inout;

import it.miromannino.multilevelnetwork.generic.MutableInt;
import it.miromannino.multilevelnetwork.operator.pathpattern.Automaton;
import it.miromannino.multilevelnetwork.operator.pathpattern.AutomatonLink;
import it.miromannino.multilevelnetwork.operator.pathpattern.AutomatonLinkActionType;
import it.miromannino.multilevelnetwork.operator.pathpattern.AutomatonNode;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class PathPatternAutomatonDotExport {

	static final String NODE_NAME_PREFIX = "s";
	static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Write, using the Writer object, the dot format to represent the automaton
	 * @param automaton the automaton to export
	 * @param fw the Writer object used to write the dot file (the method doesn't close it).
	 */
	public static void export(Automaton automaton, Writer fw) throws IOException {
		 MutableInt nodeIdNum = new MutableInt(0);

		HashMap<AutomatonNode, String> nodeNameMap = new HashMap<AutomatonNode, String>();
		fw.write("digraph automaton {" + LINE_SEPARATOR);
			nodeIdNum.incrementByOne();
			nodeNameMap.put(automaton.getInitialNode(), NODE_NAME_PREFIX + nodeIdNum);
			export(automaton.getInitialNode(), fw, nodeNameMap, nodeIdNum);
		fw.write("}");
	}

	private static void export(AutomatonNode node, Writer fw,
							   Map<AutomatonNode, String> nodeNameMap, MutableInt nodeIdNum) throws IOException {

		assert(nodeNameMap.containsKey(node));
		if (node.isFinalNode()) {
			fw.write(nodeNameMap.get(node) + "[label = \"" + nodeNameMap.get(node) + "(final)\"]");
		}

		Iterator<AutomatonLink> it = node.getLinkIterator();
		while(it.hasNext()) {
			AutomatonLink l = it.next();
			AutomatonNode destinationNode = l.getDestinationNode();
			boolean destinationNodeIsNew = false;

			//memorize the new node if this is the first time that we see it
			if (!nodeNameMap.containsKey(destinationNode)) {
				//This is the first time that we encounter this node, increment the node id and put it to the map
				nodeIdNum.incrementByOne();
				nodeNameMap.put(destinationNode, NODE_NAME_PREFIX + nodeIdNum);
				destinationNodeIsNew = true;
			}

			//write the arc, and the actionType value.
			fw.write("\t" + nodeNameMap.get(node) + "->" + nodeNameMap.get(destinationNode));
			if (l.getActionType().equals(AutomatonLinkActionType.Epsilon)) {
				fw.write(" [label = \"ε\"]");
			} else if (l.getActionType().equals(AutomatonLinkActionType.GeneralNode)) {
				fw.write(" [label = \"%\"]");
			} else if (l.getActionType().equals(AutomatonLinkActionType.SpecificNode)) {
				fw.write(" [label = \"" + l.getActionArgument().getId() + "\"]");
			}
			fw.write(";" + LINE_SEPARATOR);

			//recursion
			if (destinationNodeIsNew) {
				export(destinationNode, fw, nodeNameMap, nodeIdNum);
			}

		}
	}
}
