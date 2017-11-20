package com.akvone.machinelearning;

import com.akvone.machinelearning.core.General;
import com.akvone.machinelearning.core.HyperParams;
import org.ejml.simple.SimpleMatrix;

public class Main {

    public static void main(String[] args) throws Exception {
        SimpleMatrix startWeightVector = new SimpleMatrix(1, 3);
        startWeightVector.set(0, 1);
        startWeightVector.set(1, 1);
        startWeightVector.set(2, 1);

        HyperParams hyperParams = new HyperParams(
                50000,
                0.001,
                100,
                0.01,
                startWeightVector,
                0.1,
                0,
                0
        );

        new General(hyperParams).run();
    }
}
