package source1;

// A simple mancala game with a text based interface.


import Minimax.MinimaxProblem;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class Mancala implements MinimaxProblem, Cloneable {
	
	public static final boolean DEBUG = false;
	
	private int[][] players;
	private int bins;
	private int totalStones;
	private int currentPlayer; // Player whose move is next.
	Strategy[] strategies; // An array with two elements = the two players' strategies.
	private int stonesMoved = 0; // Stones moved on last move.
	private boolean debugMode = false;
	
	private boolean maximizing = false;
	
	public void setMaximizing( boolean maximizing ) {
		this.maximizing = maximizing;
	}
	
	private static final int STORAGE = 0;
	
	public static Stdin stdin;
	
	/// bins is the number of bins per side, not including the mancala.
	/// stones is the number of stones initially in each bin.
	public Mancala( int bins , int stonesPerBin , Strategy s1 , Strategy s2 ) {
		this.bins = bins;
		this.totalStones = bins * stonesPerBin * 2;
		players = new int[2][bins + 1];
		for (int i = 0; i <= 1; i++) {
//                        players[i] = new int [bins + 1];
			for (int j = 1; j <= bins; j++) { // Initially 0 stones in the mancala.
				players[i][j] = stonesPerBin;
			}
		}
		currentPlayer = 0; // Player0 always starts first.
		strategies = new Strategy[2];
		strategies[0] = s1;
		strategies[1] = s2;
	}

///=============================== Getter-Setters ===========================///
	
	public int getBins() {
		return bins;
	}
	
	public int getPlayersTotalStones( int playerNo ) {
		int sum = 0;
		for (int j = 1; j <= bins; j++) {
			sum += players[playerNo][j];
		}
		return sum;
	}
	
	public int getStonesInStorage( int player ) {
		return players[player][STORAGE];
	}
///=============================== Getter-Setters ===========================///
	
	public int countTotalStones() {
		int result = 0;
		for (int i = 0; i <= 1; i++) {
			for (int j = 0; j <= bins; j++) {
				result = result + players[i][j];
			}
		}
		return result;
	}
	
	public int stonesMoved() {
		return stonesMoved;
	}
	
	public int currentPlayer() {
		return currentPlayer;
	}
	
	public int opponentPlayer() {
		return otherPlayer( currentPlayer );
	}
	
	// Given one player, return the other.
	public static int otherPlayer( int player ) {
		return (player + 1) % 2;
	}

	// Player defaults to current player.
	public int getBin( int bin ) {
		return players[currentPlayer][bin];
	}
	
	// Bin 0 is the mancala; other bins are numbered 1 through max number.
	public int getBin( int player , int bin ) {
		return players[player][bin];
	}
	
	public boolean cannotMove() {
		// Returns true if the currentPlayer cannot move.
		for (int i = 1; i <= bins; i++) {
			if (players[currentPlayer][i] > 0) {
				// System.out.println("CannotMove: player = " + currentPlayer + "; result = " + false);
				return false;
			}
		}
		// System.out.println("CannotMove: player = " + currentPlayer + "; result = " + true);
		return true;
	}
	
	// Steal stones from bin opposite specified bin to put in this player's mancala,
	// along with extra stone about to be place in empty bin.
	private void stealBin( int player , int bin ) {
		int oppositeBin = bins + 1 - bin;
		int oppositePlayer = otherPlayer( player );
		players[player][STORAGE] += players[oppositePlayer][oppositeBin] + 1;
		players[oppositePlayer][oppositeBin] = 0;
	}
	
	public void flushStones( int player ) {
		for (int i = 1; i <= bins; i++) {
			flushBin( player , i );
		}
	}
	
	// Add stones from bin n to the mancala.
	private void flushBin( int player , int bin ) {
		players[player][STORAGE] += players[player][bin];
		players[player][bin] = 0;
	}
	
	
	
	
	
	
	
	
	
	
	// Game is over when either (1) one player has more than half of the total
	// stones in his mancala; or (2) current player cannot move.
	public boolean isGameOver() {
		int half = totalStones / 2;
		return (players[0][STORAGE] > half) || (players[1][STORAGE] > half) || cannotMove();
//		return getPlayersTotalStones( currentPlayer ) == 0 && getPlayersTotalStones( opponentPlayer() ) == 0;
	}
	
	public int move() {
		// Invokes strategy of current player to make move. Returns the bin selected.
		int bin = strategies[currentPlayer].selectMove( this );
//		if (bin <= 0) return -1;
		if (currentPlayer == 1)
			bin = players[0].length - bin;// need to think length is already +1
//		System.out.println( this + "returned " + bin + " when size=" + getSuccessors().size() );
		move( bin );
		return bin;
	}
	
	
	public void move( int bin ) {
		// This method is a mess! I bet it can be simplified.
		
		// Bin should be a bin index for current player that holds one or more stones.
		// Performs the basic Mancala move: removes stones from bin
		// and places around board. IF last stone is placed in empty
		// bin one player's side, that stone and opposing stones are
		// placed in player's mancala. If last stone goes in player's
		// Mancala, current player stays the same and so goes again.
		// Otherwise, current player becomes other player.
		int stones = players[currentPlayer][bin];
		if (getPlayersTotalStones( opponentPlayer() ) == 0) {
			flushStones( currentPlayer );
			currentPlayer = opponentPlayer();
		} else {
			stonesMoved = stones;
			players[currentPlayer][bin] = 0;
			int currentSide = currentPlayer;
			int currentBin = bin - 1; // Start distributing stones in well to right of selected bin.
			for (int s = stones; s > 0; s--) {
				if ((s == 1) && (currentSide == currentPlayer) && (currentBin > 0)
						    && (players[currentSide][currentBin] == 0)) {
					// Check for case of "stealing" stones from other side.
					// Special cases for distributing stones to a mancala.
					stealBin( currentPlayer , currentBin );
				} else if (currentBin == 0) {
					if (currentSide == currentPlayer) {
						// If it's our mancala, place a stone.
						players[currentSide][currentBin]++;
						if (s == 1) {
							// We're placing our last stone in our own Mancala.
							if (cannotMove()) {
								flushStones( otherPlayer( currentPlayer ) );
							}
							return;
						}
					} else {
						// If it's other mancala, don't place a stone.
						s++; // Counteract subtract of for loop. Yuck!
					}
					// In any case, switch to other side.
					currentSide = otherPlayer( currentSide );
					currentBin = bins;
				} else {
					// Regular stone distribution.
					players[currentSide][currentBin]++;
					currentBin--;
				}
			}
			currentPlayer = otherPlayer( currentPlayer );
			if (cannotMove()) {
				flushStones( otherPlayer( currentPlayer ) );
			}
		}
	}
	
	public static void main( String[] args ) {
		stdin = new Stdin();
		playLoop();
	}


///======================== IO Methods =======================================////
	
	public static void playLoop() {
		int bins = 2;//stdin.readInt( "Specify the number of bins on each side." );
		int stones = 2;//stdin.readInt( "Specify the number of stones initially in each bin." );
		Strategy s0 = selectStrategy( 0 );
		Strategy s1 = selectStrategy( 1 );
		PrintStream ps = System.out;
		try {
			System.setOut( new PrintStream( "out.txt" ) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		play( bins , stones , s0 , s1 );
		System.setOut( ps );
	}
	
	public static Strategy selectStrategy( int player ) {
		return Strategy.intToStrategy( 0 );
	}
	
	
	///======================== IO Methods =======================================////
//================================================================================
	
	public static void play( int bins , int stones , Strategy s0 , Strategy s1 ) {
		Mancala board = new Mancala( bins , stones , s0 , s1 );

//		try {
////			System.out.println( board.getSuccessor( 1 ).getSuccessor( 1 ) + " " + board.getSuccessor( 2 ).getSuccessor( 1 ).getSuccessors() );
//			System.out.println( board + " " + board.getSuccessor( 2 ) + " " + board.getSuccessor( 2 ).getSuccessor( 1 ) );
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}

//		board.move( 1 );
//		board
		
		System.out.println( board );
		int round = 0;
		while (!board.isGameOver()) {
			System.out.println( "------------" + round + "--------------" );
			int currentPlayer = board.currentPlayer();
			System.out.println( "Player " + currentPlayer + "\'s move." );
			int bin = board.move();
			if (bin <= 0) break;
			System.out.println( "Player " + currentPlayer + " selects "
					                    + board.stonesMoved() + " stones from bin " + bin );
			System.out.println( board );
			System.out.println( "\n\n\n--------------------------\n\n\n" );
			round++;
		}
		System.out.println( "Final board configuration:\n" );
		System.out.println( board );
		if (board.getBin( 0 , 0 ) == board.getBin( 1 , 0 )) {
			System.out.println( "The game ends in a tie!" );
		} else if (board.getBin( 0 , 0 ) > board.getBin( 1 , 0 )) {
			System.out.println( "Player0 wins!" );
		} else {
			System.out.println( "Player1 wins!" );
		}
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//========================== Minimax Methods =================================
	
	@Override
	public double getUtilVal() {
		return strategies[currentPlayer()].getUtilValue( this );
	}
	
	public Mancala getSuccessor( int bin ) throws CloneNotSupportedException {
		Mancala suc = (Mancala) this.clone();
		suc.move( bin );
		if (this.currentPlayer == suc.currentPlayer) suc.setMaximizing( this.maximizing );
		else suc.setMaximizing( !this.maximizing );
		return suc;
	}
	
	@Override
	public ArrayList< MinimaxProblem > getSuccessors() {
		ArrayList< MinimaxProblem > suclist = new ArrayList<>();
//		if (currentPlayer == 0)
		for (int i = 1; i <= bins; ++i) {
			try {
				if (players[currentPlayer][i] > 0)
					suclist.add( i - 1 , getSuccessor( i ) );
				else
					suclist.add( i - 1 , null );
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return suclist;
	}
	
	@Override
	public boolean isTerminal() {
		return isGameOver();
	}
	
	@Override
	public boolean isMaximizing() {
		return false;
	}
// ==========================================================================
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Mancala clone = (Mancala) super.clone();
		clone.players = new int[this.players.length][this.players[0].length];
		for (int r = 0; r < this.players.length; r++)
			if (this.players[r].length >= 0)
				System.arraycopy( this.players[r] , 0 , clone.players[r] , 0 , this.players[r].length );
		clone.strategies = this.strategies.clone();
		return clone;
	}


///======================== Print Utilities ==================================////
	
	public String toString() {
		// Return a string-based representation of this game.
		return edgeLine() + player0Line() + middleLine() + player1Line() + edgeLine()
				       + (debugMode ? (countTotalStones() + " stones.\n") : "")
				;
	}
	
	public String edgeLine() {
		return "+----" + middleDashes() + "----+\n";
	}
	
	public String player0Line() {
		StringBuffer sb = new StringBuffer();
		sb.append( "|    |" );
		for (int i = 1; i <= bins; i++) {
			sb.append( " " + numberString( getBin( 0 , i ) ) + " |" );
		}
		sb.append( "    |\n" );
		return sb.toString();
	}
	
	public String middleLine() {
		return "| " + numberString( getBin( 0 , 0 ) ) + " "
				       + middleDashes()
				       + " " + numberString( getBin( 1 , 0 ) ) + " |\n";
	}
	
	public String player1Line() {
		StringBuffer sb = new StringBuffer();
		sb.append( "|    |" );
		for (int i = bins; i > 0; i--) {
			sb.append( " " + numberString( getBin( 1 , i ) ) + " |" );
		}
		sb.append( "    |\n" );
		return sb.toString();
	}
	
	public String middleDashes() {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= bins; i++) {
			sb.append( "+----" );
		}
		sb.append( "+" );
		return sb.toString();
	}
	
	public String numberString( int n ) {
		// Return a two character string with an integer.
		// Assumes input is in range [0..99].
		if ((0 <= n) && (n < 10)) {
			return " " + Integer.toString( n );
		} else {
			return Integer.toString( n );
		}
	}

///======================== Print Utilities ==================================////
//================================================================================

}

