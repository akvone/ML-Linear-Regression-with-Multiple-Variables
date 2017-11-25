package com.akvone.machinelearning.core;

import lombok.AllArgsConstructor;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;

import static com.akvone.machinelearning.core.General.sum;

@AllArgsConstructor
public class Core {
    private HyperParams H;

    /**
     * @return Result of hypothesis function: h(x, startWeightVector) = sum of (wi*xi), where i: 0..featureNumber
     */
    public double f_hypothesis(SimpleMatrix w, TrainingObject trObj) {
        SimpleMatrix multiplicatedMatrix = w.mult(trObj.xMatrixRepresentation().transpose());

        return multiplicatedMatrix.get(0);
    }

    /**
     * @return Cost function J
     */
    public double f_J(SimpleMatrix w, ArrayList<TrainingObject> T) {
        return Math.sqrt(1d / (2 * H.trainingSetLength) * sum(0, H.trainingSetLength,
                o -> Math.pow(f_hypothesis(w, o) - o.y, 2),
                T::get));
    }
}
