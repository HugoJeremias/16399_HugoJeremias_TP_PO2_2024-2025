// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import pt.ipbeja.estig.po2.snowman.model.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Represents the game board in the Snowman game.
 * It extends GridPane to create a grid layout for the game elements.
 * This class handles user input, and the visual representation of the game board.
 * It includes methods for loading levels, creating the game board, handling keyboard controls,
 * and generating visual elements like snowballs, monsters, and snowmen.
 */
public class GameBoard extends GridPane implements View {
    private GameModel gameModel;
    private static final int SIZE = 5, CELL_SIZE = 100;
    private final Label movesLabel, scoreLabel, leaderBoardLabel;
    private int lastRow = 2, lastCol = 2, movesCount = 0, level;
    private static final double smallSnowballSize = CELL_SIZE / 6.0,
                               averageSnowballSize = CELL_SIZE / 4.0,
                               bigSnowballSize = CELL_SIZE / 3.0;
    private Node[][] cells;
    private String playerName;

    /**
     * Constructor for the GameBoard class.
     * Initializes the game board with a moves, level, and score label.
     *
     * @param movesLabel the label to display the number of moves made
     * @param level the current level of the game
     * @param scoreLabel the label to display the score
     */
    public GameBoard(Label movesLabel, int level, Label scoreLabel, Label leaderBoardLabel, String playerName) {
        this.movesLabel = movesLabel;
        this.cells = new Node[SIZE][SIZE];
        this.level = level;
        this.playerName = playerName;
        this.gameModel = new GameModel(this, this.level, this.playerName);
        this.scoreLabel = scoreLabel;
        this.leaderBoardLabel = leaderBoardLabel;
        loadLevel();
        loadMusic();
        createNumberedGameBoard();
        createButtonBox();
        KeyboardControls();
        this.updateBoard();
    }

    /**
     * Loads the background music for the game.
     * The music is played in a loop indefinitely.
     */
    private void loadMusic() {
        String musicFile = getClass().getResource("/soundtrack.mp3").toExternalForm();
        Media media = new Media(musicFile);
        MediaPlayer mediaPlayer = new MediaPlayer(media);


        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    /**
     * Resets the visual labels and gameBoard for a new level.
     */
    public void loadLevel() {
        Position monsterPosition = this.getMonster().getPosition();
        this.getChildren().clear();
        this.movesLabel.setText("");
        this.movesCount = 0;
        this.lastRow = monsterPosition.getRow();
        this.lastCol = monsterPosition.getCol();
        this.leaderBoardLabel.setText(this.getTopFiveScores());
    }

    private void createButtonBox() {
        Button undoButton = new Button("⟲");
        Button redoButton = new Button("⟳");
        Button changeLevelButton = new Button("Mudar Nível");
        Button closeButton = new Button("Recomeçar Nível atual");

        // Add button actions
        undoButton.setOnAction(e -> this.undo());
        redoButton.setOnAction(e -> this.redo());
        changeLevelButton.setOnAction(e -> changeLevel());
        closeButton.setOnAction(e -> restartLevel());

        HBox buttonBox = new HBox(10, undoButton, redoButton, changeLevelButton, closeButton);
        buttonBox.setStyle("-fx-padding: 10; -fx-alignment: bottom-right;");
        this.getChildren().add(buttonBox);
    }

    /**
     * Restarts the current level by reinitializing the game model and updating the game board.
     * This method is called when the player chooses to restart the level.
     */
    private void restartLevel() {
        this.gameModel = new GameModel(this, this.level, this.playerName);
        this.cells = new Node[SIZE][SIZE];
        loadLevel();
        createNumberedGameBoard();
        createButtonBox();
        KeyboardControls();
        this.updateBoard();
    }

    /**
     * Undoes the last move made by the player.
     * This method interacts with the GameModel to revert the game state to the previous state.
     */
    @Override
    public void undo() {
        gameModel.undo();
        this.updateBoard();
        KeyboardControls();
    }

    /**
     * Redoes the last undone move made by the player.
     * This method interacts with the GameModel to restore the game state to the next state.
     */
    @Override
    public void redo() {
        gameModel.redo();
        this.updateBoard();
        KeyboardControls();
    }

    /**
     * Prompts the user to change the game level.
     * Displays a dialog with a choice box for selecting the level.
     * If the user confirms, it updates the game model and reloads the game board.
     */
    private void changeLevel() {
        Alert dialog = createLevelChangeDialog();
        Optional<ButtonType> result = dialog.showAndWait();
        handleLevelChangeResult(result, getLevelChoiceBox(dialog), dialog);
    }

    /**
     * Creates a dialog for changing the game level.
     * The dialog contains a choice box for selecting the level and buttons for confirmation.
     *
     * @return an Alert dialog for changing the level
     */
    private Alert createLevelChangeDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Mudar de nível");
        dialog.setHeaderText(null);

        ButtonType okButton = new ButtonType("Ok");
        ButtonType closeButton = new ButtonType("Fechar");
        dialog.getButtonTypes().setAll(okButton, closeButton);

        Label levelLabel = new Label("Nível:");
        ChoiceBox<Integer> levelChoice = new ChoiceBox<>();
        levelChoice.getItems().addAll(1, 2);
        levelChoice.setValue(1);

        VBox content = new VBox(levelLabel, levelChoice);
        dialog.getDialogPane().setContent(content);

        return dialog;
    }

