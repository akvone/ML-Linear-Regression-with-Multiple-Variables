package com.akvone.machinelearning.core;

import com.akvone.machinelearning.core.algorithms.Genetic;
import com.akvone.machinelearning.core.algorithms.GradientDescent;
import com.akvone.machinelearning.core.algorithms.SearchBestAlgorithm;
import com.akvone.machinelearning.core.math.CoreFunctions;
import com.akvone.machinelearning.core.math.Normalizator;
import com.akvone.machinelearning.core.parameters.GeneticParams;
import com.akvone.machinelearning.core.parameters.GradientDescentParams;
import com.akvone.machinelearning.core.parameters.HyperParams;
import com.akvone.machinelearning.ui.PlotDrawer;
import org.jzy3d.colors.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;

import static com.akvone.machinelearning.core.TrainingObject.averageOfValues;
import static com.akvone.machinelearning.core.TrainingObject.rangeOfValues;
import static java.util.Collections.max;
import static java.util.Collections.min;

public class General {
    private static Logger LOG = LoggerFactory.getLogger(General.class);

    private HyperParams H;
    private final GradientDescentParams GDP;
    private final GeneticParams GP;

    private ArrayList<TrainingObject> T;
    private ArrayList<TrainingObject> TN;
    private Normalizator normalizator;

    public General(HyperParams HP, GradientDescentParams GDP, GeneticParams GP) {
        this.H = HP;
        this.GDP = GDP;
        this.GP = GP;
    }

    private void initialize() throws Exception {
        T = DataParser.parse("prices.txt");

        H.trainingSetLength = T.size();
        H.featureNumber = T.get(0).X.size();

        for (int i = 0; i < H.featureNumber; i++) {
            int finalI = i;

            rangeOfValues.add(
                    max(T, Comparator.comparing(o -> o.X.get(finalI))).X.get(finalI) -
                            min(T, Comparator.comparing(o -> o.X.get(finalI))).X.get(finalI)
            );
            averageOfValues.add(T.stream().mapToDouble(value -> value.X.get(finalI)).average().getAsDouble());
        }

        LOG.info("Average of values: {}", TrainingObject.averageOfValues);
        LOG.info("Range of values is: {}", TrainingObject.rangeOfValues);
    }

    public void run() throws Exception {
        initialize();

        normalizator = new Normalizator(H);
        TN = normalizator.normalize(T);

        PlotDrawer.setTrainingObjects(TN);
        PlotDrawer.runInParallel();
        Thread.sleep(3000);

        LOG.info("Run GradientDescent Algorithm");
        GradientDescent gradientDescent = runGradientDescent(TN);


        LOG.info("Run Genetic Algorithm");
        Genetic genetic = runGenetic(TN);

        logResults(gradientDescent);
        logResults(genetic);
    }

    private GradientDescent runGradientDescent(ArrayList<TrainingObject> T) throws InterruptedException {
        GradientDescent gradientDescent = new GradientDescent(H, GDP, T);
        runAlgorithm(gradientDescent);

        PlotDrawer.addNewHypothesis(new CoreFunctions(H), gradientDescent.getBestWeight(), Color.RED);

        return gradientDescent;
    }

    private Genetic runGenetic(ArrayList<TrainingObject> T) throws InterruptedException {
        Genetic genetic = new Genetic(H, GP, T);
        runAlgorithm(genetic);

        PlotDrawer.addNewHypothesis(new CoreFunctions(H), genetic.getBestWeight(), Color.GREEN);

        return genetic;
    }

    private void runAlgorithm(SearchBestAlgorithm searchBestAlgorithm) throws InterruptedException {
        int localMinimumCounter = 0;
        double previousError = Double.MAX_VALUE;

        for (int i = 1; i <= H.globalIterationNumber && localMinimumCounter <= H.localMinimumIterationNumber; i++) {
            searchBestAlgorithm.makeIteration();
            double currentError = searchBestAlgorithm.getErrorFromBest();

            if (currentError < previousError - H.localMinimumThreshold) {
                localMinimumCounter = 0;
            } else {
                localMinimumCounter++;
            }
            previousError = currentError;

            LOG.trace("{}. Iteration number = {}. J = {}", searchBestAlgorithm.getClass().getSimpleName(), i, currentError);
        }

        LOG.info("Algorithm {} has been done", searchBestAlgorithm.getClass().getSimpleName(), searchBestAlgorithm.getErrorFromBest());
    }

    private void logResults(SearchBestAlgorithm searchBestAlgorithm) {
        LOG.info("***");
        LOG.info("Algorithm: {}", searchBestAlgorithm.getClass().getSimpleName());
        LOG.info("Output weight vector {}", normalizator.denormalizeWeightVector(searchBestAlgorithm.getBestWeight()));
        LOG.info("Best error {}", searchBestAlgorithm.getErrorFromBest());
        LOG.info("***");
    }
}
