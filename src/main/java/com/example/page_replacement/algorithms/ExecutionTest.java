package com.example.page_replacement.algorithms;

import com.example.page_replacement.algorithms.implementation.FIFO;

public class ExecutionTest {
    public static void main(String[] args) {

        int[] chain = {1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5};

        FIFO fifo = new FIFO(3);
        fifo.execute(chain);
        System.out.println("[log] total faults: "+fifo.getFaults()); // valor esperado: 9

    }
}
