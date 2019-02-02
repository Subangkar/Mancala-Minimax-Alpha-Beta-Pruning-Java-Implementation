package Mancala.Heuristics;

import Mancala.Player.MancalaBoard;

public class Heuristic1 extends MancalaHeuristic {
	@Override
	public int getUtilValue( MancalaBoard board ) {
		//heuristic-1: The evaluation function is
		//(stones_in_my_storage – stones_in_opponents_storage)
		int stones_in_my_storage = board.getStonesInStorage( board.getMaxPlayer() );//board.getPlayersTotalStones( board.currentPlayer() );
		int stones_in_opponents_storage = board.getStonesInStorage( MancalaBoard.otherPlayer( board.getMaxPlayer() ) );//board.getPlayersTotalStones( MancalaBoard.otherPlayer( board.currentPlayer() ) );
		return stones_in_my_storage - stones_in_opponents_storage;
	}
}
