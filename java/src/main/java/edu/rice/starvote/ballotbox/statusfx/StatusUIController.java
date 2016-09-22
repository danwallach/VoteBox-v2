package edu.rice.starvote.ballotbox.statusfx;

/**
 * Created by luej on 9/9/16.
 */
import java.net.URL;
import java.util.ResourceBundle;

import edu.rice.starvote.ballotbox.BallotStatus;
import edu.rice.starvote.ballotbox.IStatusUpdate;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class StatusUIController implements IStatusUpdate {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane frame;

    @FXML
    private Text statusValue;

    @FXML
    void initialize() {
        assert frame != null : "FXML Frame element was not injected";
        assert statusValue != null : "FXML Text element was not injected";
    }

    public void pushStatus(BallotStatus status) {
        final ObservableList<String> frameStyle = frame.getStyleClass();
        switch (status) {
            case OFFLINE:
                frameStyle.clear();
                frameStyle.add("red");
                statusValue.setText("Use next machine.");
                break;
            case WAITING:
                frameStyle.clear();
                frameStyle.add("gray");
                statusValue.setText("Please insert your ballot.");
                break;
            case SPOOLING:
                frameStyle.clear();
                frameStyle.add("gray");
                statusValue.setText("");
                break;
            case ACCEPT:
                frameStyle.clear();
                frameStyle.add("green");
                statusValue.setText("Your ballot was cast!");
                break;
            case REJECT:
                frameStyle.clear();
                frameStyle.add("red");
                statusValue.setText("Your ballot was not recognized. Only insert unfolded ballots.");
                break;
            default:
                frameStyle.clear();
                frameStyle.add("red");
                statusValue.setText("ERROR");
                break;
        }
    }
}
