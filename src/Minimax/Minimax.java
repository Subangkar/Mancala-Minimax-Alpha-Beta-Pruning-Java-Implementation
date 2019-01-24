package Minimax;

import java.util.ArrayList;

public class Minimax {
	
	// Initial values of
// Aplha and Beta
	static int MAX = 1000;
	static int MIN = -1000;
	static double INF = 10000000;
	
	static double alphabeta( MinimaxProblem state , double alpha , double beta , boolean isMaximizing ) {
		if (state.isTerminal()) return state.getUtilVal();
		if (isMaximizing) {
			double v = -INF;
			for (MinimaxProblem s : state.getSuccessors()) {
				if (s == null) continue;
				v = Math.max( v , alphabeta( s , alpha , beta , s.isMaximizing() ) );
				alpha = Math.max( alpha , v );
				if (alpha >= beta) break; //beta-cut-off
			}
			return v;
			
		} else {
			double v = INF;
			for (MinimaxProblem s : state.getSuccessors()) {
				if (s == null) continue;
				v = Math.min( v , alphabeta( s , alpha , beta , s.isMaximizing() ) );
				beta = Math.min( beta , v );
				if (alpha >= beta) break; //alpha-cut-off
			}
			return v;
		}
	}
	
	
	public static int minimax( MinimaxProblem root ) throws Exception {
		
		int idx = 0;
//		System.err.println( ">>For \n" + (Mancala)root + " Selected >> +\n"+root.getSuccessors());// + (Mancala)list.get( idx )
		double max = alphabeta( root , -INF , INF , true );
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
