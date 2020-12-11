package hulubattle.client.controller;

import java.util.logging.Logger;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

public class MainViewController {
    private Logger logger = Logger.getLogger("hulubattle.client.controller.MainViewController");

    @FXML
    private Button submitBtn;
    @FXML
    private Button moveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button skillBtn;
    @FXML
    private Label logLabel;
    @FXML
    private Label skillDesc;
    @FXML
    private Label moveDesc;
    @FXML
    private AnchorPane mapPane;

    public MainViewController() {
        // no-arg constuctor
    }

    public void initialize() {
        logger.info("initialize called");
        IntStream.range(0, 9).forEach(i -> {
            Line hLine = new Line();
            hLine.setEndX(600.0);
            Line vLine = new Line();
            vLine.setEndY(600.0);
            AnchorPane.setTopAnchor(hLine, 60.0 * ( i + 1));
            AnchorPane.setLeftAnchor(hLine, 0.0);
            AnchorPane.setTopAnchor(vLine, 0.0);
            AnchorPane.setLeftAnchor(vLine, 60.0 * (i + 1));
            mapPane.getChildren().addAll(hLine, vLine);
        });
    }
}
