package pt.ipbeja.estig.po2.snowman.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Score {
    private String playerName;
    private int level;
    private int movesCount;
    private String dateTime;

    public Score(String playerName, int level, int movesCount) {
        this.playerName = playerName;
        this.level = level;
        this.movesCount = movesCount;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.dateTime = LocalDateTime.now().format(formatter);
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
        if (dateTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            this.dateTime = LocalDateTime.now().format(formatter);
        }
        return dateTime;
    }

    public int compareTo(Score other) {
        return Integer.compare(this.movesCount, other.movesCount);
    }

    @Override
    public String toString() {
        return this.playerName + " (" + this.level + "): " + this.movesCount + " movimentos";
    }

    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }
}
