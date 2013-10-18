package it.miromannino.multilevelnetwork.model;

import java.util.HashSet;
import java.util.Iterator;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class PathSet implements Iterable<Path> {

	HashSet<Path> set;

	/**
	 * Create a PathSet with the nodes in the array. All of these nodes must be not null, and in the case of duplicates
	 * only one path is inserted in the set.
	 */
	public PathSet(Path[] paths) {
		set = new HashSet<Path>(paths.length);
		for (Path p : paths) {
			if (p == null) throw new NullPointerException("all paths in the array must be not null");
			set.add(p);
		}
	}

	/**
	 * Create an empty PathSet with an initial capacity of {@code initialCapacity}
	 * @param initialCapacity the initial capacity of the set that store the path set (we use {@code HashSet})
	 */
	public PathSet(int initialCapacity) {
		set = new HashSet<Path>(initialCapacity);
	}

	/**
	 * Create an empty PathSet
	 */
	public PathSet() {
		set = new HashSet<Path>();
	}

	/**
	 * Add a path to the set.
	 * @param p the path to add
	 * @return If this set already contains the element, the call leaves the set unchanged and returns false.
	 */
	public boolean addPath(Path p) {
		return set.add(p);
	}

	/**
	 * Remove a path form the set.
	 * @param p the path to remove
	 * @return Returns true if this set contained the element (or equivalently, if this set changed as a result of the call).
	 */
	public boolean removePath(Path p) {
		return set.remove(p);
	}

	/**
	 * Determines if the set contains a path p
	 * @param p the path to check
	 * @return returns true if and only if this set contains an element e such that (path==null ? e==null : path.equals(e)).
	 */
	public boolean containsPath(Path p) {
		return set.contains(p);
	}

	/**
	 * Return the number of paths in the set
	 */
	public int getSize() {
		return set.size();
	}

	/**
	 * Return the iterator to iterate the paths
	 */
	@Override
	public Iterator<Path> iterator() {
		return set.iterator();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<Path> it = set.iterator();
		sb.append("{");
		while(it.hasNext()) {
			Path p = it.next();
			sb.append(p.toString());
			if (it.hasNext()) sb.append(", ");
		}
		sb.append("}");
		return sb.toString();
	}

}
