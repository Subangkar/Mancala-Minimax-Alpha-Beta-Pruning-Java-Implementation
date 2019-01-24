package Minimax;

import source1.Mancala;

import java.util.ArrayList;

public class Minimax {
	
	// Initial values of
// Aplha and Beta
	static int MAX = 1000;
	static int MIN = -1000;
	static double INF = 10000000;
	
	// Returns optimal value for
// current player (Initially called
// for root and maximizer)
	static int minimax( int depth , int nodeIndex ,
	                    Boolean maximizingPlayer ,
	                    int values[] , int alpha ,
	                    int beta ) {
		// Terminating condition. i.e
		// leaf node is reached
		if (depth == 3)
			return values[nodeIndex];
		
		if (maximizingPlayer) {
			int best = MIN;
			
			// Recur for left and
			// right children
			for (int i = 0; i < 2; i++) {
				int val = minimax( depth + 1 , nodeIndex * 2 + i ,
						false , values , alpha , beta );
				best = Math.max( best , val );
				alpha = Math.max( alpha , best );
				
				// Alpha Beta Pruning
				if (beta <= alpha)
					break;
			}
			return best;
		} else {
			int best = MAX;
			
			// Recur for left and
			// right children
			for (int i = 0; i < 2; i++) {
				
				int val = minimax( depth + 1 , nodeIndex * 2 + i ,
						true , values , alpha , beta );
				best = Math.min( best , val );
				beta = Math.min( beta , best );
				
				// Alpha Beta Pruning
				if (beta <= alpha)
					break;
			}
			return best;
		}
	}
	
	// Driver Code
	public static void main( String[] args ) {
		
		int values[] = { 3 , 5 , 6 , 9 , 1 , 2 , 0 , -1 };
		System.out.println( "The optimal value is : " +
				                    minimax( 0 , 0 , true , values , MIN , MAX ) );
		
	}
	
	
	static int depth;
	
	public static int minimax( MinimaxProblem root ) throws Exception {
		
		int idx = 0;
//		System.err.println( ">>For \n" + (Mancala)root + " Selected >> +\n"+root.getSuccessors());// + (Mancala)list.get( idx )
		double max = maxValue( root , -INF , INF );
		ArrayList< MinimaxProblem > list = root.getSuccessors();
		for (int i = 0; i < list.size(); ++i) {
			if (list.get( i ) != null && list.get( i ).getUtilVal() == max) idx = i;
		}
		return idx;
//		throw new Exception( "Item Not Found" );
	}
	
	private static double maxValue( MinimaxProblem state , double alpha , double beta ) {
		if (state.isTerminal()) return state.getUtilVal();
//		System.out.println("MAX = \n"+state.getSuccessors());
		double v = -INF;
		for (MinimaxProblem s : state.getSuccessors()) {
			if (s == null) continue;
			v = Math.max( v , minValue( s , alpha , beta ) );
			if (v >= beta) return v;
			alpha = Math.max( alpha , v );
		}
		return v;
	}
	
	private static double minValue( MinimaxProblem state , double alpha , double beta ) {
		if (state.isTerminal()) return state.getUtilVal();
//		System.out.println("MIN = "+state.getSuccessors());
		
		double v = INF;
		for (MinimaxProblem s : state.getSuccessors()) {
			if (s == null) continue;
			v = Math.min( v , maxValue( s , alpha , beta ) );
			if (v <= alpha) return v;
			beta = Math.min( beta , v );
		}
		return v;
	}
}
// This code is contributed by vt_m.
