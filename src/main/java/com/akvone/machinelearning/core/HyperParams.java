package com.akvone.machinelearning.core;

import lombok.AllArgsConstructor;
import org.ejml.simple.SimpleMatrix;

@AllArgsConstructor
public class HyperParams {
    public int globalIterationNumber;
    public double localMinimumThreshold;
    public int localMinimumIterationNumber;

    public double step; // alpha step
    public SimpleMatrix startWeightVector; //weightVector

    public double mutationProbability;

    public int trainingSetLength;
    public int featureNumber;
}
