package com.akvone.machinelearning.core;

import com.akvone.machinelearning.core.searchbestalgorithms.Genetic;
import com.akvone.machinelearning.core.searchbestalgorithms.GradientDescent;
import com.akvone.machinelearning.core.searchbestalgorithms.SearchBestAlgorithm;
import com.akvone.machinelearning.ui.PlotDrawer;
import org.ejml.simple.SimpleMatrix;
import org.jzy3d.colors.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;

import static com.akvone.machinelearning.core.TrainingObject.averageOfValues;
import static com.akvone.machinelearning.core.TrainingObject.rangeOfValues;
import static java.util.Collections.max;
import static java.util.Collections.min;

public class General {
    private static Logger LOG = LoggerFactory.getLogger(General.class);

    private HyperParams H;

    ArrayList<TrainingObject> T;
    ArrayList<TrainingObject> TN;
    Normalizator normalizator;

    public General(HyperParams hyperParams) {
        this.H = hyperParams;
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
    }

    public void run() throws Exception {
        initialize();

        normalizator = new Normalizator(H);
        TN = normalizator.normalize(T);

        PlotDrawer.setTrainingObjects(TN);
        PlotDrawer.runInParallel();
        Thread.sleep(3000);

        LOG.info("Run GradientDescent Algorithm");
        runGradientDescent(TN);

        LOG.info("Run Genetic Algorithm");
        runGenetic(TN);
    }

    private void runGradientDescent(ArrayList<TrainingObject> T) throws InterruptedException {
        GradientDescent gradientDescent = new GradientDescent(H, T);
        runAlgorithm(gradientDescent);

//        PlotDrawer.updateHypothesis(new Core(H), gradientDescent.getBestWeight());
        PlotDrawer.addNewHypothesis(new Core(H), gradientDescent.getBestWeight(), Color.RED);
    }

    private void runGenetic(ArrayList<TrainingObject> T) throws InterruptedException {
        ArrayList<SimpleMatrix> initialPopulation = new ArrayList<>();
        initialPopulation.add(new SimpleMatrix(new double[][]{{1, 2, 3}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{4, 5, 6}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{7, 8, 9}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{10, 11, 12}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{13, 14, 15}}));

        Genetic genetic = new Genetic(H, T, initialPopulation, 5);
        runAlgorithm(genetic);

        PlotDrawer.addNewHypothesis(new Core(H), genetic.getBestWeight(), Color.GREEN);
    }

    private void runAlgorithm(SearchBestAlgorithm searchBestAlgorithm) throws InterruptedException {
        int localMinimumCounter = 0;
        double previousError = Double.MAX_VALUE;

        SimpleMatrix w_current = H.startWeightVector;
        for (int i = 1; i <= H.globalIterationNumber && localMinimumCounter <= H.localMinimumIterationNumber; i++) {
            searchBestAlgorithm.makeIteration();
            double currentError = searchBestAlgorithm.getErrorFromBest();

            if (currentError > previousError && Math.abs(previousError - currentError) < H.localMinimumThreshold) {
                localMinimumCounter++;

            } else {
                localMinimumCounter = 0;

            }
            previousError = currentError;

            LOG.trace("{}. Iteration number = {}. J = {}", searchBestAlgorithm.getClass().getSimpleName(), i, currentError);
        }

        LOG.trace("J = {}", searchBestAlgorithm.getClass().getSimpleName(), searchBestAlgorithm.getErrorFromBest());
        LOG.info("Average {}", averageOfValues);
        LOG.info("Range {}", rangeOfValues);
        LOG.info("Output weight vector {}", normalizator.denormalizeWeightVector(searchBestAlgorithm.getBestWeight()));
    }


    public static <T> double sum(int firstIndex, int lastIndex, Function<T, Double> function, Function<Integer, T> supplier) {
        double sum = 0;

        for (int i = firstIndex; i < lastIndex; i++) {
            T arg = supplier.apply(i);
            sum += function.apply(arg);
        }

        return sum;
    }
}
