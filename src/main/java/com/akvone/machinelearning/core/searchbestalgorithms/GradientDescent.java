package com.akvone.machinelearning.core.searchbestalgorithms;

import com.akvone.machinelearning.core.Core;
import com.akvone.machinelearning.core.HyperParams;
import com.akvone.machinelearning.core.TrainingObject;
import lombok.AllArgsConstructor;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;

@AllArgsConstructor
public class GradientDescent extends SearchBestAlgorithm {
    private HyperParams H;
    private Core core;
    private ArrayList<TrainingObject> T;
    private SimpleMatrix w_current;

    @Override
    public SimpleMatrix makeIterationGetBest() {
        SimpleMatrix w_sum = new SimpleMatrix(1, H.featureNumber);

        for (TrainingObject trObj : T) {
            SimpleMatrix xi = trObj.xMatrixRepresentation();

            double multiplier = H.step / H.trainingSetLength * (core.f_hypothesis(w_current, trObj) - trObj.y);
            SimpleMatrix iterationWeight = xi.scale(multiplier);

            w_sum = w_sum.plus(iterationWeight);
        }

        w_current = w_current.minus(w_sum);

        return w_current;
    }
}
