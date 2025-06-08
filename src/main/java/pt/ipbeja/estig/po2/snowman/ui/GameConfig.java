// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.ui;

/**
 * Represents the configuration for the game, including the level, player name, and level style.
 */
public class GameConfig {
    private int level;
    private String playerName;
    private String levelStyle;

    /**
     * Constructs a GameConfig with the specified level, player name, and level style.
     *
     * @param level       the level of the game
     * @param playerName  the name of the player
     * @param levelStyle  the style of the level
     */
    public GameConfig(int level, String playerName, String levelStyle) {
        this.level = level;
        this.playerName = playerName;
        this.levelStyle = levelStyle;
    }
    /**
     * Returns the level of the game.
     *
     * @return the game level
     */
    public int getLevel() {
        return level;
    }
    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }
    /**
     * Returns the style of the level.
     *
     * @return the level style
     */
    public String getLevelStyle() {
        return levelStyle;
    }
}
