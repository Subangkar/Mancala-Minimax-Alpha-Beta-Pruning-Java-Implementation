package Minimax;

import source1.Mancala;
import Mancala.*;

import java.util.ArrayList;

public class Minimax {
	
	private static final double INF = 10000000;
	
	private static class Solution {
		MinimaxProblem instance;
		double v;
		
		public Solution( MinimaxProblem instance , double v ) {
			this.instance = instance;
			this.v = v;
		}
		
		void set( MinimaxProblem instance , double v ) {
			this.instance = instance;
			this.v = v;
		}
		
		static Solution max( Solution sol1 , Solution sol2 ) {
			if (sol2 == null || sol1 != null && sol1.v > sol2.v) return sol1;
			else return sol2;
		}
		
		static Solution min( Solution sol1 , Solution sol2 ) {
			if (sol2 == null || sol1 != null && sol1.v < sol2.v) return sol1;
			else return sol2;
		}
	}
	
	public static int minimax( MinimaxProblem root , int maxDepth ) {
		Solution opt = alphabeta( root , -INF , INF , true , maxDepth );
		ArrayList< MinimaxProblem > list = root.getSuccessors();
		for (int i = 0; i < list.size(); ++i) {
			if (list.get( i ) != null && list.get( i ).problemequals( opt.instance )) return i;
		}
//		for (MinimaxProblem suc : root.getSuccessors()) {
//			if (suc != null && suc.problemequals( opt.instance )) return suc;
//		}
		return -1;
	}
	
	
	private static Solution alphabeta( MinimaxProblem state , double alpha , double beta , boolean isMaximizing , int maxdepth ) {
		if (state.isTerminal() || maxdepth == 0) {
			System.err.println( "----------Term -------------" + ((MancalaBoard) state).currentPlayer() + "\n\n" );
			return new Solution( state , state.getUtilVal() );
		}
//		for (int i = 0; i < 2 * maxdepth; ++i) System.err.print( "---------------" );
		for (int i = 0; i < maxdepth; ++i) System.err.print( "---------------" );
		if (isMaximizing) {
			Solution maxSolution = new Solution( state , -INF );
			System.err.println( "----------------------Taking Max--------------" + ((MancalaBoard) state).currentPlayer() + "\n\n" );
			for (MinimaxProblem s : state.getSuccessors()) {
				if (s == null) continue;
				Solution solution = alphabeta( s , alpha , beta , s.isMaximizing() , maxdepth - 1 );
//				System.err.println( solution.instance + " has Util: " + solution.v + "\n\n" );
				solution = new Solution( s , solution.v );
				maxSolution = Solution.max( maxSolution , solution );
				alpha = Math.max( alpha , maxSolution.v );
				if (alpha >= beta) break; //beta-cut-off
			}
			for (int i = 0; i < maxdepth; ++i) System.err.print( "---------------" );
			System.err.println( "@Depth: " + maxdepth + "  " + maxSolution.instance + " has Util: " + maxSolution.v + "\n\n" );
			System.err.println( "----------------------Took   Max--------------\n\n" );
			return maxSolution;
			
		} else {
			Solution minSolution = new Solution( null , INF );
			System.err.println( "----------------------Taking Min--------------" + ((MancalaBoard) state).currentPlayer() + "\n\n" );
			for (MinimaxProblem s : state.getSuccessors()) {
				if (s == null) continue;
				Solution solution = alphabeta( s , alpha , beta , s.isMaximizing() , maxdepth - 1 );
//				System.err.println( solution.instance + " has Util: " + solution.v + "\n\n" );
				solution = new Solution( s , solution.v );
				minSolution = Solution.min( minSolution , solution );
				beta = Math.min( beta , minSolution.v );
				if (alpha >= beta) break; //alpha-cut-off
			}
			for (int i = 0; i < maxdepth; ++i) System.err.print( "---------------" );
			System.err.println( "@Depth: " + maxdepth + "  " + minSolution.instance + " has Util: " + minSolution.v + "\n\n" );
			System.err.println( "----------------------Took   Min--------------\n\n" );
			return minSolution;
		}
	}
	
	
}
// This code is contributed by vt_m.
