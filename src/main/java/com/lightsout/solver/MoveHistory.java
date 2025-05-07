package com.lightsout.solver;

import java.util.*;

public class MoveHistory {
    private static class Attempt implements Comparable<Attempt> {
        final List<Solver.Move> moves;
        final int remainingLights;
        final long timestamp;
        final double score;  

        Attempt(List<Solver.Move> moves, int remainingLights) {
            this.moves = new ArrayList<>(moves);
            this.remainingLights = remainingLights;
            this.timestamp = System.currentTimeMillis();
            
            // Score calculation: weighted sum of remaining lights and move count

            this.score = (remainingLights * 5.0) + (moves.size() * 0.5);
        }

        @Override
        public int compareTo(Attempt other) {
            return Double.compare(this.score, other.score);
        }

        @Override
        public String toString() {
            return String.format("Attempt(lights=%d, moves=%d, score=%.2f)", 
                remainingLights, moves.size(), score);
        }
    }

    private final List<Attempt> attempts;
    private final Set<String> triedSequences;
    private Attempt bestAttempt;
    private final int maxHistorySize;

    public MoveHistory(int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
        this.attempts = new ArrayList<>();
        this.triedSequences = new HashSet<>();
        this.bestAttempt = null;
    }

    /**
     * Records a sequence of moves and their result.
     * @param moves The sequence of moves
     * @param remainingLights Number of lights still on after the sequence
     * @return true if this attempt is the new best attempt
     */
    public boolean recordAttempt(List<Solver.Move> moves, int remainingLights) {
        Attempt attempt = new Attempt(moves, remainingLights);
        boolean isNewBest = false;
        
        // Update best attempt if this is better (lower score is better)
        if (bestAttempt == null || attempt.score < bestAttempt.score) {
            bestAttempt = attempt;
            isNewBest = true;
        }

        attempts.add(attempt);
        triedSequences.add(generateSequenceKey(moves));


        if (attempts.size() > maxHistorySize) {
            Attempt removed = attempts.remove(0);
            triedSequences.remove(generateSequenceKey(removed.moves));
        }

        return isNewBest;
    }

    /**
     * Checks if a sequence of moves has been tried before.
     * @param moves The sequence to check
     * @return true if this exact sequence has been tried
     */
    public boolean hasTriedSequence(List<Solver.Move> moves) {
        return triedSequences.contains(generateSequenceKey(moves));
    }

    /**
     * Gets the best sequence of moves found so far.
     * @return The moves from the best attempt, or empty list if no attempts made
     */
    public List<Solver.Move> getBestMoves() {
        return bestAttempt != null ? new ArrayList<>(bestAttempt.moves) : new ArrayList<>();
    }

    /**
     * Gets the number of lights remaining in the best attempt.
     * @return The number of lights, or Integer.MAX_VALUE if no attempts made
     */
    public int getBestRemainingLights() {
        return bestAttempt != null ? bestAttempt.remainingLights : Integer.MAX_VALUE;
    }

    /**
     * Gets the score of the best attempt so far.
     * @return The score, or Double.MAX_VALUE if no attempts made
     */
    public double getBestScore() {
        return bestAttempt != null ? bestAttempt.score : Double.MAX_VALUE;
    }

    /**
     * Gets statistics about the move sequences tried.
     * @return Map containing statistics about the attempts
     */
    public Map<String, Object> getStatistics() {
        if (attempts.isEmpty()) {
            return Collections.emptyMap();
        }

        // Sort attempts by score for percentile calculations
        List<Attempt> sortedAttempts = new ArrayList<>(attempts);
        Collections.sort(sortedAttempts);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAttempts", attempts.size());
        stats.put("bestScore", getBestScore());
        stats.put("bestRemainingLights", getBestRemainingLights());
        stats.put("bestMoveCount", bestAttempt.moves.size());
        
        // Calculate averages
        double avgScore = attempts.stream()
            .mapToDouble(a -> a.score)
            .average()
            .orElse(0.0);
        stats.put("averageScore", avgScore);
        
        // Calculate median score (50th percentile)
        double medianScore = sortedAttempts.get(sortedAttempts.size() / 2).score;
        stats.put("medianScore", medianScore);
        
        // Calculate 90th percentile score
        int index90 = (int)(sortedAttempts.size() * 0.9);
        double percentile90Score = sortedAttempts.get(index90).score;
        stats.put("percentile90Score", percentile90Score);
        
        return stats;
    }

    /**
     * Generates a unique key for a sequence of moves.
     */
    private String generateSequenceKey(List<Solver.Move> moves) {
        StringBuilder key = new StringBuilder();
        for (Solver.Move move : moves) {
            key.append(move.row).append(',').append(move.col).append(';');
        }
        return key.toString();
    }

    public void clear() {
        attempts.clear();
        triedSequences.clear();
        bestAttempt = null;
    }
}
