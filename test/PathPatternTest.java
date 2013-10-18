import it.miromannino.multilevelnetwork.inout.PathPatternAutomatonDotExport;
import it.miromannino.multilevelnetwork.model.Node;
import it.miromannino.multilevelnetwork.operator.pathpattern.Automaton;
import it.miromannino.multilevelnetwork.operator.pathpattern.PathPattern;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class PathPatternTest {

	private static final String testOutputPath = "./test_output/";
	private static final String dotFileName = "testPathPattern";
	private static final String dotExtension = ".dot";

	@Before
	public void setUp() {
	}

	@Test
	public void automatonCreation1() throws IOException {
		PathPattern p = new PathPattern.Concatenation(new PathPattern.GenericNode(), new PathPattern.GenericNode());
		Automaton automaton = new Automaton(p);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "1" + dotExtension);
		PathPatternAutomatonDotExport.export(automaton, fw);
		fw.close();
	}

	@Test
	public void automatonCreation2() throws IOException {
		Node n = new Node("n1");
		PathPattern p = new PathPattern.Concatenation(
				new PathPattern.Alternation(
						new PathPattern.GenericNode(),
						new PathPattern.GraphNode(n)
				),
				new PathPattern.GenericNode());
		Automaton automaton = new Automaton(p);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "2" + dotExtension);
		PathPatternAutomatonDotExport.export(automaton, fw);
		fw.close();

	}

	@Test
	public void automatonCreation3() throws IOException {
		Node n = new Node("n1");
		PathPattern p = new PathPattern.Alternation(
				new PathPattern.Concatenation(
						new PathPattern.GenericNode(),
						new PathPattern.GraphNode(n)
				),
				new PathPattern.GenericNode());
		Automaton automaton = new Automaton(p);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "3" + dotExtension);
		PathPatternAutomatonDotExport.export(automaton, fw);
		fw.close();

	}

	@Test
	public void automatonCreation4() throws IOException {
		Node n = new Node("n1");
		PathPattern p = new PathPattern.Alternation(
				new PathPattern.Concatenation(
						new PathPattern.GenericPath(),
						new PathPattern.GraphNode(n)
				),
				new PathPattern.GenericNode());
		Automaton automaton = new Automaton(p);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "4" + dotExtension);
		PathPatternAutomatonDotExport.export(automaton, fw);
		fw.close();

	}

	@Test
	public void automatonCreation5() throws IOException {
		Node a = new Node("a");
		Node b = new Node("b");
		PathPattern p = new PathPattern.Concatenation(
				new PathPattern.Alternation(
						new PathPattern.GraphNode(a),
						new PathPattern.GraphNode(b)
				),
				new PathPattern.GenericNode());
		Automaton automaton = new Automaton(p);

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "5" + dotExtension);
		PathPatternAutomatonDotExport.export(automaton, fw);
		fw.close();

	}

}
