package com.lightsout.solver;

import com.lightsout.model.Board;
import java.util.List;

/**
 * Interface for implementing different Lights Out puzzle solving strategies.
 */
public interface Solver {
    /**
     * Solves the given Lights Out puzzle board.
     * @param board The board to solve
     * @return List of moves (row, col pairs) that solve the puzzle, or empty list if no solution exists
     */
    List<Move> solve(Board board);

    /**
     * Represents a move in the solution sequence
     */
    class Move {
        public final int row;
        public final int col;

        public Move(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return String.format("Move(%d, %d)", row, col);
        }
    }
} 