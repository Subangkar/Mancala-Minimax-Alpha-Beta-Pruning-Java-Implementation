package source1;

import Minimax.*;

import static source1.Mancala.MAX_DEPTH;

public class AskUserStrategy extends Strategy {
// A strategy that asks the user which bin to pick.
// Must ensure the user's choice is in range and non-empty.
	
	public AskUserStrategy() {
	}
	
	public int selectMove( Mancala board ) {
//		int player = board.currentPlayer();
//		int bins = board.getBins();
//		int bin = Stdin.readIntInRange( 1 , bins , "Please select a bin to move for Player" + player );
//		while (board.getBin( bin ) == 0) {
//			System.err.println( "Bin " + bin + " is empty. Please try again." );
//			bin = Stdin.readIntInRange( 1 , bins , "Please select a bin to move for Player" + player );
//		}
//		System.out.println("Ok");
		int bin = 0;
		try {
			bin = Minimax.minimax( board,MAX_DEPTH ) + 1; // index starts from 0 bt bin from 1
			if (Mancala.DEBUG)
				for (MinimaxProblem m : board.getSuccessors()) {
//					if (m != null) System.err.println( m + " has " + Minimax.minimax( m ) + "\n\n" );
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bin;
	}
	
	@Override
	public int getUtilValue( Mancala board ) {
		//heuristic-1: The evaluation function is
		//(stones_in_my_storage – stones_in_opponents_storage)
		int stones_in_my_storage = board.getStonesInStorage( board.getMaxPlayer() );//board.getPlayersTotalStones( board.currentPlayer() );
		int stones_in_opponents_storage = board.getStonesInStorage( Mancala.otherPlayer( board.getMaxPlayer() ) );//board.getPlayersTotalStones( MancalaBoard.otherPlayer( board.currentPlayer() ) );
		return stones_in_my_storage - stones_in_opponents_storage;
	}
	
	public String toString() {
		return "AskUserStrategy";
	}
	
}
