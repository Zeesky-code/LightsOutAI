package com.lightsout.solver;

import com.lightsout.model.Board;
import java.util.*;

/**
 * Depth-First Search solver for Lights Out puzzle.
 */
public class DFSSolver extends SearchBasedSolver {
    
    public DFSSolver() {
        super(SolverConfig.getDefault());
    }

    public DFSSolver(SolverConfig config) {
        super(config);
    }

    @Override
    protected List<Move> searchForSolution(Board board) {
        return dfs(board, new ArrayList<>());
    }

    private List<Move> dfs(Board board, List<Move> currentMoves) {
        if (shouldTerminate()) {
            return null;
        }

        recordState(board, currentMoves);

        if (countLightsOn(board) == 0) {
            return currentMoves;
        }
        if (currentMoves.size() >= config.getMaxMovesPerAttempt()) {
            return null;
        }

        for (Move move : getNextMoves(board, currentMoves)) {
            Board nextBoard = board.copy();
            nextBoard.toggleLight(move.row, move.col);
            
            List<Move> nextMoves = new ArrayList<>(currentMoves);
            nextMoves.add(move);

            List<Move> solution = dfs(nextBoard, nextMoves);
            if (solution != null) {
                return solution;
            }
        }

        return null;
    }
} 