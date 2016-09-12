package edu.rice.starvote.ballotbox;

import edu.rice.starvote.ballotbox.statusfx.StatusUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import votebox.AuditoriumParams;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * Created by luej on 9/9/16.
 */
public class BallotBoxFX extends Application {

    private FXSTARController controller;
    private StatusUIController fxmlController;

    @Override
    public void init() throws Exception {
        final AuditoriumParams _constants = new AuditoriumParams("bs.conf");
        final int serial;
        final List<String> args = getParameters().getUnnamed();
        if (args.size() == 1) {
            serial = Integer.parseInt(args.get(0));
        } else {
            serial = _constants.getDefaultSerialNumber();
        }
        assert serial != -1 : "No machineID provided";
        final Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter launch code: ");
        final String launchCode = keyboard.next();
        keyboard.close();
        fxmlController = new StatusUIController();
        controller = new FXSTARController(serial, launchCode, fxmlController);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final InputStream fxmlStream = getClass().getClassLoader().getResourceAsStream("StatusUI.fxml");
        assert fxmlStream != null : "Failed to load UI XML resource";
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(fxmlController);
        final Parent root = fxmlLoader.load(fxmlStream);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Ballot box 1.6");
        primaryStage.setScene(scene);
        primaryStage.show();

        controller.run();
    }

    public static void main (String[] args) {
        launch(args);
    }
}
