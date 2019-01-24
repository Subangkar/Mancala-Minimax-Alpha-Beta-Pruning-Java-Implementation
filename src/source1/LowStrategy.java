package source1;

public class LowStrategy extends Strategy {
// A strategy that chooses the non-empty bin with the smallest index for each move.
	
	public LowStrategy() {
	}
	
	public int selectMove( Mancala board ) {
		int bins = board.getBins();
		for (int i = 1; i <= bins; i++) {
			board.debugPrint( "Low: i = " + i + "; board.getBin(player, i) = "
					                  + board.getBin( board.currentPlayer() , i ) );
			if (board.getBin( i ) != 0) {
				return i;
			}
		}
		// Should never get to this point, but java will yell if we don't return something.
		return bins;
	}
	
	@Override
	public int getUtilValue( Mancala board ) {
		return 0;
	}
	
	public String toString() {
		return "LowStrategy";
	}
	
}
