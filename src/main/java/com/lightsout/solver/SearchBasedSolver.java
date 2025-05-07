package com.lightsout.solver;

import com.lightsout.model.Board;
import java.util.*;

/**
 * Base class for search-based solvers (DFS, BFS) with common pruning strategies.
 */
public abstract class SearchBasedSolver implements Solver {
    protected final SolverConfig config;
    protected final MoveHistory moveHistory;
    protected final Set<String> visitedStates;
    protected long startTime;

    public SearchBasedSolver(SolverConfig config) {
        this.config = config;
        this.moveHistory = new MoveHistory(config.getMaxAttempts());
        this.visitedStates = new HashSet<>();
    }

    @Override
    public List<Move> solve(Board board) {
        startTime = System.currentTimeMillis();
        visitedStates.clear();
        moveHistory.clear();
        return searchForSolution(board);
    }

    protected abstract List<Move> searchForSolution(Board board);

    protected boolean shouldTerminate() {
        Map<String, Object> stats = moveHistory.getStatistics();
        int totalAttempts = stats.isEmpty() ? 0 : (int) stats.get("totalAttempts");
        return System.currentTimeMillis() - startTime > config.getTimeoutMillis() ||
               totalAttempts >= config.getMaxAttempts();
    }

    /**
     * Gets valid next moves from the current position.
     * Applies pruning strategies to reduce search space.
     */
    protected List<Move> getNextMoves(Board board, List<Move> currentMoves) {
        List<Move> validMoves = new ArrayList<>();
        int size = board.getSize();

        // Strategy 1: If this is the first move, only consider the top-left quadrant
        if (currentMoves.isEmpty()) {
            int maxRow = (size + 1) / 2;
            int maxCol = (size + 1) / 2;
            for (int row = 0; row < maxRow; row++) {
                for (int col = 0; col < maxCol; col++) {
                    validMoves.add(new Move(row, col));
                }
            }
            return validMoves;
        }

        // Strategy 2: For subsequent moves, consider all positions
        Move lastMove = currentMoves.get(currentMoves.size() - 1);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Skip moves that would undo the previous move
                if (isUndoMove(row, col, lastMove)) {
                    continue;
                }

                // Skip moves that would create a previously seen board state
                Board tempBoard = board.copy();
                tempBoard.toggleLight(row, col);
                String state = getBoardState(tempBoard);
                if (visitedStates.contains(state)) {
                    continue;
                }

                validMoves.add(new Move(row, col));
            }
        }

        return validMoves;
    }

    /**
     * Checks if a move would effectively undo the previous move.
     */
    private boolean isUndoMove(int row, int col, Move lastMove) {
        return Math.abs(row - lastMove.row) <= 1 && Math.abs(col - lastMove.col) <= 1;
    }

    protected String getBoardState(Board board) {
        StringBuilder state = new StringBuilder();
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                state.append(board.getCell(i, j) ? '1' : '0');
            }
        }
        return state.toString();
    }

    protected void recordState(Board board, List<Move> moves) {
        visitedStates.add(getBoardState(board));
        moveHistory.recordAttempt(moves, countLightsOn(board));
    }

    protected int countLightsOn(Board board) {
        int count = 0;
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getCell(i, j)) {
                    count++;
                }
            }
        }
        return count;
    }
} 