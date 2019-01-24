package source1;

// A simple mancala game with a text based interface.


import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Mancala {
	
	private int[][] players;
	private int bins;
	private int stonesPerBin;
	private int totalStones;
	private int currentPlayer; // Player whose move is next.
	Strategy[] strategies; // An array with two elements = the two players' strategies.
	private int stonesMoved = 0; // Stones moved on last move.
	private boolean debugMode = false;
	
	public static Stdin stdin;
	
	/// bins is the number of bins per side, not including the mancala.
	/// stones is the number of stones initially in each bin.
	public Mancala( int bins , int stonesPerBin , Strategy s1 , Strategy s2 ) {
		this.bins = bins;
		this.stonesPerBin = stonesPerBin;
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
	
	public int getStonesPerBin() {
		return stonesPerBin;
	}
	
	public int getTotalStones() {
		return totalStones;
	}
///=============================== Getter-Setters ===========================///
//================================================================================
	
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
		// Return true if current player needs human input.
		// Not used by text-based version, but needed by video version.
		return currentPlayer;
	}
	
	public boolean isCurrentPlayerHuman() {
		// Return true if current player needs human input.
		// Not used by text-based version, but needed by video version.
		return Strategy.strategyToInt( strategies[currentPlayer] ) == 0;
	}
	
	public int getBin( int bin ) {
		// Player defaults to current player.
		return players[currentPlayer][bin];
	}
	
	public int getBin( int player , int bin ) {
		// Bin 0 is the mancala; other bins are numbered 1 through max number.
		return players[player][bin];
	}
	
	public boolean cannotMove() {
		// Returns true if the currentPlayer cannot move.
		debugPrint( "cannotMove(): currentPlayer = " + currentPlayer + "; board = \n" + this );
		for (int i = 1; i <= bins; i++) {
			if (players[currentPlayer][i] > 0) {
				// System.out.println("CannotMove: player = " + currentPlayer + "; result = " + false);
				debugPrint( "cannotMove() returns false" );
				return false;
			}
		}
		// System.out.println("CannotMove: player = " + currentPlayer + "; result = " + true);
		debugPrint( "cannotMove() returns true" );
		return true;
	}
	
	public boolean isGameOver() {
		// Game is over when either (1) one player has more than half of the total
		// stones in his mancala; or (2) current player cannot move.
		int half = totalStones / 2;
		debugPrint( "isGameOver: player = " + currentPlayer
				            + "; half = " + half
				            + "; players[0][0] = " + players[0][0]
				            + ";\n   players[1][0] = " + players[1][0]
				            + "; cannotMove() = " + cannotMove()
				            + "; result = " + ((players[0][0] > half) || (players[1][0] > half) || cannotMove()) );
		
		return (players[0][0] > half) || (players[1][0] > half) || cannotMove();
	}
	
	public int move() {
		// Invokes strategy of current player to make move. Returns the bin selected.
		int bin = strategies[currentPlayer].selectMove( this );
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
		debugPrint( "Board before move(" + bin + "):\n" + this );
		if ((bin <= 0) || (bin > bins)) {
			printError( "MOVE: illegal bin index " + bin );
		} else {
			int stones = players[currentPlayer][bin];
			if (stones == 0) {
				printError( "MOVE: Attempt to move bin with no stones." );
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
								debugPrint( "Board after move(" + bin + "):\n" + this );
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
					debugPrint( "Board after move(" + bin + "):\n" + this );
				}
			}
		}
	}
	
	
	private void stealBin( int player , int bin ) {
		// Steal stones from bin opposite specified bin to put in this player's mancala,
		// along with extra stone about to be place in empty bin.
		debugPrint( "Board before stealBin:\n" + this );
		int oppositeBin = bins + 1 - bin;
		int oppositePlayer = otherPlayer( player );
		players[player][0] += players[oppositePlayer][oppositeBin] + 1;
		players[oppositePlayer][oppositeBin] = 0;
		// System.out.println("Stole stones from Player" + oppositePlayer + "\'s bin " + oppositeBin);
		debugPrint( "Board after stealBin:\n" + this );
	}
	
	public void flushStones( int player ) {
		for (int i = 1; i <= bins; i++) {
			flushBin( player , i );
		}
	}
	
	private void flushBin( int player , int bin ) {
		// Add stones from bin n to the mancala.
		players[player][0] += players[player][bin];
		players[player][bin] = 0;
	}
	
	// Given one player, return the other.
	public static int otherPlayer( int player ) {
		return (player + 1) % 2;
	}
	
	public void printError( String msg ) {
		System.out.println( "Mancala error -- " + msg );
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
	
	
	public static void main( String[] args ) {
		stdin = new Stdin();
		playLoop();
	}


///======================== IO Methods =======================================////
	// A driver loop for the text-based version of the game.
	// You will not use code below this line in the graphical version of the game.
	// However, you should study the play() method, since you will need to perform
	// similar actions in your code.
	
	public static void playLoop() {
		int bins = stdin.readInt( "Specify the number of bins on each side." );
		int stones = stdin.readInt( "Specify the number of stones initially in each bin." );
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
		while (stdin.readBoolean( "Do you wish to play another game, give boolean value, true/false" )) {
			play( bins , stones , s0 , s1 );
		}
	}
	
	public static Strategy selectStrategy( int player ) {
		System.err.println( "Select a strategy for Player" + player );
		System.err.println( "  0 is AskUserStrategy" );
		System.err.println( "  1 is RandomStrategy" );
		System.err.println( "  2 is LowStrategy" );
		System.err.println( "  3 is AgainStrategy" );
		return Strategy.intToStrategy( stdin.readIntInRange( 0 , 3 ) );
	}
	
	
	public void debugPrint( String s ) {
		if (debugMode) {
			System.out.println( s );
		}
	}

        /*public Mancala clone() {
                //Mancala now = new Mancala(this.bins, this.stonesPerBin, this.strategies[0], this.strategies[1]);
        }*/
///======================== IO Methods =======================================////
//================================================================================
	
	public static void play( int bins , int stones , Strategy s0 , Strategy s1 ) {
		Mancala board = new Mancala( bins , stones , s0 , s1 );
		
		System.out.println( board );
		while (!board.isGameOver()) {
			int currentPlayer = board.currentPlayer();
			System.out.println( "Player " + currentPlayer + "\'s move." );
			int bin = board.move();
			System.out.println( "Player " + currentPlayer + " selects "
					                    + board.stonesMoved() + " stones from bin " + bin );
			System.out.println( board );
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
	
}

