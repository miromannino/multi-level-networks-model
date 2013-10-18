package it.miromannino.multilevelnetwork.operator;

/**
 * © 2013 by Miro Mannino. All rights reserved
 */


import it.miromannino.multilevelnetwork.model.Path;

/**
 * The preposition that determines if a path is correct or not.
 * i.e. the path pattern decide the sructure of the path, but the path condition check the other things for a
 * path that satisfy a path pattern (like the content).
 *
 * one can use this in this way:
 *
 * <pre>
 * {@code
 * 	new PathCondition() {
 *		@literal@Override
 *		public ConditionResult predicate(Path p) {
 *			if (p.getSize() > 5) return ConditionResult.DefinitelyFalse;
 *			if (p.getSize() == 5) return ConditionResult.True;
 *			return ConditionResult.False;
 *		}
 *	}
 * }
 * </pre>
 *
 * that means we want a path with a length that is equal to 5. If we have a path with a length that is greater than
 * 5 we can return {@code DefinitelyFalse}, because extending the path we will have wrong paths.
 *
 */
public abstract class PathCondition {

	public enum ConditionResult {
		DefinitelyFalse, //The path is not correct, and extending the path with other nodes will be not correct.
						//This is to make some optimizations (branch and bound)
		False, //The path is not correct, but extengin the path we can have a path that is correct
				//e.g. the condition is about the length of the path, that is False until we reach the correct length
		True //The path is correct
	}

	abstract public ConditionResult predicate(Path p);

	/* ------- PATH CONDITION IMPLEMENTATIONS CLASSES --------- */
	/* useful classes, to help the user to not defines always the same classes */

	/**
	 * The true predicate (i.e. it returns always {@code ConditionResult.True})
	 */
	public static class TruePredicate extends PathCondition {

		public ConditionResult predicate(Path p) {
			return ConditionResult.True;
		}

	}

	/**
	 * The false predicate (i.e. it returns always {@code ConditionResult.False})
	 */
	public static class FalsePredicate extends PathCondition {

		public ConditionResult predicate(Path p) {
			return ConditionResult.False;
		}

	}

	/**
	 * The definitely false predicate (i.e. it returns always {@code ConditionResult.DefinitelyFalse})
	 */
	public static class DefinitelyFalsePredicate extends PathCondition {

		public ConditionResult predicate(Path p) {
			return ConditionResult.DefinitelyFalse;
		}

	}

	/**
	 * The predicate that is true only if the path has a specific length. Is better to use this class when one want
	 * to use this predicate, because it is optimized.
	 */
	public static class SpecificLengthPredicate extends PathCondition {

		int length;

		public SpecificLengthPredicate(int length) {
			if (length < -1) throw new IllegalArgumentException("the length must be greater or equal than -1");
			this.length = length;
		}

		public ConditionResult predicate(Path p) {
			if (p.getLength() > length) return ConditionResult.DefinitelyFalse;
			if (p.getLength() == length) return ConditionResult.True;
			return ConditionResult.False;
		}

	}

}
