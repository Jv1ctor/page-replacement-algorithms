package com.example.page_replacement.algorithms.implementation;

import com.example.page_replacement.algorithms.AlgorithmsBase;

public class FIFO extends AlgorithmsBase {

    int oldest = 0; // o mais velho é sempre o primeiro

    public FIFO(int lengthFrames) {
        super(lengthFrames);
    }

    @Override
    protected int selectVictiam(int i) {
        
        int victim = oldest;
        System.out.println("[FIFO] out: "+frames[victim]);
        oldest = (oldest + 1) % lengthFrames; // o mais velho é sempre o próximo (após a substituição)
        return victim;
    }

}
