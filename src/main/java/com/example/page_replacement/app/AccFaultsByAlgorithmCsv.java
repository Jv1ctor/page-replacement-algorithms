package com.example.page_replacement.app;

import com.example.page_replacement.algorithms.AlgorithmsBase;
import com.example.page_replacement.algorithms.implementation.FIFO;
import com.example.page_replacement.algorithms.implementation.LRU;
import com.example.page_replacement.algorithms.implementation.NFU;
import com.example.page_replacement.algorithms.implementation.Optimal;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccFaultsByAlgorithmCsv {
    public static void main(String[] args) {
        int[] chain = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1};
        int lengthFrame = 3;

        AlgorithmsBase[] algs = {
                new FIFO(lengthFrame),
                new LRU(lengthFrame),
                new NFU(lengthFrame),
                new Optimal(lengthFrame)
        };

        int[] faultsList = new int[algs.length];

        ArrayList<String[]> data = new ArrayList<>();

        // Header CSV
        data.add(new String[]{
                "algoritmo", "faults"
        });

        for (int i = 0; i < algs.length; i++) {
            algs[i].execute(chain);
            int faults = algs[i].getFaults();
            faultsList[i] = faults;
            data.add(new String[]{algs[i].getClass().getSimpleName(), String.valueOf(faults)});
        }

        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(500)
                .title("Faltas de página por algoritmo")
                .xAxisTitle("Algoritmo")
                .yAxisTitle("Faltas")
                .build();

        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setLabelsVisible(true);

        List<String> names = Arrays.stream(algs)
                .map(it -> it.getClass().getSimpleName())
                .toList();

        List<Integer> faultsValues = Arrays.stream(faultsList)
                .boxed()          // IntStream -> Stream<Integer>
                .toList();

        chart.addSeries("Faltas", names, faultsValues);

        // Abre uma janela com o gráfico
        new SwingWrapper<>(chart).displayChart();

        // exporta csv
        String[][] dataArr = data.stream().toArray(String[][]::new);
        CsvExport.export("src/data", "acc-faults-by-algorithms", dataArr);
    }
}