    /**
     * Retrieves the choice box for selecting the level from the dialog.
     *
     * @param dialog the Alert dialog containing the choice box
     * @return the ChoiceBox for selecting the level
     */
    private ChoiceBox<Integer> getLevelChoiceBox(Alert dialog) {
        VBox content = (VBox) dialog.getDialogPane().getContent();
        return (ChoiceBox<Integer>) content.getChildren().get(1);
    }

    /**
     * Handles the result of the level change dialog.
     * If the user confirms, it updates the game model and reloads the game board.
     * If the user closes the dialog, it simply closes it without making changes.
     *
     * @param result the result of the dialog
     * @param levelChoice the choice box containing the selected level
     * @param dialog the Alert dialog
     */
    public void handleLevelChangeResult(Optional<ButtonType> result, javafx.scene.control.ChoiceBox<Integer> levelChoice, Alert dialog) {
        if(result.isPresent()) {
            String btn = result.get().getText();
            if(btn.equals("Ok")) {
                this.level = levelChoice.getValue();
                this.gameModel = new GameModel(this, this.level, this.playerName);
                this.cells = new Node[SIZE][SIZE];
                loadLevel();
                createNumberedGameBoard();
                createButtonBox();
                KeyboardControls();
                this.updateBoard();
            }
            else if(btn.equals("Fechar")) {
                dialog.close();
            }
        }
    }

    /**
     * Creates a labeled game board with labels for rows and columns.
     * Each cell is generated using the generateElement method.
     */
   private void createNumberedGameBoard() {
        GridPane numberedGrid = new GridPane();

        for (int col = 0; col < SIZE; col++) {
            Label colLabel = new Label(String.valueOf((char)('A' + col)));
            colLabel.setMinSize(CELL_SIZE, 20);
            colLabel.setAlignment(Pos.CENTER);
            numberedGrid.add(colLabel, col + 1, 0);
        }

        for (int row = 0; row < SIZE; row++) {
            Label rowLabel = new Label(String.valueOf(row + 1));
            rowLabel.setMinSize(20, CELL_SIZE);
            rowLabel.setAlignment(Pos.CENTER);
            numberedGrid.add(rowLabel, 0, row + 1);

            for (int col = 0; col < SIZE; col++) {
                Node cell = this.generateElement(row, col);
                numberedGrid.add(cell, col + 1, row + 1);
                cells[row][col] = cell;
            }
        }

        this.getChildren().add(numberedGrid);
    }

