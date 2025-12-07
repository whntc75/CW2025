package com.comp2042;

public enum AIDifficulty {
    EASY(0.3),
    MEDIUM(0.6),
    HARD(0.95);

    private final double accuracy;

    AIDifficulty(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAccuracy() {
        return accuracy;
    }
}
