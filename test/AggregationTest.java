import it.miromannino.multilevelnetwork.inout.NetworkLevelDotExport;
import it.miromannino.multilevelnetwork.model.NetworkLevel;
import it.miromannino.multilevelnetwork.model.Node;
import it.miromannino.multilevelnetwork.model.Path;
import it.miromannino.multilevelnetwork.model.PathSet;
import it.miromannino.multilevelnetwork.operator.Aggregation;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class AggregationTest {

	NetworkLevel nl1;
	Node[] n = new Node[10];

	private static final String testOutputPath = "./test_output/";
	private static final String dotFileName = "testAggregation";
	private static final String dotExtension = ".dot";

	@Before
	public void setUp() throws IOException {

		//Build nl1
		nl1 = new NetworkLevel("nl1");
		n[0] = nl1.addNewNode("a", 1);
		n[1] = nl1.addNewNode("b", 2);
		n[2] = nl1.addNewNode("c", 3);
		n[3] = nl1.addNewNode("d", 4);
		n[4] = nl1.addNewNode("e", 5);
		nl1.addNewArc(n[0], n[1], 10); //a->b
		nl1.addNewArc(n[0], n[2], 20); //a->c
		nl1.addNewArc(n[3], n[4], 30); //d->e
		nl1.addNewArc(n[3], n[1], 40); //d->b

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "_nl1" + dotExtension);
		NetworkLevelDotExport.export(nl1, fw);
		fw.close();

	}

	@Test
	public void aggregationTests() throws IOException {

		PathSet ps = new PathSet( new Path[] {
				new Path(new Node[] {n[0], n[1]}),
				new Path(new Node[] {n[0], n[2]}),
				new Path(new Node[] {n[3], n[4]}),
				new Path(new Node[] {n[3], n[1]})
			});

		Aggregation.CFunction cf = new Aggregation.CFunction() {

			@Override
			public Path cFunction(Path p) {
				return new Path(new Node[] {p.getNodeAtPosition(1), Aggregation.NewNode});
			}

		};

		Aggregation.Assignments a = new Aggregation.Assignments() {

			@Override
			public void changeData(Path aggregatePath, Path currentPath) {

				//aggregate function SUM of all the values in the destination node
				if (aggregatePath.getNodeAtPosition(2).getData() == null) {
					aggregatePath.getNodeAtPosition(2).setData(currentPath.getNodeAtPosition(2).getData());
				} else {
					aggregatePath.getNodeAtPosition(2).setData(
							(Integer)currentPath.getNodeAtPosition(2).getData() + (Integer)aggregatePath.getNodeAtPosition(2).getData()
					);
				}

				//aggregate function SUM of all the values in the arcs
				if (aggregatePath.getNodeAtPosition(1).getNeighborLinkData(aggregatePath.getNodeAtPosition(2)) == null) {
					aggregatePath.getNodeAtPosition(1).setNeighborLinkData(
							aggregatePath.getNodeAtPosition(2),
							currentPath.getNodeAtPosition(1).getNeighborLinkData(currentPath.getNodeAtPosition(2))
					);
				} else {
					aggregatePath.getNodeAtPosition(1).setNeighborLinkData(
							aggregatePath.getNodeAtPosition(2),
							(Integer)currentPath.getNodeAtPosition(1).getNeighborLinkData(currentPath.getNodeAtPosition(2)) +
									(Integer)aggregatePath.getNodeAtPosition(1).getNeighborLinkData(aggregatePath.getNodeAtPosition(2))
					);
				}

			}

		};

		NetworkLevel ris = Aggregation.aggregate("risNl1", ps, cf, a, "nn");

		//Export
		FileWriter fw = new FileWriter(testOutputPath + dotFileName + "_nl1ris1" + dotExtension);
		NetworkLevelDotExport.export(ris, fw);
		fw.close();

	}

}
