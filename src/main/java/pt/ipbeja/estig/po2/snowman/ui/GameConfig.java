package pt.ipbeja.estig.po2.snowman.ui;

public class GameConfig {
    private int level;
    private String playerName;
    private String levelStyle;

    public GameConfig(int level, String playerName, String levelStyle) {
        this.level = level;
        this.playerName = playerName;
        this.levelStyle = levelStyle;
    }
    public int getLevel() {
        return level;
    }
    public String getPlayerName() {
        return playerName;
    }
    public String getLevelStyle() {
        return levelStyle;
    }
}
