import it.miromannino.multilevelnetwork.inout.NetworkLevelDotExport;
import it.miromannino.multilevelnetwork.model.NetworkLevel;
import it.miromannino.multilevelnetwork.model.Node;
import it.miromannino.multilevelnetwork.model.PathSet;
import it.miromannino.multilevelnetwork.operator.PathCondition;
import it.miromannino.multilevelnetwork.operator.Selection;
import it.miromannino.multilevelnetwork.operator.pathpattern.PathPattern;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class SelectionTest {

	NetworkLevel nl1, nl2;
	Node[] n = new Node[10];
	Node[] n2 = new Node[10];

	private static final String testOutputPath = "./test_output/";
	private static final String dotFileName = "testSelection";
	private static final String dotExtension = ".dot";

	@Before
	public void setUp() throws IOException {

		//Build nl1
		nl1 = new NetworkLevel("nl1");
		n[0] = nl1.addNewNode("a");
		n[1] = nl1.addNewNode("b");
		n[2] = nl1.addNewNode("c");
		n[3] = nl1.addNewNode("d");
		n[4] = nl1.addNewNode("e");
		n[5] = nl1.addNewNode("f");
		n[6] = nl1.addNewNode("g");
		nl1.addNewArc(n[0], n[1]);
		nl1.addNewArc(n[0], n[2]);
		nl1.addNewArc(n[0], n[3]);
		nl1.addNewArc(n[1], n[4]);
		nl1.addNewArc(n[1], n[5]);
		nl1.addNewArc(n[4], n[6]);
		nl1.addNewArc(n[2], n[6]);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "_nl1" + dotExtension);
		NetworkLevelDotExport.export(nl1, fw);
		fw.close();

		//Build nl2
		nl2 = new NetworkLevel("nl2");
		n2[0] = nl2.addNewNode("a");
		n2[1] = nl2.addNewNode("b");
		n2[2] = nl2.addNewNode("c");
		n2[3] = nl2.addNewNode("d");
		n2[4] = nl2.addNewNode("e");
		n2[5] = nl2.addNewNode("f");
		n2[6] = nl2.addNewNode("g");
		nl2.addNewArc(n2[0], n2[1]);
		nl2.addNewArc(n2[0], n2[2]);
		nl2.addNewArc(n2[0], n2[3]);
		nl2.addNewArc(n2[1], n2[4]);
		nl2.addNewArc(n2[1], n2[5]);
		nl2.addNewArc(n2[4], n2[6]);
		nl2.addNewArc(n2[2], n2[6]);
		nl2.addNewArc(n2[5], n2[0]);
		nl2.addNewArc(n2[6], n2[1]);

		//Export
		fw = new FileWriter(testOutputPath + dotFileName + "_nl2" + dotExtension);
		NetworkLevelDotExport.export(nl2, fw);
		fw.close();

	}

	@Test
	public void selectionTests() throws IOException {

		//Test selection of "a -> *" in nl1
		PathSet ris = Selection.select(nl1, new PathPattern.Concatenation(
				new PathPattern.GraphNode(n[0]),
				new PathPattern.GenericPath()),
			new PathCondition.TruePredicate());
		String risStr = ris.toString();
		Assert.assertTrue(risStr.contains("(a)"));
		Assert.assertTrue(risStr.contains("(a -> b)"));
		Assert.assertTrue(risStr.contains("(a -> d)"));
		Assert.assertTrue(risStr.contains("(a -> c)"));
		Assert.assertTrue(risStr.contains("(a -> b -> f)"));
		Assert.assertTrue(risStr.contains("(a -> b -> e)"));
		Assert.assertTrue(risStr.contains("(a -> c -> g)"));
		Assert.assertTrue(risStr.contains("(a -> b -> e -> g)"));
		Assert.assertTrue(ris.getSize() == 8);

		//Test selection of "a -> *" in nl2 (that have a cycle)
		PathSet ris2 = Selection.select(nl2, new PathPattern.Concatenation(
				new PathPattern.GraphNode(n2[0]),
				new PathPattern.GenericPath()),
			new PathCondition.TruePredicate());
		String risStr2 = ris2.toString();
		Assert.assertTrue(risStr2.contains("(a)"));
		Assert.assertTrue(risStr2.contains("(a -> b)"));
		Assert.assertTrue(risStr2.contains("(a -> d)"));
		Assert.assertTrue(risStr2.contains("(a -> c)"));
		Assert.assertTrue(risStr2.contains("(a -> b -> f)"));
		Assert.assertTrue(risStr2.contains("(a -> b -> e)"));
		Assert.assertTrue(risStr2.contains("(a -> c -> g)"));
		Assert.assertTrue(risStr2.contains("(a -> b -> e -> g)"));
		Assert.assertTrue(risStr2.contains("(a -> c -> g -> b)"));
		Assert.assertTrue(risStr2.contains("(a -> c -> g -> b -> f)"));
		Assert.assertTrue(risStr2.contains("(a -> c -> g -> b -> e)"));
		Assert.assertTrue(ris2.getSize() == 11);

		//Test selection of "% -> *" in nl2 with the predicate that wants only paths with length = 1
		PathSet ris3 = Selection.select(nl1, new PathPattern.Concatenation(
				new PathPattern.GenericNode(),
				new PathPattern.GenericPath()),
			new PathCondition.SpecificLengthPredicate(1));
		String risStr3 = ris3.toString();
		Assert.assertTrue(risStr3.contains("(a -> b)"));
		Assert.assertTrue(risStr3.contains("(a -> c)"));
		Assert.assertTrue(risStr3.contains("(a -> d)"));
		Assert.assertTrue(risStr3.contains("(e -> g)"));
		Assert.assertTrue(risStr3.contains("(b -> f)"));
		Assert.assertTrue(risStr3.contains("(c -> g)"));
		Assert.assertTrue(risStr3.contains("(b -> e)"));
		Assert.assertTrue(ris3.getSize() == 7);

		//Test selection of "a -> % | b -> %" in nl1
		PathSet ris4 = Selection.select(nl1, new PathPattern.Alternation(
				new PathPattern.Concatenation(
						new PathPattern.GraphNode(n[0]),
						new PathPattern.GenericNode()),
				new PathPattern.Concatenation(
						new PathPattern.GraphNode(n[1]),
						new PathPattern.GenericNode())),
				new PathCondition.TruePredicate());
		String risStr4 = ris4.toString();
		Assert.assertTrue(risStr4.contains("(a -> b)"));
		Assert.assertTrue(risStr4.contains("(a -> d)"));
		Assert.assertTrue(risStr4.contains("(a -> c)"));
		Assert.assertTrue(risStr4.contains("(b -> e)"));
		Assert.assertTrue(risStr4.contains("(b -> f)"));
		Assert.assertTrue(ris4.getSize() == 5);

		//Test selection of "a -> * -> b" in nl2 (that have a cycle)
		PathSet ris5 = Selection.select(nl2, new PathPattern.Concatenation(
				new PathPattern.GraphNode(n2[0]),
				new PathPattern.Concatenation(
						new PathPattern.GenericPath(),
						new PathPattern.GraphNode(n2[1])
				)),
				new PathCondition.TruePredicate());
		String risStr5 = ris5.toString();
		Assert.assertTrue(risStr5.contains("(a -> b)"));
		Assert.assertTrue(risStr5.contains("(a -> c -> g -> b)"));
		Assert.assertTrue(ris5.getSize() == 2);
	}
}
