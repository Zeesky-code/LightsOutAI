package com.lightsout.solver;

import com.lightsout.model.Board;
import java.util.*;

public class GaussianSolver implements Solver {
    
    @Override
    public List<Move> solve(Board board) {
        int size = board.getSize();
        int n = size * size;
        boolean[][] matrix = new boolean[n][n + 1];
        
        buildMatrix(matrix, board);
        
        // Solve using Gaussian elimination
        boolean hasSolution = gaussianElimination(matrix);
        if (!hasSolution) {
            return null;
        }
        
        // Extract solution
        return extractSolution(matrix, size);
    }
    
    private void buildMatrix(boolean[][] matrix, Board board) {
        int size = board.getSize();
        int n = size * size;
        
        // Fill coefficient matrix (effect of each button press)
        for (int i = 0; i < n; i++) {
            int row = i / size;
            int col = i % size;
            
            // Set coefficients for current button and its neighbors
            setCoefficient(matrix, i, row, col, size);
            if (row > 0) setCoefficient(matrix, i, row - 1, col, size);
            if (row < size - 1) setCoefficient(matrix, i, row + 1, col, size);
            if (col > 0) setCoefficient(matrix, i, row, col - 1, size);
            if (col < size - 1) setCoefficient(matrix, i, row, col + 1, size);
        }
        
        for (int i = 0; i < n; i++) {
            matrix[i][n] = board.getCell(i / size, i % size);
        }
    }
    
    private void setCoefficient(boolean[][] matrix, int equation, int row, int col, int size) {
        matrix[equation][row * size + col] = true;
    }
    
    private boolean gaussianElimination(boolean[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int lead = 0;
        
        for (int r = 0; r < rows; r++) {
            if (lead >= cols - 1) {
                break;
            }
            
            // Find pivot
            int i = r;
            while (!matrix[i][lead]) {
                i++;
                if (i == rows) {
                    i = r;
                    lead++;
                    if (lead == cols - 1) {
                        break;
                    }
                }
            }
            
            // Swap rows if needed
            if (i != r) {
                boolean[] temp = matrix[r];
                matrix[r] = matrix[i];
                matrix[i] = temp;
            }
            
            // Eliminate column
            for (i = 0; i < rows; i++) {
                if (i != r && matrix[i][lead]) {
                    for (int j = lead; j < cols; j++) {
                        matrix[i][j] = matrix[i][j] ^ matrix[r][j];
                    }
                }
            }
            lead++;
        }
        
        // Check if solution exists
        for (int r = lead; r < rows; r++) {
            if (matrix[r][cols - 1]) {
                return false; 
            }
        }
        
        return true;
    }
    
    private List<Move> extractSolution(boolean[][] matrix, int size) {
        List<Move> solution = new ArrayList<>();
        int n = size * size;
        
        // Back substitution
        for (int i = 0; i < n; i++) {
            if (matrix[i][n]) {
                solution.add(new Move(i / size, i % size));
            }
        }
        
        return solution;
    }
} 