package com.akvone.machinelearning.core;

import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrainingObject {
    public static double x0value = 1;
    public static ArrayList<Double> averageOfValues = new ArrayList<>();
    public static ArrayList<Double> rangeOfValues = new ArrayList<>();

    public final List<Double> X; // without x0
    public final double y;

    public TrainingObject(double y, Double... X) {
        this.X = Arrays.asList(X);
        this.y = y;
    }

    public static TrainingObject getInitializedWithDefaults(double y, Double... X) {
        Double[] features = new Double[X.length + 1];
        features[0] = x0value;
        for (int i = 1; i <= X.length; i++) {
            features[i] = X[i - 1];
        }

        return new TrainingObject(y, features);
    }

    public SimpleMatrix xMatrixRepresentation() {
        SimpleMatrix matrix = new SimpleMatrix(1, X.size());

        for (int i = 0; i < X.size(); i++) {
            matrix.set(i, X.get(i));
        }

        return matrix;
    }
}
