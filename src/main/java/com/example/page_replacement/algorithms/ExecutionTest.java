package com.example.page_replacement.algorithms;

import com.example.page_replacement.algorithms.implementation.FIFO;
import com.example.page_replacement.algorithms.implementation.NFU;
import com.example.page_replacement.algorithms.implementation.Optimal;

public class ExecutionTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        // ===== Teste 1: sequencia de Belady, 3 quadros =====
        int[] chain = {1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5};

        check("Teste 1 FIFO", new FIFO(3), chain, 9);
        check("Teste 1 Optimal", new Optimal(3), chain, 7);
        check("Teste 1 NFU", new NFU(3), chain, 10);


        // ===== Teste 2: sequencia de Belady, 4 quadros (anomalia: FIFO piora) =====
        check("Teste 2 FIFO", new FIFO(4), chain, 10);
        check("Teste 2 Optimal", new Optimal(4), chain, 6);
        check("Teste 2 NFU", new NFU(4), chain, 8);


        // ===== Teste 3: sequencia classica, 3 quadros =====
        int[] chain3 = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1};

        check("Teste 3 FIFO", new FIFO(3), chain3, 15);
        check("Teste 3 Optimal", new Optimal(3), chain3, 9);
        check("Teste 3 NFU", new NFU(3), chain3, 13);


        // ===== Teste 4: pagina popular (NFU deve proteger a pagina 1) =====
        int[] chain4 = {1, 1, 1, 1, 2, 3, 4, 1, 5, 1, 6, 1};

        check("Teste 4 FIFO", new FIFO(3), chain4, 7);
        check("Teste 4 Optimal", new Optimal(3), chain4, 6);
        check("Teste 4 NFU", new NFU(3), chain4, 6);


        // ===== Teste 5: laco maior que a memoria (pior caso do FIFO) =====
        int[] chain5 = {1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4};

        check("Teste 5 FIFO", new FIFO(3), chain5, 12);
        check("Teste 5 Optimal", new Optimal(3), chain5, 6);
        check("Teste 5 NFU", new NFU(3), chain5, 12);


        // ===== Teste 6: tudo cabe na memoria (so faltas compulsorias) =====
        int[] chain6 = {1, 2, 3, 1, 2, 3, 1, 2, 3};

        check("Teste 6 FIFO", new FIFO(3), chain6, 3);
        check("Teste 6 Optimal", new Optimal(3), chain6, 3);
        check("Teste 6 NFU", new NFU(3), chain6, 3);


        // ===== Teste 7: apenas 1 quadro (caso de borda) =====
        int[] chain7 = {1, 2, 1, 2, 1};

        check("Teste 7 FIFO", new FIFO(1), chain7, 5);
        check("Teste 7 Optimal", new Optimal(1), chain7, 5);
        check("Teste 7 NFU", new NFU(1), chain7, 5);


        // ========== Testes com memoria pre-carregada (frames iniciais) ==========

        // ===== Teste 8: memoria ja contem todas as paginas usadas (nenhuma falta) =====
        int[] initial8 = {1, 2, 3};

        check("Teste 8 FIFO", new FIFO(3, initial8), chain6, 0);
        check("Teste 8 Optimal", new Optimal(3, initial8), chain6, 0);
        check("Teste 8 NFU", new NFU(3, initial8), chain6, 0);


        // ===== Teste 9: sequencia de Belady com {1, 2, 3} ja na memoria =====
        // Mesmo comportamento do teste 1, mas sem as 3 faltas compulsorias.
        int[] initial9 = {1, 2, 3};

        check("Teste 9 FIFO", new FIFO(3, initial9), chain, 6);
        check("Teste 9 Optimal", new Optimal(3, initial9), chain, 4);
        check("Teste 9 NFU", new NFU(3, initial9), chain, 7);


        // ===== Teste 10: memoria parcialmente cheia ({7, 0} em 3 quadros) =====
        // O quadro que sobra deve ficar livre (-1). Depois de ler 7, 0, 1 o estado
        // fica igual ao do teste 3, entao sao 2 faltas a menos.
        int[] initial10 = {7, 0};

        check("Teste 10 FIFO", new FIFO(3, initial10), chain3, 13);
        check("Teste 10 Optimal", new Optimal(3, initial10), chain3, 7);
        check("Teste 10 NFU", new NFU(3, initial10), chain3, 11);


        // ===== Teste 11: memoria cheia de paginas que nunca sao usadas =====
        // Todas as paginas iniciais sao trocadas; mesmo resultado do teste 6.
        int[] initial11 = {9, 8, 7};

        check("Teste 11 FIFO", new FIFO(3, initial11), chain6, 3);
        check("Teste 11 Optimal", new Optimal(3, initial11), chain6, 3);
        check("Teste 11 NFU", new NFU(3, initial11), chain6, 3);


        // ===== Teste 12: quadros nao informados devem comecar livres =====
        // {1} em 3 quadros -> [1, -1, -1]: 2 e 3 entram nos quadros livres, sem trocas.
        int[] initial12 = {1};
        int[] chain12 = {2, 3, 1, 2, 3};

        check("Teste 12 FIFO", new FIFO(3, initial12), chain12, 2);
        check("Teste 12 Optimal", new Optimal(3, initial12), chain12, 2);
        check("Teste 12 NFU", new NFU(3, initial12), chain12, 2);


        System.out.println();
        System.out.println("[result] " + passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void check(String name, AlgorithmsBase algorithm, int[] chain, int expected) {
        algorithm.execute(chain);
        int faults = algorithm.getFaults();

        if (faults == expected) {
            passed++;
            System.out.println("[PASS] " + name + ": " + faults + " faults");
        } else {
            failed++;
            System.out.println("[FAIL] " + name + ": " + faults + " faults (expected " + expected + ")");
        }
    }
}
