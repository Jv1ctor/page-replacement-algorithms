package com.example.page_replacement.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class CsvExport {

    public static void export(String path, String filename, String[][] data){
        String fullPath = path.concat("/").concat(filename);
        File folder = new File(path);

        if(!folder.exists() && !folder.isDirectory()){
            boolean created = folder.mkdir();

            if(created){
                System.out.println("created folder");
            }else{
                System.out.println("folder exist and is not possible create");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            for (String[] linha : data) {
                writer.write(String.join(";", linha));
                writer.newLine();
            }
            System.out.println("Arquivo CSV gerado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
