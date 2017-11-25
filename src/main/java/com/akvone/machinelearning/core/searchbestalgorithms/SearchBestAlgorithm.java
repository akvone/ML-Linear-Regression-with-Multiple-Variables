package com.akvone.machinelearning.core.searchbestalgorithms;

import org.ejml.simple.SimpleMatrix;

public abstract class SearchBestAlgorithm {

    abstract public void makeIteration();

    abstract public SimpleMatrix getBestWeight();

    abstract public double getErrorFromBest();
}
