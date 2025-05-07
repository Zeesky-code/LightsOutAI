package com.lightsout.solver;

import com.lightsout.model.Board;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A solver that uses random search to find a solution.
 * Uses MoveHistory to track attempts and avoid repeating unsuccessful sequences.
 */
public class RandomSearchSolver implements Solver {
    private final Random random = new Random();
    private final SolverConfig config;
    private final MoveHistory moveHistory;

    public RandomSearchSolver() {
        this(SolverConfig.getDefault());
    }

    public RandomSearchSolver(SolverConfig config) {
        this.config = config;
        this.moveHistory = new MoveHistory(config.getMaxAttempts());
    }

    @Override
    public List<Move> solve(Board board) {
        long startTime = System.currentTimeMillis();
        Board workingBoard = board.copy();
        List<Move> currentMoves = new ArrayList<>();

        for (int attempt = 0; attempt < config.getMaxAttempts(); attempt++) {
            if (System.currentTimeMillis() - startTime > config.getTimeoutMillis()) {
                break;
            }

            workingBoard = board.copy();
            currentMoves.clear();

            // Try to find a solution within the move limit
            for (int move = 0; move < config.getMaxMovesPerAttempt(); move++) {
                int row = random.nextInt(board.getSize());
                int col = random.nextInt(board.getSize());
                Move newMove = new Move(row, col);

                currentMoves.add(newMove);
                if (!config.isAllowRepeatMoves() && moveHistory.hasTriedSequence(currentMoves)) {
                    currentMoves.remove(currentMoves.size() - 1);
                    continue;
                }

                workingBoard.toggleLight(row, col);
                
                int remainingLights = countLightsOn(workingBoard);
                if (remainingLights == 0) {
                    moveHistory.recordAttempt(currentMoves, 0);
                    return currentMoves; // Return only complete solutions
                }

                // Record attempt for tracking purposes
                moveHistory.recordAttempt(new ArrayList<>(currentMoves), remainingLights);
            }
        }

        return null; // No solution found within attempt limit
    }

    private int countLightsOn(Board board) {
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