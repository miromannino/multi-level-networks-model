package it.miromannino.multilevelnetwork.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class LevelsCoupling implements Iterable<Couple> {

	List<Couple> couples;

	/**
	 * Create a Levels Coupling without couples (i.e. no couples)
	 */
	public LevelsCoupling() {
		this.couples = new ArrayList<Couple>();
	}

	/**
	 * Create a Levels Coupling with the couples in the array {@code couples}.
	 */
	public LevelsCoupling(Couple[] couples) {
		for (Couple c : couples) if (c == null) throw new NullPointerException("couples must be not null");
		this.couples = new ArrayList<Couple>(couples.length);
		for (Couple c : couples) this.couples.add(c);
	}

	/**
	 * Return the number of couples in the Levels Coupling
	 */
	public int getNumberOfCouples() {
		return couples.size();
	}

	/**
	 * Return the couple at pos {@code pos}.
	 * @param pos the position of the couple
	 */
	public Couple getCouple(int pos) {
		return couples.get(pos);
	}

	/**
	 * Removes the first occurrence of the specified couple from this list of couples.
	 * If this list does not contain the element, it is unchanged.
	 *
	 * @return {@code true} if this list contained the specified couple
	 * */
	public boolean removeCouple(Couple couple) {
		if (couple == null) throw new NullPointerException("couple must be not null");
		return couples.remove(couple);
	}

	/**
	 * To iterate the couples in the level coupling
	 */
	public Iterator<Couple> iterator() {
		return couples.iterator();
	}

}
