package com.example.page_replacement.algorithms.implementation;

import com.example.page_replacement.algorithms.AlgorithmsBase;

public class Optimal extends AlgorithmsBase {
    public Optimal(int lengthFrames) {
        super(lengthFrames);
    }
    public Optimal(int lengthFrames, int[] frames){
        super(lengthFrames, frames);
    }

    @Override
    protected int selectVictim(int i) {
        int victim = -1;
        int farther = -1;

       for (int idxP = 0; idxP < this.frames.length; idxP++){
           int nextUsage = Integer.MAX_VALUE;

           for (int j = i + 1; j < this.chain.length; j++){
               if(this.chain[j] == this.frames[idxP]){
                   nextUsage = j;
                   break;
               }
           }

           if(nextUsage > farther){
               farther = nextUsage;
               victim =  idxP;
           }
       }

       return victim;
    }
}
