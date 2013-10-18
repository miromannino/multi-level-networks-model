package it.miromannino.multilevelnetwork.operator;

import it.miromannino.multilevelnetwork.model.Node;
import it.miromannino.multilevelnetwork.model.Path;

import java.util.Iterator;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class Concatenation {

	/**
	 * Return the concatenation between two path. The resulting length is {@code p1.length + p2.length + 1}
	 * @param p1 if empty p2 is returned (must be not null)
	 * @param p2 if empty p1 is returned (must be not null)
	 * @return the resulting path (not necessary a new one, can be one of the argument if there are empty paths)
	 */
	public static Path concatenate(Path p1, Path p2) {
		if (p1 == null || p2 == null) throw new NullPointerException("the paths must be not null");

		//Little optimizations
		if (p1.getLength() == -1) return p2;
		if (p2.getLength() == -1) return p1;

		//Path ris = new Path(p1.getLength() + p2.getSize() + 1);
		Path ris = new Path();
		for (Node n : p1) {
			ris.appendNode(n);
		}
		for (Node n : p2) {
			ris.appendNode(n);
		}
		return ris;
	}

}
