package Mancala;

import Minimax.*;

public class Heuristic1 extends MancalaHeuristic {
	@Override
	public int selectMove( MancalaBoard board ) {
		int bin = 0;
		try {
			bin = Minimax.minimax( board , Main.MAX_DEPTH ) + 1; // index starts from 0 bt bin from 1
			if (Main.DEBUG)
				for (MinimaxProblem m : board.getSuccessors()) {
//					if (m != null) System.err.println( m + " has " + Minimax.minimax( m ) + "\n\n" );
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bin;
	}
	
	@Override
	public int getUtilValue( MancalaBoard board ) {
		//heuristic-1: The evaluation function is
		//(stones_in_my_storage – stones_in_opponents_storage)
		int stones_in_my_storage = board.getStonesInStorage( board.getMaxPlayer() );//board.getPlayersTotalStones( board.currentPlayer() );
		int stones_in_opponents_storage = board.getStonesInStorage( MancalaBoard.otherPlayer( board.getMaxPlayer() ) );//board.getPlayersTotalStones( MancalaBoard.otherPlayer( board.currentPlayer() ) );
		return stones_in_my_storage - stones_in_opponents_storage;
	}
}
