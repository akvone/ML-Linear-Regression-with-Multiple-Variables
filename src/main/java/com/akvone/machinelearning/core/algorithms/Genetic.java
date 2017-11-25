package com.akvone.machinelearning.core.algorithms;

import com.akvone.machinelearning.core.math.CoreFunctions;
import com.akvone.machinelearning.core.parameters.GeneticParams;
import com.akvone.machinelearning.core.parameters.HyperParams;
import com.akvone.machinelearning.core.TrainingObject;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Genetic implements SearchBestAlgorithm {

    private HyperParams HP;
    private GeneticParams GP;
    private ArrayList<TrainingObject> T;
    private List<SimpleMatrix> currentPopulation = new ArrayList<>();

    private CoreFunctions core;


    public Genetic(HyperParams HP, GeneticParams GP, ArrayList<TrainingObject> T) {
        this.HP = HP;
        this.GP = GP;
        this.T = T;

        currentPopulation = new ArrayList<>(GP.initialPopulation);
        core = new CoreFunctions(HP);
    }

    @Override
    public void makeIteration() {
        currentPopulation = makeGeneticsIteration(currentPopulation, GP.survivorNumber);
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

    private double calculateError(SimpleMatrix w) {
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

        for (int i = 0; i < HP.featureNumber; i++) {
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
        if (Math.random() < GP.mutationProbability) {
            return 2 * Math.random();
        } else {
            return 1;
        }
    }
}
