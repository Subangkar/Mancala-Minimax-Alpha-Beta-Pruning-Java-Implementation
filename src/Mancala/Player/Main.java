package Mancala.Player;


import Mancala.Heuristics.MancalaHeuristic;

import java.io.*;

public class Main {
	public static final int nBins = 6;
	public static final int nStones = 4;
	public static final boolean DEBUG = false;
	public static final int MAX_DEPTH = 11;
	public static Stdin stdin;
	public static final int nMaxStages = 150;
	public static final boolean printBoard = false;
	public static final int NO_GAMES_PER_HEURISTIC = 50;
	public static final int MAX_ORDER = 2;
	public static final int[] HEURISTIC_PLAYER0 = {3};
	public static final int[] HEURISTIC_PLAYER1 = {2};
	
	private static PrintStream stdout_, stdin_;
	
	public static void main( String[] args ) throws IOException {
		stdin = new Stdin();
		stdout_ = System.out;
		PrintWriter logFile = new PrintWriter( new FileWriter( "log.log" ) );
		
		int h0, h1;
		for (int i : HEURISTIC_PLAYER0) {
			for (int j : HEURISTIC_PLAYER1) {
				for (int order = 0; order < MAX_ORDER; ++order) {
					if (order == 1 && i == j) break;
					h0 = order == 0 ? i : j;
					h1 = order == 0 ? j : i;
					int n0 = 0, n1 = 0, nD = 0;
					for (int game = 0; game < NO_GAMES_PER_HEURISTIC; ++game) {
						int r = playLoop( selectStrategy( h0 ) , selectStrategy( h1 ) );
						if (r == 0) ++n0;
						if (r == 1) ++n1;
						if (r == -1) ++nD;
						System.setOut( stdout_ );
						if (i == 1 && j == 1) break;
					}
					logFile.println( "0>Heuristic " + h0 + " :" + n0 + "\n1>Heuristic " + h1 + " :" + n1 + "\n >Draw        :" + nD + "\n" );
				}
				
			}
		}
		logFile.flush();
	}


///======================== IO Methods =======================================////
	
	public static int playLoop( MancalaHeuristic s0 , MancalaHeuristic s1 ) {
		int bins = nBins;//stdin.readInt( "Specify the number of bins on each side." );
		int stones = nStones;//stdin.readInt( "Specify the number of stones initially in each bin." );
		try {
			System.setOut( new PrintStream( "out.log" ) );
			System.setErr( new PrintStream( "err.log" ) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return play( bins , stones , s0 , s1 );
//		System.setOut( ps );
	}
	
	public static MancalaHeuristic selectStrategy( int n ) {
		return MancalaHeuristic.intToStrategy( n );
	}
	
	
	///======================== IO Methods =======================================////
//================================================================================
	
	public static int play( int bins , int stones , MancalaHeuristic s0 , MancalaHeuristic s1 ) {
		MancalaBoard board = new MancalaBoard( bins , stones , s0 , s1 );
		
		if (printBoard) System.out.println( board );
		int round = 0;
		while (!board.isGameOver() && round < nMaxStages) {
			if (printBoard) System.out.println( "------------" + round + "--------------" );
			int currentPlayer = board.currentPlayer();
			if (printBoard) System.out.println( "Player " + currentPlayer + "\'s move." );
			int bin = board.move();
			if (bin <= 0) break;
			if (printBoard) System.out.println( "Player " + currentPlayer + " selects "
					                                    + board.stonesMoved() + " stones from bin " + bin );
			if (printBoard) System.out.println( board );
			if (printBoard) System.out.println( "\n\n\n--------------------------\n\n\n" );
			round++;
		}
		System.out.println( "Final board configuration:\n" );
		System.out.println( board );
		if (board.getBin( 0 , 0 ) == board.getBin( 1 , 0 )) {
			if (printBoard) System.out.println( "The game ends in a tie!" );
			return -1;
		} else if (board.getBin( 0 , 0 ) > board.getBin( 1 , 0 )) {
			if (printBoard) System.out.println( "Player0 wins!" );
			return 0;
		} else {
			if (printBoard) System.out.println( "Player1 wins!" );
			return 1;
		}
	}
	
	
}
