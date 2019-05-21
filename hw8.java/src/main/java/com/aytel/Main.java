package com.aytel;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class Main extends Application {
    static final int FRAMES_GAP = 30;

    private static final int HEIGHT = 720;
    private static final int WIDTH = 1280;
    private static final Timeline timeline = new Timeline();

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);

        var pane = new Pane();
        pane.setStyle("-fx-background-color: #75bbfd;");

        var text = new Text();
        text.setText("1 — small bullet\n" +
                "2 — medium bullet\n" +
                "3 — large bullet\n");

        text.setX(0);
        text.setY(20);
        text.setFont(new Font(20));

        pane.getChildren().addAll(text);

        var landCanvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext landGC = landCanvas.getGraphicsContext2D();

        var aimsCanvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext aimsGC = aimsCanvas.getGraphicsContext2D();

        var cannonCanvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext cannonGC = cannonCanvas.getGraphicsContext2D();

        pane.getChildren().addAll(landCanvas, aimsCanvas, cannonCanvas);

        var scene = new Scene(pane);
        var gameLoop = new GameLoop(this, landGC, aimsGC, cannonGC);

        scene.setOnKeyPressed(gameLoop::pressKey);
        scene.setOnKeyReleased(gameLoop::releaseKey);

        primaryStage.setScene(scene);
        primaryStage.show();

        gameLoop.start();
    }

    void finish() {
        var finalPane = new Pane();
        finalPane.setStyle("-fx-background-color: #75bbfd;");

        var finalText = new Text();
        finalText.setY(HEIGHT >> 1);
        finalText.setX((WIDTH >> 1) - 120);
        finalText.setText("Congratulations!");
        finalText.setFill(Color.GREEN);
        finalText.setFont(new Font(40));

        finalPane.getChildren().add(finalText);
        primaryStage.setScene(new Scene(finalPane, WIDTH, HEIGHT));

        try {
            this.stop();
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        launch(args);
    }
}
