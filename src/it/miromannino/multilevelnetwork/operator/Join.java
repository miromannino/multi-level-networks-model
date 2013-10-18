package it.miromannino.multilevelnetwork.operator;

import com.sun.tools.javac.util.List;
import it.miromannino.multilevelnetwork.model.*;

import java.util.*;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class Join {

	/**
	 * This is the function callback that join the data of two nodes, or the data of two arcs.
	 * Hence, it can ge used to define the f_d or the f_v function.
	 */
	public abstract static class DataJoin {

		public abstract Object join(Object a, Object b);

	}

	public static NetworkLevel join(String resultLevelID, NetworkLevel a, NetworkLevel b, Couple c, DataJoin fd, DataJoin fv) {
		NetworkLevel G = new NetworkLevel(resultLevelID);
		Map<Node, Set<Node>> Mh = new HashMap<Node, Set<Node>>();
		Iterator<Node> nodeIterator = a.getNodeIterator();
		while (nodeIterator.hasNext()) {
			Node n = nodeIterator.next();
			Node x = G.addNewNode(n.getId(), n.getData());

			//M_h(n) = {x}
			Set<Node> xSet = new HashSet<Node>();
			xSet.add(x);
			Mh.put(n, xSet);

			Set<Node> CM = C_M(n, c);
			for (Node l : CM) {
				if (!Mh.containsKey(l)) {
					Set<Node> xLSet = new HashSet<Node>();
					xLSet.add(x);
					Mh.put(l, xLSet);
				} else {
					Mh.get(l).add(x);
				}

				x.setData(fd.join(x.getData(), l.getData()));

			}
		}

		arcExp(a, Mh, G, fv);
		arcExp(b, Mh, G, fv);

		return G;
	}

	private static Set<Node> C_M(Node n, Couple c) {
		Set<Node> R = new HashSet<Node>();
		for (CoupleLink link : c) {
			if (link.getDepartureNode().equals(n)) {
				R.add(link.getDestinationNode());
			}
		}
		return R;
	}

	private static void arcExp(NetworkLevel A, Map<Node, Set<Node>> Mh, NetworkLevel G, DataJoin fv) {
		ArcIterator arcIterator = A.getArcIterator();
		while (arcIterator.hasNext()) {
			Arc a = arcIterator.next();
			Set<Node> Mh_n1 = Mh.get(a.getFromNode());
			Set<Node> Mh_n2 = Mh.get(a.getToNode());
			if (Mh_n1 != null) {
				for (Node x1 : Mh_n1) {
					if (Mh_n2 != null) {
						for (Node x2 : Mh_n2) {
							if (x1.hasNeighbor(x2)) {
								x1.setNeighborLinkData(x2,
										fv.join(a.getFromNode().getNeighborLinkData(a.getToNode()),
												x1.getNeighborLinkData(x2)));
							} else {
								x1.addNeighbor(x2);
								x1.setNeighborLinkData(x2, a.getFromNode().getNeighborLinkData(a.getToNode()));
							}

						}
					}
				}
			}
		}
	}


}
