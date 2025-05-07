package com.lightsout.solver;

import com.lightsout.model.Board;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple solver that uses random search to find a solution.
 * This is not optimal but serves as a good starting point for the project.
 */
public class RandomSearchSolver implements Solver {
    private static final int MAX_ATTEMPTS = 1000;
    private static final int MAX_MOVES = 25;
    private final Random random = new Random();

    @Override
    public List<Move> solve(Board board) {
        Board workingBoard = board.copy();
        List<Move> bestSolution = new ArrayList<>();
        int bestLightsOn = countLightsOn(workingBoard);

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            Board tempBoard = board.copy();
            List<Move> moves = new ArrayList<>();
            
            for (int move = 0; move < MAX_MOVES; move++) {
                int row = random.nextInt(board.getSize());
                int col = random.nextInt(board.getSize());
                
                tempBoard.toggleLight(row, col);
                moves.add(new Move(row, col));
                
                int currentLightsOn = countLightsOn(tempBoard);
                
                if (currentLightsOn == 0) {
                    return moves;
                }
                
                // Keep track of the best partial solution
                if (currentLightsOn < bestLightsOn) {
                    bestLightsOn = currentLightsOn;
                    bestSolution = new ArrayList<>(moves);
                }
            }
        }

        // Return the best partial solution if no complete solution was found
        return bestSolution;
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