    /**
     * Sets up keyboard controls for the game board.
     * Allows the player to move the monster using arrow keys.
     */
    private void KeyboardControls() {
        this.setFocusTraversable(true);
        this.requestFocus();
        this.setOnKeyPressed(event -> {
            Direction direction = null;
            if (event.getCode() == KeyCode.UP) {
                direction = Direction.UP;
            } else if (event.getCode() == KeyCode.DOWN) {
                direction = Direction.DOWN;
            } else if (event.getCode() == KeyCode.LEFT) {
                direction = Direction.LEFT;
            } else if (event.getCode() == KeyCode.RIGHT) {
                direction = Direction.RIGHT;
            }
            if(direction != null) {
                try {
                    if (this.moveMonster(direction)) updateMovesInformation();
                } catch (IOException e) {
                    System.out.println("Invalid Input. Ignoring...");
                }
            }
        });
    }

    /**
     * Updates the moves information after each move.
     * Increments the moves count, updates the score label, and updates the board.
     */
    private void updateMovesInformation() {
        this.movesCount++;
        int newRow = this.getMonster().getPosition().getRow();
        int newCol = this.getMonster().getPosition().getCol();
        this.scoreLabel.setText("Pontuação: " + movesCount);
        this.movesLabel.setText(this.movesLabel.getText() + " " + this.updateMovesValue(this.lastRow, this.lastCol, newRow, newCol));
        this.lastRow = newRow;
        this.lastCol = newCol;
        this.updateBoard();
        if(movesCount == 100) {
            this.showGameCompleteMessage(false, null);
        }
        this.updateMovesCount(movesCount);
    }

    /**
     * Displays a message indicating whether the game is complete and if it was won or lost.
     * Provides options to restart the game, start a new level, or close the application.
     * Also creates a visual representation of the snowman if it exists in the game.
     *
     * @param gameWon indicates if the game was won
     * @param snowman the Snowman object if it exists, null otherwise
     */
    @Override
    public void showGameCompleteMessage(boolean gameWon, Snowman snowman) {
        if(snowman != null) {
            updateBoard();
            StackPane cell = (StackPane) cells[snowman.getPosition().getRow()][snowman.getPosition().getCol()];
            generateSnowman(cell);
        }
        String isTopScore = isTopScore(this.movesCount) ? " TOP!!" : "";
        Alert alert = new javafx.scene.control.Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Jogo Completo");
        alert.setHeaderText(null);
        ButtonType closeBtn = new ButtonType("Fechar");
        alert.getButtonTypes().setAll(closeBtn, new ButtonType("Recomeçar"), new ButtonType("Novo Nível"));
        alert.setContentText(gameWon ? "Vitória!\nPontuação: " + this.movesCount + " pontos" + isTopScore : "Game Over.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()) {
            String btn = result.get().getText();
            gameCompletionOption(btn);
        }
    }

    /**
     * Handles the options presented to the player after the game is completed.
     * Depending on the button clicked, it either restarts the game, starts a new level, or closes the application.
     *
     * @param btn the button type selected by the player
     */
    public void gameCompletionOption(String btn) {
        switch (btn) {
            case "Recomeçar" -> {
                this.gameModel = new GameModel(this, this.level, this.playerName);
                this.cells = new Node[SIZE][SIZE];
                loadLevel();
                createNumberedGameBoard();
                createButtonBox();
                KeyboardControls();
                this.updateBoard();
            }
            case "Novo Nível" -> {
                this.level++;
                this.gameModel = new GameModel(this, this.level, this.playerName);
                this.cells = new Node[SIZE][SIZE];
                loadLevel();
                createNumberedGameBoard();
                createButtonBox();
                KeyboardControls();
                this.updateBoard();
            }
            case "Fechar" -> javafx.application.Platform.exit();
        }
    }

    //region Visual MobileElement Generation

