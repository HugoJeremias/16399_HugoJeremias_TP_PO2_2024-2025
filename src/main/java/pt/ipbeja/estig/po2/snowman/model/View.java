// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

import javafx.scene.Node;

import java.io.IOException;
import java.util.List;

/**
 * Interface representing the view in the Snowman game.
 * It defines methods for generating UI elements, updating the game board,
 * showing messages, and interacting with game elements like the monster and snowballs.
 */
public interface View {
    /**
     * Generates a Node representing the game board.
     *
     * @param row  the row index of the board
     * @param col  the column index of the board
     * @return returns the Node representing the game board element
     */
    Node generateElement(int row, int col);
    /**
     * Updates the game board.
     */
    void updateBoard();
    /**
     * Shows a message indicating whether the game is complete and if it was won or lost.
     *
     * @param gameWon indicates if the game was won
     * @param snowman the Snowman object representing the snowman in the game
     */
    void showGameCompleteMessage(boolean gameWon, Snowman snowman);
    /**
     * Returns the current monster in the game.
     *
     * @return the monster
     */
    Monster getMonster();
    /**
     * Checks if there is a snowman at the specified position.
     * @param pos the position to check
     * @param cellSize the size of the cell in the game board
     * @return true if there is a snowman at the position, false otherwise
     */
    Boolean hasSnowman(Position pos, double cellSize);
    /**
     * Returns the positionContent at the specified row and column.
     *
     * @param row the row index
     * @param col the column index
     * @return the content of the position
     */
    PositionContent getPositionContent(int row, int col);
    /**
     * Returns a list of snowballs in the game.
     *
     * @return a list of SnowBall objects
     */
    List<SnowBall> getSnowballs();
    /**
     * Updates the string that shows the moves the player has played.
     *
     * @param lastRow The row of the previous move
     * @param lastCol The column of the previous move
     * @param newRow The row of the current move
     * @param newCol The column of the current move
     * @return a string representing the updated moves
     */
    String updateMovesValue(int lastRow, int lastCol, int newRow, int newCol);
    /**
     * Moves the monster in the specified direction.
     *
     * @param direction the direction to move the monster
     * @return true if the monster was moved successfully, false otherwise
     * @throws IOException if an I/O error occurs
     */
    Boolean moveMonster(Direction direction) throws IOException;
    /**
     * Updates the number of moves made by the player.
     */
    void updateMovesCount(int movesCount);

    /**
     * Returns the top five scores in the game.
     *
     */
    String getTopFiveScores();
    /**
     * Undoes the last action in the game.
     */
    void undo();
    /**
     * Redoes the last undone action in the game.
     */
    void redo();
    /**
     * Checks if the current number of moves is a top score.
     *
     * @param movesCount the number of moves to check
     * @return true if the moves count is a top score, false otherwise
     */
    boolean isTopScore(int movesCount);

}
