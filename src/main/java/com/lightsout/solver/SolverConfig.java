package com.lightsout.solver;

public class SolverConfig {
    // Default values
    public static final int DEFAULT_MAX_ATTEMPTS = 1000;
    public static final int DEFAULT_MAX_MOVES = 25;
    public static final long DEFAULT_TIMEOUT_MILLIS = 30000; // 30 seconds
    public static final boolean DEFAULT_ALLOW_REPEAT_MOVES = true;

    private final int maxAttempts;
    private final int maxMovesPerAttempt;
    private final long timeoutMillis;
    private final boolean allowRepeatMoves;

    private SolverConfig(Builder builder) {
        this.maxAttempts = builder.maxAttempts;
        this.maxMovesPerAttempt = builder.maxMovesPerAttempt;
        this.timeoutMillis = builder.timeoutMillis;
        this.allowRepeatMoves = builder.allowRepeatMoves;
        validate();
    }

    private void validate() {
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("maxAttempts must be positive");
        }
        if (maxMovesPerAttempt <= 0) {
            throw new IllegalArgumentException("maxMovesPerAttempt must be positive");
        }
        if (timeoutMillis <= 0) {
            throw new IllegalArgumentException("timeoutMillis must be positive");
        }
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getMaxMovesPerAttempt() {
        return maxMovesPerAttempt;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public boolean isAllowRepeatMoves() {
        return allowRepeatMoves;
    }

    public static SolverConfig getDefault() {
        return new Builder().build();
    }


    public static class Builder {
        private int maxAttempts = DEFAULT_MAX_ATTEMPTS;
        private int maxMovesPerAttempt = DEFAULT_MAX_MOVES;
        private long timeoutMillis = DEFAULT_TIMEOUT_MILLIS;
        private boolean allowRepeatMoves = DEFAULT_ALLOW_REPEAT_MOVES;

        public Builder maxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }

        public Builder maxMovesPerAttempt(int maxMovesPerAttempt) {
            this.maxMovesPerAttempt = maxMovesPerAttempt;
            return this;
        }

        public Builder timeoutMillis(long timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
            return this;
        }

        public Builder allowRepeatMoves(boolean allowRepeatMoves) {
            this.allowRepeatMoves = allowRepeatMoves;
            return this;
        }

        public SolverConfig build() {
            return new SolverConfig(this);
        }
    }

    @Override
    public String toString() {
        return "SolverConfig{" +
                "maxAttempts=" + maxAttempts +
                ", maxMovesPerAttempt=" + maxMovesPerAttempt +
                ", timeoutMillis=" + timeoutMillis +
                ", allowRepeatMoves=" + allowRepeatMoves +
                '}';
    }
} 