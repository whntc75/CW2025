package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty lines = new SimpleIntegerProperty(0);//record the total number of lines cleared

    public IntegerProperty scoreProperty() {
        return score;
    }

      public IntegerProperty linesProperty() {
        return lines;
    }

    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    public void addLines(int n) {lines.setValue(lines.getValue() + n);}//Accumulate the number of lines cleared


    public void reset() {
        score.setValue(0);
        lines.setValue(0);//Reset the line count to zero when starting a new game
    }
}
