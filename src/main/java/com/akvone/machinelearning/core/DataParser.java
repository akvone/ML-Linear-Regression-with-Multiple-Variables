package com.akvone.machinelearning.core;

import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;
import static java.lang.Double.valueOf;

public class DataParser {

    public static ArrayList<TrainingObject> parse(String filename) throws Exception {
        ArrayList<TrainingObject> dataList = new ArrayList<>();

        URL url = DataParser.class.getResource("/" + filename);
        URI uri = url.toURI();
        Files.lines(Paths.get(uri), StandardCharsets.UTF_8)
                .skip(1)
                .forEach(s -> {
                    String[] values = s.split(",");
                    dataList.add(TrainingObject.getInitializedWithDefaults(parseDouble(values[2]), valueOf(values[0]), valueOf(values[1])));
                });

        return dataList;
    }
}
