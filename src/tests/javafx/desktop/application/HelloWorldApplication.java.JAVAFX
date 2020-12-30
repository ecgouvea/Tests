package tests.javafx.desktop.application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloWorldApplication extends Application {

    Stage window;
    Button button;
    private int timer;


    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Inside init() method! Perform necessary initializations here.");

/*
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
*/

        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("this is called every 1 seconds on UI thread");
                button.setText(String.valueOf(timer++));
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle(" TutorialsFace - JavaFX");
        // Creating a simple UI button
        button = new Button("Click me");
        // StackPane is a type of layout which we will look at later..
        StackPane layout = new StackPane();

        //Adding the button to the layout
        layout.getChildren().add(button);

        // Create scene with resolution of 300x250 and add layout to it...
        Scene scene = new Scene(layout, 300, 250);

        // Add scene to the stage i.e to the window
        window.setScene(scene);
        window.show();
    }
/*
    @Override
    public void start(Stage primaryStage) throws Exception {
        Label label = new Label("Hello World");
        label.setAlignment(Pos.CENTER);
        Scene scene = new Scene(label, 500, 350);

        primaryStage.setTitle("Hello World Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
*/
    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Inside stop() method! Destroy resources. Perform Cleanup.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}