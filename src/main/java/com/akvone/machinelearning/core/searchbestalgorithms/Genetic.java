package com.akvone.machinelearning.core.searchbestalgorithms;

import com.akvone.machinelearning.core.Core;
import com.akvone.machinelearning.core.HyperParams;
import com.akvone.machinelearning.core.TrainingObject;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Genetic extends SearchBestAlgorithm {

    private HyperParams H;
    private ArrayList<TrainingObject> T;
    private List<SimpleMatrix> currentPopulation;
    private int survivorNumber;

    private Core core;


    public Genetic(HyperParams H, ArrayList<TrainingObject> T, List<SimpleMatrix> currentPopulation, int survivorNumber) {
        this.H = H;
        this.T = T;
        this.currentPopulation = currentPopulation;
        this.survivorNumber = survivorNumber;

        core = new Core(H);
    }

    @Override
    public void makeIteration() {
        currentPopulation = makeGeneticsIteration(currentPopulation, survivorNumber);
    }

    @Override
    public SimpleMatrix getBestWeight() {
        SimpleMatrix bestChild = Collections.max(currentPopulation, Comparator.comparing(this::calculateError));

        return bestChild;
    }

    @Override
    public double getErrorFromBest() {
        return core.f_J(getBestWeight(), T);
    }

    private double calculateError(SimpleMatrix w){
        return core.f_J(w, T);
    }

    private List<SimpleMatrix> makeGeneticsIteration(List<SimpleMatrix> parents, int survivorNumber) {
        ArrayList<SimpleMatrix> children = selection(parents);

        ArrayList<Double> error = new ArrayList<>();
        for (SimpleMatrix w : parents) {
            error.add(core.f_J(w, T));
        }

        children.sort((Comparator.comparing(this::calculateError)));

        List<SimpleMatrix> survivors = children.subList(0, survivorNumber);

        return survivors;
    }

    private ArrayList<SimpleMatrix> selection(List<SimpleMatrix> parents) {
        ArrayList<SimpleMatrix> children = new ArrayList<>();

        for (int i = parents.size() - 1; i >= 1; i--) {
            for (int j = i - 1; j >= 0; j--) {
                children.add(crossover(parents.get(i), parents.get(j)));
            }
        }

        return children;
    }

    private SimpleMatrix crossover(SimpleMatrix parent1, SimpleMatrix parent2) {
        SimpleMatrix child = new SimpleMatrix(parent1); // Все коеф. от 1 родителя

        for (int i = 0; i < H.featureNumber; i++) {
            double newValue;

            if (Math.random() > 0.5) {
                newValue = parent1.get(i);
            } else {
                newValue = parent2.get(i);
            }

            newValue *= generateMutationCoefficient();

            child.set(i, newValue);
        }

        return child;
    }

    private double generateMutationCoefficient() {
        if (Math.random() < H.mutationProbability) {
            return 2 * Math.random();
        } else {
            return 1;
        }
    }
}
