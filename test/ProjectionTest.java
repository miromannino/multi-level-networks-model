import it.miromannino.multilevelnetwork.model.Node;
import it.miromannino.multilevelnetwork.model.Path;
import it.miromannino.multilevelnetwork.model.PathSet;
import it.miromannino.multilevelnetwork.operator.Projection;
import junit.framework.Assert;
import org.junit.Test;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class ProjectionTest {

	@Test
	public void pathProjectionTests() {
		Node n1 = new Node("a");
		Node n2 = new Node("b");
		Node n3 = new Node("c");
		Node n4 = new Node("d");
		Node n5 = new Node("e");

		Path p1 = new Path(new Node[] {n1, n2, n3, n4, n5});

		Path ris1 = Projection.project(new Projection.ConstantIndex(1), new Projection.ConstantIndex(3), p1);
		Assert.assertTrue(ris1.equals(new Path(new Node[] {n1, n2, n3})));

		boolean error = false;
		try {
			Projection.project(new Projection.ConstantIndex(2), new Projection.ConstantIndex(1), p1);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		Assert.assertTrue(error);

		Path ris2 = Projection.project(new Projection.ConstantIndex(2), new Projection.ConstantIndex(2), p1);
		Assert.assertTrue(ris2.equals(new Path(new Node[] {n2})));

		Path ris3 = Projection.project(new Projection.ConstantIndex(3), p1);
		Assert.assertTrue(ris3.equals(new Path(new Node[] {n3})));

		Path ris4 = Projection.project(new Projection.ConstantIndex(5), p1);
		Assert.assertTrue(ris4.equals(new Path(new Node[] {n5})));

		Path ris5 = Projection.project(new Projection.ConstantIndex(1), new Projection.ConstantIndex(5), p1);
		Assert.assertTrue(ris5.equals(p1));

		Path ris6 = Projection.project(new Projection.ConstantIndex(6), new Projection.ConstantIndex(7), p1);
		Assert.assertTrue(ris6.equals(Path.NULL_PATH));

		Path ris7 = Projection.project(new Projection.ConstantIndex(6), p1);
		Assert.assertTrue(ris7.equals(Path.NULL_PATH));

		Path ris8 = Projection.project(new Projection.Index() {
			@Override
			public int get(Path p) { return p.getLength()/2; }
		}, p1);
		Assert.assertTrue(ris8.equals(new Path(new Node[] {n2})));

		Path ris9 = Projection.project(new Projection.Index() {
			@Override
			public int get(Path p) { return p.getLength()/2; }
		}, new Projection.Index() {
			@Override
			public int get(Path p) { return p.getLength()/2 + 1; }
		}, p1);
		Assert.assertTrue(ris9.equals(new Path(new Node[] {n2, n3})));

	}

	@Test
	public void pathSetProjectionTests() {
		Node n1 = new Node("a");
		Node n2 = new Node("b");
		Node n3 = new Node("c");
		Node n4 = new Node("d");
		Node n5 = new Node("e");

		Path p1 = new Path(new Node[] {n1, n2, n3, n4, n5});
		Path p2 = new Path(new Node[] {n2, n3, n4, n5});
		Path p3 = new Path(new Node[] {n3, n4, n5});
		Path p4 = new Path(new Node[] {n3, n4, n1, n2, n5});
		Path p5 = new Path(new Node[] {n5, n4, n3, n2, n1});

		PathSet ps1 = new PathSet(new Path[] {p1, p2, p3, p4, p5});
		PathSet ps2 = new PathSet(new Path[] {p1});
		PathSet ps3 = new PathSet(new Path[] {});

		PathSet ris1 = Projection.project(new Projection.ConstantIndex(1), new Projection.ConstantIndex(4), ps1);
		Assert.assertTrue(ris1.getSize() == 5);
		Assert.assertTrue(ris1.containsPath(new Path(new Node[] {n1, n2, n3, n4})));
		Assert.assertTrue(ris1.containsPath(p2));
		Assert.assertTrue(ris1.containsPath(p3));
		Assert.assertTrue(ris1.containsPath(new Path(new Node[] {n3, n4, n1, n2})));
		Assert.assertTrue(ris1.containsPath(new Path(new Node[] {n5, n4, n3, n2})));

		PathSet ris2 = Projection.project(new Projection.ConstantIndex(1), ps1);
		Assert.assertTrue(ris2.getSize() == 4);
		Assert.assertTrue(ris2.containsPath(new Path(new Node[] {n1})));
		Assert.assertTrue(ris2.containsPath(new Path(new Node[] {n2})));
		Assert.assertTrue(ris2.containsPath(new Path(new Node[] {n3})));
		Assert.assertTrue(ris2.containsPath(new Path(new Node[] {n5})));

		PathSet ris3 = Projection.project(new Projection.ConstantIndex(4), new Projection.ConstantIndex(5), ps1);
		Assert.assertTrue(ris3.getSize() == 4);
		Assert.assertTrue(ris3.containsPath(new Path(new Node[] {n4, n5})));
		Assert.assertTrue(ris3.containsPath(new Path(new Node[] {n5})));
		Assert.assertTrue(ris3.containsPath(new Path(new Node[] {n2, n5})));
		Assert.assertTrue(ris3.containsPath(new Path(new Node[] {n2, n1})));

		PathSet ris4 = Projection.project(new Projection.ConstantIndex(1), ps2);
		Assert.assertTrue(ris4.getSize() == 1);
		Assert.assertTrue(ris4.containsPath(new Path(new Node[] {n1})));

		PathSet ris5 = Projection.project(new Projection.ConstantIndex(8), ps2);
		Assert.assertTrue(ris5.getSize() == 0);

		PathSet ris6 = Projection.project(new Projection.ConstantIndex(7), ps1);
		Assert.assertTrue(ris6.getSize() == 0);

		PathSet ris7 = Projection.project(new Projection.ConstantIndex(1), ps3);
		Assert.assertTrue(ris7.getSize() == 0);

	}


}
