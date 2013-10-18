package it.miromannino.multilevelnetwork.generic;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


public class MutableInt {

	int value;

	public MutableInt(int initialValue) {
		this.value = initialValue;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void incrementByOne() {
		this.value++;
	}

	public void decrementByOne() {
		this.value--;
	}

	public void incrementBy(int step) {
		this.value += step;
	}

	public void decrementBy(int step) {
		this.value -= step;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}