import it.miromannino.multilevelnetwork.inout.NetworkLevelDotExport;
import it.miromannino.multilevelnetwork.model.Couple;
import it.miromannino.multilevelnetwork.model.NetworkLevel;
import it.miromannino.multilevelnetwork.model.Node;
import it.miromannino.multilevelnetwork.operator.Join;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class JoinTest {

	NetworkLevel nl1, nl2, nl3, nl4;
	Node[] n = new Node[10];
	Node[] l = new Node[10];
	Node[] n2 = new Node[10];
	Node[] l2 = new Node[10];

	Couple couple, couple2;

	private static final String testOutputPath = "./test_output/";
	private static final String dotFileName = "joinSelection";
	private static final String dotExtension = ".dot";

	@Before
	public void setUp() throws IOException {

		//Build nl1
		nl1 = new NetworkLevel("nl1");
		n[1] = nl1.addNewNode("n1");
		n[2] = nl1.addNewNode("n2");

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "_nl1" + dotExtension);
		NetworkLevelDotExport.export(nl1, fw);
		fw.close();

		//Build nl2
		nl2 = new NetworkLevel("nl2");
		l[1] = nl2.addNewNode("l1");
		l[2] = nl2.addNewNode("l2");
		l[3] = nl2.addNewNode("l3");
		nl2.addNewArc(l[1], l[3], 5);
		nl2.addNewArc(l[2], l[3], 7);

		//Export
		fw = new FileWriter(testOutputPath + dotFileName + "_nl2" + dotExtension);
		NetworkLevelDotExport.export(nl2, fw);
		fw.close();

		couple = new Couple(nl1, nl2);
		couple.addNewCoupleLink(n[1], l[1]);
		couple.addNewCoupleLink(n[1], l[2]);
		couple.addNewCoupleLink(n[2], l[3]);

		//-------------

		//Build nl1
		nl3 = new NetworkLevel("nl3");
		n2[1] = nl3.addNewNode("n1");
		n2[2] = nl3.addNewNode("n2");

		//Export
		fw = new FileWriter(testOutputPath + dotFileName + "_nl3" + dotExtension);
		NetworkLevelDotExport.export(nl3, fw);
		fw.close();

		//Build nl2
		nl4 = new NetworkLevel("nl4");
		l2[1] = nl4.addNewNode("l1");
		l2[2] = nl4.addNewNode("l2");
		l2[3] = nl4.addNewNode("l3");
		nl4.addNewArc(l2[1], l2[3], 5);
		nl4.addNewArc(l2[2], l2[3], 7);

		//Export
		fw = new FileWriter(testOutputPath + dotFileName + "_nl4" + dotExtension);
		NetworkLevelDotExport.export(nl4, fw);
		fw.close();

		couple2 = new Couple(nl3, nl4);
		couple2.addNewCoupleLink(n2[1], l2[1]);
		couple2.addNewCoupleLink(n2[1], l2[2]);
		couple2.addNewCoupleLink(n2[1], l2[3]);
		couple2.addNewCoupleLink(n2[2], l2[3]);

	}

	@Test
	public void joinTests() throws IOException {

		Join.DataJoin fv = new Join.DataJoin() {
			@Override
			public Object join(Object a, Object b) {
				return ((Integer)a + (Integer)b);
			}
		};
		Join.DataJoin fd = new Join.DataJoin() {
			@Override
			public Object join(Object a, Object b) {
				return null;
			}
		};
		NetworkLevel nlris = Join.join("nlris", nl1, nl2, couple, fd, fv);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "_nlris" + dotExtension);
		NetworkLevelDotExport.export(nlris, fw);
		fw.close();

		NetworkLevel nlris2 = Join.join("nlris2", nl3, nl4, couple2, fd, fv);

		//Export
		fw = new FileWriter(testOutputPath + dotFileName + "_nlris2" + dotExtension);
		NetworkLevelDotExport.export(nlris2, fw);
		fw.close();
	}

}
