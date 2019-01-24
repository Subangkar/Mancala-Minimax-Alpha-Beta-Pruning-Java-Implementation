package Minimax;

import source1.Mancala;

import java.util.ArrayList;

public class Minimax {
	
	static double INF = 10000000;
	
	public static int minimax( MinimaxProblem root ) {
		
		int idx = 0;
//		System.err.println( ">>For \n" + (Mancala)root + " Selected >> +\n"+root.getSuccessors());// + (Mancala)list.get( idx )
//		if (root == null) return -1;
		double max = alphabeta( root , -INF , INF , true );
		ArrayList< MinimaxProblem > list = root.getSuccessors();
		for (int i = 0; i < list.size(); ++i) {
			if (list.get( i ) != null && list.get( i ).getUtilVal() == max) idx = i;
			if (Mancala.DEBUG && list.get( i ) != null) System.out.print( list.get( i ).getUtilVal() + " " );
		}
		if (Mancala.DEBUG) System.out.println( " max=" + max );
		return idx;
	}
	
	
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
	
	
}
// This code is contributed by vt_m.
