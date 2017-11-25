package com.akvone.machinelearning.core.algorithms;

import org.ejml.simple.SimpleMatrix;

public interface SearchBestAlgorithm {
    void makeIteration();

    SimpleMatrix getBestWeight();

    double getErrorFromBest();
}
