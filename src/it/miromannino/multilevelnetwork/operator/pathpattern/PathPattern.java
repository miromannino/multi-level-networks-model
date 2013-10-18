package it.miromannino.multilevelnetwork.operator.pathpattern;

import it.miromannino.multilevelnetwork.model.Node;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public abstract class PathPattern {

	public static class EmptyPath extends PathPattern {

	}

	/**
	 * The path pattern that identify a specific node in the graph
	 *
	 *  e.g.
	 *
	 * we have a network level {@code nl1} with
	 *
	 * <pre>
	 * {@code Node n = nl1.addNewNode("a");}
	 * </pre>
	 *
	 * the path pattern that can be written as "a", is the path pattern created with:
	 *
	 * <pre>
	 * {@code new PathPattern.GraphNode(n);}
	 * </pre>
	 */
	public static class GraphNode extends PathPattern {

		Node n;

		public GraphNode(Node n) {
			if (n == null) throw new NullPointerException("the node must be not null");
			this.n = n;
		}

		public Node getNode() {
			return n;
		}

	}

	/**
	 * Specify a generic node. Is written in the thesis as the symbol "%"
	 */
	public static class GenericNode extends PathPattern {

	}

	/**
	 * Specify a optional generic node. Is written in the thesis as the symbol "?"
	 */
	public static class OptionalGenericNode extends PathPattern {

	}

	/**
	 * Specify a generic path. Is written in the thesis as the symbol "*"
	 */
	public static class GenericPath extends PathPattern {

	}

	/**
	 * A concatenation between two path pattern. Is written in the thesis as the symbol "->"
	 *
	 * e.g.
	 *
	 * We have the path pattern "a" and "*". We want all the paths that start form the node {@code a}.
	 * We can do this with the path pattern:
	 *
	 * <pre>
	 *    {@code new PathPattern.Concatenation(new PathPattern.GraphNode(a), new PathPattern.GenericPath())}
	 * </pre>
	 *
	 */
	public static class Concatenation extends PathPattern {
		PathPattern fromPath;
		PathPattern toPath;

		public Concatenation(PathPattern fromPath, PathPattern toPath) {
			if (fromPath == null) throw new NullPointerException("the fromPath must be not null");
			if (toPath == null) throw new NullPointerException("the toPath must be not null");
			this.fromPath = fromPath;
			this.toPath = toPath;
		}
	}

	/**
	 * An alternation between two path pattern. Is written in the thesis as the symbol "|"
	 *
	 * <p>
	 * e.g.
	 *
	 * We have the path pattern "a -> *" and the path pattern "b -> *".
	 * We want all the paths that start form the node {@code a} or the node {@code b}.
	 * We can do this with the path pattern:
	 *
	 * <pre>
	 *    {@code new PathPattern.Alternation(firstPathPattern, secondPathPattern)}
	 * </pre>
	 * </p>
	 */
	public static class Alternation extends PathPattern {
		PathPattern firstPath;
		PathPattern secondPath;

		public Alternation(PathPattern firstPath, PathPattern secondPath) {
			if (firstPath == null) throw new NullPointerException("the fromPath must be not null");
			if (secondPath == null) throw new NullPointerException("the toPath must be not null");
			this.firstPath = firstPath;
			this.secondPath = secondPath;
		}
	}

}
