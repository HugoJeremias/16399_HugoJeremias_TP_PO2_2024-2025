// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

/**
 * Represents a mobile element in the game, such as a snowball or a monster.
 * This class provides methods to get and set the position of the element,
 * as well as to move it in a specified direction.
 */
public abstract class MobileElement {
    private Position position;

    /**
     * Constructs a MobileElement with the specified position.
     *
     * @param position the initial position of the mobile element
     */
    public MobileElement(Position position) {
        this.position = position;
    }

    /**
     * Returns the current position of the mobile element.
     *
     * @return the position of the mobile element
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of the mobile element to a new position.
     *
     * @param position the new position to set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Moves the mobile element in the specified direction.
     *
     * @param direction the direction in which to move the element
     */
    public void move(Direction direction) {
        this.position = this.position.getPositionAt(direction);
    }

}
