// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

/**
 * Represents a monster in the game.
 * The monster is controlled by the player and can interact with snowballs in order to create a snowman.
 */
public class Monster extends MobileElement{

    /**
     * Constructs a Monster at the specified position.
     *
     * @param position the initial position of the monster
     */
    public Monster(Position position) {
        super(position);
    }
}
