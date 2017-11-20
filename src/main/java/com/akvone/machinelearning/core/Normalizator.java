package com.akvone.machinelearning.core;

import lombok.AllArgsConstructor;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;

import static com.akvone.machinelearning.core.General.sum;
import static com.akvone.machinelearning.core.TrainingObject.averageOfValues;
import static com.akvone.machinelearning.core.TrainingObject.rangeOfValues;

@AllArgsConstructor
public class Normalizator {

    HyperParams H;

    public ArrayList<TrainingObject> normalize(ArrayList<TrainingObject> T) {
        ArrayList<TrainingObject> TN = new ArrayList<>();

        for (int i = 0; i < H.trainingSetLength; i++) {
            TrainingObject trObj = T.get(i);
            TN.add(new TrainingObject(trObj.y, getNormalizedFeatures(trObj)));
        }

        return TN;
    }

    public SimpleMatrix denormalizeWeightVector(SimpleMatrix w) {
        SimpleMatrix denormalizedWeightVector = new SimpleMatrix(1, H.featureNumber);

        denormalizedWeightVector.set(0, w.get(0) +
                sum(1, H.featureNumber,
                        i -> -averageOfValues.get(i) * w.get(i) / rangeOfValues.get(i),
                        index -> index));

        for (int i = 1; i < H.featureNumber; i++) {
            denormalizedWeightVector.set(i, w.get(i) / rangeOfValues.get(i));
        }

        return denormalizedWeightVector;
    }

    private double normalizeFeature(double initialValue, double averageValue, double rangeValue) {
        return (initialValue - averageValue) / rangeValue;
    }

    private Double[] getNormalizedFeatures(TrainingObject trObj) {
        Double[] normalizedFeatures = new Double[H.featureNumber];

        normalizedFeatures[0] = TrainingObject.x0value;
        for (int featureIndex = 1; featureIndex < H.featureNumber; featureIndex++) {
            normalizedFeatures[featureIndex] = normalizeFeature(
                    trObj.X.get(featureIndex),
                    averageOfValues.get(featureIndex),
                    rangeOfValues.get(featureIndex)
            );
        }

        return normalizedFeatures;
    }
}
