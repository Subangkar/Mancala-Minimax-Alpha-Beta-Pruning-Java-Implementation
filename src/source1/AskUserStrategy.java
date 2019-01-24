package source1;

public class AskUserStrategy extends Strategy {
// A strategy that asks the user which bin to pick.
// Must ensure the user's choice is in range and non-empty.
	
	public AskUserStrategy() {
	}
	
	public int selectMove( Mancala board ) {
		int player = board.currentPlayer();
		int bins = board.getBins();
		int bin = Stdin.readIntInRange( 1 , bins , "Please select a bin to move for Player" + player );
		while (board.getBin( bin ) == 0) {
			System.err.println( "Bin " + bin + " is empty. Please try again." );
			bin = Stdin.readIntInRange( 1 , bins , "Please select a bin to move for Player" + player );
		}
		return bin;
	}
	
	public String toString() {
		return "AskUserStrategy";
	}
	
}
