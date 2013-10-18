import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import it.miromannino.multilevelnetwork.model.*;

import java.util.Iterator;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class ModelTest {

	private static final int NUM_NODES = 20;
	Node[] n = new Node[NUM_NODES];
	Node n0_2, n0_3, n0_4;

	@Before
	public void setUp() {
		for (int i = 0; i < NUM_NODES; i++) {
			n[i] = new Node(String.valueOf((char) (i + 'a')));
		}
		n0_2 = new Node(Character.toString('a'));
		n0_3 = new Node("a", 42);
		n0_4 = new Node("a", 24);
	}

	@Test
	public void nodeIdTests() {
		String[] strs = {"a", "a2", "a2a", "A2s2390", "2", "2A", "A=", ")sdf"};
		int errs = 0;
		for (String s : strs) {
			try {
				new Node(s);
			} catch (IllegalArgumentException ex) {
				errs++;
			}
		}
		Assert.assertEquals(errs, 4);
	}

	@Test
	public void nodeTests() {
		Assert.assertFalse(n[0].equals(n[1]));
		Assert.assertFalse(n[0].getId().equals(n[1].getId()));
		Assert.assertTrue(n[0].getId().equals(n0_2.getId()));
		Assert.assertFalse(n[0].equals(n0_2));
		Assert.assertFalse(n[0].equals(n0_3));
		Assert.assertFalse(n[0].equals(n0_4));
		Assert.assertFalse(n0_3.equals(n0_4));
		Assert.assertFalse(n0_3.getData().equals(n0_4.getData()));
		Assert.assertTrue(n0_3.getData().equals(42));
		Assert.assertTrue(n0_4.getData().equals(24));
	}

	@Test
	public void linksTests() {
		for (int i = 0; i < NUM_NODES; i++) Assert.assertFalse(n[i].getNeighborIterator().hasNext());

		n[0].addNeighbor(n[1]);
		Assert.assertTrue(n[0].getNeighborIterator().hasNext());
		Assert.assertTrue(n[0].hasNeighbor(n[1]));
		Assert.assertFalse(n[1].hasNeighbor(n[0]));
		Assert.assertFalse(n[1].getNeighborIterator().hasNext());

		Assert.assertFalse(n[0].addNeighbor(n[1]));
		int count = 0;
		Iterator<Node> itN = n[0].getNeighborIterator();
		while(itN.hasNext()) {
			count++;
			Node ng = itN.next();
			Assert.assertTrue(ng.equals(n[1]));
			Assert.assertTrue(n[0].getNeighborLinkData(ng) == null);
		}
		Assert.assertTrue(count == 1);
		Iterator<Link> itL = n[0].getLinksIterator();
		count = 0;
		while(itL.hasNext()) {
			count++;
			Link l = itL.next();
			Assert.assertTrue(l.getDestinationNode().equals(n[1]));
			Assert.assertTrue(l.getData() == null);
		}
		Assert.assertTrue(count == 1);

		Assert.assertTrue(n[0].addNeighbor(n[2]));
		count = 0;
		itN = n[0].getNeighborIterator();
		while(itN.hasNext()) {
			count++;
			Node ng = itN.next();
			Assert.assertTrue(ng.equals(n[1]) || ng.equals(n[2]));
			Assert.assertTrue(n[0].getNeighborLinkData(ng) == null);
		}
		Assert.assertTrue(count == 2);
		itL = n[0].getLinksIterator();
		count = 0;
		while(itL.hasNext()) {
			count++;
			Link l = itL.next();
			Assert.assertTrue(l.getDestinationNode().equals(n[1]) || l.getDestinationNode().equals(n[2]));
			Assert.assertTrue(l.getData() == null);
		}
		Assert.assertTrue(count == 2);

		Assert.assertTrue(n[0].addNeighbor(n[3], 42));
		Assert.assertTrue(n[0].hasNeighbor(n[3]));
		Assert.assertTrue(n[0].getNeighborLinkData(n[3]).equals(42));
		count = 0;
		itN = n[0].getNeighborIterator();
		while(itN.hasNext()) {
			count++;
			itN.next();
		}
		Assert.assertTrue(count == 3);
		itL = n[0].getLinksIterator();
		count = 0;
		while(itL.hasNext()) {
			count++;
			itL.next();
		}
		Assert.assertTrue(count == 3);

		for(int i = 1; i < NUM_NODES; i++) {
			if (i <= 3) {
				Assert.assertFalse(n[0].addNeighbor(n[i]));
			} else {
				Assert.assertTrue(n[0].addNeighbor(n[i]));
			}
		}
		for(int i = 1; i < NUM_NODES; i++) {
			Assert.assertFalse(n[0].addNeighbor(n[i]));
		}
		count = 0;
		itN = n[0].getNeighborIterator();
		while(itN.hasNext()) {
			count++;
			itN.next();
		}
		Assert.assertTrue(count == NUM_NODES-1);
		itL = n[0].getLinksIterator();
		count = 0;
		while(itL.hasNext()) {
			count++;
			itL.next();
		}
		Assert.assertTrue(count == NUM_NODES-1);

		itN = n[0].getNeighborIterator();
		while(itN.hasNext()) {
			itN.next();
			itN.remove();
		}
		itN = n[0].getNeighborIterator();
		Assert.assertFalse(itN.hasNext());
		itL = n[0].getLinksIterator();
		Assert.assertFalse(itL.hasNext());

		for(int i = 1; i < NUM_NODES; i++) {
			Assert.assertTrue(n[0].addNeighbor(n[i]));
		}
		itL = n[0].getLinksIterator();
		count = 0;
		while(itL.hasNext()) {
			count++;
			itL.next();
		}
		Assert.assertTrue(count == NUM_NODES-1);
		for (int i = 1; i < NUM_NODES; i++) {
			n[0].removeNeighbor(n[i]);
		}
		itN = n[0].getNeighborIterator();
		Assert.assertFalse(itN.hasNext());
		itL = n[0].getLinksIterator();
		Assert.assertFalse(itL.hasNext());


	}

	@Test
	public void arcTests() {
		Arc a1 = new Arc(n[0], n[1]);
		Arc a2 = new Arc(n[1], n[0]);
		Arc a3 = new Arc(n[0], new Node("b"));
		Arc a4 = new Arc(n[0], n[1], 55);
		Arc a5 = new Arc(n[0], n[1], 42);

		Assert.assertFalse(a1.equals(a2));
		Assert.assertFalse(a1.equals(a3));
		Assert.assertTrue(a1.equals(a4));
		Assert.assertTrue(a1.equals(a5));
		Assert.assertFalse(a2.equals(a3));
		Assert.assertFalse(a2.equals(a4));
		Assert.assertFalse(a2.equals(a5));
		Assert.assertFalse(a3.equals(a4));
		Assert.assertFalse(a3.equals(a5));
		Assert.assertTrue(a4.equals(a5));

		Assert.assertFalse(a5.getData().equals(a4.getData()));
		Assert.assertTrue(a4.getData().equals(55));
		Assert.assertTrue(a5.getData().equals(42));
	}

	@Test
	public void networkLevelInsertingTests() {

		NetworkLevel nl = new NetworkLevel("nl1");
		Node[] nodes = new Node[4];

		Assert.assertTrue((nodes[0] = nl.addNewNode("a")) != null);
		Assert.assertTrue((nodes[1] = nl.addNewNode("b")) != null);
		Assert.assertTrue((nodes[2] = nl.addNewNode("c")) != null);
		Assert.assertTrue((nodes[3] = nl.addNewNode("d")) != null);
		Assert.assertTrue(nl.addNewNode("a") == null);
		Assert.assertTrue(nl.containsNode(nodes[0]));
		Assert.assertTrue(nl.containsNode(nodes[1]));
		Assert.assertTrue(nl.containsNode(nodes[2]));
		Assert.assertTrue(nl.containsNode(nodes[3]));
		Assert.assertFalse(nl.containsNode(n[0]));
		Assert.assertFalse(nl.containsNode(n[1]));
		Assert.assertFalse(nl.containsNode(n[2]));
		Assert.assertFalse(nl.containsNode(n[3]));

		Assert.assertTrue(nl.addNewArc(nodes[0], nodes[1]));
		Assert.assertTrue(nl.addNewArc(nodes[1], nodes[2]));
		Assert.assertTrue(nl.addNewArc(nodes[2], nodes[3]));
		Assert.assertTrue(nl.addNewArc(nodes[3], nodes[0]));
		Assert.assertFalse(nl.addNewArc(nodes[0], nodes[1]));
		boolean error = false;
		try {
			nl.addNewArc(nodes[0], n[1]);
		} catch(IllegalArgumentException ex) {
			error = true;
		}
		Assert.assertTrue(error);
		error = false;
		try {
			nl.addNewArc(n[1], nodes[0]);
		} catch(IllegalArgumentException ex) {
			error = true;
		}
		Assert.assertTrue(error);
		error = false;
		try {
			nl.addNewArc(n[0], n[1]);
		} catch(IllegalArgumentException ex) {
			error = true;
		}
		Assert.assertTrue(error);

		Assert.assertTrue(nl.addNewArc(nodes[0], nodes[2], 42));

		Assert.assertTrue(nodes[0].hasNeighbor(nodes[1]));
		Assert.assertTrue(nodes[0].hasNeighbor(nodes[2]));
		Assert.assertTrue(nodes[1].hasNeighbor(nodes[2]));
		Assert.assertTrue(nodes[2].hasNeighbor(nodes[3]));
		Assert.assertTrue(nodes[3].hasNeighbor(nodes[0]));

		Assert.assertFalse(n[0].hasNeighbor(n[1]));
		Assert.assertFalse(n[0].hasNeighbor(n[2]));
		Assert.assertFalse(n[1].hasNeighbor(n[2]));
		Assert.assertFalse(n[2].hasNeighbor(n[3]));
		Assert.assertFalse(n[3].hasNeighbor(n[0]));

		Assert.assertFalse(nodes[0].hasNeighbor(n[1]));
		Assert.assertFalse(nodes[0].hasNeighbor(n[2]));
		Assert.assertFalse(nodes[1].hasNeighbor(n[2]));
		Assert.assertFalse(nodes[2].hasNeighbor(n[3]));
		Assert.assertFalse(nodes[3].hasNeighbor(n[0]));

	}

	@Test
	public void arcIteratorTests() {
		NetworkLevel nl = new NetworkLevel("nl1");
		final int NUM_NODES = 10;
		Node[] nodes = new Node[NUM_NODES];

		for (int i = 0; i < NUM_NODES; i++) {
			nodes[i] = nl.addNewNode(String.valueOf((char) ('a'+ i)));
		}

		nl.addNewArc(nodes[0], nodes[1]);
		nl.addNewArc(nodes[1], nodes[2]);
		nl.addNewArc(nodes[2], nodes[3]);
		nl.addNewArc(nodes[3], nodes[0]);
		nl.addNewArc(nodes[1], nodes[4], 33);
		nl.addNewArc(nodes[5], nodes[6]);
		nl.addNewArc(nodes[6], nodes[7], 42);
		nl.addNewArc(nodes[6], nodes[8]);
		nl.addNewArc(nodes[6], nodes[9]);

		ArcIterator it = nl.getArcIterator();
		Assert.assertTrue(it.hasNext());
		boolean[] present = new boolean[9];
		for (int i = 0; i < 9; i++) present[i] = false;
		while (it.hasNext()) {
			Arc arc = it.next();
			if (arc.getFromNode().equals(nodes[0]) && arc.getToNode().equals(nodes[1])) {
				Assert.assertFalse(present[0]);
				present[0] = true;
			}
			if (arc.getFromNode().equals(nodes[1]) && arc.getToNode().equals(nodes[2])) {
				Assert.assertFalse(present[1]);
				present[1] = true;
			}
			if (arc.getFromNode().equals(nodes[2]) && arc.getToNode().equals(nodes[3])) {
				Assert.assertFalse(present[2]);
				present[2] = true;
			}
			if (arc.getFromNode().equals(nodes[3]) && arc.getToNode().equals(nodes[0])) {
				Assert.assertFalse(present[3]);
				present[3] = true;
			}
			if (arc.getFromNode().equals(nodes[1]) && arc.getToNode().equals(nodes[4])) {
				Assert.assertTrue(arc.getData().equals(33));
				Assert.assertFalse(present[4]);
				present[4] = true;
			}
			if (arc.getFromNode().equals(nodes[5]) && arc.getToNode().equals(nodes[6])) {
				Assert.assertFalse(present[5]);
				present[5] = true;
			}
			if (arc.getFromNode().equals(nodes[6]) && arc.getToNode().equals(nodes[7])) {
				Assert.assertTrue(arc.getData().equals(42));
				Assert.assertFalse(present[6]);
				present[6] = true;
			}
			if (arc.getFromNode().equals(nodes[6]) && arc.getToNode().equals(nodes[8])) {
				Assert.assertFalse(present[7]);
				present[7] = true;
			}
			if (arc.getFromNode().equals(nodes[6]) && arc.getToNode().equals(nodes[9])) {
				Assert.assertFalse(present[8]);
				present[8] = true;
			}
		}
		for (int i = 0; i < 9; i++) {
			Assert.assertTrue(present[i]);
		}
		boolean error = false;
		try {
			it.next();
		} catch (Exception ex) {
			error = true;
		}
		Assert.assertTrue(error);

		//Remove test
		it = nl.getArcIterator();
		while (it.hasNext()) {
			Arc arc = it.next();
			if (arc.getFromNode().equals(nodes[1]) && arc.getToNode().equals(nodes[2])) it.remove();
			if (arc.getFromNode().equals(nodes[3]) && arc.getToNode().equals(nodes[0])) it.remove();
			if (arc.getFromNode().equals(nodes[5]) && arc.getToNode().equals(nodes[6])) it.remove();
			if (arc.getFromNode().equals(nodes[6]) && arc.getToNode().equals(nodes[7])) it.remove();
		}
		it = nl.getArcIterator();
		present = new boolean[5];
		for (int i = 0; i < 5; i++) present[i] = false;
		while (it.hasNext()) {
			Arc arc = it.next();
			if (arc.getFromNode().equals(nodes[0]) && arc.getToNode().equals(nodes[1])) {
				Assert.assertFalse(present[0]);
				present[0] = true;
			}
			if (arc.getFromNode().equals(nodes[2]) && arc.getToNode().equals(nodes[3])) {
				Assert.assertFalse(present[1]);
				present[1] = true;
			}
			if (arc.getFromNode().equals(nodes[1]) && arc.getToNode().equals(nodes[4])) {
				Assert.assertFalse(present[2]);
				present[2] = true;
			}
			if (arc.getFromNode().equals(nodes[6]) && arc.getToNode().equals(nodes[8])) {
				Assert.assertFalse(present[3]);
				present[3] = true;
			}
			if (arc.getFromNode().equals(nodes[6]) && arc.getToNode().equals(nodes[9])) {
				Assert.assertFalse(present[4]);
				present[4] = true;
			}
			Assert.assertFalse(arc.getFromNode().equals(nodes[1]) && arc.getToNode().equals(nodes[2]));
			Assert.assertFalse(arc.getFromNode().equals(nodes[3]) && arc.getToNode().equals(nodes[0]));
			Assert.assertFalse(arc.getFromNode().equals(nodes[5]) && arc.getToNode().equals(nodes[6]));
			Assert.assertFalse(arc.getFromNode().equals(nodes[6]) && arc.getToNode().equals(nodes[7]));
		}
		for (int i = 0; i < 5; i++) {
			Assert.assertTrue(present[i]);
		}

		NetworkLevel nl2 = new NetworkLevel("nl2");
		Node n1 = nl2.addNewNode("a");
		Node n2 = nl2.addNewNode("b");
		nl2.addNewArc(n1, n2);
		ArcIterator it2 = nl2.getArcIterator();
		Assert.assertTrue(it2.hasNext());
		Arc arc = it2.next();
		Assert.assertTrue(arc.getFromNode().equals(n1) && arc.getToNode().equals(n2));
		Assert.assertFalse(it2.hasNext());
		error = false;
		try {
			it2.next();
		} catch (Exception ex) {
			error = true;
		}
		Assert.assertTrue(error);

		NetworkLevel nl3 = new NetworkLevel("nl3");
		Node n3 = nl3.addNewNode("a");
		ArcIterator it3 = nl3.getArcIterator();
		Assert.assertFalse(it3.hasNext());
		error = false;
		try {
			it3.next();
		} catch (Exception ex) {
			error = true;
		}
		Assert.assertTrue(error);
	}

	@Test
	public void pathTest() {
		Node n1 = new Node("a");
		Node n2 = new Node("b");
		Node n3 = new Node("c");
		Node n4 = new Node("d");

		Path p = new Path(3);
		p.appendNode(n1);
		p.appendNode(n2);
		p.appendNode(n3);
		Assert.assertTrue("(a -> b -> c)".equals(p.toString()));

		Path p2 = new Path(new Node[] {n1, n2, n3});
		Assert.assertTrue("(a -> b -> c)".equals(p2.toString()));

		Path p3 = new Path(new Node[] {n1, n2});
		Assert.assertTrue("(a -> b)".equals(p3.toString()));

		Path p4 = new Path(new Node[] {n1});
		Assert.assertTrue("(a)".equals(p4.toString()));

		//Equal Test
		Assert.assertTrue(p.equals(p2));
		Assert.assertTrue(p2.equals(p));
		Assert.assertFalse(p2.equals(p3));
		Assert.assertFalse(p3.equals(p2));
		Assert.assertFalse(p4.equals(p2));
		Assert.assertFalse(p4.equals(p3));
		Assert.assertFalse(p4.equals(p));

		//Append remove Test
		p2.appendNode(n4);
		Assert.assertTrue(p2.getLength() == 3);
		Assert.assertTrue(p2.getNodeAtPosition(4).equals(n4));
		Assert.assertTrue(p2.getNodeAtPosition(3).equals(n3));
		Assert.assertTrue(p2.getNodeAtPosition(2).equals(n2));
		Assert.assertTrue(p2.getNodeAtPosition(1).equals(n1));
		Assert.assertTrue(p2.removeLastNode().equals(n4));
		Assert.assertTrue(p2.getLength() == 2);
		Assert.assertTrue(p2.getNodeAtPosition(3).equals(n3));
		Assert.assertTrue(p2.getNodeAtPosition(2).equals(n2));
		Assert.assertTrue(p2.getNodeAtPosition(1).equals(n1));
		Assert.assertTrue(p2.removeLastNode().equals(n3));
		Assert.assertTrue(p2.getLength() == 1);
		Assert.assertTrue(p2.getNodeAtPosition(2).equals(n2));
		Assert.assertTrue(p2.getNodeAtPosition(1).equals(n1));
		Assert.assertTrue(p2.removeLastNode().equals(n2));
		Assert.assertTrue(p2.getLength() == 0);
		Assert.assertTrue(p2.getNodeAtPosition(1).equals(n1));
		Assert.assertTrue(p2.removeLastNode().equals(n1));
		Assert.assertTrue(p2.getLength() == -1);
		p2.appendNode(n1);
		p2.appendNode(n2);
		p2.appendNode(n3);
		Assert.assertTrue(p2.getLength() == 2);
		Assert.assertTrue(p2.getNodeAtPosition(3).equals(n3));
		Assert.assertTrue(p2.getNodeAtPosition(2).equals(n2));
		Assert.assertTrue(p2.getNodeAtPosition(1).equals(n1));

		//Void path
		Path p5 = new Path(new Node[] {});
		Assert.assertTrue(p5.getLength() == -1);
		Assert.assertTrue("()".equals(p5.toString()));
		Path p6 = Path.NULL_PATH;
		Assert.assertTrue("()".equals(p6.toString()));
		Assert.assertTrue(p6.getLength() == -1);
	}

	@Test
	public void pathSetTest() {
		Node n1 = new Node("a");
		Node n2 = new Node("b");
		Node n3 = new Node("c");

		Path p1 = new Path(new Node[] {n1, n2, n3});
		Path p2 = new Path(new Node[] {n2, n1, n3});
		Path p3 = new Path(new Node[] {n1, n2, n3});
		Path p4 = new Path(new Node[] {n1, n3});

		PathSet ps = new PathSet();
		Assert.assertTrue(ps.getSize() == 0);

		Assert.assertTrue(ps.addPath(p1));
		Assert.assertTrue(ps.addPath(p2));
		Assert.assertTrue(ps.containsPath(p3));
		Assert.assertFalse(ps.addPath(p3));
		Assert.assertTrue(ps.addPath(p4));
		Assert.assertTrue(ps.getSize() == 3);

		String res = ps.toString();
		Assert.assertTrue(res.contains("(a -> b -> c)"));
		Assert.assertTrue(res.contains("(b -> a -> c)"));
		Assert.assertTrue(res.contains("(a -> c)"));

		PathSet ps2 = new PathSet(new Path[0]);
		Assert.assertTrue(ps2.getSize() == 0);
	}

}
