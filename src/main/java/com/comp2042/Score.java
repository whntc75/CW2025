package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty lines = new SimpleIntegerProperty(0);//record the total number of lines cleared
    private final IntegerProperty level = new SimpleIntegerProperty(1);//The level starts at 1

    public IntegerProperty scoreProperty() {
        return score;
    }

      public IntegerProperty linesProperty() {
        return lines;
    }

     public IntegerProperty levelProperty() { return level; }//Return the int value of the current level

    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    public void addLines(int n) {
    lines.setValue(lines.getValue() + n);//Accumulate the number of lines cleared
    int totalLines = lines.get();      //The total number of lines cleared
    int newLevel = totalLines / 10 + 1; //Level up every time 10 lines are cleared
    level.set(newLevel);}


    public void reset() {
        score.setValue(0);
        lines.setValue(0);//Reset the line count to zero when starting a new game
        level.setValue(1);
    }
}
