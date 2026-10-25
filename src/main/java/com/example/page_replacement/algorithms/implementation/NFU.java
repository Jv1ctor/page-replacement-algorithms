package com.example.page_replacement.algorithms.implementation;

import com.example.page_replacement.algorithms.AlgorithmsBase;

public class NFU extends AlgorithmsBase {

    private final int[] counts;
    private final int[] loadedAt;
    private int clock = 0;

    public NFU(int lengthFrames) {
        super(lengthFrames);
        this.counts = new int[lengthFrames];
        this.loadedAt = new int[lengthFrames];
    }

    @Override
    protected int selectVictiam(int i) {
        int victim = 0;
        for (int k = 1; k < lengthFrames; k++) {
            if (counts[k] < counts[victim] || (counts[k] == counts[victim] && loadedAt[k] < loadedAt[victim])) {
                victim = k;
            }
        }
        return victim;
    }

    @Override
    protected void onLoad(int page, int pos) {
        counts[pos] = 1;
        loadedAt[pos] = clock++;
    }

    @Override
    protected void onUse(int page, int pos) {
        counts[pos]++;
    }

    @Override
    protected void onSwap(int victimPos, int oldPage, int newPage) {
        counts[victimPos] = 1;
        loadedAt[victimPos] = clock++;
    }
}
