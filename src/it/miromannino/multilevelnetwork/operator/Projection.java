package it.miromannino.multilevelnetwork.operator;

import it.miromannino.multilevelnetwork.model.Path;
import it.miromannino.multilevelnetwork.model.PathSet;

import java.util.Iterator;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class Projection {

	/* ------- INDEX CLASSES --------- */

	//static is for the Nested class (and not the inner), with this we can do {@code new Projection.Index()}
	public static abstract class Index {
		 public abstract int get(Path p);
	}

	//static is for the Nested class (and not the inner), with this we can do {@code new Projection.ConstantIndex()}
	public static class ConstantIndex extends Index {
		int index;

		public ConstantIndex(int index) {
			this.index = index;
		}

		@Override
		public int get(Path p) {
			return index;
		}
	}

	/* ------------------------------- */

	/**
	 * The projection of the path set {@code p} with the start index {@code s} and the end index {@code e}
	 * @param s the start index, that may depends on the length of the path (e.g. {@code s(p) = p.length/2})
	 * @param e the end index, that may depends on the length of the path (e.g. {@code e(p) = p.length/2})
	 * @param p the path set
	 * @return a new path set, with all projected paths
	 */
	public static PathSet project(Index s, Index e, PathSet p) {
		if (p == null) throw new NullPointerException("the set must be not null");

		PathSet ris = new PathSet(p.getSize());
		Iterator<Path> it = p.iterator();
		while(it.hasNext()) {
			Path pr = project(s, e, it.next());
			if (pr.getLength() > -1) ris.addPath(pr);
		}
		return ris;
	}

	/**
	 * The projection of the path set {@code p} with the start index {@code pos} and the end index {@code pos}
	 * @param pos the position index, that may depends on the length of the path (e.g. {@code pos(p) = p.length/2})
	 * @param p the path set
	 * @return a new path set, with all projected paths
	 */
	public static PathSet project(Index pos, PathSet p) {
		return project(pos, pos, p);
	}

	/**
	 * Projection for the single path. Note that {@code s(p) <= e(p)} for all path, this method only check that it is true for {@code p}.
	 * @param s the start position index, that may depends on the length of the path (e.g. {@code s(p) = p.length/2})
	 * @param e the end position index, that may depends on the length of the path (e.g. {@code e(p) = p.length/2})
	 * @param p the path
	 * @return the projection results. If {@code s(p) > p.length + 1} (i.e. the start position doesn't exists) returns an empty path
	 */
	public static Path project(Index s, Index e, Path p) {
		if (p == null) throw new NullPointerException("the path must be not null");
		if (s.get(p) > e.get(p)) throw new IllegalArgumentException("s(p) <= e(p) for all p");

		int k = p.getLength() + 1; //k is the last position (see the paper)

		if (s.get(p) > k) return Path.NULL_PATH; //If the start is after the last element we return an empty path

		int minek = (e.get(p) < k) ? e.get(p) : k;
		Path ris = new Path(minek - s.get(p));
		for (int i = s.get(p); i <= minek; i++) {
			ris.appendNode(p.getNodeAtPosition(i));
		}

		return ris;
	}

	/**
	 * Projection for the single path. Note that {@code s(p) <= e(p)} for all path, this method only check that it is true for {@code p}.
	 * @param pos the start and end position index, that may depends on the length of the path (e.g. {@code p.length/2})
	 *            this means that the projection is called with {@code s = e = pos}. Is the same if you call {@code project(pos, pos, p)}
	 * @param p the path
	 * @return the projection results. If {@code s(p) > p.length + 1} (i.e. the start position doesn't exists) returns an empty path
	 */
	public static Path project(Index pos, Path p) {
		return project(pos, pos, p);
	}

}
