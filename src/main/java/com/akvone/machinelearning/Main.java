package com.akvone.machinelearning;

import com.akvone.machinelearning.core.General;
import com.akvone.machinelearning.core.parameters.GeneticParams;
import com.akvone.machinelearning.core.parameters.GradientDescentParams;
import com.akvone.machinelearning.core.parameters.HyperParams;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        HyperParams hyperParams = new HyperParams(
                50000,
                0.0001,
                100,
                0,
                0
        );
        //
        SimpleMatrix startWeightVector = new SimpleMatrix(1, 3);
        startWeightVector.set(0, 1);
        startWeightVector.set(1, 1);
        startWeightVector.set(2, 1);

        GradientDescentParams gradientDescentParams = new GradientDescentParams(0.01, startWeightVector);
        //
        ArrayList<SimpleMatrix> initialPopulation = new ArrayList<>();
        initialPopulation.add(new SimpleMatrix(new double[][]{{1, 2, 3}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{4, 5, 6}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{7, 8, 9}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{10, 11, 12}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{13, 14, 15}}));

        GeneticParams geneticParams = new GeneticParams(5, 0.1, initialPopulation);


        new General(hyperParams, gradientDescentParams, geneticParams).run();
    }
}
