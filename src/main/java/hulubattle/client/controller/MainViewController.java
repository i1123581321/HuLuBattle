package hulubattle.client.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import hulubattle.client.view.CharacterGrid;
import hulubattle.game.data.AbstractCharacterData;
import hulubattle.game.data.AbstractSkillData;
import hulubattle.game.data.DataSupplier;
import hulubattle.game.data.JsonDataSupplier;
import hulubattle.game.data.SimpleCharacterData;
import hulubattle.game.data.SimpleSkillData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class MainViewController {
    public static final double GRID_WIDTH = 60.0;
    public static final double GRID_HEIGHT = 60.0;
    public static final int GRID_ROW = 10;
    public static final int GRID_COL = 10;

    private enum State {
        INIT, MOVE_1, MOVE_2, CAST_1, CAST_2
    }

    private Logger logger = Logger.getLogger("hulubattle.client.controller.MainViewController");

    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    private Button moveBtn;
    @FXML
    private Label moveDesc;
    @FXML
    private Button skillBtn;
    @FXML
    private Label skillDesc;

    @FXML
    private VBox controlBox;

    @FXML
    private Label logLabel;

    @FXML
    private AnchorPane mapPane;

    private DataSupplier<AbstractCharacterData> charactersData;
    private DataSupplier<AbstractSkillData> skillsData;
    private Map<Integer, CharacterGrid> cMap;

    private State state = State.INIT;
    private int source;
    private int target;
    private int x;
    private int y;
    private int skill;

    public MainViewController() throws URISyntaxException, IOException {
        URL characterURL = getClass().getClassLoader().getResource("config/characters.json");
        URL skillURL = getClass().getClassLoader().getResource("config/skills.json");
        charactersData = new JsonDataSupplier<>(SimpleCharacterData.class, characterURL);
        skillsData = new JsonDataSupplier<>(SimpleSkillData.class, skillURL);
    }

    public void initialize() {
        logger.info("initialize called");

        IntStream.range(0, 11).forEach(i -> {
            Line hLine = new Line();
            hLine.setEndX(GRID_COL * GRID_WIDTH);
            Line vLine = new Line();
            vLine.setEndY(GRID_ROW * GRID_HEIGHT);
            AnchorPane.setTopAnchor(hLine, GRID_HEIGHT * i);
            AnchorPane.setLeftAnchor(hLine, 0.0);
            AnchorPane.setTopAnchor(vLine, 0.0);
            AnchorPane.setLeftAnchor(vLine, GRID_WIDTH * i);
            mapPane.getChildren().addAll(hLine, vLine);
        });

        mapPane.setOnMouseClicked(e -> {
            this.x = (int) (e.getX() / GRID_WIDTH);
            this.y = (int) (e.getY() / GRID_HEIGHT);
            logger.info(String.format("x: %d, y: %d", this.x, this.y));
        });

        setupCharacter(0, 0, 3, 4, 0);
        setupCharacter(1, 0, 3, 6, 1);
    }

    private void setupCharacter(int id, int dataId, int x, int y, int camp) {
        CharacterGrid character = new CharacterGrid(id, dataId, camp);
        character.move(x, y);
        character.setHp(1);
        character.setOnAction(e -> {
            switch (this.state) {
                case INIT:
                    this.source = id;
                    this.controlBox.setVisible(true);
                    this.moveBtn.setDisable(false);
                    this.skillBtn.setDisable(false);
                    this.submitBtn.setDisable(true);
                    break;
                case CAST_1:
                    break;
                default:
                    // do-nothing
                    break;
            }
        });
        character.appendTo(mapPane);
    }
}
