package com.example.page_replacement.algorithms;

import java.util.Arrays;

public abstract class AlgorithmsBase {
    // for config logs
    private boolean logger = false;

    protected final int lengthFrames;
    protected final int[] frames;
    protected int faults = 0;
    protected int[] faultsHistory = new int[0];
    protected int[] chain = new int[0]; // only optimal algorithm


    public AlgorithmsBase(int lengthFrames){
        this.lengthFrames = lengthFrames;
        this.frames = new int[lengthFrames];
        Arrays.fill(this.frames, -1);
    }
    public AlgorithmsBase(int lengthFrames, int[] frames){
        this(lengthFrames); // quadros nao informados ficam livres (-1)
        System.arraycopy(frames, 0, this.frames, 0, Math.min(frames.length, lengthFrames));
    }

    public final void execute(int[] chain) {
        this.chain = chain; // only optimal algorithm
        this.faultsHistory = new int[chain.length];
        for (int i = 0; i < chain.length; i++) {
            this.loggerOut("[frames state] "+Arrays.toString(frames));
            int page = chain[i];
            this.loggerOut("[log] reading "+chain[i]);
            int pos = this.findPage(page);

            if (pos != -1) {
                this.loggerOut("[ok] hit!");
                this.faultsHistory[i] = this.faults;
                this.onUse(page, pos);
            } else {
                this.loggerOut("[error] miss!");
                this.faults++;
                this.faultsHistory[i] = this.faults;
                pos = freeFrame();
                if (pos != -1) {
                    this.loggerOut("[log] adding "+chain[i]);
                    this.frames[pos] = page;
                    this.onLoad(page, pos);
                } else {
                    this.loggerOut("[error] no space!");
                    int vPos = this.selectVictim(i);
                    int oldPage = this.frames[vPos];
                    this.frames[vPos] = page;
                    this.onSwap(vPos, oldPage, page);
                }
            }

        }
    }

    protected abstract int selectVictim(int p);
    protected void onUse(int page, int pos) {}
    protected void onLoad(int page, int pos) {}
    protected void onSwap(int victimPos, int oldPage, int newPage) {}


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
    public int[] getFaultsHistory() { return this.faultsHistory; }

    // for log config
    public void onLogger(){
        this.logger = true;
    }

    protected void loggerOut(String text){
        if(this.logger){
            System.out.println(text);
        }
    }
}
