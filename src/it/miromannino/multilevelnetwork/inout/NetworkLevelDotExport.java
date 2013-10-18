package it.miromannino.multilevelnetwork.inout;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */

import it.miromannino.multilevelnetwork.model.Arc;
import it.miromannino.multilevelnetwork.model.NetworkLevel;
import it.miromannino.multilevelnetwork.model.Node;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class helps to take a Network Level and export it to the Graph Viz dot format
 */
public class NetworkLevelDotExport {

	static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Write, using the Writer object, the dot format to represent the graph
	 * @param nl the Network Level to export
	 * @param fw the Writer object used to write the dot file (the method doesn't close it).
	 */
	public static void export(NetworkLevel nl, Writer fw) throws IOException {
		fw.write("digraph " + nl.getId() + " {" + LINE_SEPARATOR);
		Iterator<Node> it = nl.getNodeIterator();
		while(it.hasNext()) {
			Node n = it.next();
			fw.write("\t" + n.getId());
			if (n.getData() != null) {
				fw.write(" [label = \"" + n.getId() + " [" + String.valueOf(n.getData()) + "]\"]");
			}
			fw.write(";" + LINE_SEPARATOR);
		}
		Iterator<Arc> itA = nl.getArcIterator();
		while(itA.hasNext()) {
			Arc a = itA.next();
			fw.write("\t" + a.getFromNode().getId() + "->" + a.getToNode().getId() );
			if (a.getData() != null) {
				fw.write(" [label = \"" + String.valueOf(a.getData()) + "\"]");
			}
			fw.write(";" + LINE_SEPARATOR);
		}
		fw.write("}");
	}

}
