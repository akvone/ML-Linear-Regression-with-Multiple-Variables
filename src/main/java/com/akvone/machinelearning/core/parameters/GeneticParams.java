package com.akvone.machinelearning.core.parameters;

import lombok.AllArgsConstructor;
import org.ejml.simple.SimpleMatrix;

import java.util.List;

@AllArgsConstructor
public class GeneticParams {
    public int survivorNumber;
    public double mutationProbability;
    public List<SimpleMatrix> initialPopulation;
}
