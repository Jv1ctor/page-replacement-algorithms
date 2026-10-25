package com.example.page_replacement.algorithms.implementation;

import com.example.page_replacement.algorithms.AlgorithmsBase;

public class LRU extends AlgorithmsBase {
    private final int[] lastUsed;
    private int clock = 0;

    public LRU(int lengthFrames) {
        super(lengthFrames);
        this.lastUsed = new int[lengthFrames];
    }

    public LRU(int lengthFrames, int[] frames) {
        super(lengthFrames, frames);
        this.lastUsed = new int[lengthFrames];

        for (int k = 0; k < lengthFrames; k++) {
            if (this.frames[k] != -1) {
                lastUsed[k] = clock++;
            }
        }
    }
    @Override
    protected void onUse(int page, int pos) {
        lastUsed[pos] = clock++;
    }

    @Override
    protected void onLoad(int page, int pos) {
        lastUsed[pos] = clock++;
    }

    @Override
    protected void onSwap(int victimPos, int oldPage, int newPage) {
        lastUsed[victimPos] = clock++;
    }

    @Override
    protected int selectVictim(int i) {
        int victim = 0;
        for (int k = 1; k < lengthFrames; k++) {
            if (lastUsed[k] < lastUsed[victim]) {
                victim = k;
            }
        }
        return victim;
    }
}