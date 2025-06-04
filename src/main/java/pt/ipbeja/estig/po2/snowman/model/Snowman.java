// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;
/**
 * Represents a snowman in the game
 * A snowman is composed of three snowballs: base, middle, and top.
 * The snowman is valid if the snowballs are of the correct types:
 * - Base: BIG or BIG_AVERAGE
 * - Middle: AVERAGE or empty
 * - Top: SMALL or AVERAGE_SMALL
 * If the snowman is valid, the level ends.
 */
public class Snowman {
    private final Position position;
    private final SnowBall base;
    private final SnowBall middle;
    private final SnowBall top;

    /**
     * Constructs a Snowman at the specified position with the given snowballs.
     *
     * @param position the position of the snowman
     * @param base the base snowball
     * @param middle the middle snowball
     * @param top the top snowball
     */
    public Snowman(Position position, SnowBall base, SnowBall middle, SnowBall top) {
        this.position = position;
        this.base = base;
        this.middle = middle;
        this.top = top;
    }

    /**
     * Returns the position of the snowman.
     *
     * @return the position of the snowman
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Checks whether a valid snowman has been created.
     *
     * @return true if the snowman is valid, false otherwise
     */
    public boolean isValid() {
        return (base.getType() == SnowBallType.BIG &&
               middle.getType() == SnowBallType.AVERAGE &&
               top.getType() == SnowBallType.SMALL) ||
                (base.getType() == SnowBallType.BIG_AVERAGE &&
                top.getType() == SnowBallType.SMALL) ||
                (base.getType() == SnowBallType.BIG &&
                top.getType() == SnowBallType.AVERAGE_SMALL);
    }
}