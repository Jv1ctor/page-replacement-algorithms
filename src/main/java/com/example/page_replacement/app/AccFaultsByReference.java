package com.example.page_replacement.app;

import com.example.page_replacement.algorithms.AlgorithmsBase;
import com.example.page_replacement.algorithms.implementation.FIFO;
import com.example.page_replacement.algorithms.implementation.LRU;
import com.example.page_replacement.algorithms.implementation.NFU;
import com.example.page_replacement.algorithms.implementation.Optimal;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class AccFaultsByReference {

    public static void main(String[] args) {
        int[] chain = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
        int lengthFrame = 3;

        AlgorithmsBase[] algs = {
                new FIFO(lengthFrame),
                new Optimal(lengthFrame),
                new LRU(lengthFrame),
                new NFU(lengthFrame)
        };

        ArrayList<String[]> data = new ArrayList<>();

        String[] header = new String[2 + algs.length];
        header[0] = "passo";
        header[1] = "referencia";
        for (int j = 0; j < algs.length; j++) {
            header[2 + j] = algs[j].getClass().getSimpleName();
        }
        data.add(header);

        int[][] histories = new int[algs.length][];
        for (int j = 0; j < algs.length; j++) {
            algs[j].execute(chain);
            histories[j] = algs[j].getFaultsHistory();
        }

        for (int i = 0; i < chain.length; i++) {
            String[] line = new String[2 + algs.length];
            line[0] = String.valueOf(i + 1);
            line[1] = String.valueOf(chain[i]);
            for (int j = 0; j < algs.length; j++) {
                line[2 + j] = String.valueOf(histories[j][i]);
            }
            data.add(line);
        }

        // AXLE X: steps 1..n
        double[] steps = IntStream.rangeClosed(1, chain.length)
                .asDoubleStream()
                .toArray();

        // AXLE Y: histories convert to int[][] for double[][]
        double[][] accFaults = Arrays.stream(histories)
                .map(h -> Arrays.stream(h).asDoubleStream().toArray())
                .toArray(double[][]::new);

        // Series names (array algs is origin)
        String[] names = Arrays.stream(algs)
                .map(a -> a.getClass().getSimpleName())
                .toArray(String[]::new);

        XYChart chart = QuickChart.getChart(
                "Faltas acumuladas por referência (" + lengthFrame + " molduras)",
                "Referencia", "Faltas acumuladas",
                names, steps, accFaults
        );


        Map<Integer, Integer> references  = new HashMap<>();
        String[] labels = new String[chain.length];
        for (int i = 0; i < chain.length; i++) {
            int k = references.merge(chain[i], 1, Integer::sum) - 1;
            String pad = " ".repeat(k);
            labels[i] = pad + chain[i] + pad;
        }

        chart.setCustomXAxisTickLabelsFormatter(x -> {
            int i = (int) Math.round(x);
            if (Math.abs(x - i) > 1e-9 || i < 1 || i > chain.length) return "";
            return labels[i - 1];
        });

        chart.getStyler().setXAxisMin(1.0);
        chart.getStyler().setXAxisMax((double) chain.length);

        new SwingWrapper<>(chart).displayChart();

        String[][] dataArr = data.toArray(new String[0][]);
        CsvExport.export("src/data", "acc-faults-by-references", dataArr);
    }
}