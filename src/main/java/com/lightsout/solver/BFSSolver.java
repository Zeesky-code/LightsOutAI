package com.lightsout.solver;

import com.lightsout.model.Board;
import java.util.*;

/**
 * Breadth-First Search solver for Lights Out puzzle.
 */
public class BFSSolver extends SearchBasedSolver {
    
    public BFSSolver() {
        super(SolverConfig.getDefault());
    }

    public BFSSolver(SolverConfig config) {
        super(config);
    }

    @Override
    protected List<Move> searchForSolution(Board board) {
        Queue<SearchState> queue = new LinkedList<>();
        queue.offer(new SearchState(board, new ArrayList<>()));

        while (!queue.isEmpty() && !shouldTerminate()) {
            SearchState current = queue.poll();
            recordState(current.board, current.moves);

            if (countLightsOn(current.board) == 0) {
                return current.moves;
            }

            if (current.moves.size() >= config.getMaxMovesPerAttempt()) {
                continue;
            }

            for (Move move : getNextMoves(current.board, current.moves)) {
                Board nextBoard = current.board.copy();
                nextBoard.toggleLight(move.row, move.col);
                
                List<Move> nextMoves = new ArrayList<>(current.moves);
                nextMoves.add(move);

                queue.offer(new SearchState(nextBoard, nextMoves));
            }
        }

        return null;
    }

    /**
     * Helper class to store a board state and its corresponding move sequence.
     */
    private static class SearchState {
        final Board board;
        final List<Move> moves;

        SearchState(Board board, List<Move> moves) {
            this.board = board;
            this.moves = moves;
        }
    }
} 