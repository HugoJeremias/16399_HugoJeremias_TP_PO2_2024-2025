// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

/**
 * Enumeration representing the different types of snowballs in the game.
 * Each type has a size associated with it, which is used to determine
 * the order of the snowballs when creating a snowman.
 */
public enum SnowBallType {
    BIG(6),
    AVERAGE(5),
    SMALL(4),
    BIG_AVERAGE(3),
    BIG_SMAL(2),
    AVERAGE_SMALL(1);

    private final int size;

    /**
     * Constructs a SnowBallType with the specified size.
     *
     * @param size the size of the snowball type
     */
    SnowBallType(int size) {
        assert size > 0 : "Size must be positive";
        this.size = size;
    }

    /**
     * Compares this snowball type with another to determine if it is smaller.
     *
     * @param other the other snowball type to compare with
     * @return true if this snowball type is smaller than the other, false otherwise
     */
    public boolean isSmallerThan(SnowBallType other) {
        return this.size < other.size;
    }

}
