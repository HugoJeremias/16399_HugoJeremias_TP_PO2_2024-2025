// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
    private Position monsterPosition = null;
    private static final int SIZE = 5, CELL_SIZE = 100;
    private final Label movesLabel, scoreLabel;
    private int lastRow = 2, lastCol = 2, movesCount = 0, level = 1;
    private static final double smallSnowballSize = CELL_SIZE / 6.0,
                               averageSnowballSize = CELL_SIZE / 4.0,
                               bigSnowballSize = CELL_SIZE / 3.0;
    private double aditionalRadius = 0;
    private Node[][] cells;

    /**
     * Constructor for the GameBoard class.
     * Initializes the game board with a moves, level, and score label.
     *
     * @param movesLabel the label to display the number of moves made
     * @param level the current level of the game
     * @param scoreLabel the label to display the score
     */
    public GameBoard(Label movesLabel, int level, Label scoreLabel) {
        this.movesLabel = movesLabel;
        this.gameModel = new GameModel(this);
        this.cells = new Node[SIZE][SIZE];
        this.level = level;
        this.scoreLabel = scoreLabel;
        loadLevel();
        createNumberedGameBoard();
        setupKeyboardControls();
        this.updateBoard();
    }

    /**
     * Resets the visual labels and gameBoard for a new level.
     */
    public void loadLevel() {
        this.monsterPosition = this.getMonster().getPosition();
        this.getChildren().clear();
        this.movesLabel.setText("");
        this.movesCount = 0;
        this.lastRow = monsterPosition.getRow();
        this.lastCol = monsterPosition.getCol();
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
    private void setupKeyboardControls() {
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
            assert direction != null;
            try {
                this.moveMonster(direction);
            } catch (IOException e) {
                e.printStackTrace();
            }

            updateMovesInformation();

        });
    }

    /**
     * Updates the moves information after each move.
     * Increments the moves count, updates the score label, and updates the board.
     */
    private void updateMovesInformation() {
        movesCount++;
        int newRow = this.getMonster().getPosition().getRow();
        int newCol = this.getMonster().getPosition().getCol();
        this.updateMovesValue(lastRow, lastCol, newRow, newCol);
        this.scoreLabel.setText("Pontuação: " + (100 - movesCount));
        movesLabel.setText(movesLabel.getText() + " " + this.updateMovesValue(lastRow, lastCol, newRow, newCol));
        this.lastRow = newRow;
        this.lastCol = newCol;
        updateBoard();
        if(movesCount == 100) {
            showGameCompleteMessage(false, null);
        }
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
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Jogo Completo");
        alert.setHeaderText(null);
        ButtonType closeBtn = new ButtonType("Fechar");
        alert.getButtonTypes().setAll(closeBtn, new ButtonType("Recomeçar"), new ButtonType("Novo Nível"));
        alert.setContentText(gameWon ? "Vitória!\nPontuação: " + (100 - this.movesCount) + " pontos" : "Game Over.");
        Optional<ButtonType> result = alert.showAndWait();
        gameCompletionOption(result);
    }

    /**
     * Handles the options presented to the player after the game is completed.
     * Depending on the button clicked, it either restarts the game, starts a new level, or closes the application.
     *
     * @param result the button type selected by the player
     */
    public void gameCompletionOption(Optional<ButtonType> result) {
        assert result.isPresent();
        String btn = result.get().getText();
        switch (btn) {
            case "Recomeçar" -> loadLevel();
            case "Novo Nível" -> {
                this.level++;
                this.gameModel = new GameModel(this, this.level);
                this.cells = new Node[SIZE][SIZE];
                loadLevel();
                createNumberedGameBoard();
                setupKeyboardControls();
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
        this.aditionalRadius = 0;
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
        if(this.aditionalRadius > 0) {
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
            case NO_SNOW:
                background.setFill(Color.GRAY);
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
}

