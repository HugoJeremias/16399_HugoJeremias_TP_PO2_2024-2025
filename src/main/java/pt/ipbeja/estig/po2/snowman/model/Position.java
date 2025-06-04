// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

/**
 * Represents a position on a grid in the game.
 * The position is defined by its row and column.
 */
public class Position {
    private int col;
    private int row; //nullable

    /**
     * Constructs a Position with the specified row and column.
     *
     * @param row the row of the position
     * @param col the column of the position
     */
    public Position(int row, int col) {
        this.col = col;
        this.row = row;
    }

    /**
     * Returns the column of the position.
     *
     * @return the column of the position
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the row of the position.
     *
     * @return the row of the position
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns a new Position that is offset from this position by the specified direction.
     *
     * @param direction the direction in which to offset the position
     * @return a new Position that is offset from this position
     */
    public Position getPositionAt(Direction direction) {
        return new Position(
                this.row + direction.getRowDelta(),
                this.col + direction.getColDelta()
        );
    }

    /**
     * Checks if this position is equal to another object.
     * Two positions are considered equal if they have the same row and column.
     *
     * @param positionToCompare the object to compare with
     * @return true if the positions are equal, false otherwise
     */
    @Override
    public boolean equals(Object positionToCompare) {
        if (this == positionToCompare) return true;
        if (positionToCompare == null || getClass() != positionToCompare.getClass()) return false;
        Position position = (Position) positionToCompare;
        return row == position.row && col == position.col;
    }

    /**
     * Returns a formatted string for this position.
     *
     * @return the formatted string representation of the position
     */
    @Override
    public String toString() {
        return  "row=" + row +
                ", col=" + col;
    }

}
