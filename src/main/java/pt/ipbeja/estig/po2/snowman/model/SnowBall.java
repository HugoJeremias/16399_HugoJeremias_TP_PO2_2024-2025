// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

/**
 * Represents a snowball in the game.
 * Snowballs can be of different types and can grow in size.
 */
public class SnowBall extends MobileElement{
    private SnowBallType type;

    /**
     * Constructs a SnowBall at the specified position with the given type.
     *
     * @param position the initial position of the snowball
     * @param type the type of the snowball (SMALL, AVERAGE, BIG)
     */
    public SnowBall(Position position, SnowBallType type) {
        super(position);
        this.type = type;
    }

    /**
     * Returns the type of the snowball.
     *
     * @return the type of the snowball
     */
    public SnowBallType getType() {
        return type;
    }

    /**
     * Converts a snowball into a bigger snowball, unless it is already at max size.
     */
    public void grow() {
        switch (this.type) {
            case SMALL:
                this.type = SnowBallType.AVERAGE;
                break;
            case AVERAGE:
                this.type = SnowBallType.BIG;
                break;
            case BIG:
                break;
        }
    }

    /**
     * Sets the type of the snowball.
     *
     * @param snowBallType the new type of the snowball
     */
    public void setType(SnowBallType snowBallType) {
        this.type = snowBallType;
    }
}
