package com.akvone.machinelearning.core;

import com.akvone.machinelearning.core.searchbestalgorithms.Genetic;
import com.akvone.machinelearning.core.searchbestalgorithms.GradientDescent;
import com.akvone.machinelearning.core.searchbestalgorithms.SearchBestAlgorithm;
import org.ejml.simple.SimpleMatrix;
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
    private Core core;

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

        core = new Core(H, TN);

//        PlotDrawer.setTrainingObjects(TN);
//        PlotDrawer.runInPparallel();
//        Thread.sleep(3000);

        LOG.info("Run GradientDescent Algorithm");
        runGradientDescent();
//
//        LOG.info("Run Genetic Algorithm");
//        runGenetic();
    }

    private void runGradientDescent() throws InterruptedException {
        GradientDescent gradientDescent = new GradientDescent(H, core, TN, H.startWeightVector);
        runAlgorithm(gradientDescent);
    }

    private void runGenetic() throws InterruptedException {
        ArrayList<SimpleMatrix> initialPopulation = new ArrayList<>();
        initialPopulation.add(new SimpleMatrix(new double[][]{{1, 2, 3}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{4, 5, 6}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{7, 8, 9}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{10, 11, 12}}));
        initialPopulation.add(new SimpleMatrix(new double[][]{{13, 14, 15}}));

        Genetic genetic = new Genetic(H, core, initialPopulation, 5);
        runAlgorithm(genetic);
    }

    private void runAlgorithm(SearchBestAlgorithm searchBestAlgorithm) throws InterruptedException {
        int localMinimumCounter = 0;
        double previousJ = Double.MAX_VALUE;

        SimpleMatrix w_current = H.startWeightVector;
        for (int i = 1; i <= H.globalIterationNumber && localMinimumCounter <= H.localMinimumIterationNumber; i++) {
            SimpleMatrix w_new = searchBestAlgorithm.makeIterationGetBest();
            double currentJ = core.f_J(w_new);

            if (Math.abs(previousJ - currentJ) < H.localMinimumThreshold) {
                localMinimumCounter++;

            } else {
                localMinimumCounter = 0;

            }
            w_current = w_new;
            previousJ = currentJ;

            LOG.trace("{}. Iteration number = {}. J = {}", searchBestAlgorithm.getClass().getSimpleName(), i, currentJ);
        }

//        PlotDrawer.updateHypothesis(current_w);

        LOG.info("Average {}", averageOfValues);
        LOG.info("Range {}", rangeOfValues);
        LOG.info("Output weight vector {}", normalizator.denormalizeWeightVector(w_current));
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
