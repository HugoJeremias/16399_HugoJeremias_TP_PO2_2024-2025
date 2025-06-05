package pt.ipbeja.estig.po2.snowman.model;

public class Score {
    private String playerName;
    private int level;
    private int movesCount;
    private String dateTime;

    public Score(String playerName, int level, int movesCount) {
        this.playerName = playerName;
        this.level = level;
        this.movesCount = movesCount;
        this.dateTime = "";
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLevel() {
        return level;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public String getDateTime() {
        return dateTime;
    }
}
