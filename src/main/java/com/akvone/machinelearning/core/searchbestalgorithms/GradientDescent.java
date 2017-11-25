package com.akvone.machinelearning.core.searchbestalgorithms;

import com.akvone.machinelearning.core.Core;
import com.akvone.machinelearning.core.HyperParams;
import com.akvone.machinelearning.core.TrainingObject;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;

public class GradientDescent extends SearchBestAlgorithm {
    private HyperParams H;
    private ArrayList<TrainingObject> T;
    private Core core;
    private SimpleMatrix w_current;

    public GradientDescent(HyperParams H, ArrayList<TrainingObject> T) {
        this.H = H;
        this.T = T;

        core = new Core(this.H);
        w_current = H.startWeightVector;
    }

    @Override
    public void makeIteration() {
        SimpleMatrix w_sum = new SimpleMatrix(1, H.featureNumber);

        for (TrainingObject trObj : T) {
            SimpleMatrix xi = trObj.xMatrixRepresentation();

            double multiplier = H.step / H.trainingSetLength * (core.f_hypothesis(w_current, trObj) - trObj.y);
            SimpleMatrix iterationWeight = xi.scale(multiplier);

            w_sum = w_sum.plus(iterationWeight);
        }

        w_current = w_current.minus(w_sum);
    }

    @Override
    public SimpleMatrix getBestWeight() {
        return w_current;
    }

    @Override
    public double getErrorFromBest() {
        return core.f_J(w_current, T);
    }
}
