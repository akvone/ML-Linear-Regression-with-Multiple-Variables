package com.akvone.machinelearning.core.math;

import com.akvone.machinelearning.core.TrainingObject;
import com.akvone.machinelearning.core.parameters.HyperParams;
import lombok.AllArgsConstructor;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.function.Function;

@AllArgsConstructor
public class CoreFunctions {
    private HyperParams H;

    public static <T> double sum(int firstIndex, int lastIndex, Function<T, Double> function, Function<Integer, T> supplier) {
        double sum = 0;

        for (int i = firstIndex; i < lastIndex; i++) {
            T arg = supplier.apply(i);
            sum += function.apply(arg);
        }

        return sum;
    }

    /**
     * @return Result of hypothesis function: h(weightVector, x) = sum of (wi*xi), where i: 0..featureNumber
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
