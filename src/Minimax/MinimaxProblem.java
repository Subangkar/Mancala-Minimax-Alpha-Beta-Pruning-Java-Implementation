package Minimax;

import java.util.ArrayList;

public interface MinimaxProblem {
	double getUtilVal();
	ArrayList<MinimaxProblem> getSuccessors();
	boolean isTerminal();
	
}
