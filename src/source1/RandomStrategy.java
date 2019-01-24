package source1;

public class RandomStrategy extends Strategy {
// A strategy that randomly chooses a non-empty bin for each move.
	
	Randomizer rand;
	
	public RandomStrategy() {
		rand = new Randomizer();
	}
	
	public int selectMove( Mancala board ) {
		int bins = board.getBins();
		int bin = rand.intBetween( 1 , bins );
		int player = board.currentPlayer();
		board.debugPrint( "Random: bin = " + bin + "; board.getBin(player, i) = "
				                  + board.getBin( board.currentPlayer() , bin ) );
		while (board.getBin( bin ) == 0) {
			board.debugPrint( "Random: bin = " + bin + "; board.getBin(player, bin) = "
					                  + board.getBin( board.currentPlayer() , bin ) );
			bin = rand.intBetween( 1 , bins );
		}
		return bin;
	}
	
	@Override
	public int getUtilValue( Mancala board ) {
		return 0;
	}
	
	public String toString() {
		return "RandomStrategy";
	}
	
}
