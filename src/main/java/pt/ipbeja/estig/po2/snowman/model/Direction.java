// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

/**
 * Represents the four possible directions in which a mobile element can move.
 * Each direction is defined by the difference in position from the given direction enumeration, or delta.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int rowDelta;
    private final int colDelta;

    Direction(int rowDelta, int colDelta) {
        this.rowDelta = rowDelta;
        this.colDelta = colDelta;
    }

    /**
     * Returns the change in row for this direction.
     *
     * @return the row delta
     */
    public int getRowDelta() {
        return rowDelta;
    }

    /**
     * Returns the change in column for this direction.
     *
     * @return the column delta
     */
    public int getColDelta() {
        return colDelta;
    }
}