package source1;

public class AgainStrategy extends Strategy {
// A strategy that attempts to find a move that will lead to another move.
// If there is no such move, defers to the LowStrategy.
	
	LowStrategy low;
	
	public AgainStrategy() {
		low = new LowStrategy();
	}
	
	public int selectMove( Mancala board ) {
		int bins = board.getBins();
		for (int i = 1; i <= bins; i++) {
			board.debugPrint( "Again: i = " + i + "; board.getBin(player, i) = "
					                  + board.getBin( board.currentPlayer() , i ) );
			if (board.getBin( i ) == i) {
				return i;
			}
			
		}
		// Didn't find a bin that will allow us to go again. Defer to low strategy.
		return low.selectMove( board );
	}
	
	@Override
	public int getUtilValue( Mancala board ) {
		return 0;
	}
	
	public String toString() {
		return "AgainStrategy";
	}
	
}
