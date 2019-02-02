package Mancala;


public abstract class MancalaHeuristic {
	abstract public int selectMove( MancalaBoard board );
	// Return the index of a non-empty bin to move.
	// Assumes that at least one move is possible.
	
	static MancalaHeuristic intToStrategy( int i ) {
		// Returns a new MancalaHeuristic corresponding to the number between 0 and 3.
		if (i == 1) {
			return new Heuristic1();
//		} else if (i == 2) {
//			return new LowStrategy();
//		} else if (i == 3) {
//			return new AgainStrategy();
		} else {
			return new UserHeuristic();
		}
	}
	
	
	public static int strategyToInt( MancalaHeuristic s ) {
		// Returns an integer corresponding to the given strategy.
		// Make this a static rather than an instance method to put all numbers in one place.
//		if (s instanceof RandomStrategy) {
//			return 1;
//		} else if (s instanceof LowStrategy) {
//			return 2;
//		} else if (s instanceof AgainStrategy) {
//			return 3;
//		} else {
//			return 0;
//		}
		return 0;
	}
	
	public abstract int getUtilValue( MancalaBoard board );
}
