package com.lightsout.model;

/**
 * Represents the Lights Out game board.
 * In this game, clicking any cell toggles its state and the state of adjacent cells.
 * The goal is to turn off all lights.
 */
public class Board {
    private boolean[][] grid;
    private final int size;

    /**
     * Creates a new board with the specified size.
     * @param size The size of the board (size x size)
     */
    public Board(int size) {
        this.size = size;
        this.grid = new boolean[size][size];
    }

    /**
     * Creates a new board with the specified size and initial state.
     * @param size The size of the board
     * @param initialState The initial state of the board
     */
    public Board(int size, boolean[][] initialState) {
        this.size = size;
        this.grid = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(initialState[i], 0, this.grid[i], 0, size);
        }
    }

    /**
     * Toggles a cell and its adjacent cells.
     * @param row Row index
     * @param col Column index
     */
    public void toggleLight(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Invalid position");
        }

        // Toggle the clicked cell
        toggle(row, col);

        // Toggle adjacent cells
        if (row > 0) toggle(row - 1, col);        // Up
        if (row < size - 1) toggle(row + 1, col); // Down
        if (col > 0) toggle(row, col - 1);        // Left
        if (col < size - 1) toggle(row, col + 1); // Right
    }

    private void toggle(int row, int col) {
        grid[row][col] = !grid[row][col];
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    /**
     * Checks if the puzzle is solved (all lights are off).
     * @return true if all lights are off, false otherwise
     */
    public boolean isSolved() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the state of a specific cell.
     * @param row Row index
     * @param col Column index
     * @return true if the light is on, false otherwise
     */
    public boolean getCell(int row, int col) {
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Invalid position");
        }
        return grid[row][col];
    }

    /**
     * Gets the size of the board.
     * @return The size of the board
     */
    public int getSize() {
        return size;
    }

    /**
     * Creates a copy of the current board state.
     * @return A new Board instance with the same state
     */
    public Board copy() {
        return new Board(size, grid);
    }

    /**
     * Randomizes the board state with the given number of random moves.
     * @param moves Number of random moves to make
     */
    public void randomize(int moves) {
        for (int i = 0; i < moves; i++) {
            int row = (int) (Math.random() * size);
            int col = (int) (Math.random() * size);
            toggleLight(row, col);
        }
    }
} 