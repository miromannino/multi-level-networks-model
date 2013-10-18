import it.miromannino.multilevelnetwork.inout.NetworkLevelDotExport;
import it.miromannino.multilevelnetwork.model.NetworkLevel;
import it.miromannino.multilevelnetwork.model.Node;
import it.miromannino.multilevelnetwork.model.Path;
import it.miromannino.multilevelnetwork.model.PathSet;
import it.miromannino.multilevelnetwork.operator.PathCondition;
import it.miromannino.multilevelnetwork.operator.Selection;
import it.miromannino.multilevelnetwork.operator.pathpattern.PathPattern;
import it.miromannino.multilevelnetwork.operator.Synthesis;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class SynthesisTest {

	NetworkLevel nl1;
	Node[] n = new Node[10];

	private static final String testOutputPath = "./test_output/";
	private static final String dotFileName = "testSynthesis";
	private static final String dotExtension = ".dot";

	@Before
	public void setUp() throws IOException {

		//Build nl1
		nl1 = new NetworkLevel("nl1");
		n[0] = nl1.addNewNode("a");
		n[1] = nl1.addNewNode("b");
		n[2] = nl1.addNewNode("c", 38);
		n[3] = nl1.addNewNode("d");
		n[4] = nl1.addNewNode("e");
		n[5] = nl1.addNewNode("f");
		n[6] = nl1.addNewNode("g", 42);
		nl1.addNewArc(n[0], n[1]);
		nl1.addNewArc(n[0], n[2], 3);
		nl1.addNewArc(n[0], n[3]);
		nl1.addNewArc(n[1], n[4]);
		nl1.addNewArc(n[1], n[5]);
		nl1.addNewArc(n[4], n[6], 4);
		nl1.addNewArc(n[2], n[6]);
		nl1.addNewArc(n[5], n[0]);
		nl1.addNewArc(n[6], n[1]);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "_nl1" + dotExtension);
		NetworkLevelDotExport.export(nl1, fw);
		fw.close();

	}

	@Test
	public void sythesisTests() throws IOException {

		//Test selection of "a -> *" in nl1
		PathSet ris1 = Selection.select(nl1, new PathPattern.Concatenation(
				new PathPattern.GraphNode(n[0]),
				new PathPattern.GenericPath()),
				new PathCondition.TruePredicate());

		NetworkLevel nlRis1 = Synthesis.synthesize("nl1Ris", ris1);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "_nl1_pathsFromA" + dotExtension);
		NetworkLevelDotExport.export(nlRis1, fw);
		fw.close();

		PathSet ins1 = new PathSet(new Path[] {
				new Path(new Node[] { n[0], n[1]}),
				new Path(new Node[] { n[0], n[1], n[2]}),
				new Path(new Node[] { n[0], n[3]}),
				new Path(new Node[] { n[3], n[2]})
		});

		NetworkLevel nlRis2 = Synthesis.synthesize("nl1Ris2", ins1);

		//Export
		fw = new FileWriter(testOutputPath + dotFileName + "_nl1_ins1" + dotExtension);
		NetworkLevelDotExport.export(nlRis2, fw);
		fw.close();


	}

}