    /**
     * Generates a default type of the visual snowball object.
     * The snowball is represented as a Circle with a specific radius and color.
     * The default size is set to the small snowball size.
     *
     */
    private Circle defaultSnowball(){
        Circle circle = new Circle();
        circle.setRadius(smallSnowballSize);
        circle.setFill(Color.LIGHTBLUE);
        circle.setStroke(Color.BLACK);
        return circle;
    }

    /**
     * Generates a snowball of a specific type and adds it to the given cell.
     * The snowball can be of different sizes based on the SnowBallType.
     * If the type requires an additional stacked snowball, it generates it.
     *
     * @param type the type of snowball to generate
     * @param cell the cell where the snowball will be added
     */
    public void generateSnowBall(SnowBallType type, StackPane cell) {
        double aditionalRadius = 0;
        assert cell != null && type != null;
        Circle circle = defaultSnowball();
        switch (type) {
            case AVERAGE: {
                circle.setRadius(averageSnowballSize);
                break;
            }
            case BIG: {
                circle.setRadius(bigSnowballSize);
                break;
            }
            case BIG_AVERAGE: {
                circle.setRadius(bigSnowballSize);
                aditionalRadius = averageSnowballSize;
                break;
            }
            case BIG_SMAL: {
                circle.setRadius(bigSnowballSize);
                aditionalRadius = smallSnowballSize;
                break;
            }
            case AVERAGE_SMALL: {
                circle.setRadius(averageSnowballSize);
                aditionalRadius = smallSnowballSize;
                break;
            }
        }
        if(aditionalRadius > 0) {
            Circle aditional = defaultSnowball();
            aditional.setRadius(aditionalRadius);
            aditional.setTranslateY(-averageSnowballSize);
            cell.getChildren().addAll(circle, aditional);
        }
        else {
            cell.getChildren().add(circle);
        }
    }

    /**
     * Generates the visual monster object in the given cell.
     * The monster is represented as a red Circle.
     *
     * @param cell the cell where the monster will be added
     */
    private void generateMonster(StackPane cell) {
        assert cell != null;
        Circle monster = new Circle((double) CELL_SIZE / 4);
        monster.setFill(Color.RED);
        cell.getChildren().add(monster);
    }

    /**
     * Generates a snowman in the given cell.
     * The snowman is represented by three circles of different sizes stacked vertically.
     * The base circle is the largest, followed by a middle circle, and finally a top circle.
     *
     * @param cell the cell where the snowman will be added
     */
    public void generateSnowman(StackPane cell) {
        assert cell != null;
        Circle baseCircle = defaultSnowball();
        baseCircle.setRadius(bigSnowballSize);

        Circle middleCircle = defaultSnowball();
        middleCircle.setRadius(averageSnowballSize);
        middleCircle.setTranslateY(-averageSnowballSize);

        Circle topCircle = defaultSnowball();
        topCircle.setTranslateY(-(averageSnowballSize + smallSnowballSize));
        cell.getChildren().addAll(baseCircle, middleCircle, topCircle);
    }
    //endregion

    /**
     * Sets the background color of a cell based on the content type.
     * The cell can represent snow, no snow, or a block.
     *
     * @param cell the StackPane representing the cell
     * @param content the content type of the position
     */
    private void setCellBackground(StackPane cell, PositionContent content) {
        Rectangle background = new Rectangle(CELL_SIZE, CELL_SIZE);
        switch (content) {
            case SNOW:
                background.setFill(Color.WHITE);
                break;
            case BLOCK:
                background.setFill(Color.BLACK);
                break;
            default:
                background.setFill(Color.GRAY);
        }
        background.setStroke(Color.BLACK);
        cell.getChildren().add(background);
    }

    /**
     * Updates the visual game board by iterating through each cell,
     * clearing its contents, setting the background based on the position content,
     * and generating the visual elements like the monster, snowman, and snowballs.
     * This method is called to refresh the game board after any changes in the game state.
     *
     */
    @Override
    public void updateBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                StackPane cell = (StackPane) cells[i][j];
                cell.getChildren().clear();

