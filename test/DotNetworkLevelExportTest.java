import it.miromannino.multilevelnetwork.inout.NetworkLevelDotExport;
import it.miromannino.multilevelnetwork.model.NetworkLevel;
import it.miromannino.multilevelnetwork.model.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class DotNetworkLevelExportTest {

	private static final String testOutputPath = "./test_output/";
	private static final String dotFileName = "testNetworkLevel";
	private static final String dotExtension = ".dot";

	@Before
	public void setUp() {
	}

	@Test
	public void exportTest1() throws IOException {

		//Network Level creation
		NetworkLevel nl1 = new NetworkLevel("nl1");
		Node n1 = nl1.addNewNode("a");
		Node n2 = nl1.addNewNode("b");
		Node n3 = nl1.addNewNode("c");
		nl1.addNewArc(n1, n2);
		nl1.addNewArc(n2, n1);
		nl1.addNewArc(n1, n3);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "1" + dotExtension);
		NetworkLevelDotExport.export(nl1, fw);
		fw.close();
	}

	@Test
	public void exportTest2() throws IOException {

		//Network Level creation
		NetworkLevel nl2 = new NetworkLevel("nl2");
		Node n1 = nl2.addNewNode("a");
		Node n2 = nl2.addNewNode("b");
		Node n3 = nl2.addNewNode("c");
		nl2.addNewArc(n1, n2, 2);
		nl2.addNewArc(n2, n1, "fr");
		nl2.addNewArc(n1, n3);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "2" + dotExtension);
		NetworkLevelDotExport.export(nl2, fw);
		fw.close();
	}

	@Test
	public void exportTest3() throws IOException {

		//Network Level creation
		NetworkLevel nl3 = new NetworkLevel("nl3");
		Node n1 = nl3.addNewNode("a", 2);
		Node n2 = nl3.addNewNode("b", "d2");
		Node n3 = nl3.addNewNode("c");
		nl3.addNewArc(n1, n2, 2);
		nl3.addNewArc(n2, n1, "d3");
		nl3.addNewArc(n1, n3);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "3" + dotExtension);
		NetworkLevelDotExport.export(nl3, fw);
		fw.close();
	}

}
