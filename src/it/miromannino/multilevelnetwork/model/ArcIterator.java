package it.miromannino.multilevelnetwork.model;

import java.util.Iterator;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */

public class ArcIterator implements Iterator<Arc> {

	Iterator<Node> nodeIterator;
	Iterator<Link> linkIterator;
	Node currentNode;
	Link currentArc;

	public ArcIterator(Iterator<Node> nodeIterator) {
		this.nodeIterator = nodeIterator;
		linkIterator = null;
		currentNode = null;
	}

	public boolean hasNext() {
		if (currentNode != null && linkIterator.hasNext()) return true;

		boolean nodeFounded = false;
		while(!nodeFounded) {
			if (!nodeIterator.hasNext()) return false;
			currentNode = nodeIterator.next();
			linkIterator = currentNode.getLinksIterator();
			nodeFounded = linkIterator.hasNext();
		}
		return linkIterator.hasNext();
	}

	public Arc next() {
		if (currentNode == null || !linkIterator.hasNext()) {
			boolean nodeFounded = false;
			while(!nodeFounded) {
				currentNode = nodeIterator.next();
				linkIterator = currentNode.getLinksIterator();
				nodeFounded = linkIterator.hasNext();
			}
		}
		currentArc = linkIterator.next();
		return new Arc(currentNode, currentArc.destinationNode, currentArc.data);
	}

	public void remove() {
		//if (currentArc != null) currentNode.removeNeighbor(currentArc.destinationNode);
		if (linkIterator != null) linkIterator.remove();
	}
}
