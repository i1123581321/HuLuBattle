package hulubattle.client.view;

import hulubattle.client.controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class CharacterGrid {
    private VBox box = new VBox();
    private Button button = new Button();
    private ImageView image;
    private ProgressBar hpBar;

    private int id;
    private int dataId;
    private int camp;

    public CharacterGrid(int id, int dataId, int camp) {
        this.id = id;
        this.dataId = dataId;
        this.camp = camp;

        box.setMaxSize(MainViewController.GRID_WIDTH, MainViewController.GRID_HEIGHT);
        box.setMinSize(MainViewController.GRID_WIDTH, MainViewController.GRID_HEIGHT);
        box.setAlignment(Pos.CENTER);

        button.setMinSize(MainViewController.GRID_WIDTH, MainViewController.GRID_HEIGHT - 10);
        button.setMaxSize(MainViewController.GRID_WIDTH, MainViewController.GRID_HEIGHT - 10);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        image = new ImageView(
                getClass().getClassLoader().getResource(String.format("image/%d%d.png", camp, dataId)).toString());
        image.setFitHeight(MainViewController.GRID_HEIGHT - 10.0);
        image.setFitWidth(MainViewController.GRID_WIDTH - 10.0);
        image.setPreserveRatio(true);
        image.setCache(true);

        button.setGraphic(image);

        hpBar = new ProgressBar();
        hpBar.setMinSize(MainViewController.GRID_WIDTH - 15.0, 10.0);
        hpBar.setMaxSize(MainViewController.GRID_WIDTH - 15.0, 10.0);
        hpBar.setStyle("-fx-accent: green;");
        
        box.getChildren().addAll(button, hpBar);
    }

    public void move(int x, int y) {
        AnchorPane.setLeftAnchor(box, x * MainViewController.GRID_WIDTH);
        AnchorPane.setTopAnchor(box, y * MainViewController.GRID_HEIGHT);
    }

    public void setHp(double percentage) {
        hpBar.setProgress(percentage);
    }

    public void appendTo(AnchorPane pane) {
        pane.getChildren().add(box);
    }

    public void removeFrom(AnchorPane pane) {
        pane.getChildren().remove(this.box);
    }

    /**
     * @return the dataId
     */
    public int getDataId() {
        return dataId;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the camp
     */
    public int getCamp() {
        return camp;
    }

    /**
     * @param value
     * @see javafx.scene.control.ButtonBase#setOnAction(javafx.event.EventHandler)
     */

    public final void setOnAction(EventHandler<ActionEvent> value) {
        button.setOnAction(value);
    }

    /**
     * @param value
     * @see javafx.scene.Node#setDisable(boolean)
     */

    public final void setDisable(boolean value) {
        button.setDisable(value);
    }
}
