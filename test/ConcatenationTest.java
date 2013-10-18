import it.miromannino.multilevelnetwork.model.*;
import it.miromannino.multilevelnetwork.operator.Concatenation;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class ConcatenationTest {

	Path p1, p2, p3, p4, p5, p6, pv1, pv2;
	Node n1, n2, n3;

	@Before
	public void setUp() {
		n1 = new Node("a");
		n2 = new Node("b");
		n3 = new Node("c");
		p1 = new Path(new Node[] {n1, n2, n3});
		p2 = new Path(new Node[] {n1, n2});
		p3 = new Path(new Node[] {n3, n1});
		p4 = new Path(new Node[] {n1});
		p5 = new Path(new Node[] {n2});
		p6 = new Path(new Node[] {n3});

		pv1 = new Path(new Node[]{});
		pv2 = Path.NULL_PATH;
	}

	@Test
	public void equalsTests() {

		Path pris = Concatenation.concatenate(p1, p2);

		Assert.assertTrue(Concatenation.concatenate(p1, p2).equals(new Path(new Node[]{n1, n2, n3, n1, n2})));

		Assert.assertTrue(
				Concatenation.concatenate(p2, p3).equals(Concatenation.concatenate(p1, p4)));

		Assert.assertTrue(
				Concatenation.concatenate(p4, p5).equals(p2)
		);

		Assert.assertTrue(Concatenation.concatenate(p1, p3).getLength() == p1.getLength() + p2.getLength() + 1);

	}

	@Test
	public void emptyTests() {

		Assert.assertTrue(Concatenation.concatenate(p1, pv1) == p1);
		Assert.assertTrue(Concatenation.concatenate(p1, pv1).equals(p1));
		Assert.assertTrue(Concatenation.concatenate(pv1, pv2).equals(new Path(new Node[0])));
		Assert.assertTrue(Concatenation.concatenate(pv1, p5) == p5);
		Assert.assertTrue(Concatenation.concatenate(pv1, p5).equals(p5));

	}

}