                PositionContent content = this.getPositionContent(i, j);
                setCellBackground(cell, content);

                Position position = new Position(i, j);

                if (this.getMonster().getPosition().equals(position)) {
                    generateMonster(cell);
                }

                if(this.hasSnowman(position, CELL_SIZE)) {
                    generateSnowman(cell);
                }
                else
                {
                    for (SnowBall snowball : this.getSnowballs()) {
                        if (snowball.getPosition().equals(position)) {
                            generateSnowBall(snowball.getType(), cell);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the current monster in the game.
     *
     * @return the Monster object representing the monster in the game
     */
    @Override
    public Monster getMonster() {
        return gameModel.getMonster();
    }

    /**
     * Checks if there is a snowman at the specified position.
     *
     * @param position the position to check
     * @param CELL_SIZE the size of the cell in the game board
     * @return true if there is a snowman at the position, false otherwise
     */
    @Override
    public Boolean hasSnowman(Position position, double CELL_SIZE) {
        return gameModel.hasSnowman(position, CELL_SIZE);
    }

    /**
     * Returns the content of the position at the specified row and column.
     *
     * @param row the row index
     * @param col the column index
     * @return the content of the position
     */
    @Override
    public PositionContent getPositionContent(int row, int col) {
        return gameModel.getPositionContent(row, col);
    }

    /**
     * Returns a list of snowballs in the game.
     *
     * @return a list of SnowBall objects
     */
    @Override
    public List<SnowBall> getSnowballs() {
        return gameModel.getSnowballs();
    }

    /**
     * Generates a visual element for the game board at the specified row and column.
     * Each cell is represented as a StackPane containing a Rectangle background.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return a Node representing the visual element of the cell
     */
    @Override
    public Node generateElement(int row, int col) {
        StackPane cell = new StackPane();

        Rectangle background = new Rectangle(CELL_SIZE, CELL_SIZE);
        background.setFill(Color.WHITE);
        background.setStroke(Color.BLACK);

        cell.getChildren().add(background);

        return cell;
    }

    /**
     * Updates the moves value based on the last and new positions.
     * This method is called to keep track of the player's moves.
     *
     * @param lastRow the row of the last move
     * @param lastCol the column of the last move
     * @param newRow the row of the new move
     * @param newCol the column of the new move
     * @return a string representing the updated moves value
     */
    @Override
    public String updateMovesValue(int lastRow, int lastCol, int newRow, int newCol) {
        return gameModel.updateMovesValue(lastRow, lastCol, newRow, newCol);
    }

    /**
     * Moves the monster in the specified direction.
     * This method interacts with the GameModel to perform the move and update the game state.
     *
     * @param direction the direction in which to move the monster
     * @return true if the move was successful, false otherwise
     * @throws IOException if an error occurs during the move operation
     */
    @Override
    public Boolean moveMonster(Direction direction) throws IOException {
        return gameModel.moveMonster(direction);
    }

    /**
     * Updates the moves count in the game model.
     * This method is called to keep track of the number of moves made by the player.
     *
     * @param movesCount the number of moves made by the player
     */
    @Override
    public void updateMovesCount(int movesCount) {
        gameModel.setMovesCount(movesCount);
    }

    /**
     * Returns the top five scores for the current level from the game model.
     * This method retrieves the top scores and formats them for display.
     *
     * @return a string containing the top five scores
     */
    @Override
    public String getTopFiveScores() {
        return gameModel.getTopFiveScores();
    }

    /**
     * Checks if the current moves count is a top score.
     * This method interacts with the GameModel to determine if the moves count qualifies as a top score.
     *
     * @param movesCount the number of moves made by the player
     * @return true if the moves count is a top score, false otherwise
     */
    @Override
    public boolean isTopScore(int movesCount) {
        return gameModel.isTopScore(movesCount);
    }
}

