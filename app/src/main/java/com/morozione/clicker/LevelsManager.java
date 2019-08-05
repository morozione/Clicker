package com.morozione.clicker;

/**
 * Created by morozione on 10/13/17.
 */

public class LevelsManager {
    private int level = 1;
    private int startRecord = 35;

    public void upperLevel() {
        level++;
        startRecord += level*20;
    }

    public int getStartRecord() {
        return startRecord;
    }

    public int getLevel() {
        return level;
    }
}
