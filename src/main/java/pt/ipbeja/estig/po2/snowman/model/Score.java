// Autor : 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a player's score in the Snowman game.
 * It contains the player's name, level, moves count, and the date and time of the score.
 * The score can be compared with other scores based on the number of moves.
 */
public class Score {
    private String playerName;
    private int level;
    private int movesCount;
    private String dateTime;

    /**
     * Constructs a Score object with the specified player name, level, and moves count.
     * The date and time are set to the current time in the format "yyyyMMddHHmmss".
     *
     * @param playerName the name of the player
     * @param level the level of the game
     * @param movesCount the number of moves made by the player
     */
    public Score(String playerName, int level, int movesCount) {
        this.playerName = playerName;
        this.level = level;
        this.movesCount = movesCount;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.dateTime = LocalDateTime.now().format(formatter);
    }

    /**
     * Returns the name of the player.
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the level of the game.
     * @return the game level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the number of moves made by the player.
     * @return the moves count
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * Returns the date and time of the score in the format "yyyyMMddHHmmss".
     * If the dateTime is not set, it initializes it to the current time.
     * @return the date and time of the score
     */
    public String getDateTime() {
        if (dateTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            this.dateTime = LocalDateTime.now().format(formatter);
        }
        return dateTime;
    }

    /**
     * Compares this score with another score based on the number of moves.
     * Returns a negative integer, zero, or a positive integer as this score
     * has fewer, equal to, or more moves than the specified score.
     *
     * @param other the score to compare with
     * @return a negative integer, zero, or a positive integer as this score
     *         has fewer, equal to, or more moves than the specified score
     */
    public int compareTo(Score other) {
        return Integer.compare(this.movesCount, other.movesCount);
    }

    /**
     * Returns a string representation of the score.
     * The format is "movesCount level playerName".
     *
     * @return a string representation of the score
     */
    @Override
    public String toString() {
        return this.movesCount + " " + this.level + " " + this.playerName;
    }

    /**
     * Sets the number of moves made by the player.
     *
     * @param movesCount the number of moves to set
     */
    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }
}
