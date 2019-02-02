package Mancala;

import source1.Stdin;
//import source1.MancalaHeuristic;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
	public static final int nBins = 2;
	public static final int nStones = 6;
	public static final boolean DEBUG = true;
	public static final int MAX_DEPTH = 15;
	public static Stdin stdin;
	public static final int nMaxStages = 150;
	
	public static void main( String[] args ) {
		stdin = new Stdin();
		playLoop();
	}


///======================== IO Methods =======================================////
	
	public static void playLoop() {
		int bins = nBins;//stdin.readInt( "Specify the number of bins on each side." );
		int stones = nStones;//stdin.readInt( "Specify the number of stones initially in each bin." );
		MancalaHeuristic s0 = selectStrategy( 0 );
		MancalaHeuristic s1 = selectStrategy( 1 );
		PrintStream ps = System.out;
		try {
//			System.setOut( new PrintStream( "out.txt" ) );
			System.setErr( new PrintStream( "log.txt" ) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		play( bins , stones , s0 , s1 );
		System.setOut( ps );
	}
	
	public static MancalaHeuristic selectStrategy( int player ) {
		return MancalaHeuristic.intToStrategy( player == 1 ? 0 : 1 );
	}
	
	
	///======================== IO Methods =======================================////
//================================================================================
	
	public static void play( int bins , int stones , MancalaHeuristic s0 , MancalaHeuristic s1 ) {
		MancalaBoard board = new MancalaBoard( bins , stones , s0 , s1 );

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
		while (!board.isGameOver() && round < nMaxStages) {
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
	
	
}
