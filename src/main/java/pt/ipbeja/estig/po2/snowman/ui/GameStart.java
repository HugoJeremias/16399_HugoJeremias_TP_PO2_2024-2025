// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;


/**
 * Main class to start the Snowman game application.
 * It initializes the game configuration, creates the game board,
 * and sets up the user interface.
 */
public class GameStart extends Application {
    /**
     * Starts the JavaFX application.
     * It shows a configuration dialog to get the player's name, level, and level style,
     * then initializes the game board based on the selected options.
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        GameConfig config = showConfigDialog();
        String playerName = config.getPlayerName();
        int level = config.getLevel();
        String levelStyle = config.getLevelStyle();

        VBox infoPane = createInfoPane(playerName);
        ScrollPane scrollPane = createMovesPane();
        Label movesLabel = (Label) ((VBox) scrollPane.getContent()).getChildren().get(0);
        Label scoreLabel = (Label) infoPane.getChildren().get(1);
        Label leaderBoardLabel = (Label) infoPane.getChildren().get(2);

        if(levelStyle == null || levelStyle.equals("Formas")) {
            GameBoard gameBoard = new GameBoard(movesLabel, level, scoreLabel, leaderBoardLabel, playerName);
            showGameBoard(gameBoard, infoPane, scrollPane, stage);
        }
        else {
            ImageGameBoard gameBoard = new ImageGameBoard(movesLabel, level, scoreLabel, leaderBoardLabel, playerName);
            showImageGameBoard(gameBoard, infoPane, scrollPane, stage);
        }
    }

    /**
     * Displays the game board with the specified configuration.
     * It sets up the layout with the game board on the left and the info pane on the right.
     *
     * @param gameBoard the game board to display
     * @param infoPane the pane containing player information and game stats
     * @param scrollPane the scroll pane for displaying moves
     * @param stage the primary stage for this application
     */
    private void showImageGameBoard(ImageGameBoard gameBoard, VBox infoPane, ScrollPane scrollPane, Stage stage) {

        VBox leftPane = new VBox(gameBoard, scrollPane);
        leftPane.setMaxWidth(400);

        HBox root = new HBox(10, leftPane, infoPane);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("A Good Snowman is Hard to Build");
        stage.show();
        gameBoard.requestFocus();
    }

    /**
     * Displays the game board with the specified configuration.
     * It sets up the layout with the game board on the left and the info pane on the right.
     *
     * @param gameBoard the game board to display
     * @param infoPane the pane containing player information and game stats
     * @param scrollPane the scroll pane for displaying moves
     * @param stage the primary stage for this application
     */
    private void showGameBoard(GameBoard gameBoard, VBox infoPane, ScrollPane scrollPane, Stage stage) {
        VBox leftPane = new VBox(gameBoard, scrollPane);
        leftPane.setMaxWidth(400);

        HBox root = new HBox(10, leftPane, infoPane);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("A Good Snowman is Hard to Build");
        stage.show();
        gameBoard.requestFocus();
    }

    /**
     * Shows a dialog to configure the game settings.
     * It allows the player to enter their name, select a level, and choose a design style.
     *
     * @return a GameConfig object containing the player's name, level, and design style
     */
    private GameConfig showConfigDialog() {
        Dialog<GameConfig> dialog = new Dialog<>();
        dialog.setTitle("Configuração do Jogo");

        Label nameLabel = new Label("Nome:");
        TextField nameField = new TextField();
        nameField.setPromptText("Insira o seu nome");
        final int MAX_CHARS = 3;
        nameField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > MAX_CHARS) {
                nameField.setText(oldText);
            }
        });

        Label levelLabel = new Label("Nível:");
        javafx.scene.control.ChoiceBox<Integer> levelChoice = new javafx.scene.control.ChoiceBox<>();
        levelChoice.getItems().addAll(1, 2);
        levelChoice.setValue(1);

        Label levelDesignLabel = new Label("Estilo:");
        javafx.scene.control.ChoiceBox<String> designChoice = new javafx.scene.control.ChoiceBox<>();
        designChoice.getItems().addAll("Formas", "Imagens");
        designChoice.setValue("Formas");

        VBox dialogContent = new VBox(10, nameLabel, nameField, levelLabel, levelChoice, levelDesignLabel, designChoice );
        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);

        dialog.setResultConverter(btn -> {
            if (btn == javafx.scene.control.ButtonType.OK) {
                return new GameConfig(
                        levelChoice.getValue(),
                        nameField.getText().isEmpty() ? "Jogador" : nameField.getText(),
                        designChoice.getValue()
                );
            }
            return null;
        });

        Optional<GameConfig> result = dialog.showAndWait();
        return result.orElse(new GameConfig(1, "Jogador", "Formas"));

    }

    /**
     * Creates a pane to display player information, score, and game legend.
     *
     * @param playerName the name of the player
     * @return a VBox containing the player information and game legend
     */
    private VBox createInfoPane(String playerName) {
        Label playerNameLabel = new Label("Jogador: " + playerName);
        Label scoreLabel = new Label("Pontuação: ");
        Label leaderboardLabel = new Label("Pontuações: ");
        VBox legendBox = new VBox(5);
        legendBox.getChildren().add(new Label("Legenda:"));
        HBox snowballLegend = new HBox(5, new Circle(8, javafx.scene.paint.Color.LIGHTBLUE), new Label("Bola de neve pequena"));
        HBox monsterLegend = new HBox(5, new Circle(8, javafx.scene.paint.Color.RED), new Label("Monstro"));
        HBox snowLegend = new HBox(5, new Rectangle(16, 16, javafx.scene.paint.Color.WHITE), new Label("Neve"));
        HBox blockLegend = new HBox(5, new Rectangle(16, 16, javafx.scene.paint.Color.BLACK), new Label("Bloco"));
        HBox arrowsLegend = new HBox(5, new Label("← ↑ → ↓"), new Label("Mover personagem"));
        HBox goalLegend = new HBox(5, new Label("🎯"), new Label("Construir o boneco de neve!"));
        legendBox.getChildren().addAll(snowballLegend, monsterLegend, snowLegend, blockLegend, arrowsLegend, goalLegend);
        VBox infoPane = new VBox(10, playerNameLabel, scoreLabel, leaderboardLabel, legendBox);
        infoPane.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");
        return infoPane;
    }

    /**
     * Creates a scroll pane to display the moves made by the player.
     * It contains a label that will be updated with the moves.
     *
     * @return a ScrollPane containing the moves label
     */
    private ScrollPane createMovesPane() {
        Label movesLabel = new Label("Jogadas:");
        movesLabel.setWrapText(true);
        VBox contentBox = new VBox(movesLabel);
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(100);
        scrollPane.setMaxWidth(550);
        return scrollPane;
    }

    /**
     * Main method to launch the JavaFX application.
     * It initializes the JavaFX runtime and starts the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch();
    }

}
