package com.akvone.machinelearning.core.algorithms;

import com.akvone.machinelearning.core.math.CoreFunctions;
import com.akvone.machinelearning.core.TrainingObject;
import com.akvone.machinelearning.core.parameters.GradientDescentParams;
import com.akvone.machinelearning.core.parameters.HyperParams;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;

public class GradientDescent implements SearchBestAlgorithm {
    private HyperParams HP;
    private GradientDescentParams GDP;
    private ArrayList<TrainingObject> T;
    private CoreFunctions core;
    private SimpleMatrix w_current;

    public GradientDescent(HyperParams HP, GradientDescentParams GDP, ArrayList<TrainingObject> T) {
        this.HP = HP;
        this.GDP = GDP;
        this.T = T;

        w_current = this.GDP.startWeightVector;
        core = new CoreFunctions(this.HP);
    }

    @Override
    public void makeIteration() {
        SimpleMatrix w_sum = new SimpleMatrix(1, HP.featureNumber);

        for (TrainingObject trObj : T) {
            SimpleMatrix xi = trObj.xMatrixRepresentation();

            double multiplier = GDP.step / HP.trainingSetLength * (core.f_hypothesis(w_current, trObj) - trObj.y);
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
