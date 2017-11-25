package com.akvone.machinelearning.core.parameters;

import lombok.AllArgsConstructor;
import org.ejml.simple.SimpleMatrix;

@AllArgsConstructor
public class GradientDescentParams {
    public double step;
    public SimpleMatrix startWeightVector;
}
