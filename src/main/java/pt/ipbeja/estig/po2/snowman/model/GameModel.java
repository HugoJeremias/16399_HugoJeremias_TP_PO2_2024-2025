// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private final View view;
    private final BoardModel boardModel = new BoardModel();
    private int level = 1, movesCount = 0;
    private String filename = "Levels/level" + level + ".txt", movesString, scoresFilename = "Scores/Scores.txt";
    private Monster monster;
    private Score score;
    private List<Score> scores = new ArrayList<>();


    public GameModel(View view, int level, String playerName) {
        this.view = view;
        this.level = level;
        this.filename = "Levels/level" + level + ".txt";
        this.score = new Score(playerName, this.level, this.movesCount);
        try {
            this.boardModel.loadLevelFromFile(this.filename);
            this.LoadScoresFromFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void stackSnowballs(SnowBall base, SnowBall top) {
        if (base.getType() == SnowBallType.BIG && top.getType() == SnowBallType.AVERAGE) {
            base.setType(SnowBallType.BIG_AVERAGE);
            this.boardModel.getSnowballs().remove(top);
        } else if (base.getType() == SnowBallType.BIG && top.getType() == SnowBallType.SMALL) {
            base.setType(SnowBallType.BIG_SMAL);
            this.boardModel.getSnowballs().remove(top);
        } else if (base.getType() == SnowBallType.AVERAGE && top.getType() == SnowBallType.SMALL) {
            base.setType(SnowBallType.AVERAGE_SMALL);
            this.boardModel.getSnowballs().remove(top);
        }
        else if (base.getType() == SnowBallType.BIG_AVERAGE && top.getType() == SnowBallType.SMALL) {
            top.setPosition(base.getPosition());
            this.boardModel.checkForSnowman();
            this.boardModel.updateBoard();
            return;
        }
        this.boardModel.updateBoard();
    }


     public boolean moveMonster(Direction direction) throws IOException {
        this.monster = this.boardModel.getMonster();
        Position currentPos = this.monster.getPosition();
        Position newPos = currentPos.getPositionAt(direction);
        boolean moved = false;

        if (!this.boardModel.isValidPosition(newPos)) return false;

        PositionContent nextContent = this.boardModel.getPositionContent(newPos.getRow(), newPos.getCol());

        if (nextContent == PositionContent.BLOCK) return false;

        if (this.boardModel.isPositionEmpty(newPos)) {
            this.monster.setPosition(newPos);
            this.boardModel.updateBoard();
            moved = true;
        }
        SnowBall snowball = this.boardModel.getSnowballAt(newPos);
        if(snowball != null) moved = this.checkForSnowballs(snowball, newPos, direction);

        if (moved && this.isGameComplete()) {
            view.showGameCompleteMessage(true, this.boardModel.getSnowmen().get(0));
        }
        return moved;
    }


    public Boolean checkForSnowballs(SnowBall snowball, Position newPos, Direction direction) {
        if (this.isAverageSmallSplit(snowball, newPos, direction)) return false;
        if (this.isBigAverageSplit(snowball, newPos, direction)) return false;

        Position nextPos = newPos.getPositionAt(direction);
        if (!this.boardModel.isValidPosition(nextPos)) return false;
        if (this.boardModel.getPositionContent(nextPos.getRow(), nextPos.getCol()) == PositionContent.BLOCK) return false;

        SnowBall target = this.boardModel.getSnowballAt(nextPos);
        if (target != null) return this.handleStackOrBlock(snowball, target, newPos);

        if (this.boardModel.getPositionContent(nextPos.getRow(), nextPos.getCol()) == PositionContent.SNOW) snowball.grow();

        if (this.boardModel.isPositionEmpty(nextPos)) {
            this.moveSnowballAndMonster(snowball, nextPos, newPos);
            return true;
        }
        return true;
    }

    private boolean isAverageSmallSplit(SnowBall snowball, Position newPos, Direction direction) {
        if (snowball.getType() == SnowBallType.AVERAGE_SMALL) {
            Position topPos = newPos.getPositionAt(direction);
            if (this.boardModel.isValidPosition(topPos) && this.boardModel.isPositionEmpty(topPos)) {
                SnowBall small = new SnowBall(topPos, SnowBallType.SMALL);
                this.boardModel.getSnowballs().add(small);
                snowball.setType(SnowBallType.AVERAGE);
                this.boardModel.updateBoard();
            }
            return true;
        }
        return false;
    }

    private boolean isBigAverageSplit(SnowBall snowball, Position newPos, Direction direction) {
        if (snowball.getType() == SnowBallType.BIG_AVERAGE) {
            Position topPos = newPos.getPositionAt(direction);
            if (this.boardModel.isValidPosition(topPos) && this.boardModel.isPositionEmpty(topPos)) {
                SnowBall average = new SnowBall(topPos, SnowBallType.AVERAGE);
                this.boardModel.getSnowballs().add(average);
                snowball.setType(SnowBallType.BIG);
                this.boardModel.updateBoard();
            }
            return true;
        }
        return false;
    }

    private boolean handleStackOrBlock(SnowBall snowball, SnowBall target, Position newPos) {
        if (snowball.getType().isSmallerThan(target.getType()) ||
                (target.getType() == SnowBallType.BIG_AVERAGE && snowball.getType() == SnowBallType.SMALL)) {
            stackSnowballs(target, snowball);
            this.monster.setPosition(newPos);
            this.boardModel.updateBoard();
            this.boardModel.checkForSnowman();
            return true;
        }
        return false;
    }

    private void moveSnowballAndMonster(SnowBall snowball, Position nextPos, Position newPos) {
        snowball.setPosition(nextPos);
        this.monster.setPosition(newPos);
        this.boardModel.updateBoard();
        this.boardModel.checkForSnowman();
    }


    public PositionContent getPositionContent(int row, int col) {
        return this.boardModel.getPositionContent(row, col);
    }

    public Monster getMonster() {
        return this.boardModel.getMonster();
    }

    public List<SnowBall> getSnowballs() {
        return this.boardModel.getSnowballs();
    }

    public List<Snowman> getSnowmen() {
        return this.boardModel.getSnowmen();
    }

    public boolean isGameComplete() throws IOException {
        if (this.boardModel.getSnowballs().isEmpty() && !this.boardModel.getSnowmen().isEmpty()) {
            saveGameToFile(String.valueOf(this.score.getLevel()), this.movesString, this.getSnowmen().get(0).getPosition());
            return true;
        } else {
            return false;
        }
    }

    public boolean hasSnowman(Position position, double CELL_SIZE) {
        for (Snowman snowman : getSnowmen()) {
            Position base = snowman.getPosition();
            if (base.equals(position)) {
                return true;
            }
        }
        return false;
    }

    public void saveGameToFile(String levelFile, String moves, Position snowmanPos) throws IOException {
        String dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filename = "Games/snowman" + dateString + ".txt";
        try (PrintWriter out = new PrintWriter(filename)) {
            List<String> mapLines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get("Levels/level" + levelFile + ".txt"));
            out.println("Jogador:" + this.score.getPlayerName());
            out.println("Mapa:" + this.score.getLevel());
            for (String line : mapLines) {
                out.println(line);
            }
            out.println("Movimentos:" + moves);
            out.println("Total de movimentos: " + this.score.getMovesCount());
            out.println("Posição do boneco de neve: (" + (snowmanPos.getRow() + 1) + "," + (char)('A' + snowmanPos.getCol()) + ")");
        }
        this.saveScoreToFile();
    }

    private void saveScoreToFile() {
        try (PrintWriter out = new PrintWriter(new java.io.OutputStreamWriter(
                new java.io.FileOutputStream(this.scoresFilename, true), StandardCharsets.UTF_8))) {
            out.println(this.score.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String updateMovesValue(int lastRow, int lastCol, int newRow, int newCol) {
        String move = "(" + (lastRow + 1) + "," + (char)('A' + lastCol) + ") -> (" +
                (newRow + 1) + "," + (char)('A' + newCol) + ")";
        this.movesString += ", " + move;
        return move;
    }

    public void setMovesCount(int movesCount) {
        this.score.setMovesCount(movesCount);
    }

    private List<Score> LoadScoresFromFile() throws IOException {
        try {
            List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(this.scoresFilename));
            for(String line : lines) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    String playerName = parts[2].trim();
                    int level = Integer.parseInt(parts[1].trim());
                    int movesCount = Integer.parseInt(parts[0].trim());
                    Score score = new Score(playerName, level, movesCount);
                    if(level == this.level)
                        this.scores.add(score);
                }
            }
            return this.scores;
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public String getTopFiveScores() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pontuações:\n");
        this.scores.sort((s1, s2) -> s1.compareTo(s2));
        int count = 0;
        for (Score score : this.scores) {
            if (count >= 5) break;
            sb.append(score.toString()).append("\n");
            count++;
        }
        return sb.toString();
    }

}

