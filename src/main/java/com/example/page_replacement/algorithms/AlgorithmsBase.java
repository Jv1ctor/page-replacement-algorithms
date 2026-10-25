package com.example.page_replacement.algorithms;

import java.util.Arrays;

public abstract class AlgorithmsBase {
    protected final int lengthFrames;
    protected final int[] frames;
    protected int faults = 0;

    public AlgorithmsBase(int lengthFrames){
        this.lengthFrames = lengthFrames;
        this.frames = new int[lengthFrames];
        Arrays.fill(this.frames, -1);
    }

    public final void execute(int[] chain) {
        for (int i = 0; i < chain.length; i++) {
            System.out.println("[frames state] "+Arrays.toString(frames));
            int page = chain[i];
            System.out.println("[log] reading "+chain[i]);
            int pos = this.findPage(page);

            if (pos != -1) {
                System.out.println("[ok] hit!");
                this.onUse(page, pos);
            } else {
                System.out.println("[error] miss!");
                this.faults++;
                pos = freeFrame();
                if (pos != -1) {
                    System.out.println("[log] adding "+chain[i]);
                    this.frames[pos] = page;
                    this.onLoad(page, pos);
                } else {
                    System.out.println("[error] no space!");
                    int vPos = this.selectVictiam(i);
                    int oldPage = this.frames[vPos];
                    this.frames[vPos] = page;
                    this.onSwap(vPos, oldPage, page);
                }
            }

        }
    }

    protected abstract int selectVictiam(int i);
    protected void onUse(int page, int pos) {}
    protected void onLoad(int page, int pos) {}
    protected void onSwap(int victiamPos, int oldPage, int newPage) {}


    protected int findPage(int page) {
        for (int i = 0; i < this.lengthFrames; i++) {
            if (frames[i] == page) return i;
        }

        return -1;
    }

    protected int freeFrame() {
        for (int i = 0; i < this.lengthFrames; i++){
            if (frames[i] == -1) return i;
        }
        return -1;
    }

    public int getFaults() { return faults; }
}
