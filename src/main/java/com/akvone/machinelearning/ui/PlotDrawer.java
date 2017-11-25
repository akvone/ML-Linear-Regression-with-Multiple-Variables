package com.akvone.machinelearning.ui;

import com.akvone.machinelearning.core.math.CoreFunctions;
import com.akvone.machinelearning.core.TrainingObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.ejml.simple.SimpleMatrix;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Scale;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.axes.layout.providers.StaticTickProvider;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlotDrawer extends Application {

    private static Coord3d[] coordinates = null;
    private static LineStrip lineStrip;

    private static Scatter scatter;
    private static AWTChart chart;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void runInParallel() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            executorService.submit(() -> Application.launch(""));
        } finally {
            executorService.shutdownNow();
        }
    }

    static {
        scatter = new Scatter();
        scatter.setColor(Color.BLUE);
        scatter.setWidth(5);

//        lineStrip = new LineStrip();
//        lineStrip.setWidth(2);
//        lineStrip.setWireframeColor(Color.RED);
    }

    public static void addNewHypothesis(CoreFunctions core, SimpleMatrix w, Color color) {
        LineStrip lineStrip = new LineStrip();

        lineStrip.addAll(getPointsForHypothesis(core, w));
        lineStrip.setWidth(2);
        lineStrip.setWireframeColor(color);

        chart.getScene().getGraph().add(lineStrip);
    }

    public static void updateHypothesis(CoreFunctions core, SimpleMatrix w) {
        lineStrip.clear();

        lineStrip.addAll(getPointsForHypothesis(core, w));

        lineStrip.updateBounds();
    }

    private static List<Point> getPointsForHypothesis(CoreFunctions core, SimpleMatrix w) {
        return Arrays.asList(
                newPoint(core, w, -10, -10),
                newPoint(core, w, 0, 0),
                newPoint(core, w, 10, 10)
        );
    }

    private static Point newPoint(CoreFunctions core, SimpleMatrix w, double x, double y) {
        TrainingObject buildPointObject = TrainingObject.initializeWithDefaults(0, x, y);

        return new Point(new Coord3d(x, y, core.f_hypothesis(w, buildPointObject)));
    }

    public static void setTrainingObjects(ArrayList<TrainingObject> T) {
        Coord3d[] coordinates = new Coord3d[T.size()];
        for (int i = 0; i < T.size(); i++) {
            TrainingObject trObj = T.get(i);

            coordinates[i] = new Coord3d(trObj.X.get(1), trObj.X.get(2), trObj.y);
        }

        PlotDrawer.coordinates = coordinates;
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Jzy3d
        JavaFXChartFactory factory = new JavaFXChartFactory();
        AWTChart chart = getDemoChart(factory, "offscreen");
        ImageView imageView = factory.bindImageView(chart);

        // JavaFX
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        pane.getChildren().add(imageView);

        factory.addSceneSizeChangedListener(chart, scene);

        stage.setWidth(1000);
        stage.setHeight(1000);
    }

    private AWTChart getDemoChart(JavaFXChartFactory factory, String toolkit) {
        Quality quality = Quality.Fastest;
        quality.setSmoothPolygon(true);
        quality.setAnimated(true);

        chart = (AWTChart) factory.newChart(quality, toolkit);

        scatter.setData(coordinates);

        chart.getScene().getGraph().add(scatter);
//        chart.getScene().getGraph().add(lineStrip);

        chart.getAxeLayout().setXTickProvider(new StaticTickProvider(new double[]{0, 1000, 2000, 3000}));
        chart.getAxeLayout().setYTickProvider(new StaticTickProvider(new double[]{0, 5}));
        chart.getAxeLayout().setZTickProvider(new StaticTickProvider(new double[]{0, 100000, 500000}));

        chart.setScale(new Scale(0, 400000));

        return chart;
    }
}