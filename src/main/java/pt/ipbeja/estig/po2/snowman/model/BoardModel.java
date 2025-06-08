// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the board model in the Snowman game.
 * It manages the positions of the monster, snowballs, and snowmen,
 * and provides methods to load levels, check positions, and update the board.
 */
public class BoardModel {
    private Monster monster;
    private List<SnowBall> snowballs;
    private List<Snowman> snowmen;
    private List<List<PositionContent>> board;

    /**
     * Constructs a BoardModel with an empty board and no snowballs or snowmen.
     */
    public BoardModel() {}

    /**
     * Loads a level from a file and initializes the board, snowballs, and monster.
     *
     * @param filename the name of the file containing the level data
     * @throws IOException if an error occurs while reading the file
     */
    public void loadLevelFromFile(String filename) throws IOException {
        List<List<PositionContent>> grid = new ArrayList<>();
        List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(filename));
        this.snowballs = new ArrayList<>();
        this.snowmen = new ArrayList<>();
        int rowIdx = 0;

        for (String line : lines) {
            grid.add(getRowsForGrid(line, rowIdx));
            rowIdx++;
        }
        this.board = grid;
    }

    /**
     * Parses a line from the level file and creates a list of PositionContent for that row.
     *
     * @param line the line from the level file
     * @param rowIdx the index of the row in the grid
     * @return a list of PositionContent representing the row
     */
    private List<PositionContent> getRowsForGrid(String line, int rowIdx) {
            List<PositionContent> row = new ArrayList<>();
            String[] tokens = line.split(" ");
            for (int colIdx = 0; colIdx < tokens.length; colIdx++) {
                String token = tokens[colIdx];
                switch (token) {
                    case ".":
                        row.add(PositionContent.NO_SNOW);
                        break;
                    case "*":
                        row.add(PositionContent.SNOW);
                        break;
                    case "#":
                        row.add(PositionContent.BLOCK);
                        break;
                    case "S":
                        row.add(PositionContent.NO_SNOW);
                        this.snowballs.add(new SnowBall(new Position(rowIdx, colIdx), SnowBallType.SMALL));
                        break;
                    case "X":
                        row.add(PositionContent.NO_SNOW);
                        this.monster = new Monster(new Position(rowIdx, colIdx));
                        break;
                    case "A":
                        row.add(PositionContent.NO_SNOW);
                        this.snowballs.add(new SnowBall(new Position(rowIdx, colIdx), SnowBallType.AVERAGE));
                        break;
                    case "B":
                        row.add(PositionContent.NO_SNOW);
                        this.snowballs.add(new SnowBall(new Position(rowIdx, colIdx), SnowBallType.BIG));
                        break;
                    default:
                        row.add(PositionContent.SNOW);
                }
            }
            return row;
    }

    /**
     * Returns the content of the position at the specified row and column.
     *
     * @param row the row index
     * @param col the column index
     * @return the content of the position, or BLOCK if the position is out of bounds
     */
    public PositionContent getPositionContent(int row, int col) {
        if (isOutOfBounds(row, col)) {
            System.out.println("Invalid position");
            return PositionContent.BLOCK;
        }
        return this.board.get(row).get(col);
    }

    /**
     * Returns the current List<List<PositionContent>> representing the game board.
     *
     * @return the current List<List<PositionContent>>.
     */
    public List<List<PositionContent>> getBoard() {
        return this.board;
    }
    /**
     * Sets the game board to a new List<List<PositionContent>>.
     *
     * @param board the new List<List<PositionContent>> to set as the game board.
     */
    public void setBoard(List<List<PositionContent>> board) {
        this.board = board;
    }

    /**
     * Checks if the specified row and column are out of bounds of the board.
     *
     * @param row the row index
     * @param col the column index
     * @return true if the position is out of bounds, false otherwise
     */
    private boolean isOutOfBounds(int row, int col) {
        return row < 0 || row >= board.size() || col < 0 || col >= board.get(0).size();
    }

    /**
     * Sets the content of the position at the specified row and column.
     *
     * @param row the row index
     * @param col the column index
     * @param content the content to set at the position
     */
    public void setPositionContent(int row, int col, PositionContent content) {
        if (!isOutOfBounds(row, col)) {
            board.get(row).set(col, content);
        }
    }

    /**
     * Checks if the specified position is valid (not out of bounds and not a block).
     *
     * @param position the position to check
     * @return true if the position is valid, false otherwise
     */
    public boolean isValidPosition(Position position) {
        int row = position.getRow();
        int col = position.getCol();
        return !isOutOfBounds(row, col) || !isBlock(row, col);
    }

    /**
     * Checks if the specified position is a block.
     *
     * @param row the row index
     * @param col the column index
     * @return true if the position is a block, false otherwise
     */
    private boolean isBlock(int row, int col) {
        PositionContent content = getPositionContent(row, col);
        return content == PositionContent.BLOCK;
    }

    /**
     * Returns the current monster in the game.
     *
     * @return the monster
     */
    public Monster getMonster() {
        return monster;
    }

    /**
     * Returns the snowballs in the game.
     *
     * @return the list of snowballs in the game
     */
    public List<SnowBall> getSnowballs() {
        return snowballs;
    }

    /**
     * returns the fully built snowmen int the game
     *
     * @return the list of snowmen.
     */
    public List<Snowman> getSnowmen() {
        return new ArrayList<>(this.snowmen);
    }

    /**
     * Updates the game board by clearing snowballs and updating snowmen positions.
     */
    public void updateBoard() {
        clearSnowballs();
        updateSnowmen();
    }

    /**
     * Clears all snowballs from the board.
     * It sets the position content of each snowball's position to NO_SNOW.
     */
    private void clearSnowballs() {
        for (SnowBall snowball : snowballs) {
            Position pos = snowball.getPosition();
            setPositionContent(pos.getRow(), pos.getCol(), PositionContent.NO_SNOW);
        }
    }

    /**
     * Updates the positions of all snowmen on the board.
     * It sets the position content of each snowman's position to SNOWMAN.
     */
    private void updateSnowmen() {
        for (Snowman snowman : snowmen) {
            Position pos = snowman.getPosition();
            setPositionContent(pos.getRow(), pos.getCol(), PositionContent.SNOWMAN);
        }
    }

    /**
     * Returns the snowball at the specified position, if it exists.
     *
     * @param position the position to check for a snowball
     * @return the SnowBall at the position, or null if no snowball is found
     */
    public SnowBall getSnowballAt(Position position) {
        for (SnowBall snowball : snowballs) {
            if (snowball.getPosition().equals(position)) {
                return snowball;
            }
        }
        return null;
    }

    /**
     * Checks if there is a snowball at the specified position.
     *
     * @param position the position to check
     * @return true if there is a snowball at the position, false otherwise
     */
    public boolean hasSnowballAt(Position position) { return getSnowballAt(position) != null; }

    /**
     * Checks if the specified position is empty (not occupied by a monster, snowball, or snowman).
     *
     * @param position the position to check
     * @return true if the position is empty, false otherwise
     */
    public boolean isPositionEmpty(Position position) {
        if (!isValidPosition(position)) return false;
        if (monster.getPosition().equals(position)) return false;
        if (hasSnowballAt(position)) return false;
        for (Snowman snowman : snowmen) {
            if (snowman.getPosition().equals(position)) return false;
        }
        return true;
    }

    /**
     * Checks if there is a snowman at the specified position.
     * if the base is valid, it checks if there is a small snowball on top of it.
     * it creates a snowman if both conditions are met.
     */
    public void checkForSnowman() {
        for (SnowBall base : new ArrayList<>(snowballs)) {
            if (base.getType() != SnowBallType.BIG_AVERAGE) continue;
            SnowBall top = findSmallBallOnBase(base);
            if (top != null) {
                createSnowman(base, top);
            }
        }
    }

    /**
     * Finds a small snowball on top of the specified base snowball.
     *
     * @param base the base snowball to check for a small snowball
     * @return the small snowball on top of the base, or null if not found
     */
    private SnowBall findSmallBallOnBase(SnowBall base) {
        for (SnowBall sb : snowballs) {
            if (sb != base && sb.getType() == SnowBallType.SMALL && sb.getPosition().equals(base.getPosition())) {
                return sb;
            }
        }
        return null;
    }

    /**
     * Creates a snowman using the specified base and top snowballs.
     *
     * @param base the base snowball of the snowman
     * @param top the top snowball of the snowman
     */
    private void createSnowman(SnowBall base, SnowBall top) {
        Snowman snowman = new Snowman(base.getPosition(), base, null, top);
        snowmen.add(snowman);
        snowballs.remove(base);
        snowballs.remove(top);
        updateBoard();
    }

    /**
     * Sets the all the snowballs in the game.
     *
     */
    public void setSnowballs(List<SnowBall> snowballs) {
        this.snowballs = new ArrayList<>(snowballs);
    }

    /**
     * Returns a deep copy of the current game board.
     * This is useful for undo functionality or saving the game state.
     *
     * @return a deep copy of the board as a List of Lists of PositionContent
     */
    public List<List<PositionContent>> deepCopyBoard() {
        List<List<PositionContent>> copy = new ArrayList<>();
        for (List<PositionContent> row : this.board) {
            List<PositionContent> rowCopy = new ArrayList<>(row);
            copy.add(rowCopy);
        }
        return copy;
    }

}