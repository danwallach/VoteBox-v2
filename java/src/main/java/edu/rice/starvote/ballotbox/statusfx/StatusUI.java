package edu.rice.starvote.ballotbox.statusfx;/**
 * Created by luej on 9/7/16.
 */

import edu.rice.starvote.ballotbox.BallotStatus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StatusUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        final InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("StatusUI.fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader();
        final StatusUIController controller = new StatusUIController();
        fxmlLoader.setController(controller);
        final Parent root = fxmlLoader.load(resourceStream);


        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getClassLoader().getResource("StatusUI.css").toExternalForm());

        primaryStage.setTitle("Ballot box 1.5");
        primaryStage.setScene(scene);
        primaryStage.show();

        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> controller.pushStatus(BallotStatus.ACCEPT), 3, 6, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> controller.pushStatus(BallotStatus.REJECT), 6, 6, TimeUnit.SECONDS);
    }
}
