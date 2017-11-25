package com.akvone.machinelearning.core.parameters;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HyperParams {
    public int globalIterationNumber;
    public double localMinimumThreshold;
    public int localMinimumIterationNumber;

    public int trainingSetLength;
    public int featureNumber;
}
