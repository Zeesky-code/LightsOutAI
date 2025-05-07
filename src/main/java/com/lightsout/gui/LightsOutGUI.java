package com.lightsout.gui;

import com.lightsout.model.Board;
import com.lightsout.solver.RandomSearchSolver;
import com.lightsout.solver.DFSSolver;
import com.lightsout.solver.BFSSolver;
import com.lightsout.solver.Solver;
import com.lightsout.solver.Solver.Move;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class LightsOutGUI extends JFrame {
    private final Board board;
    private final JButton[][] buttons;
    private final int size;
    private static final Color LIGHT_ON = new Color(255, 255, 0);  // Bright yellow
    private static final Color LIGHT_OFF = new Color(64, 64, 64);  // Darker gray
    private static final Color BUTTON_HOVER = new Color(200, 200, 0);  // Darker yellow for hover
    private Solver solver;
    private Timer solutionTimer;
    private boolean isSolving = false;

    public LightsOutGUI(int size) {
        this.size = size;
        this.board = new Board(size);
        this.buttons = new JButton[size][size];
        this.solver = new RandomSearchSolver();

        setTitle("Lights Out");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupUI();
        pack();
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(32, 32, 32)); // Dark background
    }

    private void setupUI() {
        JPanel gamePanel = new JPanel(new GridLayout(size, size, 5, 5));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gamePanel.setBackground(new Color(32, 32, 32));

        // Create the grid of buttons
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                JButton button = createButton(row, col);
                buttons[row][col] = button;
                gamePanel.add(button);
            }
        }

        // Control panel with better organization
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controlPanel.setBackground(new Color(48, 48, 48));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Solver selection
        JComboBox<String> solverSelect = new JComboBox<>(new String[]{
            "Random Search", "Depth-First Search", "Breadth-First Search"
        });
        solverSelect.setBackground(new Color(120, 120, 120));
        solverSelect.setForeground(Color.WHITE);
        ((JTextField)solverSelect.getEditor().getEditorComponent()).setBackground(new Color(120, 120, 120));
        solverSelect.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground(new Color(160, 160, 160));
                } else {
                    c.setBackground(new Color(120, 120, 120));
                }
                c.setForeground(Color.WHITE);
                return c;
            }
        });
        solverSelect.addActionListener(e -> {
            String selected = (String) solverSelect.getSelectedItem();
            switch (selected) {
                case "Random Search":
                    solver = new RandomSearchSolver();
                    break;
                case "Depth-First Search":
                    solver = new DFSSolver();
                    break;
                case "Breadth-First Search":
                    solver = new BFSSolver();
                    break;
            }
            solverSelect.setSelectedItem(selected);
        });
        
        JButton newGameButton = new JButton("New Game");
        JButton solveButton = new JButton("Solve");
        JButton resetButton = new JButton("Reset");
        
        // Style the control buttons
        styleControlButton(newGameButton);
        styleControlButton(solveButton);
        styleControlButton(resetButton);
        
        newGameButton.addActionListener(e -> newGame());
        solveButton.addActionListener(e -> solve());
        resetButton.addActionListener(e -> stopSolving());

        // Add components to control panel with labels
        JPanel solverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        solverPanel.setBackground(controlPanel.getBackground());
        JLabel solverLabel = new JLabel("Solver: ");
        solverLabel.setForeground(Color.WHITE);
        solverPanel.add(solverLabel);
        solverPanel.add(solverSelect);
        
        controlPanel.add(solverPanel);
        controlPanel.add(newGameButton);
        controlPanel.add(solveButton);
        controlPanel.add(resetButton);

        // Main layout
        setLayout(new BorderLayout(10, 10));
        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Initialize a random puzzle
        newGame();
    }

    private void styleControlButton(JButton button) {
        button.setBackground(new Color(80, 80, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    private JButton createButton(int row, int col) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(60, 60));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (board.getCell(row, col)) {
                    button.setBackground(BUTTON_HOVER);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButtonState(button, board.getCell(row, col));
            }
        });
        
        ActionListener actionListener = e -> {
            if (!isSolving) {
                board.toggleLight(row, col);
                updateButtons();
                
                if (board.isSolved()) {
                    JOptionPane.showMessageDialog(this, "Congratulations! You solved the puzzle!");
                }
            }
        };
        
        button.addActionListener(actionListener);
        return button;
    }

    private void updateButtonState(JButton button, boolean isLit) {
        button.setBackground(isLit ? LIGHT_ON : LIGHT_OFF);
        
        // Custom painting for the button
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = c.getWidth();
                int h = c.getHeight();
                
                // Draw the base color
                g2d.setColor(button.getBackground());
                g2d.fillRoundRect(0, 0, w - 1, h - 1, 15, 15);
                
                // Add gradient overlay
                GradientPaint gp = new GradientPaint(
                    0, 0, 
                    new Color(255, 255, 255, 30), 
                    0, h, 
                    new Color(0, 0, 0, 30)
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w - 1, h - 1, 15, 15);
                
                g2d.dispose();
            }
        });
    }

    private void updateButtons() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                updateButtonState(buttons[row][col], board.getCell(row, col));
            }
        }
    }

    private void newGame() {
        stopSolving();
        board.randomize(10);
        updateButtons();
    }

    private void stopSolving() {
        if (solutionTimer != null && solutionTimer.isRunning()) {
            solutionTimer.stop();
        }
        isSolving = false;
    }

    private void solve() {
        if (isSolving) {
            return;
        }

        List<Move> solution = solver.solve(board);
        
        if (solution == null) {
            JOptionPane.showMessageDialog(this, 
                "No solution found! Try running the solver again or use a different solving method.");
            return;
        }

        isSolving = true;
        final int[] moveIndex = {0};

        solutionTimer = new Timer(500, e -> {
            if (moveIndex[0] < solution.size()) {
                Move move = solution.get(moveIndex[0]);
                board.toggleLight(move.row, move.col);
                updateButtons();
                moveIndex[0]++;

                if (moveIndex[0] == solution.size()) {
                    ((Timer)e.getSource()).stop();
                    isSolving = false;
                    if (board.isSolved()) {
                        JOptionPane.showMessageDialog(LightsOutGUI.this, 
                            "Puzzle solved in " + solution.size() + " moves!");
                    } else {
                        JOptionPane.showMessageDialog(LightsOutGUI.this,
                            "Solution did not solve the puzzle. Try running the solver again!");
                    }
                }
            }
        });

        solutionTimer.start();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LightsOutGUI gui = new LightsOutGUI(3);
            gui.setVisible(true);
        });
    }
} 