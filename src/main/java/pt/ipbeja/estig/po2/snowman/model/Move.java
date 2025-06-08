// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

import java.util.List;

/**
 * Represents a move in the Snowman game.
 * It contains information about the monster's new and old positions,
 * the old and new game boards, and the old and new snowballs.
 */
public class Move {
    private final Position newMonsterPosition;
    private final Position oldMonsterPosition;
    private final List<List<PositionContent>> oldBoard;
    private final List<List<PositionContent>> newBoard;
    private final List<SnowBall> oldSnowballs;
    private final List<SnowBall> newSnowballs;

    /**
     * Constructs a Move object with the specified parameters.
     *
     * @param newMonsterPosition the new position of the monster
     * @param oldMonsterPosition the old position of the monster
     * @param oldBoard the old game board
     * @param newBoard the new game board
     * @param oldSnowballs the list of old snowballs
     * @param newSnowballs the list of new snowballs
     */
    public Move(Position newMonsterPosition, Position oldMonsterPosition,
                List<List<PositionContent>> oldBoard, List<List<PositionContent>> newBoard,
                List<SnowBall> oldSnowballs, List<SnowBall> newSnowballs) {
        this.newMonsterPosition = newMonsterPosition;
        this.oldMonsterPosition = oldMonsterPosition;
        this.oldBoard = oldBoard;
        this.newBoard = newBoard;
        this.oldSnowballs = oldSnowballs;
        this.newSnowballs = newSnowballs;
    }

    /**
     * Returns the new position of the monster.
     *
     * @return the new monster position
     */
    public Position getNewMonsterPosition() {
        return newMonsterPosition;
    }

    /**
     * Returns the old position of the monster.
     *
     * @return the old monster position
     */
    public Position getOldMonsterPosition() {
        return oldMonsterPosition;
    }

    /**
     * Returns the old game board.
     *
     * @return the old board
     */
    public List<List<PositionContent>>  getOldBoard() {
        return oldBoard;
    }

    /**
     * Returns the new game board.
     *
     * @return the new board
     */
    public List<List<PositionContent>>  getNewBoard() {
        return newBoard;
    }

    /**
     * Returns the list of old snowballs.
     *
     * @return the old snowballs
     */
    public List<SnowBall> getOldSnowballs() {
        return oldSnowballs;
    }

    /**
     * Returns the list of new snowballs.
     *
     * @return the new snowballs
     */
    public List<SnowBall> getNewSnowballs() {
        return newSnowballs;
    }
}
